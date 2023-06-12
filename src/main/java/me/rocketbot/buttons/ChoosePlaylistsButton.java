package me.rocketbot.buttons;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.rocketbot.RocketBotButton;
import me.rocketbot.lavaplayer.GuildMusicManager;
import me.rocketbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.io.File;
import java.sql.*;

public class ChoosePlaylistsButton implements RocketBotButton {
    @Override
    public String getId() {
        return "choose-playlist";
    }

    @Override
    public void execute(ButtonInteractionEvent event) {
        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
        AudioTrackInfo info = guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack().getInfo();

        String chosen = event.getButton().getId();
        String jdbcUrl = "jdbc:sqlite:" + new File("rocket.db").getAbsolutePath();
        String sql = "INSERT INTO tracks(title,link,type) VALUES(?,?,?)";

        try(Connection connection = DriverManager.getConnection(jdbcUrl);
            PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, info.title);
            pstmt.setString(2, info.uri);
            pstmt.setString(3, chosen);
            pstmt.executeUpdate();

            event.reply("Track " + info.title + " added to "+ chosen.toUpperCase()).queue();
        } catch (SQLException e) {
            event.reply("An error occurred while adding the track to RocketPlaylist.").setEphemeral(true).queue();
        }
    }
}
