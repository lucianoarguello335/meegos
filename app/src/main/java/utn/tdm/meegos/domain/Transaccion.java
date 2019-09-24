package utn.tdm.meegos.domain;

public class Transaccion {
    String id;
    String requestName;
    String responseType;
    long fecha;

    public Transaccion(String id, String requestName, String responseType, long fecha) {
        this.id = id;
        this.requestName = requestName;
        this.responseType = responseType;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestName() {
        return requestName;
    }

    public void setRequestName(String requestName) {
        this.requestName = requestName;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Transaccion{" +
                "id='" + id + '\'' +
                ", requestName='" + requestName + '\'' +
                ", responseType='" + responseType + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}
