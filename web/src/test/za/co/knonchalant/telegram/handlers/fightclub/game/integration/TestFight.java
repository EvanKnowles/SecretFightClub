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

    @Before
    public void setUp() {
        Club ourFakeClub = new Club();
        ourFakeClub.setId(FAKE_CLUB_ID);
        ourFakeClub.setName("Totes not a fake club");
        ourFakeClub.setJoinCode("NOTAFAKE");
        ClubDAO.get().persistClub(ourFakeClub);

        Item item = new Item();
        item.setName("owwy");
        item.setDamage(10);
        item.setDamageType(EDamageType.ATTACK);

        FighterDAO.get().persistItem(item);
    }

    public void registerFighterJoinClub(int userId) {
        handleMessage(createMockUpdate(userId, "/register"));
        handleMessage(createMockUpdate(userId, "Oyster"));
        handleMessage(createMockUpdate(userId, "NOTAFAKE"));
    }

    @Test
    public void startFight() {
        registerFighterJoinClub(1);
        registerFighterJoinClub(2);

        Club notafake = ClubDAO.get().findClub("NOTAFAKE");
        Assert.assertEquals(2, notafake.getFighters().size());

        handleMessage(createMockUpdate(1, "/restart"));
        handleMessage(createMockUpdate(2, "/optin"));

        AwfulMockUpdate awfulMockUpdate = createMockUpdate(1);
        UseItemWrathHandler.restartGame(MOCK_BOT_API, FighterDAO.get(), notafake.getFighters(), awfulMockUpdate);

        handleMessage(createMockUpdate(1, "/roll"));
        handleMessage(createMockUpdate(2, "/roll"));

    }

}
