package kr.lunaf.verify.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LunaVerifierPacketReceivedEvent extends Event {
  private static final HandlerList HANDLERS = new HandlerList();
  private final String rawLine;

  public LunaVerifierPacketReceivedEvent(boolean async, String rawLine) {
    super(async);
    this.rawLine = rawLine;
  }

  public String getRawLine() {
    return rawLine;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }
}
