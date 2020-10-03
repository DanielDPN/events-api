package com.events.api.payload.request.auth;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@RequiredArgsConstructor
public class Login {

    @NotBlank
    @NonNull
    private String username;

    @NotBlank
    @NonNull
    private String password;

}
