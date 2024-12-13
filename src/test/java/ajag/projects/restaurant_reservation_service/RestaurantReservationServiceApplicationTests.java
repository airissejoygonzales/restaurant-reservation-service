package ajag.projects.restaurant_reservation_service;

import ajag.projects.restaurant_reservation_service.entities.Customer;
import ajag.projects.restaurant_reservation_service.entities.Reservation;
import ajag.projects.restaurant_reservation_service.enums.PreferredComms;
import ajag.projects.restaurant_reservation_service.repositories.CustomerRepository;
import ajag.projects.restaurant_reservation_service.repositories.ReservationRepository;
import ajag.projects.restaurant_reservation_service.services.CustomerService;
import ajag.projects.restaurant_reservation_service.services.MessagingService;
import ajag.projects.restaurant_reservation_service.services.ReservationSchedulingService;
import ajag.projects.restaurant_reservation_service.services.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantReservationServiceApplicationTests {

	@InjectMocks
	private ReservationService reservationService;

	@Mock
	private ReservationRepository reservationRepository;

	@Mock
	private CustomerService customerService;

	@Mock
	private ReservationSchedulingService reservationSchedulingService;

	@Mock
	private MessagingService messagingService;

	Map<Long, List<Reservation>> reservationMap = new HashMap<>();

    @BeforeEach
	void setUp() {
		// Arrange: Prepare mock customer data
        Customer customer1 = new Customer();
		customer1.setId(1L);
		customer1.setName("John");
		customer1.setEmail("john@example.com");
		customer1.setPhoneNumber("09222222222");
		customer1.setPreferredComms(PreferredComms.EMAIL);
		reservationMap.put(customer1.getId(), new ArrayList<>());

        Customer customer2 = new Customer();
		customer2.setId(2L);
		customer2.setName("Jane");
		customer2.setEmail("jane@example.com");
		customer2.setPhoneNumber("09999999999");
		customer2.setPreferredComms(PreferredComms.SMS);
		reservationMap.put(customer2.getId(), new ArrayList<>());

		// Arrange: Prepare mock reservation data
		Reservation reservation1 = new Reservation();
		reservation1.setCustomer(customer1);
		reservation1.setReservationDate(LocalDateTime.of(2025, 12, 9, 10, 30, 0));
		reservation1.setGuestCount(5);
		reservationMap.get(reservation1.getCustomer().getId()).add(reservation1);

        Reservation reservation2 = new Reservation();
		reservation2.setCustomer(customer1);
		reservation2.setReservationDate(LocalDateTime.of(2025, 1, 10, 1, 30, 0));
		reservation2.setGuestCount(12);
		reservationMap.get(reservation2.getCustomer().getId()).add(reservation2);

		// Mock reservation repository behavior
		lenient().when(reservationRepository.findAllByCustomer_IdOrderByStatusDescReservationDateAsc(1L))
				.thenReturn(reservationMap.get(1L));

		// Mocking dynamic behavior for save method based on the input reservation object
		lenient().when(reservationRepository.save(ArgumentMatchers.any(Reservation.class)))
				.thenAnswer(invocation -> {
					// Dynamically get the reservation passed as argument and return it
					return invocation.getArgument(0);
				});


		// Mock customer service behavior
		lenient().when(customerService.findById(2L)).thenReturn(Optional.of(customer2));
	}

	@Test
	void testGetCustomerReservations() {
		// Act: Call the method for retrieving customer reservations
		List<Reservation> result = reservationService.getCustomerReservations(1L);

		// Assert: Verify the result
		assertThat(result).hasSize(2);

		// Validate the first reservation
		assertThat(result.get(0).getCustomer().getId()).isEqualTo(1L);
		assertThat(result.get(0).getCustomer().getName()).isEqualTo("John");
		assertThat(result.get(0).getCustomer().getEmail()).isEqualTo("john@example.com");
		assertThat(result.get(0).getCustomer().getPhoneNumber()).isEqualTo("09222222222");
		assertThat(result.get(0).getCustomer().getPreferredComms()).isEqualTo(PreferredComms.EMAIL);
		assertThat(result.get(0).getReservationDate()).isEqualTo("2025-12-09T10:30:00");
		assertThat(result.get(0).getGuestCount()).isEqualTo(5);

		// Validate the second reservation
		assertThat(result.get(1).getCustomer().getId()).isEqualTo(1L);
		assertThat(result.get(1).getCustomer().getName()).isEqualTo("John");
		assertThat(result.get(1).getCustomer().getEmail()).isEqualTo("john@example.com");
		assertThat(result.get(1).getCustomer().getPhoneNumber()).isEqualTo("09222222222");
		assertThat(result.get(1).getCustomer().getPreferredComms()).isEqualTo(PreferredComms.EMAIL);
		assertThat(result.get(1).getReservationDate()).isEqualTo("2025-01-10T01:30:00");
		assertThat(result.get(1).getGuestCount()).isEqualTo(12);
	}

	@Test
	void testCreateReservation() {

		// Arrange: Prepare mock data
		Customer customer = new Customer();
		customer.setId(2L);

		Reservation reservation = new Reservation();
		reservation.setCustomer(customer);
		reservation.setReservationDate(LocalDateTime.of(2025, 2, 14, 1, 0, 0));
		reservation.setGuestCount(2);

		// Act: Call the method for creating reservation
		Reservation result = reservationService.createReservation(reservation);

		// Assert: Validate the created reservation
		assertThat(result).isNotNull();
		assertThat(result.getCustomer().getId()).isEqualTo(2L);
		assertThat(result.getCustomer().getName()).isEqualTo("Jane");
		assertThat(result.getCustomer().getEmail()).isEqualTo("jane@example.com");
		assertThat(result.getCustomer().getPhoneNumber()).isEqualTo("09999999999");
		assertThat(result.getCustomer().getPreferredComms()).isEqualTo(PreferredComms.SMS);
		assertThat(result.getReservationDate()).isEqualTo("2025-02-14T01:00:00");
		assertThat(result.getGuestCount()).isEqualTo(2);
	}
}
