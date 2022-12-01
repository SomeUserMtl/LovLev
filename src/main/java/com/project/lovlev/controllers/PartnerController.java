package com.project.lovlev.controllers;

import com.project.lovlev.exceptions.NotFoundException;
import com.project.lovlev.models.Partner;
import com.project.lovlev.models.User;
import com.project.lovlev.repositories.PartnerRepository;
import com.project.lovlev.repositories.UserRepository;
import com.project.lovlev.services.security.IAuthenticationAccess;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

@Data
@RestController
public class PartnerController {
    PartnerRepository partnerRepository;
    UserRepository userRepository;
    IAuthenticationAccess authentication;

    public PartnerController(PartnerRepository partnerRepository,
                             UserRepository userRepository,
                             IAuthenticationAccess authentication) {
        this.partnerRepository = partnerRepository;
        this.userRepository = userRepository;
        this.authentication = authentication;
    }

    // Get partner by partnerId
    @GetMapping(value = "/partner/{id}")
    public ResponseEntity<Partner> getById(@PathVariable Long id) {

        User user = userRepository.getById(authentication.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (authentication.returnRole("ROLE_ADMIN") || user
                .getPartners()
                .stream()
                .anyMatch(partner -> partner.getId().equals(id)))
            return partnerRepository
                    .getById(id)
                    .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    // Get all partners
    @GetMapping("/partners/{userid}")
    public ResponseEntity<Set<Partner>> findAllPartners(@PathVariable(required = false) Optional<Long> userid) {

        if (authentication.returnRole("ROLE_ADMIN")) {
            return userid.map(aLong
                    -> new ResponseEntity<>(userRepository.getById(aLong).orElseThrow(()
                    -> new NotFoundException("User not found")).getPartners(), HttpStatus.OK)).orElseGet(()
                    -> new ResponseEntity<>(partnerRepository.findAll(), HttpStatus.OK));
        }
        else if (userid.isPresent() && userid.get().equals(authentication.getUserId())) {
            return new ResponseEntity<>(userRepository.getById(userid.get()).orElseThrow(()
                    -> new NotFoundException("User not found")).getPartners(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }


    // Create partner
    @PostMapping(path = "/partner",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Partner> create(@RequestBody @Valid Partner newPartner) {
        User user = userRepository
                .getById(authentication.getUserId())
                .orElseThrow(() -> new NotFoundException("Partner not found with id " + authentication.getUserId()));
        user.addPartner(newPartner);
        userRepository.save(user);
//        Partner partner = partnerRepository.save(newPartner);
        return new ResponseEntity<>(newPartner, HttpStatus.CREATED);
    }
}