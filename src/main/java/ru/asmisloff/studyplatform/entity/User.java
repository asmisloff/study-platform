package ru.asmisloff.studyplatform.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "login", nullable = false, length = 50)
    private String login;

    @Column(name = "pwd", nullable = false, length = 150)
    private String password;

    @Column(name = "first_name", nullable = false, length = 150)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 150)
    private String lastName;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "phone", length = 50)
    private String phone;

    @OneToOne
    @JoinColumn(name = "avatar_id")
    private FileAsset avatar;

    @Column(name = "registration_time")
    private LocalDateTime registrationTime;

    @Column(name = "last_update_time")
    private LocalDateTime lastUpdateTime;

    @Column(name = "deletion_time")
    private LocalDateTime deletionTime;

    @Column(name = "last_updated_user_id")
    private Long lastUpdatedUserId;

    @Column(name = "deleted_user_id")
    private Long deletedUserId;

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") })
    @Setter(AccessLevel.NONE)
    private Set<Role> roles;

    public User(String login, String password,
                String firstName, String lastName,
                String email) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registrationTime = LocalDateTime.now();
        this.roles = new HashSet<>();
    }
}

