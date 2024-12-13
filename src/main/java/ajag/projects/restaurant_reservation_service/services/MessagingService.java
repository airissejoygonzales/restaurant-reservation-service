package ajag.projects.restaurant_reservation_service.services;

import ajag.projects.restaurant_reservation_service.entities.Customer;
import ajag.projects.restaurant_reservation_service.entities.Reservation;
import ajag.projects.restaurant_reservation_service.enums.PreferredComms;

import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/** This service class is responsible for sending notification and reminders to the customer through their preferred communication method. */
@Service
public class MessagingService {

    private static final Logger logger = LoggerFactory.getLogger(MessagingService.class);

    /** This method represents the sending of notifications when reservations are confirmed and/or canceled. */
    protected void sendNotification(Reservation reservation) {
        Customer customer = reservation.getCustomer();
        String preferredComms = customer.getPreferredComms() == PreferredComms.EMAIL ? customer.getEmail() : customer.getPhoneNumber();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy h:mma");
        String reservationDateTime = reservation.getReservationDate().format(formatter);

        String notification = String.format("""
            
                Sending Notification to: %s
                
                Dear %s,
                
                Your reservation for %d guests has been %s.
                Reservation Date and Time: %s
                
                Kind Regards,
                Us""",
            preferredComms,
            customer.getName(),
            reservation.getGuestCount(),
            reservation.getStatus().toString().toLowerCase(),
            reservationDateTime
        );

        logger.info(notification);
    }

    /** This method represents the sending of reminders 4 hours prior to the reservation date. */
    protected void sendReservationReminder(Reservation reservation) {
        Customer customer = reservation.getCustomer();
        String preferredComms = customer.getPreferredComms() == PreferredComms.EMAIL ? customer.getEmail() : customer.getPhoneNumber();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy h:mma");
        String reservationDateTime = reservation.getReservationDate().format(formatter);

        String reminder = String.format("""
                
                Sending Reminder to: %s
                
                Dear %s,
                
                You have a reservation for %d guests.
                Reservation Date and Time: %s
                
                Thanks and Kind Regards,
                Us""",
            preferredComms,
            customer.getName(),
            reservation.getGuestCount(),
            reservationDateTime
        );

        logger.info(reminder);
    }

    /** This method represents the sending of reminders in the reservation date. */
    public void sendReservationCompletion(Reservation reservation) {
        Customer customer = reservation.getCustomer();
        String preferredComms = customer.getPreferredComms() == PreferredComms.EMAIL ? customer.getEmail() : customer.getPhoneNumber();

        String notification = String.format("""
                
                Sending Reminder to: %s
                
                Dear %s,
                
                You have a reservation scheduled right now!
                Enjoy your experience and have a great time!
                
                Kind Regards,
                Us""",
            preferredComms,
            customer.getName()
        );

        logger.info(notification);
    }
}
