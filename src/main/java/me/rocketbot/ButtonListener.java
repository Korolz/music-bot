package me.rocketbot;

import me.rocketbot.buttons.ChoosePlaylistsButton;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ButtonListener extends ListenerAdapter {
    private List<RocketBotButton> buttons = new ArrayList<>();
    private List<String> playlists = new ArrayList<>(Arrays.asList("pivo", "rap", "phonk", "chill", "wave"));//list of playlists NOTE: ADD ID OF BUTTON HERE IF ADDING NEW PLAYLIST
    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        for(RocketBotButton button : buttons){
            if(button.getId().equals(event.getButton().getId())) {
                button.execute(event);
                return;
            }
            for(String playlist : playlists){
                if(event.getButton().getId().equals(playlist)){
                    buttons.get(0).execute(event);
                }
            }
        }
    }
    public void add(RocketBotButton button) {
    buttons.add(button);
}
}