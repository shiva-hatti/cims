package com.iris.dto;

import java.io.Serializable;

import com.iris.util.Validations;

public class AutoCalFormulaBean implements Serializable {

	private static final long serialVersionUID = 4209526460511591926L;

	private String formulaJson;
	private String crossElrJson;

	public String getFormulaJson() {
		return formulaJson;
	}

	public void setFormulaJson(String formulaJson) {
		this.formulaJson = Validations.trimInput(formulaJson);
	}

	public String getCrossElrJson() {
		return crossElrJson;
	}

	public void setCrossElrJson(String crossElrJson) {
		this.crossElrJson = Validations.trimInput(crossElrJson);
	}

}