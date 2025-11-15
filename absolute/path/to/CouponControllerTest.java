@Test
public void shouldReturnStatusOkAndCoupon_whenFindCouponById() throws Exception {
    String id = UUID.randomUUID().toString();
    Coupon coupon = new Coupon("CODE123", "Description", 10.0, LocalDate.now(), true);
    when(couponService.findById(id)).thenReturn(coupon);
    
    mockMvc.perform(get("/coupon/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value("CODE123"))
            .andExpect(jsonPath("$.discountValue").value(10.0));
}

@Test
public void shouldReturnStatusNotFound_whenCouponDoesNotExist() throws Exception {
    String id = UUID.randomUUID().toString();
    when(couponService.findById(id)).thenThrow(new EntityNotFoundException("Coupon not found"));
    
    mockMvc.perform(get("/coupon/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
}