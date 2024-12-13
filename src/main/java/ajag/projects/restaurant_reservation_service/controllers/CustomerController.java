package ajag.projects.restaurant_reservation_service.controllers;

import ajag.projects.restaurant_reservation_service.entities.Customer;
import ajag.projects.restaurant_reservation_service.services.CustomerService;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** This controller class is used to create REST APIs for managing customers.
 * It provides endpoints for adding, retrieving, and updating customer information. */
@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    /** This constructor is used to inject the {@link CustomerService} dependency into this class.*/
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /** Adds a new customer to the system.
     *
     * @param customer The {@link Customer} entity to be added.
     * @return The saved {@link Customer} entity. */
    @PostMapping
    public Customer addCustomer(@RequestBody Customer customer) {
        return customerService.addCustomer(customer);
    }

    /** Retrieves a customer by their email.
     *
     * @param email The email of the customer to retrieve.
     * @return An {@link Optional} containing the {@link Customer} if found, otherwise empty. */
    @GetMapping("/{email}")
    public Optional<Customer> getCustomer(@PathVariable String email) {
        return customerService.findByEmail(email);
    }

    /** Updates the details of an existing customer.
     *
     * @param customer The {@link Customer} entity with updated information.
     * @return The updated {@link Customer} entity. */
    @PutMapping
    public Customer updateCustomer(@RequestBody Customer customer) {
        return customerService.updateCustomer(customer);
    }

}

