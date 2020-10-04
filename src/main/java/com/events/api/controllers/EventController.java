package com.events.api.controllers;

import com.events.api.models.Event;
import com.events.api.models.User;
import com.events.api.repository.EventRepository;
import com.events.api.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation(value = "Retorna uma lista de eventos")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna a lista de eventos"),
            @ApiResponse(code = 204, message = "Lista de eventos vazia"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção")
    })
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents(@RequestParam(required = false) String name) {
        try {
            List<Event> events = new ArrayList<>();

            if (name == null)
                events.addAll(eventRepository.findAll());
            else
                events.addAll(eventRepository.findByNameContainingIgnoreCase(name));

            if (events.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(events, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Retorna um evento")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna um evento"),
            @ApiResponse(code = 404, message = "Evento não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable("id") String id) {
        Optional<Event> eventData = eventRepository.findById(id);

        return eventData.map(event -> new ResponseEntity<>(event, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "Retorna o novo evento registrado")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Retorna um evento"),
            @ApiResponse(code = 400, message = "Falta o parâmetro 'user'"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção")
    })
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
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

    @ApiOperation(value = "Retorna o evento atualizado")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Retorna um evento"),
            @ApiResponse(code = 400, message = "Falta o parâmetro 'user'"),
            @ApiResponse(code = 404, message = "Evento não encontrado"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção")
    })
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<Event> updateEvent(@PathVariable("id") String id, @Valid @RequestBody Event event) {
        try {
            Optional<Event> eventData = eventRepository.findById(id);

            if (eventData.isPresent()) {
                Event _event = eventData.get();
                _event.setName(event.getName());
                _event.setDate(event.getDate());
                Optional<User> user = userRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
                if (user.isPresent()) {
                    _event.setUser(user.get());
                    return new ResponseEntity<>(eventRepository.save(_event), HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Remove o evento")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Remove o evento"),
            @ApiResponse(code = 404, message = "Evento não encontrado"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção")
    })
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteEvent(@PathVariable("id") String id) {
        try {
            Optional<Event> eventData = eventRepository.findById(id);

            if (eventData.isPresent()) {
                eventRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ApiOperation(value = "Remove todos os eventos")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Remove todos os eventos"),
            @ApiResponse(code = 500, message = "Foi gerada uma exceção")
    })
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
