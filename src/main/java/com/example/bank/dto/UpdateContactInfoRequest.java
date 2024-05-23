package com.example.bank.dto;

import lombok.Getter;

import java.util.Map;

@Getter
public class UpdateContactInfoRequest {
    private Map<String, String> phoneNumbers;
    private Map<String, String> emailAddresses;
}
