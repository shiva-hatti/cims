package com.iris.sdmx.exceltohtml.bean;

import java.util.Collections;
import java.util.List;

/**
 * @author apagaria
 *
 */
public class SdmxEleDimTypeMapBean {

	/**
	 * 
	 */
	private String dsdCode;

	/**
	 * 
	 */
	private String elementVer;

	/**
	 * 
	 */
	private List<String> closedDim;

	/**
	 * 
	 */
	private List<String> openDim;

	/**
	 * @return the dsdCode
	 */
	public String getDsdCode() {
		return dsdCode;
	}

	/**
	 * @param dsdCode the dsdCode to set
	 */
	public void setDsdCode(String dsdCode) {
		this.dsdCode = dsdCode;
	}

	/**
	 * @return the elementVer
	 */
	public String getElementVer() {
		return elementVer;
	}

	/**
	 * @param elementVer the elementVer to set
	 */
	public void setElementVer(String elementVer) {
		this.elementVer = elementVer;
	}

	/**
	 * @return the closedDim
	 */
	public List<String> getClosedDim() {
		return closedDim;
	}

	/**
	 * @param closedDim the closedDim to set
	 */
	public void setClosedDim(List<String> closedDim) {
		Collections.sort(closedDim);
		this.closedDim = closedDim;
	}

	/**
	 * @return the openDim
	 */
	public List<String> getOpenDim() {
		return openDim;
	}

	/**
	 * @param openDim the openDim to set
	 */
	public void setOpenDim(List<String> openDim) {
		Collections.sort(openDim);
		this.openDim = openDim;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "SdmxEleDimTypeMapBean [dsdCode=" + dsdCode + ", elementVer=" + elementVer + ", closedDim=" + closedDim + ", openDim=" + openDim + "]";
	}

}
