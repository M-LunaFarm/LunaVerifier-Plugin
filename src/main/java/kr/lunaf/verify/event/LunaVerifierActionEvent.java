package kr.lunaf.verify.event;

import com.google.gson.JsonObject;
import kr.lunaf.verify.TokenReplacer;
import kr.lunaf.verify.api.LunaVerifierPayload;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LunaVerifierActionEvent extends Event implements Cancellable {
  private static final HandlerList HANDLERS = new HandlerList();
  private final String actionType;
  private final JsonObject action;
  private final TokenReplacer tokens;
  private final String eventType;
  private final LunaVerifierPayload payload;
  private boolean cancelled;

  public LunaVerifierActionEvent(
    boolean async,
    String actionType,
    JsonObject action,
    TokenReplacer tokens,
    String eventType,
    LunaVerifierPayload payload
  ) {
    super(async);
    this.actionType = actionType;
    this.action = action == null ? null : action.deepCopy().getAsJsonObject();
    this.tokens = tokens;
    this.eventType = eventType;
    this.payload = payload;
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

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancel) {
    this.cancelled = cancel;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }
}
