package kabal.arena.listener;

import kabal.arena.player.ArenaPlayer;
import kabal.arena.plugin.ArenaGame;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.plugin.Plugin;

public class ArenaBlockListener extends BlockListener {

	private ArenaGame arenaGame;
	private Plugin plugin;
	
	public ArenaBlockListener(ArenaGame aArenaGame, Plugin aPlugin) {
		arenaGame = aArenaGame;
		plugin = aPlugin;
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
