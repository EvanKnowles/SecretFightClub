package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.util.StringPrettifier;

import static za.co.knonchalant.liketosee.util.StringPrettifier.listNames;

class GiveMuteItemCommand extends FightClubCommand
{
  private final IUpdate update;
  private final FighterDAO fighterDAO;
  private final Fighter attacker;
  private final Item item;
  private final Fighter[] victims;

  GiveMuteItemCommand(IUpdate update, FighterDAO fighterDAO, Fighter attacker, Item item, Fighter[] victims) {
    this.update = update;
    this.fighterDAO = fighterDAO;
    this.attacker = attacker;
    this.item = item;
    this.victims = victims;
  }

  @Override
  void execute(MessageSender handler) {
    for (Fighter victim : victims) {
      fighterDAO.give(item, victim);
    }
    fighterDAO.remove(item);

    sendMuteMessage(attacker.getName(), item, victims, handler, update);
  }

  private void sendMuteMessage(String attackerName, Item item, Fighter[] victims, MessageSender handler, IUpdate update) {
    String victimNames = listNames(victims, fighterDAO);

    String useMsg;
    if (item.getAttackText() == null) {
      useMsg = attackerName + " gives " + StringPrettifier.prettify(item.getName()) + " to " + victimNames;
    } else {
      useMsg = item.format(attackerName, victimNames);
    }
    handler.sendMessage(update, useMsg);
  }

}
