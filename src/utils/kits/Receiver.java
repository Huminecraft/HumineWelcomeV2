package utils.kits;

import org.bukkit.entity.Player;

public class Receiver {

	private Player player;
	private Player demandeur;
	private boolean validation;
	
	public Receiver(Player target, Player demandeur) {
		this.player = target;
		this.demandeur = demandeur;
		this.validation = false;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public boolean isValidation() {
		return validation;
	}
	
	public void setValidation(boolean validation) {
		this.validation = validation;
	}
	
	public Player getDemandeur() {
		return demandeur;
	}
}
