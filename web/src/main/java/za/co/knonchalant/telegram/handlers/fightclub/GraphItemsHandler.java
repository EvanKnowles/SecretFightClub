package za.co.knonchalant.telegram.handlers.fightclub;

import org.knowm.xchart.BitmapEncoder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.XYChart;
import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by evan on 2016/04/08.
 */
public class GraphItemsHandler extends ValidFighterMessageHandler {

    private static final String COMMAND = "graphdamage";
    private static long lastQueriedAt = 0;
    private static final Object sync = new Object();

    public GraphItemsHandler(String botName, IBotAPI bot) {
        super(botName, COMMAND, bot, true);
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

        List<Item> items = fighterDAO.getAllUncarriedItems();

        Map<Double, Long> count = getCount();

        for (Item item : items) {
            double damage = Math.floor(item.getDamage());
            if (damage < 0) {
                continue;
            }
            if (!count.containsKey(damage)) {
                count.put(damage, 1L);
            } else {
                Long aLong = count.get(damage);
                count.put(damage, aLong + 1);
            }
        }

        List<Double> damageData = new ArrayList<>(count.keySet());
        damageData.sort(Double::compareTo);

        List<Double> occurenceData = new ArrayList<>();

        for (Double damageDatum : damageData) {
            occurenceData.add(Double.valueOf(count.get(damageDatum)));
        }

        XYChart chart = QuickChart.getChart("Damage Frequencies", "amount of damage", "number of items", "occurences", damageData, occurenceData);
        try {
            byte[] bitmapBytes = BitmapEncoder.getBitmapBytes(chart, BitmapEncoder.BitmapFormat.PNG);
            sendMessage(update, "Item damage count graph:");
            getBot().sendPhoto(String.valueOf(update.getChatId()), bitmapBytes);
        } catch (IOException e) {
            sendMessage(update, "Failed to generate graph.");
            e.printStackTrace();
        }

        return null;
    }

    private HashMap<Double, Long> getCount() {
        HashMap<Double, Long> doubleLongHashMap = new HashMap<>();
        for (double i = 0; i <= 100.0; i++) {
            doubleLongHashMap.put(i, 0L);
        }
        return doubleLongHashMap;
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
