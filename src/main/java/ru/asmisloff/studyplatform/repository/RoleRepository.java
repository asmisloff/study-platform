package ru.asmisloff.studyplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.asmisloff.studyplatform.entity.Role;

@Repository
interface RoleRepository extends JpaRepository<Role, Long> {
}
