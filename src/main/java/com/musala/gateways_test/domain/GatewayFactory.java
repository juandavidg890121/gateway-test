package com.musala.gateways_test.domain;

import com.musala.gateways_test.domain.values.IPv4;
import com.musala.gateways_test.web.dto.GatewayDTO;
import org.springframework.stereotype.Component;

/**
 * Gateway factory util class
 */
@Component
public class GatewayFactory {

    /**
     * Create a {@link Gateway} from {@link GatewayDTO}
     *
     * @param gatewayDTO
     * @return
     */
    public Gateway from(GatewayDTO gatewayDTO) {
        return Gateway.builder()
                .serialNumber(gatewayDTO.getSerialNumber())
                .name(gatewayDTO.getName())
                .iPv4(new IPv4(gatewayDTO.getIpv4()))
                .build();
    }
}
