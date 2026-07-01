package com.example.demo.service;

import com.example.demo.entity.Event;
import com.example.demo.entity.Venue;
import com.example.demo.repository.VenueRepository;
import com.example.demo.repository.EventRepository;
import com.example.demo.response.EventInventoryResponse;
import com.example.demo.response.VenueInventoryResponse;
import com.example.demo.response.VenueResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class InventoryService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;



    public List<EventInventoryResponse> getAllEvents() {
        final List<Event> events = eventRepository.findAll();

        return events.stream()
                .map(event -> EventInventoryResponse.builder()
                        .eventId(event.getId())
                        .event(event.getName())
                        .capacity(event.getLeftCapacity())
                        .totalCapacity(event.getTotalCapacity())
                        .ticketPrice(event.getTicketPrice())
                        .venue(VenueResponse.builder()
                                .id(event.getVenue().getId())
                                .name(event.getVenue().getName())
                                .address(event.getVenue().getAddress())
                                .location(event.getVenue().getAddress())
                                .city("")
                                .totalCapacity(event.getVenue().getTotalCapacity())
                                .capacity(event.getVenue().getTotalCapacity())
                                .build())
                        .build())
                .toList();
    }

    public List<EventInventoryResponse> getAvailableEvents() {
        final List<Event> events = eventRepository.findAvailableEvents();

        return events.stream()
                .map(event -> EventInventoryResponse.builder()
                        .eventId(event.getId())
                        .event(event.getName())
                        .capacity(event.getLeftCapacity())
                        .totalCapacity(event.getTotalCapacity())
                        .ticketPrice(event.getTicketPrice())
                        .venue(VenueResponse.builder()
                                .id(event.getVenue().getId())
                                .name(event.getVenue().getName())
                                .address(event.getVenue().getAddress())
                                .location(event.getVenue().getAddress())
                                .city("")
                                .totalCapacity(event.getVenue().getTotalCapacity())
                                .capacity(event.getVenue().getTotalCapacity())
                                .build())
                        .build())
                .toList();
    }

    public VenueInventoryResponse getVenueInformation(final Long venueId) {
        final Venue venue = venueRepository.findById(venueId).orElseThrow(() -> new EntityNotFoundException("Venue not found: " + venueId));
        return VenueInventoryResponse.builder()
                .venueId(venue.getId())
                .venueName(venue.getName())
                .totalCapacity(venue.getTotalCapacity())
                .build();
    }

    public EventInventoryResponse getEventInventory(final Long eventId) {
        final Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));

        return EventInventoryResponse.builder()
                .event(event.getName())
                .capacity(event.getLeftCapacity())
                .totalCapacity(event.getTotalCapacity())
                .ticketPrice(event.getTicketPrice())
                .eventId(event.getId())
                .venue(VenueResponse.builder()
                        .id(event.getVenue().getId())
                        .name(event.getVenue().getName())
                        .address(event.getVenue().getAddress())
                        .location(event.getVenue().getAddress())
                        .city("")
                        .totalCapacity(event.getVenue().getTotalCapacity())
                        .capacity(event.getVenue().getTotalCapacity())
                        .build())
                .build();
    }

    public void updateEventCapacity(final Long eventId, final Long ticketsBooked) {
        final Event event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Event not found: " + eventId));
        event.setLeftCapacity(event.getLeftCapacity() - ticketsBooked);
        eventRepository.saveAndFlush(event);
        log.info("Updated event capacity for event id: {} with tickets booked: {}", eventId, ticketsBooked);
    }


}
