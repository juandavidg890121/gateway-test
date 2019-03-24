package com.musala.gateways_test.web.dto;

import com.musala.gateways_test.domain.values.STATUS;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Peripheral device data transfer object to receive data from REST service
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PeripheralDeviceDTO {

    private String UID;
    private String vendor;
    private LocalDate created;
    private STATUS status;
    private String gatewayId;
}
