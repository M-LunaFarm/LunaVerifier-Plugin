package kr.lunaf.verify.api;

@FunctionalInterface
public interface LunaVerifierActionHandler {
  void handle(LunaVerifierActionContext context);
}
