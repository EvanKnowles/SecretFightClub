package za.co.knonchalant.telegram.handlers.fightclub.exceptions;

public class HandlerActionNotAllowedException extends Exception {
  public HandlerActionNotAllowedException(String reason) {
    super(reason);
  }
}
