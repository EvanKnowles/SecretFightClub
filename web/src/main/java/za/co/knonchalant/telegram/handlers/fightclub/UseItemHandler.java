package za.co.knonchalant.telegram.handlers.fightclub;

import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IResponseMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.util.StringPrettifier;
import za.co.knonchalant.telegram.VerticalButtonBuilder;
import za.co.knonchalant.telegram.handlers.fightclub.details.ItemDetails;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.HandlerActionNotAllowedException;

import java.util.Arrays;
import java.util.List;

/**
 * Created by evan on 2016/04/08.
 */
public class UseItemHandler extends ActiveFighterMessageHandler implements IResponseMessageHandler<ItemDetails> {

  public UseItemHandler(String botName, IBotAPI bot) {
        super(botName, "use", bot, true);
    }

  static String makeItemButtonText(Item item) {
      return StringPrettifier.itemIcon(item) + " " + item.getName();
  }

  @Override
    public String getDescription() {
        return "Use one of your treasures.";
    }

    @Override
    public PendingResponse handle(IUpdate update, FighterDAO fighterDAO, Fighter fighter) throws HandlerActionNotAllowedException {
      List<Item> itemsCarriedBy = fighterDAO.getItemsCarriedBy(fighter.getId());

      if (itemsCarriedBy.isEmpty()) {
        throw new HandlerActionNotAllowedException("You're not carrying anything. You could roll for something.");
      }

      String[][] buttons = VerticalButtonBuilder.createVerticalButtons(getButtons(itemsCarriedBy));
      ReplyKeyboardMarkup inlineKeyboardMarkup = new ReplyKeyboardMarkup(buttons, true, true, true);
      getBot().sendMessage(update.getChatId(), "What do you want to use, " + StringPrettifier.describePlayer(fighter, fighterDAO) + "?", ParseMode.Markdown, false, (int) update.getMessageId(), inlineKeyboardMarkup);

      return new PendingResponse(update.getChatId(), update.getUser().getId(), "use", new ItemDetails());
    }

    private String[] getButtons(List<Item> itemsCarriedBy) {
        String[] array = new String[itemsCarriedBy.size()];
        for (int i = 0; i < itemsCarriedBy.size(); i++) {
            Item item = itemsCarriedBy.get(i);
            array[i] = StringPrettifier.itemIcon(item) + " " + item.getName();
        }
        return array;
    }

    @Override
    public List<IResponseHandler<ItemDetails>> getHandlers() {
        return Arrays.asList(new UseItemSelectionHandler(), new UseItemWrathHandler());
    }
}
