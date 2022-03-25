package com.example.demo_fy;

import java.io.File;

public class Session {
    private String name;
    private File recording;

    public Session(String name, File recording) {
        this.name = name;
        this.recording = recording;
    }

    public File getRecording() {
        return recording;
    }

    public void setRecording(File recording) {
        this.recording = recording;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
