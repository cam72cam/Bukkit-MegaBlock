package me.cmesh.MegaBlock;

import java.util.*;

import me.cmesh.MegaBlock.MegaListener.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MegaBlock implements Listener {
	private JavaPlugin plugin;
	private MegaListener listener;
	private List<MegaStructureTemplate> templates;
	private ArrayList<MegaStructure> instances;
	
	public MegaBlock(JavaPlugin pl, MegaListener ml, MegaStructureTemplate ... s) {
		this(pl, ml, Arrays.asList(s));
	}
	
	public MegaBlock(JavaPlugin pl, MegaListener ml, List<MegaStructureTemplate> templates) {
		this.plugin = pl;
		this.listener = ml;
		this.templates = templates;
		this.instances = new ArrayList<MegaStructure>();//TODO serialize/deserialize
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent ev) {
		if (ev.getAction() == Action.LEFT_CLICK_BLOCK || ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
			for(MegaStructure structure : instances) {
				if (structure.containsBlock(ev.getClickedBlock())) {
					listener.onClick(new Event<PlayerInteractEvent>(ev, structure));
					break;
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent ev) {
		for (MegaStructureTemplate t : templates ) {
			MegaStructure struct = t.checkLocation(ev.getBlock());
			if (struct != null) {
				listener.onCreate(new Event<BlockPlaceEvent>(ev, struct));
				if (!ev.isCancelled()) {
					instances.add(struct);
				}
			}
		}
	}
	
	//Clone fixes concurrent modification exception
	@SuppressWarnings("unchecked")
	@EventHandler
	public void onBlockBreak(BlockBreakEvent ev) {
		for(MegaStructure structure : (ArrayList<MegaStructure>)instances.clone()) {
			if (structure.containsBlock(ev.getBlock())) {
				listener.onBreak(new Event<BlockBreakEvent>(ev, structure));
				if (!ev.isCancelled()) {
					instances.remove(structure);
				}
			}
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onBlockFlow(BlockFromToEvent ev) {
		for(MegaStructure structure : instances) {
			if (structure.containsBlock(ev.getBlock())) {
				ev.setCancelled(true);
				return;
			}
		}
	}
	@EventHandler
	public void foo(BlockPhysicsEvent ev) {
		for(MegaStructure structure : instances) {
			if (structure.containsBlock(ev.getBlock())) {
				ev.setCancelled(true);
				return;
			}
		}
	}
}