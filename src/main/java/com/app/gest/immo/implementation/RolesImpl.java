package com.app.gest.immo.implementation;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.app.gest.immo.config.securities.Groupes;
import com.app.gest.immo.dto.RoleDto;
import com.app.gest.immo.repository.IGroupesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.gest.immo.config.securities.Roles;
import com.app.gest.immo.repository.IRolesRepository;
import com.app.gest.immo.service.IRoles;

 @Service
public class RolesImpl implements IRoles {

	@Autowired
	private IRolesRepository iRolesRepository;
     @Autowired
     private IGroupesRepository iGroupesRepository;

     @Override
	public Roles save(Roles roles) throws Exception {
		Roles role = iRolesRepository.save(roles);
		return role;
	}

     @Override
     public Roles toRoles(RoleDto roleDto) throws Exception {
        Roles role = new Roles();
        Set<Groupes> groupe = new java.util.HashSet<>();
         roleDto.getListGroups().forEach(group -> {
             groupe.add(iGroupesRepository.findByName(group));
         });
         role.setDescription(roleDto.getDescription());
         if (roleDto.getId() !=0){
             role.setId(roleDto.getId());
         }
         role.setFonction(roleDto.getFunctions());
         role.setListGroupes(groupe);
         role.setNom(roleDto.getNom());

         return role;
     }

     @Override
     public List<Roles> findAll() {
         return List.of();
     }

     @Override
	public List<Roles> list() throws Exception {
		List<Roles> listRoles = iRolesRepository.findAll();
		return listRoles;
	}

	@Override
	public Roles update(Roles roles) throws Exception {
		Roles role = iRolesRepository.saveAndFlush(roles);
		return role;
	}

	@Override
	public void delete(Roles roles) throws Exception {
		iRolesRepository.delete(roles);

	}

	@Override
	public void deleteById(Long id) throws Exception {
		Optional<Roles> roles = iRolesRepository.findById(id);
		if (roles == null || roles.isEmpty()) {

		}
		Roles role = roles.get();
		iRolesRepository.delete(role);

	}

	@Override
	public Roles findByNom(String nom) throws Exception {
		Optional<Roles> listRoles = iRolesRepository.findByNom(nom);
		if (listRoles == null || listRoles.isEmpty()) {
			throw new Exception("Role not found");
		}
		return listRoles.get();
	}
}
