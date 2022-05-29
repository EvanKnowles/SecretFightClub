package za.co.knonchalant.telegram.handlers.fightclub.exceptions;

public class AlreadyRegisteredException extends HandlerActionNotAllowedException {
    public AlreadyRegisteredException() {
        super("You're already registered, chill.");
    }
}
