package main;

import java.io.File;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import utils.commands.ChangeDefaultSpawnCommand;
import utils.commands.ResetStartCommand;
import utils.events.BlockMovementEvent;
import utils.events.ChatEvent;
import utils.events.NewPlayerConnectEvent;
import utils.events.RespawnEvent;
import utils.kits.NewbieManager;
import utils.kits.ReceiverManager;

public class MainWelcome extends JavaPlugin{

	private final File playerFile = new File(getDataFolder(), "players.yml");
	private final File playerLocation = new File(getDataFolder(), "Locations");
	private static MainWelcome instance;
	
	private static NewbieManager newbieManager;
	private static ReceiverManager receiverManager;
	
	@Override
	public void onEnable() {
		instance = this;
		newbieManager = new NewbieManager();
		receiverManager = new ReceiverManager();
		
		saveDefaultConfig();
		
		if(!this.playerFile.exists()) {
			try {
				this.playerFile.createNewFile();
				FileConfiguration config = YamlConfiguration.loadConfiguration(this.playerFile);
				config.createSection("Players");
				config.save(this.playerFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if(!this.playerLocation.exists())
			this.playerLocation.mkdirs();
		
		commands();
		events();
	}
	
	private void commands() {
		this.getCommand("setwelcomespawn").setExecutor(new ChangeDefaultSpawnCommand());
		this.getCommand("resetwelcomespawn").setExecutor(new ResetStartCommand());
	}

	private void events() {
		this.getServer().getPluginManager().registerEvents(new BlockMovementEvent(), this);
		this.getServer().getPluginManager().registerEvents(new ChatEvent(), this);
		this.getServer().getPluginManager().registerEvents(new NewPlayerConnectEvent(), this);
		this.getServer().getPluginManager().registerEvents(new RespawnEvent(), this);
	}
	
	public static void sendMessage(CommandSender sender, String message) {
		sender.sendMessage(ChatColor.AQUA + "[Welcome] " + ChatColor.WHITE + message);
	}
	
	public void saveNewbie(Player player) {
		FileConfiguration config = YamlConfiguration.loadConfiguration(MainWelcome.getInstance().getPlayerFile());
		config.set("Players." + player.getUniqueId().toString(), "");
		try {
			config.save(getPlayerFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
		File file = new File(getPlayerLocation(), player.getUniqueId().toString() + ".yml");
		if(!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		Location location = safeLocation(player.getLocation());
		config = YamlConfiguration.loadConfiguration(file);
		config.set("Location", location);
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Location safeLocation(Location location) {
		int x = location.getBlockX(), y = location.getBlockY(), z = location.getBlockZ();
		while(location.getWorld().getBlockAt(x, y, z).getType() == Material.AIR) {
			y = y - 1;
		}
		
		return new Location(location.getWorld(), x, y+1, z);
	}
	
	public Location loadNewbieLocation(Player player) {
		File file = new File(getPlayerLocation(), player.getUniqueId().toString() + ".yml");
		if(!file.exists()) {
			return null;
		}
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		Location loc = config.getLocation("Location");
		return loc;
	}
	
	public void removeNewbieLocation(Player player) {
		File file = new File(getPlayerLocation(), player.getUniqueId().toString() + ".yml");
		if(file.exists()) {
			file.delete();
		}
		
		FileConfiguration config = YamlConfiguration.loadConfiguration(getPlayerFile());
		config.set("Players."+player.getUniqueId().toString(), null);
		try {
			config.save(getPlayerFile());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static MainWelcome getInstance() {
		return instance;
	}
	
	public File getPlayerFile() {
		return playerFile;
	}
	
	public static NewbieManager getNewbieManager() {
		return newbieManager;
	}
	
	public static ReceiverManager getReceiverManager() {
		return receiverManager;
	}
	
	public File getPlayerLocation() {
		return playerLocation;
	}
}
