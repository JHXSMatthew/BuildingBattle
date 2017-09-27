package com.github.JHXSMatthew;

import java.util.ArrayList;
import java.util.logging.Logger;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.JHXSMatthew.Controllers.GameController;
import com.github.JHXSMatthew.Controllers.ItemController;
import com.github.JHXSMatthew.Controllers.MapController;
import com.github.JHXSMatthew.Controllers.MySQLController;
import com.github.JHXSMatthew.Controllers.PlayerController;
import com.github.JHXSMatthew.Controllers.QueueController;
import com.github.JHXSMatthew.Controllers.SkullController;
import com.github.JHXSMatthew.Controllers.SubjectController;
import com.github.JHXSMatthew.Controllers.WorldController;
import com.github.JHXSMatthew.Listeners.BlockListener;
import com.github.JHXSMatthew.Listeners.EntityListener;
import com.github.JHXSMatthew.Listeners.GUIListener;
import com.github.JHXSMatthew.Listeners.PingListener;
import com.github.JHXSMatthew.Listeners.PlayerListener;
import com.github.JHXSMatthew.Objects.Message;
import com.github.JHXSMatthew.Tools.NMSHandler;
import com.github.JHXSMatthew.setUp.Config;
import com.github.JHXSMatthew.setUp.Map;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.mcndsj.GameEvent.Events.GameInitReadyEvent;

public class BuildingBattle extends JavaPlugin{
	static Logger logger;
	Map current;
	private static GameController gc;
	private static WorldController wc;
	private static BuildingBattle instance;
	private static MapController mc;
	private static Message msg ;
	private static SubjectController sc;
	private static ItemController ic;
	private static NMSHandler nms;
	private static PlayerController pc;
	private static QueueController qc;
	private static SkullController skuc;
	private static Config config;
	private static MySQLController sql;
	public ArrayList<String> white;
	
	public static Permission permission = null;
	public static Economy economy = null;
	public static Chat chat = null;
	
	public int editMode = 0;
	
