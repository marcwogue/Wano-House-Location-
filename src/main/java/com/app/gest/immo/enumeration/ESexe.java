package com.app.gest.immo.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ESexe {
	
	Masculin("M"), Feminin("F"), Autre("A");

	String code;
	ESexe(String code) {
		this.code =code;
	}
	
	String getSexe() {
		return code;
	}

	@JsonCreator
	public static ESexe fromCode(String code) {
		for (ESexe sexe : ESexe.values()) {
			if (sexe.code.equalsIgnoreCase(code) || sexe.name().equalsIgnoreCase(code)) {
				return sexe;
			}
		}
		throw new IllegalArgumentException("Invalid sexe: " + code);
	}

}
