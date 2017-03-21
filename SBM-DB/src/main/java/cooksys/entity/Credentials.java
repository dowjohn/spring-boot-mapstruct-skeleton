package cooksys.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Embeddable
public class Credentials {
    @NotNull
    @Column(unique = true, nullable = false)
    private String username;

    @NotNull
    @Column(nullable = false)
    private String password;
}
