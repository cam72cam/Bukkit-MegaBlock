package me.cmesh.MegaBlock;

import java.util.*;

import me.cmesh.MegaBlock.MegaListener.Event;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MegaBlock implements Listener {
	private JavaPlugin plugin;
	private MegaListener listener;
	private List<MegaStructureTemplate> templates;
	private List<MegaStructure> instances;
	
	public MegaBlock(JavaPlugin pl, MegaListener ml, MegaStructureTemplate ... s) {
		plugin = pl;
		listener = ml;
		templates = Arrays.asList(s);
		instances = new ArrayList<MegaStructure>();//TODO serialize/deserialize
		
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent ev) {
		if (ev.getAction() == Action.LEFT_CLICK_BLOCK || ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
			for(MegaStructure structure : instances) {
				if (structure.containsBlock(ev.getClickedBlock())) {
					listener.onClick(new Event<PlayerInteractEvent>(ev, null, null));
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
				listener.onCreate(new Event<BlockPlaceEvent>(ev, null, null));
				if (!ev.isCancelled()) {
					instances.add(struct);
				}
			}
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockBreakEvent ev) {
		for(MegaStructure structure : instances) {
			if (structure.containsBlock(ev.getBlock())) {
				listener.onBreak(new Event<BlockBreakEvent>(ev, null, null));
				if (!ev.isCancelled()) {
					instances.remove(structure);
				}
			}
		}
	}
}