package utn.tdm.meegos.domain;

public class Chat {

    public static final int ENVIADO = 1;
    public static final int RECIBIDO = 2;

    private int _id;
    private String timestamp;
    private String from;
    private String to;
    private int origen;
    private String message;

    public Chat(int id, String timestamp, String from, String to, int origen, String message) {
        this._id = id;
        this.timestamp = timestamp;
        this.from = from;
        this.to = to;
        this.origen = origen;
        this.message = message;
    }

    public Chat(String timestamp, String from, String to, int origen, String message) {
        this._id = -1;
        this.timestamp = timestamp;
        this.from = from;
        this.to = to;
        this.origen = origen;
        this.message = message;
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
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

    public int getOrigen() {
        return origen;
    }

    public void setOrigen(int origen) {
        this.origen = origen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
