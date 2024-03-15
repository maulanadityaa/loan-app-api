package org.enigma.livecodeloan.service;

import org.enigma.livecodeloan.constant.ERole;
import org.enigma.livecodeloan.model.entity.Role;

public interface RoleService {
    Role getOrSave(ERole role);

    Role getByName(ERole name);
}
