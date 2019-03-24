package com.musala.gateways_test.domain;

import com.musala.gateways_test.web.dto.GatewayDTO;

import java.util.Collection;
import java.util.Optional;

/**
 * Gateway service interface
 */
public interface GatewayService {

    Gateway save(GatewayDTO gateway);

    Optional<Gateway> findById(String serialNumber);

    Collection<Gateway> findAll();

    Long count();

    boolean validate(GatewayDTO gatewayDTO);
}
