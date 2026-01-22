package kr.lunaf.verify.api;

import com.google.gson.JsonObject;
import kr.lunaf.verify.TokenReplacer;
import org.bukkit.plugin.java.JavaPlugin;

public final class LunaVerifierActionContext {
  private final JavaPlugin plugin;
  private final String actionType;
  private final JsonObject action;
  private final TokenReplacer tokens;
  private final String eventType;
  private final LunaVerifierPayload payload;
  private final boolean async;

  public LunaVerifierActionContext(
    JavaPlugin plugin,
    String actionType,
    JsonObject action,
    TokenReplacer tokens,
    String eventType,
    LunaVerifierPayload payload,
    boolean async
  ) {
    this.plugin = plugin;
    this.actionType = actionType;
    this.action = action == null ? null : action.deepCopy().getAsJsonObject();
    this.tokens = tokens;
    this.eventType = eventType;
    this.payload = payload;
    this.async = async;
  }

  public JavaPlugin getPlugin() {
    return plugin;
  }

  public String getActionType() {
    return actionType;
  }

  public JsonObject getAction() {
    if (action == null) {
      return new JsonObject();
    }
    return action.deepCopy().getAsJsonObject();
  }

  public TokenReplacer getTokens() {
    return tokens;
  }

  public String getEventType() {
    return eventType;
  }

  public LunaVerifierPayload getPayload() {
    return payload;
  }

  public boolean isAsync() {
    return async;
  }
}
