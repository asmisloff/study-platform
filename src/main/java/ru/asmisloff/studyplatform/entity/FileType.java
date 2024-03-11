package ru.asmisloff.studyplatform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "file_types")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FileType {

    @Id
    @Column(name = "id")
    Long id;

    @Column(name = "name")
    @Enumerated(EnumType.STRING)
    MediaType mediaType;
}
