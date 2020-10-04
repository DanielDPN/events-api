package com.events.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "users")
public class User {

    @ApiModelProperty(value = "Código do usuário")
    @Id
    private String id;

    @ApiModelProperty(value = "Username do usuário")
    @NonNull
    private String username;

    @ApiModelProperty(value = "E-mail do usuário")
    @NonNull
    private String email;

    @ApiModelProperty(value = "Senha do usuário")
    @NonNull
    @JsonIgnore
    private String password;

    @ApiModelProperty(value = "Roles do usuário")
    @DBRef
    private Set<Role> roles = new HashSet<>();

}
