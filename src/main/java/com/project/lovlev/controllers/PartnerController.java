package com.project.lovlev.controllers;

import com.project.lovlev.models.Partner;
import com.project.lovlev.models.SecurityUser;
import com.project.lovlev.models.User;
import com.project.lovlev.repositories.PartnerRepository;
import com.project.lovlev.repositories.UserRepository;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Data
@RestController
public class PartnerController {
    PartnerRepository partnerRepository;
    UserRepository userRepository;

    public PartnerController(PartnerRepository partnerRepository,
                             UserRepository userRepository) {
        this.partnerRepository = partnerRepository;
        this.userRepository = userRepository;
    }

    // Get partner by partnerId
    @PreAuthorize("hasRole('ROLE_ADMIN, ROLE_USER')")
    @GetMapping(value = "partner")
    public ResponseEntity<Partner> getById(@RequestParam Long id){
        Optional<Partner> partner = Optional
                        .ofNullable(partnerRepository
                        .getById(id));

        return partner.map(value
                -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(()
                -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN, ROLE_USER')")
    @GetMapping("partners")
    public ResponseEntity<Iterable<Partner>> getAllUsers(Authentication authentication) {

        SecurityUser userPrincipal = (SecurityUser) authentication.getPrincipal();
        Iterable<Partner> partners = partnerRepository.findAll(userPrincipal.getCurrentUserId());
        return new ResponseEntity<>(partners, HttpStatus.OK);
    }

    // Add a new partner
    @PostMapping(path = "partner",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Partner> addPartner(@RequestBody @Valid Partner newPartner,
                                              Authentication authentication) {
        SecurityUser userPrincipal = (SecurityUser) authentication.getPrincipal();
        User user = userRepository.getById(userPrincipal.getCurrentUserId());
        newPartner.setUser(user);
        Partner partner = partnerRepository.save(newPartner);
        return new ResponseEntity<>(partner, HttpStatus.CREATED);
    }
}