package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.candogram.handlers.User;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EDamageType;
import za.co.knonchalant.telegram.handlers.fightclub.game.testmocks.MockBotAPI;
import za.co.knonchalant.telegram.handlers.fightclub.game.testmocks.MockFighterDAO;
import za.co.knonchalant.telegram.scheduled.AwfulMockUpdate;

public class TestWithMocks {
    static MessageSender messageSender = MessageSender.forBot(new MockBotAPI());

    IUpdate update = createMockUpdate();
    FighterDAO dao = new MockFighterDAO();
    Item attackItem = createItem(10);
    Item healingItem = createItem(-10);

    Fighter testFighter1 = createFighter(100, "testFighter1");
    Fighter testFighter2 = createFighter(100, "testFighter2");
    Fighter testFighter3 = createFighter(100, "testFighter3");

    public TestWithMocks() {
        dao.persistFighter(testFighter1);
        dao.persistFighter(testFighter2);
        dao.persistFighter(testFighter3);
    }

    private Fighter createFighter(int health, String name) {
        Fighter f = new Fighter();
        f.setHealth(health);
        f.setName(name);
        return f;
    }

    Item createItem(int damage) {
        return new Item("test item", damage, EDamageType.ATTACK, "test attack");
    }

    private AwfulMockUpdate createMockUpdate() {
        User user = new User(-200L, "testuser", "Testy", "Testoffoles");
        return new AwfulMockUpdate(-100L, user);
    }
}
