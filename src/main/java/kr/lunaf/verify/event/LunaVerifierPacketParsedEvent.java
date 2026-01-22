package kr.lunaf.verify.event;

import com.google.gson.JsonObject;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LunaVerifierPacketParsedEvent extends Event {
  private static final HandlerList HANDLERS = new HandlerList();
  private final JsonObject packet;

  public LunaVerifierPacketParsedEvent(boolean async, JsonObject packet) {
    super(async);
    this.packet = packet == null ? null : packet.deepCopy().getAsJsonObject();
  }

  public JsonObject getPacket() {
    if (packet == null) {
      return new JsonObject();
    }
    return packet.deepCopy().getAsJsonObject();
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }
}
