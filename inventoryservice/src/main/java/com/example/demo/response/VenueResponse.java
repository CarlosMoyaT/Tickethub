package com.example.demo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VenueResponse {
    private Long id;
    private String name;
    private String address;
    private String location;
    private String city;
    private Long totalCapacity;
    private Long capacity;
}
