package com.example.bank.controller;

import com.example.bank.dto.BankUserDTO;
import com.example.bank.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BankUserControllerTest {

    @InjectMocks
    BankUserController bankUserController;

    @Mock
    TransactionService transactionService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTransferBalanceChange() {
        Long senderId = 1L;
        Long receiverId = 2L;
        BigDecimal amount = new BigDecimal("100.00");

        BankUserDTO senderBeforeTransfer = new BankUserDTO();
        senderBeforeTransfer.setId(senderId);
        senderBeforeTransfer.setBalance(new BigDecimal("500.00"));

        BankUserDTO receiverBeforeTransfer = new BankUserDTO();
        receiverBeforeTransfer.setId(receiverId);
        receiverBeforeTransfer.setBalance(new BigDecimal("300.00"));

        BankUserDTO senderAfterTransfer = new BankUserDTO();
        senderAfterTransfer.setId(senderId);
        senderAfterTransfer.setBalance(new BigDecimal("400.00"));

        BankUserDTO receiverAfterTransfer = new BankUserDTO();
        receiverAfterTransfer.setId(receiverId);
        receiverAfterTransfer.setBalance(new BigDecimal("400.00"));

        when(transactionService.transfer(senderId, receiverId, amount)).thenReturn(senderAfterTransfer);

        ResponseEntity<BankUserDTO> response = bankUserController.transfer(senderId, receiverId, amount);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(senderAfterTransfer, response.getBody());

        verify(transactionService, times(1)).transfer(senderId, receiverId, amount);
    }
}