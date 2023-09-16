package com.faithfulolaleru.droneservice.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.faithfulolaleru.droneservice.dtos.MedicationRequest;
import com.faithfulolaleru.droneservice.dtos.MedicationResponse;
import com.faithfulolaleru.droneservice.entity.Drone;
import com.faithfulolaleru.droneservice.entity.Medication;
import com.faithfulolaleru.droneservice.enums.DroneModel;
import com.faithfulolaleru.droneservice.enums.DroneState;
import com.faithfulolaleru.droneservice.exception.GeneralException;
import com.faithfulolaleru.droneservice.repository.MedicationRepository;
import com.faithfulolaleru.droneservice.utils.AppUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {MedicationServiceImpl.class, ModelMapper.class})
class MedicationServiceImplTest {

    @Autowired
    private MedicationService medicationService;

    @MockBean
    private MedicationRepository medicationRepository;

    @MockBean
    private Cloudinary cloudinary;

    private ModelMapper modelMapper = new ModelMapper();



    @Test
    void createMedicationSuccessfully() throws IOException {

        MedicationRequest request  = getMedicationRequest();

        when(medicationRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Skip validations, do seperately

        Map cloudinaryMapResponse  = new HashMap<>();
        cloudinaryMapResponse.put("url", "testing");

        when(cloudinary.uploader().upload(any(), ObjectUtils.emptyMap())).thenReturn(cloudinaryMapResponse);

        when(medicationRepository.save(any())).thenReturn(getMedicationEntity());

        MedicationResponse testResponse = modelMapper.map(getMedicationEntity(), MedicationResponse.class);

        MedicationResponse serviceResponse = medicationService.createMedication(request);

        assertThat(serviceResponse).isNotNull();
        assertThat(serviceResponse.getName()).isEqualTo(testResponse.getName());
        assertThat(serviceResponse.getPhotoLink()).isNotNull();  // just to make sure picture uploaded
    }

    @Test
    void makeSureValidationsWork() {

        try (MockedStatic<AppUtils> utilities = Mockito.mockStatic(AppUtils.class)) {

            utilities.when(() -> AppUtils.generateRandomString(10))
                    .thenReturn("0123456789");

            utilities.when(() -> AppUtils.validateMedicationEntityToSave(any(Medication.class)))
                    .thenReturn(false);

            Exception exception = assertThrows(GeneralException.class,
                    () -> medicationService.createMedication(getMedicationRequest()));

            assertEquals("Invalid Request, Unable to Create Medication", exception.getMessage());
        }
    }

    @Test
    void createMedicationFailed_MedicationAlreadyExists() {

        when(medicationRepository.findByName(anyString())).thenReturn(Optional.of(getMedicationEntity()));

        Exception exception = assertThrows(GeneralException.class,
                () -> medicationService.createMedication(getMedicationRequest()));

        assertEquals("Medication already exists, cannot create anew", exception.getMessage());
    }

    @Test
    void setDroneForMedicationSuccessfully() {

        when(medicationRepository.findByCode(anyString())).thenReturn(Optional.of(getMedicationEntity()));

        when(medicationRepository.save(any())).thenReturn(getMedicationEntityWithDrone());

        MedicationResponse testResponse = modelMapper.map(getMedicationEntityWithDrone(), MedicationResponse.class);

        MedicationResponse serviceResponse = medicationService.setDroneForMedication("XYZ1", getDroneEntity());

        assertThat(serviceResponse).isNotNull();
        assertThat(serviceResponse.getDrone()).isNotNull();
        assertThat(testResponse.getDrone()).isNotNull();
        assertThat(serviceResponse.getCode()).isEqualTo(testResponse.getCode());
    }

    @Test
    void setDroneForMedicationFailed_MedicationAlreadyOnDifferentDrone() {

        when(medicationRepository.findByCode(anyString())).thenReturn(Optional.of(getMedicationEntityWithDrone()));

        MedicationResponse serviceResponse = medicationService.setDroneForMedication("XYZ1", getDroneEntity());

        assertThat(serviceResponse).isNull();
    }

    private Medication getMedicationEntity() {
        return Medication.builder()
                .id(UUID.fromString("ae013960-701b-4442-a125-23c157f4df45"))
                .name("DRUG1")
                .weight(20)
                .code("XYZ1")
                .photoLink("/")
                .build();
    }
    private Medication getMedicationEntityWithDrone() {

        return Medication.builder()
                .id(UUID.fromString("ae013960-701b-4442-a125-23c157f4df45"))
                .name("DRUG1")
                .weight(20)
                .code("XYZ1")
                .photoLink("/test")
                .drone(getDroneEntity())
                .build();
    }

    private MultipartFile getMultiPartFile() {
        MultipartFile file;

        Path path = Paths.get("/Users/faithfulolaleru/Pictures/myphoto.jpg");
        String name = "myphoto.jpg";
        String originalFileName = "myphoto.jpg";
        String contentType = "image/jpeg";
        byte[] content = null;
        try {
            content = Files.readAllBytes(path);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }

        file = new MockMultipartFile(name, originalFileName, contentType, content);

        return file;
    }

    private Drone getDroneEntity() {

        return Drone.builder()
                .serial(UUID.fromString("ae013960-701b-4442-a125-23c157f4df45"))
                .model(DroneModel.LIGHTWEIGHT)
                .weight(100)
                .batteryCapacity(80)
                .state(DroneState.IDLE)
                .build();
    }

    private MedicationRequest getMedicationRequest() {

        MedicationRequest request = new MedicationRequest();
        request.setName("TestName");
        request.setWeight(30);
        request.setFile(getMultiPartFile());

        return request;
    }

}