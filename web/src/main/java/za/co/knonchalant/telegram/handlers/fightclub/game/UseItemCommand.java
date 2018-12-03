package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;

import java.util.ArrayList;
import java.util.List;

public abstract class UseItemCommand {
    private final Item usedItem;
    private final String usedByName;
    private final List<String> resultingMessages = new ArrayList<>();

    UseItemCommand(String usedByName, Item usedItem) {
        this.usedByName = usedByName;
        this.usedItem = usedItem;
    }

    /**
     * Implementing classes have an obligation to populate resultingMessages inside this method
     */
    public abstract void execute();

    Item getUsedItem() {
        return usedItem;
    }

    String getUsedByName() {
        return usedByName;
    }

    static String listNames(Fighter[] victims) {
        StringBuilder b = new StringBuilder();
        boolean first = true;
        for (Fighter victim : victims) {
            if (!first) {
                b.append(" and ");
            }
            first = false;
            b.append(victim.getName());
        }
        return b.toString();
    }

    public List<String> getResultingMessages() {
        return resultingMessages;
    }

    void addMessage(String message) {
        resultingMessages.add(message);
    }
}
