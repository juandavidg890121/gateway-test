package com.musala.gateways_test;

import com.musala.gateways_test.domain.Gateway;
import com.musala.gateways_test.domain.GatewayService;
import com.musala.gateways_test.domain.values.IPv4;
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

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class GatewayControllerMockTests {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mvc;

    @MockBean
    private GatewayService gatewayService;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .build();
    }

    @Test
    public void shouldFetchAGatewayList() throws Exception {
        given(gatewayService.findAll()).willReturn(
                Arrays.asList(
                        Gateway.builder()
                                .serialNumber("123")
                                .name("Gateway1")
                                .iPv4(new IPv4("10.10.10.10"))
                                .build(),
                        Gateway.builder().serialNumber("124")
                                .name("Gateway2")
                                .iPv4(new IPv4("11.11.11.11"))
                                .build()));

        mvc.perform(get("/gateway").contentType(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$[0].serialNumber", Matchers.containsString("123")))
                .andExpect(jsonPath("$[1].serialNumber", Matchers.containsString("124")));
    }
}
