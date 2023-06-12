package me.rocketbot.commands;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.rocketbot.RocketBotCommand;
import me.rocketbot.lavaplayer.GuildMusicManager;
import me.rocketbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.List;

public class NowPlaying implements RocketBotCommand {

    Button addButton = Button.primary("add", Emoji.fromUnicode("U+1F4E5"));
    Button removeButton = Button.primary("remove", Emoji.fromUnicode("U+1F4E4"));
    Button loopButton = Button.secondary("loop", Emoji.fromUnicode("U+1F502"));

    @Override
    public String getName() {
        return "now";
    }

    @Override
    public String getDescription() {
        return "Shows info what is now broadcasting";
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
        if(guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack() == null) {
            event.reply("I am not playing anything").setEphemeral(true).queue();
            return;
        }
        AudioTrackInfo info = guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack().getInfo();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(6113681);
        embedBuilder.setTitle("Currently Playing"); //try russian
        embedBuilder.setDescription("**Name:** `" + info.title + "`");
        embedBuilder.appendDescription("\n**Author:** `" + info.author + "`");
        event.replyEmbeds(embedBuilder.build()).addActionRow(
                addButton,
                removeButton,
                loopButton,
                Button.link(info.uri, "Link")).setEphemeral(true).queue();
    }
}
