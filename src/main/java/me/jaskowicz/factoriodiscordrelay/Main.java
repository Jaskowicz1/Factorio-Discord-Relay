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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class Main {

    public static String discordBotToken = "";
    public static JDA jda;
    public static String guildID = "";
    public static String consoleChannelID = "";
    public static String chatChannelID = "";
    public static List<String> disabledCommands = new ArrayList<>();
    public static boolean advancedConsole = false;
    public static int playersOnline = 0;
    public static Rcon rcon;

    // All files within the packages 'Rcon' and 'Exceptions' belong to  the github user: Kronos666.

    public static void main(String[] args) {

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

}
