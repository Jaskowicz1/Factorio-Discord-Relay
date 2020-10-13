package me.jaskowicz.factoriodiscordrelay.Tasks;

import me.jaskowicz.factoriodiscordrelay.Main;
import me.jaskowicz.factoriodiscordrelay.Settings.MAIN_SETTINGS;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import org.apache.commons.io.input.ReversedLinesFileReader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ServerCount extends TimerTask {

    @Override
    public void run() {
        try {
            String count = Main.rcon.command("/players online count");

            //Main.M_SERVER_COUNT = Integer.parseInt(count);

            Main.jda.getPresence().setActivity(Activity.playing(count));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
