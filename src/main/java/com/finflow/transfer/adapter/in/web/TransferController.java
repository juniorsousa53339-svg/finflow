package com.finflow.transfer.adapter.in.web;

import com.finflow.transfer.adapter.in.web.dto.TransferRequestDTO;
import com.finflow.transfer.adapter.in.web.dto.TransferResponseDTO;
import com.finflow.transfer.adapter.in.web.mapper.TransferWebMapper;
import com.finflow.transfer.domain.model.Transfer;
import com.finflow.transfer.domain.port.in.CreateTransferUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final CreateTransferUseCase transferUseCase;
    private final TransferWebMapper transferWebMapper;

    @PostMapping
    public ResponseEntity<TransferResponseDTO> createTransfer(
            @RequestBody @Valid TransferRequestDTO request
    ) {


        Transfer savando =
                transferUseCase.createTransfer(
                        transferWebMapper.toDomain(request));

        var resposta =
                transferWebMapper.toResponseDTO(savando);

        return ResponseEntity.status(
                        HttpStatus.CREATED).body(resposta);
    }
}
