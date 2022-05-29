package za.co.knonchalant.telegram.handlers.fightclub.game.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import za.co.knonchalant.liketosee.dao.ClubDAO;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Club;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EDamageType;
import za.co.knonchalant.telegram.handlers.fightclub.UseItemWrathHandler;
import za.co.knonchalant.telegram.scheduled.AwfulMockUpdate;

public class TestFight extends IntegrationTestFramework {
    public static final long FAKE_CLUB_ID = -5L;
    private static final String OWWIE_BUTTON = "\uD83D\uDD2A owwy";

    @Before
    public void setUp() {
        Club ourFakeClub = new Club();
        ourFakeClub.setId(FAKE_CLUB_ID);
        ourFakeClub.setName("Totes not a fake club");
        ourFakeClub.setJoinCode("NOTAFAKE");
        ClubDAO.get().persistClub(ourFakeClub);

        Item item = new Item();
        item.setId(0);
        item.setName("owwy");
        item.setDamage(10);
        item.setDamageType(EDamageType.ATTACK);

        FighterDAO.get().persistItem(item);
    }

    public void registerFighterJoinClub(int userId, String name) {
        handleMessage(createMockUpdate(userId, "/register", name));
        handleMessage(createMockUpdate(userId, "Oyster", name));
        handleMessage(createMockUpdate(userId, "NOTAFAKE", name));
    }

    @Test
    public void startFight() {
        registerFighterJoinClub(1, "test-one");
        registerFighterJoinClub(2, "test-two");

        Club notafake = ClubDAO.get().findClub("NOTAFAKE");
        Assert.assertEquals(2, notafake.getFighters().size());

        handleMessage(createMockUpdate(1, "/restart"));
        handleMessage(createMockUpdate(2, "/optin"));

        AwfulMockUpdate awfulMockUpdate = createMockUpdate(1);
        UseItemWrathHandler.restartGame(MOCK_BOT_API, FighterDAO.get(), notafake.getFighters());

        handleMessage(createMockUpdate(1, "/roll"));
        assertResponse(1, "an owwy!");
        handleMessage(createMockUpdate(2, "/roll"));

        handleMessage(createMockUpdate(1, "/use"));
        handleMessage(createMockUpdate(1, OWWIE_BUTTON));
        handleMessage(createMockUpdate(1, "test-two"));

        Assert.assertEquals(90, notafake.getFighters().get(1).getHealth(), 0.1);
        handleMessage(createMockUpdate(1, "/use"));
        assertResponse(1, "You're not carrying anything.");
    }

}
