package ajag.projects.restaurant_reservation_service.repositories;

import ajag.projects.restaurant_reservation_service.entities.Customer;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/** This repository interface is used to manage {@link Customer} entities.
 * It extends {@link JpaRepository}, providing methods used to interact with the {@link Customer} entities.*/
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /** This method fetches the customer data for the email provided. */
    Optional<Customer> findByEmail(String email);

}

