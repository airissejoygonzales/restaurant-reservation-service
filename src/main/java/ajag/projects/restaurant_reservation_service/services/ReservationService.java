package ajag.projects.restaurant_reservation_service.services;

import ajag.projects.restaurant_reservation_service.entities.Customer;
import ajag.projects.restaurant_reservation_service.entities.Reservation;
import ajag.projects.restaurant_reservation_service.enums.ReservationStatus;
import ajag.projects.restaurant_reservation_service.exceptions.NotFoundException;
import ajag.projects.restaurant_reservation_service.repositories.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** This service class is responsible for handling reservation-related operations.
 * It uses {@link ReservationRepository} to perform CRUD operations on the {@link Reservation} entity. */
@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final CustomerService customerService;
    private final ReservationSchedulingService schedulerService;
    private final MessagingService messagingService;

    /** This constructor is used to inject the dependencies ({@link ReservationRepository},
     * {@link CustomerService}, {@link ReservationSchedulingService}, {@link MessagingService}) into this class.*/
    public ReservationService(ReservationRepository reservationRepository, CustomerService customerService, ReservationSchedulingService schedulerService, MessagingService messagingService) {
        this.reservationRepository = reservationRepository;
        this.customerService = customerService;
        this.schedulerService = schedulerService;
        this.messagingService = messagingService;
    }

    /** This method is used to add a new reservation in the system
     * It first checks if a customer ID (reservation.customer.id) is provided in the reservation(parameter).
     * (1) If the customer ID is provided, the method will try to find the existing customer using that ID.
     *      If found, the method will link the reservation to that customer.
     *      If NOT found, a {@link NotFoundException} is thrown.
     * (2) If the customer ID is NOT provided, the method will create a new customer using the details from the reservation (reservation.customer).
     *     The new customer will be saved in the database and the reservation will be linked to that new customer.
     * After determining the correct customer, the reservation is saved in the database.
     * Then new task schedules related to the saved reservation will be created as well.
     * Lastly, the customer will be notified of the confirmed reservations using the sendNotification method.
     *
     * @param reservation - request entity containing the details of the reservation and the linked customer. */
    @Transactional
    public Reservation createReservation(Reservation reservation) {
        Long customerId = reservation.getCustomer().getId();

        if(customerId == null){
            Customer newCustomer = customerService.addCustomer(reservation.getCustomer());
            reservation.setCustomer(newCustomer);
        } else {
            Customer existingCustomer = customerService.findById(customerId)
                .orElseThrow(() -> new NotFoundException("Customer not found! ID: " + customerId));
            reservation.setCustomer(existingCustomer);
        }

        Reservation savedReservation = reservationRepository.save(reservation);

        schedulerService.addReservationSchedules(savedReservation);
        messagingService.sendNotification(savedReservation);

        return savedReservation;
    }

    /** This method is used to cancel a reservation.
     * It searches for the reservation using the id(parameter).
     * (1) If NOT found, the method throws a {@link NotFoundException}.
     * (2) If found, the method sets that existing reservation's status to 'CANCELLED'
     *     then saves the updated version in the database.
     * Then the task schedules related to the canceled reservation will be canceled as well.
     * Lastly, the customer will be notified of the cancellation using the sendNotification method.
     *
     * @param id - ID of the reservation. */
    public Reservation cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Reservation not found! ID: " + id));

        reservation.setStatus(ReservationStatus.CANCELLED);
        Reservation canceledReservation = reservationRepository.save(reservation);

        schedulerService.cancelReservationSchedule(canceledReservation.getId());
        messagingService.sendNotification(canceledReservation);

        return canceledReservation;
    }

    /** This method is used to get all the reservation entities using the customerId(parameter).
     *
     * @param customerId - ID of the customer that is linked to the reservation. */
    public List<Reservation> getCustomerReservations(Long customerId) {
        return reservationRepository.findAllByCustomer_IdOrderByStatusDescReservationDateAsc(customerId);
    }

    /** This method is used to update the details of a reservation.
     * It searches for the reservation using the id(parameter).
     * (1) If NOT found, the method throws a {@link NotFoundException}.
     * (2) If found, the method updates the value of the reservationDate and guestCount of that existing reservation
     *     then saves the updated version in the database.
     * Then the task schedules related to the updated reservation will be updated as well.
     * Lastly, the customer will be notified that these changes are confirmed using the sendNotification method.
     *
     * @param id - ID of the reservation.
     * @param reservationDate - date and time of the reservation
     * @param guestCount - number of guests for the reservation*/
    public Reservation updateReservation(Long id, LocalDateTime reservationDate, Integer guestCount) {
        Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Reservation not found! ID: " + id));

        reservation.setReservationDate(reservationDate);
        reservation.setGuestCount(guestCount);

        Reservation updatedReservation = reservationRepository.save(reservation);

        schedulerService.updateReservationReminder(updatedReservation);
        messagingService.sendNotification(updatedReservation);

        return updatedReservation;
    }

    /** This method is used to delete a reservation.
     * It searches for the reservation using the id(parameter).
     * (1) If NOT found, the method throws a {@link NotFoundException}.
     * (2) If found, the method deletes that existing reservation.
     *
     * @param id - ID of the reservation. */
    public void deleteReservationById(Long id) {
        Reservation reservation = reservationRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Reservation not found! ID: " + id));
        reservationRepository.delete(reservation);
    }
}
