# Factorio-Discord-Relay
Factorio-Discord-Relay (FDR) is a .jar (NOT a mod) which allows you to connect your Factorio server to discord (via discord bot) and relay information from Factorio to Discord.


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


### FDR is NOT a mod, it can be ran from anywhere.


---


# Usage

When everything is setup, you can talk in the chat channel you specified to talk in-game via discord.
You can also talk in the console channel you specified to execute commands in the console via discord.


---


# Licence


<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Creative Commons License" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />This work is licensed under a <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International License</a>.
