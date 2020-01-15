package utils.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.MainWelcome;


public class ChangeDefaultSpawnCommand implements CommandExecutor{

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
		
		MainWelcome.sendMessage(sender, "Spawn de "+target.getName()+"redéfini");
		MainWelcome.sendMessage(sender, "Position : " + target.getLocation().toString());
		MainWelcome.sendMessage(target, "Votre spawn est redéfini par " + sender.getName());
		
		MainWelcome.getInstance().saveNewbie(target);
		return true;
	}
	
	private Player serverContains(String message) {
		for(Player player : MainWelcome.getInstance().getServer().getOnlinePlayers()) {
			if(player.getName().equals(message)) return player;
		}
		return null;
	}
		
}
