package com.softwebdevelopers.ecommerce.models.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "invoice_reference")
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceReference {

    @EmbeddedId
    private InvoiceReferenceIdentity id;

    @Column(name = "latest_sequence")
    private int latestSequence;

    @Column(name = "latest_invoice_number", length = 100)
    private String latestInvoiceNumber;
}
