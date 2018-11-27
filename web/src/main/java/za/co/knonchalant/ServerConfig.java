package za.co.knonchalant;

public class ServerConfig {
    private static boolean production;
    private static String botName;
    private static String botSecret;

    static {
        botName = System.getProperty("botName");
        botSecret = System.getProperty("botSecret");

        String productionString = System.getProperty("production");
        production = productionString != null && productionString.toLowerCase().equals("true");

        System.out.println("**************************************");
        System.out.println("*            Bots 'n whatnots        *");
        System.out.println("*  Server configuration:             *");
        System.out.println("*                                    *");
        System.out.println(String.format("*       Production: %s              *", production ? "Yes" : "No "));
        System.out.println("**************************************");
    }



    public static boolean isProduction() {
        return production;
    }

    public static boolean isTest() {
        return !production;
    }
}
