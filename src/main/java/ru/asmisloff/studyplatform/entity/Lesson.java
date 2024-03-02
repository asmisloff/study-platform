package ru.asmisloff.studyplatform.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.*;

@Getter
@Setter
@Entity
@Table(name = "lessons")
@NoArgsConstructor
public class Lesson {

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

    @Column(name = "content")
    private String content;

    @OneToMany
    @JoinColumn(name = "contained_lesson_id")
    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Set<Lesson> nested = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "contained_lesson_id")
    @Setter(AccessLevel.NONE)
    private Lesson containedLesson;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "idx", nullable = false)
    private int index;

    public Lesson(
        String title,
        String description,
        int index,
        User createdUser,
        Course course
    ) {
        this.title = title;
        this.description = description;
        this.createdUser = createdUser;
        this.course = course;
        this.index = index;
        this.creationTime = OffsetDateTime.now();
    }

    public List<Lesson> getNested() {
        return nested.stream().sorted(Comparator.comparingInt(Lesson::getIndex)).toList();
    }

    public void addNested(@NotNull Lesson lesson) {
        Objects.requireNonNull(lesson);
        if (lesson == this) {
            throw new IllegalArgumentException();
        }
        if (lesson.course != this.course) {
            throw new IllegalArgumentException();
        }
        if (lesson.containedLesson != null && lesson.containedLesson != this) {
            containedLesson.removeNested(lesson);
        }
        lesson.containedLesson = this;
        this.nested.add(lesson);
    }

    public void removeNested(Lesson lesson) {
        if (lesson == null || lesson.containedLesson != this) {
            return;
        }
        lesson.containedLesson = null;
        nested.remove(lesson);
    }

    public boolean hasNested() {
        return !nested.isEmpty();
    }
}