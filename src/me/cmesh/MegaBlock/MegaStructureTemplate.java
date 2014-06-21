package me.cmesh.MegaBlock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.util.Vector;

public class MegaStructureTemplate {
	//level, x, y
	private List<Material> [][][] structure;
	
	private CoordSpace [] directions = 
		{
			new CoordSpace(BlockFace.NORTH, BlockFace.EAST ),
			new CoordSpace(BlockFace.NORTH, BlockFace.WEST ),
			new CoordSpace(BlockFace.SOUTH, BlockFace.EAST ),
			new CoordSpace(BlockFace.SOUTH, BlockFace.WEST ),
			
			new CoordSpace(BlockFace.EAST, BlockFace.NORTH),
			new CoordSpace(BlockFace.EAST, BlockFace.SOUTH),
			new CoordSpace(BlockFace.WEST, BlockFace.NORTH),
			new CoordSpace(BlockFace.WEST, BlockFace.SOUTH),
		};
	
	public MegaStructureTemplate (List<Material> [][][] blueprint) {
		structure = blueprint;
	}
	
	//Because java
	private class MaterialListTemp extends ArrayList<Material> {private static final long serialVersionUID = 1L;}
	public MegaStructureTemplate (Material [][][][] blueprint) {
		structure = new MaterialListTemp [blueprint.length][][];
		for (int i = 0; i < blueprint.length; i++) {
			structure[i] = new MaterialListTemp [blueprint[i].length][];
			for (int j = 0; j < blueprint[i].length; j++) {
				structure[i][j] = new MaterialListTemp [blueprint[i][j].length];
				for(int k = 0; k < blueprint[i][j].length; k++) {
					List<Material> original = Arrays.asList(blueprint[i][j][k]);
					//Have I mentioned that I hate java?
					MaterialListTemp converted = new MaterialListTemp();
					converted.addAll(original);
					structure[i][j][k] = converted;
				}
			}
		}
	}
	
	private Map<CoordSpace,Location> findOrigin(Vector offset, Block block) {
		Map<CoordSpace, Location> res = new HashMap<CoordSpace, Location>();  
		for (CoordSpace dr : directions) {
			Block b = block
				.getRelative(BlockFace.DOWN,             (int) offset.getX())	//Always below us
				.getRelative(dr.Row().getOppositeFace(), (int) offset.getY())
				.getRelative(dr.Col().getOppositeFace(), (int) offset.getZ());
			
			//We match or are ignored because empty
			//empty origin will be kind of a crappy situation, might want to remove at some point
			if (this.structure[0][0][0].isEmpty() || this.structure[0][0][0].contains(b.getType())) {
				//This should get us a coordspace and potential origin
				res.put(dr, b.getLocation());
			}
		}
		return res;
	}
	private boolean verifyStructure(CoordSpace dr, Location origin) {
		for (int i = 0; i < structure.length; i ++) {
			List<Material>[][] level = structure[i];
			for (int j = 0; j < level.length; j++) {
				List<Material>[] row = level[j];
				for (int k = 0; k < row.length; k++) {
					List<Material> col = row[k];
					Block curr = origin.getBlock()
						.getRelative(BlockFace.UP, i)
						.getRelative(dr.Row(), j)
						.getRelative(dr.Col(), k);
					//not (Have value(s) or contains curr's type) 
					if (!(col.isEmpty() || col.contains(curr.getType()))) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public MegaStructure checkLocation(Block block) {
		Material locMat = block.getType();
		for (int i = 0; i < structure.length; i ++) {
			List<Material>[][] level = structure[i];
			for (int j = 0; j < level.length; j++) {
				List<Material>[] row = level[j];
				for (int k = 0; k < row.length; k++) {
					List<Material> col = row[k];
					//Don't care for empty here
					if (col.contains(locMat)) {
						//We have a possible position
						//This is offset from origin
						//We need to find the origin from this location
						Vector v = new Vector(i,j,k);
						for (Entry<CoordSpace, Location> set : findOrigin(v, block).entrySet()) {
							if (verifyStructure(set.getKey(), set.getValue())) {
								return new MegaStructure(this, set.getValue().getBlock(), set.getKey());
							}
						}
					}
				}
			}
		}
		return null;
	}

	public List<Block> getAllBlocks(Block origin, CoordSpace space) {
		List<Block> blocks = new ArrayList<Block>();
		
		for (int i = 0; i < structure.length; i ++) {
			List<Material>[][] level = structure[i];
			for (int j = 0; j < level.length; j++) {
				List<Material>[] row = level[j];
				for (int k = 0; k < row.length; k++) {
					Block curr = origin
						.getRelative(BlockFace.UP, i)
						.getRelative(space.Row(), j)
						.getRelative(space.Col(), k);
					blocks.add(curr);
				}
			}
		}
		return blocks;
	}

	public List<List<Block>> getAllLevels(Block origin, CoordSpace space) {
		List<List<Block>> blocks = new ArrayList<List<Block>>();
		
		for (int i = 0; i < structure.length; i ++) {
			List<Block> blockLevel = new ArrayList<Block>();
			List<Material>[][] level = structure[i];
			for (int j = 0; j < level.length; j++) {
				List<Material>[] row = level[j];
				for (int k = 0; k < row.length; k++) {
					Block curr = origin
						.getRelative(BlockFace.UP, i)
						.getRelative(space.Row(), j)
						.getRelative(space.Col(), k);
					blockLevel.add(curr);
				}
			}
			blocks.add(blockLevel);
		}
		return blocks;
	}
}
