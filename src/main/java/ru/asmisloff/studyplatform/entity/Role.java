package ru.asmisloff.studyplatform.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "roles")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    @Column(name = "id")
    Long id;

    @Column(name = "name", length = 12, nullable = false)
    String name;
}
