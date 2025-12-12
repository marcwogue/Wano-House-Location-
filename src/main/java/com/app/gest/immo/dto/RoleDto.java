package com.app.gest.immo.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class RoleDto implements Serializable {
    private long id;
    private String functions;
    private String nom;
    private String description;
    private Set<String>  listGroups = new HashSet<>();

}
