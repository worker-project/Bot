package com.workerai.bot;

import com.workerai.bot.event.EventHandler;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Bot {
	private static JDA jda;

	public static void main(String[] args) throws Exception {
		JDABuilder builder = JDABuilder.createDefault(args[0]);
		builder.setActivity(Activity.playing("WorkerAI"));
		builder.addEventListeners(new EventHandler());
		builder.enableIntents(GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT);
		jda = builder.build();

		jda.awaitReady();
		Runtime.getRuntime().addShutdownHook(new Thread(() -> jda.shutdownNow()));

		jda.upsertCommand("send", "envoit le message pour créer un ticket dans le salon spécifié").addOption(OptionType.CHANNEL, "channel", "le salon dans lequel envoyer le message", true).queue();
		jda.upsertCommand("ping", "Calculate ping of the bot").queue();
		jda.upsertCommand("info", "Show info about the specified user").addOption(OptionType.USER, "user", "the user").queue();
	}


	public static JDA getJda() {
		return jda;
	}
}
