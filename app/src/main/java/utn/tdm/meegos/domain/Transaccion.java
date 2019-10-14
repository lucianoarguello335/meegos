package utn.tdm.meegos.domain;

public class Transaccion {

    String requestId;

    /**
     * register-user | send-message | get-messages
     */
    String requestName;

    /**
     * succes | error
     */
    String responseType;

    String errorCode;

    /**
     * La fecha se representa en milisegundos.
     */
    long fecha;

    public Transaccion() {
        this.requestId = "";
        this.requestName = "";
        this.responseType = "";
        this.errorCode = "";
        this.fecha = 0;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
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
                "requestId='" + requestId + '\'' +
                ", requestName='" + requestName + '\'' +
                ", responseType='" + responseType + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}
