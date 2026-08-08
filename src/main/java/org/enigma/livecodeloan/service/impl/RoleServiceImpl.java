package org.enigma.livecodeloan.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.livecodeloan.constant.ERole;
import org.enigma.livecodeloan.model.entity.Role;
import org.enigma.livecodeloan.model.exception.ApplicationException;
import org.enigma.livecodeloan.repository.RoleRepository;
import org.enigma.livecodeloan.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role getOrSave(ERole role) {
        Optional<Role> optionalRole = roleRepository.findByName(role);

        if (optionalRole.isPresent()) {
            return optionalRole.get();
        }

        Role currentRole = Role.builder()
                .name(role)
                .build();
        return roleRepository.save(currentRole);
    }

    @Override
    public Role getByName(ERole name) {
        return roleRepository.findByName(name).orElseThrow(() -> new ApplicationException("Role not found", String.format("Cannot find role with name=%s", name), HttpStatus.NOT_FOUND));
    }
}
