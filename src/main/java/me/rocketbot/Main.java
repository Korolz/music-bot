package me.rocketbot;

import io.github.cdimascio.dotenv.Dotenv;
import me.rocketbot.buttons.AddButton;
import me.rocketbot.buttons.ChoosePlaylistsButton;
import me.rocketbot.buttons.LoopButton;
import me.rocketbot.buttons.RemoveButton;
import me.rocketbot.commands.Shutdown;
import me.rocketbot.commands.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.exceptions.InvalidTokenException;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {

    private final Dotenv config = Dotenv.load(); //for .env token variable
    private final JDA jda; //for building a bot instance

    public Main() {
        String token = config.get("BOT_TOKEN");

        JDABuilder builder = JDABuilder.createDefault(token);
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setActivity(Activity.playing("CHESS 2")); //insert activity
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_PRESENCES);
        jda = builder.build(); //throws LoginException

        CommandManager manager = new CommandManager();
        manager.add(new Play());
        manager.add(new Shutdown());
        manager.add(new NowPlaying());
        manager.add(new Loop());
        manager.add(new Queue());
        manager.add(new Skip());
        manager.add(new Clear());
        manager.add(new Radio());
        manager.add(new RadioPivo());
        manager.add(new RadioPhonk());
        manager.add(new RadioRap());
        manager.add(new RadioChill());
        manager.add(new RadioWave());
        //manager.add(new List()); //TODO watch in class List

        ButtonListener buttonListener = new ButtonListener();
        buttonListener.add(new ChoosePlaylistsButton());//DO NOT CHANGE THIS POSITION RELATED TO BUTTONLISTENER(SHOULD ALWAYS ADD FIRST)
        buttonListener.add(new AddButton());
        buttonListener.add(new LoopButton());
        buttonListener.add(new RemoveButton());

        jda.addEventListener(manager);

        jda.addEventListener(buttonListener);
    }

    public static void main(String[] args) {
        //setup
        try {
            Main rocketbot = new Main();
        } catch (InvalidTokenException e) {
            System.out.println("ENVIRONMENT ERROR: Provided BOT_TOKEN is invalid! Check the .env file.");
        }
    }

}