package com.faithfulolaleru.droneservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity(name = "medication_table")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Medication {

    @Id
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "weight")
    private Integer weight;

    @Column(name = "code")
    private String code;

    @Column(name = "photo_link")
    private String photoLink;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    @JoinColumn(name = "drone_serial", referencedColumnName = "serial")
    private Drone drone;
}
