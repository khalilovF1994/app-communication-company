package com.example.appcommunicationcompany.service;

import com.example.appcommunicationcompany.config.JwtProvider;
import com.example.appcommunicationcompany.entity.User;
import com.example.appcommunicationcompany.payload.GenericResponse;
import com.example.appcommunicationcompany.payload.UserDto;
import com.example.appcommunicationcompany.payload.UserLogin;
import com.example.appcommunicationcompany.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtProvider jwtProvider;


    public UserDto register(UserDto userDto) {
        boolean existsByPhoneNumberAndDeletedFalse = userRepository.existsByPhoneNumberAndDeletedFalse(userDto.getPhoneNumber());
        if (existsByPhoneNumberAndDeletedFalse)
            throw new RuntimeException("user phone number already used");
        boolean existsByEmailAddressAndDeletedFalse = userRepository.existsByEmailAddressAndDeletedFalse(userDto.getEmailAddress());
        if (existsByEmailAddressAndDeletedFalse)
            throw new RuntimeException("user email address already used");
        User user = new User();
        user.setFullName(userDto.getFullName());
        user.setPhoneNumber(userDto.getPhoneNumber());
        user.setBirthDate(userDto.getBirthDate());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmailAddress(userDto.getEmailAddress());
        user.setEmailCode(UUID.randomUUID().toString());
        User save = userRepository.save(user);
        sendEmail(user.getEmailAddress(), user.getEmailCode());
        return UserDto.toDto(save);
    }

    public Boolean sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("farkhodkhalilov7@gmail.com");
            simpleMailMessage.setTo(sendingEmail);
            simpleMailMessage.setSubject("Account tasdiqlandi");
            simpleMailMessage.setText("<a href='http://localhost:8082/api/v1/auth/verifyEmail?emailCode=" + emailCode + "&email=" + sendingEmail + "'> Tasdiqlang</a>");
            javaMailSender.send(simpleMailMessage);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }

    public GenericResponse verifyEmail(String email, String emailCode) {
        Optional<User> code = userRepository.findByEmailAddressAndEmailCode(email, emailCode);
        if (code.isPresent()) {
            User user = code.get();
            user.setEnabled(true);
            user.setEmailCode(null);
            userRepository.save(user);
            return new GenericResponse("account success verify", true);
        }
        return new GenericResponse("account filed success", false);
    }

    public GenericResponse login(UserLogin login) {
        try {
            Authentication authenticate = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login.getUserName(), login.getPassword()));

            User user = (User) authenticate.getPrincipal();
            String token = jwtProvider.generateToken(login.getUserName(), user.getRoles());
            return new GenericResponse("token", true, token);

        } catch (Exception e) {
            return new GenericResponse("userName and password wrong", false);
        }
    }
}
