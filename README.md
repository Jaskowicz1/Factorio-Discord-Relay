# Factorio-Discord-Relay
Factorio-Discord-Relay (FDR) is a .jar (NOT a Factorio mod) which allows you to connect your Factorio server to discord (via discord bot) and relay information from Factorio to Discord.

# This is now old and deprecated!
Please use my [newest version here](https://github.com/Jaskowicz1/fdr-remake). I simply do not want to maintain this version anymore as it deserves to be remade in a better way.


Documentation sections listed here:
 - Requirements
 - Installation
 - Usage
 - Errors
 - Credit
 - Licence

---


# Requirements

- A Factorio server.
- A discord bot with a valid token.
- one channel for console messages
- one channel for chat messages
- Rcon enabled on the Factorio server (```/opt/factorio-init/config contains EXTRA_BINARGS at the very bottom. Add "--rcon-port <port> --rcon-password <password>" within EXTRA_BINARGS```).

(chat and console messages can be the same but may cause conflict).


---


# Installation

Download the latest .jar from the releases tab.

Once downloaded, place it anywhere within your pc. FDR will tell you that the config file has been created successfully (if no errors occur). Once it has, edit the config to match the values.

Make sure your discord bot is in your server and has permissions to see and chat in the channels (ID form) you gave it.

Once those values are edited correctly, save the config and run the server. If everything is setup correctly, you should see the bot come online!

Take a look at the usage section for an example of the config.


### FDR is NOT a mod for Factorio, it can be ran from anywhere by anything (if done correctly).


---


# Usage

When everything is setup, you can talk in the chat channel you specified to talk in-game via discord.
You can also talk in the console channel you specified to execute commands in the console via discord.

Example config:
```
botToken: ************************
chatID: 676062796890374189
consoleID: 676064233183772702
guildID: 652367853302972427
serverOutFilePath: /opt/factorio/server.out
rconHost: 127.0.0.1
rconPort: 27015
rconPassword: **********
disableDiscordConsole: false
ignoreStartWarning: false
cleanMessages: false
```

`cleanMessages` will disable achievements within your factorio server. If you wish to keep achievements on, I highly recommend you keep `cleanMessages` as false.

If you have any issues, look at the Errors section. If it doesn't list your issue or you still don't understand, feel free to write a ticket under the `Issues` tab.


---


# Errors

There are different types of errors you may get, or issues where you don't even get an error and the program cuts out.

### Actual errors

ConnectionTimedOut - Rcon failed to connect in time or couldn't find the Rcon server. Check the port and/or host.

AuthenticationException - Credentials for Rcon are incorrect. Correct the credentials.

IOException - Either Rcon failed to send a message or a file threw an error being read. If it's the case of files (the program should say), check the permissions of that file and the program.

NumberFormatException - The thing you put in that was meant to be a number (most likely rconPort) was not a valid number.

### Hidden errors

Bot loads but doesn't work? - Check your channels. There may be two of the same channel name. Somehow, this causes issues (No idea how as the IDs should be different but oh well) so just make sure to look out for it.

Console isn't being disabled, even though it should be disabled? - make sure `true` is spelt correctly. `disableDiscordConsole` will be ignored if the value is not `false` or `true` (NOT case-sensitive).


---


# Credit

Thank you to 'jschmidt-1' for cleaning up ServerTask. It has made things much easier and cleaner.
Thank you to 'AGuyNamedJens' for the Pull Request to fix achievements, making servers still eligible for achievements.

A major thanks to https://github.com/Kronos666/rkon-core for the Rcon code.


---


# Licence


<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.
