package za.co.knonchalant.liketosee.util;

import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EDamageType;

import java.util.List;

public abstract class StringPrettifier {

    private static final String[] VOWELS = {"a", "e", "i", "o", "u"};

    private static final String SPLASH_ATTACK_ICON = "\uD83E\uDD91"; // Squid

    private static final String DAMAGE_ICON = "\uD83D\uDD2A"; // kitchen knife
    private static final String HEAVY_DAMAGE_ICON = "\uD83D\uDCA3"; // bomb
    private static final String MASSIVE_DAMAGE_ICON = "\u2622\ufe0f"; // radioactive

    private static final String HEALING_ICON = "\uD83D\uDC8A"; // pill
    private static final String HEAVY_HEALING_ICON = "\uD83C\uDF4C"; // banana
    private static final String MASSIVE_HEALING_ICON = "\uD83C\uDFE5"; // hospital
    private static final String ALL_ATTACK_ICON = "\uD83C\uDF2A"; // tornado

    public static final String SKULL = "\uD83D\uDC80"; // skull - player is dead
    public static final String SILENCE = "\uD83D\uDD15"; // muted bell - player is silenced

    private StringPrettifier() {
    }

    public static String prettify(String name) {
        String nameLower = name.toLowerCase();
        if (nameLower.startsWith("the ") || nameLower.startsWith("an ") || nameLower.startsWith("a ")) {
            // if the name includes a prefix of 'a' or 'an' or 'the', then use it as-is
            return name;
        }
        for (String vowel : VOWELS) {
            if (nameLower.startsWith(vowel)) {
                return "an " + name;
            }
        }
        return "a " + name;
    }

    public static String describePlayer(Fighter fighter, FighterDAO fighterDAO)
    {
        StringBuilder b = new StringBuilder();
        if (fighter.isDead()) {
            b.append(StringPrettifier.SKULL + " ");
        }
        b.append(fighter.getName());
        if (isSilenced(fighter, fighterDAO)) {
            b.append(" " + SILENCE);
        }
        return b.toString();
    }

    public static boolean isSilenced(Fighter attacker, FighterDAO fighterDAO)
    {
        List<Item> carrying = fighterDAO.getItemsCarriedBy(attacker.getId());
        return carrying.stream().anyMatch(i -> i.getDamageType() == EDamageType.SILENCE);
    }

    public static String itemIcon(Item item) {
        StringBuilder icon = new StringBuilder();

        EDamageType i = item.getDamageType();
        if (i == EDamageType.SPLASH_ATTACK) {
            icon.append(SPLASH_ATTACK_ICON);
        }
        if (i == EDamageType.ATTACK_ALL) {
            icon.append(ALL_ATTACK_ICON);
        }

        if (item.getDamage() < 0) {
            if (item.getDamage() < -60) {
                icon.append(MASSIVE_HEALING_ICON);
            } else if (item.getDamage() < -40) {
                icon.append(HEAVY_HEALING_ICON);
            } else {
                icon.append(HEALING_ICON);
            }
        } else if (item.getDamage() > 0) {
            if (item.getDamage() > 60) {
                icon.append(MASSIVE_DAMAGE_ICON);
            } else if (item.getDamage() > 40) {
                icon.append(HEAVY_DAMAGE_ICON);
            } else {
                icon.append(DAMAGE_ICON);
            }
        }
        return icon.toString();
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

    public static String listNames(Fighter[] fighters, FighterDAO fighterDAO)
    {
        StringBuilder b = new StringBuilder();
        boolean first = true;
        for (Fighter victim : fighters)
        {
            if (!first) {
                b.append(" and ");
            }
            first = false;
            b.append(StringPrettifier.describePlayer(victim, fighterDAO));
        }
        return b.toString();
    }
}
