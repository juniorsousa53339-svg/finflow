package com.finflow.transfer.adapter.out.persistence.repository;

import com.finflow.transfer.adapter.out.persistence.entity.TransferJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferJpaRepository extends JpaRepository<TransferJpaEntity, Long> {
}
