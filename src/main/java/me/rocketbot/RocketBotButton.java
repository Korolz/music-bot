package me.rocketbot;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface RocketBotButton {
    //interface of all Buttons
    String getId();
    void execute(ButtonInteractionEvent event);
}
