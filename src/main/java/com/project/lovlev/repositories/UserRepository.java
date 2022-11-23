package com.project.lovlev.repositories;

import com.project.lovlev.models.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    //get by username and password
    User findByUsernameAndPassword(String username, String password);

    //find by username
    @Query("SELECT u FROM user u WHERE u.username = :username")
    User findUserByUsername(@Param("username") String username) throws UsernameNotFoundException;

    //add user record to user table
    User save(User user);

    //find all users
    Iterable<User> findAll();

    //delete user
    @Transactional
    void deleteById(Long id);

    //delete multiple users
    @Transactional
    void deleteAllByIdIn(Collection<Long> id);

    // update user by id
//    @Transactional
//    void updateUserById(Long id);
}
