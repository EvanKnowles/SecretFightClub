package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IResponseMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.util.StringPrettifier;
import za.co.knonchalant.telegram.handlers.fightclub.details.ItemDetails;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.HandlerActionNotAllowedException;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static za.co.knonchalant.telegram.handlers.fightclub.RollHandler.getTotalProbability;
import static za.co.knonchalant.telegram.handlers.fightclub.RollHandler.swapProbability;

/**
 * Created by evan on 2016/04/08.
 */
public class ListItemsHandler extends ValidFighterMessageHandler {

    private static long lastQueriedAt = 0;
    private static final Object sync = new Object();

    public ListItemsHandler(String botName, IBotAPI bot) {
        super(botName, "listitems", bot, true);
    }

    @Override
    public String getDescription() {
        return "See a list of all items";
    }

    @Override
    public PendingResponse handle(IUpdate update, FighterDAO fighterDAO, Fighter fighter) {
        synchronized (sync) {
            if ((System.currentTimeMillis() - lastQueriedAt) < 60000) {
                // Some very basic Ken-protection
                return null;
            }
            lastQueriedAt = System.currentTimeMillis();
        }

        List<Item> items = fighterDAO.findAllItems();
        items.sort(Comparator.comparing(Item::getDamage).reversed().thenComparing(Item::getName));

        if (update.getText() != null && update.getText().startsWith("rm ")) {
            String updateText = update.getText().substring("rm ".length());
            int[] ids = extractItemIDs(updateText);
            for (int id : ids) {
                Item item = new Item();
                item.setId(id);
                fighterDAO.remove(item);
                sendMessage(update, "Removed item " + id);
            }
            return null;
        }

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
            b.append(" (").append(StringPrettifier.itemIcon(item)).append(item.getDamage()).append(")");
            double probabilityOfChoosing = swapProbability(item) / total;
            b.append(" ").append(percentageFormat.format(probabilityOfChoosing));
            b.append("[").append(item.getId()).append("]");
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
