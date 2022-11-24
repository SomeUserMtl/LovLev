package com.project.lovlev.controllers;

import com.project.lovlev.models.Partner;
import com.project.lovlev.models.User;
import com.project.lovlev.repositories.PartnerRepository;
import com.project.lovlev.repositories.UserRepository;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "partner")
    public ResponseEntity<Partner> getById(@RequestParam Long id){
        Optional<Partner> partner = Optional
                        .ofNullable(partnerRepository
                        .getById(id));

        return partner.map(value
                -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(()
                -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get list of partners
//    @GetMapping("partners")
//    @ResponseBody
//    public ResponseEntity<List<Partner>> getPartners() {
//        List<Partner> partners = partnerRepository.findAll();
//
//        if (partners.isEmpty())
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//
//        return new ResponseEntity<>(partners, HttpStatus.OK);
//    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("partners")
    public ResponseEntity<Iterable<Partner>> getAllUsers() {
        Iterable<Partner> partners = partnerRepository.findAll();
        return new ResponseEntity<>(partners, HttpStatus.OK);
    }

    // Add a new partner
    @PostMapping(path = "partner",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Partner> addPartner(@RequestBody Partner newPartner) {
        User user = userRepository.getById(8L);
        newPartner.setUser(user);
        Partner partner = partnerRepository.save(newPartner);
        return new ResponseEntity<>(partner, HttpStatus.CREATED);
    }
}