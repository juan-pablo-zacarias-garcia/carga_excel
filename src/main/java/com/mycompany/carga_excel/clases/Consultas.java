/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carga_excel.clases;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author ELIZABETH
 */
public class Consultas {
    
    public Consultas(){
        
    }
    
    
    
    public String columns = "(" +
"MES1 ,SEMANA1 ,TABLA1 ,PRODUCTO1 ,CODIGO1," +
"HECTAREAS1 ,RANCHO1 ,OBSERVACIONES1 ,REND_KG_X_HA1 ," +
"KGS_TOTALES1 ,B1 ,PLANTULA1 ,vacia1 ,AGROQUIMICOS1 ," +
"vacia2 ,FERTILIZANTES1 ,vacia3 ,MANO_DE_OBRA1 ,vacia4 ," +
"FLETES1 ,vacia5 ,RENTA1 ,vacia6 ,MAQUILA1 ,EMPAQUE1 ," +
"C1 ,TOTAL_DIRECTOS1 ,TOTAL_INDIRECTOS1 ,TOTAL_COSTO1 ,COSTO_X_HA1 ," +
"D1 ,COSTO_DE_EMPAQUE1 ,NO_CAJAS1 ,E1 ,MANO_DE_OBRA2 ,vacia7 ," +
"FLETES2 ,vacia8 ,MAQUILA2  ,vacia9 ,EMPAQUE2 ,vacia10 ,TOTAL_COSTO_EMPAQUE1 ," +
"F1 ,CAJAS_MERMADAS1 ,NO_CAJAS2 ,G1 ,MANO_DE_OBRA3 ,FLETES3 ,MAQUILA3 ," +
"EMPAQUE3 ,TOTAL_MERMAS1 ,COSTO_TOTAL1  ,H1 ,VENTAS_EXPOR1 ,VENTAS_EMPAQUE1 ,\n" +
"VENTAS_TAYLOR1 ,VENTAS_FRESH_EXPRESS1 ,VENTAS_GRANEL1 ,VENTAS_RANCHO_VIEJO1 ,VENTAS_ROYAL_ROSE1 ," +
"VENTAS_AVALON1 ,COM_MONTERREY1 ,COM_GUADALAJARA1 ,VENTAS_ESA_FRESH1 ,OTROS_CLIENTES1 ," +
"VENTAS_TOTALES1 ,I1 ,UTILIDAD_O_PERDIDA1)";
    public String tablas_bd = "Select name from sysobjects where type=\'U\';";
    
    public String headers_table_db = "SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'tablas';";
    public String getHeadersTable(String table){
        
        return null;
    }
    
}
