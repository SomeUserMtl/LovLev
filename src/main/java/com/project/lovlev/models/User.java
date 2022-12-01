package com.project.lovlev.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.lovlev.enums.Sex;
import com.project.lovlev.services.validation.UserPost;
import com.project.lovlev.services.validation.UserPut;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "users")
public class User {
    @Null(message = "id must be null", groups = {UserPost.class, UserPut.class})
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[a-zA-Z\s]{0,20}$", message = "First name can only contain letters or spaces", groups = {UserPost.class, UserPut.class})
    @Column(name = "first_name")
    private String firstName;

    @Pattern(regexp = "^[a-zA-Z\s]{0,20}$", message = "Middle name can only contain letters or spaces", groups = {UserPost.class, UserPut.class})
    @Column(name = "middle_name")
    private String middleName;

    @Pattern(regexp = "^[a-zA-Z\s]{0,20}$", message = "last name can only contain letters or spaces", groups = {UserPost.class, UserPut.class})
    @Column(name = "last_name")
    private String lastName;

    @Email(message = "Email should be valid", groups = {UserPost.class, UserPut.class})
    @NotBlank(message = "Email is required", groups = UserPost.class)
    @Length(max = 50, message = "Email can't be longer than 50 characters", groups = {UserPost.class, UserPut.class})
    @Column(name = "email", unique = true)
    private String email;

    @NotBlank(message = "Username is required", groups = UserPost.class)
    @Pattern(regexp = "^[a-zA-Z0-9]{3,15}$", message = "Username can only contain letters, numbers and must be between 4-8 characters",groups = {UserPost.class, UserPut.class})
    @Column(name = "username",unique = true)
    private String username;

    @NotBlank(message = "Password is required", groups = UserPost.class)
    @Pattern(regexp = "^.[^\s]{4,100}", message = "password must be between 4-100 characters, no spaces allowed", groups = {UserPost.class, UserPut.class})
    @Column(name = "password")
    private String password;

    @Pattern(regexp = "^ROLE_[A-Z]{1,10}$", message = "role must only contain uppercase letters of th following format: ROLE_[A-Z]", groups = {UserPost.class, UserPut.class})
    @Column(name = "roles")
    private String roles;

    @Column(name = "sex")
    @Enumerated(EnumType.ORDINAL)
    private Sex sex;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private Set<Partner> partners = new HashSet<>();

    //remove partner
    public void removePartner(Partner partner){
        partners.remove(partner);
    }

    public void addPartner(Partner newPartner) {
        partners.add(newPartner);
    }
}
