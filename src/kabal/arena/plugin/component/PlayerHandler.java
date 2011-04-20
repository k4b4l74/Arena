package kabal.arena.plugin.component;

import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import kabal.arena.player.ArenaPlayer;
import kabal.arena.plugin.ArenaGame;

public class PlayerHandler implements ArenaHandler {

	private ArenaGame arenaGame;
	
	public PlayerHandler(ArenaGame aArenaGame) {
		arenaGame = aArenaGame;
	}
	
	@Override
	public void init() {
		for (Iterator<ArenaPlayer> iterator = arenaGame.getRedTeam().iterator(); iterator.hasNext();) {
			ArenaPlayer arenaPlayer = iterator.next();
			giveMaxHealtAndPlayerInventory(arenaPlayer);
		}
		
		for (Iterator<ArenaPlayer> iterator = arenaGame.getBlueTeam().iterator(); iterator.hasNext();) {
			ArenaPlayer arenaPlayer = iterator.next();
			giveMaxHealtAndPlayerInventory(arenaPlayer);
		}
	}

	@Override
	public void destroyed() {
		for (Iterator<ArenaPlayer> iterator = arenaGame.getRedTeam().iterator(); iterator.hasNext();) {
			ArenaPlayer arenaPlayer = iterator.next();
			arenaPlayer.getPlayer().getInventory().clear();
		}
		
		for (Iterator<ArenaPlayer> iterator = arenaGame.getBlueTeam().iterator(); iterator.hasNext();) {
			ArenaPlayer arenaPlayer = iterator.next();
			arenaPlayer.getPlayer().getInventory().clear();
		}
	}
	
	/*
	 * FUNCTION CALLED BY LISTENER
	 * 
	 */
	public void onEntityDeath_giveMaxHealtAndPlayerInventory(EntityDeathEvent event) {
		init();
	}
	
	public void onPlayerQuit_removePlayerOffTeam(ArenaPlayer arenaPlayer, PlayerQuitEvent event) {
		Server server = arenaPlayer.getPlayer().getServer();
		if (arenaGame.isOnRedTeam(arenaPlayer)) {
			arenaGame.getRedTeam().remove(arenaPlayer);
			server.broadcastMessage(ChatColor.RED + "Player " + arenaPlayer.getPlayer().getName() + " has been removed from RED team");
		} else {
			arenaGame.getBlueTeam().remove(arenaPlayer);
			server.broadcastMessage(ChatColor.BLUE + "Player " + arenaPlayer.getPlayer().getName() + " has been removed from BLUE team");
		}
	}
	
	public void onPlayerQuit_markDead(ArenaPlayer arenaPlayer, PlayerQuitEvent event) {
		arenaPlayer.setDead(true);
	}
	
	public boolean onPlayerRespawn_isPlayerDied(ArenaPlayer arenaPlayer) {
		return arenaPlayer.isDead();
	}
	
	public boolean onEntityDamage_proceedDamage(ArenaPlayer arenaPlayer, EntityDamageEvent event) {
		boolean willDie = false;
		
		int damage = event.getDamage();
		int oldHealth = arenaPlayer.getPlayer().getHealth();
		int newHealth = oldHealth - damage;
		
		if (newHealth < 0) {
			event.setDamage(999);
			willDie = true;
		}
		
		return willDie;
	}
	
	public void onEntityDeath_playerDied(ArenaPlayer arenaPlayer) {
		arenaPlayer.setDead(true);
	}
	
	public void onEntityDeath_playersAlive() {
		setAlivePlayerInTeam(arenaGame.getRedTeam());
		setAlivePlayerInTeam(arenaGame.getBlueTeam());
	}
	
	/*
	 * UTIL FUNCTION
	 */
	private void giveMaxHealtAndPlayerInventory(ArenaPlayer arenaPlayer) {
		Player player = arenaPlayer.getPlayer();
		
		player.setHealth(20);
		
		player.getInventory().clear();
		player.getInventory().addItem(new ItemStack(Material.WOOD_SWORD, 1));
		player.getInventory().addItem(new ItemStack(Material.IRON_PICKAXE, 1));
		player.getInventory().addItem(new ItemStack(Material.BOW, 1));
		player.getInventory().addItem(new ItemStack(Material.ARROW, 10));
		player.getInventory().addItem(new ItemStack(Material.LADDER, 6));
		player.getInventory().addItem(new ItemStack(Material.COOKED_FISH, 1));
		player.getInventory().addItem(new ItemStack(Material.BREAD, 1));
		
	}
	
	private void setAlivePlayerInTeam(List<ArenaPlayer> team) {
		for (Iterator<ArenaPlayer> iterator = team.iterator(); iterator.hasNext();) {
			ArenaPlayer arenaPlayer = (ArenaPlayer) iterator.next();
			arenaPlayer.setDead(false);
		}
	}
}
