package za.co.knonchalant.telegram;

import za.co.knonchalant.candogram.IBot;

import javax.ejb.EJB;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ShutdownServletContextListener implements ServletContextListener {

    @EJB
    IBot iBot;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        iBot.shutdown();
    }
}
