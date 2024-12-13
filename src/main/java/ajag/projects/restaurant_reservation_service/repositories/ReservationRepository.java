package ajag.projects.restaurant_reservation_service.repositories;

import ajag.projects.restaurant_reservation_service.entities.Reservation;
import ajag.projects.restaurant_reservation_service.enums.ReservationStatus;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

/** This repository interface is used to manage {@link Reservation} entities.
 * It extends {@link JpaRepository}, providing methods used to interact with the {@link Reservation
} entities.*/
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    /** This method fetches all reservation data filtered by the status. */
    List<Reservation> findAllByStatus(ReservationStatus status);

    /** This method fetches all reservation data filtered by the customer id.
     * The returned reservations are sorted by the status in descending order (CONFIRMED, COMPLETED, CANCELLED)
     *  and then by the reservation date in ascending order (starting with the earliest reservation). */
    List<Reservation> findAllByCustomer_IdOrderByStatusDescReservationDateAsc(Long customerId);

}

