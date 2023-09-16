package com.faithfulolaleru.droneservice.dtos;

import com.faithfulolaleru.droneservice.entity.Drone;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicationResponse {

    private String name;
    private Integer weight;
    private String code;
    private String photoLink;
    private Drone drone;
    private String responseMessage;
}
