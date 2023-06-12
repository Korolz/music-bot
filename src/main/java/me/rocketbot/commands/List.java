package me.rocketbot.commands;

import me.rocketbot.RocketBotCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;


public class List implements RocketBotCommand {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescription() {
        return "Lists all songs, added to RocketPlaylist";
    }

    @Override
    public java.util.List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        //TODO
        //Add link to youtube playlist that is synced with database
    }
}
