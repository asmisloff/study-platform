package ru.asmisloff.studyplatform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "learning_statuses")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LearningStatus {

    @Id
    @Column(name = "id")
    Long id;

    @Column(name = "name", length = 16, nullable = false)
    @Enumerated(EnumType.STRING)
    Status status;
}
