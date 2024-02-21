package ru.asmisloff.studyplatform.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "paragraphs")
@NoArgsConstructor
public class Paragraph {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_user_id")
    private User createdUser;

    @ManyToOne
    @JoinColumn(name = "last_updated_user_id")
    private User lastUpdatedUser;

    @ManyToOne
    @JoinColumn(name = "deleted_user_id")
    private User deletedUser;

    @Column(name = "creation_time", nullable = false)
    private OffsetDateTime creationTime;

    @Column(name = "last_update_time")
    private OffsetDateTime lastUpdateTime;

    @Column(name = "deletion_time")
    private OffsetDateTime deletionTime;

    @OneToMany(mappedBy = "paragraph", orphanRemoval = true, cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private List<Lesson> lessons = new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "idx", nullable = false)
    private int index;

    public Paragraph(String title, String description, User createdUser, Course course, int index) {
        this.title = title;
        this.description = description;
        this.createdUser = createdUser;
        this.course = course;
        this.index = index;
        creationTime = OffsetDateTime.now();
    }

    public boolean addLesson(Lesson lesson) {
        return lessons.add(lesson);
    }
}