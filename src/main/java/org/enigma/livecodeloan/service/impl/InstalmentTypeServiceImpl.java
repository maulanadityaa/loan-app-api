package org.enigma.livecodeloan.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.livecodeloan.model.entity.InstalmentType;
import org.enigma.livecodeloan.model.request.InstalmentTypeRequest;
import org.enigma.livecodeloan.model.response.InstalmentTypeResponse;
import org.enigma.livecodeloan.repository.InstalmentTypeRepository;
import org.enigma.livecodeloan.service.InstalmentTypeService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstalmentTypeServiceImpl implements InstalmentTypeService {
    private final InstalmentTypeRepository instalmentTypeRepository;

    @Override
    public InstalmentTypeResponse create(InstalmentTypeRequest instalmentTypeRequest) {
        InstalmentType instalmentType = toInstalmentType(instalmentTypeRequest);
        instalmentTypeRepository.save(instalmentType);

        return toInstalmentTypeResponse(instalmentType);
    }

    @Override
    public InstalmentTypeResponse update(InstalmentTypeRequest instalmentTypeRequest) {
        InstalmentType instalmentType = instalmentTypeRepository.findById(instalmentTypeRequest.getId()).orElse(null);

        if (instalmentType != null) {
            instalmentType = InstalmentType.builder()
                    .id(instalmentType.getId())
                    .instalmentType(instalmentTypeRequest.getInstalmentType())
                    .build();
            instalmentTypeRepository.save(instalmentType);

            return toInstalmentTypeResponse(instalmentType);
        }

        return null;
    }

    @Override
    public InstalmentTypeResponse getById(String id) {
        InstalmentType instalmentType = instalmentTypeRepository.findById(id).orElse(null);

        if (instalmentType != null) {
            return toInstalmentTypeResponse(instalmentType);
        }

        return null;
    }

    @Override
    public List<InstalmentTypeResponse> getAll() {
        List<InstalmentType> instalmentTypes = instalmentTypeRepository.findAll();
        List<InstalmentTypeResponse> instalmentTypeResponseList = new ArrayList<>();

        for (InstalmentType instalmentType : instalmentTypes) {
            instalmentTypeResponseList.add(toInstalmentTypeResponse(instalmentType));
        }

        return instalmentTypeResponseList;
    }

    @Override
    public void delete(String id) {
        InstalmentType instalmentType = instalmentTypeRepository.findById(id).orElse(null);

        if (instalmentType != null) {
            instalmentTypeRepository.delete(instalmentType);
        }
    }

    private static InstalmentTypeResponse toInstalmentTypeResponse(InstalmentType instalmentType) {
        return InstalmentTypeResponse.builder()
                .id(instalmentType.getId())
                .instalmentType(instalmentType.getInstalmentType().name())
                .build();
    }

    private static InstalmentType toInstalmentType(InstalmentTypeRequest instalmentTypeRequest) {
        return InstalmentType.builder()
                .instalmentType(instalmentTypeRequest.getInstalmentType())
                .build();
    }
}
