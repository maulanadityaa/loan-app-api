package org.enigma.livecodeloan.repository;

import org.enigma.livecodeloan.constant.ERole;
import org.enigma.livecodeloan.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByName(ERole name);
}
