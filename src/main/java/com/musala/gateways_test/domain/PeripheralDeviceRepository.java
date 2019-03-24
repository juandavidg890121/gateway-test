package com.musala.gateways_test.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Peripheral device repository
 */
@Repository
public interface PeripheralDeviceRepository extends JpaRepository<PeripheralDevice, String> {

    Collection<PeripheralDevice> findAllByGateway_SerialNumber(String serialNumber);
}
