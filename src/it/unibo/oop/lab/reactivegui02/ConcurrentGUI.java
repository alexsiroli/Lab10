package it.unibo.oop.lab.reactivegui02;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ConcurrentGUI extends JFrame {

    private static final long serialVersionUID = 1L;
    private static final double WIDTH_PERC = 0.2;
    private static final double HEIGHT_PERC = 0.1;
    private final JLabel display = new JLabel();
    private final JButton up = new JButton("up");
    private final JButton down = new JButton("down");
    private final JButton stop = new JButton("stop");

    public ConcurrentGUI() {
        super();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize((int) (screenSize.getWidth() * WIDTH_PERC), (int) (screenSize.getHeight() * HEIGHT_PERC));
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        final JPanel panel = new JPanel();
        panel.add(display);
        panel.add(up);
        panel.add(down);
        panel.add(stop);
        this.getContentPane().add(panel);
        this.setVisible(true);

        final Agent agent = new Agent();
        new Thread(agent).start();

        up.addActionListener(e -> agent.upCounting());
        down.addActionListener(e -> agent.downCounting());
        stop.addActionListener(e -> {
                                        agent.stopCounting();
                                        up.setEnabled(false);
                                        down.setEnabled(false);
                                        stop.setEnabled(false);
                                        });

    }

    private class Agent implements Runnable {

        private volatile boolean stop;
        private volatile boolean direction = true;
        private int counter;

        @Override
        public void run() {
            while (!this.stop) {
                try {

                    SwingUtilities.invokeAndWait(() -> ConcurrentGUI.this.display.setText(Integer.toString(Agent.this.counter)));

                    this.counter = this.direction ? this.counter + 1 : this.counter - 1;

                    Thread.sleep(100);
                } catch (InvocationTargetException | InterruptedException ex) {

                    ex.printStackTrace();
                }
            }
        }

        public void upCounting() {
            this.direction = true;
        }

        public void downCounting() {
            this.direction = false;
        }

        public void stopCounting() {
            this.stop = true;
        }

    }

}
