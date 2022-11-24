package com.project.lovlev.models;

import com.project.lovlev.enums.Colors;
import com.project.lovlev.enums.Sex;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "partner")
public class Partner{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "age")
    private int age;

    @Column(name = "height_feet")
    private int heightFeet;

    @Column(name = "height_inches")
    private int heightInches;

    @Column(name = "hair_color")
    @Enumerated(EnumType.ORDINAL)
    private Colors hairColor;

    @Column(name = "eye_color")
    @JoinColumn(name="user_id", nullable=false)
    @Enumerated(EnumType.ORDINAL)
    private Colors eyeColor;

    @Enumerated(EnumType.ORDINAL)
    private Sex sex;

    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
}
