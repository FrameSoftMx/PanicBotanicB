package com.framesoft.panicbotanic;

public class constusuarios {

    private String nombre;
    private String contrasena;
    private String confcont;
    private String celular;
    private String email;
    private String token;
    public constusuarios() {

    }
    public constusuarios(  String token) {
        this.token = token;
    }
    public constusuarios( String nombre,String contrasena, String confcont, String celular,String email) {
        this.nombre=nombre;
        this.contrasena = contrasena;
        this.confcont = confcont;
        this.celular=celular;
        this.email = email;

    }



    public String getNombre() {
        return nombre;
    }

    public String getContrasena() {
        return contrasena;
    }
    public String getConfcont() {
        return confcont;
    }

    public String getCelular() {
        return celular;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }

}
