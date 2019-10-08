package utn.tdm.meegos.domain;

import android.graphics.Bitmap;

public class Contacto {

    private Long id;
    private String lookupKey;
    private String nombre;
    private String apellido;
    private String alias;
    private Bitmap photoThumbnail;

    public Contacto(Long id, String lookupKey, String nombre, String apellido, String alias, Bitmap photoThumbnail) {
        this.id = id;
        this.lookupKey = lookupKey;
        this.nombre = nombre;
        this.apellido = apellido;
        this.alias = alias;
        this.photoThumbnail = photoThumbnail;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLookupKey() {
        return lookupKey;
    }

    public void setLookupKey(String lookupKey) {
        this.lookupKey = lookupKey;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Bitmap getPhotoThumbnail() {
        return photoThumbnail;
    }

    public void setPhotoThumbnail(Bitmap photoThumbnail) {
        this.photoThumbnail = photoThumbnail;
    }
}
