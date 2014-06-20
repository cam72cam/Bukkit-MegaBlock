package me.cmesh.MegaBlock;

import org.bukkit.block.BlockFace;

public class CoordSpace {
	private BlockFace row;
	private BlockFace col;
	
	public CoordSpace(BlockFace row, BlockFace col) {
		this.row = row;
		this.col = col;
	}
	
	public BlockFace Row() {
		return this.row;
	}
	
	public BlockFace Col() {
		return this.col;
	}
	public String toString() {
		return this.row + "," + this.col;
	}
}