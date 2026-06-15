package com.group8.hsf302.bus_ticket_booking.Infrastructure.Security;

public interface PasswordHasher {
    String hash(String rawPassword);
    boolean verify(String rawPassword, String hashedPassword);
}
