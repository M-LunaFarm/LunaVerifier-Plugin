package kr.lunaf.verify.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LunaVerifierPacketRejectedEvent extends Event {
  private static final HandlerList HANDLERS = new HandlerList();
  private final String reason;

  public LunaVerifierPacketRejectedEvent(boolean async, String reason) {
    super(async);
    this.reason = reason;
  }

  public String getReason() {
    return reason;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }
}
