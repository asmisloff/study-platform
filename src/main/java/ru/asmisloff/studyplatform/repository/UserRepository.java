package ru.asmisloff.studyplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.asmisloff.studyplatform.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT count(*) > 0 FROM User u WHERE u.id = :id")
    Boolean isActive(long id);
}
