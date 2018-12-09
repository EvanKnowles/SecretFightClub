package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.util.StringPrettifier;

import java.text.NumberFormat;
import java.util.Comparator;
import java.util.List;

import static za.co.knonchalant.telegram.handlers.fightclub.RollHandler.getTotalProbability;
import static za.co.knonchalant.telegram.handlers.fightclub.RollHandler.swapProbability;

/**
 * Created by evan on 2016/04/08.
 */
public class ListItemsHandler extends ValidFighterMessageHandler {

    private static final String COMMAND = "listitems";
    private static long lastQueriedAt = 0;
    private static final Object sync = new Object();

    public ListItemsHandler(String botName, IBotAPI bot) {
        super(botName, COMMAND, bot, false);
    }

    @Override
    public String getDescription() {
        return "See a list of all items";
    }

    @Override
    public PendingResponse handle(IUpdate update, FighterDAO fighterDAO, Fighter fighter) {
        String keywords = getKeywords(update.getText(), COMMAND);
        if (keywords.startsWith("rm")) {
            String updateText = keywords.substring("rm ".length());
            int[] ids = extractItemIDs(updateText);
            int removedCount = 0;

            for (int id : ids) {
                Item item = new Item();
                item.setId(id);
                removedCount += fighterDAO.removeItem(id) ? 1 : 0;

                sendMessage(update, "Removed item " + id);
            }
            sendMessage(update, "Removed " + removedCount + " items (request was for '" + updateText + "')");
            return null;
        }

        synchronized (sync) {
            if ((System.currentTimeMillis() - lastQueriedAt) < 60000) {
                // Some very basic Ken-protection
                return null;
            }
            lastQueriedAt = System.currentTimeMillis();
        }

        List<Item> items = fighterDAO.getAllUncarriedItems();
        items.sort(Comparator.comparing(Item::getDamage).reversed().thenComparing(Item::getName));
        sendMessage(update, "*Behold the inventory!*");

        double total = getTotalProbability(items);

        StringBuilder b = new StringBuilder();
        NumberFormat percentageFormat = NumberFormat.getPercentInstance();
        percentageFormat.setMinimumFractionDigits(1);

        final int linesToBuffer = 5;
        int linesBuffered = 0;

        for (Item item : items) {
            b.append(" - ");
            b.append(item.getName());
            b.append(" (").append(StringPrettifier.itemIcon(item)).append((int)item.getDamage()).append(", ");
            double probabilityOfChoosing = swapProbability(item) / total;
            b.append(" ").append(percentageFormat.format(probabilityOfChoosing)).append(")");
            b.append(" [id=").append(item.getId()).append("]");
            b.append("\n");
            linesBuffered++;
            if (linesBuffered >= linesToBuffer) {
                sendMessage(update, b.toString());
                b = new StringBuilder();
                linesBuffered = 0;
            }
        }
        String lastMsg = b.toString();
        if (!lastMsg.isEmpty()) {
            sendMessage(update, lastMsg);
        }

        return null;
    }

    private int[] extractItemIDs(String idsList) {
        String[] toks = idsList.split(",");
        int[] ids = new int[toks.length];
        for (int i = 0; i < toks.length; i++) {
            ids[i] = Integer.parseInt(toks[i].trim());
        }
        return ids;
    }
}
