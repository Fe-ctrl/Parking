package parkingproject.com.Parking.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import parkingproject.com.Parking.model.Spot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import java.util.Map;

@Repository
public interface SpotRepository extends JpaRepository<Spot, Long> {

}
