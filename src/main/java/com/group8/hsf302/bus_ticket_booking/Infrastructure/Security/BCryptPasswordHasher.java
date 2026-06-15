package com.group8.hsf302.bus_ticket_booking.Infrastructure.Security;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BCryptPasswordHasher implements PasswordHasher {
    @Override
    public String hash(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(12));
    }

    @Override
    public boolean verify(String rawPassword, String hashedPassword) {
        try{
            return  BCrypt.checkpw(rawPassword, hashedPassword);
        }catch(Exception e){
            return false;
        }
    }
}
