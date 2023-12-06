package ru.amisfloff.studyplatform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "course")
@Getter
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @Setter
    @Column(name = "title", nullable = false)
    String title;

    @Setter
    @Column(name = "author", nullable = false)
    String author;
}
