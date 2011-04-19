package kabal.arena.plugin.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import kabal.arena.block.SpectatorBlock;
import kabal.arena.player.ArenaPlayer;
import kabal.arena.plugin.ArenaGame;

/**
 * Handler for player respawn and spectator respawn
 */
public class RespawnHandler implements ArenaHandler {

	private ArenaGame arenaGame;
	
	private List<Location> redTeamLocations = null;
	private List<Location> blueTeamLocations = null;
	private Location spectatorLocation = null;
	
	public RespawnHandler(ArenaGame aArenaGame) {
		arenaGame = aArenaGame;
	}

	public void init(Location aSpectatorLocation) {
		spectatorLocation = aSpectatorLocation;
		init();
	}
	
	@Override
	public void init() {
		redTeamLocations = new ArrayList<Location>();
		blueTeamLocations = new ArrayList<Location>();
		
		for (Iterator<ArenaPlayer> iterator = arenaGame.getRedTeam().iterator(); iterator.hasNext();) {
			ArenaPlayer arenaPlayer = iterator.next();
			redTeamLocations.add(arenaPlayer.getPlayer().getLocation());
			
		}
		for (Iterator<ArenaPlayer> iterator = arenaGame.getBlueTeam().iterator(); iterator.hasNext();) {
			ArenaPlayer arenaPlayer = iterator.next();
			blueTeamLocations.add(arenaPlayer.getPlayer().getLocation());
		}
	}

	@Override
	public void destroyed() {
		spectatorLocation = null;
		redTeamLocations = new ArrayList<Location>();
		blueTeamLocations = new ArrayList<Location>();
	}
	
	/*
	 * FUNCTION CALLED BY LISTENER
	 * 
	 */
	public void onPlayerJoin_teleportToSpectatorSpawn(ArenaPlayer arenaPlayer, PlayerJoinEvent event) {
		arenaPlayer.getPlayer().teleport(spectatorLocation);
	}
	
	public void onPlayerQuit_teleportToSpectatorSpawn(ArenaPlayer arenaPlayer, PlayerQuitEvent event) {
		arenaPlayer.getPlayer().teleport(spectatorLocation);
	}
	
	public void onPlayerRespawn_teleportToSpectatorSpawn(ArenaPlayer arenaPlayer, PlayerRespawnEvent event) {
		arenaPlayer.getPlayer().teleport(spectatorLocation);
	}
	
	public void onBlockDamage_noGlassDamage(BlockDamageEvent event) {
		Block block = event.getBlock();
		if (SpectatorBlock.isSpectatorBlock(block)) {
			event.setCancelled(true);
		}
	}
	
	public void onEntityDeath_teleportAllPlayersToSpawn(EntityDeathEvent event) {
		List<Location> tempRedLocations = new ArrayList<Location>();
		tempRedLocations.addAll(redTeamLocations);
		for (Iterator<ArenaPlayer> iterator = arenaGame.getRedTeam().iterator(); iterator.hasNext();) {
			ArenaPlayer arenaPlayer = iterator.next();
			teleportPlayerWithRandomLocation(arenaPlayer, tempRedLocations);
		}
		
		List<Location> tempBlueLocations = new ArrayList<Location>();
		tempRedLocations.addAll(blueTeamLocations);
		for (Iterator<ArenaPlayer> iterator = arenaGame.getBlueTeam().iterator(); iterator.hasNext();) {
			ArenaPlayer arenaPlayer = iterator.next();
			teleportPlayerWithRandomLocation(arenaPlayer, tempBlueLocations);
		}
	}
	
	/*
	 * UTIL FUNCTIONS
	 */
	private void teleportPlayerWithRandomLocation(ArenaPlayer player, List<Location> locations) {
		Random r = new Random();
		int randomPosition = r.nextInt(locations.size());
		Location location = locations.get(randomPosition);
		teleportPlayer(player, location);
		locations.remove(randomPosition);
	}
	
	private void teleportPlayer(ArenaPlayer player, Location location) {
		player.getPlayer().teleport(location);
	}
}
