package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;

public class DeathCheckCommand extends FightClubCommand {

    private final IUpdate update;
    private final Fighter victim;
    private final String damageCauser;

    public DeathCheckCommand(IUpdate update, Fighter victim, String damageCauser) {
        this.update = update;
        this.victim = victim;
        this.damageCauser = damageCauser;
    }

    @Override
    void execute(MessageSender handler) {
        if (victim.getHealth() <= 0) {
            if (damageCauser.equalsIgnoreCase(victim.getName())) {
                handler.sendMessage(update, "It's all too much for " + update.getUser().getFirstName() + "; goodbye, cruel world.");
            } else {
                handler.sendMessage(update, "Like OMG! " + damageCauser + " killed " + victim.getName() + "!");
            }
        }
    }


}
