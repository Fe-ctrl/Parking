package parkingproject.com.Parking.repository;

import parkingproject.com.Parking.model.ParkingTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<ParkingTicket, Long> {

    @Procedure(procedureName = "PROC_REGISTRY_ENTRY")
    String registerEntry(
            @Param("p_vehicle_plate") String p_vehicle_plate,
            @Param("p_type_id") Integer p_type_id,
            @Param("p_model") String p_model,
            @Param("p_color") String p_color
    );

    @Procedure(procedureName = "PROC_REGISTRY_EXIT")
    String registerExit(
            @Param("p_vehicle_plate") String p_vehicle_plate
    );

    @Procedure(procedureName = "PROC_CANCEL_TICKET")
    String cancelTicket(
            @Param("p_vehicle_place") String p_vehicle_plate
    );
}