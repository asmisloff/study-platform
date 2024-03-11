package ru.asmisloff.studyplatform.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "files")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FileAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "client_name", length = 100, nullable = false)
    String clientName;

    @Column(name = "actual_name", length = 16, nullable = false)
    String actualName;

    @ManyToOne(optional = false)
    FileType type;

    @Column(name = "thumbnail", nullable = false, length = 16)
    String thumbnail;

    @Column(name = "bucket", nullable = false, length = 16)
    String bucket;
}
