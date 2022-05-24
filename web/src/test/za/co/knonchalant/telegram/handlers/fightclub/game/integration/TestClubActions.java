package za.co.knonchalant.telegram.handlers.fightclub.game.integration;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import za.co.knonchalant.liketosee.dao.ClubDAO;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Club;

import javax.naming.NamingException;

public class TestClubActions extends IntegrationTestFramework {
    public static final long FAKE_CLUB_ID = -5L;

    @Before
    public void setUp() {
        Club ourFakeClub = new Club();
        ourFakeClub.setId(FAKE_CLUB_ID);
        ourFakeClub.setName("Totes not a fake club");
        ourFakeClub.setJoinCode("NOTAFAKE");
        ClubDAO.get().persistClub(ourFakeClub);
    }

    @Test
    public void registerFighterNewClub() {
        handleMessage(createMockUpdate(1, "/register"));
        assertResponse(1, "Signing up are we? Pick a class then.");

        // pick a class
        handleMessage(createMockUpdate(1, "Oyster"));
        Assert.assertFalse("pending response map is not empty", pendingResponseMap.isEmpty());
        Assert.assertTrue("pending response knows we picked Oyster", pendingResponseMap.get(1L).get(0).getDetails().contains("Oyster"));

        // pick a club
        handleMessage(createMockUpdate(1, "NOTAREALID"));
        assertResponse(1, "Righto, you're in a brand spankin' new club called");
        Assert.assertEquals(1, FighterDAO.get().getFighterByUserId(1).getClub().getFighters().size());
    }


    @Test
    public void registerFighterJoinClub() throws NamingException {
        handleMessage(createMockUpdate(1, "/register"));
        assertResponse(1, "Signing up are we? Pick a class then.");

        // pick a class
        handleMessage(createMockUpdate(1, "Oyster"));
        Assert.assertFalse("pending response map is not empty", pendingResponseMap.isEmpty());
        Assert.assertTrue("pending response knows we picked Oyster", pendingResponseMap.get(1L).get(0).getDetails().contains("Oyster"));

        // pick a club
        handleMessage(createMockUpdate(1, "NOTAFAKE"));
        assertResponse(1, "Righto, you've joined ");
        Assert.assertEquals(1, ClubDAO.get().getClub(FAKE_CLUB_ID).getFighters().size());
    }

    @Test
    public void joinCreatedClub() {
        registerFighterNewClub();
        String lastResponse = MOCK_BOT_API.getLastResponse(1);
        String code = lastResponse.substring(lastResponse.indexOf("joincode of ") + "joincode of ".length());

        handleMessage(createMockUpdate(2, "/register"));
        handleMessage(createMockUpdate(2, "Oyster"));
        handleMessage(createMockUpdate(2, code));
        assertResponse(2, "Righto, you've joined Super Amazeballs Club");
    }

    @Test
    public void renameClub() {
        registerFighterNewClub();

        handleMessage(createMockUpdate(1, "/rename"));
        assertResponse(1, "So fickle - what's the band- I mean, club, called ");
        handleMessage(createMockUpdate(1, "Super Duper Awesome Club"));
        assertResponse(1, "Super Duper Awesome Club");
    }

    @Test
    public void renameClubWithMoreThanOnePerson() {
        joinCreatedClub();

        handleMessage(createMockUpdate(1, "/rename"));
        assertResponse(1, "Look, you're never gonna agree on a new name.");
    }

    @Test
    public void registerFighterWrongClass() {
        handleMessage(createMockUpdate(1, "/register"));
        assertResponse(1, "Signing up are we? Pick a class then.");

        handleMessage(createMockUpdate(1, "Something else I dunno"));

        assertResponse(1, "Quit muckin' around");
    }
}
