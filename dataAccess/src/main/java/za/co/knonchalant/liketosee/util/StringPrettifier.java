package za.co.knonchalant.liketosee.util;

import za.co.knonchalant.liketosee.domain.fightclub.Item;

public abstract class StringPrettifier {

  private static final String[] VOWELS = {"a", "e", "i", "o", "u"};

  private static final String HEALING_ICON = "\uD83D\uDC8A"; // pill
  private static final String DAMAGE_ICON = "\uD83D\uDCA3"; // bomb

  private StringPrettifier() {
  }

  public static String prettify(String name)
  {
    String nameLower = name.toLowerCase();
    if (nameLower.startsWith("the ") || nameLower.startsWith("an ") || nameLower.startsWith("a ")) {
      // if the name includes a prefix of 'a' or 'an' or 'the', then use it as-is
      return name;
    }
    for (String vowel : VOWELS)
    {
      if (nameLower.startsWith(vowel)) {
        return "an " + name;
      }
    }
    return "a " + name;
  }

  public static String itemIcon(Item item) {
    if (item.getDamage() < 0) {
      return HEALING_ICON; // heart icon - for healing items
    }
    if (item.getDamage() > 0)
    {
      return DAMAGE_ICON; // bomb icon - for items that do damage
    }
    return " "; // items that do nothing
  }
}
