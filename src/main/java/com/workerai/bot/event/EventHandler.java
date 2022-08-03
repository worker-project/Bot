package com.workerai.bot.event;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.time.format.DateTimeFormatter;

public class EventHandler extends ListenerAdapter {
    int ticketBugCount = 0;
    int ticketAiQuestionsCount = 0;
    int ticketOrderQuestionsCount = 0;

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        System.out.println("ButtonInteractionEvent");
        System.out.println(event.getButton().getId());

        switch (event.getButton().getId()) {
            case "bug":
                event.reply("bug").setEphemeral(true)
                        .flatMap(interactionHook -> {
                            try {
                                return event.getGuild().createTextChannel("Ticket" + ++ticketBugCount, event.getGuild().getCategoryById("1004410451486048365"));
                            } catch (InsufficientPermissionException e) {
                                event.getChannel().sendMessageEmbeds(generateErrorMessage(e.getPermission())).queue();
                                return null;
                            }
                        })
                        .flatMap(textChannel -> {
                            try {
                                EmbedBuilder builder = new EmbedBuilder();
                                builder.setTitle("Micheal");
                                builder.setDescription("This is just a test");
                                builder.setColor(Color.GREEN);
                                return textChannel.sendMessageEmbeds(builder.build());
                            } catch (InsufficientPermissionException e) {
                                event.getChannel().sendMessageEmbeds(generateErrorMessage(e.getPermission())).queue();
                                return null;
                            }
                        })
                        .queue();
                break;
            case "question":
                event.reply("Creating question ticket").setEphemeral(true)
                        .flatMap(interactionHook -> {
                            try {
                                return event.getGuild().createTextChannel("Ticket" + ++ticketAiQuestionsCount);
                            } catch (InsufficientPermissionException e) {
                                event.getChannel().sendMessageEmbeds(generateErrorMessage(e.getPermission())).queue();
                                return null;
                            }
                        })
                        .flatMap(textChannel -> {
                            try {
                                EmbedBuilder builder = new EmbedBuilder();
                                builder.setTitle("test");
                                builder.setDescription("This is just a test");
                                builder.setColor(Color.GREEN);
                                return textChannel.sendMessageEmbeds(builder.build());

                            } catch (InsufficientPermissionException e) {
                                event.getChannel().sendMessageEmbeds(generateErrorMessage(e.getPermission())).queue();
                                return null;
                            }
                        })
                        .queue();
                break;
            case "order":
                event.reply("Creating order ticket").setEphemeral(true)
                        .flatMap(interactionHook -> {
                            try {
                                return event.getGuild().createTextChannel("Ticket" + ++ticketOrderQuestionsCount);
                            } catch (InsufficientPermissionException e) {
                                event.getChannel().sendMessageEmbeds(generateErrorMessage(e.getPermission())).queue();
                                return null;
                            }
                        })
                        .flatMap(textChannel -> {
                            try {
                                EmbedBuilder builder = new EmbedBuilder();
                                builder.setTitle("test");
                                builder.setDescription("This is just a test");
                                builder.setColor(Color.GREEN);
                                return textChannel.sendMessageEmbeds(builder.build());

                            } catch (InsufficientPermissionException e) {
                                event.getChannel().sendMessageEmbeds(generateErrorMessage(e.getPermission())).queue();
                                return null;
                            }
                        })
                        .queue();
                break;
        }
    }

    private MessageEmbed generateErrorMessage(Permission permission) {
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Error");
        eb.setDescription("The bot don't have the permission " + permission.getName());
        eb.setColor(Color.RED);
        return eb.build();
    }

    @Override
    public void onReady(ReadyEvent event) {
        System.out.println("Bot is ready!");
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("send")) {
            try {
                System.out.println("So, if i'm here, i have the permissions to send messages");
                TextChannel channel = event.getGuild().getTextChannelById(event.getOption("channel").getAsString());

                EmbedBuilder builder = new EmbedBuilder();
                builder.setTitle("Create a Ticket");
                builder.setDescription("You can create a ticket by clicking on the correct button below");
                builder.setColor(Color.GREEN);
                channel.sendMessageEmbeds(builder.build())
                        .setActionRow(
                                Button.danger("bug", "Bug Report"),
                                Button.primary("questions", "Questions"),
                                Button.success("order", "Order Questions"))//*/
                        .queue();
                event.reply("Message sent to " + channel.getName()).setEphemeral(false)
                        .queue();
            } catch (InsufficientPermissionException e) {
                try {
                    event.getChannel().sendMessageEmbeds(generateErrorMessage(e.getPermission())).queue();
                } catch (InsufficientPermissionException e1) {
                    event.getUser().openPrivateChannel().queue(channel -> channel.sendMessage("The bot don't have the permission to send message in this channel").queue());
                }
            }
        } else if (event.getName().equals("ping")) {
            long time = System.currentTimeMillis();
            event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                    .flatMap(v ->
                            event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                    ).queue(); // Queue both reply and edit
        } else if (event.getName().equals("info")) {
            Member member = event.getOption("user").getAsMember();
            event.reply("User: " + member.getEffectiveName() + " (" + member.getId() + ")").setEphemeral(false)
                    .queue();
            EmbedBuilder eb = new EmbedBuilder();

            eb.setTitle(member.getAsMention());
            eb.setAuthor(member.getAsMention(), null, member.getAvatarUrl());
            eb.setColor(new Color(210, 144, 52));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            eb.addField("Date de création du compte", member.getTimeCreated().format(formatter), true);
            eb.addField("Membre depuis le", member.getTimeJoined().format(formatter), true);
            event.getChannel().sendMessageEmbeds(eb.build()).queue();
        }
    }

}
