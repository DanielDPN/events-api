package com.events.api.models;

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

    @Id
    private String id;
    @NonNull
    private String name;
    @NonNull
    private Date date;
    @DBRef
    @NonNull
    private User user;

}
