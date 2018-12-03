package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;

import java.util.List;

public class AttackHandler
{
  public static List<String> doAttack(FighterDAO fighterDAO, String attackerName, Item item, Fighter... victims)
  {
    UseItemCommand handler;
    if (item.getDamage() > 0) {
      handler = new UseAttackItemCommand(attackerName, item, victims);
    } else {
      handler = new UseHealingItemCommand(attackerName, item, victims);
    }
    handler.execute();

    for (Fighter victim : victims)
    {
      fighterDAO.persistFighter(victim);
    }
    fighterDAO.remove(item);

    return handler.getResultingMessages();
  }
}
