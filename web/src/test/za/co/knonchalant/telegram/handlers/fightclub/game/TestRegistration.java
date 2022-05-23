package za.co.knonchalant.telegram.handlers.fightclub.game;

import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import za.co.knonchalant.candogram.Bots;
import za.co.knonchalant.candogram.IBotAPI;
import za.co.knonchalant.candogram.domain.BaseDetail;
import za.co.knonchalant.candogram.domain.PendingResponse;
import za.co.knonchalant.candogram.handlers.IMessageHandler;
import za.co.knonchalant.candogram.handlers.IResponseHandler;
import za.co.knonchalant.candogram.handlers.IResponseMessageHandler;
import za.co.knonchalant.candogram.handlers.IUpdate;
import za.co.knonchalant.liketosee.dao.ClubDAO;
import za.co.knonchalant.liketosee.dao.FighterDAO;
import za.co.knonchalant.liketosee.domain.fightclub.Club;
import za.co.knonchalant.telegram.bots.SecretFightClubBotAPIBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TestRegistration extends TestWithMocks {
    private static final Logger LOGGER = Logger.getLogger(TestRegistration.class.getName());

    private Bots bot;

    @Before
    public void setUp() {
        FighterDAO.set(mockFighterDAO);
        ClubDAO.set(mockClubDAO);

        mockFighterDAO.clear();

        Club ourFakeClub = new Club();
        ourFakeClub.setName("Totes not a fake club");
        ourFakeClub.setJoinCode("NOTAFAKE");
        ClubDAO.get().persistClub(ourFakeClub);

        SecretFightClubBotAPIBuilder secretFightClubBotAPIBuilder = new SecretFightClubBotAPIBuilder();
        bot = secretFightClubBotAPIBuilder.buildBotForAPI("secretFightClub", MOCK_BOT_API);
    }

    @Test
    public void registerFighter() {
        handleMessage(createMockUpdate(1, "/register"));
        assertResponse(1, "Signing up are we? Pick a class then.");

        // pick a class
        handleMessage(createMockUpdate(1, "Oyster"));
        Assert.assertFalse("pending response map is not empty", pendingResponseMap.isEmpty());
        Assert.assertTrue("pending response knows we picked Oyster", pendingResponseMap.get(1L).get(0).getDetails().contains("Oyster"));

        // pick a club
        handleMessage(createMockUpdate(1, "NOTAREALID"));
        assertResponse(1, "Righto, you're in a brand spankin' new club called");
    }

    @Test
    public void registerFighterJoinClub() {
        handleMessage(createMockUpdate(1, "/register"));
        assertResponse(1, "Signing up are we? Pick a class then.");

        // pick a class
        handleMessage(createMockUpdate(1, "Oyster"));
        Assert.assertFalse("pending response map is not empty", pendingResponseMap.isEmpty());
        Assert.assertTrue("pending response knows we picked Oyster", pendingResponseMap.get(1L).get(0).getDetails().contains("Oyster"));

        // pick a club
        handleMessage(createMockUpdate(1, "NOTAFAKE"));
        assertResponse(1, "Righto, you've joined ");
    }

    @Test
    public void registerFighterWrongClass() {
        handleMessage(createMockUpdate(1, "/register"));
        assertResponse(1, "Signing up are we? Pick a class then.");

        handleMessage(createMockUpdate(1, "Something else I dunno"));

        assertResponse(1, "Quit muckin' around");
    }

    private void assertResponse(int userId, String responseText) {
        Assert.assertTrue(MOCK_BOT_API.getLastResponse(userId).contains(responseText));
    }

    private void handleMessage(IUpdate theUpdate) {
        List<IMessageHandler> iMessageHandlers = bot.getHandlers();

        if (theUpdate.getText() == null || (!theUpdate.getText().startsWith("/"))) {
            if (tryHandleAsResponse(theUpdate, iMessageHandlers, MOCK_BOT_API)) {
                return;
            }
        }

        for (IMessageHandler iMessageHandler : iMessageHandlers) {
            try {
                handle(iMessageHandler, theUpdate);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Handler " + iMessageHandler.toString() + " broke", ex);
            }
        }
    }

    private boolean tryHandleAsResponse(IUpdate update, List<IMessageHandler> iMessageHandlers, IBotAPI bot) {
        List<PendingResponse> pendingResponses = pendingResponseMap.get(update.getUser().getId());

        for (PendingResponse pendingResponse : pendingResponses) {
            for (IMessageHandler iMessageHandler : iMessageHandlers) {
                try {
                    if (processHandler(update, pendingResponse, iMessageHandler, bot)) return true;
                } catch (Exception ex) {
                    LOGGER.log(Level.WARNING, iMessageHandler.getClass().toString() + " failed on message: " + ex.toString());
                }
            }
        }
        return false;
    }

    private boolean processHandler(IUpdate update, PendingResponse pendingResponse, IMessageHandler iMessageHandler, IBotAPI bot) {
        if (!(iMessageHandler instanceof IResponseMessageHandler)) {
            return false;
        }

        IResponseMessageHandler handler = (IResponseMessageHandler) iMessageHandler;
        List<IResponseHandler> handlers = handler.getHandlers();
        for (IResponseHandler<?> iResponseHandler : handlers) {
            if (tryHandle(update, pendingResponse, iResponseHandler, bot)) return true;
        }

        return false;
    }

    private boolean tryHandle(IUpdate update, PendingResponse pendingResponse, IResponseHandler iResponseHandler, IBotAPI bot) {
        try {
            Gson gson = new Gson();
            BaseDetail details = (BaseDetail) gson.fromJson(pendingResponse.getDetails(), iResponseHandler.getDetailsClass());

            if (details.getStep() == iResponseHandler.getStep() && pendingResponse.getIdentifier().equals(iResponseHandler.getIdentifier())) {
                iResponseHandler.setBot(bot);
                pendingResponse.setStepRetry(false);
                pendingResponse.setStepHandled(false);

                PendingResponse resultResponse = iResponseHandler.handleResponse(update, details, pendingResponse);

                if (resultResponse.isStepHandled()) {
                    details.nextStep();
                }
                resultResponse.setDetails(details);

                if (!resultResponse.isComplete()) {
                    pendingResponseMap.get(update.getUser().getId()).add(resultResponse);
//                    telegramDAO.persistPendingResponse(resultResponse);
                } else {
                    clearPending(update);
                }

                return resultResponse.isComplete() || resultResponse.isStepHandled() || resultResponse.isStepRetry();
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Handle response issue.", ex);
            return true;
        }

        return false;
    }

    private void clearPending(IUpdate update) {
        pendingResponseMap.remove(update.getUser().getId());
    }

    private void handle(IMessageHandler iMessageHandler, IUpdate update) {
        try {
            if (iMessageHandler.matches(update)) {
                PendingResponse handle = iMessageHandler.handle(update);
                if (handle != null) {
                    pendingResponseMap.remove(update.getUser().getId());

                    ArrayList<PendingResponse> pendingResponses = new ArrayList<>();
                    pendingResponseMap.put(update.getUser().getId(), pendingResponses);
                    pendingResponses.add(handle);
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Command handler exception", ex);
        }
    }
}
