package ru.asmisloff.studyplatform.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.OffsetDateTime;

@Entity
@Table(name = "students_to_courses")
@NoArgsConstructor
@Getter
@Setter
public class StudentToCourse {

    @EmbeddedId
    private StudentToCourseId id;

    @Column(name = "start_time")
    private OffsetDateTime startTime = OffsetDateTime.now();

    @Column(name = "completion_time")
    private OffsetDateTime completionTime;

    @Column(name = "score")
    private int score = 0;

    @Column(name = "course_rating")
    private Short courseRating;

    @Column(name = "review")
    private String review;

    public StudentToCourse(StudentToCourseId id) {
        this.id = id;
    }
}
