package kabal.arena.plugin;

import java.util.logging.Logger;

import kabal.arena.listener.ArenaBlockListener;
import kabal.arena.listener.ArenaEntityListener;
import kabal.arena.listener.ArenaPlayerListener;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Arena extends JavaPlugin {

	//Logger
	private static final Logger log = Logger.getLogger("Minecraft");
	
	//Arena game
	private static final ArenaGame arenaGame = ArenaGame.getInstance();
	
	//Listener
	private static final ArenaBlockListener blockListener = new ArenaBlockListener(arenaGame);
	private static final ArenaEntityListener entityListener = new ArenaEntityListener(arenaGame);
	private static final ArenaPlayerListener playerListener = new ArenaPlayerListener(arenaGame);
	
	@Override
	public void onDisable() {
		log.info( "Arena mod disabled" );	
	}

	@Override
	public void onEnable() {
		PluginManager pluginMgr = getServer().getPluginManager();
		
		//Register for events
		pluginMgr.registerEvent(Event.Type.BLOCK_DAMAGE, blockListener, Event.Priority.High, this);
		
		pluginMgr.registerEvent(Event.Type.ENTITY_DAMAGE, entityListener, Event.Priority.Normal, this);
		pluginMgr.registerEvent(Event.Type.ENTITY_DEATH, entityListener, Event.Priority.Normal, this);
		
		pluginMgr.registerEvent(Event.Type.PLAYER_RESPAWN, playerListener, Event.Priority.Normal, this);
		pluginMgr.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Normal, this);
		pluginMgr.registerEvent(Event.Type.PLAYER_JOIN, playerListener, Event.Priority.Normal, this);
		pluginMgr.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Event.Priority.Normal, this);
		pluginMgr.registerEvent(Event.Type.PLAYER_EGG_THROW, playerListener, Event.Priority.Low, this);
		
		log.info( "Arena mod enabled" );
	} 

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		String commandName = command.getName();
		PermissionModule permissionModule = PermissionModule.getInstance(getServer().getPluginManager());
		Player player = (Player) sender;
		
		//ADD BLUE PLAYER
		if (commandName.equalsIgnoreCase(CommandList.CMD_ADD_BLUEPLAYER)) {
			if (permissionModule.getPermission(CommandList.CMD_ADD_BLUEPLAYER, player)) {
				arenaGame.proceedCommandAddBlue(player, args, getServer());
			} else {
				player.sendMessage(ChatColor.RED + "You don't have permissions.");
			}
			return true;
		}
		
		//ADD RED PLAYER
		if (commandName.equalsIgnoreCase(CommandList.CMD_ADD_REDPLAYER)) {
			if (permissionModule.getPermission(CommandList.CMD_ADD_REDPLAYER, player)) {
				arenaGame.proceedCommandAddRed(player, args, getServer());			
			} else {
				player.sendMessage(ChatColor.RED + "You don't have permissions.");
			}
			return true;
		}
		
		//REMOVE A PLAYER
		if (commandName.equalsIgnoreCase(CommandList.CMD_REMOVE_PLAYER)) {
			if (permissionModule.getPermission(CommandList.CMD_REMOVE_PLAYER, player)) {
				arenaGame.proceedCommandRemovePlayer(player, args, getServer());
			} else {
				player.sendMessage(ChatColor.RED + "You don't have permissions.");
			}
			return true;
		}		
		
		//START THE GAME
		if (commandName.equalsIgnoreCase(CommandList.CMD_GAME_START)) {
			if (permissionModule.getPermission(CommandList.CMD_GAME_START, player)) {
				arenaGame.proceedCommandStartGame(player, args);
			} else {
				player.sendMessage(ChatColor.RED + "You don't have permissions.");
			}
			return true;
		}
		
		//STOP THE GAME
		if (commandName.equalsIgnoreCase(CommandList.CMD_GAME_STOP)) {
			if (permissionModule.getPermission(CommandList.CMD_GAME_STOP, player)) {
				arenaGame.proceedCommandStopGame(player, args);			
			} else {
				player.sendMessage(ChatColor.RED + "You don't have permissions.");
			}
			return true;
		}
		
		//SET THE ROUND LIMIT
		if (commandName.equalsIgnoreCase(CommandList.CMD_SET_ROUNDLIMIT)) {
			if (permissionModule.getPermission(CommandList.CMD_SET_ROUNDLIMIT, player)) {
				arenaGame.proceedCommandSetRoundLimit(player, args);					
			} else {
				player.sendMessage(ChatColor.RED + "You don't have permissions.");
			}
			return true;
		}
		
		return false;
	}
}
