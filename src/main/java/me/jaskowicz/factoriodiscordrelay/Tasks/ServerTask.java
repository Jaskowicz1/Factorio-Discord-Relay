package me.jaskowicz.factoriodiscordrelay.Tasks;

import me.jaskowicz.factoriodiscordrelay.Main;
import me.jaskowicz.factoriodiscordrelay.Settings.MAIN_SETTINGS;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ServerTask extends TimerTask {

    private List<String> lastResults = new ArrayList<>();

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
                System.out.println("Specified file does not exist or can not be read! Please create the file that you specified if you wish to use it.");
                return;
            }

            if (!scanner.hasNextLine()) {
                System.out.println("Specified file does not have any information regarding IPs and Ports (IP:PORT)!");
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
                            //Main.playersOnline += 1;

                            //Main.jda.getPresence().setActivity(Activity.playing(Main.playersOnline + " players"));

                            Objects.requireNonNull(Objects.requireNonNull(Main.jda.getGuildById(Main.guildID)).getTextChannelById(Main.consoleChannelID))
                                    .sendMessage(":heavy_plus_sign: " + results.get(i)).queue();
                            break;
                        } else if (results.get(i).contains("[LEAVE]")) {
                            //Main.playersOnline -= 1;

                            //Main.jda.getPresence().setActivity(Activity.playing(Main.playersOnline + " players"));

                            Objects.requireNonNull(Objects.requireNonNull(Main.jda.getGuildById(Main.guildID)).getTextChannelById(Main.consoleChannelID))
                                    .sendMessage(":heavy_minus_sign: " + results.get(i)).queue();
                            break;
                        } else if (results.get(i).contains("[CHAT]")) {
                            //String message = ":speech_balloon: [" + FormatUtils.formatTimeFromDate(new Date(System.currentTimeMillis())) + "] " + event.getPlayer().getName() + " » " + event.getMessage();
                            String message = ":speech_balloon: " + results.get(i);

                            Objects.requireNonNull(Objects.requireNonNull(Main.jda.getGuildById(Main.guildID)).getTextChannelById(Main.chatChannelID))
                                    .sendMessage(message).queue();
                            break;
                        } else if (results.get(i).contains("[COMMAND]")) {
                            String message = ":exclamation: " + results.get(i);

                            Objects.requireNonNull(Objects.requireNonNull(Main.jda.getGuildById(Main.guildID)).getTextChannelById(Main.consoleChannelID))
                                    .sendMessage(message).queue();
                            break;
                        } else if (results.get(i).contains("Cannot execute command.")) {
                            String message = ":warning: " + results.get(i);

                            Objects.requireNonNull(Objects.requireNonNull(Main.jda.getGuildById(Main.guildID)).getTextChannelById(Main.consoleChannelID))
                                    .sendMessage(message).queue();
                        }
                    }
                }
            }

            lastResults.clear();
            lastResults.addAll(results);
        } else {
            System.out.println("Server.out file is missing!");
        }
    }
}
