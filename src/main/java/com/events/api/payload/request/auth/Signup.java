package com.events.api.payload.request.auth;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@RequiredArgsConstructor
public class Signup {

    @NonNull
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NonNull
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

    private Set<String> roles;

    @NonNull
    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

}
