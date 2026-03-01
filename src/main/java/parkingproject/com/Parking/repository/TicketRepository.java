package parkingproject.com.Parking.repository;

import parkingproject.com.Parking.model.ParkingTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<ParkingTicket, Long> {

    @Procedure(procedureName = "proc_registry_entry")
    String registerEntry(
            @Param("p_vehicle_plate") String plate,
            @Param("p_type_id") Integer typeId,
            @Param("p_model") String model,
            @Param("p_color") String color
    );

    @Procedure(procedureName = "proc_registry_exit")
    String registerExit(
            @Param("p_vehicle_plate") String plate
    );
}