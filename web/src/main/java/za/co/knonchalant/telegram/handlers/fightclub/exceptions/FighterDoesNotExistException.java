package za.co.knonchalant.telegram.handlers.fightclub.exceptions;

public class FighterDoesNotExistException extends HandlerActionNotAllowedException {
  public FighterDoesNotExistException() {
    super("Uh, you don't exist. Go away, ghosty");
  }
}
