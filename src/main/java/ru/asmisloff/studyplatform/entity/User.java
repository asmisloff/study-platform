package ru.asmisloff.studyplatform.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private final Long id = null;

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
    private OffsetDateTime registrationTime;

    @Column(name = "last_update_time")
    private OffsetDateTime lastUpdateTime;

    @Column(name = "deletion_time")
    private OffsetDateTime deletionTime;

    @Column(name = "last_updated_user_id")
    private Long lastUpdatedUserId;

    @Column(name = "deleted_user_id")
    private Long deletedUserId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = { @JoinColumn(name = "user_id") },
        inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<Role> roles = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "students_to_courses",
        joinColumns = { @JoinColumn(name = "user_id") },
        inverseJoinColumns = { @JoinColumn(name = "course_id") }
    )
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Set<Course> relatedCourses = new HashSet<>();

    public User(
        String login,
        String password,
        String firstName,
        String lastName,
        String email
    ) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.registrationTime = OffsetDateTime.now();
        this.roles = new HashSet<>();
    }

    public void subscribeToCourse(Course course) {
        relatedCourses.add(course);
    }

    public Stream<Course> relatedCourses() {
        return relatedCourses.stream();
    }

    public void updateRoles(Collection<Role> roles) {
        this.roles.clear();
        this.roles.addAll(roles);
    }
}
