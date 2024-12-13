package ajag.projects.restaurant_reservation_service.services;

import ajag.projects.restaurant_reservation_service.entities.Customer;
import ajag.projects.restaurant_reservation_service.exceptions.NotFoundException;
import ajag.projects.restaurant_reservation_service.repositories.CustomerRepository;

import java.util.Optional;

import org.springframework.stereotype.Service;

/** This service class is responsible for handling customer-related operations.
 * It uses {@link CustomerRepository} to perform CRUD operations on the {@link Customer} entity. */
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    /** This constructor is used to inject the {@link CustomerRepository} dependency into this class.*/
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    /** This method is used to add new customer in the system. It saves the customer(parameter) in the database.
     *
     * @param customer - request entity containing the details of the customer. */
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    /** This method is used to get the customer entity using the email(parameter).
     *
     * @param email - email address of the customer. */
    public Optional<Customer> findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    /** This method is used to get the customer entity using the id(parameter).
     *
     * @param id - ID of the customer. */
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    /** This method is used to update the details of a customer.
     * It checks the existence of a customer using the id of the customer(parameter).
     * (1) If it does NOT exist, the method throws a {@link NotFoundException}.
     * (2) If it does exist, the method saves the customer(parameter) in the database.
     *
     * @param customer - request entity containing the details of the customer. */
    public Customer updateCustomer(Customer customer) {
        if (!customerRepository.existsById(customer.getId())) {
            throw new NotFoundException("Customer not found! ID: " + customer.getId());
        }
        return customerRepository.save(customer);
    }

}
