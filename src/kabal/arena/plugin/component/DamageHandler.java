package kabal.arena.plugin.component;

import org.bukkit.event.entity.EntityDamageEvent;

import kabal.arena.player.ArenaPlayer;
import kabal.arena.plugin.ArenaGame;

public class DamageHandler implements ArenaHandler {

	private ArenaGame arenaGame;
	
	public DamageHandler(ArenaGame aArenaGame) {
		arenaGame = aArenaGame;
	}
	
	@Override
	public void init() {

	}

	@Override
	public void destroyed() {

	}
	
	/*
	 * FUNCTION CALLED BY LISTENER
	 * 
	 */
	public void onEntityDamage_changeDamage(ArenaPlayer player, EntityDamageEvent event) {
		if (player.isDead()) {
			event.setCancelled(true);
		} else {
			
		}
	}

	/*
	 * UTIL FUNCTIONS
	 */
	
}
