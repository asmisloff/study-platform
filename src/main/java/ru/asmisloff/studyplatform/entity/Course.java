package ru.asmisloff.studyplatform.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "courses")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Course {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    @Setter
    private String title;

    @Column(name = "description")
    @Setter
    private String description;

//    @Column(name = "created_user_id")
//    @Setter
//    private User author;
}
