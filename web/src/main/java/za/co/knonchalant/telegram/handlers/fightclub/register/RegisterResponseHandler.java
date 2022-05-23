package za.co.knonchalant.telegram.handlers.fightclub.register;

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
    public static final String RESPONSE_TEXT = "Enter your club's super-secret code, or any club's not super code to start your own super secret club. No pressure.";

    @Override
    public int getStep() {
        return 0;
    }

    @Override
    public PendingResponse handleResponse(IUpdate update, RegisterDetails state, PendingResponse pendingResponse) {
        String response = update.getText();

        User user = update.getUser();

        String userName = user.getUserName() != null ? user.getUserName() : user.getFirstName();

        sendMessage(update, userName + " chooses to be a " + response);

        EClasses type = EClasses.fromName(response);
        if (type == null) {
            sendMessage(update, "Quit muckin' around, secret fight club is serious business.");
            return pendingResponse.retry();
        }

        state.setChosenClass(response);
        sendMessage(update, RESPONSE_TEXT);

        return pendingResponse.handled();
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
