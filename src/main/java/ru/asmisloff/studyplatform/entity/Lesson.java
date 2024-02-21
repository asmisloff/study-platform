package ru.asmisloff.studyplatform.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "lessons")
@NoArgsConstructor
public class Lesson  implements Comparable<Lesson> {

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

    @ManyToOne(optional = false)
    @JoinColumn(name = "paragraph_id")
    private Paragraph paragraph;

    @Column(name = "idx", nullable = false)
    private int index;

    public Lesson(String title,
                  String description,
                  User createdUser,
                  Paragraph paragraph,
                  int index) {
        this.title = title;
        this.description = description;
        this.createdUser = createdUser;
        this.paragraph = paragraph;
        this.index = index;
        this.creationTime = OffsetDateTime.now();
    }

    @Override
    public int compareTo(@NotNull Lesson o) {
        return this.index - o.index;
    }
}