package ajag.projects.restaurant_reservation_service.enums;

/** Enum representing the status of the reservation. */
public enum ReservationStatus {
    CANCELLED, // Status of the reservation when customer chose to cancel
    COMPLETED, // Status of the reservation when the reservation date and time already past
    CONFIRMED // Status of the reservation at the start when reservation is created
}