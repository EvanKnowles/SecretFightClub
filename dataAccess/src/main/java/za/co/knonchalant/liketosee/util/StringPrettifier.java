package za.co.knonchalant.liketosee.util;

import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EDamageType;

public abstract class StringPrettifier {

  private static final String[] VOWELS = {"a", "e", "i", "o", "u"};

  private static final String SPLASH_ATTACK_ICON = "\uD83E\uDD91"; // Squid

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
      StringBuilder icon = new StringBuilder();
      EDamageType i = item.getDamageType();
      if (i == EDamageType.SPLASH_ATTACK) {
        icon.append(SPLASH_ATTACK_ICON);
      }

      if (item.getDamageType() == EDamageType.ATTACK) {
        if (item.getDamage() > 60) {
          icon.append(MASSIVE_DAMAGE_ICON);
        } else if (item.getDamage() > 40) {
          icon.append(HEAVY_DAMAGE_ICON);
        } else {
          icon.append(DAMAGE_ICON);
        }
      }

      if (item.getDamageType() == EDamageType.SPLASH_ATTACK) {
        icon.append(SPLASH_ATTACK_ICON);
      }
      return icon.toString();
    }
    return ""; // items that do nothing
  }

  public static String pluralize(long count, String word) {
    return pluralize(count, word, word + "s");
  }

  public static String pluralize(long count, String singularWord, String pluralWord) {
    if (count == 0) {
      return pluralWord; // 0 eggs
    }
    if (count == 1) {
      return singularWord; // 1 egg
    }
    return pluralWord; // 20 eggs
  }
}
