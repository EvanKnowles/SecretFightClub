package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EDamageType;
import za.co.knonchalant.liketosee.util.StringPrettifier;

import java.util.List;

import static za.co.knonchalant.liketosee.util.StringPrettifier.*;

class AttackCommand extends FightClubCommand
{
  private final IUpdate update;
  private final FighterDAO fighterDAO;
  private final Fighter attacker;
  private final Item item;
  private final Fighter[] victims;

  AttackCommand(IUpdate update, FighterDAO fighterDAO, Fighter attacker, Item item, Fighter[] victims) {
    this.update = update;
    this.fighterDAO = fighterDAO;
    this.attacker = attacker;
    this.item = item;
    this.victims = victims;
  }

  @Override
  void execute(MessageSender handler) {
    // First, check if attacker is muted:
    if (isSilenced(attacker, fighterDAO)) {
      handler.sendMessage(update, attacker + ", you've been silenced!");
      return;
    }

    // Inflict damage:
    for (Fighter victim : victims)
    {
      victim.damage(item.getDamage());
      fighterDAO.persistFighter(victim);
    }
    fighterDAO.remove(item);

    if (item.getDamage() > 0) {
      sendAttackMessages(describePlayer(attacker, fighterDAO), item, victims, handler, update);
    } else {
      sendHealMessages(describePlayer(attacker, fighterDAO), handler, victims);
    }
  }

  private void sendHealMessages(String attackerName, MessageSender handler, Fighter[] victims)
  {
    String victimNames = listNames(victims, fighterDAO);

    if (item.getAttackText() == null) {
      handler.sendMessage(update, attackerName + " uses " + StringPrettifier.prettify(item.getName()) + " and heals " + Math.abs(item.getDamage()) + " points on " + victimNames);
    } else {
      handler.sendMessage(update, item.format(attackerName, victimNames));
    }
  }

  private void sendAttackMessages(String attackerName, Item item, Fighter[] victims, MessageSender handler, IUpdate update) {
    String victimNames = listNames(victims, fighterDAO);

    String useMsg;
    if (item.getAttackText() == null) {
      useMsg = attackerName + " uses " + StringPrettifier.prettify(item.getName()) + " on " + victimNames;
    } else {
      useMsg = item.format(attackerName, victimNames);
    }
    String commentary = describe(item.getDamage(), victims);
    handler.sendMessage(update, useMsg + "\n" + attackerName + " damages " + victimNames + " for " + item.getDamage() + " points. " + commentary);
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
