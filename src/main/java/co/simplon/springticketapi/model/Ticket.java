package co.simplon.springticketapi.model;

import java.time.LocalDateTime;

public class Ticket {
    private Long id;
    private LocalDateTime date;
    private String description;
    private Long learnerIdx;
    private boolean isSolved;

    public Ticket(Long id, LocalDateTime date, String description, Long learnerIdx, boolean isSolved) {
        this.id = id;
        this.date = date;
        this.description = description;
        this.learnerIdx = learnerIdx;
        this.isSolved = isSolved;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public Long getLearnerIdx() {
        return learnerIdx;
    }

    public boolean isSolved() {
        return isSolved;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
