package com.finflow.transfer.domain.port.out;

import com.finflow.transfer.domain.model.Transfer;

public interface TransferPersistencePort{
    Transfer save(Transfer transfer);
}
