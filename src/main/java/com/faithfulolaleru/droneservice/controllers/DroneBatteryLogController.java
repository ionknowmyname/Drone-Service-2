package com.faithfulolaleru.droneservice.controllers;


import com.faithfulolaleru.droneservice.dtos.DroneBatteryLogResponse;
import com.faithfulolaleru.droneservice.services.DroneBatteryLogService;
import com.faithfulolaleru.droneservice.utils.AppResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/droneBatteryLog")
@AllArgsConstructor
public class DroneBatteryLogController {

    private final DroneBatteryLogService droneBatteryLogService;


    @GetMapping("/id/{serial}")
    public AppResponse<?> getAllBatteryLogsByBatterySerial(@PathVariable("serial") UUID serial) {

        List<DroneBatteryLogResponse> response = droneBatteryLogService.getAllBatteryLogsByDroneSerial(serial);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("SUCCESSFUL")
                .data(response)
                .build();
    }
}
