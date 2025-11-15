package br.com.washington.coupon.controller;

import br.com.washington.coupon.exception.CodeLengthException;
import br.com.washington.coupon.exception.CouponAlreadyExistsException;
import br.com.washington.coupon.exception.CouponIdNotFoundException;
import br.com.washington.coupon.exception.DiscountValueException;
import br.com.washington.coupon.mock.CouponCreateRequestMock;
import br.com.washington.coupon.model.Coupon;
import br.com.washington.coupon.service.CouponService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CouponController.class)
public class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private CouponService couponService;

    @Test
    public void shouldCreateCoupon_andReturnStatusCreated() throws Exception {
        var request = CouponCreateRequestMock.buildGoodValues();
        var coupon = Coupon.create(request.code(), request.description(), request.discountValue(), request.expirationDate(), request.published());
        when(couponService.create(any())).thenReturn(coupon);
        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldCreateCoupon_andReturnStatusCreated_whenCodeContainsSpecialCharacters() throws Exception {
        var request = CouponCreateRequestMock.buildGoodValuesCodeSpecialCharacters();
        var coupon = Coupon.create(request.code(), request.description(), request.discountValue(), request.expirationDate(), request.published());
        when(couponService.create(any())).thenReturn(coupon);
        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldNotCreateCoupon_andReturnStatusBadRequest_whenCodeWithSpecialCharactersIsGreaterThanSix() throws Exception {
        var request = CouponCreateRequestMock.buildBadValuesCodeSpecialCharacters();
        when(couponService.create(any())).thenThrow(new CodeLengthException("Coupon code must contain exactly 6 alphanumeric characters"));
        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateCoupon_andReturnStatusBadRequest_whenCodeIsLessThanSix() throws Exception {
        var request = CouponCreateRequestMock.buildBadValuesCodeSizeLessThanSix();
        when(couponService.create(any())).thenThrow(new CodeLengthException("Coupon code must contain exactly 6 alphanumeric characters"));
        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotCreateCoupon_andReturnStatusBadRequest_whenCodeIsGraterThanSix() throws Exception {
        var request = CouponCreateRequestMock.buildBadValuesCodeSizeGreaterThanSix();
        when(couponService.create(any())).thenThrow(new CodeLengthException("Coupon code must contain exactly 6 alphanumeric characters"));
        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void shouldNotCreateCoupon_andReturnBadRequest_whenDiscountValueIsLessThanZeroPointFive() throws Exception {
        var request = CouponCreateRequestMock.buildBadValuesDiscountValue();
        when(couponService.create(any())).thenThrow(new DiscountValueException("Discount value must be greater than or equal to 0.5"));
        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void shouldNotCreateCouponAndReturnStatusConflict_whenCouponAlreadyExists() throws Exception {
        var request = CouponCreateRequestMock.buildGoodValues();
        when(couponService.create(any())).thenThrow(new CouponAlreadyExistsException("Coupon code already exists"));
        mockMvc.perform(post("/coupon")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    public void shouldReturnStatusOkAndCoupon_whenFindCouponById() throws Exception {
        String id = UUID.randomUUID().toString();
        var request = CouponCreateRequestMock.buildGoodValues();
        var coupon = Coupon.create(request.code(), request.description(), request.discountValue(), request.expirationDate(), request.published());
        when(couponService.findById(UUID.fromString(id))).thenReturn(coupon);

        mockMvc.perform(get("/coupon/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value("CODE10"))
                .andExpect(jsonPath("$.discountValue").value(10.0))
                .andExpect(jsonPath("$.description").value("DESCRIPTION"))
                .andExpect(jsonPath("$.expirationDate").value("2025-12-01T10:00:00"))
                .andExpect(jsonPath("$.published").value(true));
    }

    @Test
    public void shouldReturnStatusNotFound_whenCouponDoesNotExist() throws Exception {
        var id = UUID.randomUUID();
        when(couponService.findById(id)).thenThrow(new CouponIdNotFoundException("Coupon not found"));

        mockMvc.perform(get("/coupon/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnStatusNoContent_whenDeleteCoupon() throws Exception {
        var id = UUID.randomUUID();
        doNothing().when(couponService).delete(id);

        mockMvc.perform(delete("/coupon/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnStatusNotFound_whenDeleteCouponDoesNotExist() throws Exception {
        var id = UUID.randomUUID();
        doThrow(new CouponIdNotFoundException("Coupon not found for this ID")).when(couponService).delete(id);

        mockMvc.perform(delete("/coupon/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldThrowException_whenPassMalformattedID() throws Exception {
        var id = "123456";
        mockMvc.perform(get("/coupon/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
