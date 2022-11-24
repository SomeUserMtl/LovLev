package com.project.lovlev.models;

import com.project.lovlev.enums.Colors;
import com.project.lovlev.enums.Sex;
import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@Entity(name = "partner")
public class Partner{

    @Null(message = "id must be null")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^[a-zA-Z\s]{0,20}$", message = "First name can only contain letters or spaces")
    @Column(name = "first_name")
    private String firstName;

    @Pattern(regexp = "^[a-zA-Z\s]{0,20}$", message = "Middle name can only contain letters or spaces")
    @Column(name = "middle_name")
    private String middleName;

    @Pattern(regexp = "^[a-zA-Z\s]{0,20}$", message = "last name can only contain letters or spaces")
    @Column(name = "last_name")
    private String lastName;

    @Pattern(regexp = "^[a-zA-Z0-9]{0,20}$", message = "nickname can only contain letters or numbers")
    @Column(name = "nickname")
    private String nickname;

    @Min(value = 0, message = "age must be positive")
    @Max(value = 120, message = "age must be less than 120")
    @Column(name = "age")
    private int age;

    @Min(value = 0, message = "feet height must be greater than 0")
    @Max(value = 8, message = "feet height must be less than 8")
    @Column(name = "height_feet")
    private int heightFeet;

    @Min(value = 0, message = "inches height must be greater than 0")
    @Max(value = 11, message = "inches height must be less than 11")
    @Column(name = "height_inches")
    private int heightInches;

    @Column(name = "hair_color")
    @Enumerated(EnumType.ORDINAL)
    private Colors hairColor;

    @Column(name = "eye_color")
    @JoinColumn(name="user_id", nullable=false)
    @Enumerated(EnumType.ORDINAL)
    private Colors eyeColor;

    @Column(name = "sex")
    @Enumerated(EnumType.ORDINAL)
    private Sex sex;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
}
