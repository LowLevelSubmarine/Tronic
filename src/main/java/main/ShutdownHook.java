package main;

public class ShutdownHook extends Thread {

    @Override
    public void run() {
        Main.shutdown();
    }

}
