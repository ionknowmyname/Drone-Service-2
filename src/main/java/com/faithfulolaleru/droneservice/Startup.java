package com.faithfulolaleru.droneservice;

import com.faithfulolaleru.droneservice.entity.Drone;
import com.faithfulolaleru.droneservice.entity.Medication;
import com.faithfulolaleru.droneservice.enums.DroneModel;
import com.faithfulolaleru.droneservice.enums.DroneState;
import com.faithfulolaleru.droneservice.repository.DroneRepository;
import com.faithfulolaleru.droneservice.repository.MedicationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Slf4j
@Component
@AllArgsConstructor
public class Startup implements ApplicationListener<ApplicationReadyEvent> {


    private final DroneRepository droneRepository;

    private final MedicationRepository medicationRepository;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        log.info("Loading Drones DB");
        List<Drone> droneList = List.of(createNewDrone(), createNewDrone());
        droneRepository.saveAll(droneList);

        log.info("Loading Medications DB");
        List<Medication> medicationList = List.of(
                createNewMedication("Test1", "HYFVSAHWXT"),
                createNewMedication("Test2", "XY7VSAHWXT"),
                createNewMedication("Test3", "TYFVSAHGHD"));

        medicationRepository.saveAll(medicationList);

    }

    public Drone createNewDrone() {

        return Drone.builder()
                .serial(UUID.randomUUID())
                .model(DroneModel.LIGHTWEIGHT)
                .state(DroneState.IDLE)
                .weight(30)
                .batteryCapacity(95)
                .medications(new ArrayList<>())
                .build();
    }

    public Medication createNewMedication(String name, String code) {

        return Medication.builder()
                .id(UUID.randomUUID())
                .name(name)
                .code(code)
                .weight(20)
                .photoLink("http://res.cloudinary.com/ddhreljwl/image/upload/v1694887421/muq5feuzxpwuw4zpt8wh.jpg")
                .build();

    }
}
