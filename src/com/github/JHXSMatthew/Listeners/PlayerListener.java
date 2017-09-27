package com.github.JHXSMatthew.Listeners;



import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.github.JHXSMatthew.BuildingBattle;
import com.github.JHXSMatthew.Game.Game;
import com.github.JHXSMatthew.Game.GamePlayer;
import com.mcndsj.GameEvent.Events.GameInitReadyEvent;
import com.mcndsj.GameEvent.Events.GameStartEvent;

public class PlayerListener implements Listener {
	private ArrayList<String> noSpam;
	private boolean stop = false;
	
	public PlayerListener(){
		noSpam = new ArrayList<String>();
		
		new BukkitRunnable(){
			@Override
			public void run() {
				noSpam.clear();
			}
			
		}.runTaskTimerAsynchronously(BuildingBattle.get(), 20, 100);
	}
	
	/*
	@EventHandler
	public void onLogin(PlayerLoginEvent evt){
		if(!BuildingBattle.get().white.contains(evt.getPlayer().getName())){
			evt.disallow(Result.KICK_OTHER, "您不在内测名单内，请关注我们的群公告！");
		}
	
	}
	*/
	
	@EventHandler
	public void onJoin(PlayerJoinEvent evt  ){
		
		/*
		
		if(!BuildingBattle.get().white.contains(p.getName())){
			p.kickPlayer("您不在内测白名单内，请您耐心等待！");
		}
		*/
		Player p = evt.getPlayer();
		BuildingBattle.getPc().newPlayer(p);
	//	BuildingBattle.getQc().joinQ(gp);
		evt.setJoinMessage("");
		
		if(Bukkit.getOnlinePlayers().size() >= Bukkit.getMaxPlayers()){
			if(!stop){
				Bukkit.getPluginManager().callEvent(new GameStartEvent());
				stop = true;
			}

		}
	}
	
	
	@EventHandler
	public void onMove(PlayerMoveEvent evt){
		GamePlayer p = BuildingBattle.getPc().getPlayer(evt.getPlayer());
		if(p == null){
			System.out.print("PLAYER MOVE ERROR , NULL Player");
			BuildingBattle.getPc().newPlayer(evt.getPlayer());
			return;
		}
		
		Game g = p.getGame();
		
		if(g  == null){
			return;
		}
		if(g.getState() < 2 ) return;
		
		if(g.getState() == 2){
			if(p.getPlot().isOutOfBound(evt.getTo())){
				Location l = p.get().getLocation().clone();
				if(p.getPlot().getUp().getY() < l.getY()){
					l.setY(l.getY() - 2);
					p.get().teleport(l);
				}else{
					p.teleportToPlot(p.getPlot());
				}
			
				p.get().sendMessage(BuildingBattle.getMsg().getMsg("cant-get-out-bound"));
				
			}
			
		}else if(g.getState() < 4){
			if(g.getCurrentView().getPlot().isOutOfBound(evt.getTo())){
				Location l = p.get().getLocation().clone();
				if(g.getCurrentView().getPlot().getUp().getY() < l.getY()){
					l.setY(l.getY()- 2);
					p.get().teleport(l);
				}else{
					p.teleportToPlot(g.getCurrentView().getPlot());
				}
				p.get().sendMessage(BuildingBattle.getMsg().getMsg("cant-get-out-bound"));
				
			}
			
		}
			
	}
	
    @EventHandler
    public void onInventoryOpenEvent(InventoryOpenEvent e){
        if (e.getInventory().getHolder() instanceof Chest || e.getInventory().getHolder() instanceof DoubleChest){
            e.setCancelled(true);
        }
    }
	
