package com.softwebdevelopers.ecommerce.repository;

import com.softwebdevelopers.ecommerce.models.common.InvoiceReferenceIdentity;
import com.softwebdevelopers.ecommerce.models.common.InvoiceReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CInvoiceReferenceRepository extends JpaRepository<InvoiceReference, InvoiceReferenceIdentity> {

    Optional<InvoiceReference> findByIdYearAndIdUserId(int year, Long userId);

}
