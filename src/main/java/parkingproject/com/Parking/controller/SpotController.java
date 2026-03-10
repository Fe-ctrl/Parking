package parkingproject.com.Parking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import parkingproject.com.Parking.service.SpotService;

import java.util.Map;

@RestController
@RequestMapping("/api/spots")
@CrossOrigin(origins = "http://localhost:5173")
public class SpotController {

    @Autowired
    private SpotService spotService;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getOccupancy() {
        Map<String, Object> stats = spotService.getOccupancyStats();
        return ResponseEntity.ok(stats);
    }
}