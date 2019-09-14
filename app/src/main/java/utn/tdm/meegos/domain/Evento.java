package utn.tdm.meegos.domain;

public class Evento {

    public static final int LLAMADA = 1;
    public static final int SMS = 2;
    public static final int CHAT = 3;

    public static final int ENTRANTE = 1;
    public static final int SALIENTE = 2;

    /**
     * ID con el que se identifica al evento.
     */
    private long id;

    /**
     * Los tipos de eventos son:
     * <ol>
     *     <li>LlamadaTelefonica</li>
     *     <li>SMS</li>
     *     <li>Chat</li>
     * </ol>
     */
    private int tipo;

    /**
     * La fecha se representa en milisegundos.
     */
    private long fecha;

    /**
     * Id del contacto. _ID
     */
    private long contactoId;

    /**
     * Lookup key del contacto. LOOKUP_KEY
     */
    private String contactoLookup;

    /**
     * Nombre completo del contacto para mostar en la lista.
     */
    private String contactoNombre;

    /**
     * Numero con el cual el contacto realizo el evento.
     */
    private String contactoNumero;

    /**
     * Los origenes son:
     * <ol>
     *     <li>Entrante</li>
     *     <li>Saliente</li>
     * </ol>
     */
    private int origen;

    /**
     * Contenido del mensaje WEB. Chat.
     */
    private String smsWeb;

    public Evento(long id, int tipo, long fecha, long contactoId, String contactoLookup,
                  String contactoNombre, String contactoNumero, int origen, String smsWeb) {
        this.id = id;
        this.tipo = tipo;
        this.fecha = fecha;
        this.contactoId = contactoId;
        this.contactoLookup = contactoLookup;
        this.contactoNombre = contactoNombre;
        this.contactoNumero = contactoNumero;
        this.origen = origen;
        this.smsWeb = smsWeb;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContactoLookup() { return contactoLookup; }

    public void setContactoLookup(String contactoLookup) { this.contactoLookup = contactoLookup; }

    public int getTipo() { return tipo; }

    public void setTipo(int tipo) { this.tipo = tipo; }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public long getContactoId() {
        return contactoId;
    }

    public void setContactoId(long contactoId) {
        this.contactoId = contactoId;
    }

    public String getContactoNombre() {
        return contactoNombre;
    }

    public void setContactoNombre(String contactoNombre) {
        this.contactoNombre = contactoNombre;
    }

    public String getContactoNumero() { return contactoNumero; }

    public void setContactoNumero(String contactoNumero) { this.contactoNumero = contactoNumero; }

    public int getOrigen() { return origen; }

    public void setOrigen(int origen) { this.origen = origen; }

    public String getSmsWeb() {
        return smsWeb;
    }

    public void setSmsWeb(String smsWeb) {
        this.smsWeb = smsWeb;
    }
}
