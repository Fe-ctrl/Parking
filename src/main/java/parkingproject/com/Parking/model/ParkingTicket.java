package parkingproject.com.Parking.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "PARKING_TICKET")
@Data
public class ParkingTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TICKET_ID")
    private Long ticketId;

    @Column(name = "VEHICLE_PLATE")
    private String vehiclePlate;

    @Column(name = "SPOT_ID")
    private Long spotId;

    @Column(name = "RULE_ID")
    private Long ruleId;

    @Column(name = "TICKET_STATUS_ID")
    private Integer ticketStatusId;

    @Column(name = "ENTRY_TIME")
    private LocalDateTime entryTime;

    @Column(name = "EXIT_TIME")
    private LocalDateTime exitTime;

    @Column(name = "TOTAL_FEE")
    private Double totalFee;
}