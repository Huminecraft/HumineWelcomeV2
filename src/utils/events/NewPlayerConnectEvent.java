package utils.events;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import main.MainWelcome;

public class NewPlayerConnectEvent implements Listener{

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(MainWelcome.getInstance().getPlayerFile());
		
		if(!config.contains("Players." + player.getUniqueId().toString())) {
			MainWelcome.getNewbieManager().addNewbie(player);
			player.teleport(new Location(player.getWorld(), 0.0, 200.0, 0.0));
			player.setGameMode(GameMode.SPECTATOR);
			
			intro(player);
		}
	}
	
	private void intro(Player player) {
		MainWelcome.sendMessage(player, "Bienvenue sur HumineCraft ! :)");
		MainWelcome.sendMessage(player, "Comment voulez-vous commencer l'aventure ?");
		MainWelcome.sendMessage(player, "Tapez 1, se téléporter au centre de la map");
		MainWelcome.sendMessage(player, "Tapez 2, téléportation aléatoire");
		MainWelcome.sendMessage(player, "Tapez 3, se téléporter sur un ami");
	}
}
