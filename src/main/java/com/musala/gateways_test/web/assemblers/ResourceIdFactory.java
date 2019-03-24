package com.musala.gateways_test.web.assemblers;

import com.musala.gateways_test.domain.Gateway;
import com.musala.gateways_test.domain.PeripheralDevice;

/**
 * Encapsulate the logic to get the resource ID out of the domain entity
 */
public abstract class ResourceIdFactory {

    public static String getId(Gateway gateway) {
        return gateway.getSerialNumber();
    }

    public static String getId(PeripheralDevice peripheralDevice) {
        return peripheralDevice.getUID();
    }
}
