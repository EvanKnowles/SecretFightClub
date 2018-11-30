package za.co.knonchalant.telegram.handlers.fightclub.business;

import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.util.StringPrettifier;

import java.util.ArrayList;
import java.util.List;

public class AttackHandler
{
  public static List<String> doAttack(FighterDAO fighterDAO, String attackerName, Item item, Fighter... victims)
  {
    List<String> messages = new ArrayList<>(2);

    for (Fighter victim : victims)
    {
      victim.damage(item.getDamage());
      fighterDAO.persistFighter(victim);
    }
    fighterDAO.remove(item);

    String victimNames = listNames(victims);
    if (item.getDamage() > 0) {
      if (item.getAttackText() == null) {
        messages.add(attackerName + " uses " + StringPrettifier.prettify(item.getName()) + " on " + victimNames);
      } else {
        messages.add(item.format(attackerName, victimNames));
      }
      String commentary;
      commentary = describe(item.getDamage(), victims);
      messages.add(attackerName + " damages " + victimNames + " for " + item.getDamage() + " points. " + commentary);
    } else {
      if (item.getAttackText() == null) {
        messages.add(attackerName + " uses " + StringPrettifier.prettify(item.getName()) + " and heals " + Math.abs(item.getDamage()) + " points on " + victimNames);
      } else {
        messages.add(item.format(attackerName, victimNames));
      }
    }
    return messages;
  }

  private static String listNames(Fighter[] victims)
  {
    StringBuilder b = new StringBuilder();
    boolean first = true;
    for (Fighter victim : victims)
    {
      if (!first) {
        b.append(" and ");
      }
      first = false;
      b.append(victim.getName());
    }
    return b.toString();
  }

  private static String describe(double damage, Fighter[] fighters) {
    if (fighters.length == 1) {
      return describe(damage, fighters[0].getHealth());
    }
    return "No favourites being picked here - everybody gets a beating!";
  }

  /**
   * Describe just how mean it was to take X points from someone who (now) has Y health.
   */
  private static String describe(double damage, double health) {
    double previousHealth = health + damage;

    double ratio = damage / previousHealth;

    if (ratio < 0.1) {
      return "That's a tiny bit not so nice.";
    }

    if (ratio < 0.2) {
      return "That'll make high-fives a little awkward going forward.";
    }

    if (ratio < 0.4) {
      return "Kinda mean.";
    }

    if (ratio < 0.8) {
      return "Inigo Montoya would have something cutting to say about damage like that.";
    }

    return "Wowser. Someone owes someone else a drink and possibly a hug.";
  }

}
