package kabal.arena.plugin.component;


import java.util.Iterator;
import java.util.List;

import kabal.arena.player.ArenaPlayer;
import kabal.arena.plugin.ArenaGame;

public class RoundHandler implements ArenaHandler {

	private Integer currentRound = null;
	private ArenaGame arenaGame;
	
	public RoundHandler(ArenaGame aArenaGame) {
		arenaGame = aArenaGame;
	}
	
	@Override
	public void init() {
		currentRound = 0;
		arenaGame.setGameStarted(true);
	}

	@Override
	public void destroyed() {
		currentRound = 0;
		arenaGame.setGameStarted(false);
	}
	
	/*
	 * FUNCTION CALLED BY LISTENER
	 * 
	 */
	public boolean onEntityDeath_isATeamWin() {
		boolean teamWin = false;
		if (isRedTeamWin() || isBlueTeamWin()) {
			currentRound++;
			teamWin = true;
		}
		return teamWin;
	}
	
	public boolean onEntityDeath_isGameFinished() {
		boolean gameFinished = false;
		if (currentRound == arenaGame.getRoundlimit()) {
			gameFinished = true;
			destroyed();
		}
		return gameFinished;
	}
	
	
	/*
	 * UTIL FUNCTION
	 */
	
	
	private boolean isRedTeamWin() {
		return isTeamAllPlayerDead(arenaGame.getBlueTeam());
	}
	
	private boolean isBlueTeamWin() {
		return isTeamAllPlayerDead(arenaGame.getRedTeam());
	}
	
	private boolean isTeamAllPlayerDead(List<ArenaPlayer> team) {
		boolean teamAllDead = false;
		int dead = 0;
		for (Iterator<ArenaPlayer> iterator = team.iterator(); iterator.hasNext();) {
			ArenaPlayer arenaPlayer = (ArenaPlayer) iterator.next();
			if (arenaPlayer.isDead()) {
				dead++;
			}
		}
		if (dead == team.size()) {
			teamAllDead = true;
		}
		return teamAllDead;
	}
}
