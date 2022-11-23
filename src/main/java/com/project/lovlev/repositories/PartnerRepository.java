package com.project.lovlev.repositories;

import com.project.lovlev.models.Partner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository extends CrudRepository<Partner, Integer> {

    //get partner using id
    Partner getPartnerById(Long id);

    //get list of all partners
    List<Partner> findAll();

    //add partner record to partner table
    Partner save(Partner partner);

}
