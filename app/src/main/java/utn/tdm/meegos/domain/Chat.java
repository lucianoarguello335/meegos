package utn.tdm.meegos.domain;

public class Chat {
    private long id;
    private long timestamp;
    private String from;
    private String to;
    private String message;

    public Chat(long id, long timestamp, String from, String to, String message) {
        this.id = id;
        this.timestamp = timestamp;
        this.from = from;
        this.to = to;
        this.message = message;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
