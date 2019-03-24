package com.musala.gateways_test.domain;

import com.musala.gateways_test.web.dto.PeripheralDeviceDTO;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * Peripheral device service interface
 */
@Service
public interface PeripheralDeviceService {

    PeripheralDevice save(PeripheralDeviceDTO peripheralDeviceDTO);

    Optional<PeripheralDevice> findById(String uid);

    Collection<PeripheralDevice> findAll();

    Long count();

    boolean remove(String uid);

    Collection<PeripheralDevice> findByGateway(String serialNumber);


}
