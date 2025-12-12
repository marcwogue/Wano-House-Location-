package com.app.gest.immo.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.io.Serializable;

@Entity
@DiscriminatorValue("001")
public class   Ville extends Parametre implements Serializable {

}
