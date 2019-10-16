package utn.tdm.meegos.domain;

public class Transaccion {

    int _id;

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
    String timestamp;

    public Transaccion() {
        this.requestId = "";
        this.requestName = "";
        this.responseType = "";
        this.errorCode = "";
        this.timestamp = "";
    }

    public Transaccion(int _id, String requestId, String requestName, String responseType, String errorCode, String timestamp) {
        this._id = _id;
        this.requestId = requestId;
        this.requestName = requestName;
        this.responseType = responseType;
        this.errorCode = errorCode;
        this.timestamp = timestamp;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Transaccion{" +
                "requestId='" + requestId + '\'' +
                ", requestName='" + requestName + '\'' +
                ", responseType='" + responseType + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
