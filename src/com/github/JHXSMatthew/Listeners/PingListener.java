package com.github.JHXSMatthew.Listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class PingListener implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPing(ServerListPingEvent evt){
		evt.setMotd(ChatColor.GREEN + "开放中");
	}
	
}
