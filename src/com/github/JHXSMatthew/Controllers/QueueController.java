package com.github.JHXSMatthew.Controllers;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.github.JHXSMatthew.BuildingBattle;
import com.github.JHXSMatthew.Game.GamePlayer;
import com.github.JHXSMatthew.Tools.Titles;

public class QueueController {

	private ArrayList<GamePlayer> list;
	private BukkitTask thread = null;
	private Location lobby;
	
	
	public QueueController(Location arg){
		lobby = arg;
		
		
		list = new ArrayList<GamePlayer>();
		thread = new BukkitRunnable(){
			public void run(){
				if(list.size() > 0){
					Iterator<GamePlayer> iter = list.iterator();
					while(iter.hasNext()){
						GamePlayer temp = iter.next();
						if(temp.falseQuit){
							BuildingBattle.getPc().removePlayer(temp);
							iter.remove();
							continue;
						}
						
						if(BuildingBattle.getGc().JoinGame(temp)){
							iter.remove();
						}else{
							break;
						}
					}
					iter = null;
					for(GamePlayer gp : list){
						Titles.newTitle(gp.get(), ChatColor.RED  + ChatColor.BOLD.toString()+ "匹配队列","当前匹配池有 "+list.indexOf(gp) + " 人", 20, 40, 20);
					}
				}			
			}
		}.runTaskTimer(BuildingBattle.get(), 100, 100);
	}
	
	
	public void stopThread(){
		thread.cancel();
	}
	
	public void joinQ(GamePlayer p){
		
		if(p.getGame() != null){
			p.getGame().GameQuit(p);
		}
		p.setQ(true);
		p.clearBuffs();
		BuildingBattle.getIc().giveLobbyItem(p);
		p.get().teleport(lobby);
		list.add(p);
		Titles.newTitle(p.get(), ChatColor.RED  + ChatColor.BOLD.toString()+ "匹配队列","当前匹配池有 "+list.indexOf(p) + " 人", 20, 40, 20);
	}
	
	public Location getQlobby(){
		return lobby;
	}
	
	
}
