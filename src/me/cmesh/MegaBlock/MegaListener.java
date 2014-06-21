package me.cmesh.MegaBlock;

import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public interface MegaListener {
	public class Event<T extends Cancellable > {
		public T event;
		public MegaStructure structure;
		
		public Event(T event, MegaStructure s) {
			this.event = event;
			this.structure = s;
		}
	}
	
	public void onClick(Event<PlayerInteractEvent> ev);
	public void onCreate(Event<BlockPlaceEvent> ev);
	public void onBreak(Event<BlockBreakEvent> ev);
}
