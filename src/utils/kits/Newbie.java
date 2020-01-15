package utils.kits;

import org.bukkit.entity.Player;

public class Newbie {

	private boolean canMove;
	private Player player;
	private int choice;
	private boolean chooseFriend;
	
	public Newbie(Player player) {
		this.player = player;
		this.canMove = false;
		this.chooseFriend = false;
		this.choice = -1;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public boolean isCanMove() {
		return canMove;
	}
	
	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}
	
	public int getChoice() {
		return choice;
	}
	
	public void setChoice(int choice) {
		this.choice = choice;
	}
	
	public boolean isChooseFriend() {
		return chooseFriend;
	}
	
	public void setChooseFriend(boolean chooseFriend) {
		this.chooseFriend = chooseFriend;
	}
}
