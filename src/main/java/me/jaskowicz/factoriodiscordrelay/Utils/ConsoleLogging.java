package me.jaskowicz.factoriodiscordrelay.Utils;

public class ConsoleLogging {

    public static void sendErrorMessage(String message) {
        if(!System.getProperty("os.name").contains("Win")) {
            System.out.println(ConsoleColour.RED + "ERROR: " + message + ConsoleColour.RESET);
        } else {
            System.out.println("ERROR: " + message);
        }
    }

    public static void sendWarningMessage(String message) {
        if(!System.getProperty("os.name").contains("Win")) {
            System.out.println(ConsoleColour.YELLOW + "WARNING: " + message + ConsoleColour.RESET);
        } else {
            System.out.println("WARNING: " + message);
        }
    }

    public static void sendGreenMessage(String message) {
        if(!System.getProperty("os.name").contains("Win")) {
            System.out.println(ConsoleColour.GREEN + message + ConsoleColour.RESET);
        } else {
            System.out.println(message);
        }
    }
}
