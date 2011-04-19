package kabal.arena.plugin;

import kabal.arena.player.ArenaPlayer;
import kabal.arena.plugin.component.DamageHandler;
import kabal.arena.plugin.component.PlayerHandler;
import kabal.arena.plugin.component.RespawnHandler;
import kabal.arena.plugin.component.RoundHandler;
import kabal.arena.plugin.component.ScoreHandler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.mysql.jdbc.StringUtils;

public class ArenaGame {
	private Integer roundlimit = null;
	private Location spectatorLocation = null;
	
	private boolean gameStarted = false;
	private List<ArenaPlayer> redTeam = null;
	private List<ArenaPlayer> blueTeam = null;
	
	private RoundHandler roundHandler = null;
	private RespawnHandler spawnHandler = null;
	private ScoreHandler scoreHandler = null;
	private DamageHandler damageHandler = null;
	private PlayerHandler playerHandler = null;
	
	private static ArenaGame instance = null;
	
	public ArenaGame() {
		//Default value
		roundlimit = 10;
		
		redTeam = new ArrayList<ArenaPlayer>();
		blueTeam = new ArrayList<ArenaPlayer>();
		
		roundHandler = new RoundHandler(this);
		spawnHandler = new RespawnHandler(this);
		scoreHandler = new ScoreHandler(this);
		damageHandler = new DamageHandler(this);
		playerHandler = new PlayerHandler(this);
	}
	
	public static ArenaGame getInstance() {
		if (instance == null) {
			instance = new ArenaGame();
		}
		return instance;
	}

	/*
	 * 
	 * ON COMMAND FUNCTIONS
	 * 
	 */
	
	/**
	 * Called when a player adds a new player in red team.
	 * @param sender
	 * @param args
	 */
	public void proceedCommandAddRed(Player sender, String[] args, Server server) {
		String playerName = args[0];
		if (StringUtils.isEmptyOrWhitespaceOnly(playerName)) {
			sender.sendMessage(ChatColor.RED + "Player name is empty.");
		} else {
			if (isGameStarted()) {
				sender.sendMessage(ChatColor.RED + "Game is started, you can't add new player");
			} else {
				Player player = server.getPlayer(playerName);
				if (player == null) {
					sender.sendMessage(ChatColor.RED + "Player " + playerName + " is unknown.");
				} else {
					if (getArenaPlayer(player) != null) {
						sender.sendMessage(ChatColor.RED + "Player " + playerName + " is already an Arena Player.");
					} else {
						ArenaPlayer arenaPlayer = new ArenaPlayer(player);
						redTeam.add(arenaPlayer);
						sender.sendMessage(ChatColor.GREEN + "Player " + playerName + " added to the RED TEAM.");
					}
				}
			}		
		}
	}
	
	/**
	 * Called when a player adds a new player in blue team.
	 * @param sender
	 * @param args
	 */
	public void proceedCommandAddBlue(Player sender, String[] args, Server server) {
		String playerName = args[0];
		if (StringUtils.isEmptyOrWhitespaceOnly(playerName)) {
			sender.sendMessage(ChatColor.RED + "Player name is empty.");
		} else {
			if (isGameStarted()) {
				sender.sendMessage(ChatColor.RED + "Game is started, you can't add new player");
			} else {
				Player player = server.getPlayer(playerName);
				if (player == null) {
					sender.sendMessage(ChatColor.RED + "Player " + playerName + " is unknown.");
				} else {
					if (getArenaPlayer(player) != null) {
						sender.sendMessage(ChatColor.RED + "Player " + playerName + " is already an Arena Player.");
					} else {
						ArenaPlayer arenaPlayer = new ArenaPlayer(player);
						blueTeam.add(arenaPlayer);
						sender.sendMessage(ChatColor.GREEN + "Player " + playerName + " added to the BLUE TEAM.");
					}
				}
			}		
		}			
	}
	
