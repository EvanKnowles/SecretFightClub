package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.domain.fightclub.ReviewItem;

public class ReviewItemCommand extends FightClubCommand {
    private Item item;

    public ReviewItemCommand(Item item) {
        this.item = item;
    }

    @Override
    void execute(MessageSender handler) {
        if (item.getFighterId() != null) {
            return;
        }

        if (item.getChatId() == null) {
            return;
        }

        ReviewItem reviewItem = new ReviewItem();
    }
}