	@EventHandler
	public void onLeave(PlayerQuitEvent evt ){
		if(Bukkit.getOnlinePlayers().size() <= Bukkit.getMaxPlayers()){
			if(stop){
				Bukkit.getPluginManager().callEvent(new GameInitReadyEvent());
				stop = false;
			}

		}
		evt.setQuitMessage("");
		GamePlayer p = BuildingBattle.getPc().getPlayer(evt.getPlayer());
		if(p == null){
			return;
		}
		
		p.savePD();
		
		if(p.isinQ() == true){
			p.falseQuit = true;
			return;
		}
		
		Game g = p.getGame();
		if(g  == null){
			BuildingBattle.getPc().removePlayer(p);
			return;
		}
		
		g.GameQuit(p);
		p.setGame(null);
		
		BuildingBattle.getPc().removePlayer(p);
		if(Bukkit.getOnlinePlayers().size() <= 1 && BuildingBattle.getGc().count > 200){
			Bukkit.getServer().shutdown();
		}
		
	}
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent evt){
		evt.setCancelled(true);
		
		if(noSpam.contains(evt.getPlayer().getName())){
			return;
		}
		
		noSpam.add(evt.getPlayer().getName());
		
		String gp = BuildingBattle.chat.getPrimaryGroup(evt.getPlayer());
		//String gpp = BuildingBattle.chat.getGroupPrefix("",gp).replace("&", "§");
		String pp = BuildingBattle.chat.getPlayerPrefix(evt.getPlayer()).replace("&", "§");
		String realMsg  = null;
		//if(pp.equals(gpp)){
			realMsg=   pp + ChatColor.DARK_AQUA + evt.getPlayer().getName() + ChatColor.GOLD + " >> " +ChatColor.GRAY + evt.getMessage();
		//}else{
		//    realMsg = gpp + pp + ChatColor.DARK_AQUA + evt.getPlayer().getName() + ChatColor.GOLD + " >> " +ChatColor.GRAY + evt.getMessage();
		//}
	//	
		
		GamePlayer p = BuildingBattle.getPc().getPlayer(evt.getPlayer());
		
		BuildingBattle.getGc().setMessageTo(p.getGame(), realMsg);
	}
	
	
	@EventHandler
	public void onDropItem(PlayerDropItemEvent evt){
		evt.setCancelled(true);
	}

	@EventHandler
	public void onWeatherChange(WeatherChangeEvent evt){
		evt.setCancelled(true);
	}
	
	@EventHandler
	public void onTimeChange(){
		
	}
	
	
	@EventHandler
	public void onBed(PlayerBedEnterEvent evt){
		evt.setCancelled(true);
	}
	
	// give marks
	@EventHandler
	public void playerInteract(PlayerInteractEvent evt){
		if(evt.getPlayer().isOp() && BuildingBattle.get().editMode == 1){
			return;
		}
	
		
		
		GamePlayer p = BuildingBattle.getPc().getPlayer(evt.getPlayer());
		if(p == null){
			System.out.print("PLAYER MOVE ERROR , NULL Player");
			return;
		}
		Game g = p.getGame();
		if(g  == null){
			ItemStack item = evt.getPlayer().getItemInHand();
			if(item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() &&  (evt.getAction().equals(Action.RIGHT_CLICK_BLOCK) || evt.getAction().equals(Action.RIGHT_CLICK_AIR))  ){
				if(BuildingBattle.getIc().isQuitItem(item)){
					BuildingBattle.get().quitSend(p.get());
					return;
				}
			}
			
			if(evt.getAction().equals(Action.PHYSICAL)&& evt.getClickedBlock().getType() == Material.STONE_PLATE && !p.isinQ()){
			    BuildingBattle.getQc().joinQ(p);
			}else{
				evt.setCancelled(true);
		
				return;
			}
		}else{
			if(g.getState() <= 1 ){
				ItemStack item = evt.getPlayer().getItemInHand();
				if(item != null && item.hasItemMeta() && item.getItemMeta().hasDisplayName() &&  (evt.getAction().equals(Action.RIGHT_CLICK_BLOCK) || evt.getAction().equals(Action.RIGHT_CLICK_AIR))  ){
					g.GameQuit(p);
				//	System.out.print("hehe");
					return;
				}
			}
			
			if(g.getState() == 3){
				evt.setCancelled(true);
				if(g.getCurrentView().equals(p)){
				    p.get().sendMessage(BuildingBattle.getMsg().getMsg("could-not-self-vote"));
					return;
				}
				
				if(evt.getAction().equals(Action.RIGHT_CLICK_AIR) || evt.getAction().equals(Action.RIGHT_CLICK_BLOCK)  ){
					if(evt.getPlayer().getItemInHand() != null && !evt.getPlayer().getItemInHand().equals(Material.AIR)){
						ItemStack item = evt.getPlayer().getItemInHand();
						int mark = BuildingBattle.getIc().isVoteItem(item) ;
						if(mark != -1){
							if(p.getVotedMark() == 0){
								g.doMarkChange(p, mark+1, true);
							}else if(p.getVotedMark() !=  mark+1){
								g.doMarkChange(p, mark+1, false);
							}
							
							p.setVoted(mark + 1);
							
						}else{
							return;
						}
					}
				}
			}
		}
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onBanItem(InventoryClickEvent evt){
		if(evt.isCancelled()){
			return;
		}
		
		if(evt.getCursor() != null){
			Material type = evt.getCursor().getType();
			if(type == null){
				return;
			}
			
			if(BuildingBattle.getIc().isBan(type) ){
				evt.setCancelled(true);
				evt.setCursor(new ItemStack(Material.AIR));

				
			}
		}
	}
	
}
