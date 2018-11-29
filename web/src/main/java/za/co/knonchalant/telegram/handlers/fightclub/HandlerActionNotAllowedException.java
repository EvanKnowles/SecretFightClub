package za.co.knonchalant.telegram.handlers.fightclub;

public class HandlerActionNotAllowedException extends Exception
{
  public HandlerActionNotAllowedException(String reason)
  {
    super(reason);
  }
}
