package de.lathanda.assembler.cpu;

import java.util.LinkedList;
import de.lathanda.eos.base.event.CleanupListener;
import de.lathanda.eos.common.gui.MessageHandler;
import de.lathanda.eos.common.interpreter.AbstractMachine;
import de.lathanda.eos.common.interpreter.DebugInfo;
import de.lathanda.eos.common.interpreter.DebugListener;
import de.lathanda.eos.common.interpreter.DebugMulticaster;
import de.lathanda.eos.gui.diagram.MemoryEntry;

public class Machine implements AbstractMachine {
	private Memory memory;
	private ControlUnit cu;
	private ALU alu;
	
    private final Executer executer = new Executer();
    private final SpeedExecuter speedexecuter = new SpeedExecuter();
    private DebugInfo debugInfo = new DebugInfo();
    private volatile boolean running   = false;
    private volatile long delay = 5000;
    private final DebugMulticaster dmc = new DebugMulticaster();

    private final LinkedList<CleanupListener> gc = new LinkedList<>();

	
	public Machine() {
		memory = new Memory();
		alu = new ALU();
		cu = new ControlUnit(memory, alu);
		
	}
	public void singleOperation() {
		cu.next();
	}
	@Override
    public void run() {
        pause();
        new Thread(executer).start();
    }
	@Override
	public void skip() {
        pause();
        new Thread(speedexecuter).start();
	}
	@Override
	public void singleStep() {
        //stop continues execution
        pause();
        //execute a single step
        try {
            debugStep();
        } catch (Exception e) {
            MessageHandler.def.handle(e);
        }
	}
	private void debugStep() {
		if (cu.isFinished()) {
			stop();
		} else {
			cu.next();
		}
	}
	@Override
    public synchronized void pause() {
        if (running) {
            running = false;
            this.notifyAll();
        }
    }
	
	@Override
    public synchronized void stop() {
        //stop continues execution
        pause();
        //set machine back to start
        reset();
    }
	@Override
    public void setDelay(long delay) {
        this.delay = delay;
    }
	@Override
	public void reinit() {
		reset();
	}
	@Override
    public void addDebugListener(DebugListener dl) {
        dmc.add(dl);
    }
	@Override
    public void removeDebugListener(DebugListener dl) {
        dmc.remove(dl);
    }
	@Override
	public DebugInfo getDebugInfo() {
		return debugInfo;
	}
    private void reset() {
    	//TODO reset Control unit cu
    	
        //create new debug container
        debugInfo = new DebugInfo();
        //destroy all windows
        gc.forEach(w -> w.terminate());
        gc.clear();
        //inform debug listeners
        dmc.fireDebugPoint(debugInfo);
    }	
	@Override
	public void setBreakpoint(int linenumber, boolean b) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public LinkedList<MemoryEntry> getMemory() {
		// TODO Auto-generated method stub
		return null;
	}
    private class Executer implements Runnable {
        @Override
        public void run() {
            long laststep = System.nanoTime();
            running = true;
            try {
                while (running) {
                    int diff = (int)(laststep + delay - System.nanoTime());
                    if (diff > 1000000) {
                        synchronized (Machine.this) {
                            Machine.this.wait(diff/ 1000000);
                        }
                    }

                    laststep += delay;
                    synchronized (Machine.this) {
                        if (running) {
                            debugStep();
                        }
                    }

                }
            } catch (Exception e) {
                // thread was killed or execution ran into an error
                // either way generate message and terminate execution
                MessageHandler.def.handle(e);
                stop();
            }
        }
    }
    private class SpeedExecuter implements Runnable {
        @Override
        public void run() {
            running = true;
            try {
                while (running) {
                    synchronized (Machine.this) {
                        if (running) {
                            debugStep();
                        }
                    }
                }
            } catch (Exception e) {
                // thread was killed or execution ran into an error
                // either way generate message and terminate execution
            	MessageHandler.def.handle(e);
                stop();
            }
        }
    }
	
}
