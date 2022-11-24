package com.project.lovlev.repositories;

import com.project.lovlev.models.Partner;
import com.project.lovlev.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface PartnerRepository extends CrudRepository<Partner, Integer> {

    //get partner using id
    Partner getById(Long id);
    //get list of all partners
    List<Partner> findAll();

    //find by user
    List<Partner> findByUser(User user);

    //add partner record to partner table
    @Transactional
    Partner save(Partner partner);

}
