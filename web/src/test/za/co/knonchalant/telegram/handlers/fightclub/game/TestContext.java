package za.co.knonchalant.telegram.handlers.fightclub.game;

import za.co.knonchalant.telegram.handlers.fightclub.game.testmocks.MockClubDAO;
import za.co.knonchalant.telegram.handlers.fightclub.game.testmocks.MockFighterDAO;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class TestContext extends InitialContext {
    private static Map<String, Object> LOOKUP = new HashMap<>();

    public TestContext() throws NamingException {
        super(true /*prevents initialization*/);

        init();
    }


    @Override
    protected void init(Hashtable<?, ?> environment) throws NamingException {
        init();
    }

    public void init() {
        LOOKUP.put("java:global/fightclub-web-1.0-SNAPSHOT/FighterDAO!za.co.knonchalant.liketosee.dao.FighterDAO", new MockFighterDAO());
        LOOKUP.put("java:global/fightclub-web-1.0-SNAPSHOT/ClubDAO!za.co.knonchalant.liketosee.dao.ClubDAO", new MockClubDAO());
    }

    public Object lookup(String name) throws NamingException {
        //filthy hack to reset the initial context (and hence the DB) in between tests
        if ("reset".equals(name)) {
            init();
            return null;
        }

        if (LOOKUP.containsKey(name)) {
            return LOOKUP.get(name);
        }

        System.err.println("Missing: " + name);
        throw new NullPointerException();
    }
}
