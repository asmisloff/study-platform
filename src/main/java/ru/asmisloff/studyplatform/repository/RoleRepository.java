package ru.asmisloff.studyplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.asmisloff.studyplatform.entity.Role;

import java.util.List;
import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r FROM Role r WHERE r.name in :roleNames")
    Set<Role> findAllByName(List<String> roleNames);
}
