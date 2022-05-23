package za.co.knonchalant.telegram.handlers.fightclub.register;

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

public class PickClubResponseHandler extends BaseMessage implements IResponseHandler<RegisterDetails> {

    private static final String TOKENS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    @Override
    public int getStep() {
        return 1;
    }

    @Override
    public PendingResponse handleResponse(IUpdate update, RegisterDetails state, PendingResponse pendingResponse) {
        String response = update.getText();
        User user = update.getUser();

        EClasses type = EClasses.fromName(state.getChosenClass());

        Club club = ClubDAO.get().findClub(response);

        if (club == null) {
            club = new Club();
            club.setName("Super Amazeballs Club");
            club.setJoinCode(generateCode());

            Fighter fighter = new Fighter(user.getFirstName(), user.getId(), type);
            fighter.setClub(club);

            FighterDAO fighterDAO = FighterDAO.get();
            fighterDAO.persistFighter(fighter);

            sendMessage(update, "Righto, you're in a brand spankin' new club called Super Amazeballs Club with a joincode of " + club.getJoinCode());
            return pendingResponse.complete();
        }

        Fighter fighter = new Fighter(user.getFirstName(), user.getId(), type);
        fighter.setClub(club);

        sendMessage(update, "Righto, you've joined " + club.getName() + " - hope this doesn't end in tears.");

        FighterDAO fighterDAO = FighterDAO.get();
        fighterDAO.persistFighter(fighter);

        return pendingResponse.complete();
    }

    private String generateCode() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            result.append(TOKENS.charAt((int) (Math.random() * TOKENS.length())));
        }
        return result.toString();
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
