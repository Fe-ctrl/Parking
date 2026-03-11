CREATE OR REPLACE PROCEDURE proc_registry_entry (
    p_vehicle_plate IN VARCHAR2,
    p_type_id IN NUMBER,
    p_model IN VARCHAR2,
    p_color IN VARCHAR2,
    p_status OUT VARCHAR2
) AS 
v_spot_id NUMBER;
v_rule_id NUMBER;
v_count_active NUMBER; 

BEGIN
    SELECT count(*) INTO v_count_active 
    FROM parking_ticket 
    WHERE vehicle_plate = p_vehicle_plate AND ticket_status_id = 1;

    IF v_count_active > 0 THEN
        p_status := 'Error: Vehicle is already in the parking lot.';
        RETURN; 
    END IF;

    SELECT spot_id INTO v_spot_id FROM parking_spot 
    WHERE type_id = p_type_id AND status_id = 1 FETCH FIRST 1 ROW ONLY;

    SELECT rule_id INTO v_rule_id FROM pricing_list 
    WHERE type_id = p_type_id FETCH FIRST 1 ROW ONLY;

    MERGE INTO vehicle v USING DUAL ON (v.vehicle_plate = p_vehicle_plate)
    WHEN NOT MATCHED THEN INSERT (vehicle_plate, type_id, model, color)
    VALUES (p_vehicle_plate, p_type_id, p_model, p_color);

    INSERT INTO parking_ticket (vehicle_plate, spot_id, rule_id, entry_time, ticket_status_id)
    VALUES (p_vehicle_plate, v_spot_id, v_rule_id, SYSTIMESTAMP, 1);

    UPDATE parking_spot SET status_id = 2 WHERE spot_id = v_spot_id;

    p_status := 'Entry successful! Parked at spot: ' || v_spot_id;
    COMMIT;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        p_status := 'Error: No available spots or pricing rule for this type.';
    WHEN OTHERS THEN
        p_status := 'Error: ' || SQLERRM;
        ROLLBACK;
END;
COMMIT;

SET SERVEROUTPUT ON;
DECLARE
    v_msg VARCHAR2(200);
BEGIN
    proc_registry_entry('ABC1234', 1, 'Toyota Camry', 'Blue', v_msg);
    DBMS_OUTPUT.PUT_LINE('Result: ' || v_msg);
END;

-- 
CREATE OR REPLACE PROCEDURE proc_registry_exit (
    p_vehicle_plate IN VARCHAR2,
    p_status OUT VARCHAR2
) AS 
    v_ticket_id NUMBER;
    v_entry_time TIMESTAMP;
    v_rule_id NUMBER;
    v_spot_id NUMBER;
    v_exit_time TIMESTAMP := SYSTIMESTAMP;
    v_calculated_fee NUMBER;
BEGIN
    SELECT ticket_id, entry_time, rule_id, spot_id 
    INTO v_ticket_id, v_entry_time, v_rule_id, v_spot_id
    FROM parking_ticket
    WHERE vehicle_plate = p_vehicle_plate AND ticket_status_id = 1;

    v_calculated_fee := fn_calculate_parking_fee(v_rule_id, v_entry_time, v_exit_time);

    UPDATE parking_ticket 
    SET exit_time = v_exit_time, total_fee = v_calculated_fee, ticket_status_id = 2 
    WHERE ticket_id = v_ticket_id;

    UPDATE parking_spot SET status_id = 1 WHERE spot_id = v_spot_id;

    p_status := 'Exit successful! Fee: $' || v_calculated_fee || '. Spot ' || v_spot_id || ' is now free.';
    COMMIT;

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        p_status := 'Error: No active ticket found for plate: ' || p_vehicle_plate;
    WHEN OTHERS THEN
        p_status := 'Error: ' || SQLERRM;
        ROLLBACK;
END;
/

