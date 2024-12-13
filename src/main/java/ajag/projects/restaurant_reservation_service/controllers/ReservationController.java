package ajag.projects.restaurant_reservation_service.controllers;

import ajag.projects.restaurant_reservation_service.entities.Reservation;
import ajag.projects.restaurant_reservation_service.services.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/** This controller class is used to create REST APIs for managing reservations.
 * It provides endpoints for creating, viewing, cancelling, deleting, and updating reservation information. */
@RestController
@RequestMapping("/reservations")
//@CrossOrigin(origins = "http://localhost:3000")  // Allow CORS for this controller
public class ReservationController {

    private final ReservationService reservationService;

    /** This constructor is used to inject the {@link ReservationService} dependency into this class.*/
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    /** Creates a new reservation in the system.
     * <p>This endpoint enables customers to make a reservation by providing their name, phone number, email,
     * reservation date and time, and the number of guests. Then, the system confirms the reservation
     * and sends a notification through the customer's preferred method of communication (e.g., SMS or Email).</p>
     *
     * @param reservation The {@link Reservation} entity containing the reservation details.
     * @return A {@link ResponseEntity} containing the saved {@link Reservation} entity and an HTTP status of 201 (Created). */
    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        Reservation newReservation = reservationService.createReservation(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(newReservation);
    }

    /** Cancels an existing reservation in the system.
     * <p>This endpoint enables customers to change their plans and cancel their reservation using a reservation ID.
     * Then, the system confirms the cancellation and sends a notification through the customer's
     * preferred method of communication (e.g., SMS or Email). </p>
     *
     * @param id The ID of the reservation to be canceled.
     * @return A {@link ResponseEntity} containing the updated {@link Reservation} entity with cancellation details.*/
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<Reservation> cancelReservation(@PathVariable Long id) {
        Reservation canceledReservation = reservationService.cancelReservation(id);
        return ResponseEntity.ok(canceledReservation);
    }

    /** View all existing reservations for a specific customer.
     * <p>This endpoint enables customers to view a list of all their upcoming reservations
     * by providing their customer ID. The returned list helps customers manage their bookings effectively. </p>
     *
     * @param customerId The ID of the customer whose reservations are to be retrieved.
     * @return A list of {@link Reservation} entities representing the customer's upcoming reservations. */
    @GetMapping("/customer/{customerId}")
    public List<Reservation> viewReservations(@PathVariable Long customerId) {
        return reservationService.getCustomerReservations(customerId);
    }

    /** Updates the reservation details of an existing reservation in the system.
     * <p>This endpoint enables customers to update the time and number of guests in their reservation.</p>
     *
     * @param id The ID of the reservation to be updated.
     * @param reservationDate The new date and time of the reservation.
     * @param guestCount The updated number of guests of the reservation.
     * @return A {@link ResponseEntity} containing the updated {@link Reservation} entity.*/
    @PatchMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(
        @PathVariable Long id,
        @RequestParam LocalDateTime reservationDate,
        @RequestParam Integer guestCount) {
        Reservation updatedReservation = reservationService.updateReservation(id, reservationDate, guestCount);
        return ResponseEntity.ok(updatedReservation);
    }

    /** Deletes an existing reservation in the system.
     * <p>This endpoint enables the deletion of a reservation by providing its ID.</p>
     *
     * @param id The ID of the reservation to be deleted.
     * @return A {@link ResponseEntity} containing a confirmation message indicating successful deletion. */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservationById(id);
        return ResponseEntity.ok("Reservation successfully deleted! ID: " + id);
    }

}

