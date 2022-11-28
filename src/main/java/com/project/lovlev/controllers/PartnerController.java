package com.project.lovlev.controllers;

import com.project.lovlev.models.Partner;
import com.project.lovlev.models.SecurityUser;
import com.project.lovlev.models.User;
import com.project.lovlev.repositories.PartnerRepository;
import com.project.lovlev.repositories.UserRepository;
import com.project.lovlev.services.IAuthenticationFacade;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@Data
@RestController
public class PartnerController {
    PartnerRepository partnerRepository;
    UserRepository userRepository;
    IAuthenticationFacade authentication;


    public PartnerController(PartnerRepository partnerRepository,
                             UserRepository userRepository,
                             IAuthenticationFacade authentication) {
        this.partnerRepository = partnerRepository;
        this.userRepository = userRepository;
        this.authentication = authentication;
    }

//    authorize both role_admin and role_user
    @GetMapping("/partners")
    public ResponseEntity<Iterable<Partner>> findAllPartners() {

        Iterable<Partner> partners = partnerRepository.
                getAllPartnersByUserId(authentication.getUserId());
        return new ResponseEntity<>(partners, HttpStatus.OK);
    }

    // Create partner
    @PostMapping(path = "/partner",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Partner> addPartner(@RequestBody @Valid Partner newPartner) {
        User user = userRepository.getById(authentication.getUserId());
        user.addPartner(newPartner);
        userRepository.save(user);
        return new ResponseEntity<>(newPartner, HttpStatus.CREATED);
    }
}