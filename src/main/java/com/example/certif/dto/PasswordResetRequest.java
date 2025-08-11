package com.example.certif.dto;

import lombok.Getter;

@Getter
public class PasswordResetRequest {
    private String token;
    private String newPassword;
}
