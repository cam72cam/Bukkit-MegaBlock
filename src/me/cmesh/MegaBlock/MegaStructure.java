package me.cmesh.MegaBlock;

import java.util.*;

import org.bukkit.block.Block;

public class MegaStructure {
	private MegaStructureTemplate template;
	private Block origin;
	private CoordSpace space;
	private List<Block> ourBlocks;
	
	public MegaStructure(MegaStructureTemplate template, Block origin, CoordSpace space) {
		this.template = template;
		this.origin = origin;
		this.space = space;
		
		ourBlocks = this.template.getAllBlocks(this.origin, this.space);
	}

	public boolean containsBlock(Block clickedBlock) {
		return ourBlocks.contains(clickedBlock);
	}
}
