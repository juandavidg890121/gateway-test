package com.musala.gateways_test.domain.values;

import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Pattern;

/**
 * Ipv4 value object to create and validate IPs
 */
@Value
@NoArgsConstructor(force = true)
@Embeddable
public class IPv4 {

    @Column(name = "ipv4")
    private String iPv4;

    /**
     * IPv4 constructor
     *
     * @param iPv4
     */
    public IPv4(String iPv4) {
        Assert.notNull(iPv4, "you must provide a iPv4");
        Assert.isTrue(Pattern.matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", iPv4),
                String.format("Bad format for iPv4 [xxx.xxx.xxx.xxx] having: %s", iPv4));
        this.iPv4 = iPv4;
    }

    /**
     * Validate if String of IPv4 is valid
     *
     * @param iPv4
     * @return
     */
    public static boolean isValid(String iPv4) {
        return Pattern.matches("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$", iPv4);
    }

}
