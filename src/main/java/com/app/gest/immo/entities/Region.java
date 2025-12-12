package com.app.gest.immo.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.io.Serializable;

@DiscriminatorValue("003")
@Entity
public class Region extends Parametre implements Serializable {

}
