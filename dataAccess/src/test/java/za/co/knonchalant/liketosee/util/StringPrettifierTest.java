package za.co.knonchalant.liketosee.util;

import org.junit.Test;
import za.co.knonchalant.liketosee.domain.fightclub.Item;

import static org.junit.Assert.*;

public class StringPrettifierTest
{
  @Test
  public void testNames() {
    assertEquals("the box", StringPrettifier.prettify("the box"));
    assertEquals("the Box", StringPrettifier.prettify("the Box"));
    assertEquals("a box", StringPrettifier.prettify("a box"));
    assertEquals("a Box", StringPrettifier.prettify("a Box"));
    assertEquals("an item", StringPrettifier.prettify("an item"));
    assertEquals("an Item", StringPrettifier.prettify("an Item"));
    assertEquals("an object", StringPrettifier.prettify("object"));
    assertEquals("an Object", StringPrettifier.prettify("Object"));
    assertEquals("a thing", StringPrettifier.prettify("thing"));
    assertEquals("a Thing", StringPrettifier.prettify("Thing"));
  }

  @Test
  public void testIcons() {
    Item weapon = new Item();
    weapon.setDamage(10);
    Item healing = new Item();
    healing.setDamage(-10);
    String damagingIcon = StringPrettifier.itemIcon(weapon);
    String healingIcon = StringPrettifier.itemIcon(healing);
    assertNotEquals(healingIcon, damagingIcon);
  }

}
