package kabal.arena.item;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

public class PickAxe {

	public static void setDamage(EntityDamageEvent event) {
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent edee = (EntityDamageByEntityEvent) event;
			if (edee.getDamager() instanceof Player) {
				Player damager = (Player) edee.getDamager();
				ItemStack itemType = damager.getItemInHand();

				if (itemType.getType().getId() == Material.DIAMOND_PICKAXE
						.getId()
						|| itemType.getType().getId() == Material.IRON_PICKAXE
								.getId()
						|| itemType.getType().getId() == Material.GOLD_PICKAXE
								.getId()
						|| itemType.getType().getId() == Material.WOOD_PICKAXE
								.getId()) {
					event.setDamage(1);
				}
			}

		}
	}
}
