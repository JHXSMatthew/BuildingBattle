package com.github.JHXSMatthew.Game;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.WeatherType;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.github.JHXSMatthew.BuildingBattle;

public class GamePlot {

	private Game currentGame;
	private GamePlayer owner;
	
	private int num;
	private Location up;
	private Location down;
	private Location center;
	private int block_Y_record = -1;
	
	
	// 0 none 1 rain 2 snow
	private int weather = 0;
	private BukkitTask lightningTask = null;
	private Material floorBlock= Material.STAINED_CLAY;
	private byte data = 0;
	private long time = 0;
	
	
	boolean voted = false;
	
	public GamePlot(Game arg,int num){
		this.currentGame = arg;
		this.num = num;
		calPlotLocs();
		
		weather = 0;
	}
	
	
	
	public long getTime(){
		return time;
	}
	
	public void setTime(long arg){
		time = arg;
		owner.get().setPlayerTime(arg, false);
	}
	
	public void setWeather(int arg){
		if(arg == weather ){
			return;
		}
		
		if(weather == 3 && lightningTask!= null){
			lightningTask.cancel();
			lightningTask= null;
		}
	//fix biome 	
		int x_max = up.getBlockX();
		int z_max = up.getBlockZ();
		int x_min  = down.getBlockX();
		int z_min = down.getBlockZ();
		List<Chunk> list = new ArrayList<Chunk>();

		for(int x = x_min ; x <= x_max ; x++){
			for(int z = z_min; z <= z_max ;z ++){
				Location l  = new Location (up.getWorld(), x, up.getY(), z);
				if(!list.contains(l.getChunk())){
					list.add(l.getChunk());
				}
				
			}
		}
		
		if(arg ==2 || weather ==2){
			weather = arg;
			
			for(Chunk c : list){
				c.getWorld().setBiome(c.getX(),c.getZ(), Biome.ICE_PLAINS);
			}
			
		}else{
			for(Chunk c : list){
				c.getWorld().setBiome(c.getX(),c.getZ(), Biome.PLAINS);
			}
			
			weather = arg;
		}
		
		
		switch(weather){
		case 0:
			owner.get().setPlayerWeather(WeatherType.CLEAR);
			break;
		case 1:
			owner.get().setPlayerWeather(WeatherType.DOWNFALL);
			break;
		case 2:
			owner.get().setPlayerWeather(WeatherType.DOWNFALL);
			break;
		case 3:
			owner.get().setPlayerWeather(WeatherType.DOWNFALL);
			lightningTask =	new BukkitRunnable(){
			
				@Override
				public void run() {
					try{
						Random r = new Random();
						Location l = new Location(owner.get().getWorld(), (int) center.getX() + r.nextInt(10) -5, (int) center.getY() - 5 , (int) center.getZ() + r.nextInt(10) -5);
						owner.get().getWorld().strikeLightningEffect(l);
					}catch(Exception e){
						e.printStackTrace();
						cancel();
					}/*
					
					WrapperPlayServerNamedSoundEffect sound = new WrapperPlayServerNamedSoundEffect();
					sound.setEffectPositionX((int) center.getX() + r.nextInt(10) -5);
					sound.setEffectPositionY((int) center.getY() - 5 );
					sound.setEffectPositionZ((int) center.getZ() + r.nextInt(10) -5);
					sound.setPitch((byte) 63);
					sound.setVolume(1F);
					sound.setSoundName("ambient.weather.thunder");
					sound.sendPacket(owner.get());
					*/
				}
				
			}.runTaskTimer(BuildingBattle.get(), 20, 60);
		}
		
	
	
	}
	public int getWeather(){
		return weather;
	}
	
	public List<Chunk> getRelevantChunks(){
		List<Chunk> list = new ArrayList<Chunk>();
		int x_max = up.getBlockX();
		int z_max = up.getBlockZ();
		int x_min  = down.getBlockX();
		int z_min = down.getBlockZ();
		for(int x = x_min ; x <= x_max ; x++){
			for(int z = z_min; z <= z_max ;z ++){
				Location l  = new Location (up.getWorld(), x, up.getY(), z);
				if(!list.contains(l.getChunk())){
					list.add(l.getChunk());
				}
				
			}
		}
		return list;
	}
	
