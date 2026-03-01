package parkingproject.com.Parking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import parkingproject.com.Parking.repository.TicketRepository;

@RestController
@RequestMapping("/api/parking")
public class TicketController {

    @Autowired
    private TicketRepository ticketRepository;

    // Test the Entry Procedure
    @GetMapping("/entry")
    public String enterVehicle(
            @RequestParam String plate,
            @RequestParam Integer type,
            @RequestParam String model,
            @RequestParam String color) {

        // This calls your Oracle Procedure via the Repository
        return ticketRepository.registerEntry(plate, type, model, color);
    }

    // Test the Exit Procedure
    @GetMapping("/exit")
    public String exitVehicle(@RequestParam String plate) {
        return ticketRepository.registerExit(plate);
    }
}