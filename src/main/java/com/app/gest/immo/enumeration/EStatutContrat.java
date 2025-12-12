package com.app.gest.immo.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EStatutContrat {
	Encours("EN"),
	Resilier("RE"),
	Annuler("AN"),
	Abandonner("AB");

	private final String code;

	EStatutContrat(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	@JsonCreator
	public static EStatutContrat fromString(String value) {
		for (EStatutContrat statut : EStatutContrat.values()) {
			if (statut.name().equalsIgnoreCase(value) || statut.code.equalsIgnoreCase(value)) {
				return statut;
			}
		}
		throw new IllegalArgumentException("Valeur inconnue pour EStatutContrat : " + value);
	}

	@JsonValue
	public String toValue() {
		return name();
	}
}
