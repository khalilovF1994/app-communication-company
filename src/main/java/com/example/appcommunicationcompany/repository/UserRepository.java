package com.example.appcommunicationcompany.repository;

import com.example.appcommunicationcompany.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.Email;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Integer> {

    boolean existsByPhoneNumberAndDeletedFalse(String phoneNumber);

    boolean existsByEmailAddressAndDeletedFalse(@Email String emailAddress);

    Optional<User> findByEmailAddressAndEmailCode(@Email String emailAddress, String emailCode);


}
