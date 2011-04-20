package kabal.arena.plugin.component;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

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
		Server server = deadPlayer.getPlayer().getServer();
		ChatColor color = ChatColor.RED;
		if (!arenaGame.isOnRedTeam(deadPlayer)) {
			color = ChatColor.BLUE;
		}
		
		addDeath(deadPlayer);
		
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent) event;
			
			if ((damageEvent.getDamager() instanceof Player) &&
				(damageEvent.getEntity() instanceof Player)) {					
				Player damagerPlayer = (Player) damageEvent.getDamager();
				ArenaPlayer attackerPlayer = arenaGame.getArenaPlayer(damagerPlayer);
				if (arenaGame.sameTeam(deadPlayer, attackerPlayer)) {
					//TK
					server.broadcastMessage(color+attackerPlayer.getPlayer().getName()+" TK his friend "+deadPlayer.getPlayer().getName());
				} else {
					addKill(attackerPlayer);
					server.broadcastMessage(color+deadPlayer.getPlayer().getName()+" has been killed by "+attackerPlayer.getPlayer().getName());
				}
			}
		} else {
			server.broadcastMessage(color+deadPlayer.getPlayer().getName()+" has been killed ");
		}
	}
	
	public void onEntityDeath_showScore(ArenaPlayer deadPlayer, EntityDeathEvent event) {
		Server server = deadPlayer.getPlayer().getServer();
		server.broadcastMessage(ChatColor.GREEN + "Scores");
		for (Iterator<ArenaPlayer> iterator = scores.keySet().iterator(); iterator.hasNext();) {
			ArenaPlayer arenaPlayer = iterator.next();
			Map<ScoreType, Integer> score = scores.get(arenaPlayer);
			server.broadcastMessage(ChatColor.GREEN+arenaPlayer.getPlayer().getName()+ " KILL:" + score.get(ScoreType.KILL) + " / DEATH:" + score.get(ScoreType.DEATH));
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
