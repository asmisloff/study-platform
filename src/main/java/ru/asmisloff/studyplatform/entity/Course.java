package ru.asmisloff.studyplatform.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "course")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Course {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    @Setter
    private String title;

    @Column(name = "author", nullable = false)
    @Setter
    private String author;
}
