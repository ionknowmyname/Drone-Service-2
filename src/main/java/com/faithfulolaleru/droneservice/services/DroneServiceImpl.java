package com.faithfulolaleru.droneservice.services;


import com.faithfulolaleru.droneservice.dtos.BulkMedicationRequest;
import com.faithfulolaleru.droneservice.dtos.DroneRequest;
import com.faithfulolaleru.droneservice.dtos.DroneResponse;
import com.faithfulolaleru.droneservice.dtos.MedicationResponse;
import com.faithfulolaleru.droneservice.entity.Drone;
import com.faithfulolaleru.droneservice.entity.Medication;
import com.faithfulolaleru.droneservice.enums.DroneState;
import com.faithfulolaleru.droneservice.exception.ErrorResponse;
import com.faithfulolaleru.droneservice.exception.GeneralException;
import com.faithfulolaleru.droneservice.repository.DroneRepository;
import com.faithfulolaleru.droneservice.utils.AppUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
//import org.antlr.v4.runtime.misc.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;

    private final MedicationService medicationService;

    private final ModelMapper modelmapper;

    @Override
    public DroneResponse registerDrone(DroneRequest request) {

        // prepare to create
        Drone droneEntity = buildDroneEntity(request);
        boolean canSave = AppUtils.validateDroneEntityToSave(droneEntity);

        if(!canSave) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_DRONE,
                    "Invalid Request, Unable to Create Drone");
        }

        Drone savedEntity = droneRepository.save(droneEntity);

        return modelmapper.map(savedEntity, DroneResponse.class);
    }

    @Override
    public DroneResponse getDroneBySerial(UUID serial) {

        return droneRepository.findById(serial)
                .map(e -> modelmapper.map(e, DroneResponse.class))
                .orElseThrow(() -> new GeneralException(HttpStatus.NOT_FOUND, ErrorResponse.ERROR_DRONE,
                        "Drone with serial not found"));
    }

    @Override
    public List<DroneResponse> getAllDronesByState(DroneState state) {

        // DroneState.IDLE can give you all available drones for loading

        return droneRepository.findAllByState(state).stream()
                .map(dEntity -> modelmapper.map(dEntity, DroneResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MedicationResponse> getMedicationsOnDrone(UUID serial) {

//        return droneRepository.findById(serial)
//                .map(Drone::getMedications)
//                .get().stream().map(me -> modelmapper.map(me, MedicationResponse.class))
//                .collect(Collectors.toList());

        return findDroneBySerial(serial)
                .getMedications()
                .stream().map(me -> modelmapper.map(me, MedicationResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    // @Transactional
    public DroneResponse loadMedicationToDrone(UUID serial, BulkMedicationRequest requestDto) {

        Drone foundDrone = findDroneBySerial(serial);

        // don't let it pop error if 1 or more of the medications can't be found, so long as it finds at least 1 medication

        List<Medication> medicationsToLoad = requestDto.getMedicationCodes().stream()
                .map(c -> medicationService.getMedicationByCode(c).get())
                .collect(Collectors.toList());

        Drone droneToBeLoaded = mountOnDrone(foundDrone, medicationsToLoad);
        boolean canSave = AppUtils.validateDroneEntityToSave(droneToBeLoaded);
        boolean batteryGood = AppUtils.validateDroneToLoad(droneToBeLoaded);   // validate battery over 25%

        if(!canSave) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_DRONE,
                    "Invalid Request, Unable to load Drone");
        }
        if(!batteryGood) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_DRONE,
                    "Battery too low to load up drone");
        }

        Drone savedEntity = droneRepository.save(droneToBeLoaded);
        DroneResponse response = modelmapper.map(savedEntity, DroneResponse.class);


        // update drone details for all medication entities uploaded to drone
        savedEntity.getMedications().stream()
                .map(medication -> medication.getCode())
                .forEach(code -> medicationService.setDroneForMedication(code, savedEntity));


        return response;
    }

    @Override
    // @Transactional
    public DroneResponse unloadMedicationToDrone(UUID serial, BulkMedicationRequest requestDto) {

        Drone foundDrone = findDroneBySerial(serial);

        List<Medication> medicationsToUnload = requestDto.getMedicationCodes().stream()
                .map(c -> medicationService.getMedicationByCode(c).get())
                .collect(Collectors.toList());

        Drone droneToBeUnloaded = unmountFromDrone(foundDrone, medicationsToUnload);

        boolean canSave = AppUtils.validateDroneEntityToSave(droneToBeUnloaded);

        if(!canSave) {
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_DRONE,
                    "Invalid Request, Unable to load Drone");
        }

        Drone savedEntity = droneRepository.save(droneToBeUnloaded);
        DroneResponse response = modelmapper.map(savedEntity, DroneResponse.class);


        // update drone details for all medication entities offloaded from drone
        savedEntity.getMedications().stream()
                .map(medication -> medication.getCode())
                .forEach(code -> medicationService.unsetDroneForMedication(code));


        return response;
    }

    @Override
    public String getBatteryPercentOfDrone(UUID serial) {
        Integer batteryCapacity = findDroneBySerial(serial).getBatteryCapacity();

        return "Battery percentage of Drone is --> " + batteryCapacity;
    }

    private Drone buildDroneEntity(DroneRequest request) {

        return Drone.builder()
            .serial(UUID.randomUUID())
            .model(request.getModel())
            .weight(request.getWeight())
            .batteryCapacity(request.getBatteryCapacity())
            .state(request.getState())
            // .medications(new ArrayList<>())
            .build();
    }

    private Drone mountOnDrone(Drone dEntity, List<Medication> mEntities) {

        Integer weightSum = mEntities.stream().map(Medication::getWeight)
                .reduce(0, Integer::sum);
        // .reduce(0, (a, b) -> a + b);

        dEntity.setWeight(dEntity.getWeight() + weightSum);
        dEntity.setState(DroneState.LOADING);
        dEntity.setMedications(mEntities);

        return dEntity;
    }

    private Drone unmountFromDrone(Drone dEntity, List<Medication> mEntities) {

        Integer weightSum = mEntities.stream().map(Medication::getWeight)
                .reduce(0, Integer::sum);
        // .reduce(0, (a, b) -> a + b);

        dEntity.setWeight(dEntity.getWeight() - weightSum);
        dEntity.setState(DroneState.DELIVERED);

        // remove only medications in the list, leave the rest there
        List<Medication> currentMedications = dEntity.getMedications();

        // boolean wasRemoved = currentMedications.removeAll(mEntities);  // remove all except mEntities
        List<Medication> medicationsRemaining = currentMedications.stream()
                .filter(m -> !mEntities.contains(m))
                .collect(Collectors.toList());

        dEntity.setMedications(medicationsRemaining);

        return dEntity;
    }

    public Drone findDroneBySerial(UUID serial) {

        return droneRepository.findById(serial)
                .orElseThrow(() -> new GeneralException(
                        HttpStatus.NOT_FOUND,
                        ErrorResponse.ERROR_DRONE,
                        "Drone with serial not found"));

    }




}
