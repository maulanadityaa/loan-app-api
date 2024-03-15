package org.enigma.livecodeloan.controller;

import lombok.RequiredArgsConstructor;
import org.enigma.livecodeloan.model.request.InstalmentTypeRequest;
import org.enigma.livecodeloan.model.response.CommonResponse;
import org.enigma.livecodeloan.model.response.InstalmentTypeResponse;
import org.enigma.livecodeloan.service.InstalmentTypeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/instalment-types")
@RequiredArgsConstructor
public class InstalmentTypeController {
    private final InstalmentTypeService instalmentTypeService;

    @PostMapping
    public ResponseEntity<?> createInstalmentType(@RequestBody InstalmentTypeRequest instalmentTypeRequest) {
        InstalmentTypeResponse instalmentTypeResponse = instalmentTypeService.create(instalmentTypeRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<InstalmentTypeResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Instalment type created successfully")
                        .data(instalmentTypeResponse)
                        .build());
    }

    @GetMapping
    public ResponseEntity<?> getAllInstalmentTypes() {
        List<InstalmentTypeResponse> instalmentTypeResponses = instalmentTypeService.getAll();

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<List<InstalmentTypeResponse>>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Instalment types data retrieved successfully")
                        .data(instalmentTypeResponses)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getInstalmentTypeById(@PathVariable String id) {
        InstalmentTypeResponse instalmentTypeResponse = instalmentTypeService.getById(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<InstalmentTypeResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Instalment type data found")
                        .data(instalmentTypeResponse)
                        .build());
    }

    @PutMapping
    public ResponseEntity<?> updateInstalmentType(@RequestBody InstalmentTypeRequest instalmentTypeRequest) {
        InstalmentTypeResponse instalmentTypeResponse = instalmentTypeService.update(instalmentTypeRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<InstalmentTypeResponse>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Instalment type updated successfully")
                        .data(instalmentTypeResponse)
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInstalmentType(@PathVariable String id) {
        instalmentTypeService.delete(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<String>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("Instalment type deleted successfully")
                        .build());
    }
}
