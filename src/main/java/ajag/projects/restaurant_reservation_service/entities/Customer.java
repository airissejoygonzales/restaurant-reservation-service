package ajag.projects.restaurant_reservation_service.entities;

import ajag.projects.restaurant_reservation_service.enums.PreferredComms;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** This class represents the customers of the system that can make/update/cancel reservations */
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String phoneNumber;

    @Enumerated(EnumType.STRING) // Stores the enum as a string in the database
    private PreferredComms preferredComms = PreferredComms.EMAIL; // Default communication method

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public PreferredComms getPreferredComms() {
        return preferredComms;
    }

    public void setPreferredComms(PreferredComms preferredComms) {
        this.preferredComms = preferredComms;
    }
}
