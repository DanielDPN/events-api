package com.events.api.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "roles")
public class Role {

    @ApiModelProperty(value = "Código da role")
    @Id
    private String id;

    @ApiModelProperty(value = "Nome da role")
    @NonNull
    private ERole name;

}
