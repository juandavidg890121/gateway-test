package com.musala.gateways_test.web.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Gateway data transfer object to receive data from REST service
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GatewayDTO {

    private String serialNumber;
    private String name;
    private String ipv4;
    @Builder.Default
    private List<PeripheralDeviceDTO> peripheralDevices = new ArrayList<>();


}
