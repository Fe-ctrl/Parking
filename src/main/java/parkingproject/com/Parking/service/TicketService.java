package parkingproject.com.Parking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import parkingproject.com.Parking.repository.TicketRepository;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public String registerEntry(String plate, Integer type, String model, String color) {
        return ticketRepository.registerEntry(plate, type, model, color);
    }

    public String registerExit(String plate) {
        return ticketRepository.registerExit(plate);
    }

    public String cancelTicket(String plate) {
        return ticketRepository.cancelTicket(plate);
    }
}
