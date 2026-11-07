package com.finflow.processing.adapter.in.web;

import com.finflow.processing.adapter.in.web.dto.TransferResponseDTO;
import com.finflow.processing.domain.port.out.TransferRepositoryPort;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferRepositoryPort transferRepositoryPort;

    @GetMapping
    public ResponseEntity<List<TransferResponseDTO>> getAllTransfers() {
        List<TransferResponseDTO> transfers = transferRepositoryPort.findAll().stream()
                .map(TransferResponseDTO::fromDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(transfers);
    }
}
