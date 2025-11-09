public interface PomodoroModelListener {
    void onTick(int secondsLeft, int totalSeconds, int progressPercent);
    void onPhaseChanged(Phase phase, int secondsLeft, int totalSeconds);
    void onCyclesChanged(int cyclesCompleted);
    void onStatus(String message);
}





