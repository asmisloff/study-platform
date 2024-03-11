package ru.asmisloff.studyplatform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name", length = 12, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleName name;

    public String getNameAsString() {
        return name.name();
    }
}
