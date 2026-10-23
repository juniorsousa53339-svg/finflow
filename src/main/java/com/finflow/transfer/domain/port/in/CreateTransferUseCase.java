package com.finflow.transfer.domain.port.in;

import com.finflow.transfer.domain.model.Transfer;

public interface CreateTransferUseCase {
  Transfer createTransfer(Transfer transfer);
}
