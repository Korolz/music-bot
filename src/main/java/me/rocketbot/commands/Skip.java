package me.rocketbot.commands;

import me.rocketbot.RocketBotCommand;
import me.rocketbot.lavaplayer.GuildMusicManager;
import me.rocketbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class Skip implements RocketBotCommand {
    @Override
    public String getName() {
        return "s";
    }

    @Override
    public String getDescription() {
        return "Will skip a song";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inAudioChannel()) { //checks presence of a member
            event.reply("You need to be in a voice channel").setEphemeral(true).queue();
            return;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if(!selfVoiceState.inAudioChannel()) { //checks presence in a channel
            event.reply("I am not in an audio channel").setEphemeral(true).queue();
            return;
        }

        if(selfVoiceState.getChannel() != memberVoiceState.getChannel()) {
            event.reply("You are not in the same channel as me").setEphemeral(true).queue();
            return;
        }

        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        guildMusicManager.getTrackScheduler().getPlayer().stopTrack();
        event.reply("**Skipped**").setEphemeral(true).queue();
    }
}

