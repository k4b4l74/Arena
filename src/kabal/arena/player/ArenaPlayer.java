package kabal.arena.player;

import org.bukkit.entity.Player;

public class ArenaPlayer {
	private Player player;
	private boolean dead = false;
	
	public ArenaPlayer(Player aPlayer) {
		player = aPlayer;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}
}
