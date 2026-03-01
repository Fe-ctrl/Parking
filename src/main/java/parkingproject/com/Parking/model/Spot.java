package parkingproject.com.Parking.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Data;

@Entity
@Table(name = "PARKING_SPOT")
@Data
public class Spot {

    @Id
    @Column(name = "SPOT_ID")
    private Long spotId;

    @Column(name = "TYPE_ID")
    private Long spotType;

    @Column(name = "STATUS_ID")
    private Long spotStatus;

    @Column(name = "SPOT_NUMBER")
    private Integer spotNumber;
}