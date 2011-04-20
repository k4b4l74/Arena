package kabal.arena.listener;

import kabal.arena.player.ArenaPlayer;
import kabal.arena.plugin.ArenaGame;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.plugin.Plugin;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

public class ArenaEntityListener extends EntityListener {

	private ArenaGame arenaGame;
	private Plugin plugin;
	
	public ArenaEntityListener(ArenaGame aArenaGame, Plugin aPlugin) {
		arenaGame = aArenaGame;
		plugin = aPlugin;
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
	public void onEntityDeath(final EntityDeathEvent event) {
		Entity entity = event.getEntity();
		
		ArenaPlayer arenaPlayer = arenaGame.getArenaPlayer(entity);
		if (arenaPlayer != null && arenaGame.isGameStarted()) {
			// THE PLAYER DIED
			arenaGame.getPlayerHandler().onEntityDeath_playerDied(arenaPlayer);
			
			// CHECK IF A TEAM WIN THE ROUND
			if (arenaGame.getRoundHandler().onEntityDeath_isATeamWin(arenaPlayer)) {
				
				//CHECK IF GAME IS FINISHED
				if (arenaGame.getRoundHandler().onEntityDeath_isGameFinished()) {
					
					//SHOW SCORE
					arenaGame.getScoreHandler().onEntityDeath_showScore(arenaPlayer, event);
				}
				
				plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable() {
					public void run() {
						//MAKE PLAYERS ALIVE
						arenaGame.getPlayerHandler().onEntityDeath_playersAlive();
						
						//GIVE THEM STUFF & HEALTH
						arenaGame.getPlayerHandler().onEntityDeath_giveMaxHealtAndPlayerInventory(event);
						
						//FORCE RESPAWN ALL PLAYERS TO THEIR SPAWN POINT
						arenaGame.getSpawnHandler().onEntityDeath_teleportAllPlayersToSpawn(event);
					}
				}, 20 * 2);
			}
		}
	}
}
