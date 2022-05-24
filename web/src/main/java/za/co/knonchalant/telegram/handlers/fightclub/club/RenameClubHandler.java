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
import za.co.knonchalant.telegram.handlers.fightclub.details.RenameDetails;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.AlreadyRegisteredException;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.FighterDoesNotExistException;
import za.co.knonchalant.telegram.handlers.fightclub.exceptions.HandlerActionNotAllowedException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Rename the current club, if you're the only one in it.
 */
public class RenameClubHandler extends FightClubMessageHandler implements IResponseMessageHandler<RenameDetails> {
    public RenameClubHandler(String botName, IBotAPI bot) {
        super(botName, "rename", bot, true);
    }

    @Override
    public String getDescription() {
        return "Rename your club.";
    }

    @Override
    public void verifyFighter(FighterDAO fighterDAO, Fighter fighter, IUpdate chatId) throws HandlerActionNotAllowedException {
        if (fighter == null) {
            throw new FighterDoesNotExistException();
        }
    }

    @Override
    public PendingResponse handle(IUpdate update, FighterDAO fighterDAO, Fighter fighter) throws HandlerActionNotAllowedException {

        List<Fighter> fighters = fighter.getClub().getFighters();
        if (fighters.size() != 1) {
            sendMessage(update, "Look, you're never gonna agree on a new name. Only clubs with one fighter can change the name. You'll just... fight otherwise");
            return null;
        }

        sendMessage(update, "So fickle - what's the band- I mean, club, called now then?");

        return new PendingResponse(update.getChatId(), update.getUser().getId(), "rename", new RenameDetails());
    }

    @Override
    public List<IResponseHandler<RenameDetails>> getHandlers() {
        return Collections.singletonList(new RenameResponseHandler());
    }
}
