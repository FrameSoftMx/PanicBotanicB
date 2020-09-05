package com.framesoft.panicbotanic;

public class Contacto {
    private String nombre;
    private String tel;
    private boolean checked;

    public Contacto(String nombre, String tel ) {
        this.nombre=nombre;
        this.tel=tel;
    }

    public Contacto(String nombre, String tel , boolean checked) {
        this.nombre=nombre;
        this.tel=tel;
        this.checked = checked;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTel() {
        return tel;
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