	/**
	 * Called when a player removes a player.
	 * @param sender
	 * @param args
	 */
	public void proceedCommandRemovePlayer(Player sender, String[] args, Server server) {
		String playerName = args[0];
		if (StringUtils.isEmptyOrWhitespaceOnly(playerName)) {
			sender.sendMessage(ChatColor.RED + "Player name is empty.");
		} else {		
			if (isGameStarted()) {
				sender.sendMessage(ChatColor.RED + "Game is started, you can't remove a player");
			} else {
				Player player = server.getPlayer(playerName);
				if (getArenaPlayer(player) == null) {
					sender.sendMessage(ChatColor.RED + "Player " + playerName + " is not an Arena Player.");
				} else {
					boolean foundPlayer = false;
					for (Iterator<ArenaPlayer> iterator = redTeam.iterator(); iterator.hasNext();) {
						ArenaPlayer arenaPlayer = iterator.next();
						if (arenaPlayer.getPlayer().getName().equals(player.getName())) {
							foundPlayer = true;
							iterator.remove();
							break;
						}
					}
					
					if (!foundPlayer) {
						for (Iterator<ArenaPlayer> iterator = blueTeam.iterator(); iterator.hasNext();) {
							ArenaPlayer arenaPlayer = iterator.next();
							if (arenaPlayer.getPlayer().getName().equals(player.getName())) {
								foundPlayer = true;
								iterator.remove();
								break;
							}
						}
					}
					
					if (foundPlayer) {
						sender.sendMessage(ChatColor.RED + "Player " + playerName + " is no more an Arena Player.");
					}
				}
			}		
		}
	}
	
	/**
	 * Called when a player starts the game.
	 * @param sender
	 * @param args
	 */
	public void proceedCommandStartGame(Player sender, String[] args) {
		if (isGameStarted()) {
			sender.sendMessage(ChatColor.RED + "Game is started, you can't rerestart the game. Stop the game before.");
		} else {
			if (spectatorLocation == null) {
				sender.sendMessage(ChatColor.RED + "You need to set a spectator respawn point.");
			} else {
			
				damageHandler.init();
				playerHandler.init();
				roundHandler.init();
				scoreHandler.init();
				spawnHandler.init(spectatorLocation);
				
				setGameStarted(true);
			}
		}					
	}
	
	/**
	 * Called when a player stops the game.
	 * @param sender
	 * @param args
	 */
	public void proceedCommandStopGame(Player sender, String[] args) {
		if (!isGameStarted()) {
			sender.sendMessage(ChatColor.RED + "There is no game started to stop.");
		} else {
			damageHandler.destroyed();
			playerHandler.destroyed();
			roundHandler.destroyed();
			scoreHandler.destroyed();
			spawnHandler.destroyed();
			
			setGameStarted(false);
		}			
	}
	
	/**
	 * Called when a player set a new value for the roundlimit.
	 * @param sender
	 * @param args
	 */
	public void proceedCommandSetRoundLimit(Player sender, String[] args) {
		if (isGameStarted()) {
			sender.sendMessage(ChatColor.RED + "Game is started, you can't set now the roundlimit");
		} else {
			try {
				Integer number = Integer.valueOf(roundlimit);
				if (number > 0) {
					this.roundlimit = number;
					sender.sendMessage(ChatColor.AQUA + "Round Limit is now: " + args[0] + ".");
				} else {
					//Not a valid number
					throw new Exception();
				}
			} catch (Exception e) {
				sender.sendMessage(ChatColor.RED + args[0] + " is not a valid number.");
			}
		}			
	}
	
	/*
	 * 
	 * UTILS FUNCTIONS
	 * 
	 */
	
	/**
	 * Return true if 2 players belongs to the same team.
	 * @return boolean
	 */
	public boolean sameTeam(ArenaPlayer p1, ArenaPlayer p2) {
		boolean sameRedTeam = (redTeam.contains(p1) && redTeam.contains(p2));
		boolean sameBlueTeam = (blueTeam.contains(p1) && blueTeam.contains(p2));
		return (sameRedTeam || sameBlueTeam);
	}
	
