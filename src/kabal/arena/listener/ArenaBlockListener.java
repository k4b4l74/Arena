package kabal.arena.listener;

import kabal.arena.player.ArenaPlayer;
import kabal.arena.plugin.ArenaGame;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;

public class ArenaBlockListener extends BlockListener {

	private ArenaGame arenaGame;
	
	public ArenaBlockListener(ArenaGame aArenaGame) {
		arenaGame = aArenaGame;
		
	}
	
	@Override
	public void onBlockDamage(BlockDamageEvent event) {
		Player player = event.getPlayer();
		ArenaPlayer arenaPlayer = arenaGame.getArenaPlayer(player);
		if (arenaPlayer != null && arenaGame.isGameStarted()) {
			//NO DAMAGE ON SPECTATOR BLOCK PRISON TYPE
			arenaGame.getSpawnHandler().onBlockDamage_noGlassDamage(event);
		}
	}
}
