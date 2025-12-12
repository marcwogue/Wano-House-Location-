package com.app.gest.immo.init;

import java.util.HashSet;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.app.gest.immo.config.securities.Groupes;
import com.app.gest.immo.config.securities.Roles;
import com.app.gest.immo.config.securities.Status;
import com.app.gest.immo.config.securities.Utilisateur;
import com.app.gest.immo.repository.IGroupesRepository;
import com.app.gest.immo.repository.IRolesRepository;
import com.app.gest.immo.service.IUtilisateur;
//@Component
public class Database implements CommandLineRunner {

    private final IGroupesRepository serviceGroupe;
    private final IRolesRepository serviceRole;
    private final IUtilisateur iUtilisateur;

    private final PasswordEncoder passwordEncoder;


    public Database(IGroupesRepository serviceGroupe, IRolesRepository serviceRole, IUtilisateur iUtilisateur, PasswordEncoder passwordEncoder) {
        this.serviceGroupe = serviceGroupe;
        this.serviceRole = serviceRole;
        this.iUtilisateur = iUtilisateur;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        try {
            // Création des rôles
            Roles superAdmin = serviceRole.save(new Roles("SUPERADMIN", "Super utilisateur", "all"));
            Roles admin = serviceRole.save(new Roles("ADMIN", "Admin", "all"));
            Roles root = serviceRole.save(new Roles("ROOT", "Utilisateur Root", "all"));
            Roles user = serviceRole.save(new Roles("USER", "Utilisateur", "all"));
            Roles visitor = serviceRole.save(new Roles("VISITOR", "Visiteur", "all"));

            // Création du groupe SUPERADMIN avec tous les rôles
            Groupes superAdminGroup = new Groupes(null, new HashSet<>(), "SUPERADMIN", "Groupe des SUPERADMIN", "Description", new HashSet<>());
            superAdminGroup.getRoles().add(superAdmin);
            superAdminGroup.getRoles().add(admin);
            superAdminGroup.getRoles().add(root);
            superAdminGroup.getRoles().add(user);
            superAdminGroup.getRoles().add(visitor);
            superAdminGroup = serviceGroupe.save(superAdminGroup);

            // Création des autres groupes
            Groupes adminGroup = new Groupes(null, new HashSet<>(), "ADMIN", "Groupe des administrateurs", "Description", new HashSet<>());
            adminGroup.getRoles().add(admin);
            serviceGroupe.save(adminGroup);

            Groupes userGroup = new Groupes(null, new HashSet<>(), "USER", "Groupe des Utilisateurs", "Description", new HashSet<>());
            userGroup.getRoles().add(user);
            serviceGroupe.save(userGroup);

            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setEmail("admin@wano.com");
            utilisateur.setNumero("AllAdmin");
            utilisateur.setNom("wanoApp");
            utilisateur.setLogin("wanoApp");
            String pass = passwordEncoder.encode("12345678");
            utilisateur.setPassWord(pass);
            utilisateur.setStatus(Status.ACTIF);
            utilisateur.setGroupes(superAdminGroup); // 🔗 association obligatoire

            // Sauvegarde de l'utilisateur
            iUtilisateur.save(utilisateur);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}