# Diablo Immortal Discord Notifier Bot

This bot will notify you if certain events are about to start.

### What can the bot do?

- Sends a message when an event is starting or warns you X minutes before the event will start.
- You can set the time when the bot should send the warning message.
- You can create your own notification messages for Shadow War for e.g.
- You can change the language of the bot.

We offer full support on discord: https://discord.gg/9gwK9QdXZG

---

## Installation:

Important: If you have Administrator-Permissions you do not need to have the Bot Admin role!

1. Added the bot to your discord server by
   clicking [here](https://discord.com/oauth2/authorize?client_id=527511535309029407&permissions=8&scope=bot%20applications.commands).
2. Create a role called "Bot Admin". People with that role can control the bot. You can change the role using
   /adminrole.
3. Assign that role to the users which should control the bot or to yourself. People with Admin-Perms do not need the
   role.
4. Use /timezone <timezone> to set your timezone. The bot only supports the GMT-Timezones. You can see what GMT timezone
   matches your time using /timezones.
5. Create a textchannel you like to get the notifications in.
6. Register your created channel using /register.
7. You can see all channel settings using /info.
8. Enable or disable each event notification using /notificaion [event] [on/off (true/false)]
9. You can change what role should get mentioned in your channel using /mentionrole [@Your_Custom_Role].

---

## Commands:

> [] = Optional

> <> = Required

### Commands to modify your notification channel

| Command                                  | Description                                                                     |
|------------------------------------------|---------------------------------------------------------------------------------|
| `/register [TextChannel]`                | Registers a channel as notification channel.                                    |
| `/unregister [TextChannel]`              | Unregisters a channel as notification channel.                                  |
| `/mentionrole [Role] [TextChannel]`      | Changes the role that should get mentioned in your notification channel.        |
| `/notification <GameEvent> <True/False>` | Disables or enables a game notification in the channel you send the command in. |
| `/listnotificaions`                      | List's you all available game notifications.                                    |
| `/info [TextChannel]`                    | Show's you all nessesary informations about a registered notification channel.  |
| `/preset <Preset> <True/False>`          | Applies varius channel settings for your notification channel.                  |

### Commands to modify your server settings

| Command                          | Description                                                               |
|----------------------------------|---------------------------------------------------------------------------|
| `/timezone <TimeZone>`           | Changes the bot's timezone for your server.                               |
| `/config`                        | Show's you all nessesary setting for the bot in your server.              |
| `/language <Language>`           | Changes the bot's language for your server.                               |
| `/server <Setting> <True/False>` | You can disable event or warn messages for your entire server.            |
| `/warntime <Time in minutes>`    | Changes the time when the warn messages is send befor the actual message. |
| `/adminrole <Role>`              | Changed the role which can controll the bot.                              |

### Information or help commands

| Command      | Description                                     |
|--------------|-------------------------------------------------|
| `/help`      | List's you all commands.                        |
| `/install`   | Sends you a message on how to install this bot. |
| `/languages` | Displays all available languages for the bot.   |
| `/timezones` | Displays you all GMT timzones with the time.    |
| `/today`     | Shows you all events for today.                 |
| `/upcoming`  | Shows you all upcoming today events.            |

### Creating own notification messages

| Command                                                 | Description                                                                                                                                                                                                                                                                                                                        |
|---------------------------------------------------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `/createmessage [Weekday] [Time] [Repeating] [Message]` | This allows you to create your own notification message. Weekday is the day when the message should be sent. Time is the time when the message should be sent. Careful: 24-Times-Format! Repeating means if the message should be just send once and then getting deleted or should repeat weekly. Message is your actual message. |
| `/listmessages`                                         | Show's you a list of your messages. Each message will have  it personal ID.                                                                                                                                                                                                                                                        |
| `/deletemessage [ID]`                                   | Deletes one of your own notification  messages.                                                                                                                                                                                                                                                                                    |
| `/messageinfo [ID]`                                     | Show's you information about one of your messages.                                                                                                                                                                                                                                                                                 |
| `/editmessage [ID] [ValueToChange] [NewValue]`          | Allows you to edit your notification message.                                                                                                                                                                                                                                                                                      |
| `/message [ID] [True/False]`                            | Allows you to enable or disable each of your own notification message.                                                                                                                                                                                                                                                             |

## Languages

We're currently supporting: American-Spanish, Brazilian Portuguese, Bulgarian, Croatian, Danish, Dutch, English, Finnish,
French, German, Greek, Indonesia, Italia, Latvian, Polish, Romanian, Russian, Czech, Slovak, Spanish, Swedish Turkish
German, French, Indonesian, Italian, Polish, Portuguese/Brazilian, Russian, Spanish, Turkish & Ukrainian

---

## Support the project

I open-source almost everything I can, and I try to reply to everyone needing help using these projects. Obviously,
this takes time. You can use this service for free.

However, if you are using this project and happy with it or just want to encourage me to continue creating stuff, there
are few ways you can do it:

- Recommend the bot to others here.
- Starring and sharing the project :rocket:
- You can make donations via PayPal or Patreon. I'll probably invest it in my server ;)
- [PayPal](https://www.paypal.me/pnone)
- [Patreon](https://www.patreon.com/umbreonmajora)

Thanks! <3

---
Runtime Information:

- Written in Java 17

Running the bot with registering every command to every guild:

- screen java -jar Diablo-Immortal-Discord-Notifier-Bot-X.X.X.jar true

Running the bot without registering every command to every guild:

- screen java -jar Diablo-Immortal-Discord-Notifier-Bot-X.X.X.jar false