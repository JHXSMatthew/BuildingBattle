package com.github.JHXSMatthew.Listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;

public class EntityListener implements Listener {
	@EventHandler
	public void onEntitySpawn(CreatureSpawnEvent evt) {
		if(!evt.getSpawnReason().equals(SpawnReason.CUSTOM)){
			evt.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onDamage(EntityDamageEvent evt){
		evt.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
    public void onExplodeDestroy(EntityExplodeEvent evt) {
		evt.setCancelled(true);
	}
	
	@EventHandler
	public void onPotion(PotionSplashEvent evt){
		evt.setCancelled(true);
		for(LivingEntity i : evt.getAffectedEntities()){
			for(PotionEffect pf : i.getActivePotionEffects()){
				i.removePotionEffect(pf.getType());
			}
		}
	}
	
}
