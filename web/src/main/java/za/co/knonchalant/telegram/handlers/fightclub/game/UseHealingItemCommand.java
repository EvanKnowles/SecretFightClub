package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.util.StringPrettifier;

public class UseHealingItemCommand extends UseHealthAffectingItemCommand {
    public UseHealingItemCommand(String attackerName, Item item, Fighter[] fightersToHeal) {
        super(attackerName, item, fightersToHeal);
    }

    @Override
    public void execute() {
        super.execute();
        if (getUsedItem().getAttackText() == null) {
            addMessage(getUsedByName() + " uses " + StringPrettifier.prettify(getUsedItem().getName()) + " and heals " + Math.abs(getUsedItem().getDamage()) + " points on " + listNames(getUsedOn()));
        } else {
            addMessage(getUsedItem().format(getUsedByName(), listNames(getUsedOn())));
        }
    }
}
