package za.co.knonchalant.telegram.handlers.fightclub.game.testmocks;

import za.co.knonchalant.liketosee.dao.ClubDAO;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;

import javax.persistence.TypedQuery;
import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MockFighterDAO extends FighterDAO {
    private final Set<Item> items = new HashSet<>();
    private final Set<Fighter> fighters = new HashSet<>();

    @Override
    public void persistItem(Item item) {
        items.add(item);
    }

    @Override
    public Fighter getFighterByUserId(long userId) {
        return fighters.stream().filter(f -> f.getUserId() == userId).findFirst().orElse(null);
    }

    @Override
    public void give(Item item, Fighter fighter) {
        item = (Item) deepCopy(item);
        item.setId(null);
        item.setFighterId(fighter.getId());
        persistItem(item);
    }

    static public Object deepCopy(Object oldObj) {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            ByteArrayOutputStream bos =
                    new ByteArrayOutputStream(); // A
            oos = new ObjectOutputStream(bos); // B
            // serialize and pass the object
            oos.writeObject(oldObj);   // C
            oos.flush();               // D
            ByteArrayInputStream bin =
                    new ByteArrayInputStream(bos.toByteArray()); // E
            ois = new ObjectInputStream(bin);                  // F
            // return the new object
            return ois.readObject(); // G
        } catch (Exception e) {
            System.out.println("Exception in ObjectCloner = " + e);
        } finally {
            try {
                oos.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                ois.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    @Override
    public List<Item> getItemsCarriedBy(Long fighterId) {
        return items.stream()
                .filter(i -> i.getFighterId() != null && i.getFighterId().equals(fighterId))
                .collect(Collectors.toList());
    }

    @Override
    public void persistFighter(Fighter fighter) {
        fighters.add(fighter);
        if (fighter.getClub() != null) {
            ClubDAO.get().persistClub(fighter.getClub());
        }
    }

    @Override
    public void remove(Item item) {
        items.remove(item);
    }

    @Override
    public List<Item> getAllUncarriedItemsFor(long chatId) {
        return items.stream()
                .filter(f -> f.getFighterId() == null && (f.getChatId() == null || f.getChatId() == chatId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Fighter> findAliveFightersInRoom(long chatId) {
        return fighters.stream().filter(f -> !f.isDead()).collect(Collectors.toList());
    }

    public void clear() {
        fighters.clear();
        items.clear();
    }
}
