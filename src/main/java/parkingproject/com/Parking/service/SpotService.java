package parkingproject.com.Parking.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SpotService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Map<String, Object> getOccupancyStats() {
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("PROC_GET_OCCUPANCY_STATS");

        return jdbcCall.execute();
    }
}