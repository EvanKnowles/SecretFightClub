package za.co.knonchalant.liketosee.util;

import za.co.knonchalant.liketosee.domain.fightclub.Item;

public abstract class StringPrettifier {

  private static final String[] VOWELS = {"a", "e", "i", "o", "u"};

  private static final String DAMAGE_ICON = "\uD83D\uDD2A"; // kitchen knife
  private static final String HEAVY_DAMAGE_ICON = "\uD83D\uDCA3"; // bomb
  private static final String MASSIVE_DAMAGE_ICON = "\u2622\ufe0f"; // radioactive

  private static final String HEALING_ICON = "\uD83D\uDC8A"; // pill
  private static final String HEAVY_HEALING_ICON = "\uD83C\uDF4C"; // banana
  private static final String MASSIVE_HEALING_ICON = "\uD83C\uDFE5"; // hospital

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
      if (item.getDamage() < -60) {
        return MASSIVE_HEALING_ICON;
      }
      if (item.getDamage() < -40) {
        return HEAVY_HEALING_ICON;
      }
      return HEALING_ICON;
    }

    if (item.getDamage() > 0) {
      if (item.getDamage() > 60) {
        return MASSIVE_DAMAGE_ICON;
      }
      if (item.getDamage() > 40) {
        return HEAVY_DAMAGE_ICON;
      }
      return DAMAGE_ICON;
    }
    return ""; // items that do nothing
  }
}
