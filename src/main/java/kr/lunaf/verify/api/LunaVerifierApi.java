package kr.lunaf.verify.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.util.Set;
import kr.lunaf.verify.TokenReplacer;

public interface LunaVerifierApi {
  void registerActionHandler(String type, LunaVerifierActionHandler handler);

  void unregisterActionHandler(String type);

  boolean hasActionHandler(String type);

  Set<String> getRegisteredActionTypes();

  void executeActions(JsonArray actions, TokenReplacer tokens, String eventType, LunaVerifierPayload payload);

  LunaVerifierPayload buildPayload(JsonObject payload);
}
