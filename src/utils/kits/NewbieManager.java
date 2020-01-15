package utils.kits;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

public class NewbieManager {

	private Map<Player, Newbie> list;
	
	public NewbieManager() {
		this.list = new HashMap<>();
	}
	
	public void addNewbie(Player player) {
		this.list.put(player, new Newbie(player));
	}
	
	public void removeNewbie(Player player) {
		this.list.remove(player);
	}
	
	public Newbie getNewbie(Player player) {
		return this.list.get(player);
	}
}
