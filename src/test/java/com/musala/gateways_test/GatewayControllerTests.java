package com.musala.gateways_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.musala.gateways_test.domain.Gateway;
import com.musala.gateways_test.domain.GatewayRepository;
import com.musala.gateways_test.domain.GatewayService;
import com.musala.gateways_test.web.dto.GatewayDTO;
import com.musala.gateways_test.web.utils.HeaderUtil;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class GatewayControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private GatewayService gatewayService;
    @Autowired
    private GatewayRepository gatewayRepository;
    private MockMvc mvc;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .build();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jackson2HalModule());
        objectMapper.registerModule(new JavaTimeModule());
        gatewayRepository.deleteAll();
    }

    @Test
    public void shouldSaveAGateway() throws Exception {
        //GIVEN
        String serial = UUID.randomUUID().toString();
        String gatewayDTO = objectMapper.writeValueAsString(GatewayDTO.builder()
                .serialNumber(serial)
                .name("Gateway")
                .ipv4("10.10.10.10")
                .build());

        // WHEN - EXPECT
        this.mvc.perform(
                post("/gateway")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gatewayDTO))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", Matchers.containsString(serial)));
        assertThat(gatewayService.findById(serial).isPresent()).isTrue();
    }

    @Test
    public void shouldGetAndErrorWithBadIpv4WhenSaveAGateway() throws Exception {
        //GIVEN
        String gatewayDTO = objectMapper.writeValueAsString(GatewayDTO.builder()
                .serialNumber(String.valueOf(UUID.randomUUID().toString()))
                .name("Gateway")
                .ipv4("10.10.10")
                .build());

        // WHEN - EXPECT
        this.mvc.perform(
                post("/gateway")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gatewayDTO))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(header().exists(HeaderUtil.HEADERS.ERROR.getHeader()))
                .andExpect(header().string(HeaderUtil.HEADERS.ERROR.getHeader(), "Bad format for IPv4"));
    }

    @Test
    public void shouldGetAndErrorWhenSaveAGatewayAlreadyExist() throws Exception {
        //GIVEN
        String serial = UUID.randomUUID().toString();
        GatewayDTO gatewayDTO = GatewayDTO.builder()
                .serialNumber(serial)
                .name("Gateway")
                .ipv4("10.10.10.10")
                .build();
        gatewayService.save(gatewayDTO);
        String gatewayDTOJSON = objectMapper.writeValueAsString(gatewayDTO);

        // WHEN - EXPECT
        this.mvc.perform(
                post("/gateway")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gatewayDTOJSON))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(header().exists(HeaderUtil.HEADERS.ERROR.getHeader()))
                .andExpect(header().string(HeaderUtil.HEADERS.ERROR.getHeader(), "Gateway already exists"));
    }

    @Test
    public void shouldUpdateAGateway() throws Exception {
        //GIVEN
        String serial = UUID.randomUUID().toString();
        GatewayDTO gatewayDTO = GatewayDTO.builder()
                .serialNumber(serial)
                .name("Gateway")
                .ipv4("10.10.10.10")
                .build();
        gatewayService.save(gatewayDTO);

        gatewayDTO.setName("Update");
        String gatewayDTOJSON = objectMapper.writeValueAsString(gatewayDTO);

        // WHEN - EXPECT
        this.mvc.perform(
                put("/gateway/" + serial)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gatewayDTOJSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", Matchers.containsString(serial.toString())));
        Optional<Gateway> byId = gatewayService.findById(serial);
        assertThat(byId.isPresent()).isTrue();
        assertThat(byId.get().getName()).isEqualTo("Update");
    }

    @Test
    public void shouldGetAndErrorUpdatingWhenGatewayNotFound() throws Exception {
        //GIVEN
        String gatewayDTO = objectMapper.writeValueAsString(GatewayDTO.builder()
                .serialNumber(UUID.randomUUID().toString())
                .name("Gateway")
                .ipv4("10.10.10")
                .build());

        // WHEN - EXPECT
        this.mvc.perform(
                put("/gateway/asd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gatewayDTO))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().exists(HeaderUtil.HEADERS.ERROR.getHeader()))
                .andExpect(header().string(HeaderUtil.HEADERS.ERROR.getHeader(), "Gateway was not found"));
    }

    @Test
    public void shouldGetAndErrorUpdatingWithBadFormatIpv4() throws Exception {
        //GIVEN
        String serial = UUID.randomUUID().toString();
        GatewayDTO gatewayDTO = GatewayDTO.builder()
                .serialNumber(serial)
                .name("Gateway")
                .ipv4("10.10.10.10")
                .build();
        gatewayService.save(gatewayDTO);
        gatewayDTO.setIpv4("10.10.10");
        String gatewayDTOJSON = objectMapper.writeValueAsString(gatewayDTO);

        // WHEN - EXPECT
        this.mvc.perform(
                put("/gateway/" + serial)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gatewayDTOJSON))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(header().exists(HeaderUtil.HEADERS.ERROR.getHeader()))
                .andExpect(header().string(HeaderUtil.HEADERS.ERROR.getHeader(), "Bad format for IPv4"));
    }

    @Test
    public void shouldGetAGateway() throws Exception {
        //GIVEN
        String serial = UUID.randomUUID().toString();
        gatewayService.save(GatewayDTO.builder()
                .serialNumber(serial.toString())
                .name("Gateway")
                .ipv4("10.10.10.10")
                .build());

        // WHEN - EXPECT
        this.mvc.perform(
                get("/gateway/" + serial)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(content().string(Matchers.containsString(serial.toString())));
    }

    @Test
    public void shouldGetAndErrorWhenGetGatewayNotFound() throws Exception {
        // WHEN - EXPECT
        this.mvc.perform(
                get("/gateway/asd")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().exists(HeaderUtil.HEADERS.ERROR.getHeader()))
                .andExpect(header().string(HeaderUtil.HEADERS.ERROR.getHeader(), "Gateway was not found"));
    }

    @Test
    public void shouldFindAllGateways() throws Exception {
        //GIVEN
        String serial1 = UUID.randomUUID().toString();
        GatewayDTO gatewayDTO1 = GatewayDTO.builder()
                .serialNumber(serial1)
                .name("Gateway")
                .ipv4("10.10.10.10")
                .build();
        gatewayService.save(gatewayDTO1);

        String serial2 = UUID.randomUUID().toString();
        GatewayDTO gatewayDTO2 = GatewayDTO.builder()
                .serialNumber(serial2)
                .name("Gateway")
                .ipv4("10.10.10.10")
                .build();
        gatewayService.save(gatewayDTO2);


        // WHEN - EXPECT
        this.mvc.perform(
                get("/gateway")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$[0].serialNumber", Matchers.containsString(serial1)))
                .andExpect(jsonPath("$[1].serialNumber", Matchers.containsString(serial2)));
        assertThat(gatewayService.findAll().size()).isEqualTo(2);
    }

    @Test
    public void shouldCountAllGateways() {
        //GIVEN
        String serial1 = UUID.randomUUID().toString();
        GatewayDTO gatewayDTO1 = GatewayDTO.builder()
                .serialNumber(serial1)
                .name("Gateway")
                .ipv4("10.10.10.10")
                .build();
        gatewayService.save(gatewayDTO1);

        String serial2 = UUID.randomUUID().toString();
        GatewayDTO gatewayDTO2 = GatewayDTO.builder()
                .serialNumber(serial2)
                .name("Gateway")
                .ipv4("10.10.10.10")
                .build();
        gatewayService.save(gatewayDTO2);

        assertThat(gatewayService.count()).isEqualTo(2);
    }

}
