package com.app.gest.immo.implementation;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

import com.app.gest.immo.dto.UtilisateurDTO;
import com.app.gest.immo.repository.IGroupesRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.app.gest.immo.config.securities.Groupes;
import com.app.gest.immo.config.securities.Status;
import com.app.gest.immo.config.securities.Utilisateur;
import com.app.gest.immo.repository.IUtilisateurRepository;
import com.app.gest.immo.service.IGoupes;
import com.app.gest.immo.service.IUtilisateur;

@Service
public class USerImpl implements IUtilisateur {

    private final IUtilisateurRepository repository;
    private final IGoupes groupesService;
    private final PasswordEncoder passwordEncoder;
    private final ExecutorService executorService;
    private final IGroupesRepository iGroupesRepository;
    private final IGoupes iGoupes;

    public USerImpl(IUtilisateurRepository repository, IGoupes groupesService,
                    @Qualifier("passwordEncoder") PasswordEncoder passwordEncoder, ExecutorService executorService, IGroupesRepository iGroupesRepository, IGoupes iGoupes) {
        this.repository = repository;
        this.groupesService = groupesService;
        this.passwordEncoder = passwordEncoder;
        this.executorService = executorService;
        this.iGroupesRepository = iGroupesRepository;
        this.iGoupes = iGoupes;
    }

    @Override
    public Utilisateur findById(Long id) throws Exception {
        return repository.findById(id).orElseThrow(() -> new Exception("User with id = " + id + " doesn't exist"));
    }

    @Override
    public Utilisateur toEntitieUser(UtilisateurDTO utilisateurDTO) throws Exception {
        Utilisateur newUser = new Utilisateur();
        Groupes groupe = iGroupesRepository.findByName(utilisateurDTO.getGroupes());
        newUser.setEmail(utilisateurDTO.getEmail());
        newUser.setiP(utilisateurDTO.getIP());
        newUser.setGroupes(groupe);
        newUser.setDateModif(utilisateurDTO.getDateModif());
        newUser.setNom(utilisateurDTO.getNom());
        newUser.setNumero(utilisateurDTO.getNumero());
        newUser.setDateCreation(utilisateurDTO.getDateCreation());
        newUser.setStatus(utilisateurDTO.getStatus());
        newUser.setLogin(utilisateurDTO.getLogin());
        newUser.setMaxSession(utilisateurDTO.getMaxSession());
        newUser.setPassWord(utilisateurDTO.getPassWord());
        newUser.setUtiCreation(utilisateurDTO.getUtiCreation());
        newUser.setFirstConnexion(utilisateurDTO.isFirstConnexion());
        return newUser;
    }


    @Override
    @Transactional
    public Utilisateur save(Utilisateur utilisateur, Long idGroupe) throws Exception {
        //String pwd = generateRandomPassword();
        Groupes groupe = groupesService.finById(idGroupe);

        String j = utilisateur.getPassWord();
        if (groupe != null){
            groupe = groupesService.findByNom("PROPRIETAIRE");
            if (groupe != null){
                utilisateur.setGroupes(groupe);
            }
        }
        System.out.println(utilisateur.getPassWord());
        utilisateur.setPassWord(passwordEncoder.encode(utilisateur.getPassWord()));


        String l = utilisateur.getPassWord();
        utilisateur.setStatus(Status.ACTIF);
        try {
            saveIt(utilisateur);
            executorService.execute(() -> {
                String message = "Félicitations pour votre inscription. Login: "
                        + (utilisateur.getEmail() != null ? utilisateur.getEmail() : utilisateur.getLogin())
                        + " \n Password: " + utilisateur;
                // Envoyer l'email ou le message ici avec les informations de connexion
                System.out.println(message); // Exemple : remplacer par un envoi d'email/SMS
            });
            return utilisateur;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    @Transactional
    public Utilisateur saveAdmin(Utilisateur utilisateur, Long idGroupe) throws Exception {
        //String pwd = generateRandomPassword();
        Groupes groupe = groupesService.finById(idGroupe);

        utilisateur.setGroupes(groupe);
        System.out.println("Mot de passe temporaire pour " + utilisateur.getEmail() + ": " + utilisateur.getPassWord());

        utilisateur.setPassWord(passwordEncoder.encode(utilisateur.getPassWord()));
        utilisateur.setStatus(Status.ACTIF);
        utilisateur.setFirstConnexion(true);
        try {
            saveIt(utilisateur);
            // Envoyer le mot de passe à l'administrateur de manière sécurisée
            System.out.println("Mot de passe temporaire pour " + utilisateur.getEmail() + ": " + utilisateur.getPassWord());
            return utilisateur;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
    @Override
    public Utilisateur saveIt(Utilisateur utilisateur) throws Exception {
        checkIfUserExist(utilisateur);
        return repository.save(utilisateur);
    }
    @Override
    public Utilisateur save(Utilisateur utilisateur) throws Exception {
        checkIfUserExist(utilisateur);

        return repository.save(utilisateur);
    }



    private void checkIfUserExist(Utilisateur utilisateur) throws Exception {
        if (repository.findUtilisateurByEmail(utilisateur.getEmail()).isPresent()) {
            throw new Exception("Email déjà utilisé");
        }
        if (repository.findByNumero(utilisateur.getNumero()).isPresent()) {
            throw new Exception("Numéro de téléphone déjà utilisé");
        }
    }

    private Utilisateur getUser() throws Exception {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return findByName(username);
    }

    @Override
    public void disableUtilisateur(Long id) throws Exception {
        Utilisateur utilisateur = findById(id);
        utilisateur.setStatus(utilisateur.getStatus() == Status.ACTIF ? Status.INNATIF : Status.ACTIF);
        repository.save(utilisateur);
    }

    @Override
    public Utilisateur findByName(String name) {
        return repository.findUtilisateurByLogin(name)
                .orElseThrow(() -> new UsernameNotFoundException("User with name = " + name + " doesn't exist"));
    }

    @Override
    public Utilisateur login(String login) throws Exception {
        Optional<Utilisateur> user = repository.findUtilisateurByNom(login);
        if (user.isEmpty()) {
            user = repository.findUtilisateurByLogin(login);
            if(user.isEmpty())
                new UsernameNotFoundException("User with username " + login + " don't exist");
        }
        return (user.get());
    }
    public void activeOrDesactive(Long id) throws Exception {
        disableUtilisateur(id); // Réutilise la méthode disableUtilisateur pour la logique
                                // d'activation/désactivation
    }

    @Override
    public String update(MultipartFile file, Long id) throws Exception {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public Map<String, Integer> statusListSave() throws Exception {
        throw new UnsupportedOperationException("Unimplemented method 'statusListSave'");
    }

    @Override
    public List<Utilisateur> list() {

        return List.of();
    }

    /*private String generateRandomPassword() {
        return RandomStringUtils.randomAlphanumeric(12);
    }*/
}