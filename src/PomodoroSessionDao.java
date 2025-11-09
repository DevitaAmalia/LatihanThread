import java.sql.*;

public class PomodoroSessionDao {
    public void ensureTable() {
        String sql = "CREATE TABLE IF NOT EXISTS pomodoro_session (" +
                "id BIGINT PRIMARY KEY AUTO_INCREMENT," +
                "phase VARCHAR(20) NOT NULL," +
                "start_time TIMESTAMP NOT NULL," +
                "end_time TIMESTAMP NOT NULL," +
                "duration_sec INT NOT NULL," +
                "cycles_completed INT NOT NULL" +
                ")";
        try (Connection c = DB.getConnection(); Statement st = c.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void insertSession(Phase phase, long startMillis, long endMillis,
                              int durationSec, int cyclesCompleted) {
        String sql = "INSERT INTO pomodoro_session " +
                "(phase, start_time, end_time, duration_sec, cycles_completed) " +
                "VALUES (?, FROM_UNIXTIME(?/1000), FROM_UNIXTIME(?/1000), ?, ?)";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, phase.name());
            ps.setLong(2, startMillis);
            ps.setLong(3, endMillis);
            ps.setInt(4, durationSec);
            ps.setInt(5, cyclesCompleted);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

