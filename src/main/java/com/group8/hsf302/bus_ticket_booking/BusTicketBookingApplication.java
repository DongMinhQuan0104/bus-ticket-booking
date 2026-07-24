package com.group8.hsf302.bus_ticket_booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

// @EnableScheduling: bat tac vu dinh ky - dung cho SeatHoldCleanupScheduler
// (tu dong giai phong ghe giu tam qua han).
@SpringBootApplication
@EnableScheduling
public class BusTicketBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BusTicketBookingApplication.class, args);
	}

}
