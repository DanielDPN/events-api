package com.events.api.controllers;

import com.events.api.models.Event;
import com.events.api.models.User;
import com.events.api.repository.EventRepository;
import com.events.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/events")
public class EventController {

    final EventRepository eventRepository;
    final UserRepository userRepository;

    @Autowired
    public EventController(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents(@RequestParam(required = false) String name) {
        try {
            List<Event> events = new ArrayList<>();

            if (name == null)
                events.addAll(eventRepository.findAll());
            else
                events.addAll(eventRepository.findByNameContaining(name));

            if (events.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable("id") String id) {
        Optional<Event> eventData = eventRepository.findById(id);

        return eventData.map(event -> new ResponseEntity<>(event, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) {
        try {
            Optional<User> user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
            if (user.isPresent()) {
                Event _event = eventRepository.save(new Event(event.getName(), event.getDate(), user.get()));
                return new ResponseEntity<>(_event, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable("id") String id, @Valid @RequestBody Event event) {
        try {
            Optional<Event> eventData = eventRepository.findById(id);

            if (eventData.isPresent()) {
                Event _event = eventData.get();
                _event.setName(event.getName());
                _event.setDate(event.getDate());
                Optional<User> user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
                if (user.isPresent()) {
                    if (!user.get().equals(eventData.get().getUser())) {
                        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                    }
                } else {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
                return new ResponseEntity<>(eventRepository.save(_event), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteEvent(@PathVariable("id") String id) {
        try {
            Optional<Event> eventData = eventRepository.findById(id);

            if (eventData.isPresent()) {
                Optional<User> user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
                if (user.isPresent()) {
                    if (!user.get().equals(eventData.get().getUser())) {
                        return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
                    }
                } else {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
                eventRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteAllEvents() {
        try {
            eventRepository.deleteAll();
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
