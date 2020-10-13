package me.jaskowicz.factoriodiscordrelay.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {

    public static void getVersion(final Consumer<String> consumer) {
        try (InputStream inputStream = new URL("https://raw.githubusercontent.com/Jaskowicz1/Factorio-Discord-Relay/master/version").openStream(); Scanner scanner = new Scanner(inputStream)) {
            if (scanner.hasNext()) {
                consumer.accept(scanner.next());
            }
        } catch (IOException exception) {
            ConsoleLogging.sendErrorMessage("Failed to look for updates: " + exception.getMessage());
        }
    }
}
