package org.enigma.livecodeloan.service.impl;

import lombok.RequiredArgsConstructor;
import org.enigma.livecodeloan.model.entity.InstalmentType;
import org.enigma.livecodeloan.model.exception.ApplicationException;
import org.enigma.livecodeloan.model.request.InstalmentTypeRequest;
import org.enigma.livecodeloan.model.response.InstalmentTypeResponse;
import org.enigma.livecodeloan.repository.InstalmentTypeRepository;
import org.enigma.livecodeloan.service.InstalmentTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InstalmentTypeServiceImpl implements InstalmentTypeService {
    private final InstalmentTypeRepository instalmentTypeRepository;

    @Override
    public InstalmentTypeResponse create(InstalmentTypeRequest instalmentTypeRequest) {
        try {
            InstalmentType instalmentType = toInstalmentType(instalmentTypeRequest);
            instalmentTypeRepository.save(instalmentType);

            return toInstalmentTypeResponse(instalmentType);
        } catch (Exception e) {
            throw new ApplicationException("Instalment type cant created", String.format("Cannot create instalment with type=%s", instalmentTypeRequest.getInstalmentType()), HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public InstalmentTypeResponse update(InstalmentTypeRequest instalmentTypeRequest) {
        InstalmentType instalmentType = instalmentTypeRepository.findById(instalmentTypeRequest.getId()).orElseThrow(() -> new ApplicationException("Instalment type not found", String.format("Cannot find instalment type with id=%s", instalmentTypeRequest.getId()), HttpStatus.NOT_FOUND));

        instalmentType = InstalmentType.builder()
                .id(instalmentType.getId())
                .instalmentType(instalmentTypeRequest.getInstalmentType())
                .build();
        instalmentTypeRepository.save(instalmentType);

        return toInstalmentTypeResponse(instalmentType);
    }

    @Override
    public InstalmentTypeResponse getById(String id) {
        InstalmentType instalmentType = instalmentTypeRepository.findById(id).orElseThrow(() -> new ApplicationException("Instalment type not found", String.format("Cannot find instalment type with id=%s", id), HttpStatus.NOT_FOUND));

        return toInstalmentTypeResponse(instalmentType);
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
        InstalmentType instalmentType = instalmentTypeRepository.findById(id).orElseThrow(() -> new ApplicationException("Instalment type not found", String.format("Cannot find instalment type with id=%s", id), HttpStatus.NOT_FOUND));

        instalmentTypeRepository.delete(instalmentType);
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
