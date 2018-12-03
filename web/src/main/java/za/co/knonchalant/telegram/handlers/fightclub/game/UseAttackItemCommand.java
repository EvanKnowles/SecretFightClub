package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.util.StringPrettifier;

public class UseAttackItemCommand extends UseHealthAffectingItemCommand {
    public UseAttackItemCommand(String attackerName, Item attackWith, Fighter[] victims) {
        super(attackerName, attackWith, victims);
    }

    @Override
    public void execute() {
        super.execute();
        if (getUsedItem().getAttackText() == null) {
            addMessage(getUsedByName() + " uses " + StringPrettifier.prettify(getUsedItem().getName()) + " on " + listNames(getUsedOn()));
        } else {
            addMessage(getUsedItem().format(getUsedByName(), listNames(getUsedOn())));
        }

        String commentary = describe(getUsedItem().getDamage(), getUsedOn());
        addMessage(getUsedByName() + " damages " + listNames(getUsedOn()) + " for " + getUsedItem().getDamage() + " points. " + commentary);
    }

    private static String describe(double damage, Fighter[] fighters) {
        if (fighters.length == 1) {
            return describe(damage, fighters[0].getHealth());
        }
        return "No favourites being picked here - everybody gets a beating!";
    }

    /**
     * Describe just how mean it was to take X points from someone who (now) has Y health.
     */
    private static String describe(double damage, double health) {
        double previousHealth = health + damage;

        double ratio = damage / previousHealth;

        if (ratio < 0.1) {
            return "That's a tiny bit not so nice.";
        }

        if (ratio < 0.2) {
            return "That'll make high-fives a little awkward going forward.";
        }

        if (ratio < 0.4) {
            return "Kinda mean.";
        }

        if (ratio < 0.8) {
            return "Inigo Montoya would have something cutting to say about damage like that.";
        }

        return "Wowser. Someone owes someone else a drink and possibly a hug.";
    }
}
