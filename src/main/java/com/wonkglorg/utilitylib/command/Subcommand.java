package com.wonkglorg.utilitylib.command;

import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class Subcommand extends Arguments{
	
	public boolean executeCommand(CommandSender sender, String[] args, int offset) {
		this.args = args;
		this.parentCount = offset;
		return execute(sender, args);
	}
	
	public List<String> executeTabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args, int offset) {
		this.args = args;
		this.parentCount = offset;
		return tabComplete(sender, command, alias, args);
	}
	
	public abstract boolean execute(CommandSender sender, String[] args);
	
	public abstract List<String> tabComplete(CommandSender sender, org.bukkit.command.Command command, String alias, String[] args);
	
}