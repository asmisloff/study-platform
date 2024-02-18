package ru.asmisloff.studyplatform.entity;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "learning_statuses")
@Getter
public class LearningStatus {

    @Id
    @Column(name = "id")
    @NotNull
    Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    @NotNull
    Status status;
}
