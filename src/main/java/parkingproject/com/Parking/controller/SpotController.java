package parkingproject.com.Parking.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parkingproject.com.Parking.service.SpotService;

import java.util.Map;

@RestController
@RequestMapping("/api/spots")
public class SpotController {

    @Autowired
    private SpotService spotService;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getOccupancy() {
        Map<String, Object> stats = spotService.getOccupancyStats();
        return ResponseEntity.ok(stats);
    }
}