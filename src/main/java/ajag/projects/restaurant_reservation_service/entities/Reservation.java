package ajag.projects.restaurant_reservation_service.entities;

import ajag.projects.restaurant_reservation_service.enums.ReservationStatus;
import ajag.projects.restaurant_reservation_service.exceptions.InvalidValueException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

/** This class represents the reservations made by the customer in the system */
@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false) // Foreign key references to "customers(id)"
    private Customer customer;

    private LocalDateTime reservationDate;

    private Integer guestCount;

    @Enumerated(EnumType.STRING) // Stores the enum as a string in the database
    private ReservationStatus status = ReservationStatus.CONFIRMED; // Default status

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp // Automatically updates with the current timestamp on updates
    private LocalDateTime lastModifiedAt;

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        if (reservationDate.isBefore(LocalDateTime.now())) { throw new InvalidValueException("reservationDate should not be in the past!"); }
        this.reservationDate = reservationDate;
    }

    public Integer getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(Integer guestCount) {
        if (guestCount <= 0) { throw new InvalidValueException("guestCount should be greater than 0!"); }
        this.guestCount = guestCount;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getLastModifiedAt() {
        return lastModifiedAt;
    }

}