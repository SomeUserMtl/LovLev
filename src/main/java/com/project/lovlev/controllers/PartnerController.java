package com.project.lovlev.controllers;

import com.project.lovlev.models.Partner;
import com.project.lovlev.services.security.SecurityUser;
import com.project.lovlev.models.User;
import com.project.lovlev.repositories.PartnerRepository;
import com.project.lovlev.repositories.UserRepository;
import com.project.lovlev.services.security.IAuthenticationFacade;
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

    // Get partner by partnerId
//    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
//    @GetMapping(value = "partner")
//    public ResponseEntity<Partner> getById(@RequestParam Long id, Authentication authentication) {
//
//        SecurityUser userPrincipal = (SecurityUser) authentication.getPrincipal();
//
//
//        Optional<Partner> partner = Optional
//                        .ofNullable(partnerRepository
//                        .getByIdAndUser(id,userPrincipal.getCurrentUserId()));
//
//        return partner.map(value
//                -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(()
//                -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//    }

//    authorize both role_admin and role_user
    @GetMapping("/user/partners")
    public ResponseEntity<Iterable<Partner>> findAllPartners() {

        Iterable<Partner> partners = partnerRepository.
                getAllPartnersByUserId(authentication.getUserId());
        return new ResponseEntity<>(partners, HttpStatus.OK);
    }


    // Create partner
    @PostMapping(path = "/user/partner",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Partner> addPartner(@RequestBody @Valid Partner newPartner,
                                              Authentication authentication) {
        SecurityUser userPrincipal = (SecurityUser) authentication.getPrincipal();
        User user = userRepository.getById(userPrincipal.getCurrentUserId());
        user.addPartner(newPartner);
        userRepository.save(user);
//        Partner partner = partnerRepository.save(newPartner);
        return new ResponseEntity<>(newPartner, HttpStatus.CREATED);
    }
}