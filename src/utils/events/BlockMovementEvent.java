package utils.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import main.MainWelcome;
import utils.kits.Newbie;

public class BlockMovementEvent implements Listener{

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Newbie newbie = MainWelcome.getNewbieManager().getNewbie(event.getPlayer());
		if(newbie != null) {
			if(newbie.isCanMove() == false)
				event.setCancelled(true);
		}
	}
}
