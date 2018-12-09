package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.telegram.handlers.fightclub.details.ItemDetails;

import java.util.List;

public class UseItemWrathHandler2 extends UseItemWrathHandler implements IResponseHandler<ItemDetails> {

    @Override
    protected Fighter getFighter(IUpdate update, FighterDAO fighterDAO) {
        List<Fighter> fightersInRoom = fighterDAO.findFightersInRoom(update.getChatId());
        String fightName = update.getText();
        return fightersInRoom.stream()
                .filter(f -> f.getName().equalsIgnoreCase(fightName))
                .findFirst().orElse(null);
    }

    @Override
    public String getIdentifier() {
        return "use";
    }
}
