package com.iris.sdmx.fusion.bean;

/**
 * @author apagaria
 *
 */
public class FusionConstraintsBean {

	private Meta meta;
	private Data data;

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public Data getData() {
		return data;
	}

	/**
	 *
	 */
	@Override
	public String toString() {
		return "FusionConstraintsBean [meta=" + meta + ", data=" + data + "]";
	}

	//	public static void main(String[] args) {
	//		String jsonString = "{\n" + "  \"meta\": {\n" + "    \"id\": \"IREF292807\",\n" + "    \"test\": false,\n"
	//				+ "    \"schema\": \"https://raw.githubusercontent.com/sdmx-twg/sdmx-json/develop/structure-message/tools/schemas/1.0/sdmx-json-structure-schema.json\",\n"
	//				+ "    \"prepared\": \"2021-06-16T07:41:29Z\",\n" + "    \"contentLanguages\": [\n" + "      \"en\"\n"
	//				+ "    ],\n" + "    \"sender\": {\n" + "      \"id\": \"Unknown\"\n" + "    }\n" + "  },\n"
	//				+ "  \"data\": {\n" + "    \"contentConstraints\": [\n" + "      {\n"
	//				+ "        \"name\": \"DMID Validity Borrowings\",\n" + "        \"names\": {\n"
	//				+ "          \"en\": \"DMID Validity Borrowings\"\n" + "        },\n"
	//				+ "        \"id\": \"DMIDvalidity_BORROWINGS\",\n" + "        \"version\": \"1.0\",\n"
	//				+ "        \"agencyID\": \"TEST_DEV_TEAM\",\n" + "        \"isExternalReference\": false,\n"
	//				+ "        \"isFinal\": false,\n" + "        \"type\": \"allowed\",\n"
	//				+ "        \"constraintAttachment\": {\n" + "          \"dataStructures\": [\n"
	//				+ "            \"urn:sdmx:org.sdmx.infomodel.datastructure.DataStructure=TEST_DEV_TEAM:BORROWINGS(1.0)\"\n"
	//				+ "          ]\n" + "        },\n" + "        \"dataKeySets\": [\n" + "          {\n"
	//				+ "            \"isIncluded\": true,\n" + "            \"keys\": [\n" + "              {\n"
	//				+ "                \"keyValues\": [\n" + "                  {\n"
	//				+ "                    \"id\": \"DEPENDENCY_TYPE\",\n"
	//				+ "                    \"value\": \"INDEPENDENT\"\n" + "                  },\n"
	//				+ "                  {\n" + "                    \"id\": \"Area_Operation\",\n"
	//				+ "                    \"value\": \"DOM_OP\"\n" + "                  },\n" + "                  {\n"
	//				+ "                    \"id\": \"Borrowing_Type\",\n"
	//				+ "                    \"value\": \"BOR_CI_MONEYMARKET\"\n" + "                  },\n"
	//				+ "                  {\n" + "                    \"id\": \"Measure_Type\",\n"
	//				+ "                    \"value\": \"TOTAL\"\n" + "                  },\n" + "                  {\n"
	//				+ "                    \"id\": \"INPUT\",\n" + "                    \"value\": \"N_A\"\n"
	//				+ "                  },\n" + "                  {\n" + "                    \"id\": \"DATE\",\n"
	//				+ "                    \"value\": \"N_A\"\n" + "                  },\n" + "                  {\n"
	//				+ "                    \"id\": \"SECUNSEC\",\n" + "                    \"value\": \"N_A\"\n"
	//				+ "                  },\n" + "                  {\n" + "                    \"id\": \"TIME_SPAN\",\n"
	//				+ "                    \"value\": \"N_A\"\n" + "                  },\n" + "                  {\n"
	//				+ "                    \"id\": \"Listed\",\n" + "                    \"value\": \"N_A\"\n"
	//				+ "                  },\n" + "                  {\n" + "                    \"id\": \"Currency\",\n"
	//				+ "                    \"value\": \"INR\"\n" + "                  }\n" + "                ]\n"
	//				+ "              }\n" + "            ]\n" + "          }\n" + "        ],\n" + "        \"links\": [\n"
	//				+ "          {\n" + "            \"rel\": \"self\",\n"
	//				+ "            \"urn\": \"urn:sdmx:org.sdmx.infomodel.registry.ContentConstraint=TEST_DEV_TEAM:DMIDvalidity_BORROWINGS(1.0)\",\n"
	//				+ "            \"uri\": \"https://raw.githubusercontent.com/sdmx-twg/sdmx-json/develop/structure-message/tools/schemas/1.0/sdmx-json-structure-schema.json\",\n"
	//				+ "            \"type\": \"contentconstraint\",\n" + "            \"hreflang\": \"en\"\n"
	//				+ "          }\n" + "        ]\n" + "      }\n" + "    ]\n" + "  }\n" + "}";
	//
	//		Gson gson = new Gson();
	//		FusionConstraintsBean fusionConstraintsBean = gson.fromJson(jsonString, FusionConstraintsBean.class);
	//		//System.out.println("FusionConstraintsBean - " + fusionConstraintsBean.toString());
	//	}

}