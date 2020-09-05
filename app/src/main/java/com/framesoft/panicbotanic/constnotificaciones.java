package com.framesoft.panicbotanic;

public class constnotificaciones {

    private String fecha;
    private String titulo;
    private String descripcion;
    private Boolean visto;


    public constnotificaciones( String titulo,String descripcion,Boolean visto) {
        this.titulo=titulo;
        this.descripcion=descripcion;
        this.visto = visto;

    }

    public constnotificaciones(String titulo,String descripcion,String fecha ,Boolean visto) {

        this.titulo=titulo;
        this.descripcion=descripcion;
        this.fecha=fecha;
        this.visto = visto;

    }
    public constnotificaciones() {

    }
    public String  getFecha(){
        return fecha;
    }
    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Boolean getVisto() {
        return visto;
    }

}
