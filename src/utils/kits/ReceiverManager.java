package utils.kits;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class ReceiverManager {

	private Map<Player, Receiver> list;
	
	public ReceiverManager() {
		this.list = new HashMap<>();
	}
	
	public void addReceiver(Player target, Player demandeur) {
		this.list.put(target, new Receiver(target, demandeur));
	}
	
	public void removeReceiver(Player player) {
		this.list.remove(player);
	}
	
	public Receiver getReceiver(Player player) {
		return this.list.get(player);
	}
}
