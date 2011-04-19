package kabal.arena.listener;

import kabal.arena.player.ArenaPlayer;
import kabal.arena.plugin.ArenaGame;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

public class ArenaPlayerListener extends PlayerListener {

	private ArenaGame arenaGame;
	
	public ArenaPlayerListener(ArenaGame aArenaGame) {
		arenaGame = aArenaGame;
	}

	@Override
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		ArenaPlayer arenaPlayer = arenaGame.getArenaPlayer(player);
		if (arenaPlayer != null && arenaGame.isGameStarted()) {
			//CHECK IF PLAYER IS DEAD, IF YES HE BECOME AN OBSERVER !
			if (arenaGame.getPlayerHandler().onPlayerRespawn_isPlayerDied(arenaPlayer)) {
				//TODO: TELEPORT HIM TO THE OBSERVER SPAWN POINT AS A GHOST
			}
		}
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		ArenaPlayer arenaPlayer = arenaGame.getArenaPlayer(player);
		if (arenaPlayer != null && arenaGame.isGameStarted()) {
			//TODO: TELEPORT HIM TO THE OBSERVER SPAWN AS A GHOST
		}
	}
	
	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		ArenaPlayer arenaPlayer = arenaGame.getArenaPlayer(player);
		if (arenaPlayer != null) {
			if (arenaGame.isGameStarted()) {
				//TODO: PLAYER QUIT AND GAME IS LAUNCHED, MARK HIM AS DEAD
			} else {
				//TODO: PLAYER QUIT BEFORE GAME START, REMOVE HIM FROM THE TEAM LIST
			}
		}
	}
	
	@Override
	public void onPlayerEggThrow(PlayerEggThrowEvent event) {
		Player player = event.getPlayer();
		ArenaPlayer arenaPlayer = arenaGame.getArenaPlayer(player);
		if (arenaPlayer != null && arenaGame.isGameStarted()) {
			
		}
	}
	
	@Override
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		ArenaPlayer arenaPlayer = arenaGame.getArenaPlayer(player);
		if (arenaPlayer != null && arenaGame.isGameStarted()) {
			
		}
	}
}
