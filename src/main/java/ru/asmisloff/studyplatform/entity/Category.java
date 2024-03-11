package ru.asmisloff.studyplatform.entity;

@Entity
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Category {

    @Id
    @Column(name = "id")
    @NotNull
    private Long id;

    @Column(name = "name", length = 100, nullable = false)
    @NotNull
    private String name;

    @Column(name = "description", length = 300)
    private String description;
}
