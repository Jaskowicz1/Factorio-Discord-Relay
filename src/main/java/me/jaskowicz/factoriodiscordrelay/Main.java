package me.jaskowicz.factoriodiscordrelay;

import me.jaskowicz.factoriodiscordrelay.Exceptions.AuthenticationException;
import me.jaskowicz.factoriodiscordrelay.Listeners.MessageListener;
import me.jaskowicz.factoriodiscordrelay.Rcon.Rcon;
import me.jaskowicz.factoriodiscordrelay.Settings.MAIN_SETTINGS;
import me.jaskowicz.factoriodiscordrelay.Tasks.ServerCount;
import me.jaskowicz.factoriodiscordrelay.Tasks.ServerTask;
import me.jaskowicz.factoriodiscordrelay.Utils.ConsoleColour;
import me.jaskowicz.factoriodiscordrelay.Utils.ConsoleLogging;
import me.jaskowicz.factoriodiscordrelay.Utils.UpdateChecker;
import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Main {

    public static String discordBotToken = "";
    public static JDA jda;
    public static String guildID = "";
    public static String consoleChannelID = "";
    public static String chatChannelID = "";
    //public static List<String> disabledCommands = new ArrayList<>();
    public static Rcon rcon;

    public static int M_SERVER_COUNT = 0;

    // This work is licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License.
    // To view a copy of this license,
    // visit http://creativecommons.org/licenses/by-nc-sa/4.0/ or send a letter to Creative Commons, PO Box 1866, Mountain View, CA 94042, USA.

    // Created by: Archie Jaskowicz.
    // Assistance from: jschmidt-1 (ServerTask class)

    // All files within the packages 'Rcon' and 'Exceptions' belong to  the github user: Kronos666.
    // Rcon is a simple communication system via a port. Valve has a great documentation on this.

    public static void main(String[] args) {

        try {
            final Properties properties = new Properties();
            properties.load(Main.class.getClassLoader().getResourceAsStream("project.properties"));

            System.out.println("Loading FDR at version: " + properties.getProperty("version"));

            UpdateChecker.getVersion(version -> {

                if (!properties.getProperty("version").equals(version)) {
                    if(!System.getProperty("os.name").contains("Win")) {

                        System.out.println(ConsoleColour.YELLOW + "There is an update available!\n" +
                                "You are currently on: " + properties.getProperty("version") + "\n" +
                                "The most recent update is: " + version + ConsoleColour.RESET);
                    } else {
                        System.out.println("There is an update available!\n" +
                                "You are currently on: " + properties.getProperty("version") + "\n" +
                                "The most recent update is: " + version);
                    }
                }
            });
        } catch(IOException e) {
            e.printStackTrace();
            ConsoleLogging.sendErrorMessage("Error when obtaining version.");
        }

        readConfigFile();

        try {
            rcon = new Rcon(MAIN_SETTINGS.rconHost, MAIN_SETTINGS.rconPort, MAIN_SETTINGS.rconPassword.getBytes());

            JDABuilder builder = new JDABuilder(AccountType.BOT);

            builder.setToken(discordBotToken);
            builder.setActivity(Activity.playing("Loading..."));
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

                        String count = rcon.command("/players online count");

                        //M_SERVER_COUNT = Integer.parseInt(count);
                        builder.setActivity(Activity.playing(count));

                        if(MAIN_SETTINGS.cleanMessages) {
                            rcon.command("/silent-command game.print(\"Factorio-Discord-Relay (FDR) has loaded.\")");
                        } else {
                            rcon.command("Factorio-Discord-Relay (FDR) has loaded.");
                        }

                        System.out.println(ConsoleColour.GREEN + "Bot loaded successfully!" + ConsoleColour.RESET);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });

            try {
                jda = builder.build();
                System.out.println("Bot is set to load!");
            } catch (LoginException e) {
                e.printStackTrace();
                ConsoleLogging.sendErrorMessage("Bot failed to load. Error is shown.");
            }

            Timer timer = new Timer();
            timer.schedule(new ServerTask(), 0, 500);
            timer.schedule(new ServerCount(), 0, 1500);
        } catch (IOException | AuthenticationException ex) {
            ex.printStackTrace();
            ConsoleLogging.sendErrorMessage("Failed to connect to the RCON server.");
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
                        } else if(dataArray[0].equals("disableDiscordConsole")) {
                            if(dataArray[1].equalsIgnoreCase("true")) {
                                MAIN_SETTINGS.consoleEnabled = false;
                            } else if(dataArray[1].equalsIgnoreCase("false")) {
                                MAIN_SETTINGS.consoleEnabled = true;
                            }
                        } else if(dataArray[0].equals("ignoreStartWarning")) {
                            if(dataArray[1].equalsIgnoreCase("true")) {
                                MAIN_SETTINGS.ignoreStartWarning = true;
                                ConsoleLogging.sendWarningMessage("This will mean you can not gain achievements from here on out.");
                            } else if(dataArray[1].equalsIgnoreCase("false")) {
                                MAIN_SETTINGS.ignoreStartWarning = false;
                            }
                        } else if(dataArray[0].equals("cleanMessages")) {
                            if(dataArray[1].equalsIgnoreCase("true")) {
                                MAIN_SETTINGS.cleanMessages = true;
                                if(!MAIN_SETTINGS.ignoreStartWarning) {

                                    boolean statementFinished = false;
                                    Scanner input = new Scanner(System.in);

                                    while(!statementFinished) {
                                        ConsoleLogging.sendWarningMessage("This will mean you can not gain achievements from here on out. Are you sure you want to continue (Y/n)?");
                                        ConsoleLogging.sendWarningMessage("(You can disable this in the config by turning ignoreStartWarning to true)");
                                        String response = input.nextLine();

                                        if (response.equalsIgnoreCase("y")) {
                                            System.out.println("Continuing...");
                                            statementFinished = true;
                                        } else if (response.equalsIgnoreCase("n")) {
                                            System.out.println("Exiting Factorio-Discord-Relay.");
                                            System.exit(0);
                                        }
                                    }
                                }
                            } else if(dataArray[1].equalsIgnoreCase("false")) {
                                MAIN_SETTINGS.cleanMessages = false;
                            }
                        }
                    } /* else if (dataArray.length < 2) {
                        System.out.println("Line has too little arguments on it. Ignoring line.");
                    } else {
                        System.out.println("Line has too many arguments on it. Ignoring line.");
                    } */
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
                    System.out.println("I have created the config file for you!");
                } else {
                    ConsoleLogging.sendErrorMessage("The file failed to be created. I may be lacking permissions to create files, try executing me as administrator.");
                    return;
                }

                System.out.println("Putting default data in...");

                FileWriter fileWriter = new FileWriter("FactorioDiscordRelayConfig.txt");
                fileWriter.write("botToken: <BOT_TOKEN>\n");
                fileWriter.write("chatID: <CHAT_ID>\n");
                fileWriter.write("consoleID: <CONSOLE_ID>\n");
                fileWriter.write("guildID: <GUILD_ID>\n");
                fileWriter.write("serverOutFilePath: <FILE_PATH>\n");
                fileWriter.write("rconHost: <RCON_IP>\n");
                fileWriter.write("rconPort: <RCON_PORT>\n");
                fileWriter.write("rconPassword: <RCON_PASSWORD>");
                fileWriter.write("disableDiscordConsole: false");
                fileWriter.close();

                System.out.println("Default data has been placed in config file.");
                return;

            } catch (Exception ex) {
                ConsoleLogging.sendErrorMessage("An error occurred when trying to create the file.");
                return;
            }
        }
    }

}
