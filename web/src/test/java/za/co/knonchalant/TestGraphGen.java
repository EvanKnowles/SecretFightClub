package za.co.knonchalant;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.Test;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import za.co.knonchalant.liketosee.domain.fightclub.Item;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TestGraphGen {

    @Test
    public void test() throws IOException {
        Gson gson = new Gson();
        Type listType = new TypeToken<ArrayList<Item>>() {
        }.getType();
        List<Item> items = gson.fromJson(new String(Files.readAllBytes(Paths.get("C:\\Users\\evanj\\Dropbox\\Projects\\SecretFightClub\\web\\testitems.json"))), listType);

        Map<Double, Long> count = new HashMap<>();

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

// Create Chart
        XYChart chart = QuickChart.getChart("Damage Frequencies", "amount of damage", "number of items", "occurences", damageData, occurenceData);

// Show it
        new SwingWrapper(chart).displayChart();

        new Scanner(System.in).next();
    }

}
