package com.app.gest.immo.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.io.Serializable;

@Entity
@DiscriminatorValue("002")
public class Quartier extends Parametre implements Serializable {

}
