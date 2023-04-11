package com.iris.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author nsasane
 * @date 08/12/17
 */
public class DataInfoBean implements Serializable {

	private static final long serialVersionUID = -6557076410697882361L;
	private List<DataBlock> block;

	/**
	 * @return the block
	 */
	public List<DataBlock> getBlock() {
		return block;
	}

	/**
	 * @param block the block to set
	 */
	public void setBlock(List<DataBlock> block) {
		this.block = block;
	}

}
