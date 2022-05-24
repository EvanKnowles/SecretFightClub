package za.co.knonchalant.telegram.handlers.fightclub.club;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.ParseMode;
import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IResponseMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EClasses;
import za.co.knonchalant.telegram.handlers.fightclub.FightClubMessageHandler;
import za.co.knonchalant.telegram.handlers.fightclub.details.RegisterDetails;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.AlreadyRegisteredException;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.HandlerActionNotAllowedException;

import java.util.Arrays;
import java.util.List;

/**
 * Created by evan on 2016/04/08.
 */
public class RegisterHandler extends FightClubMessageHandler implements IResponseMessageHandler<RegisterDetails> {
    public RegisterHandler(String botName, IBotAPI bot) {
        super(botName, "register", bot, true);
    }

    @Override
    public String getDescription() {
        return "Register yourself as a new fighter.";
    }

    @Override
    public void verifyFighter(FighterDAO fighterDAO, Fighter fighter, IUpdate chatId) throws HandlerActionNotAllowedException {
        if (fighter != null) {
            throw new AlreadyRegisteredException();
        }
    }

    @Override
    public PendingResponse handle(IUpdate update, FighterDAO fighterDAO, Fighter fighter) throws HandlerActionNotAllowedException {
        Fighter fightersFor = FighterDAO.get().getFighterByUserId(update.getUser().getId());

        if (fightersFor != null) {
            sendMessage(update, "You're already registered, chill.");
            return null;
        }

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup(getButtons());

        getBot().sendMessage(update.getChatId(), "Signing up are we? Pick a class then.", ParseMode.Markdown, false, (int) update.getMessageId(), inlineKeyboardMarkup);

        return new PendingResponse(update.getChatId(), update.getUser().getId(), "register", new RegisterDetails());
    }

    private InlineKeyboardButton[] getButtons() {
        EClasses[] values = EClasses.values();

        InlineKeyboardButton[] result = new InlineKeyboardButton[values.length];
        for (int i = 0; i < values.length; i++) {
            result[i] = new InlineKeyboardButton(values[i].getName()).callbackData(values[i].getName());
        }
        return result;
    }

    @Override
    public List<IResponseHandler<RegisterDetails>> getHandlers() {
        return Arrays.asList(new RegisterResponseHandler(), new PickClubResponseHandler());
    }
}
