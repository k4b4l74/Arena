package kabal.arena.plugin.component;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockDamageEvent;

import kabal.arena.block.SpectatorBlock;
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
		
		//TODO: init List<Location> of each team here
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
	public void onBlockDamage_noGlassDamage(BlockDamageEvent event) {
		Block block = event.getBlock();
		if (SpectatorBlock.isSpectatorBlock(block)) {
			event.setCancelled(true);
		}
	}
	
	/*
	 * UTIL FUNCTIONS
	 */
}
