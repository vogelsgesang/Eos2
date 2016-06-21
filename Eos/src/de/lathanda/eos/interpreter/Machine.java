package de.lathanda.eos.interpreter;

import de.lathanda.eos.base.event.CleanupListener;
import de.lathanda.eos.gui.MessageHandler;
import de.lathanda.eos.interpreter.commands.DebugPoint;
import de.lathanda.eos.interpreter.exceptions.MemoryAccessViolation;
import de.lathanda.eos.interpreter.exceptions.ProgramTerminationException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map.Entry;
import java.util.TreeMap;

/**
 * Virtuelle Maschine zur Simulation eines Stack basierenden Assemblers.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.3
 */
public class Machine {

    private final LinkedList<Object> stack;
    private final LinkedList<Context> callstack;
    /**
     * Aktiver Kontext (Aktueller Bereich)
     */
    private Context context;
    /**
     * Globaler Kontext. Außerhalb von Funtionen ist dieser mit dem aktuellen Kontext identisch.
     */
    private Context global;
    private final TreeMap<String, UserFunction> userfunction;
    private final Executer executer = new Executer();
    private final SpeedExecuter speedexecuter = new SpeedExecuter();
    private DebugInfo debugInfo = new DebugInfo();
    private volatile boolean executing = false;
    private volatile boolean running   = false;
    private volatile boolean isFinished = false;
    private volatile long delay = 5000;
    private final DebugMulticaster dmc = new DebugMulticaster();

    private final LinkedList<CleanupListener> gc = new LinkedList<>();
    private TreeMap<Integer, DebugPoint> breakpoints = new TreeMap<>();
    
    public Machine() {
        stack = new LinkedList<>();
        callstack = new LinkedList<>();
        context = new Context();
        global = context;
        userfunction = new TreeMap<>();
    }

    public void addDebugListener(DebugListener dl) {
        dmc.add(dl);
    }

    public void removeDebugListener(DebugListener dl) {
        dmc.remove(dl);
    }
    public void setBreakpoint(int linenumber, boolean active) {
    	DebugPoint debug = breakpoints.get(linenumber);
    	if (debug != null) {
    		debug.setActiveBreakpoint(active);
    	}
    }
    public void addPossibleBreakpoint(DebugPoint debugPoint, int linenumber) {
    	if (!breakpoints.containsKey(linenumber)) {
    		breakpoints.put(linenumber, debugPoint);
    	}
    }
    public boolean hasBreakpoint(int linenumber) {
    	DebugPoint debug = breakpoints.get(linenumber);
    	if (debug != null) {
    		return debug.isActiveBreakpoint();
    	} else {
    		return false;
    	}
    }

    public void reinit() {
        userfunction.clear();
        global = new Context();
        reset();
    }

    private void reset() {
        isFinished = false;
        //clear runtime informations
        stack.clear();
        //clear memory
        callstack.clear();
        //restore starting memory
        context = global;
        global.reset();
        //create new debug container
        debugInfo = new DebugInfo();
        //destroy all windows
        gc.forEach(w -> w.terminate());
        gc.clear();
        //inform debug listeners
        dmc.fireDebugPoint(debugInfo);
    }

    public Object pop() {
    	if (stack.isEmpty()) {
    		stop();
    		throw new ProgramTerminationException();
    	}
        return stack.pop();
    }

    public void push(Object obj) {
        stack.push(obj);
    }

    public Object peek() {
        return stack.peek();
    }

    public Object get(String variable) {
        if (context.memory.containsKey(variable)) {
            return context.memory.get(variable).get();
        } else if (context.globalAccess && global.memory.containsKey(variable)) {
            return global.memory.get(variable).get();
        } else {
            throw new MemoryAccessViolation(variable);
        }
    }

    public void set(String variable, Object data) {
        if (context.memory.containsKey(variable)) {
            context.memory.get(variable).set(data);
        } else if (context.globalAccess && global.memory.containsKey(variable)) {
            global.memory.get(variable).set(data);
        } else {
            throw new MemoryAccessViolation(variable);
        }
    }
    public TreeMap<String, Variable> getMemoryDump() {
    	TreeMap<String, Variable> result = new TreeMap<>();	
    	for (Entry<String, Variable> entry : global.memory.entrySet()) {
    		result.put(entry.getKey(), entry.getValue());
    	}
    	if (context != global) {
    		for (Entry<String, Variable> entry : context.memory.entrySet()) {
    			result.put(entry.getKey(), entry.getValue());
    		}    	
    	}
    	return result;   	
    }
    public void jump(int relativ) {
        context.index += relativ;
    }

