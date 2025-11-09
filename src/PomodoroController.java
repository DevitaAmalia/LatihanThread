import javax.swing.*;

public class PomodoroController implements PomodoroModelListener {
    private final PomodoroModel model;
    private final PomodoroView view;
    private final PomodoroSessionDao sessionDao = new PomodoroSessionDao();
    private final PomodoroConfigDao configDao = new PomodoroConfigDao();
    private long phaseStartMillis = System.currentTimeMillis();

    private SwingWorker<Void, Integer> worker;
    private volatile boolean paused = false;

    public PomodoroController(PomodoroModel model, PomodoroView view) {
        this.model = model;
        this.view = view;

        // wire model -> view
        model.addListener(this);

        // wire view -> controller
        view.onStart(e -> start());
        view.onPause(e -> pause());
        view.onReset(e -> reset());
        view.onSkip(e -> skip());

        // initial render
        view.renderPhase(model.getPhase(), model.getSecondsLeft(), model.getTotalSeconds());
        view.renderStatus("Ready.");
    }

    // ====== actions ======
    public void start() {
        // ambil config terbaru dari view dan apply ke model hanya jika belum ada worker aktif
        if (worker == null || worker.isDone()) {
            model.setConfig(view.readConfig());
            configDao.save(model.getConfig());
        }

        if (worker != null && !worker.isDone()) {
            paused = false;
            view.renderStatus("Running...");
            return;
        }

        paused = false;
        view.renderStatus("Running...");

        worker = new SwingWorker<Void, Integer>() {
            @Override
            protected Void doInBackground() throws Exception {
                while (!isCancelled() && !model.isPhaseFinished()) {
                    if (paused) {
                        Thread.sleep(150);
                        continue;
                    }
                    Thread.sleep(1000);
                    model.decrementOneSecond();
                }
                return null;
            }

            @Override
            protected void done() {
                if (isCancelled()) return;
                if (model.isPhaseFinished()) {
                    java.awt.Toolkit.getDefaultToolkit().beep();
                    model.onWorkFinishedIncrementCycle();
                    long end = System.currentTimeMillis();
                    sessionDao.insertSession(model.getPhase(), phaseStartMillis, end,
                            model.getTotalSeconds(), model.getCyclesCompleted());
                    model.nextPhaseAuto();
                    view.renderStatus("Auto-switched.");
                    start(); // auto-lanjut fase berikutnya
                }
            }
        };
        worker.execute();
    }

    public void pause() {
        if (worker != null && !worker.isDone()) {
            paused = true;
            view.renderStatus("Paused.");
        }
    }

    public void reset() {
        if (worker != null && !worker.isDone()) worker.cancel(true);
        paused = false;
        model.setConfig(view.readConfig());
        model.resetToWork();
        configDao.save(model.getConfig()); 
    }

    public void skip() {
        if (worker != null && !worker.isDone()) worker.cancel(true);
        paused = false;
        model.nextPhaseAuto();
        view.renderStatus("Skipped to next phase.");
        start();
    }

    // ===== model callbacks =====
    @Override
    public void onTick(int secondsLeft, int totalSeconds, int progressPercent) {
        view.renderTick(secondsLeft, totalSeconds, progressPercent);
    }

   @Override
    public void onPhaseChanged(Phase phase, int secondsLeft, int totalSeconds) {
        view.renderPhase(phase, secondsLeft, totalSeconds);
        phaseStartMillis = System.currentTimeMillis();
    }

    @Override
    public void onCyclesChanged(int cyclesCompleted) {
        view.renderCycles(cyclesCompleted);
    }

    @Override
    public void onStatus(String message) {
        view.renderStatus(message);
    }
}
