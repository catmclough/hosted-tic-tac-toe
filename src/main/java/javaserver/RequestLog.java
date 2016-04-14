package javaserver;

import java.util.ArrayList;

public class RequestLog {
    private ArrayList<String> log;

    public RequestLog() {
        this.log = new ArrayList<String>();
    }

    public void addRequest(String request) {
        log.add(request);
    }

    public String getLogContents() {
       return String.join(System.lineSeparator(), log);
    }

    public void clearLog() {
        log.clear();
    }
}
