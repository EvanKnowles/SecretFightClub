package za.co.knonchalant.telegram.handlers.fightclub.game;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import java.util.Hashtable;

public class TestInitialContextFactory implements InitialContextFactory {

    private TestContext testContext;

    public Context getInitialContext(Hashtable<?, ?> arg0) throws NamingException {
        if (testContext == null) {
            testContext = new TestContext();
        }

        return testContext;
    }
}