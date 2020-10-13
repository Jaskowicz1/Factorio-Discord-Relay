package me.jaskowicz.factoriodiscordrelay.Tasks;

import me.jaskowicz.factoriodiscordrelay.Main;
import me.jaskowicz.factoriodiscordrelay.Settings.MAIN_SETTINGS;
import me.jaskowicz.factoriodiscordrelay.Utils.ConsoleColour;
import me.jaskowicz.factoriodiscordrelay.Utils.ConsoleLogging;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ServerTask extends TimerTask {

    // Credit to Github user jschmidt-1 for making this look better.

    private List<String> lastResults = new ArrayList<>();
    private String lastSaid = "";
    
    private static String pretifyLogLine(String logLine) {
        return logLine.replaceAll("^.+\\[\\S+\\]\\s(.+)$", "$1");
    }
    
    private void sendMessage(String channelId, String message) {
        // Prevents the discord bot from saying the same thing.
        try {
            if (!message.equals(lastSaid)) {
                Objects.requireNonNull(Objects.requireNonNull(Main.jda.getGuildById(Main.guildID)).getTextChannelById(channelId))
                        .sendMessage(message).queue();
            }

            lastSaid = message;
        } catch(InsufficientPermissionException permEx) {
            ConsoleLogging.sendErrorMessage("An error occurred when trying to send a message to " + channelId + ". It seems that the bot lacks the permission: " + permEx.getPermission().getName());
        } catch (Exception ex) {
            ConsoleLogging.sendErrorMessage("An error occurred when trying to send a message to " + channelId + ".");
        }
    }

    @Override
    public void run() {
        File file = new File(MAIN_SETTINGS.serverOutFilePath);

        if(file.exists()) {

            Scanner scanner = null;
            ReversedLinesFileReader reversedLinesFileReader = null;

            try {
                scanner = new Scanner(file);
                reversedLinesFileReader = new ReversedLinesFileReader(file, StandardCharsets.UTF_8);
            } catch (IOException e) {
                ConsoleLogging.sendErrorMessage("server.out file does not exist or can not be read! Please attempt to fix this.");
                return;
            }

            if (!scanner.hasNextLine()) {
                ConsoleLogging.sendErrorMessage("server.out file does not have any information!");
                return;
            }

            List<String> results = new ArrayList<>();

            try {
                String currentLine = "";
                while ((currentLine = reversedLinesFileReader.readLine()) != null && results.size() < 3) {
                    results.add(currentLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (Main.jda.getStatus() == JDA.Status.CONNECTED) {
                if (!results.get(0).equals(lastResults.get(0))) {
                    for(int i = 0; i < results.size(); i++) {
                        if (results.get(i).contains("[JOIN]")) {
                        	String message = ":green_circle: " + pretifyLogLine(results.get(i));

                            sendMessage(Main.chatChannelID, message);
                            break;
                        } else if (results.get(i).contains("[LEAVE]")) {
                        	String message = ":red_circle: " + pretifyLogLine(results.get(i));

                        	sendMessage(Main.chatChannelID, message);
                            break;
                        } else if (results.get(i).contains("[CHAT]")) {
                        	String message = ":speech_balloon: " + pretifyLogLine(results.get(i));

                        	sendMessage(Main.chatChannelID, message);
                            break;
                        } else if (results.get(i).contains("[COMMAND]")) {
                            String message = ":exclamation: " + pretifyLogLine(results.get(i));

                            sendMessage(Main.consoleChannelID, message);
                            break;
                        } else if (results.get(i).contains("Cannot execute command.")) {
                            String message = ":warning: " + results.get(i);

                            sendMessage(Main.consoleChannelID, message);
                        }
                    }
                }
            }

            lastResults.clear();
            lastResults.addAll(results);
        } else {
            ConsoleLogging.sendErrorMessage("server.out file is missing!");
        }
    }
}
