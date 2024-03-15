package org.enigma.livecodeloan.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.enigma.livecodeloan.constant.EStatus;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Entity
@Data
@Table(name = "mst_customer")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "date_of_birth")
    private Date dob;

    @Column(name = "phone", nullable = false, unique = true, length = 50)
    private String mobilePhone;

    @Column(name = "marriage_status", nullable = false, unique = true, length = 50)
    private String marriageStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private EStatus status;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserCredential user;
}
