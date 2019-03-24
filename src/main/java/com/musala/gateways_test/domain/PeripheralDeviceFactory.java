package com.musala.gateways_test.domain;

import com.musala.gateways_test.web.dto.PeripheralDeviceDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * Peripheral device factory util class
 */
@Component
public class PeripheralDeviceFactory {

    private final GatewayService gatewayService;

    public PeripheralDeviceFactory(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    /**
     * Create a {@link PeripheralDevice} from {@link PeripheralDeviceDTO}
     *
     * @param peripheralDeviceDTO
     * @return
     */
    public PeripheralDevice from(PeripheralDeviceDTO peripheralDeviceDTO) {
        return PeripheralDevice.builder()
                .UID(peripheralDeviceDTO.getUID())
                .vendor(peripheralDeviceDTO.getVendor())
                .created(peripheralDeviceDTO.getCreated())
                .status(peripheralDeviceDTO.getStatus())
                .gateway(!Objects.isNull(peripheralDeviceDTO.getGatewayId())
                        ? gatewayService.findById(peripheralDeviceDTO.getGatewayId())
                        .orElse(null) : null)
                .build();
    }
}
