package kabal.arena.plugin.component;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import kabal.arena.player.ArenaPlayer;
import kabal.arena.plugin.ArenaGame;

public class ScoreHandler implements ArenaHandler {

	private enum ScoreType {KILL, DEATH};
	
	private ArenaGame arenaGame;
	private Map<ArenaPlayer, Map<ScoreType, Integer>> scores = null;
	
	public ScoreHandler(ArenaGame aArenaGame) {
		arenaGame = aArenaGame;
	}
	
	@Override
	public void init() {
		scores = new HashMap<ArenaPlayer, Map<ScoreType, Integer>>();
	}

	@Override
	public void destroyed() {
		init();
	}
	
	/*
	 * FUNCTION CALLED BY LISTENER
	 * 
	 */
	public void onEntityDamage_updateScore(ArenaPlayer deadPlayer, EntityDamageEvent event) {
		addDeath(deadPlayer);
		
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent damageEvent = ( EntityDamageByEntityEvent  )event;
			
			if ((damageEvent.getDamager() instanceof Player) &&
				(damageEvent.getEntity() instanceof Player)) {					
				Player damagerPlayer = (Player) damageEvent.getDamager();
				ArenaPlayer attackerPlayer = arenaGame.getArenaPlayer(damagerPlayer);
				if (arenaGame.sameTeam(deadPlayer, attackerPlayer)) {
					//TK
				} else {
					addKill(attackerPlayer);
				}
			}
		}
	}
	
	
	/*
	 * UTIL FUNCTIONS
	 */
	private void addKill(ArenaPlayer player) {
		addScore(player, ScoreType.KILL);
	}
	
	private void addDeath(ArenaPlayer player) {
		addScore(player, ScoreType.DEATH);
	}
	
	
	private void addScore(ArenaPlayer player, ScoreType scoreType) {
		if (scores.containsKey(player)) {
			Map<ScoreType, Integer> score = scores.get(player);
			score.put(scoreType, score.get(scoreType) + 1);
		} else {
			Map<ScoreType, Integer> score = new HashMap<ScoreType, Integer>();
			score.put(scoreType, 1);
			scores.put(player, score);
		}
	}
}
