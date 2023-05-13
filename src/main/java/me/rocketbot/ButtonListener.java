package me.rocketbot;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import me.rocketbot.lavaplayer.GuildMusicManager;
import me.rocketbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.io.File;
import java.sql.*;

public class ButtonListener extends ListenerAdapter {

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getButton().getId().equals("loop")) {
            GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
            boolean isRepeat = !guildMusicManager.getTrackScheduler().isLoop();
            guildMusicManager.getTrackScheduler().setLoop(isRepeat);
            event.reply("**Repeat** is now " + isRepeat).setEphemeral(true).queue();
        }
        if (event.getButton().getId().equals("add")){
            GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
            AudioTrackInfo info = guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack().getInfo();

            //String jdbcUrl = "jdbc:sqlite:/C:\\Users\\Korolz\\rocket.db";
            String jdbcUrl = "jdbc:sqlite:" + new File("rocket.db").getAbsolutePath();
            String sql = "INSERT INTO tracks(title,link) VALUES(?,?)";
            try(Connection connection = DriverManager.getConnection(jdbcUrl);
                PreparedStatement pstmt = connection.prepareStatement(sql)) {
                    pstmt.setString(1, info.title);
                    pstmt.setString(2, info.uri);
                    pstmt.executeUpdate();

                    event.reply("Track " + info.title + " has been added to the RocketPlaylist").queue();
            } catch (SQLException e) {
                event.reply("I guess this track is already in RocketPlaylist").setEphemeral(true).queue();
            }
        }
        if (event.getButton().getId().equals("remove")){
            GuildMusicManager guildMusicManager = PlayerManager.get().getGuildMusicManager(event.getGuild());
            AudioTrackInfo info = guildMusicManager.getTrackScheduler().getPlayer().getPlayingTrack().getInfo();

            //String jdbcUrl = "jdbc:sqlite:/C:\\Users\\Korolz\\rocket.db";
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
}