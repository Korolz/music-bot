package me.rocketbot.commands;

import me.rocketbot.RocketBotCommand;
import me.rocketbot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RadioPivo implements RocketBotCommand {
    @Override
    public String getName() {
        return "radio-pivo";
    }

    @Override
    public String getDescription() {
        return "Podpivas music";
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

        PlayerManager playerManager = PlayerManager.get();

        String jdbcUrl = "jdbc:sqlite:" + new File("rocket.db").getAbsolutePath();
        ArrayList<String> radioPlaylist = new ArrayList<String>();

        try(Connection connection = DriverManager.getConnection(jdbcUrl);
            Statement statement = connection.createStatement()) {

            String sql = "SELECT link FROM tracks WHERE type = 'pivo'";
            ResultSet result = statement.executeQuery(sql);

            while(result.next()){
                radioPlaylist.add(result.getString("link"));
            }
        } catch (SQLException e) {
            System.out.println("DATABASE CONNECTION ERROR");
            e.printStackTrace();
        }

        Collections.shuffle(radioPlaylist);

        for(String link : radioPlaylist){
            playerManager.play(event.getGuild(), link);
        }
        event.reply("PIVO loaded").setEphemeral(true).queue();
    }
}

