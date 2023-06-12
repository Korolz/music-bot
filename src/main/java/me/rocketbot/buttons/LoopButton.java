package me.rocketbot.buttons;

import me.rocketbot.RocketBotButton;
import me.rocketbot.lavaplayer.GuildMusicManager;
import me.rocketbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class LoopButton implements RocketBotButton {
    @Override
    public String getId() {
        return "loop";
    }

    @Override
    public void execute(ButtonInteractionEvent event) {
        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        boolean isRepeat = !guildMusicManager.getTrackScheduler().isLoop();
        guildMusicManager.getTrackScheduler().setLoop(isRepeat);
        event.reply("**Repeat** is now " + isRepeat).setEphemeral(true).queue();
    }
}
