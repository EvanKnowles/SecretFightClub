package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;

public abstract class UseHealthAffectingItemCommand extends UseItemCommand {
    private final Fighter[] usedOn;

    public UseHealthAffectingItemCommand(String userByName, Item usedItem, Fighter[] usedOn) {
        super(userByName, usedItem);
        this.usedOn = usedOn;
    }

    @Override
    public void execute() {
        for (Fighter f : getUsedOn()) {
            f.damage(getUsedItem().getDamage());
        }
    }

    Fighter[] getUsedOn() {
        return usedOn;
    }
}
