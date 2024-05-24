package com.example.bank.utils;

import com.example.bank.dto.BankUserDTO;
import com.example.bank.model.BankUser;

public class MappingUtils {

    private MappingUtils() {}

    public static BankUserDTO mapToBankUserDTO(BankUser user) {
        return BankUserDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .birthDate(user.getBirthDate())
                .phoneNumbers(user.getPhoneNumbers())
                .emailAddresses(user.getEmailAddresses())
                .build();
    }
}
