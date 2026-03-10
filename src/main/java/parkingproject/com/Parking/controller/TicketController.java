package parkingproject.com.Parking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parkingproject.com.Parking.dto.VehicleEntryDTO;
import parkingproject.com.Parking.service.TicketService;

@RestController
@RequestMapping("/api/parking")
@CrossOrigin(origins = "http://localhost:5173")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/entry")
    public ResponseEntity<String> enterVehicle(@RequestBody VehicleEntryDTO request) {
        String result = ticketService.registerEntry(
                request.getPlate(),
                request.getType(),
                request.getModel(),
                request.getColor()
        );
        return ResponseEntity.ok(result);
    }

    @PostMapping("/exit")
    public ResponseEntity<String> exitVehicle(@RequestParam String plate) {
        return ResponseEntity.ok(ticketService.registerExit(plate));
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelTicket(@RequestParam String plate) {
        return ResponseEntity.ok(ticketService.cancelTicket(plate));
    }
}