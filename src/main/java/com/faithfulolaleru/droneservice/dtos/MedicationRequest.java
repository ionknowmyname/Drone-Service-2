package com.faithfulolaleru.droneservice.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicationRequest {

    private String name;
    private Integer weight;
    private MultipartFile file;
    // private String code;
}
