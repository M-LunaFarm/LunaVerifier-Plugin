package kr.lunaf.verify;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class LunaVotifierPlugin extends JavaPlugin {
  private static final String HMAC_ALGO = "HmacSHA256";
  private static final String CACHE_FILE = "processed-events.json";

  private final Gson gson = new Gson();
  private TcpServer tcpServer;
  private EventDeduplicator deduplicator;
  private ActionExecutor actionExecutor;
  private String serverSecret;
  private boolean requireSignature;
  private long timestampSkewSeconds;

  @Override
  public void onEnable() {
    saveDefaultConfig();
    reloadConfig();

    final int port = getConfig().getInt("listen-port", 8192);
    serverSecret = getConfig().getString("server-secret", "").trim();
    requireSignature = getConfig().getBoolean("require-signature", true);
    timestampSkewSeconds = getConfig().getLong("timestamp-skew-seconds", 300L);

    final long ttlSeconds = getConfig().getLong("idempotency-ttl-seconds", 86400L);
    deduplicator = new EventDeduplicator(new File(getDataFolder(), CACHE_FILE), ttlSeconds);
    deduplicator.load();

    actionExecutor = new ActionExecutor(this, getConfig());

    if (requireSignature && serverSecret.isEmpty()) {
      getLogger().warning("server-secret is empty. Signature verification will fail.");
    }

    tcpServer = new TcpServer(this, port);
    try {
      tcpServer.start();
      getLogger().info("Listening for LunaVotifier events on port " + port);
    } catch (Exception err) {
      getLogger().severe("Failed to start TCP listener: " + err.getMessage());
    }
  }

  @Override
  public void onDisable() {
    if (tcpServer != null) {
      tcpServer.close();
      tcpServer = null;
    }
    if (deduplicator != null) {
      deduplicator.save();
    }
    if (actionExecutor != null) {
      actionExecutor.shutdown();
    }
  }

  public String handlePacket(String rawLine) {
    if (rawLine == null || rawLine.trim().isEmpty()) {
      return "error: empty";
    }

    final JsonObject packet;
    try {
      packet = JsonParser.parseString(rawLine).getAsJsonObject();
    } catch (Exception err) {
      return "error: invalid_json";
    }

    final String timestamp = getString(packet, "timestamp");
    final String nonce = getString(packet, "nonce");
    final String signature = getString(packet, "signature");
    final JsonElement payloadElement = packet.get("payload");

    if (timestamp == null || nonce == null || payloadElement == null) {
      return "error: missing_fields";
    }

    if (requireSignature) {
      final String expected = computeSignature(timestamp, nonce, payloadElement);
      if (!secureEquals(expected, signature)) {
        return "error: bad_signature";
      }
    }

    if (timestampSkewSeconds > 0) {
      final long now = Instant.now().getEpochSecond();
      final long ts;
      try {
        ts = Long.parseLong(timestamp);
      } catch (NumberFormatException err) {
        return "error: bad_timestamp";
      }
      if (Math.abs(now - ts) > timestampSkewSeconds) {
        return "error: stale";
      }
    }

    if (!payloadElement.isJsonObject()) {
      return "error: invalid_payload";
    }

    final JsonObject payload = payloadElement.getAsJsonObject();
    final String eventId = getString(payload, "event_id");
    if (eventId == null || eventId.isEmpty()) {
      return "error: missing_event_id";
    }

    if (!deduplicator.markIfNew(eventId)) {
      return "ok";
    }

    final String eventType = getString(payload, "event_type");
    final TokenReplacer tokens = buildTokens(payload);

    JsonArray actions = new JsonArray();
    if (payload.has("actions") && payload.get("actions").isJsonArray()) {
      actions = payload.getAsJsonArray("actions");
    }

    actionExecutor.execute(actions, tokens, eventType == null ? "unknown" : eventType);

    return "ok";
  }

  private String computeSignature(String timestamp, String nonce, JsonElement payloadElement) {
    final String body = "{\"timestamp\":" + gson.toJson(timestamp)
      + ",\"nonce\":" + gson.toJson(nonce)
      + ",\"payload\":" + payloadElement.toString()
      + "}";
    return hmacSha256Hex(serverSecret, body);
  }

  private TokenReplacer buildTokens(JsonObject payload) {
    final Map<String, String> tokens = new HashMap<>();
    tokens.put("player", getString(payload, "mc_ign"));
    tokens.put("ign", getString(payload, "mc_ign"));
    tokens.put("uuid", getString(payload, "mc_uuid"));
    tokens.put("discord_id", getString(payload, "discord_user_id"));
    tokens.put("guild_id", getString(payload, "guild_id"));
    tokens.put("channel_id", getString(payload, "channel_id"));
    tokens.put("event_type", getString(payload, "event_type"));
    tokens.put("event_id", getString(payload, "event_id"));
    tokens.put("occurred_at", getString(payload, "occurred_at"));
    return new TokenReplacer(tokens);
  }

  private static String getString(JsonObject obj, String key) {
    if (obj == null || key == null || !obj.has(key) || obj.get(key).isJsonNull()) {
      return null;
    }
    try {
      return obj.get(key).getAsString();
    } catch (Exception err) {
      return null;
    }
  }

  private static String hmacSha256Hex(String secret, String input) {
    if (secret == null) {
      return "";
    }
    try {
      final Mac mac = Mac.getInstance(HMAC_ALGO);
      mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), HMAC_ALGO));
      final byte[] digest = mac.doFinal(input.getBytes(StandardCharsets.UTF_8));
      return bytesToHex(digest);
    } catch (Exception err) {
      return "";
    }
  }

  private static String bytesToHex(byte[] data) {
    final StringBuilder sb = new StringBuilder(data.length * 2);
    for (byte b : data) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  private static boolean secureEquals(String expected, String actual) {
    if (expected == null || actual == null) {
      return false;
    }
    if (expected.length() != actual.length()) {
      return false;
    }
    final byte[] a = expected.getBytes(StandardCharsets.UTF_8);
    final byte[] b = actual.getBytes(StandardCharsets.UTF_8);
    return MessageDigest.isEqual(a, b);
  }

  public void runOnMainThread(Runnable task) {
    if (Bukkit.isPrimaryThread()) {
      task.run();
      return;
    }
    Bukkit.getScheduler().runTask(this, task);
  }
}
