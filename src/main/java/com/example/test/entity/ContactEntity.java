package com.example.test.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("testCollection")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ContactEntity {
    @Id
    private String id;
    private String name;
    private String phone;
    private String email;

    public ContactEntity(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }
}
