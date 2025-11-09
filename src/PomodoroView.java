import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PomodoroView extends JFrame {
    // labels
    private final JLabel modeLabel = bigLabel("WORK");
    private final JLabel timeLabel = bigLabel("25:00");
    private final JLabel cycleLabel = new JLabel("Work completed: 0", SwingConstants.CENTER);
    private final JLabel statusLabel = new JLabel("Ready.", SwingConstants.CENTER);

    // progress
    private final JProgressBar progressBar = new JProgressBar(0, 100);

    // controls
    private final JButton startBtn = new JButton("Start");
    private final JButton pauseBtn = new JButton("Pause");
    private final JButton resetBtn = new JButton("Reset");
    private final JButton skipBtn  = new JButton("Skip ▶");

    // settings (minutes)
    private final JSpinner workMinSpin  = new JSpinner(new SpinnerNumberModel(25, 1, 180, 1));
    private final JSpinner shortMinSpin = new JSpinner(new SpinnerNumberModel(5, 1, 60, 1));
    private final JSpinner longMinSpin  = new JSpinner(new SpinnerNumberModel(15, 1, 180, 1));
    private final JSpinner cyclesSpin   = new JSpinner(new SpinnerNumberModel(4, 1, 12, 1));

    public PomodoroView() {
        setTitle("Pomodoro Timer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(560, 380);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(12,12));

        // top
        JPanel top = new JPanel(new GridLayout(2,1));
        top.add(modeLabel);
        top.add(timeLabel);
        add(top, BorderLayout.NORTH);

        // center
        JPanel center = new JPanel(new GridLayout(3,1,8,8));
        progressBar.setStringPainted(true);
        cycleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        statusLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        center.add(progressBar);
        center.add(cycleLabel);
        center.add(statusLabel);
        add(center, BorderLayout.CENTER);

        // left settings
        JPanel left = new JPanel(new GridBagLayout());
        left.setBorder(BorderFactory.createTitledBorder("Settings (minutes)"));
        addRow(left, 0, new JLabel("Work"), workMinSpin);
        addRow(left, 1, new JLabel("Short Break"), shortMinSpin);
        addRow(left, 2, new JLabel("Long Break"), longMinSpin);
        addRow(left, 3, new JLabel("Long break every"), cyclesSpin, new JLabel("work(s)"));
        add(left, BorderLayout.WEST);

        // bottom controls
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        bottom.add(startBtn);
        bottom.add(pauseBtn);
        bottom.add(resetBtn);
        bottom.add(skipBtn);
        add(bottom, BorderLayout.SOUTH);
    }

    // ===== render helpers =====
    public void renderPhase(Phase phase, int secondsLeft, int totalSeconds) {
        modeLabel.setText(phase.title());
        renderTick(secondsLeft, totalSeconds, 0);
    }

    public void renderTick(int secondsLeft, int totalSeconds, int progressPercent) {
        timeLabel.setText(format(secondsLeft));
        progressBar.setValue(progressPercent);
    }

    public void renderCycles(int cyclesCompleted) {
        cycleLabel.setText("Work completed: " + cyclesCompleted);
    }

    public void renderStatus(String msg) {
        statusLabel.setText(msg);
    }

    // ===== expose actions =====
    public void onStart(ActionListener l) { startBtn.addActionListener(l); }
    public void onPause(ActionListener l) { pauseBtn.addActionListener(l); }
    public void onReset(ActionListener l) { resetBtn.addActionListener(l); }
    public void onSkip(ActionListener l)  { skipBtn.addActionListener(l); }

    // ===== read settings =====
    public PomodoroConfig readConfig() {
        int w = (Integer) workMinSpin.getValue();
        int s = (Integer) shortMinSpin.getValue();
        int l = (Integer) longMinSpin.getValue();
        int n = (Integer) cyclesSpin.getValue();
        return new PomodoroConfig(w, s, l, n);
    }

    // ===== utilities =====
    private static JLabel bigLabel(String t) {
        JLabel l = new JLabel(t, SwingConstants.CENTER);
        l.setFont(new Font("SansSerif", Font.BOLD, 28));
        return l;
    }

    private static void addRow(JPanel p, int row, Component c1, Component c2) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4,6,4,6);
        gc.gridy = row;
        gc.gridx = 0; gc.anchor = GridBagConstraints.LINE_START;
        p.add(c1, gc);
        gc.gridx = 1; gc.weightx = 1.0; gc.fill = GridBagConstraints.HORIZONTAL;
        p.add(c2, gc);
    }
    private static void addRow(JPanel p, int row, Component c1, Component c2, Component c3) {
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(4,6,4,6);
        gc.gridy = row;
        gc.gridx = 0; gc.anchor = GridBagConstraints.LINE_START;
        p.add(c1, gc);
        gc.gridx = 1; gc.weightx = 1.0; gc.fill = GridBagConstraints.HORIZONTAL;
        p.add(c2, gc);
        gc.gridx = 2; gc.weightx = 0; gc.fill = GridBagConstraints.NONE;
        p.add(c3, gc);
    }

    public static String format(int secs) {
        int m = secs / 60;
        int s = secs % 60;
        return String.format("%02d:%02d", m, s);
    }
}
