package org.enigma.livecodeloan.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.livecodeloan.model.entity.UserRole;
import org.enigma.livecodeloan.repository.UserRoleRepository;
import org.enigma.livecodeloan.service.UserRoleService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository userRoleRepository;

    @Override
    public UserRole save(UserRole userRole) {
        return null;
    }

    @Override
    public UserRole getById(Long id) {
        return null;
    }
}
