package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.candogram.handlers.User;
import za.co.knonchalant.liketosee.dao.AdminDAO;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Item;
import za.co.knonchalant.liketosee.domain.fightclub.ReviewItem;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EApprovalStatus;
import za.co.knonchalant.telegram.handlers.fightclub.details.SubmitDetails;

import java.util.Date;

public class ReviewItemCompleteHandler extends FightClubMessage implements IResponseHandler<SubmitDetails> {
    @Override
    public int getStep() {
        return 1;
    }

    @Override
    public PendingResponse handleResponse(IUpdate update, SubmitDetails state, PendingResponse pendingResponse) {
        int itemId = state.getItemId();
        AdminDAO adminDAO = AdminDAO.get();
        ReviewItem reviewItemFor = adminDAO.getReviewItemFor(itemId);
        if (reviewItemFor.getStatus() != EApprovalStatus.PENDING) {
            sendMessage(update, "Uh, someone reviewed that already. Sorry.");
            return pendingResponse.complete();
        }

        String text = update.getText();
        EApprovalStatus newStatus = EApprovalStatus.from(text);
        if (newStatus == EApprovalStatus.APPROVED) {
            sendMessage(update, "Totes awesome, that's what I thought too.");

            FighterDAO fighterDAO = FighterDAO.get();
            Item item = fighterDAO.getItem(reviewItemFor.getItemId());
            item.setChatId(null);
            fighterDAO.persistItem(item);
        } else {
            sendMessage(update, "Yeah, screw that item.");
        }

        reviewItemFor.setStatus(newStatus);
        reviewItemFor.setReviewDate(new Date());
        User user = update.getUser();
        reviewItemFor.setReviewedById(user.getId());
        reviewItemFor.setReviewedByName(user.getFirstName());
        adminDAO.persistSubmission(reviewItemFor);

        return pendingResponse.complete();
    }

    @Override
    public Class<SubmitDetails> getDetailsClass() {
        return SubmitDetails.class;
    }

    @Override
    public String getIdentifier() {
        return "review";
    }
}
