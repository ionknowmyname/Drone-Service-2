package com.faithfulolaleru.droneservice.controllers;


import com.faithfulolaleru.droneservice.dtos.DroneRequest;
import com.faithfulolaleru.droneservice.dtos.DroneResponse;
import com.faithfulolaleru.droneservice.enums.DroneState;
import com.faithfulolaleru.droneservice.services.DroneService;
import com.faithfulolaleru.droneservice.utils.AppResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/drone")
@AllArgsConstructor
public class DroneController {

    private final DroneService droneService;


    @GetMapping("/id/{serial}")
    public AppResponse<?> getDroneBySerial(@PathVariable("serial") UUID serial) {

        DroneResponse response = droneService.getDroneBySerial(serial);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .data(response)
                .build();
    }

    @GetMapping("/all")
    public AppResponse<?> getAllDronesByState(@RequestParam(value = "state") DroneState state) {

        List<DroneResponse> response = droneService.getAllDronesByState(state);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .data(response)
                .build();
    }

    @PostMapping("/")
    public AppResponse<?> registerDrone(@RequestBody DroneRequest requestDto) {

        DroneResponse response = droneService.registerDrone(requestDto);

        return AppResponse.builder()
                .statusCode(HttpStatus.CREATED.value())
                .httpStatus(HttpStatus.CREATED)
                .data(response)
                .build();
    }

}
