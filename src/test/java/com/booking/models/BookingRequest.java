package com.booking.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data @NoArgsConstructor @AllArgsConstructor
public class BookingRequest {
    private int roomid;
    private String firstname;
    private String lastname;
    private boolean depositpaid;
    private BookingDates bookingdates;
    private String email;
    private String phone;
}
