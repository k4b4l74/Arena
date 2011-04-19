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
				//TELEPORT TO SPECTATOR SPAWN
				arenaGame.getSpawnHandler().onPlayerRespawn_teleportToSpectatorSpawn(arenaPlayer, event);
			}
		}
	}
	
	@Override
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		ArenaPlayer arenaPlayer = arenaGame.getArenaPlayer(player);
		if (arenaPlayer != null && arenaGame.isGameStarted()) {
			//TELEPORT TO SPECTATOR SPAWN
			arenaGame.getSpawnHandler().onPlayerJoin_teleportToSpectatorSpawn(arenaPlayer, event);
		}
	}
	
	@Override
	public void onPlayerQuit(PlayerQuitEvent event) {
		Player player = event.getPlayer();
		ArenaPlayer arenaPlayer = arenaGame.getArenaPlayer(player);
		if (arenaPlayer != null) {
			if (arenaGame.isGameStarted()) {
				//MARK HIM AS DEAD
				arenaGame.getPlayerHandler().onPlayerQuit_markDead(arenaPlayer, event);
			} else {
				//PLAYER QUIT BEFORE GAME START, REMOVE HIM FROM THE TEAM LIST
				arenaGame.getPlayerHandler().onPlayerQuit_removePlayerOffTeam(arenaPlayer, event);
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
