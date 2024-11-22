package com.example.test.repository;

import com.example.test.entity.ContactEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactRepository extends MongoRepository<ContactEntity,String> {

}
