package utils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;

import main.MainWelcome;

public class ResetStartCommand implements CommandExecutor{


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if(args.length < 1) {
			MainWelcome.sendMessage(sender, "/locate <name>");
			return false;
		}
		
		Player target = serverContains(args[0]);
		
		if(target == null) {
			MainWelcome.sendMessage(sender, "Joueur introuvable");
			return false;
		}
		
		MainWelcome.getInstance().removeNewbieLocation(target);
		MainWelcome.getInstance().getServer().getPluginManager().callEvent(new PlayerJoinEvent(target, ""));
		
		return true;
	}
	
	private Player serverContains(String message) {
		for(Player player : MainWelcome.getInstance().getServer().getOnlinePlayers()) {
			if(player.getName().equals(message)) return player;
		}
		return null;
	}

}
