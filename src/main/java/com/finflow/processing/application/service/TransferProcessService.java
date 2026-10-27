package com.finflow.processing.application.service;

import com.finflow.processing.domain.model.Transfer;
import com.finflow.processing.domain.port.in.TransferUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class TransferProcessService implements TransferUseCase {

    @Override
    public Transfer transferReceived(Transfer transfer) {
        return null;
    }
}
