package utils.events;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.aypi.utils.Timer;
import com.aypi.utils.inter.TimerFinishListener;

import main.MainWelcome;
import utils.kits.Newbie;
import utils.kits.Receiver;

public class ChatEvent implements Listener {

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Newbie newbie = MainWelcome.getNewbieManager().getNewbie(event.getPlayer());
		Receiver receiver = MainWelcome.getReceiverManager().getReceiver(event.getPlayer());
		if(newbie != null) {
			if(newbie.getChoice() == -1) {
				makeChoice(newbie, event.getMessage());
				event.setCancelled(true);
			}
			else if(newbie.isChooseFriend()) {
				choiceFriend(newbie, event.getMessage());
				event.setCancelled(true);
			}
		}
		
		if(receiver != null) {
			acceptMessage(receiver, event.getMessage());
			event.setCancelled(true);
		}
	}
	
	private void acceptMessage(Receiver receiver, String message) {
		if(message.equals("accept")) {
			MainWelcome.getInstance().getServer().getScheduler().runTask(MainWelcome.getInstance(), new Runnable() {
				public void run() {
					receiver.setValidation(true);
					MainWelcome.sendMessage(receiver.getPlayer(), "Vous avez accepté(e) la demande");
					MainWelcome.sendMessage(receiver.getDemandeur(), receiver.getPlayer().getName() + " a accepté la demande");
					receiver.getDemandeur().getPlayer().teleport(new Location(receiver.getPlayer().getWorld(), receiver.getPlayer().getLocation().getX(), 200.0, receiver.getPlayer().getLocation().getZ()));
					Newbie newbie = MainWelcome.getNewbieManager().getNewbie(receiver.getDemandeur());
					if(newbie != null) {
						playEffect(newbie);
						MainWelcome.getInstance().saveNewbie(newbie.getPlayer());
					}
					MainWelcome.getReceiverManager().removeReceiver(receiver.getPlayer());
					MainWelcome.getNewbieManager().removeNewbie(newbie.getPlayer());
				}
			});
			
		}
	}

	private void choiceFriend(Newbie newbie, String message) {
		if(!message.isEmpty()) {
			if(isNumber(message)) {
				if(Integer.parseInt(message) == 0) {
					newbie.setChooseFriend(false);
					newbie.setChoice(-1);
					MainWelcome.sendMessage(newbie.getPlayer(), "Comment voulez-vous commencer l'aventure ?");
					MainWelcome.sendMessage(newbie.getPlayer(), "Tapez 1, Se téléporter au centre de la map");
					MainWelcome.sendMessage(newbie.getPlayer(), "Tapez 2, Téléportation aléatoire");
					MainWelcome.sendMessage(newbie.getPlayer(), "Tapez 3, Se téléporter sur un ami");
				}
			}
			else {
				Player target = serverContains(message);
				if(target != null) {
					MainWelcome.getReceiverManager().addReceiver(target, newbie.getPlayer());
					Receiver receiver = MainWelcome.getReceiverManager().getReceiver(target);
					
					MainWelcome.sendMessage(newbie.getPlayer(), "Envoie de la demande de téléportation à " + message);
					MainWelcome.sendMessage(target, newbie.getPlayer().getName() + "a fait une demande pour se téléporter sur vous pour commencer le jeu");
					MainWelcome.sendMessage(target, "Ecris \"accept\" pour téléporter " + newbie.getPlayer().getName() + " sur toi");
					MainWelcome.sendMessage(target, "Pour refuser, tu as juste à ignorer");
					
					MainWelcome.getInstance().getServer().getScheduler().runTask(MainWelcome.getInstance(), new Runnable() {
						public void run() {
							Timer timer = new Timer(MainWelcome.getInstance(), 10, new TimerFinishListener() {
								
								@Override
								public void execute() {
									if(!receiver.isValidation()) {
										MainWelcome.sendMessage(newbie.getPlayer(), target.getName() + " a refusé la téléportation");
										MainWelcome.sendMessage(receiver.getPlayer(), "Le délai est terminé");
										MainWelcome.getReceiverManager().removeReceiver(target);
										
										MainWelcome.sendMessage(newbie.getPlayer(), "Entrez le pseudo de votre ami: ");
										MainWelcome.sendMessage(newbie.getPlayer(), "ou tapez 0, pour changer de choix");
									}
									
									MainWelcome.getReceiverManager().removeReceiver(target);
								}
							});
							timer.start();
						}
					});
				}
				else {
					MainWelcome.sendMessage(newbie.getPlayer(), "Joueur introuvable, est-il connecté ?");
					MainWelcome.sendMessage(newbie.getPlayer(), "Entrez le pseudo de votre ami: ");
					MainWelcome.sendMessage(newbie.getPlayer(), "ou tapez 0, pour changer de choix");
				}
			}
		}
	}
	
	private Player serverContains(String message) {
		for(Player player : MainWelcome.getInstance().getServer().getOnlinePlayers()) {
			if(player.getName().equals(message)) return player;
		}
		return null;
	}

	private void makeChoice(Newbie newbie, String message) {
		if(!message.isEmpty()) {
			if(isNumber(message)) {
				int n = Integer.parseInt(message);
				if(n == 1)
					first_choice(newbie);
				else if(n == 2)
					second_choice(newbie);
				else if(n == 3)
					third_choice(newbie);
				else
					MainWelcome.sendMessage(newbie.getPlayer(), "Veuillez selectionner un chiffre entre 1 et 3");
			}
			else
				MainWelcome.sendMessage(newbie.getPlayer(), "Nombre invalide");
		}
	}
	
	private void first_choice(Newbie newbie) {
		newbie.setChoice(1);
		playEffect(newbie);
		MainWelcome.getInstance().saveNewbie(newbie.getPlayer());
		MainWelcome.getNewbieManager().removeNewbie(newbie.getPlayer());
	}
	
	private void second_choice(Newbie newbie) {
		newbie.setChoice(2);
		
		MainWelcome.getInstance().getServer().getScheduler().runTask(MainWelcome.getInstance(), new Runnable() {
			public void run() {
				newbie.getPlayer().teleport(randomTeleport(newbie.getPlayer()));
				playEffect(newbie);
				MainWelcome.getInstance().saveNewbie(newbie.getPlayer());
				MainWelcome.getNewbieManager().removeNewbie(newbie.getPlayer());
			}
		});
	}
	
	private void third_choice(Newbie newbie) {
		newbie.setChoice(3);
		newbie.setChooseFriend(true);
		
		MainWelcome.sendMessage(newbie.getPlayer(), "Entrez le pseudo de votre ami: ");
		MainWelcome.sendMessage(newbie.getPlayer(), "ou tapez 0 pour changer de choix");
	}
	
	private void playEffect(Newbie newbie) {
		MainWelcome.sendMessage(newbie.getPlayer(), "Bon courage !");
		MainWelcome.getInstance().getServer().getScheduler().runTask(MainWelcome.getInstance(), new Runnable() {
			public void run() {
				newbie.getPlayer().setGameMode(GameMode.SURVIVAL);
				newbie.setCanMove(true);
				newbie.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (20 * 40), 9999));
			}
		});
	}
	
	public static boolean isNumber(String str) {
		for(int i = 0; i < str.length(); i++) {
			if(str.charAt(i) < '0' || str.charAt(i) > '9')
				return false;
		}
		return true;
	}
	
	public Location randomTeleport(Player player) {
		int x = 10_000 + (int) (Math.random() * ((95_000 - 10_000) + 1));
		int z = 10_000 + (int) (Math.random() * ((95_000 - 10_000) + 1));
		if(Math.random() < 0.4) x = -x;
		if(Math.random() < 0.4) z = -z;
		
		return new Location(player.getWorld(), x, 200.0, z);
	}
}
