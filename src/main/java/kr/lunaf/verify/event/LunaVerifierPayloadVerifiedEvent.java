package kr.lunaf.verify.event;

import kr.lunaf.verify.TokenReplacer;
import kr.lunaf.verify.api.LunaVerifierPayload;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LunaVerifierPayloadVerifiedEvent extends Event {
  private static final HandlerList HANDLERS = new HandlerList();
  private final LunaVerifierPayload payload;
  private final TokenReplacer tokens;

  public LunaVerifierPayloadVerifiedEvent(boolean async, LunaVerifierPayload payload, TokenReplacer tokens) {
    super(async);
    this.payload = payload;
    this.tokens = tokens;
  }

  public LunaVerifierPayload getPayload() {
    return payload;
  }

  public TokenReplacer getTokens() {
    return tokens;
  }

  @Override
  public HandlerList getHandlers() {
    return HANDLERS;
  }

  public static HandlerList getHandlerList() {
    return HANDLERS;
  }
}
