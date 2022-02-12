package com.softwebdevelopers.ecommerce.models.common;

import com.softwebdevelopers.ecommerce.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder(toBuilder = true)
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceReferenceIdentity implements Serializable {

    private static final long serialVersionUID = -1119068356185370930L;

    @Column(name = "year")
    private int year;

    @Column(name = "user_id")
    private Long userId;

//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
}