	public boolean setFloorBlock(Material m,byte data){
		
		if(!m.isBlock() || !m.isSolid() || m.isTransparent() || m.equals(Material.CACTUS) || m.equals(Material.TRAP_DOOR) || m.equals(Material.IRON_TRAPDOOR) 
				|| m.equals(Material.TNT) || m.equals(Material.CHEST) || m.equals(Material.ENCHANTMENT_TABLE) || m.equals(Material.DISPENSER) || m.equals(Material.DROPPER) || m.equals(Material.ENDER_CHEST) || m.equals(Material.FURNACE)|| m.equals(Material.SPONGE) || m.equals(Material.TRAPPED_CHEST) ){
			return false;
		}
	
		
			//System.out.print(up.toString());
			//System.out.print(down.toString());
			
			int x_max = up.getBlockX();
			int z_max = up.getBlockZ();
			int x_min  = down.getBlockX();
			int z_min = down.getBlockZ();
			int y_loc = down.getBlockY();
			
			//found y coordinate
			
			if(block_Y_record == -1){
				while(center.getWorld().getBlockAt(center.getBlockX() + 1, y_loc, center.getBlockZ() + 1 ).getType() != floorBlock){
					y_loc ++;
				}
				block_Y_record = y_loc;
			
			}else{
				y_loc = block_Y_record ;
			}
			//System.out.print(" x_max " +x_max + " x_min " + x_min + " z_max "  + z_max  + " z_min  " + z_min) ;
			
			for(int x = x_min ; x <= x_max ; x++){
				for(int z = z_min; z <= z_max ;z ++){
					Block b = center.getWorld().getBlockAt((int)x ,y_loc, (int)z );
					if(b.getType().equals(floorBlock)){
						b.setType(m);
						b.setData(data);
						
					}
				}
			}
			floorBlock = m;
			this.data = data;
			return true;
		
	}
	
	
	public void setPersonalPacketEnd(){
		if(lightningTask != null){
			lightningTask.cancel();
			lightningTask = null;
		}
	}
	
	public byte getFloorBlockData(){
		return data;
	}

	public Material getFloorBlock(){
		return floorBlock;
	}
	
	public void setOwner(GamePlayer p){
		this.owner = p;
	}
	public GamePlayer getOwner(){
		return owner;
	} 	
	
	public Location getTelePoint(){
		Location returnValue = center.clone();
		Random r = new Random();
		
		switch(r.nextInt(9) + 1){
			case 1:
				returnValue.setX(returnValue.getX() + currentGame.getPlotSize()/6);
				break;
			case 2:
				returnValue.setX(returnValue.getX() - currentGame.getPlotSize()/6);
				break;
			case 3:
				returnValue.setZ(returnValue.getZ() - currentGame.getPlotSize()/6);
				break;			
			case 4:
				returnValue.setZ(returnValue.getZ() + currentGame.getPlotSize()/6);
				break;
			case 5:
				returnValue.setZ(returnValue.getZ() + currentGame.getPlotSize()/6);
				returnValue.setX(returnValue.getX() + currentGame.getPlotSize()/6);
				break;
			case 7:
				returnValue.setZ(returnValue.getZ() - currentGame.getPlotSize()/6);
				returnValue.setX(returnValue.getX() - currentGame.getPlotSize()/6);
				break;
			case 8:
				returnValue.setZ(returnValue.getZ() + currentGame.getPlotSize()/6);
				returnValue.setX(returnValue.getX() - currentGame.getPlotSize()/6);
				break;
			case 9:
				returnValue.setZ(returnValue.getZ() - currentGame.getPlotSize()/6);
				returnValue.setX(returnValue.getX() + currentGame.getPlotSize()/6);
				break;
		}
		
		return  returnValue;
	}
	
	public Location getUp(){
		return this.up;
	}
	
	private void calPlotLocs(){
		
		Location upGlobal = currentGame.getUp();
		Location downGlobal = currentGame.getDown();
		
		
		
		this.up = new Location(upGlobal.getWorld(), 
									(downGlobal.getX() + ((upGlobal.getX() - downGlobal.getX())/4 ) * (num % 4) ), 
									upGlobal.getY(),
									downGlobal.getZ() + ((upGlobal.getZ() - downGlobal.getZ()) / 4 ) * (num / 4 ) - 1  );
		
		
		this.down = new Location(upGlobal.getWorld(), up.getX() + currentGame.getPlotSize() , downGlobal.getY()  , up.getZ()  + currentGame.getPlotSize());  
	
		this.down.setZ(this.down.getZ() - (num %4  + 1)* 1 );

		
		
		if(up.getX() < down.getX()){
			double temp = up.getX();
			up.setX(down.getX());
			down.setX(temp);
		}
		
		if(up.getZ() < down.getZ()){
			double temp = up.getZ();
			up.setZ(down.getZ());
			down.setZ(temp);
		}
		
		
		this.center = new Location (up.getWorld(),(up.getX() - down.getX())/2 + down.getX()  , (up.getY() - down.getY() )/2 + down.getY() , (up.getZ()-down.getZ())/2 + down.getZ() );
		//System.out.print("plot " + num);
		//System.out.print("up" + up.toString());
		//System.out.print("down" + down.toString());
		//System.out.print("center" + center.toString());
		
		
	}
	
	public boolean isOutOfBound(Location l){
		if(l != null){
			if(l.getX() >= up.getX() || l.getY() >= up.getY() || l.getZ() >= up.getZ() || l.getX() <= down.getX() || l.getZ() <= down.getZ() || l.getY() < down.getY()){
				//System.out.print("plot " + num);
				//System.out.print("up" + up.toString());
				//System.out.print("down" + down.toString());
				//System.out.print(l.toString());
				return true;
			}
		}
		return false;
	}
	
	public Location getCenter(){
		return this.center;
	}
	
}
