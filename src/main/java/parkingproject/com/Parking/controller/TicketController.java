package parkingproject.com.Parking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parkingproject.com.Parking.repository.TicketRepository;

@RestController
@RequestMapping("/api/parking")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    @GetMapping("/entry")
    public ResponseEntity<String> enterVehicle(
            @RequestParam String plate,
            @RequestParam Integer type,
            @RequestParam String model,
            @RequestParam String color) {

        String result = ticketRepository.registerEntry(plate, type, model, color);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/exit")
    public String exitVehicle(@RequestParam String plate) {
        return ticketRepository.registerExit(plate);
    }

    @GetMapping("/cancel")
    public String cancelTicket(@RequestParam String plate) {
        return ticketRepository.cancelTicket(plate);
    }
}