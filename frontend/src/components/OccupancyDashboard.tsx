import { useState, useEffect } from 'react';
import axios from 'axios';

interface OccupancyStats {
    TOTAL_SPOTS: number;
    OCCUPIED: number;
    AVAILABLE: number;
}

const OccupancyDashboard = () => {
    const [stats, setStats] = useState<OccupancyStats | null>(null);
    const [loading, setLoading] = useState<boolean>(true);

    const fetchStats = async () => {
        try {
            setLoading(true);
            const response = await axios.get<OccupancyStats>('http://localhost:8080/api/spots/stats');
            setStats(response.data);
        } catch (error) {
            console.error("Error fetching stats:", error);
        } finally {
            setLoading(false);
        }
    };

    useEffect(() => {
        fetchStats();
    }, []);

    if (loading) return <p>Connecting to Oracle Database...</p>;

    return (
        <div style={{ padding: '20px', border: '1px solid #ccc', borderRadius: '8px' }}>
            <h2>Parking Occupancy Dashboard</h2>
            {stats ? (
                <div style={{ display: 'flex', gap: '20px' }}>
                    <div className="stat-box">
                        <strong>Total:</strong> {stats.TOTAL_SPOTS}
                    </div>
                    <div className="stat-box" style={{ color: 'red' }}>
                        <strong>Occupied:</strong> {stats.OCCUPIED}
                    </div>
                    <div className="stat-box" style={{ color: 'green' }}>
                        <strong>Available:</strong> {stats.AVAILABLE}
                    </div>
                </div>
            ) : (
                <p>Could not retrieve data from backend.</p>
            )}
            <button onClick={fetchStats} style={{ marginTop: '10px' }}>Refresh</button>
        </div>
    );
};

export default OccupancyDashboard;