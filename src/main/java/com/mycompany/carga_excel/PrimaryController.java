package com.mycompany.carga_excel;

import java.io.IOException;
import javafx.fxml.FXML;
import com.mycompany.carga_excel.conexion_bd;
import com.mycompany.carga_excel.ReadExcel;
import java.sql.*;
import javafx.scene.control.Label;

public class PrimaryController {

    @FXML
    void conectar(){
        conexion_bd conn = new conexion_bd();
        conn.conectar();
    }
    @FXML
    void leer_excel(){
        ReadExcel obj1 = new ReadExcel();
        obj1.leer();
    }
}
