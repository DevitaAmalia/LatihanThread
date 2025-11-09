public class App {
public static void main(String[] args) {
    javax.swing.SwingUtilities.invokeLater(() -> {
    PomodoroConfig cfg = new PomodoroConfig(25, 5, 15, 4);
    PomodoroModel model = new PomodoroModel(cfg);
    PomodoroView view = new PomodoroView();
        new PomodoroController(model, view);
        view.setVisible(true);
    });
    }
}
