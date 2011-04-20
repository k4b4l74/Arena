package kabal.arena.item;

import kabal.arena.player.ArenaPlayer;

import org.bukkit.event.entity.EntityDamageByProjectileEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class Arrow {

	public static void setDamage(ArenaPlayer arenaPlayer, EntityDamageEvent event) {
		if (event instanceof EntityDamageByProjectileEvent) {
			EntityDamageByProjectileEvent edpe = (EntityDamageByProjectileEvent) event;
			if (edpe.getProjectile() instanceof org.bukkit.entity.Arrow) {
				int dmgFireDelaySeconds = 3;
				event.setDamage(2);
				arenaPlayer.getPlayer().setFireTicks(dmgFireDelaySeconds * 20);
			}
		}
	}
}
