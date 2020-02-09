package me.jaskowicz.factoriodiscordrelay;

import me.jaskowicz.factoriodiscordrelay.Exceptions.AuthenticationException;
import me.jaskowicz.factoriodiscordrelay.Listeners.MessageListener;
import me.jaskowicz.factoriodiscordrelay.Rcon.Rcon;
import me.jaskowicz.factoriodiscordrelay.Settings.MAIN_SETTINGS;
import me.jaskowicz.factoriodiscordrelay.Tasks.ServerTask;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Timer;

public class Main {

    public static String discordBotToken = "";
    public static JDA jda;
    public static String guildID = "";
    public static String consoleChannelID = "";
    public static String chatChannelID = "";
    //public static List<String> disabledCommands = new ArrayList<>();
    //public static boolean advancedConsole = false;
    //public static int playersOnline = 0;
    public static Rcon rcon;

    // This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License.
    // To view a copy of this license,
    // visit http://creativecommons.org/licenses/by-nc-sa/4.0/ or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

    // Created by: Archie Jaskowicz.
    // All files within the packages 'Rcon' and 'Exceptions' belong to  the github user: Kronos666.
    // Rcon is a simple communication system via a port. Valve has a great documentation on this.

    public static void main(String[] args) {

        readConfigFile();

        try {
            rcon = new Rcon(MAIN_SETTINGS.rconHost, MAIN_SETTINGS.rconPort, MAIN_SETTINGS.rconPassword.getBytes());

            JDABuilder builder = new JDABuilder(AccountType.BOT);

            builder.setToken(discordBotToken);
            builder.addEventListeners(new MessageListener());
            builder.addEventListeners(new ListenerAdapter() {
                @Override
                public void onReady(@NotNull ReadyEvent event) {
                    //event.getJDA().getPresence().setActivity(Activity.playing(""));
                    try {
                        // This just doesn't work sadly.
                        //String result = rcon.command("/command local count = 0\n" +
                        //        "for _ in pairs(game.connected_players) do count = count + 1 end\n" +
                        //        "return count");
                        rcon.command("/silent-command game.print(\"Factorio-Discord-Relay (FDR) has loaded.\")");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });

            try {
                jda = builder.build();
                System.out.println("Bot loaded successfully!");
            } catch (LoginException e) {
                e.printStackTrace();
                System.out.println("Bot failed to load. Error is shown in console.");
            }

            Timer timer = new Timer();
            timer.schedule(new ServerTask(), 0, 500);

            // Display the result in the console
            //System.out.println(result);
        } catch (IOException | AuthenticationException ex) {
            ex.printStackTrace();
        }

    }

    public static void readConfigFile() {
        File file = new File("FactorioDiscordRelayConfig.txt");

        if(file.exists()) {

            System.out.println("Config file found! Reading data...");

            Scanner scanner = null;

            try {
                scanner = new Scanner(file);
            } catch(FileNotFoundException e) {
                System.out.println("Config file does not exist or can not be read! Please create the file that you specified if you wish to use it.");
                return;
            }

            if(!scanner.hasNextLine()) {
                System.out.println("Config file does not have any information!");
                return;
            }

            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] dataArray = data.split(": ");

                if(dataArray.length != 0) {
                    if(dataArray.length == 2) {
                        if(dataArray[0].equals("botToken")) {
                            discordBotToken = dataArray[1];
                        } else if(dataArray[0].equals("chatID")) {
                            chatChannelID = dataArray[1];
                        } else if(dataArray[0].equals("consoleID")) {
                            consoleChannelID = dataArray[1];
                        } else if(dataArray[0].equals("guildID")) {
                            guildID = dataArray[1];
                        } else if(dataArray[0].equals("serverOutFilePath")) {
                            MAIN_SETTINGS.serverOutFilePath = dataArray[1];
                        } else if(dataArray[0].equals("rconHost")) {
                            MAIN_SETTINGS.rconHost = dataArray[1];
                        } else if(dataArray[0].equals("rconPort")) {
                            MAIN_SETTINGS.rconPort = Integer.parseInt(dataArray[1]);
                        } else if(dataArray[0].equals("rconPassword")) {
                            MAIN_SETTINGS.rconPassword = dataArray[1];
                        }
                    } else if (dataArray.length < 2) {
                        System.out.println("Line has too little arguments on it. Ignoring line.");
                    } else {
                        System.out.println("Line has too many arguments on it. Ignoring line.");
                    }
                } else {
                    System.out.println("Config file does not have any information!");
                    return;
                }
            }

            scanner.close();
        } else {
            try {
                boolean created = file.createNewFile();

                if(created) {
                    System.out.println("The file has been created!");
                } else {
                    System.out.println("The file failed to be created. I may be lacking permissions to create files, try executing me as administrator.");
                    return;
                }

                System.out.println("I have created the config file for you! Putting default data in...");

                FileWriter fileWriter = new FileWriter("FactorioDiscordRelayConfig.txt");
                fileWriter.write("botToken: <BOT_TOKEN>\n");
                fileWriter.write("chatID: <CHAT_ID>\n");
                fileWriter.write("consoleID: <CONSOLE_ID>\n");
                fileWriter.write("guildID: <GUILD_ID>\n");
                fileWriter.write("serverOutFilePath: <FILE_PATH>\n");
                fileWriter.write("rconHost: <RCON_IP>\n");
                fileWriter.write("rconPort: <RCON_PORT>\n");
                fileWriter.write("rconPassword: <RCON_PASSWORD>");
                fileWriter.close();

                System.out.println("Default data has been placed in config file.");
                return;

            } catch (Exception ex) {
                System.out.println("An error occurred when trying to create the file.");
                return;
            }
        }
    }

}
