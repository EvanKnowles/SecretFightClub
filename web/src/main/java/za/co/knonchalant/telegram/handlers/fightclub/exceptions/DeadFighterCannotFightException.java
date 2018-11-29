package za.co.knonchalant.telegram.handlers.fightclub.exceptions;

import za.co.knonchalant.liketosee.domain.fightclub.Fighter;

public class DeadFighterCannotFightException extends HandlerActionNotAllowedException {
  public DeadFighterCannotFightException(Fighter fighter) {
    super("Lie down, " + fighter.getName() + " - you're dead");
  }
}
