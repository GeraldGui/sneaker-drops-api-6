package com.pluralsight.sneakerdrops.controller;

import com.pluralsight.sneakerdrops.models.Sneaker;
import com.pluralsight.sneakerdrops.service.SneakerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/sneakers") // Writes an endpoint for all mapping
@CrossOrigin // Allows the front-end to communicate
public class SneakerController {

    private final SneakerService sneakerService;

    public SneakerController(SneakerService sneakerService) {
        this.sneakerService = sneakerService;
    }

    @GetMapping
    public List<Sneaker> getAll(@RequestParam(required = false) Integer year,
                                @RequestParam(required = false) String model,
                                @RequestParam(required = false) Double minPrice,
                                @RequestParam(required = false) Double maxPrice,
                                @RequestParam(required = false) String brand,
                                @RequestParam(required = false) String sort) {
        return sneakerService.search(year, model, minPrice, maxPrice, brand, sort);
    }

    @GetMapping("/{id}")
    public Sneaker getById(@PathVariable long id) {
        Sneaker sneaker = sneakerService.byId(id);
        if (sneaker == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Sneaker with id " + id);
        }
        return sneaker;
    }

    @PostMapping
    public ResponseEntity<Sneaker> create(@Valid @RequestBody Sneaker sneaker) {
        Sneaker saved = sneakerService.createSneaker(sneaker);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    public Sneaker update(@PathVariable long id,@Valid @RequestBody Sneaker sneaker) {
        Sneaker saved =  sneakerService.updateSneaker(id, sneaker);
        if(saved == null) {
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "No sneaker with id " + id);
        }
        return saved;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        if (sneakerService.byId(id) == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No sneaker with id " + id);
        }
        sneakerService.deleteSneaker(id);
        return ResponseEntity.noContent().build();
    }
}
