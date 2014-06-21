package me.cmesh.MegaBlock;

import java.util.*;

import org.bukkit.Material;
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

	public boolean containsType(Material ... mat) {
		List<Material> set = Arrays.asList(mat);
		for (Block b : ourBlocks) {
			if (set.contains(b.getType())) {
				return true;
			}
		}
		return false;
	}
	public List<Block> blocksByMaterial(Material ... mat) {
		List<Block> res = new ArrayList<Block>();
		List<Material> set = Arrays.asList(mat);
		
		for (Block b : ourBlocks) {
			if (set.contains(b.getType())) {
				res.add(b);
			}
		}
		return res;
	}

	public List<List<Block>> blockLevels() {
		return this.template.getAllLevels(this.origin, this.space);
	}
}
