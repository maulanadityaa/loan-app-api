package org.enigma.livecodeloan.service;

import org.enigma.livecodeloan.model.entity.UserRole;

public interface UserRoleService {
    UserRole save(UserRole userRole);

    UserRole getById(Long id);
}
