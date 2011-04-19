package kabal.arena.plugin.component;

public interface ArenaHandler {

	/**
	 * Called when a player start a new game
	 */
	public void init();
	
	/**
	 * Called when a player stop the game or game finishs by itself
	 */
	public void destroyed();
}
