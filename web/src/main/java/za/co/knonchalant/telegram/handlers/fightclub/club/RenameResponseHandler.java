package za.co.knonchalant.telegram.handlers.fightclub.club;

import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessage;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.candogram.handlers.User;
import za.co.knonchalant.liketosee.dao.ClubDAO;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Club;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EClasses;
import za.co.knonchalant.telegram.handlers.fightclub.details.RegisterDetails;
import za.co.knonchalant.telegram.handlers.fightclub.details.RenameDetails;

public class RenameResponseHandler extends BaseMessage implements IResponseHandler<RenameDetails> {

    @Override
    public int getStep() {
        return 0;
    }

    @Override
    public PendingResponse handleResponse(IUpdate update, RenameDetails state, PendingResponse pendingResponse) {
        String response = update.getText();

        User user = update.getUser();

        sendMessage(update, "Fine, it's called '" + response + "' now. Whatevs.");
        Fighter fighterByUserId = FighterDAO.get().getFighterByUserId(user.getId());
        Club club = fighterByUserId.getClub();
        club.setName(response);
        ClubDAO.get().persistClub(club);

        return pendingResponse.complete();
    }

    @Override
    public Class<RenameDetails> getDetailsClass() {
        return RenameDetails.class;
    }

    @Override
    public String getIdentifier() {
        return "rename";
    }
}
