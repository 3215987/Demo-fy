package com.example.demo_fy;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Piece implements Serializable {
    private String name;
    private Date time;
    private ArrayList<Session> sessions;

    /**
     * Constructor for piece with name passed in
     * @param n name of piece
     */
    public Piece(String n) {
        this.name = n;
        this.time = Calendar.getInstance().getTime();
        this.sessions = new ArrayList<Session>();
    }

    /**
     * Default constructor for Piece
     */
    public Piece() {
        this("Untitled Piece");
    }

    /**
     * getter for name
     * @return the name of the piece
     */
    public String getName() {
        return name;
    }

    /**
     * getter for time
     * @return returns the time the piece was created
     */
    public Date getTime() {
        return time;
    }

    /**
     * getter for recording sessions of the piece
     * @return returns list of sessions
     */
    public ArrayList<Session> getSessions() {
        return sessions;
    }

    /**
     * setter for name
     * @param name the piece's new name
     */
    public void setName(String name) {
        this.name = name;
    }



    public void addSession(Session s) {
        this.sessions.add(s);
    }
}
