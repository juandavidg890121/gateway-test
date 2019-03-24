package com.musala.gateways_test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.musala.gateways_test.domain.*;
import com.musala.gateways_test.domain.values.STATUS;
import com.musala.gateways_test.web.dto.GatewayDTO;
import com.musala.gateways_test.web.dto.PeripheralDeviceDTO;
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

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PeripheralDeviceControllerTests {

    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private GatewayService gatewayService;
    @Autowired
    private PeripheralDeviceService peripheralDeviceService;
    @Autowired
    private PeripheralDeviceRepository peripheralDeviceRepository;
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
        peripheralDeviceRepository.deleteAll();
    }

    @Test
    public void shouldSaveAPeripheral() throws Exception {
        //GIVEN
        String serial = UUID.randomUUID().toString();
        GatewayDTO gatewayDTO = GatewayDTO.builder()
                .serialNumber(serial)
                .name("Gateway")
                .ipv4("10.10.10.10")
                .build();
        Gateway gateway = gatewayService.save(gatewayDTO);

        String uid = UUID.randomUUID().toString();
        String peripheralDTO = objectMapper.writeValueAsString(PeripheralDeviceDTO.builder()
                .UID(uid)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build());

        // WHEN - EXPECT
        this.mvc.perform(
                post("/peripheral")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(peripheralDTO))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", Matchers.containsString(uid.toString())));
        assertThat(peripheralDeviceService.findById(uid).isPresent()).isTrue();
    }

    @Test
    public void shouldGetAndErrorWhenSaveAPeripheralAndGatewayNotExist() throws Exception {
        //GIVEN
        String uid = UUID.randomUUID().toString();
        String peripheralDTO = objectMapper.writeValueAsString(PeripheralDeviceDTO.builder()
                .UID(uid)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId("asd")
                .build());


        // WHEN - EXPECT
        this.mvc.perform(
                post("/peripheral")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(peripheralDTO))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().exists(HeaderUtil.HEADERS.ERROR.getHeader()))
                .andExpect(header().string(HeaderUtil.HEADERS.ERROR.getHeader(), "Gateway was not found"));
    }

    @Test
    public void shouldGetAndErrorWhenSaveAPeripheralAndAlreadyExist() throws Exception {
        //GIVEN
        String serial = UUID.randomUUID().toString();
        GatewayDTO gatewayDTO = GatewayDTO.builder()
                .serialNumber(serial)
                .name("Gateway")
                .ipv4("10.10.10.10")
                .build();
        Gateway gateway = gatewayService.save(gatewayDTO);

        String uid = UUID.randomUUID().toString();
        PeripheralDeviceDTO peripheralDeviceDTO = PeripheralDeviceDTO.builder()
                .UID(uid)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build();
        peripheralDeviceService.save(peripheralDeviceDTO);
        String peripheralDTO = objectMapper.writeValueAsString(peripheralDeviceDTO);

        // WHEN - EXPECT
        this.mvc.perform(
                post("/peripheral")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(peripheralDTO))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(header().exists(HeaderUtil.HEADERS.ERROR.getHeader()))
                .andExpect(header().string(HeaderUtil.HEADERS.ERROR.getHeader(), "Peripheral already exists"));
    }

    @Test
    public void shouldGetAndErrorWhenSaveAPeripheralWithGatewayMaximumAllowed() throws Exception {
        //GIVEN
        String serial = UUID.randomUUID().toString();
        GatewayDTO gatewayDTO = GatewayDTO.builder()
                .serialNumber(serial.toString())
                .name("Gateway")
                .ipv4("10.10.10.10")
                .build();
        Gateway gateway = gatewayService.save(gatewayDTO);

        String uid1 = UUID.randomUUID().toString();
        PeripheralDeviceDTO peripheralDeviceDTO1 = PeripheralDeviceDTO.builder()
                .UID(uid1)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build();
        peripheralDeviceService.save(peripheralDeviceDTO1);

        String uid2 = UUID.randomUUID().toString();
        PeripheralDeviceDTO peripheralDeviceDTO2 = PeripheralDeviceDTO.builder()
                .UID(uid2)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build();
        peripheralDeviceService.save(peripheralDeviceDTO2);

        String uid3 = UUID.randomUUID().toString();
        PeripheralDeviceDTO peripheralDeviceDTO3 = PeripheralDeviceDTO.builder()
                .UID(uid3)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build();
        peripheralDeviceService.save(peripheralDeviceDTO3);

        String uid4 = UUID.randomUUID().toString();
        PeripheralDeviceDTO peripheralDeviceDTO4 = PeripheralDeviceDTO.builder()
                .UID(uid4)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build();
        peripheralDeviceService.save(peripheralDeviceDTO4);

        String uid5 = UUID.randomUUID().toString();
        PeripheralDeviceDTO peripheralDeviceDTO5 = PeripheralDeviceDTO.builder()
                .UID(uid5)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build();
        peripheralDeviceService.save(peripheralDeviceDTO5);

        String uid6 = UUID.randomUUID().toString();
        PeripheralDeviceDTO peripheralDeviceDTO6 = PeripheralDeviceDTO.builder()
                .UID(uid6)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build();
        peripheralDeviceService.save(peripheralDeviceDTO6);

        String uid7 = UUID.randomUUID().toString();
        PeripheralDeviceDTO peripheralDeviceDTO7 = PeripheralDeviceDTO.builder()
                .UID(uid7)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build();
        peripheralDeviceService.save(peripheralDeviceDTO7);

        String uid8 = UUID.randomUUID().toString();
        PeripheralDeviceDTO peripheralDeviceDTO8 = PeripheralDeviceDTO.builder()
                .UID(uid8)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build();
        peripheralDeviceService.save(peripheralDeviceDTO8);

        String uid9 = UUID.randomUUID().toString();
        PeripheralDeviceDTO peripheralDeviceDTO9 = PeripheralDeviceDTO.builder()
                .UID(uid9)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build();
        peripheralDeviceService.save(peripheralDeviceDTO9);

        String uid10 = UUID.randomUUID().toString();
        PeripheralDeviceDTO peripheralDeviceDTO10 = PeripheralDeviceDTO.builder()
                .UID(uid10)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build();
        peripheralDeviceService.save(peripheralDeviceDTO10);

        String uid11 = UUID.randomUUID().toString();
        String peripheralDTOJSON = objectMapper.writeValueAsString(PeripheralDeviceDTO.builder()
                .UID(uid11)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build());

        // WHEN - EXPECT
        this.mvc.perform(
                post("/peripheral")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(peripheralDTOJSON))
                .andDo(print())
                .andExpect(status().isNotAcceptable())
                .andExpect(header().exists(HeaderUtil.HEADERS.ERROR.getHeader()))
                .andExpect(header().string(HeaderUtil.HEADERS.ERROR.getHeader(), "Maximum number of peripherals allowed per gateway is 10"));
    }

    @Test
    public void shouldUpdateAPeripheralDevice() throws Exception {
        //GIVEN
        String serial = UUID.randomUUID().toString();
        GatewayDTO gatewayDTO = GatewayDTO.builder()
                .serialNumber(serial)
                .name("Gateway")
                .ipv4("10.10.10.10")
                .build();
        Gateway gateway = gatewayService.save(gatewayDTO);

        String uid = UUID.randomUUID().toString();
        PeripheralDeviceDTO peripheralDeviceDTO = PeripheralDeviceDTO.builder()
                .UID(uid)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build();
        peripheralDeviceService.save(peripheralDeviceDTO);
        peripheralDeviceDTO.setStatus(STATUS.OFFLINE);
        String peripheralDTO = objectMapper.writeValueAsString(peripheralDeviceDTO);

        // WHEN - EXPECT
        this.mvc.perform(
                put("/peripheral/" + uid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(peripheralDTO))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", Matchers.containsString(uid)));
        Optional<PeripheralDevice> byId = peripheralDeviceService.findById(uid);
        assertThat(byId.isPresent()).isTrue();
        assertThat(byId.get().getStatus()).isEqualTo(STATUS.OFFLINE);
    }

    @Test
    public void shouldGetAndErrorUpdatingWhenPeripheralNotFound() throws Exception {
        //GIVEN
        String serial = UUID.randomUUID().toString();
        GatewayDTO gatewayDTO = GatewayDTO.builder()
                .serialNumber(serial)
                .name("Gateway")
                .ipv4("10.10.10.10")
                .build();
        Gateway gateway = gatewayService.save(gatewayDTO);

        String uid = UUID.randomUUID().toString();
        String peripheralDTO = objectMapper.writeValueAsString(PeripheralDeviceDTO.builder()
                .UID(uid)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build());

        // WHEN - EXPECT
        this.mvc.perform(
                put("/peripheral/asd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(peripheralDTO))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().exists(HeaderUtil.HEADERS.ERROR.getHeader()))
                .andExpect(header().string(HeaderUtil.HEADERS.ERROR.getHeader(), "Peripheral was not found"));
    }

    @Test
    public void shouldGetAndErrorUpdatingWhenGatewayNotFound() throws Exception {
        //GIVEN
        String serial = UUID.randomUUID().toString();
        GatewayDTO gatewayDTO = GatewayDTO.builder()
                .serialNumber(serial)
                .name("Gateway")
                .ipv4("10.10.10.10")
                .build();
        Gateway gateway = gatewayService.save(gatewayDTO);

        String uid = UUID.randomUUID().toString();
        PeripheralDeviceDTO peripheralDeviceDTO = PeripheralDeviceDTO.builder()
                .UID(uid)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build();
        peripheralDeviceService.save(peripheralDeviceDTO);
        peripheralDeviceDTO.setGatewayId("asd");
        String peripheralDTO = objectMapper.writeValueAsString(peripheralDeviceDTO);

        // WHEN - EXPECT
        this.mvc.perform(
                put("/peripheral/" + uid)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(peripheralDTO))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().exists(HeaderUtil.HEADERS.ERROR.getHeader()))
                .andExpect(header().string(HeaderUtil.HEADERS.ERROR.getHeader(), "Gateway was not found"));
    }

    @Test
    public void shouldGetAPeripheral() throws Exception {
        //GIVEN
        String serial = UUID.randomUUID().toString();
        GatewayDTO gatewayDTO = GatewayDTO.builder()
                .serialNumber(serial)
                .name("Gateway")
                .ipv4("10.10.10.10")
                .build();
        Gateway gateway = gatewayService.save(gatewayDTO);

        String uid = UUID.randomUUID().toString();
        PeripheralDeviceDTO peripheralDeviceDTO = PeripheralDeviceDTO.builder()
                .UID(uid)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build();
        peripheralDeviceService.save(peripheralDeviceDTO);

        // WHEN - EXPECT
        this.mvc.perform(
                get("/peripheral/" + uid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(content().string(Matchers.containsString(uid)));
    }

    @Test
    public void shouldGetAndErrorGetPeripheralWhenPeripheralNotFound() throws Exception {
        // WHEN - EXPECT
        this.mvc.perform(
                get("/peripheral/asd")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().exists(HeaderUtil.HEADERS.ERROR.getHeader()))
                .andExpect(header().string(HeaderUtil.HEADERS.ERROR.getHeader(), "Peripheral was not found"));
    }

    @Test
    public void shouldRemoveAPeripheral() throws Exception {
        //GIVEN
        String serial = UUID.randomUUID().toString();
        GatewayDTO gatewayDTO = GatewayDTO.builder()
                .serialNumber(serial)
                .name("Gateway")
                .ipv4("10.10.10.10")
                .build();
        Gateway gateway = gatewayService.save(gatewayDTO);

        String uid = UUID.randomUUID().toString();
        PeripheralDeviceDTO peripheralDeviceDTO = PeripheralDeviceDTO.builder()
                .UID(uid)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build();
        peripheralDeviceService.save(peripheralDeviceDTO);

        // WHEN - EXPECT
        this.mvc.perform(
                delete("/peripheral/" + uid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(content().string(Matchers.is("true")));
        assertThat(peripheralDeviceService.findById(uid).isPresent()).isFalse();
        Optional<Gateway> byId = gatewayService.findById(serial);
        assertThat(byId.isPresent()).isTrue();
        assertThat(byId.get().getPeripheralDevices()).isEmpty();
    }

    @Test
    public void shouldGetAndErrorDeletePeripheralWhenPeripheralNotFound() throws Exception {
        // WHEN - EXPECT
        this.mvc.perform(
                delete("/peripheral/asd")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().exists(HeaderUtil.HEADERS.ERROR.getHeader()))
                .andExpect(header().string(HeaderUtil.HEADERS.ERROR.getHeader(), "Peripheral was not found"));
    }

    @Test
    public void shouldFindAllPeripheralDevices() throws Exception {
        //GIVEN
        String serial = UUID.randomUUID().toString();
        GatewayDTO gatewayDTO = GatewayDTO.builder()
                .serialNumber(serial)
                .name("Gateway")
                .ipv4("10.10.10.10")
                .build();
        Gateway gateway = gatewayService.save(gatewayDTO);

        String uid1 = UUID.randomUUID().toString();
        PeripheralDeviceDTO peripheralDeviceDTO1 = PeripheralDeviceDTO.builder()
                .UID(uid1)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build();
        peripheralDeviceService.save(peripheralDeviceDTO1);

        String uid2 = UUID.randomUUID().toString();
        PeripheralDeviceDTO peripheralDeviceDTO2 = PeripheralDeviceDTO.builder()
                .UID(uid2)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build();
        peripheralDeviceService.save(peripheralDeviceDTO2);

        // WHEN - EXPECT
        this.mvc.perform(
                get("/peripheral")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8"))
                .andExpect(jsonPath("$[0].uid", Matchers.containsString(uid1)))
                .andExpect(jsonPath("$[1].uid", Matchers.containsString(uid2)));
        assertThat(peripheralDeviceService.findAll().size()).isEqualTo(2);
    }

    @Test
    public void shouldCountAllPeripheralDevices() {
        //GIVEN
        String serial = UUID.randomUUID().toString();
        GatewayDTO gatewayDTO = GatewayDTO.builder()
                .serialNumber(serial)
                .name("Gateway")
                .ipv4("10.10.10.10")
                .build();
        Gateway gateway = gatewayService.save(gatewayDTO);

        String uid1 = UUID.randomUUID().toString();
        PeripheralDeviceDTO peripheralDeviceDTO1 = PeripheralDeviceDTO.builder()
                .UID(uid1)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build();
        peripheralDeviceService.save(peripheralDeviceDTO1);

        String uid2 = UUID.randomUUID().toString();
        PeripheralDeviceDTO peripheralDeviceDTO2 = PeripheralDeviceDTO.builder()
                .UID(uid2)
                .vendor("Vendor")
                .created(LocalDate.now())
                .status(STATUS.ONLINE)
                .gatewayId(gateway.getSerialNumber())
                .build();
        peripheralDeviceService.save(peripheralDeviceDTO2);

        assertThat(peripheralDeviceService.count()).isEqualTo(2);
    }

}
