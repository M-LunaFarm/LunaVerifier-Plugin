package kr.lunaf.verify.event;

import java.net.URI;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import kr.lunaf.verify.api.LunaVerifierPayload;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LunaVerifierHttpRequestEvent extends Event implements Cancellable {
  private static final HandlerList HANDLERS = new HandlerList();
  private final LunaVerifierPayload payload;
  private final URI uri;
  private final String method;
  private final String bodyText;
  private final Duration timeout;
  private final Map<String, String> headers;
  private boolean cancelled;

  public LunaVerifierHttpRequestEvent(
    boolean async,
    LunaVerifierPayload payload,
    URI uri,
    String method,
    String bodyText,
    Duration timeout,
    Map<String, String> headers
  ) {
    super(async);
    this.payload = payload;
    this.uri = uri;
    this.method = method;
    this.bodyText = bodyText;
    this.timeout = timeout;
    this.headers = headers == null ? Collections.emptyMap() : Collections.unmodifiableMap(headers);
  }

  public LunaVerifierPayload getPayload() {
    return payload;
  }

  public URI getUri() {
    return uri;
  }

  public String getMethod() {
    return method;
  }

  public String getBodyText() {
    return bodyText;
  }

  public Duration getTimeout() {
    return timeout;
  }

  public Map<String, String> getHeaders() {
    return headers;
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
