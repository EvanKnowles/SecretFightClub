package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IResponseMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.util.StringPrettifier;
import za.co.knonchalant.telegram.handlers.fightclub.details.ItemDetails;

import java.text.NumberFormat;
import java.util.*;

import static za.co.knonchalant.telegram.handlers.fightclub.RollHandler.getTotalProbability;
import static za.co.knonchalant.telegram.handlers.fightclub.RollHandler.swapProbability;

/**
 * Created by evan on 2016/04/08.
 */
public class RestartHandler extends FightclubMessageHandler implements IResponseMessageHandler<ItemDetails> {

    private static final Set<String> votesFor = new HashSet<>();

    public RestartHandler(String botName, IBotAPI bot) {
        super(botName, "restart", bot, true);
    }

    @Override
    public String getDescription() {
        return "Vote for a restart. Because it's stuck... or something";
    }

    @Override
    public PendingResponse handle(IUpdate update) {
        FighterDAO fighterDAO = FighterDAO.get();
        long userId = update.getUser().getId();
        Fighter fighter = fighterDAO.getFighter(userId);
        String fighterName = fighter.getName();

        int votesGiven;
        synchronized (votesFor) {
            votesFor.add(fighterName);
            votesGiven = votesFor.size();
        }

        List<Fighter> fightersInRoom = fighterDAO.findFightersInRoom(update.getChatId());
        int fighterCount = fightersInRoom.size();
        double requiredVotes = 0.5 * (double)fighterCount;
        int votesStillNeeded = (int) (Math.ceil(requiredVotes) - votesGiven);
        sendMessage(update, fighterName + " votes for a restart! Send /restart to agree\n" + votesStillNeeded + " more vote(s) needed");
        if (votesStillNeeded <= 0) {
            sendMessage(update, "Motion carried - we're restarting!");
            synchronized (votesFor) {
                votesFor.clear();
                for (Fighter f : fightersInRoom) {
                    f.damage(f.getHealth() + 1);
                    UseItemWrathHandler.checkForDeathAndConsequences(getBot(), update, fighterDAO, f, "Democracy");
                }
            }
        }
        return null;
    }

    @Override
    public List<IResponseHandler<ItemDetails>> getHandlers() {
        return Arrays.asList(new ItemDamageResponseHandler(), new ItemUsageResponseHandler());
    }
}
