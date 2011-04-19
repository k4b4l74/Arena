package kabal.arena.listener;

import kabal.arena.player.ArenaPlayer;
import kabal.arena.plugin.ArenaGame;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;

public class ArenaEntityListener extends EntityListener {

	private ArenaGame arenaGame;
	
	public ArenaEntityListener(ArenaGame aArenaGame) {
		arenaGame = aArenaGame;
	}
	
	@Override
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		
		ArenaPlayer arenaPlayer = arenaGame.getArenaPlayer(entity);
		if (arenaPlayer != null && arenaGame.isGameStarted()) {
			// CHANGE DAMAGE IF NECESSARY
			arenaGame.getDamageHandler().onEntityDamage_changeDamage(arenaPlayer, event);
			
			// RESOLVE DAMAGE ON PLAYER.
			boolean willDie = arenaGame.getPlayerHandler().onEntityDamage_proceedDamage(arenaPlayer, event);
			if (willDie) {
				//UPDATE THE SCORE IF NECESSARY
				arenaGame.getScoreHandler().onEntityDamage_updateScore(arenaPlayer, event);
			}
		}
	}
	
	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		
		ArenaPlayer arenaPlayer = arenaGame.getArenaPlayer(entity);
		if (arenaPlayer != null && arenaGame.isGameStarted()) {
			// THE PLAYER DIED
			arenaGame.getPlayerHandler().onEntityDeath_playerDied(arenaPlayer);
			
			// CHECK IF A TEAM WIN THE ROUND
			if (arenaGame.getRoundHandler().onEntityDeath_isATeamWin()) {
				
				//CHECK IF GAME IS FINISHED
				if (arenaGame.getRoundHandler().onEntityDeath_isGameFinished()) {
					
					//TODO: SHOW SCORE
					
				}
				
				//MAKE PLAYERS ALIVE
				arenaGame.getPlayerHandler().onEntityDeath_playersAlive();
				
				//TODO: FORCE RESPAWN ALL PLAYERS TO THEIR SPAWN POINT
				
				
			}
		}
	}
}
