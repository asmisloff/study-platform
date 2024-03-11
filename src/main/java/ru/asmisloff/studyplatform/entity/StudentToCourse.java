package ru.asmisloff.studyplatform.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
