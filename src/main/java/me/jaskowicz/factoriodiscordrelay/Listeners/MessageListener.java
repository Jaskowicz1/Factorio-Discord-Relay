package me.jaskowicz.factoriodiscordrelay.Listeners;

import me.jaskowicz.factoriodiscordrelay.Main;
import me.jaskowicz.factoriodiscordrelay.Settings.MAIN_SETTINGS;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        // Prevent massive loop
        if (event.getAuthor().isBot() || event.isWebhookMessage()) {
            return;
        }

        // Debug line.
        //System.out.println("Hello! I have received a message from " + event.getAuthor().getName() + ": " + event.getMessage().getContentRaw());

        if (event.getChannelType().equals(ChannelType.TEXT)) {
            if(event.getGuild().getId().equals(Main.guildID)) {
                if(event.getChannel().getId().equals(Main.consoleChannelID)) {
                    if(MAIN_SETTINGS.consoleEnabled) {
                        System.out.println("[Debug]: " + event.getMessage().getContentRaw() + " was fired from console.");

                        try {
                            /*

                            Soon.

                            if(Main.disabledCommands.contains(event.getMessage().getContentRaw())) {
                                Objects.requireNonNull(Objects.requireNonNull(Main.jda.getGuildById(Main.guildID)).getTextChannelById(Main.consoleChannelID))
                                        .sendMessage(":x: This command is disabled.").queue();

                                return;
                            }

                             */

                            Main.rcon.command("/command " + event.getMessage().getContentRaw());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Objects.requireNonNull(Objects.requireNonNull(Main.jda.getGuildById(Main.guildID)).getTextChannelById(Main.consoleChannelID))
                                .sendMessage(":x: Discord console is currently disabled.").queue();
                    }
                } else if(event.getChannel().getId().equals(Main.chatChannelID)) {

                    String userName = event.getMember().getNickname() == null ? event.getMember().getEffectiveName() : event.getMember().getNickname();

                    try {
                        Main.rcon.command("[Discord] " + userName + " » " + event.getMessage().getContentRaw());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
