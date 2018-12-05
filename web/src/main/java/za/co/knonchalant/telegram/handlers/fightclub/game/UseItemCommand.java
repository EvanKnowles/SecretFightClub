package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;

public class UseItemCommand extends FightClubCommand {
    private final IUpdate update;
    private final FighterDAO fighterDAO;
    private final String useBy;
    private final Item item;
    private final Fighter[] useOn;

    public UseItemCommand(IUpdate update, FighterDAO fighterDAO, String useBy, Item item, Fighter... useOn) {
        this.update = update;
        this.fighterDAO = fighterDAO;
        this.useBy = useBy;
        this.item = item;
        this.useOn = useOn;
   }

    @Override
    void execute(MessageSender handler) {
        FightClubCommand c = new AttackCommand(update, fighterDAO, useBy, item, useOn);
        CommandExecutor.execute(c, handler);

        for (Fighter victim : useOn) {
            FightClubCommand d = new DeathCheckCommand(update, fighterDAO, victim, update.getUser().getFirstName());
            CommandExecutor.execute(d, handler);
        }
    }
}
