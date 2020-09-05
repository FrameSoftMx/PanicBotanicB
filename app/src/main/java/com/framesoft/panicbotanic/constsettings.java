package com.framesoft.panicbotanic;

public class constsettings {
    private String nombre;
    private String descripcion;
    private boolean checked;

    public constsettings(String nombre, String descripcion) {
        this.nombre=nombre;
        this.descripcion=descripcion;
    }

    public constsettings(String nombre, String descripcion , boolean checked) {
        this.nombre=nombre;
        this.descripcion=descripcion;
        this.checked = checked;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isChecked()
    {
        return checked;
    }

    public void Checked(boolean checked)
    {
        this.checked= checked;
    }
}
