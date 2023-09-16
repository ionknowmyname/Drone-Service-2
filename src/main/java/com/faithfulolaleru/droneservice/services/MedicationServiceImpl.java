package com.faithfulolaleru.droneservice.services;


import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.faithfulolaleru.droneservice.dtos.MedicationRequest;
import com.faithfulolaleru.droneservice.dtos.MedicationResponse;
import com.faithfulolaleru.droneservice.entity.Medication;
import com.faithfulolaleru.droneservice.exception.ErrorResponse;
import com.faithfulolaleru.droneservice.exception.GeneralException;
import com.faithfulolaleru.droneservice.repository.MedicationRepository;
import com.faithfulolaleru.droneservice.utils.AppUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
@AllArgsConstructor
@Slf4j
public class MedicationServiceImpl implements MedicationService {



    private final MedicationRepository medicationRepository;

    private final ModelMapper modelMapper;

    private final Cloudinary cloudinary;


    @Override
    public MedicationResponse createMedication(MedicationRequest request) {

        String photoLink = "";
        String responseMessage = "No file was uploaded";


        // check if medication exists before attempting to create new medication
        Optional<Medication> foundMedication = medicationRepository.findByName(request.getName());
        if(foundMedication.isPresent()) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_MEDICATION,
                    "Medication already exists, cannot create anew");
        }

        // for file

        Map upload = new HashMap<>();
        try {
            MultipartFile file = request.getFile();
            if(!file.isEmpty()) {

                Map params = ObjectUtils.asMap(
                    "use_filename", true,
                    "unique_filename", false,
                    "overwrite", true
                );

                // upload = cloudinary.uploader().upload(file, params);
                upload = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());

                log.info("result from cloudinary upload --> ", upload.entrySet());

                responseMessage = "file successfully uploaded";
            }
        } catch (IOException ex) {
            log.error("Error while mapping to class --> {}", ex.getMessage());
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_MEDICATION,
                    "Error while uploading your file");
        }

        // prepare to create
        Medication medicationEntity = buildMedicationEntity(request, upload.get("url").toString());
        boolean canSave = AppUtils.validateMedicationEntityToSave(medicationEntity);

        if(!canSave) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_MEDICATION,
                    "Invalid Request, Unable to Create Medication");
        }

        Medication savedEntity = medicationRepository.save(medicationEntity);
        MedicationResponse response = modelMapper.map(savedEntity, MedicationResponse.class);
        response.setResponseMessage(responseMessage);


        return response;
    }

    @Override
    public Optional<Medication> getMedicationByCode(String code) {
        return medicationRepository.findByCode(code);
    }

    @Override
    public String test(MultipartFile file) {
        return null;
    }

    public Medication buildMedicationEntity(MedicationRequest request, String photoLink) {
        return Medication.builder()
                .id(UUID.randomUUID())
                .name(request.getName())
                .code(AppUtils.generateRandomString(10).toUpperCase(Locale.ROOT))
                .weight(request.getWeight())
                .photoLink(photoLink)
                .build();
    }


}
