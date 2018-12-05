package za.co.knonchalant.telegram.handlers.fightclub.game.testmocks;

import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MockFighterDAO extends FighterDAO {
    private Set<Item> items = new HashSet<>();
    private Set<Fighter> fighters = new HashSet<>();

    @Override
    public void persistItem(Item item) {
        items.add(item);
    }

    @Override
    public void persistFighter(Fighter fighter) {
        fighters.add(fighter);
    }

    @Override
    public void remove(Item item) {
        items.remove(item);
    }

    @Override
    public List<Fighter> findAliveFightersInRoom(long chatId) {
        return fighters.stream().filter(f -> !f.isDead()).collect(Collectors.toList());
    }
}
