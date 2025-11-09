import java.sql.*;

public class PomodoroConfigDao {
    public void ensureTable() {
        String sql = "CREATE TABLE IF NOT EXISTS pomodoro_config (" +
                "id INT PRIMARY KEY," +
                "work_min INT NOT NULL," +
                "short_min INT NOT NULL," +
                "long_min INT NOT NULL," +
                "long_every INT NOT NULL" +
                ")";
        try (Connection c = DB.getConnection(); Statement st = c.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public PomodoroConfig loadOrDefault() {
        String sql = "SELECT work_min, short_min, long_min, long_every " +
                     "FROM pomodoro_config WHERE id=1";
        try (Connection c = DB.getConnection();
             Statement st = c.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                return new PomodoroConfig(rs.getInt(1), rs.getInt(2), rs.getInt(3), rs.getInt(4));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
        return new PomodoroConfig(25,5,15,4);
    }

    public void save(PomodoroConfig cfg) {
        String up = "REPLACE INTO pomodoro_config(id, work_min, short_min, long_min, long_every) " +
                    "VALUES (1,?,?,?,?)";
        try (Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement(up)) {
            ps.setInt(1, cfg.getWorkSeconds()/60);
            ps.setInt(2, cfg.getShortBreakSeconds()/60);
            ps.setInt(3, cfg.getLongBreakSeconds()/60);
            ps.setInt(4, cfg.getLongBreakEveryNWorks());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}

