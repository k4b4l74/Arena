package kabal.arena.plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import com.nijikokun.bukkit.Permissions.Permissions;

public class PermissionModule {

	//Logger
	private static final Logger log = Logger.getLogger( "Minecraft" );
	
	//List of arena permissions
	private static final String ARENA_ADMIN = "ar.admin";
	
	//Permission plugin here
	private boolean permissionPluginEnabled = false;
	
	//Map of command / permission
	private Map<String, String> permissions = null;
	
	//Unique instance
	private static PermissionModule instance;
	
	//Private Constructor
	private PermissionModule(PluginManager pluginManager) {
		permissions = new HashMap<String, String>();
		
		permissions.put(CommandList.CMD_ADD_BLUEPLAYER, ARENA_ADMIN);
		permissions.put(CommandList.CMD_ADD_REDPLAYER, ARENA_ADMIN);
		permissions.put(CommandList.CMD_REMOVE_PLAYER, ARENA_ADMIN);
		permissions.put(CommandList.CMD_GAME_START, ARENA_ADMIN);
		permissions.put(CommandList.CMD_GAME_STOP, ARENA_ADMIN);
		permissions.put(CommandList.CMD_SET_ROUNDLIMIT, ARENA_ADMIN);
		permissions.put(CommandList.CMD_SET_SPECSPAWN, ARENA_ADMIN);
		
		permissionPluginEnabled = (pluginManager.getPlugin("Permissions") != null);
		if (!permissionPluginEnabled) {
			log.warning("Arena permissions system not enabled ! Using default.");
		}
	}
	
	public static PermissionModule getInstance(PluginManager pluginManager) {
		if (instance == null) {
			instance = new PermissionModule(pluginManager);
		}
		return instance;
	}
	
	/**
	 * Return true, if the player can execute the command
	 * @param command the command
	 * @param player the player
	 * @return boolean
	 */
	public boolean getPermission(String command, Player player) {
		if(permissionPluginEnabled && Permissions.Security.permission(player, permissions.get(command))) {
			return true;
		} else if(!permissionPluginEnabled && player.isOp()) {
			return true;
		}
		return false;		
	}
}
