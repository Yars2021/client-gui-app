package ru.itmo.p3114.s312198.io;

import java.util.LinkedList;

public class GUIReaderAdapter extends ConsoleReader {
    private final LinkedList<String> buffer = new LinkedList<>();

    private String getFirst() {
        String line = "";
        if (!buffer.isEmpty()) {
            line = buffer.getFirst();
            buffer.removeFirst();
        }
        return line;
    }

    public void push(String line) {
        buffer.addLast(line);
    }

    @Override
    public String readLine() {
        return getFirst();
    }

    @Override
    public String readConsoleLine() {
        return getFirst();
    }

    @Override
    public String flexibleConsoleReadLine() {
        return getFirst();
    }

    @Override
    public String readConsolePassword() {
        return getFirst();
    }

    @Override
    public String flexibleConsoleReadPassword() {
        return getFirst();
    }

    @Override
    public void close() {
        buffer.clear();
    }
}
