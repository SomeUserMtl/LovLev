package com.project.lovlev.repositories;

import com.project.lovlev.models.SecurityUser;
import com.project.lovlev.models.User;
import com.project.lovlev.services.AuthenticationFacade;
import com.project.lovlev.services.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import javax.validation.constraints.Null;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    //get by id
    User getById(Long id);

    //find by username
    @Query("SELECT u FROM users u WHERE u.username = :username")
    Optional<User> findUserByUsername(String username) throws UsernameNotFoundException;

    //add user record to user table
    @Transactional
    User save(User user);

    //find all users
    Iterable<User> findAll();

//    //delete user
    @Transactional
    void deleteUserById(Long id);


    //delete multiple users
    @Transactional
    void deleteAllByIdIn(Collection<Long> id);

    // update user by id
//    @Modifying
//    @Transactional
//    @Query("UPDATE users u SET u.username = :username, u.password = :password, u.email = :email, u.roles = :role WHERE u.id = :id")
//    void updateUserById(String username, String password, String email, String role, Long id);
}
