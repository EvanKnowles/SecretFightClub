package za.co.knonchalant.telegram.handlers.fightclub.game;

import org.junit.Test;
import za.co.knonchalant.liketosee.domain.fightclub.Item;

import static org.junit.Assert.*;

public class AttackItemCommandTest extends TestWithMocks {

    @Test
    public void testAttackItemDecreasesHealth() {
        UseItemCommand c = new UseItemCommand(update, dao, testFighter1.getName(), attackItem, testFighter2);

        assertEquals(100.0, testFighter2.getHealth(), 0.1);
        CommandExecutor.execute(c, messageSender);
        assertEquals(90, testFighter2.getHealth(), 0.1);
    }

    @Test
    public void testHealingItemIncreaasesHealth() {
        UseItemCommand c = new UseItemCommand(update, dao, testFighter1.getName(), healingItem, testFighter2);

        assertEquals(100, testFighter2.getHealth(), 0.1);
        CommandExecutor.execute(c, messageSender);
        assertEquals(110, testFighter2.getHealth(), 0.1);
    }

    @Test
    public void testHealingItemCapsAt150() {
        Item enormousHealingItem = createItem(-9999);
        UseItemCommand c = new UseItemCommand(update, dao, testFighter1.getName(), enormousHealingItem, testFighter2);

        assertEquals(100, testFighter2.getHealth(), 0.1);
        CommandExecutor.execute(c, messageSender);
        assertEquals(150, testFighter2.getHealth(), 0.1); // caps health at 150
    }


    @Test
    public void testDamageCallsCheckDeadCommand() {
        Item enormousDamageItem = createItem(9999);
        UseItemCommand c = new UseItemCommand(update, dao, testFighter1.getName(), enormousDamageItem, testFighter2);

        assertFalse(testFighter2.isDead());
        CommandExecutor.execute(c, messageSender);
        assertTrue(testFighter2.isDead());
    }

}