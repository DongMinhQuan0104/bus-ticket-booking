package com.group8.hsf302.bus_ticket_booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BusTicketBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(
				BusTicketBookingApplication.class,
				args
		);
	}
}