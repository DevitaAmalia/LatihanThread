public class App {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            PomodoroSessionDao sessionDao = new PomodoroSessionDao();
            PomodoroConfigDao configDao = new PomodoroConfigDao();
            sessionDao.ensureTable();
            configDao.ensureTable();

            PomodoroConfig cfg = configDao.loadOrDefault();
            PomodoroModel model = new PomodoroModel(cfg);
            PomodoroView view = new PomodoroView();
            PomodoroController controller = new PomodoroController(model, view);

            view.setVisible(true);

        });
    }
}
