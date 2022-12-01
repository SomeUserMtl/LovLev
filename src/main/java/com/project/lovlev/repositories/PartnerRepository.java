package com.project.lovlev.repositories;

import com.project.lovlev.models.Partner;
import com.project.lovlev.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PartnerRepository extends CrudRepository<Partner, Integer> {

    //get all partners with user_id = id
    @Query(value = "SELECT * FROM partner WHERE user_id = ?1", nativeQuery = true)
    List<Partner> getAllPartnersByUserId(Long id);

    //get partner by id
    Optional<Partner> getById(Long id);

    //findall
    Set<Partner> findAll();

    //get partner using id
//    @Query("SELECT p FROM partner p WHERE p.id = :id AND p.user.id = :userId")
//    Partner getByIdAndUser(Long id, Long userId);
//
    //get list of all partners
//    @Query("SELECT p FROM partner p WHERE p.id = :id")
//     List<Partner> findPartnersByUserId(Long id);
//
//    //find by user
//
//    List<Partner> findByUser(User user);
//
//    //add partner record to partner table
//    @Transactional
//    Partner save(Partner partner);
}
