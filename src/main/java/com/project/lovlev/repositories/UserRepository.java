package com.project.lovlev.repositories;

import com.project.lovlev.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    //get by id
    User getById(Long id);

    //find by username
    @Query("SELECT u FROM users u WHERE u.username = :username")
    Optional<User> findUserByUsername(String username) throws UsernameNotFoundException;

    //create or update user
    @Modifying
    @Transactional
    User save(User user);

    //find all users
    Iterable<User> findAll();

    //delete user
    @Transactional
    void deleteUserById(Long id);

    //delete multiple users
    @Transactional
    void deleteAllByIdIn(Collection<Long> id);
}
