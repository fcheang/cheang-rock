package com.suntek.scheduler.ui;

import java.awt.*;
import javax.swing.SwingUtilities;

/**
 * <p>Title: Appointment Scheduler</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 * <p>Company: Sunteksystems</p>
 *
 * @author not attributable
 * @version 1.0
 */

public class WaitCursorEventQueue extends EventQueue {

    public WaitCursorEventQueue(int delay) {
        this.delay = delay;
        waitTimer = new WaitCursorTimer();
        waitTimer.setDaemon(true);
        waitTimer.start();
    }

    protected void dispatchEvent(AWTEvent event) {
        waitTimer.startTimer(event.getSource());
        try {
            super.dispatchEvent(event);
        }
        finally {
            waitTimer.stopTimer();
        }
    }

    private int delay;
    private WaitCursorTimer waitTimer;

    private class WaitCursorTimer
        extends Thread {

        synchronized void startTimer(Object source) {
            this.source = source;
            notify();
        }

        synchronized void stopTimer() {
            if (parent == null)
                interrupt();
            else {
                parent.setCursor(null);
                parent = null;
            }
        }

        public synchronized void run() {
            while (true) {
                try {
                    //wait for notification from startTimer()
                    wait();

                    //wait for event processing to reach the threshold, or
                    //interruption from stopTimer()
                    wait(delay);

                    if (source instanceof Component)
                        parent =
                            SwingUtilities.getRoot( (Component) source);
                    else if (source instanceof MenuComponent) {
                        MenuContainer mParent =
                            ( (MenuComponent) source).getParent();
                        if (mParent instanceof Component)
                            parent = SwingUtilities.getRoot(
                                (Component) mParent);
                    }
                    if (parent != null && parent.isShowing())
                        parent.setCursor(
                            Cursor.getPredefinedCursor(
                                Cursor.WAIT_CURSOR));
                }
                catch (InterruptedException ie) {}
            }
        }

        private Object source;
        private Component parent;
    }
}