	@Override
	public void onEnable(){
		instance = this;


		logger = Logger.getLogger("Minecraft");
		logger.info("建筑战争-by JHXS QQ 68638023");
		saveDefaultConfig();
		
		logger.info("  >>读取全局设置");
		config = new Config();
		config.load();
		logger.info("  <<完成!");

		if(!config.setup){
			logger.info("  >>预备设置");
			logger.info("  >>注册控制器");
			wc = new WorldController();
			logger.info("    --WC done");
			logger.info("  <<完成!");
		}else{

			
			logger.info("  >>注册发包");
		
			
			
			logger.info("  <<完成!");
			
			
			logger.info("  >>注册控制器");
			pc = new PlayerController();
			logger.info("    --PC done");
			mc = new MapController();
			logger.info("    --MC done");
			qc = new QueueController(config.lobby);
			logger.info("    --QC done");
			gc = new GameController();
			logger.info("    --GC done");
			wc = new WorldController();
			logger.info("    --WC done");
			msg = new Message();
			logger.info("    --MSG done");
			sc = new SubjectController();
			logger.info("    --SC done");
			ic = new ItemController();
			logger.info("    --IC done");
			nms = new NMSHandler();
			logger.info("    --NMS done");
			sql = new MySQLController();
			
			skuc = new SkullController();
			logger.info("    --SKUC done");
			try{
				sql.openConnection();
			}catch(Exception e){
				e.printStackTrace();
				logger.info("    --SQL Error");
				sql = null;
			}
			logger.info("    --SQL done");
			logger.info("  <<完成!");
			
			logger.info("  >>注册监视器");
			getServer().getPluginManager().registerEvents(new PlayerListener(), this);
			logger.info("    --Player done");
			getServer().getPluginManager().registerEvents(new BlockListener(), this);
			logger.info("    --Block done");			
			getServer().getPluginManager().registerEvents(new EntityListener(), this);
			logger.info("    --Entity done");			
			getServer().getPluginManager().registerEvents(new GUIListener(), this);
			logger.info("    --GUI done");	
			getServer().getPluginManager().registerEvents(new PingListener(), this);
			logger.info("    --Ping done");	

			
			logger.info("  <<完成!");
			try{
			logger.info("  >>注册权限经济绑定");
			setupEconomy();
			logger.info("    --Eco done");
			setupChat();	
			logger.info("    --Chat done");
			setupPermissions();
			logger.info("    --Perm done");
			logger.info("  <<完成!");
			}catch(Exception e ){
				logger.info("  <<无法加载!");
			}
			
		    getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		
		/*	
		//test mode enable
		
			white = new ArrayList<String>();
			white.add("jhxs");
			
			BufferedReader bf = null;
			try {
				 bf = new BufferedReader(new FileReader(Bukkit.getPluginManager().getPlugin("BuildingBattle").getDataFolder()  +"/"+ "whitelist.yml"));
				
			} catch (FileNotFoundException e) {
				System.err.print("Error, IO exception Dest = "+ Bukkit.getPluginManager().getPlugin("BuildingBattle").getDataFolder()  + "/" + "whitelist.yml");
				e.printStackTrace();
			}
			if(bf == null){
				return;
			}
			String data ;
			try {
				while((data = bf.readLine()) != null){
					white.add(data);
					
				}
			} catch (IOException e) {
				System.err.print("Error, IO exception Dest = "+ Bukkit.getPluginManager().getPlugin("BuildingBattle").getDataFolder() + "\\"+ "whitelist.yml");
				e.printStackTrace();
			}
			
			try {
				bf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			*/
		}
		
		Bukkit.getPluginManager().callEvent(new GameInitReadyEvent());
	}
	@Override
	public void onDisable(){
		gc.disposeAllGame();
		/*
		BufferedWriter bf = null;
		try {
			 bf = new BufferedWriter(new FileWriter(Bukkit.getPluginManager().getPlugin("BuildingBattle").getDataFolder()  +"/"+ "whitelist.yml",false));
			
		} catch (IOException e) {
			System.err.print("Error, IO exception Dest = "+ Bukkit.getPluginManager().getPlugin("BuildingBattle").getDataFolder()  + "/" + "whitelist.yml");
			e.printStackTrace();
		}
		
		
		for(String s : white){
			try {
				bf.write(s + "\n");
			} catch (IOException e) {
				System.err.print("Write exception");
				break;
			}
			
		}
		try {
		
			bf.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
	}
	
    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private boolean setupChat()
    {
        RegisteredServiceProvider<Chat> chatProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.chat.Chat.class);
        if (chatProvider != null) {
            chat = chatProvider.getProvider();
        }

        return (chat != null);
    }

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
	
	public static SkullController getSkuc(){
		return skuc;
	}
	
	public static QueueController getQc(){
		
		return qc;
	}
	public static MySQLController getSQL(){
		return sql;
	}
	
	
	public static PlayerController getPc(){
		
		return pc;
	}
	public static NMSHandler getNms(){
		return nms;
	}
	
	public static ItemController getIc(){
		return ic;
	}
	
	public static SubjectController getSc(){
		return sc;
	}
	
	public static Message getMsg(){
		return msg;
	}
	public static GameController getGc(){
		return gc;
	}
	public static WorldController getWc(){
		return wc;
	}
	
	public static MapController getMc(){
		return mc;
	}
	public  static Logger getlog(){
		return logger;
	}
	
	public static BuildingBattle get(){
		return instance;
	}
	
	public void quitSend(Player p){
		 ByteArrayDataOutput out = ByteStreams.newDataOutput();
		  out.writeUTF("Connect");
		//  System.out.print("YES,YOU ARE SNEt");
		  out.writeUTF("lobby1");
		//  System.out.print("YES,YOU ARE SNEt");
		  // If you don't care about the player
		  // Player player = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
		  // Else, specify them
		  p.sendPluginMessage(this, "BungeeCord", out.toByteArray());
	}
	
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {

		
		if(!commandLabel.equals("bb")){
			return false;
		}
		if(args.length < 1){
			return false;
		}
		
		if((args[0].equals("w") ||args[0].equals("white") ) && sender.hasPermission("lobby.admin") ){
			if(args.length == 2){
				white.add(args[1]);
				sender.sendMessage(args[1]);
			}
			return true;
			
		}

		if(!sender.isOp()){
			return false;
		}

		Player p = (Player)sender;
		
		if(args[0].equals("start")){
			if(pc.getPlayer(p).getGame() == null){
				return false;
			}
			pc.getPlayer(p).getGame().switchState((short)2);
			return true;
		}
		
		
		if(args[0].equals("sha")){
			for(Entity e : p.getWorld().getEntities()){
				if(! (e instanceof Player)){
					e.remove();
				}	
			}
			return true;
		}
		if(args[0].equals("jiawin")){
			if(args.length< 2){
				return false;
			}
			pc.getPlayer(p).getPD().AddWins();
			return true;
		}
		
		if(args[0].equals("edit")){
			if(editMode == 1){
				editMode = 0;
				p.sendMessage("0");
			}else{
				editMode = 1;
				p.sendMessage("1");
			}
			return true;
		}
		

		
		if(args[0].equals("setup")){
			if(args.length > 1){
				this.current = new Map(args[1]);
				sender.sendMessage(args[1] + " 创建完成");
			}else{
				sender.sendMessage("起个名字大爷");
			}
			return true;
			
		}
		if(args[0].equals("setlobby")){
			if(this.current == null){
				sender.sendMessage("do setup first");
			}else{
				this.current.lobby = p.getLocation();
				sender.sendMessage(" lobby done");
			}
			return true;
		}
		
		if(args[0].equals("setloc")){
			if(args.length > 1){
				try{
				if(Integer.parseInt(args[1]) == 1){
					this.current.up =  p.getLocation();
					p.sendMessage(" up ok");
					
				}else{
					this.current.down = p.getLocation();
					p.sendMessage(" down ok");
				}
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				p.sendMessage("1 = up  2 = down");
			}
			return true;
		}
		
		if(args[0].equals("setplayer")){
			if(args.length > 1){
				try{
					current.Player = Integer.parseInt(args[1]);
					p.sendMessage(" player ok " +  current.Player);
					
				}catch(Exception e ){
					e.printStackTrace();
				}
			}else{
				p.sendMessage("number is required");
			}
			return true;
			
		}
		
		if(args[0].equals("settime")){
			if(args.length> 1){
				try{
					current.time = Integer.parseInt(args[1]);
					p.sendMessage(" done time " + current.time + "  second");
					
				}catch (Exception e){
					e.printStackTrace();
				}
			}
			
			
		}
		if(args[0].equals("save")){
			if(current.down != null && current.up != null && current.time != 0 && current.Player != 0 && current.lobby != null ){
				current.save();
				getConfig().set("setUp", true);
			}else{
				if(current.down == null){
					p.sendMessage("down null");
				}
				if(current.up == null){
					p.sendMessage("up null");
				}
				if(current.Player == 0){
					p.sendMessage("Player null");
				}
				if(current.time == 0){
					p.sendMessage("time null");
				}
				if(current.lobby == null){
					p.sendMessage("lobby null");
				}
				
			}
			return true;
		}
		
		if(args[0].equals("qlobby")){
			if(args.length == 1){
				config.lobby= p.getLocation();
				config.save();
			}else{
				p.sendMessage("hehe, taiduole");
			}
			
			return true;
		}
		
		return false;
	}
}
