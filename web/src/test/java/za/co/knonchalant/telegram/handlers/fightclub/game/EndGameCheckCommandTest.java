package za.co.knonchalant.telegram.handlers.fightclub.game;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EndGameCheckCommandTest extends TestWithMocks {
    @Before
    public void setUp() {
        dao.clear();
        dao.persistFighter(testFighter1);
        dao.persistFighter(testFighter2);
        dao.persistFighter(testFighter3);
    }

    @Test
    public void testEndGameCheckDoesNothingIfMultiplePlayersAlive() {
        EndGameCheckCommand c = new EndGameCheckCommand(update, dao);

        CommandExecutor.execute(c, messageSender);
        assertEquals(100, testFighter2.getHealth(), 0.1);

    }
}