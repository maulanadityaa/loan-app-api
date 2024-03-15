package org.enigma.livecodeloan.repository;

import org.enigma.livecodeloan.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
//    @Query(value = "SELECT * FROM m_customer WHERE status = 'ACTIVE'", nativeQuery = true)
//    List<Customer> getCustomerWhereActive();
}
