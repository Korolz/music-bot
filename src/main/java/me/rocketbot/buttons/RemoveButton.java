package me.rocketbot.buttons;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.rocketbot.RocketBotButton;
import me.rocketbot.lavaplayer.GuildMusicManager;
import me.rocketbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RemoveButton implements RocketBotButton {
    @Override
    public String getId() {
        return "remove";
    }

    @Override
    public void execute(ButtonInteractionEvent event) {
        GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
            AudioTrackInfo info = guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack().getInfo();

            String jdbcUrl = "jdbc:sqlite:" + new File("rocket.db").getAbsolutePath();
            String sql = "DELETE FROM tracks WHERE link = ?";

            try(Connection connection = DriverManager.getConnection(jdbcUrl);
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, info.uri);
                    pstmt.executeUpdate();

                    event.reply("Track " + info.title + " has been removed from the RocketPlaylist").queue();
            } catch (SQLException e) {
                event.reply("I guess this track is not in RocketPlaylist").setEphemeral(true).queue();
            }
    }
}
