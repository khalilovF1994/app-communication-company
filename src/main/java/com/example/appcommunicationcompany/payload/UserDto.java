package com.example.appcommunicationcompany.payload;

import com.example.appcommunicationcompany.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.Period;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @NotNull private String fullName;

    @NotNull private String password;

    @NotNull private LocalDate birthDate;

    @Transient
    private Integer age;

    public Integer getAge() {
        return birthDate!=null? Period.between(birthDate,LocalDate.now()).getYears():0;
    }


    @Pattern(regexp = "^998[389][012345789][0-9]{7}$")
    @NotNull  private String phoneNumber;

    @NotNull @Email private String emailAddress;

    public static UserDto toDto(User save) {
        UserDto userDto=new UserDto();
        userDto.setFullName(save.getFullName());
        userDto.setPassword(save.getPassword());
        userDto.setBirthDate(save.getBirthDate());
        userDto.setPhoneNumber(save.getPhoneNumber());
        userDto.setEmailAddress(save.getEmailAddress());
        return userDto;


    }
}
