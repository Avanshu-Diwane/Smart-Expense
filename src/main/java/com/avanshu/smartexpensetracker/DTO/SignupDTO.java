package com.avanshu.smartexpensetracker.DTO;

import lombok.Getter;
import lombok.Setter;

public class SignupDTO {
    @Getter
    @Setter
    public static class SignupRequest {
        private String name;
        private String email;
        private String password;
    }
}
