package org.enigma.livecodeloan.service;

import org.enigma.livecodeloan.model.request.InstalmentTypeRequest;
import org.enigma.livecodeloan.model.response.InstalmentTypeResponse;

import java.util.List;

public interface InstalmentTypeService {
    InstalmentTypeResponse create(InstalmentTypeRequest instalmentTypeRequest);

    InstalmentTypeResponse update(InstalmentTypeRequest instalmentTypeRequest);

    InstalmentTypeResponse getById(String id);

    List<InstalmentTypeResponse> getAll();

    void delete(String id);
}
