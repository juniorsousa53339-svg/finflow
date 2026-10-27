package com.finflow.processing.adapter.out.persistence;

import com.finflow.processing.adapter.out.entity.TransferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataTransferRepository extends JpaRepository<TransferEntity, Long> {
}
