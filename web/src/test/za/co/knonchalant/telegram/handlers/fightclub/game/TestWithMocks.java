package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.candogram.handlers.User;
import za.co.knonchalant.liketosee.domain.fightclub.Club;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EDamageType;
import za.co.knonchalant.telegram.handlers.fightclub.game.testmocks.MockBotAPI;
import za.co.knonchalant.telegram.handlers.fightclub.game.testmocks.MockClubDAO;
import za.co.knonchalant.telegram.handlers.fightclub.game.testmocks.MockFighterDAO;
import za.co.knonchalant.telegram.scheduled.AwfulMockUpdate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestWithMocks {
    protected static final MockBotAPI MOCK_BOT_API = new MockBotAPI();
    protected static MessageSender messageSender = MessageSender.forBot(MOCK_BOT_API);

    protected Map<Long, List<PendingResponse>> pendingResponseMap = new HashMap<>();

    protected MockFighterDAO mockFighterDAO = new MockFighterDAO();
    protected MockClubDAO mockClubDAO = new MockClubDAO();

    protected IUpdate update = createMockUpdate(null);


    protected Item attackItem = createItem(10);
    protected Item healingItem = createItem(-10);

    protected Club club = new Club();

    protected Fighter testFighter1 = createFighter(100, "testFighter1", -100);
    protected Fighter testFighter2 = createFighter(100, "testFighter2", -200);
    protected Fighter testFighter3 = createFighter(100, "testFighter3", -300);

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

    protected AwfulMockUpdate createMockUpdate(int userId) {
        User user = new User(userId, "testuser", "Testy", "Testoffoles");
        return new AwfulMockUpdate(userId, user, null);
    }

    protected AwfulMockUpdate createMockUpdate(int userId, String text) {
        User user = new User(userId, "testuser", "Testy", "Testoffoles");
        return new AwfulMockUpdate(userId, user, null, text);
    }

    protected AwfulMockUpdate createMockUpdate(int userId, String text, String username) {
        User user = new User(userId, username, username, "Mc" + username);
        return new AwfulMockUpdate(userId, user, null, text);
    }
}
