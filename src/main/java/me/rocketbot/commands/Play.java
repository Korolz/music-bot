package me.rocketbot.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import me.rocketbot.RocketBotCommand;
import me.rocketbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class Play implements RocketBotCommand {

    @Override
    public String getName() {
        return "p";
    }

    @Override
    public String getDescription() {
        return "Will add a song to a queue"; //try russian
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "link", "link to the song(YouTube, SoundCloud, etc)", true)); //try russian
        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inAudioChannel()) { //checks presence of a member
            event.reply("You need to be in a voice channel").setEphemeral(true).queue(); //try russian
            return;
        }

        Member self = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = self.getVoiceState();

        if(!selfVoiceState.inAudioChannel()) { //checks presence of a member in a same channel
            event.getGuild().getAudioManager().openAudioConnection(memberVoiceState.getChannel());
        } else {
            if(selfVoiceState.getChannel() != memberVoiceState.getChannel()) {
                event.reply("You are not in the same channel as me").setEphemeral(true).queue(); //try russian
                return;
            }
        }

        String link = event.getOption("link").getAsString();
        try {
            new URI(link);
        } catch (URISyntaxException e) {
            link = "ytsearch:" + link;
        }

        PlayerManager playerManager = PlayerManager.get();
        event.reply("**Queuing** [track](" + link + ")").queue();
        playerManager.play(event.getGuild(), link);
    }
}