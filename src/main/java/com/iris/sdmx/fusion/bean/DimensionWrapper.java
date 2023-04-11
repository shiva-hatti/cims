/**
 * 
 */
package com.iris.sdmx.fusion.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @author sajadhav
 *
 */
public class DimensionWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6530948777515738347L;

	private List<Dimension> dimensions;

	/**
	 * @return the dimensions
	 */
	public List<Dimension> getDimensions() {
		return dimensions;
	}

	/**
	 * @param dimensions the dimensions to set
	 */
	public void setDimensions(List<Dimension> dimensions) {
		this.dimensions = dimensions;
	}

}