--
CREATE OR REPLACE PROCEDURE proc_cancel_ticket (
    p_vehicle_plate IN VARCHAR2,
    p_status OUT VARCHAR2
) AS
v_ticket_id NUMBER;
v_spot_id NUMBER;
v_exit_time TIMESTAMP := SYSTIMESTAMP;
v_entry_time TIMESTAMP;
BEGIN
    SELECT ticket_id, spot_id, entry_time INTO v_ticket_id, v_spot_id, v_entry_time
    FROM parking_ticket
    WHERE vehicle_plate = p_vehicle_plate AND ticket_status_id = 1;

    IF (fn_cancel_ticket(v_entry_time)) THEN
        UPDATE parking_ticket 
        SET exit_time = v_exit_time, total_fee = 0, ticket_status_id = 3 
        WHERE ticket_id = v_ticket_id;

        UPDATE parking_spot SET status_id = 1 WHERE spot_id = v_spot_id;

        p_status := 'Ticket cancelled successfully! Spot ' || v_spot_id || ' is now free.';
        COMMIT;
    ELSE
        p_status := 'Cancellation failed: More than 5 minutes have passed since entry, the minimum tax will need to be paid.';
    END IF;

    EXCEPTION
    WHEN NO_DATA_FOUND THEN
        p_status := 'Error: No active ticket found for this plate.';
    WHEN OTHERS THEN
        p_status := 'Error: ' || SQLERRM;
        ROLLBACK;
END;
/

-- 
CREATE OR REPLACE FUNCTION fn_calculate_parking_fee (
    p_rule_id IN NUMBER,
    p_entry_time IN TIMESTAMP,
    p_exit_time IN TIMESTAMP
) RETURN NUMBER AS 
    v_hourly_price NUMBER;
    v_min_minutes NUMBER;
    v_total_minutes NUMBER;
    v_charge_minutes NUMBER;
BEGIN
    SELECT hourly_price, minimum_time 
    INTO v_hourly_price, v_min_minutes
    FROM pricing_list 
    WHERE rule_id = p_rule_id;

    SELECT (EXTRACT(DAY FROM (p_exit_time - p_entry_time)) * 1440) +
           (EXTRACT(HOUR FROM (p_exit_time - p_entry_time)) * 60) +
           (EXTRACT(MINUTE FROM (p_exit_time - p_entry_time)))
    INTO v_total_minutes FROM DUAL;

    IF v_total_minutes < v_min_minutes THEN
        v_charge_minutes := v_min_minutes;
    ELSE
        v_charge_minutes := v_total_minutes;
    END IF;

    RETURN ROUND((v_charge_minutes / 60) * v_hourly_price, 2);

EXCEPTION
    WHEN NO_DATA_FOUND THEN
        RETURN 0;
    WHEN OTHERS THEN
        RETURN -1;
END;
/

-- 
CREATE OR REPLACE FUNCTION fn_cancel_ticket (
    p_entry_time IN TIMESTAMP
) RETURN BOOLEAN AS 
    v_current_time TIMESTAMP := SYSTIMESTAMP;
    v_total_minutes NUMBER;
BEGIN
        SELECT (EXTRACT(DAY FROM (v_current_time - p_entry_time)) * 1440) +
           (EXTRACT(HOUR FROM (v_current_time - p_entry_time)) * 60) +
           (EXTRACT(MINUTE FROM (v_current_time - p_entry_time)))
    INTO v_total_minutes FROM DUAL;

    IF v_total_minutes < 5 THEN
        RETURN TRUE;
    ELSE
        RETURN FALSE;
    END IF;
END;
/

CREATE OR REPLACE PROCEDURE proc_get_occupancy_stats (
    p_total_spots OUT NUMBER,
    p_occupied_spots OUT NUMBER,
    p_available_spots OUT NUMBER
) AS
BEGIN
    SELECT COUNT(*) INTO p_total_spots FROM parking_spot;
    
    SELECT COUNT(*) INTO p_occupied_spots FROM parking_spot WHERE status_id = 2;
    
    SELECT COUNT(*) INTO p_available_spots FROM parking_spot WHERE status_id = 1;
END;
/