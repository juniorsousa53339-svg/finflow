package com.finflow.processing.domain.port.in;

import com.finflow.processing.domain.model.Transfer;

public interface TransferUseCase {

    Transfer transferReceived(Transfer transfer);
}
