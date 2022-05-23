package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.candogram.handlers.User;
import za.co.knonchalant.liketosee.domain.fightclub.Club;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EDamageType;
import za.co.knonchalant.telegram.handlers.fightclub.game.testmocks.MockBotAPI;
import za.co.knonchalant.telegram.handlers.fightclub.game.testmocks.MockFighterDAO;
import za.co.knonchalant.telegram.scheduled.AwfulMockUpdate;

public class TestWithMocks {
    static MessageSender messageSender = MessageSender.forBot(new MockBotAPI());

    IUpdate update = createMockUpdate(null);
    MockFighterDAO dao = new MockFighterDAO();
    Item attackItem = createItem(10);
    Item healingItem = createItem(-10);

    Club club = new Club();

    Fighter testFighter1 = createFighter(100, "testFighter1", -100);
    Fighter testFighter2 = createFighter(100, "testFighter2", -200);
    Fighter testFighter3 = createFighter(100, "testFighter3", -300);

    private Fighter createFighter(int health, String name, int userId) {
        Fighter f = new Fighter();
        f.setClub(club);
        club.getFighters().add(f);
        f.setUserId(userId);
        f.setHealth(health);
        f.setName(name);
        return f;
    }

    Item createItem(int damage) {
        return new Item("test item", damage, EDamageType.ATTACK, "test attack", -1);
    }

    protected AwfulMockUpdate createMockUpdate(String title) {
        User user = new User(-200L, "testuser", "Testy", "Testoffoles");
        return new AwfulMockUpdate(-100L, user, title);
    }
}
