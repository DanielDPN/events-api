package com.events.api.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "events")
public class Event {

    @ApiModelProperty(value = "Código do evento")
    @Id
    private String id;
    @ApiModelProperty(value = "Nome do evento")
    @NonNull
    private String name;
    @ApiModelProperty(value = "Data do evento")
    @NonNull
    private Date date;
    @ApiModelProperty(value = "Usuário que criou/atualizou o evento")
    @DBRef
    @NonNull
    private User user;

}
