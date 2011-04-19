package kabal.arena.block;

import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * The material used for the spectator prison
 * @author gettori
 *
 */
public class SpectatorBlock {

	public static boolean isSpectatorBlock(Block block) {
		return (block.getType() == Material.GLASS);
	}
}
