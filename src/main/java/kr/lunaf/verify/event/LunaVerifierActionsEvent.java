package kr.lunaf.verify.event;

import com.google.gson.JsonArray;
import kr.lunaf.verify.api.LunaVerifierPayload;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LunaVerifierActionsEvent extends Event implements Cancellable {
  private static final HandlerList HANDLERS = new HandlerList();
  private final LunaVerifierPayload payload;
  private final JsonArray actions;
  private boolean cancelled;

  public LunaVerifierActionsEvent(boolean async, LunaVerifierPayload payload, JsonArray actions) {
    super(async);
    this.payload = payload;
    this.actions = actions == null ? null : actions.deepCopy().getAsJsonArray();
  }

  public LunaVerifierPayload getPayload() {
    return payload;
  }

  public JsonArray getActions() {
    if (actions == null) {
      return new JsonArray();
    }
    return actions.deepCopy().getAsJsonArray();
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
