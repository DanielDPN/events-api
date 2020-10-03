package com.events.api.payload.request.events;

import com.events.api.models.User;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Date;

public class Create {

    @NonNull
    @NotBlank
    @Size(max = 50)
    private String name;
    @NonNull
    @NotBlank
    private Date date;
    @NonNull
    @NotBlank
    private User user;

}
