package com.prasanna.products.repository;

import com.prasanna.products.entity.PendingProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends JpaRepository<PendingProduct, Long> {}
