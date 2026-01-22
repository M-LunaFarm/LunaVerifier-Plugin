package kr.lunaf.verify.event;

import kr.lunaf.verify.api.LunaVerifierPayload;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LunaVerifierDuplicateEvent extends Event {
  private static final HandlerList HANDLERS = new HandlerList();
  private final String eventId;
  private final LunaVerifierPayload payload;

  public LunaVerifierDuplicateEvent(boolean async, String eventId, LunaVerifierPayload payload) {
    super(async);
    this.eventId = eventId;
    this.payload = payload;
  }

  public String getEventId() {
    return eventId;
  }

  public LunaVerifierPayload getPayload() {
    return payload;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }
}
