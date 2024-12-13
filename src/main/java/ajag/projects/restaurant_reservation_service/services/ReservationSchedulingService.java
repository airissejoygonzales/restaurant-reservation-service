package ajag.projects.restaurant_reservation_service.services;

import ajag.projects.restaurant_reservation_service.entities.Customer;
import ajag.projects.restaurant_reservation_service.entities.Reservation;
import ajag.projects.restaurant_reservation_service.enums.ReservationStatus;
import ajag.projects.restaurant_reservation_service.repositories.CustomerRepository;
import ajag.projects.restaurant_reservation_service.repositories.ReservationRepository;

import jakarta.annotation.PostConstruct;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

/** This service class is responsible for handling the schedules for the reservation.
 * It uses {@link CustomerRepository} to perform CRUD operations on the {@link Customer} entity. */
@Service
public class ReservationSchedulingService {

    private final TaskScheduler taskScheduler;
    private final ReservationRepository reservationRepository;
    private final MessagingService messagingService;

    /** This constructor is used to inject the dependencies ({@link TaskScheduler},
     * {@link ReservationRepository}, {@link MessagingService}) into this class.*/
    public ReservationSchedulingService(TaskScheduler taskScheduler, ReservationRepository reservationRepository, MessagingService messagingService) {
        this.taskScheduler = taskScheduler;
        this.reservationRepository = reservationRepository;
        this.messagingService = messagingService;
    }

    /** This variable stores all active scheduled tasks (sending reminder/completing reservation). */
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new HashMap<>();

    /** Initialize confirmed reservations from the database and schedule them on application startup. */
    @PostConstruct
    public void initializeReservations() {
        List<Reservation> reservations = reservationRepository.findAllByStatus(ReservationStatus.CONFIRMED);
        for (Reservation reservation : reservations) {
            addReservationSchedules(reservation);
        }
    }

    /** This method schedules the tasks that will either
     * (1) send a reminder 4 hours prior the reservation or
     * (2) complete the reservation by setting its status to COMPLETED.
     * Then adds it to the list of active scheduled tasks. */
    private void scheduleReservationTask(Runnable task, Long reservationId, String cronExpression) {
        ScheduledFuture<?> future = taskScheduler.schedule(task, new CronTrigger(cronExpression));
        scheduledTasks.put(reservationId, future);
    }

    /** This method creates the runnable tasks and calls the method scheduleReservationTask to add a schedule. */
    protected void addReservationSchedules(Reservation reservation) {

        // This is the task for the reservation completion.
        // When this task is run, it
        // (1) sets the reservation status to COMPLETED and saves it to the database,
        // (2) cancels the current schedule for the task so it won't run twice
        // (3) sends a reminder to the customer about his/her reservation right now
        Runnable completeTask = () -> {
            reservation.setStatus(ReservationStatus.COMPLETED);
            reservationRepository.save(reservation);

            cancelReservationSchedule(reservation.getId());

            messagingService.sendReservationCompletion(reservation);
        };

        // This is the task for the reservation reminder
        // When this task is run, it
        // (1) sends a reminder to the customer that s/he has a reservation 4 hours from now
        // (2) cancels the current schedule for the task so it won't run twice
        // (3) schedule the task for completing the reservation
        Runnable reminderTask = () -> {
            messagingService.sendReservationReminder(reservation);

            cancelReservationSchedule(reservation.getId());

            scheduleReservationTask(completeTask, reservation.getId(), getCronExpression(reservation.getReservationDate()));
        };

        // If the current time is before the 4-hour mark before the reservation,
        // then the reminderTask will be added to the schedules.
        // But if it's already past that then the completeTask will be scheduled instead.
        if(LocalDateTime.now().isBefore(reservation.getReservationDate().minusHours(4))) {
            scheduleReservationTask(reminderTask, reservation.getId(), getCronExpression(reservation.getReservationDate().minusHours(4)));
        } else {
            scheduleReservationTask(completeTask, reservation.getId(), getCronExpression(reservation.getReservationDate()));
        }

    }

    /** This method cancels the current schedule of the reservation and creates a new one for the new reservation. */
	protected void updateReservationReminder(Reservation reservation) {
        cancelReservationSchedule(reservation.getId());
		addReservationSchedules(reservation);
	}

    /** This method cancels the current schedule of the reservation. */
	protected void cancelReservationSchedule(Long reservationId) {
		ScheduledFuture<?> future = scheduledTasks.get(reservationId);
		if (future != null) {
			future.cancel(false);
			scheduledTasks.remove(reservationId);
		}
	}

    /** This method help create the cron expression to be used in scheduling the reservation tasks created.
     * It converts the given LocalDateTime to String with the cron pattern. */
    private String getCronExpression(LocalDateTime reservation) {

        int minute = reservation.getMinute();
        int hour = reservation.getHour();
        int dayOfMonth = reservation.getDayOfMonth();
        int month = reservation.getMonthValue();
        int dayOfWeek = reservation.getDayOfWeek().getValue(); // 1=Monday, ..., 7=Sunday

        return String.format("0 %d %d %d %d %d", minute, hour, dayOfMonth, month, dayOfWeek);
    }

}