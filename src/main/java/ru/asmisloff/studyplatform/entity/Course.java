package ru.asmisloff.studyplatform.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
public class Course {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "estimated_duration")
    private Integer estimatedDuration;

    @Column(name = "tag", length = 32)
    private String tag;

    @OneToMany(mappedBy = "course", orphanRemoval = true, cascade = CascadeType.ALL)
    @Setter(AccessLevel.NONE)
    private Set<Lesson> lessons = new HashSet<>();

    public Course(String title, String description, User createdUser) {
        this.title = title;
        this.description = description;
        this.createdUser = createdUser;
    }

    public Stream<Lesson> sortedLessons() {
        return lessons.stream().sorted(Comparator.comparingInt(Lesson::getIndex));
    }

    public void addLesson(@NotNull Lesson lesson) {
        Objects.requireNonNull(lesson);
        if (lesson.getCourse() != null && lesson.getCourse() != this) {
            throw new IllegalArgumentException();
        }
        lesson.setCourse(this);
        lessons.add(lesson);
    }

    public void removeLesson(@NotNull Lesson lesson) {
        Objects.requireNonNull(lesson);
        lessons.remove(lesson);
    }
}
