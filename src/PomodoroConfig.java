public class PomodoroConfig {
    private int workMinutes;
    private int shortBreakMinutes;
    private int longBreakMinutes;
    private int longBreakEveryNWorks;

    public PomodoroConfig(int workMinutes, int shortBreakMinutes, int longBreakMinutes, int longBreakEveryNWorks) {
        this.workMinutes = workMinutes;
        this.shortBreakMinutes = shortBreakMinutes;
        this.longBreakMinutes = longBreakMinutes;
        this.longBreakEveryNWorks = longBreakEveryNWorks;
    }


    public int getWorkSeconds() { return workMinutes * 60; }
    public int getShortBreakSeconds() { return shortBreakMinutes * 60; }
    public int getLongBreakSeconds() { return longBreakMinutes * 60; }
    public int getLongBreakEveryNWorks() { return longBreakEveryNWorks; }


    public void setWorkMinutes(int m) { this.workMinutes = m; }
    public void setShortBreakMinutes(int m) { this.shortBreakMinutes = m; }
    public void setLongBreakMinutes(int m) { this.longBreakMinutes = m; }
    public void setLongBreakEveryNWorks(int n) { this.longBreakEveryNWorks = n; }
    
}
