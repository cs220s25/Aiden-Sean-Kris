package edu.moravian;

import java.util.Random;
//This class is currently NOT in use as if someone wanted the bot to be more gojo like
//they could just add the messages in the DiscordMessageHandler class
public class GojoMessageStyler {
    // Generic Gojo-style messages for different game actions
    public static String getStartGameMessage(String username) {
        String[] messages = {
                "**" + username + ", you're stepping into my *Infinite Domain*! 🌀 Prepare to lose!**",
                "**Oh? Another challenger? 🥱 This’ll be over faster than a Hollow Purple.**",
                "**" + username + ", let me show you why *throughout heaven and earth, I alone am the strongest*! 💫**",
                "**Heh, " + username + ", welcome to my Blackjack *Infinity*. ♾️ Good luck, you’ll need it!**"
        };
        return messages[new Random().nextInt(messages.length)];
    }

    public static String getBetMessage(String username, int betAmount) {
        String[] messages = {
                "**" + username + " bets " + betAmount + " coins! 🤑 Bold... but futile in my domain!**",
                "**" + betAmount + " coins? That’s cute. Did you bring your *Six Eyes*? 👀**",
                "**" + username + " thinks " + betAmount + " can save them? 💸 This is *Infinity*!**",
                "**Betting " + betAmount + " coins? A drop in the ocean of my endless probability! 🌊♾️**"
        };
        return messages[new Random().nextInt(messages.length)];
    }

    public static String getHitMessage(String username) {
        String[] messages = {
                "**" + username + " draws a card. *Don’t worry, it’s not Hollow Purple.* 🟣**",
                "**Hit me? No, hit *yourself*! This is Blackjack, not sorcery training! 🤜🃏**",
                "**" + username + ", trying to push through my Infinity? That’s adorable! 🌀**",
                "**You’re drawing a card? Sounds like a *Limitless* mistake! 🤯**"
        };
        return messages[new Random().nextInt(messages.length)];
    }

    public static String getBustedMessage(String username) {
        String[] messages = {
                "**BOOM! 💥 " + username + " is busted! Was that a Hollow Purple or just bad luck? 🟣🃏**",
                "**Oops, " + username + " went over 21. *Infinity can’t save you now!* ♾️**",
                "**Crushed under your own greed, " + username + "? Guess you’ve met my *Cursed Technique*! 😏**",
                "**Busted? Maybe next time use your *Domain Expansion*. Oh wait, you don’t have one! 🌀**"
        };
        return messages[new Random().nextInt(messages.length)];
    }

    public static String getStandMessage(String username) {
        String[] messages = {
                "**" + username + " stands! 🃏 Bold move in *my* domain. Let’s see what happens...**",
                "**Standing still won’t save you, " + username + ". Infinity isn’t *that* forgiving. ♾️**",
                "**" + username + " chooses to stand. Let’s call this move: *Standing Purple*. 🟣**",
                "**Bracing yourself, " + username + "? Even *Limitless* can’t predict this outcome! 🤔**"
        };
        return messages[new Random().nextInt(messages.length)];
    }

    public static String getJoinMessage(String username) {
        String[] messages = {
                "**Yo, " + username + "! Welcome to my Blackjack domain! Prepare to witness *Perfection*! 🌀**",
                "**Another challenger? 🤨 " + username + ", this is MY game! Let’s do this! 🃏**",
                "**" + username + " joins the game. Let me show you why I’m untouchable. ♾️✨**",
                "**Hey, " + username + "! You ready to get hit with a Hollow Purple? 🟣💥**"
        };
        return messages[new Random().nextInt(messages.length)];
    }

    public static String getWinMessage(String username, int winAmount) {
        String[] messages = {
                "**What?! " + username + " wins " + winAmount + " coins? Must’ve been a glitch in *Infinity*! 🌀**",
                "**" + username + ", you got lucky. Even *Six Eyes* didn’t see that coming. 👀💸**",
                "**" + winAmount + " coins? Fine, take it! But don’t let it go to your head. 😤🃏**",
                "**" + username + " wins. Enjoy your victory, but remember... I’m *still* Gojo. 💪🟣**"
        };
        return messages[new Random().nextInt(messages.length)];
    }

    public static String getLoseMessage(String username, int lossAmount) {
        String[] messages = {
                "**" + username + " loses " + lossAmount + " coins. *Infinity claims another victim!* ♾️💸**",
                "**Down goes " + username + "! You should’ve known better in my domain! 🃏💥**",
                "**Another player defeated by probability. Guess that’s why they call me the strongest! 🌀😏**",
                "**" + lossAmount + " coins gone! Don’t worry, the pain is *Limitless*! 😜🟣**"
        };
        return messages[new Random().nextInt(messages.length)];
    }

    public static String getTieMessage(String username) {
        String[] messages = {
                "**A tie? Seriously, " + username + "? *Hollow Purple can’t break this stalemate!* 🟣🤝**",
                "**Balance maintained, " + username + ". Just like *Infinity*. ♾️**",
                "**A tie... so boring. I expected more from you, " + username + ". 🥱**",
                "**Neither of us wins? In *my domain*? Unacceptable! 😤🌀**"
        };
        return messages[new Random().nextInt(messages.length)];
    }
}