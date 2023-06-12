package me.rocketbot.buttons;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.rocketbot.RocketBotButton;
import me.rocketbot.lavaplayer.GuildMusicManager;
import me.rocketbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class AddButton implements RocketBotButton {
    //DECLARATION OF BUTTONS-PLAYLISTS
    Button pivo = Button.primary("pivo", "PIVO");
    Button rap = Button.primary("rap", "RAP");
    Button phonk = Button.primary("phonk", "PHONK");
    Button chill = Button.primary("chill", "CHILL");
    Button wave = Button.primary("wave", "WAVE");
    @Override
    public String getId() {
        return "add";
    }

    @Override
    public void execute(ButtonInteractionEvent event) {
        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        AudioTrackInfo info = guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack().getInfo();

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(13401854);
        embedBuilder.setTitle("Adding to ROCKET PLAYLIST");
        embedBuilder.setAuthor("Name: " + info.title);
        event.replyEmbeds(embedBuilder.build())
                .addActionRow(
                        pivo,
                        rap,
                        phonk,
                        chill,
                        wave)
                .setEphemeral(true).queue();

    }
}