    public void create(String variable, Type type) throws Exception {    	
        Variable v = new Variable(type, variable);
        if (!type.isAbstract()) {
        	v.set(type.newInstance());
        	if (v.get() instanceof CleanupListener) {
        		gc.add((CleanupListener)v.get());
        	}
        }
        context.memory.put(variable, v);
    }

    public void create(String variable, Type type, Object data) throws Exception {
        Variable v = new Variable(type, variable);
        v.set(data);
        context.memory.put(variable, v);
    }

    private boolean microStep() throws Exception {
        Context actcontext = context;
        try {
        	if (actcontext.program[actcontext.index].execute(this)) {
        		actcontext.index++;
        	}
    	} catch (Exception e) {
    		MessageHandler.def.handleOrThrowException(e);
    		actcontext.index++;
    	}
        return update();
    }

    private boolean update() {
        //remark - operations can create a sub context
        while (context.program.length <= context.index) {
            if (callstack.size() == 0) {
                isFinished = true;
                return true;
            } else {
                context = callstack.pop();
            }
        }
        return false;
    }
    /**
     * called by interpreter if debug point is reached
     * @param codeRange 
     */
    public void debugStop(Marker codeRange) {
        executing = false;
        debugInfo.setCodeRange(codeRange);
        dmc.fireDebugPoint(debugInfo);
    }
    /**
     * Returns actual debugInfo
     * @return
     */
    public DebugInfo getDebugInfo() {
		return debugInfo;
	}

	/**
     * executes a single statement/expression
     */
    public synchronized void singleStep() {
        //stop continues execution
        pause();
        //execute a single step
        try {
            debugStep();
        } catch (Exception e) {
            MessageHandler.def.handle(e);
        }
    }
    public synchronized void skip() {
        pause();
        new Thread(speedexecuter).start();
    }

    private void debugStep() throws Exception {
        if (isFinished) {
            stop();
        }
        executing = true;
        running = true;
        while (executing && running) {
       		if (microStep()) {
       			running = false;
       		}
        }
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }

    public synchronized void stop() {
        //stop continues execution
        pause();
        //set machine back to start
        reset();
    }

    public void run() {
        pause();
        new Thread(executer).start();
    }

    public synchronized void pause() {
        if (running) {
            running = false;
            this.notifyAll();
        }
    }

    public void call(MethodType mt, Object[] args) {
        callstack.push(context);
        context = new Context();
        UserFunction uf = userfunction.get(mt.signature);
        context.program = uf.program;
        context.globalAccess = uf.globalAccess;
        context.index = 0;
        Type[] parameterTypes = mt.parameters;
        for (int i = 0; i < parameterTypes.length; i++) {
            Variable v = new Variable(parameterTypes[i], uf.paramters[i]);
            v.set(args[i]);
            context.memory.put(uf.paramters[i], v);
        }
    }

    public void createUserFunction(String signature, String[] parameters, ArrayList<Command> ops, boolean globalAccess) {
        UserFunction uf = new UserFunction();
    	for(Command command : ops) {
    		command.prepare(this);
    	}
        uf.program = new Command[ops.size()];
        uf.program = ops.toArray(uf.program);
        uf.paramters = parameters;
        uf.globalAccess = globalAccess;
        userfunction.put(signature, uf);
    }

    public void setProgram(ArrayList<Command> ops) {
    	for(Command command : ops) {
    		command.prepare(this);
    	}
        context.program = new Command[ops.size()];
        context.program = ops.toArray(context.program);
        context.index = 0;
    }

    private class Context {

        public int index = 0;
        public Command[] program;
        public boolean globalAccess;
        public TreeMap<String, Variable> memory = new TreeMap<>();

        public void reset() {
            index = 0;
            memory.clear();
        }
    }

    private class UserFunction {

        public String[] paramters;
        public Command[] program;
        public boolean globalAccess;
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
    protected static class DebugMulticaster {

        private final LinkedList<DebugListener> debugListener;

        protected DebugMulticaster() {
            debugListener = new LinkedList<>();
        }

        void add(DebugListener cl) {
            debugListener.add(cl);
        }

        void remove(DebugListener cl) {
            debugListener.remove(cl);
        }

        void fireDebugPoint(DebugInfo debug) {
            debugListener.forEach((dl) -> {
                dl.debugPointReached(debug);
            });
        }
    }
}
