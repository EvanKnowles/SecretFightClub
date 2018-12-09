package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.AdminDAO;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.domain.fightclub.ReviewItem;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EApprovalStatus;

public class SubmitItemCommand extends FightClubCommand {
    private IUpdate update;
    private Item item;

    public SubmitItemCommand(IUpdate update, Item item) {
        this.update = update;
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

        AdminDAO adminDAO = AdminDAO.get();

        ReviewItem reviewItem = adminDAO.getReviewItemFor(item.getId());
        if (reviewItem != null) {
            handler.sendMessage(update, "That item is already being reviewed - patience padawan.");
            return;
        }

        ReviewItem wannabe = new ReviewItem(item.getId(), item.getName());
        adminDAO.persistSubmission(wannabe);

        handler.sendMessage(update, "The best minds of our generation are busy... doing something else. But some other people are checking your submission.");
    }
}
