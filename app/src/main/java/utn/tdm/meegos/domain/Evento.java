package utn.tdm.meegos.domain;

import utn.tdm.meegos.domain.enumeration.OrigenEvento;
import utn.tdm.meegos.domain.enumeration.TipoEvento;

public class Evento {

    /**
     * ID con el que se identifica al evento.
     */
    private int id;

    /**
     * Los tipos de eventos son:
     * <ol>
     *     <li>LlamadaTelefonica</li>
     *     <li>SMS</li>
     *     <li>Chat</li>
     * </ol>
     */
    private TipoEvento tipo;

    /**
     * La fecha se representa en milisegundos.
     */
    private long fecha;

    /**
     * Id del contacto. _ID
     */
    private int contactoId;

    /**
     * Nombre completo del contacto para mostar en la lista.
     */
    private String contactoNombre;

    /**
     * Los origenes son:
     * <ol>
     *     <li>Entrante</li>
     *     <li>Saliente</li>
     * </ol>
     */
    private OrigenEvento origen;

    /**
     * Contenido del mensaje WEB. Chat.
     */
    private String smsWeb;

    public Evento(int id, TipoEvento tipo, long fecha, int contactoId, String contactoNombre, OrigenEvento origen, String smsWeb) {
        this.id = id;
        this.tipo = tipo;
        this.fecha = fecha;
        this.contactoId = contactoId;
        this.contactoNombre = contactoNombre;
        this.origen = origen;
        this.smsWeb = smsWeb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public void setTipo(TipoEvento tipo) {
        this.tipo = tipo;
    }

    public long getFecha() {
        return fecha;
    }

    public void setFecha(long fecha) {
        this.fecha = fecha;
    }

    public int getContactoId() {
        return contactoId;
    }

    public void setContactoId(int contactoId) {
        this.contactoId = contactoId;
    }

    public String getContactoNombre() {
        return contactoNombre;
    }

    public void setContactoNombre(String contactoNombre) {
        this.contactoNombre = contactoNombre;
    }

    public OrigenEvento getOrigen() {
        return origen;
    }

    public void setOrigen(OrigenEvento origen) {
        this.origen = origen;
    }

    public String getSmsWeb() {
        return smsWeb;
    }

    public void setSmsWeb(String smsWeb) {
        this.smsWeb = smsWeb;
    }
}
