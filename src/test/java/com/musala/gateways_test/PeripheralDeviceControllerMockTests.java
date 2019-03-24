package com.musala.gateways_test;

import com.musala.gateways_test.domain.Gateway;
import com.musala.gateways_test.domain.PeripheralDevice;
import com.musala.gateways_test.domain.PeripheralDeviceService;
import com.musala.gateways_test.domain.values.IPv4;
import com.musala.gateways_test.domain.values.STATUS;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PeripheralDeviceControllerMockTests {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mvc;

    @MockBean
    private PeripheralDeviceService peripheralDeviceService;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .build();
    }

    @Test
    public void shouldFetchAPeripheralList() throws Exception {
        Gateway gateway = Gateway.builder()
                .serialNumber("123")
                .name("Gateway1")
                .iPv4(new IPv4("10.10.10.10"))
                .build();
        given(peripheralDeviceService.findAll()).willReturn(
                Arrays.asList(
                        PeripheralDevice.builder()
                                .UID("123")
                                .vendor("Vendor")
                                .status(STATUS.ONLINE)
                                .created(LocalDate.now())
                                .gateway(gateway)
                                .build(),
                        PeripheralDevice.builder()
                                .UID("124")
                                .vendor("Vendor")
                                .status(STATUS.OFFLINE)
                                .created(LocalDate.now())
                                .gateway(gateway)
                                .build()));

        mvc.perform(get("/peripheral").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$[0].uid", Matchers.containsString("123")))
                .andExpect(jsonPath("$[1].uid", Matchers.containsString("124")));
    }

    @Test
    public void shouldFetchAPeripheralByGateway() throws Exception {
        Gateway gateway = Gateway.builder()
                .serialNumber("123")
                .name("Gateway1")
                .iPv4(new IPv4("10.10.10.10"))
                .build();
        given(peripheralDeviceService.findByGateway("123")).willReturn(
                Arrays.asList(
                        PeripheralDevice.builder()
                                .UID("123")
                                .vendor("Vendor")
                                .status(STATUS.ONLINE)
                                .created(LocalDate.now())
                                .gateway(gateway)
                                .build(),
                        PeripheralDevice.builder()
                                .UID("124")
                                .vendor("Vendor")
                                .status(STATUS.OFFLINE)
                                .created(LocalDate.now())
                                .gateway(gateway)
                                .build()));

        mvc.perform(get("/peripheral/123/gateway/").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$[0].uid", Matchers.containsString("123")))
                .andExpect(jsonPath("$[1].uid", Matchers.containsString("124")));
    }
}
