/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carga_excel.clases;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

/**
 *
 * @author ELIZABETH
 */
public class Consultas {
    
    public Consultas(){
        
    }
    public String tablas_bd = "Select name from sysobjects where type=\'U\';";
    

    public String getHeadersTable(String table){
        String headers_table_db = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = '"+table+"';";
        String columns="(";
        try{
            Conexion_bd obj_bd = new Conexion_bd();
            Connection  conn = obj_bd.conectar("");
            
            ResultSet headers = obj_bd.query(headers_table_db, conn);
            
            while (headers.next()) {
                String item = headers.getString("COLUMN_NAME");
                columns=columns+item+",";
            }
            columns = columns.substring(0, columns.length() - 1)+")";
            //Cerramos la conexión
            conn.close();
            return columns;
        }catch(Exception e){
            System.out.println("Consultas/getHeadersTable/ "+e);
            return null;
        }
        
    }
    public String [] getTypesColumns(String tablaBD){
        String query = "Select  DATA_TYPE as type from information_schema.columns WHERE TABLE_NAME='"+tablaBD+"'";
        ArrayList<String> types = new ArrayList<String>();
        try{
            Conexion_bd obj_bd = new Conexion_bd();
            Connection  conn = obj_bd.conectar("");
            
            ResultSet rs = obj_bd.query(query, conn);
            
            while (rs.next()) {
                String item = rs.getString("type");
                types.add(item);
            }
            //Cerramos la conexión
            conn.close();
            return types.toArray(new String[types.size()]);
        }catch(Exception e){
            System.out.println("Consultas/getTypesColumns/ "+e);
            return null;
        }
    }
    
}
