package za.co.knonchalant.telegram.handlers.fightclub;

import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.BaseMessage;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.candogram.handlers.User;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Fighter;
import za.co.knonchalant.liketosee.domain.fightclub.enums.EClasses;
import za.co.knonchalant.telegram.handlers.fightclub.details.RegisterDetails;

public class RegisterResponseHandler extends BaseMessage implements IResponseHandler<RegisterDetails> {
    @Override
    public int getStep() {
        return 0;
    }

    @Override
    public PendingResponse handleResponse(IUpdate update, RegisterDetails state, PendingResponse pendingResponse) {
        String response = update.getText();
        System.out.println(response);

        User user = update.getUser();

        String userName = user.getUserName() != null ? user.getUserName() : user.getFirstName();

        sendMessage(update, userName + " chooses to be a " + response);

        EClasses type = EClasses.fromName(response);
        if (type == null) {
            sendMessage(update, "Quit muckin' around, secret fight club is serious business.");
            return null;
        }

        Fighter fighter = new Fighter(user.getFirstName(), user.getId(), update.getChatId(), type);
        FighterDAO fighterDAO = FighterDAO.get();
        fighterDAO.persistFighter(fighter);

        return pendingResponse.complete();
    }

    @Override
    public Class<RegisterDetails> getDetailsClass() {
        return RegisterDetails.class;
    }

    @Override
    public String getIdentifier() {
        return "register";
    }
}
