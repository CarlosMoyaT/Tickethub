package com.example.bookingservice;

import com.example.bookingservice.client.InventoryServiceClient;
import com.example.bookingservice.entity.Customer;
import com.example.bookingservice.event.BookingEvent;
import com.example.bookingservice.request.BookingRequest;
import com.example.bookingservice.response.BookingResponse;
import com.example.bookingservice.response.InventoryResponse;
import com.example.bookingservice.respository.CustomerRepository;
import com.example.bookingservice.service.BookingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private InventoryServiceClient inventoryServiceClient;
    @Mock
    private KafkaTemplate<String, BookingEvent> kafkaTemplate;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void creteBooking_shouldThrowException_whenCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        BookingRequest request = BookingRequest.builder()
                .userId(1L)
                .eventId(1L)
                .ticketCount(2L)
                .build();

        assertThrows(RuntimeException.class, () -> bookingService.createBooking(request));

    }

    @Test
    void createBooking_shouldThrowException_whenNotEnoughCapacity() {
        Customer customer = Customer.builder().id(1L).build();
        InventoryResponse inventory = InventoryResponse.builder()
                .capacity(1L)
                .ticketPrice(new BigDecimal("10.00"))
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(inventoryServiceClient.getInventory(1L)).thenReturn(inventory);

        BookingRequest request = BookingRequest.builder()
                .userId(1L)
                .eventId(1L)
                .ticketCount(5L)
                .build();

        assertThrows(RuntimeException.class, () -> bookingService.createBooking(request));

    }

    @Test
    void createBooking_shouldSendKafkaEvent_whenBookingIsValid() {
        Customer customer = Customer.builder().id(1L).build();
        InventoryResponse inventory = InventoryResponse.builder()
                .capacity(10L)
                .ticketPrice(new BigDecimal("10.00"))
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(inventoryServiceClient.getInventory(1L)).thenReturn(inventory);

        BookingRequest request = BookingRequest.builder()
                .userId(1L)
                .eventId(1L)
                .ticketCount(2L)
                .build();

        BookingResponse response = bookingService.createBooking(request);

        verify(kafkaTemplate, times(1)).send(eq("booking"), any(BookingEvent.class));
        assertThat(response.getTotalPrice()).isEqualByComparingTo(new BigDecimal("20.00"));
    }
}