	/**
	 * Call this for get the ArenaPlayer. If null this is not an arena player
	 * @param player the player
	 * @return ArenaPlayer != null if the arena player exist
	 */
	public ArenaPlayer getArenaPlayer(Player player) {
		ArenaPlayer foundArenaPlayer = null;
		if (redTeam != null) {
			for (Iterator<ArenaPlayer> iterator = redTeam.iterator(); iterator.hasNext();) {
				ArenaPlayer arenaPlayer = iterator.next();
				if (arenaPlayer.getPlayer().getName().equals(player.getName())) {
					foundArenaPlayer = arenaPlayer;
					break;
				}
			}
		}
		if (blueTeam != null && foundArenaPlayer == null) {
			for (Iterator<ArenaPlayer> iterator = blueTeam.iterator(); iterator.hasNext();) {
				ArenaPlayer arenaPlayer = iterator.next();
				if (arenaPlayer.getPlayer().getName().equals(player.getName())) {
					foundArenaPlayer = arenaPlayer;
					break;
				}
			}			
		}
		return foundArenaPlayer;
	}
	
	/**
	 * Call this for get the ArenaPlayer. If null this is not an arena player
	 * @param entity the entity
	 * @return ArenaPlayer != null if the arena player exist
	 */
	public ArenaPlayer getArenaPlayer(Entity entity) {
		ArenaPlayer foundArenaPlayer = null;
		if (redTeam != null) {
			for (Iterator<ArenaPlayer> iterator = redTeam.iterator(); iterator.hasNext();) {
				ArenaPlayer arenaPlayer = iterator.next();
				if (arenaPlayer.getPlayer().getEntityId() == (entity.getEntityId())) {
					foundArenaPlayer = arenaPlayer;
					break;
				}
			}
		}
		if (blueTeam != null && foundArenaPlayer == null) {
			for (Iterator<ArenaPlayer> iterator = blueTeam.iterator(); iterator.hasNext();) {
				ArenaPlayer arenaPlayer = iterator.next();
				if (arenaPlayer.getPlayer().getEntityId() == (entity.getEntityId())) {
					foundArenaPlayer = arenaPlayer;
					break;
				}
			}			
		}
		return foundArenaPlayer;
	}
	
	/**
	 * Call this for know if an arenaPlayer belongs to Red Team or not
	 * @param aPlayer
	 * @return
	 */
	public boolean isOnRedTeam(ArenaPlayer aPlayer) {
		boolean isOnRedTeam = false;
		if (redTeam != null) {
			for (Iterator<ArenaPlayer> iterator = redTeam.iterator(); iterator.hasNext();) {
				ArenaPlayer arenaPlayer = iterator.next();
				if (arenaPlayer.getPlayer().getName().equals(aPlayer.getPlayer().getName())) {
					isOnRedTeam = true;
					break;
				}
			}
		}		
		return isOnRedTeam;
	}
	
	/**
	 * Return the state of the game
	 * @return true of game has started
	 */
	public boolean isGameStarted() {
		return gameStarted;
	}

	/*
	 * SETTERS
	 */
	public void setGameStarted(boolean gameStarted) {
		this.gameStarted = gameStarted;
	}	
	
	/*
	 * GETTERS
	 */
	public Integer getRoundlimit() {
		return roundlimit;
	}
	
	public List<ArenaPlayer> getRedTeam() {
		return redTeam;
	}

	public List<ArenaPlayer> getBlueTeam() {
		return blueTeam;
	}

	public RoundHandler getRoundHandler() {
		return roundHandler;
	}

	public RespawnHandler getSpawnHandler() {
		return spawnHandler;
	}

	public ScoreHandler getScoreHandler() {
		return scoreHandler;
	}

	public DamageHandler getDamageHandler() {
		return damageHandler;
	}

	public PlayerHandler getPlayerHandler() {
		return playerHandler;
	}
}
