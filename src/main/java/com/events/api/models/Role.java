package com.events.api.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Document(collection = "roles")
public class Role {
    @Id
    private String id;

    @NonNull
    private ERole name;

}
