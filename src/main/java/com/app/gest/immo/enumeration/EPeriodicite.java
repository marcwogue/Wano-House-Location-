package com.app.gest.immo.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EPeriodicite {

	Mensuelle(0),
	Trimestrielle(1),
	Semestrielle(2),
	Annuelle(3);

	private final Integer code;

	EPeriodicite(Integer code) {
		this.code = code;
	}

	public Integer getPeriode() {
		return code;
	}

	// Permet de renvoyer le nom correct lors de la sérialisation
	@JsonValue
	public String toValue() {
		return this.name();
	}

	// Permet de désérialiser une valeur insensible à la casse (comme "MENSUEL")
	@JsonCreator
	public static EPeriodicite fromString(String value) {
		for (EPeriodicite periodicite : EPeriodicite.values()) {
			if (periodicite.name().equalsIgnoreCase(value)) {
				return periodicite;
			}
		}
		throw new IllegalArgumentException("Valeur invalide pour EPeriodicite : " + value);
	}
}
