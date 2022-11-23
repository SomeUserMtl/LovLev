package com.project.lovlev.controllers;

import com.project.lovlev.models.Partner;
import com.project.lovlev.repositories.PartnerRepository;
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

    public PartnerController(PartnerRepository partnerRepository) {
        this.partnerRepository = partnerRepository;
    }

    // Get partner by partnerId
    @GetMapping(value = "partner")
    public ResponseEntity<Partner> getPartnerById(@RequestParam Optional<Long> partnerId){
        Optional<Partner> partner = Optional
                        .ofNullable(partnerRepository
                        .getPartnerById(partnerId.orElseThrow()));

        return partner.map(value
                -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(()
                -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Get list of partners
    @GetMapping("partners")
    @ResponseBody
    public ResponseEntity<List<Partner>> getPartners() {
        List<Partner> partners = partnerRepository.findAll();

        if (partners.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return new ResponseEntity<>(partners, HttpStatus.OK);
    }

    // Add a new partner
    @PostMapping(path = "partner",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Partner> addPartner(@RequestBody Partner newPartner) {
        Partner partner = partnerRepository.save(newPartner);
        return new ResponseEntity<>(partner, HttpStatus.CREATED);
    }
}
