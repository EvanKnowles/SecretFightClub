package za.co.knonchalant.telegram.handlers.fightclub.game;

import org.junit.Before;
import org.junit.Test;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;

import static org.junit.Assert.assertEquals;

public class UseItemCommandTest extends TestWithMocks {
    @Before
    public void setUp() {
        dao.clear();
        dao.persistFighter(testFighter1);
        dao.persistFighter(testFighter2);
        dao.persistFighter(testFighter3);
    }

    @Test
    public void testUseItemAffectsAllVictims() {
        Fighter[] useOn = new Fighter[] { testFighter1, testFighter2, testFighter3 };
        UseItemCommand c = new UseItemCommand(update, dao, testFighter1, attackItem, useOn);

        assertEquals(100.0, testFighter1.getHealth(), 0.1);
        assertEquals(100.0, testFighter3.getHealth(), 0.1);
        assertEquals(100.0, testFighter3.getHealth(), 0.1);

        CommandExecutor.execute(c, messageSender);

        assertEquals(90.0, testFighter1.getHealth(), 0.1);
        assertEquals(90.0, testFighter3.getHealth(), 0.1);
        assertEquals(90.0, testFighter3.getHealth(), 0.1);
    }

    @Test
    public void testUseItemAffectsOnlySpecifiedVictims() {
        Fighter[] useon = new Fighter[] { testFighter2 };
        UseItemCommand c = new UseItemCommand(update, dao, testFighter1, attackItem, useon);

        assertEquals(100.0, testFighter1.getHealth(), 0.1);
        assertEquals(100.0, testFighter2.getHealth(), 0.1);
        assertEquals(100.0, testFighter3.getHealth(), 0.1);

        CommandExecutor.execute(c, messageSender);

        assertEquals(100.0, testFighter1.getHealth(), 0.1);
        assertEquals(90.0, testFighter2.getHealth(), 0.1);
        assertEquals(100.0, testFighter3.getHealth(), 0.1);
    }

}