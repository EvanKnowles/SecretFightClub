package za.co.knonchalant.telegram.handlers.fightclub.game;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EndGameCheckCommandTest extends TestWithMocks {
    @Before
    public void setUp() {
        mockFighterDAO.clear();
        mockFighterDAO.persistFighter(testFighter1);
        mockFighterDAO.persistFighter(testFighter2);
        mockFighterDAO.persistFighter(testFighter3);
    }

    @Test
    public void testEndGameCheckDoesNothingIfMultiplePlayersAlive() {
        EndGameCheckCommand c = new EndGameCheckCommand(update, mockFighterDAO);

        CommandExecutor.execute(c, messageSender);
        assertEquals(100, testFighter2.getHealth(), 0.1);

    }
}