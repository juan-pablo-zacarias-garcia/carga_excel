/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carga_excel.clases;

/**
 *
 * @author ELIZABETH
 */
public class DatosBD {
    public String host;
    public String instance;
    public String database;
    public String user;
    public String password;
    DatosBD(String host, String instance, String database, String user, String password){
        this.host=host;
        this.instance = instance;
        this.database = database;
        this.user = user;
        this.password = password;
    }
}
