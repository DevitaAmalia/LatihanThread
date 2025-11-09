import java.util.ArrayList;
import java.util.List;

public class PomodoroModel {
    private PomodoroConfig config;
    private Phase phase = Phase.WORK;
    private int secondsLeft;
    private int totalSeconds;
    private int cyclesCompleted;

    private final List<PomodoroModelListener> listeners = new ArrayList<>();

    public PomodoroModel(PomodoroConfig config) {
        this.config = config;
        applyPhase(Phase.WORK, config.getWorkSeconds());
    }

    public void addListener(PomodoroModelListener l) { listeners.add(l); }
    public void removeListener(PomodoroModelListener l) { listeners.remove(l); }

    public Phase getPhase() { return phase; }
    public int getSecondsLeft() { return secondsLeft; }
    public int getTotalSeconds() { return totalSeconds; }
    public int getCyclesCompleted() { return cyclesCompleted; }
    public PomodoroConfig getConfig() { return config; }

    public void setConfig(PomodoroConfig newConfig) {
        this.config = newConfig;
        fireStatus("Config updated.");
    }

    public void applyPhase(Phase newPhase, int durationSeconds) {
        this.phase = newPhase;
        this.totalSeconds = durationSeconds;
        this.secondsLeft = durationSeconds;
        firePhaseChanged();
        fireTick();
    }

    public void decrementOneSecond() {
        if (secondsLeft > 0) {
            secondsLeft--;
            fireTick();
        }
    }

    public boolean isPhaseFinished() { return secondsLeft <= 0; }

    public void onWorkFinishedIncrementCycle() {
        if (phase == Phase.WORK) {
            cyclesCompleted++;
            fireCyclesChanged();
        }
    }

    public void nextPhaseAuto() {
        if (phase == Phase.WORK) {
            int n = config.getLongBreakEveryNWorks();
            if (n > 0 && (cyclesCompleted % n) == 0) {
                applyPhase(Phase.LONG_BREAK, config.getLongBreakSeconds());
            } else {
                applyPhase(Phase.SHORT_BREAK, config.getShortBreakSeconds());
            }
        } else {
            applyPhase(Phase.WORK, config.getWorkSeconds());
        }
    }

    public void resetToWork() {
        cyclesCompleted = 0;
        applyPhase(Phase.WORK, config.getWorkSeconds());
        fireStatus("Reset.");
    }

    private void fireTick() {
        int progress = (int) Math.round((1.0 * (totalSeconds - secondsLeft) / Math.max(1, totalSeconds)) * 100.0);
        for (PomodoroModelListener l : listeners) l.onTick(secondsLeft, totalSeconds, progress);
    }

    private void firePhaseChanged() {
        for (PomodoroModelListener l : listeners) l.onPhaseChanged(phase, secondsLeft, totalSeconds);
    }

    private void fireCyclesChanged() {
        for (PomodoroModelListener l : listeners) l.onCyclesChanged(cyclesCompleted);
    }

    private void fireStatus(String msg) {
        for (PomodoroModelListener l : listeners) l.onStatus(msg);
    }
}


