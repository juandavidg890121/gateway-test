package com.musala.gateways_test.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Gateway repository
 */
@Repository
public interface GatewayRepository extends JpaRepository<Gateway, String> {
}
