package utils.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import main.MainWelcome;

public class RespawnEvent implements Listener{

	@EventHandler
	public void onRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		if(player.getBedSpawnLocation() != null) {
			event.setRespawnLocation(player.getBedSpawnLocation());
		}
		else {
			Location loc = MainWelcome.getInstance().loadNewbieLocation(player);
			event.setRespawnLocation(loc);
		}
	}
}
