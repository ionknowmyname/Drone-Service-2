package com.faithfulolaleru.droneservice.controllers;


import com.faithfulolaleru.droneservice.dtos.MedicationRequest;
import com.faithfulolaleru.droneservice.dtos.MedicationResponse;
import com.faithfulolaleru.droneservice.services.MedicationService;
import com.faithfulolaleru.droneservice.utils.AppResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "api/v1/medication")
@AllArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;


    @PostMapping(value = "/", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE,
            MediaType.APPLICATION_JSON_VALUE })
    public AppResponse<?> createMedication(MedicationRequest request) {

        MedicationResponse response = medicationService.createMedication(request);

        return AppResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .httpStatus(HttpStatus.CREATED)
                .data(response)
                .build();
    }

    @PostMapping("/test")
    public AppResponse<?> createFileTest(@RequestPart(value = "file") MultipartFile file) {

        String response = medicationService.test(file);

        return AppResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .httpStatus(HttpStatus.CREATED)
                .data(response)
                .build();
    }

}
