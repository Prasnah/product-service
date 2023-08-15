package com.prasanna.products.Repository;

import com.prasanna.products.Entity.PendingProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalRepository extends JpaRepository<PendingProduct, Long> {}
