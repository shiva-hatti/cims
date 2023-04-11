/**
 * 
 */
package com.iris.sdmx.util;

/**
 * @author sajadhav
 *
 */
public enum SDMXDocumentEnum {

	ID("id"), REF("ref"), DATA_SET("DataSet"), OBS("Obs"), VERSION("version"), AGENCY_ID("agencyID"), STRUCTURE_REF("structureRef"), TYPE("type"), DATA_SCOPE("dataScope"), ACTION("action"), HEADER("Header"), SENDER("Sender"), STRUCTURE("Structure"), NAME_SPACE("namespace"), DIMENSION_AT_OBSERVATION("dimensionAtObservation"), STRUCTURE_ID("structureID"), SET_ID("SetId"), STRUCTURE_SPECIFICATION_DATA("StructureSpecificData"), XBRL("xbrl");

	private String constant;

	private SDMXDocumentEnum(String constant) {
		this.constant = constant;
	}

	public String getConstant() {
		return constant;
	}

	public static SDMXDocumentEnum getEnumByValue(String value) {
		for (SDMXDocumentEnum sdmxDocumentEnum : SDMXDocumentEnum.values()) {
			if (sdmxDocumentEnum.getConstant().equalsIgnoreCase(value)) {
				return sdmxDocumentEnum;
			}
		}
		return null;
	}

}
