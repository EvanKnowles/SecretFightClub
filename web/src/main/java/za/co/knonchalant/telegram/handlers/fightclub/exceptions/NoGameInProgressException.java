package za.co.knonchalant.telegram.handlers.fightclub.exceptions;

import za.co.knonchalant.liketosee.domain.fightclub.Fighter;

public class NoGameInProgressException extends HandlerActionNotAllowedException {
    public NoGameInProgressException(Fighter fighter) {
        super("The game is over, " + fighter.getName() + " - start a new game with /restart");
    }
}
