package com.iris.dto;

import java.util.List;

/**
 * @author sajadhav
 *
 */
public class NbfcAndOtherCategoryDto {

	private List<SubCategoryDto> nbfcCatDtos;

	private List<SubCategoryDto> otherCateDtos;

	/**
	 * @return the nbfcCatDtos
	 */
	public List<SubCategoryDto> getNbfcCatDtos() {
		return nbfcCatDtos;
	}

	/**
	 * @param nbfcCatDtos the nbfcCatDtos to set
	 */
	public void setNbfcCatDtos(List<SubCategoryDto> nbfcCatDtos) {
		this.nbfcCatDtos = nbfcCatDtos;
	}

	/**
	 * @return the otherCateDtos
	 */
	public List<SubCategoryDto> getOtherCateDtos() {
		return otherCateDtos;
	}

	/**
	 * @param otherCateDtos the otherCateDtos to set
	 */
	public void setOtherCateDtos(List<SubCategoryDto> otherCateDtos) {
		this.otherCateDtos = otherCateDtos;
	}

}
