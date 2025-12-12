package com.app.gest.immo.enumeration;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum EEtatBien {

	Occupe("Occupe"),
	Libre("Libre"),
	EnReparation("EnReparation"),
	Abandonne("Abandonne");

	private final String code;

	EEtatBien(String code) {
		this.code = code;
	}

	@JsonValue
	public String getCode() {
		return code;
	}

	@JsonCreator
	public static EEtatBien fromCode(String code) {
		for (EEtatBien etat : EEtatBien.values()) {
			if (etat.code.equalsIgnoreCase(code)) {
				return etat;
			}
		}
		throw new IllegalArgumentException("Invalid code for EEtatBien: " + code);
	}
}
