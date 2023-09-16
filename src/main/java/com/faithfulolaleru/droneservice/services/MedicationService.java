package com.faithfulolaleru.droneservice.services;

import com.faithfulolaleru.droneservice.dtos.MedicationRequest;
import com.faithfulolaleru.droneservice.dtos.MedicationResponse;
import com.faithfulolaleru.droneservice.entity.Medication;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;


public interface MedicationService {

    MedicationResponse createMedication(MedicationRequest request);

    Optional<Medication> getMedicationByCode(String code);

    String test(MultipartFile file);

}
