public class RunTimer {
    private long start = 0;
    private long end = 0;
    private long elapsed = 0;
    boolean stopped = true;

    /**
     * <p>Determine if the RunTimer is currently stopped.</p>
     *
     * @return true if and only if the RunTimer is currently stopped
     */
    public boolean isStopped() { return stopped; }

    /**
     * <p>Start the RunTimer if it is currently stopped. The method
     * has no effect if the RunTimer is already running. </p>
     *
     */
    public void start() {
	if (stopped) {
	    start = System.nanoTime();
	    stopped = false;
	}
    }

    /**
     * <p>Stop the RunTimer if it is not already stopped. The elapsed
     *time since the previous call to <code>this.start()</code> will
     *be added to the total elapsed time stored by the RunTimer.  </p>
     */
    public void stop() {
	if (!stopped) {
	    end = System.nanoTime();
	    elapsed += (end - start);
	    stopped = true;
	}
    }

    /**
     * <p>Stop and reset the RunTimer so that the elapsed time is
     * <code>0</code>.</p>
     */
    public void reset() {
	stop();
	elapsed = 0;
    }
			 
    /**
     * <p>Return the total elapsed time stored in the RunTimer
     * measured in nanoseconds. If the RunTimer is stopped, then this
     * time will be the sum of the durations of the start-stop
     * intervals since the previous <code>reset()</code> (if any). If
     * the RunTimer is not stopped, then the time since the last call
     * to <code>stop()</code> is included as well.</p>
     *
     * @return the total elapsed time in nanoseconds
     */
    public long getElapsedNanos() {
	if (stopped) {
	    return elapsed;
	}

	return System.nanoTime() - start + elapsed;
    }
    

    /**
     * <p>Return the total elapsed time stored in the RunTimer
     * measured in milliseconds.</p>
     *
     * @see getElapsedNanos
     * @return the total elapsed time in milliseconds
     */
    public long getElapsedMillis() {
	return getElapsedNanos() / 1_000_000;
    }

    /**
     * <p>Return the total elapsed time stored in the RunTimer
     * measured in seconds.</p>
     *
     * @see getElapsedNanos
     * @return the total elapsed time in seconds
     */
    public double getElapsedSecs() {
	return (double) getElapsedNanos() / 1_000_000_000;
    }
}
