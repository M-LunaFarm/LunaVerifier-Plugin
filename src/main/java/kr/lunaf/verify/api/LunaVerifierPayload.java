package kr.lunaf.verify.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class LunaVerifierPayload {
  private final String eventId;
  private final String eventType;
  private final String mcIgn;
  private final String mcUuid;
  private final String discordUserId;
  private final String guildId;
  private final String channelId;
  private final String occurredAt;
  private final JsonObject rawPayload;

  private LunaVerifierPayload(
    String eventId,
    String eventType,
    String mcIgn,
    String mcUuid,
    String discordUserId,
    String guildId,
    String channelId,
    String occurredAt,
    JsonObject rawPayload
  ) {
    this.eventId = eventId;
    this.eventType = eventType;
    this.mcIgn = mcIgn;
    this.mcUuid = mcUuid;
    this.discordUserId = discordUserId;
    this.guildId = guildId;
    this.channelId = channelId;
    this.occurredAt = occurredAt;
    this.rawPayload = rawPayload;
  }

  public static LunaVerifierPayload fromJson(JsonObject payload) {
    if (payload == null) {
      return new LunaVerifierPayload(null, null, null, null, null, null, null, null, null);
    }
    return new LunaVerifierPayload(
      getString(payload, "event_id"),
      getString(payload, "event_type"),
      getString(payload, "mc_ign"),
      getString(payload, "mc_uuid"),
      getString(payload, "discord_user_id"),
      getString(payload, "guild_id"),
      getString(payload, "channel_id"),
      getString(payload, "occurred_at"),
      payload.deepCopy().getAsJsonObject()
    );
  }

  public String getEventId() {
    return eventId;
  }

  public String getEventType() {
    return eventType;
  }

  public String getMcIgn() {
    return mcIgn;
  }

  public String getMcUuid() {
    return mcUuid;
  }

  public String getDiscordUserId() {
    return discordUserId;
  }

  public String getGuildId() {
    return guildId;
  }

  public String getChannelId() {
    return channelId;
  }

  public String getOccurredAt() {
    return occurredAt;
  }

  public JsonObject getRawPayload() {
    if (rawPayload == null) {
      return new JsonObject();
    }
    return rawPayload.deepCopy().getAsJsonObject();
  }

  private static String getString(JsonObject obj, String key) {
    if (obj == null || key == null || !obj.has(key) || obj.get(key).isJsonNull()) {
      return null;
    }
    final JsonElement value = obj.get(key);
    try {
      return value.getAsString();
    } catch (Exception err) {
      return null;
    }
  }
}
