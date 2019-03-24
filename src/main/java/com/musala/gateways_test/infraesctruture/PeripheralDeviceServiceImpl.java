package com.musala.gateways_test.infraesctruture;

import com.musala.gateways_test.domain.*;
import com.musala.gateways_test.web.dto.PeripheralDeviceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * Peripheral device implementation
 */
@Service
public class PeripheralDeviceServiceImpl implements PeripheralDeviceService {

    private final PeripheralDeviceRepository peripheralDeviceRepository;
    private final PeripheralDeviceFactory peripheralDeviceFactory;
    private final GatewayService gatewayService;

    @Autowired
    public PeripheralDeviceServiceImpl(PeripheralDeviceRepository peripheralDeviceRepository, PeripheralDeviceFactory peripheralDeviceFactory, GatewayService gatewayService) {
        this.peripheralDeviceRepository = peripheralDeviceRepository;
        this.peripheralDeviceFactory = peripheralDeviceFactory;
        this.gatewayService = gatewayService;
    }

    @Override
    public PeripheralDevice save(PeripheralDeviceDTO peripheralDeviceDTO) {
        return peripheralDeviceRepository.save(peripheralDeviceFactory.from(peripheralDeviceDTO));
    }

    @Override
    public Optional<PeripheralDevice> findById(String uid) {
        return peripheralDeviceRepository.findById(uid);
    }

    @Override
    public Collection<PeripheralDevice> findAll() {
        return peripheralDeviceRepository.findAll();
    }

    @Override
    public Long count() {
        return peripheralDeviceRepository.count();
    }

    @Override
    public boolean remove(String uid) {
        peripheralDeviceRepository.delete(findById(uid).get());
        peripheralDeviceRepository.flush();
        return !findById(uid).isPresent();
    }

    @Override
    public Collection<PeripheralDevice> findByGateway(String serialNumber) {
        return peripheralDeviceRepository.findAllByGateway_SerialNumber(serialNumber);
    }
}
