package cooksys.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Profile {
    private String firstName;

    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;
}
