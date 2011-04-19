package kabal.arena.plugin.component;

import java.util.Iterator;
import java.util.List;

import org.bukkit.event.entity.EntityDamageEvent;

import kabal.arena.player.ArenaPlayer;
import kabal.arena.plugin.ArenaGame;

public class PlayerHandler implements ArenaHandler {

	private ArenaGame arenaGame;
	
	public PlayerHandler(ArenaGame aArenaGame) {
		arenaGame = aArenaGame;
	}
	
	@Override
	public void init() {
		// TODO: Give item to each player here
		
	}

	@Override
	public void destroyed() {
		// TODO: Remove all items here
		
	}
	
	/*
	 * FUNCTION CALLED BY LISTENER
	 * 
	 */
	public boolean onPlayerRespawn_isPlayerDied(ArenaPlayer arenaPlayer) {
		return arenaPlayer.isDead();
	}
	
	public boolean onEntityDamage_proceedDamage(ArenaPlayer arenaPlayer, EntityDamageEvent event) {
		boolean willDie = false;
		
		int damage = event.getDamage();
		int oldHealth = arenaPlayer.getPlayer().getHealth();
		int newHealth = oldHealth - damage;
		
		if (newHealth < 0) {
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
	private void setAlivePlayerInTeam(List<ArenaPlayer> team) {
		for (Iterator<ArenaPlayer> iterator = team.iterator(); iterator.hasNext();) {
			ArenaPlayer arenaPlayer = (ArenaPlayer) iterator.next();
			arenaPlayer.setDead(false);
		}
	}
}
