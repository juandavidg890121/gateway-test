package com.musala.gateways_test.infraesctruture;

import com.musala.gateways_test.domain.Gateway;
import com.musala.gateways_test.domain.GatewayFactory;
import com.musala.gateways_test.domain.GatewayRepository;
import com.musala.gateways_test.domain.GatewayService;
import com.musala.gateways_test.domain.values.IPv4;
import com.musala.gateways_test.web.dto.GatewayDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;

/**
 * Gateway service implementation
 */
@Service
public class GatewayServiceImpl implements GatewayService {

    private final GatewayRepository gatewayRepository;
    private final GatewayFactory gatewayFactory;

    @Autowired
    public GatewayServiceImpl(GatewayRepository gatewayRepository, GatewayFactory gatewayFactory) {
        this.gatewayRepository = gatewayRepository;
        this.gatewayFactory = gatewayFactory;
    }

    @Override
    public Gateway save(GatewayDTO gatewayDTO) {
        return gatewayRepository.save(gatewayFactory.from(gatewayDTO));
    }

    @Override
    public Optional<Gateway> findById(String serialNumber) {
        return gatewayRepository.findById(serialNumber);
    }

    @Override
    public Collection<Gateway> findAll() {
        return gatewayRepository.findAll();
    }

    @Override
    public Long count() {
        return gatewayRepository.count();
    }

    @Override
    public boolean validate(GatewayDTO gatewayDTO) {
        return IPv4.isValid(gatewayDTO.getIpv4());
    }
}
