package com.tronic.bot.tools;

import java.util.LinkedList;
import java.util.List;

public class BatchProcessor<F, T> {

    private final LinkedList<Process> values = new LinkedList<>();
    private final boolean filterNulls;

    public BatchProcessor(List<F> values, TransformTask<F, T> transformTask, boolean filterNulls) {
        this.filterNulls = filterNulls;
        for (F value : values) {
            Process process = new Process(value, transformTask);
            this.values.add(process);
            process.start();
        }
    }

    public LinkedList<T> process() {
        LinkedList<T> values = new LinkedList<>();
        try {
            for (Process process : this.values) {
                process.join();
                if (!this.filterNulls || process.to != null) values.add(process.to);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return values;
    }

    private class Process extends Thread {

        private final F from;
        private final TransformTask<F, T> transformTask;
        private T to;

        Process(F from, TransformTask<F, T> transformTask) {
            this.from = from;
            this.transformTask = transformTask;
        }

        @Override
        public void run() {
            this.to = transformTask.run(this.from);
        }

    }

    public interface TransformTask<F, T> {
        T run(F from);
    }

}
