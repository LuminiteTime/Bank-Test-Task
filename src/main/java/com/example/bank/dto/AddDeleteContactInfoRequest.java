package com.example.bank.dto;

import lombok.Getter;

import java.util.Set;

@Getter
public class AddDeleteContactInfoRequest {
    private Set<String> phoneNumbers;
    private Set<String> emailAddresses;
}
