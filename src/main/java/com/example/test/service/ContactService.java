package com.example.test.service;

import com.example.test.entity.ContactEntity;
import com.example.test.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService {
    private final ContactRepository contactRepository;

    public List<ContactEntity> findAll() {
        List<ContactEntity> all = contactRepository.findAll();
        log.info("Collection size is {}", all.size());
        all.forEach(c -> log.info("Contact is {}", c.toString()));
        return all;
    }

    public ContactEntity save(ContactEntity contact) {
        ContactEntity save = contactRepository.save(contact);
        return save;
    }
}
