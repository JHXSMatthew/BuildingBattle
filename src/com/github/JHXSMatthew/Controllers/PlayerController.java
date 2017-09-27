package com.github.JHXSMatthew.Controllers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.JHXSMatthew.BuildingBattle;
import com.github.JHXSMatthew.Game.GamePlayer;

public class PlayerController {

	private HashMap<String,GamePlayer> players ;
	
	public PlayerController(){
		players = new HashMap<String,GamePlayer>();

		new BukkitRunnable(){
			public void run(){
				Set<String> key = players.keySet();
				Iterator<String> i = key.iterator();
				Bukkit.getLogger().info("=== Clean Task Begin ===" );
				while(i.hasNext()){
					String name = i.next();
					if(Bukkit.getPlayer(name) == null){
						Bukkit.getLogger().info("|__Clean " + name);
						players.remove(name);
					}
				}
				Bukkit.getLogger().info("===       Done       ===" );
			}
		}.runTaskTimer(BuildingBattle.get(), 0, 20 * 60 * 10);
	}
	
	
	public GamePlayer getPlayerByLocation(Location l){
		try{
			Iterator<GamePlayer> lt = players.values().iterator();
			while(lt.hasNext()){
				GamePlayer gp = lt.next();
				if(gp.getGame() == null || gp.getPlot() == null ){
					continue;
				}
				if(gp.getGame().getState() != 2){
					continue;
				}
				if( gp.getPlot().isOutOfBound(l)){
					continue;
				}
				
				return gp;
			}
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		
		return null;
	}
	
	public GamePlayer newPlayer(Player player){
		GamePlayer p = new GamePlayer(player);
		players.put(player.getName(), p);
		
		return p;
	}
	
	public void removePlayer(GamePlayer p){
		if(p.getGame() != null) p.getGame().GameQuit(p);
		players.remove(p.get().getName());
	}
	
	public GamePlayer getPlayer(Player p){
		if(!players.containsKey(p.getName())){
			return newPlayer(p);
		}
		return players.get(p.getName());
	}
	
	public void setMessageTo(String msg){
		Iterator<Entry<String, GamePlayer>> ilter = players.entrySet().iterator();
		while(ilter.hasNext()){
			GamePlayer p = ilter.next().getValue();
			if(p.getGame() == null){
				p.get().sendMessage(msg);
			}
		}
		
		Set<?> ks = players.keySet();
		
		StringBuffer output = new StringBuffer();
		Iterator<?> ilt = ks.iterator();
		if(ilt.hasNext()){
			output.append(ilt.next() );
		}
		while(ilt.hasNext()){
			output.append(",");
			output.append(ilt.next().toString());
		}
		System.out.print(output.toString());
	}
	
}
