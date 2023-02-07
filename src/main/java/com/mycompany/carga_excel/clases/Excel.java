/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.carga_excel.clases;

/**
 *
 * @author Juan Pablo Zacarias
 */
import java.io.File;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import static org.apache.poi.ss.usermodel.CellType.BLANK;
import static org.apache.poi.ss.usermodel.CellType.BOOLEAN;
import static org.apache.poi.ss.usermodel.CellType.ERROR;
import static org.apache.poi.ss.usermodel.CellType.FORMULA;
import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;
import static org.apache.poi.ss.usermodel.CellType._NONE;
import org.apache.poi.ss.usermodel.DateUtil;

public class Excel {

    //instancia del objeto workbook para archivos xlsx, es el libro con el que se trabajará
    private XSSFWorkbook wb;
    //La variable items almacenará los datos de las celdas de excel
    public ArrayList<String> datos;
    //Guarda el número de columnas de la tabla
    public int no_columns = 0;
    public int no_rows = 0;
    //Guarda la fila de ancabezados
    public ArrayList<String> columns = new ArrayList<String>();

    //formato de decimales
    DecimalFormat df = new DecimalFormat("#.00");

    public void leer(File file, int no_sheet, int no_columns, int fila_encabezados) {
        try {
            //instancia del objeto workbook para archivos xlsx
            wb = new XSSFWorkbook(file);
            //instancia del objeto sheet para archivos xlsx
            XSSFSheet sheet = wb.getSheetAt(no_sheet);
            //arraylist para guardar los datos de las celdas de excel
            ArrayList<String> items = new ArrayList<String>();

            //recorre las filas de la hoja de excel
            Row row = sheet.getRow(fila_encabezados);
            while (row != null) {
                for (int i = 0; i < no_columns; i++) {
                    Cell cell = row.getCell(i);
                    //almacena el contenido de la celda en el arraylist
                    items.add(getStringFromCell(cell));
                }
                //aumenta el index de las filas
                fila_encabezados++;
                this.no_rows++;
                //actualiza para leer la nueva fila
                row = sheet.getRow(fila_encabezados);
            }

            //Los datos de las celdas de la hoja de excel están en el arraylist items, incluyendo los encabezados
            this.datos = items;
            for (int i = 0; i < no_columns; i++) {
                this.columns.add(this.datos.get(i));
            }
            this.no_columns = no_columns;

        } catch (Exception e) {
            System.out.println("Error al leer archivo " + e);
        }
    }

    //Método para exportar los datos del Arraylist items a la tabla de la base de datos
    public void exportar_datos(ArrayList items, int no_columns, ArrayList<String> columns, String tablaBD) {
        //inicio la conexion para cargar los datos;
        Conexion_bd obj_bd = new Conexion_bd();
        //creamos objeto de Consultas
        Consultas querys = new Consultas();
        //Extraemos los nombres de las columnas
        String cols = "";
        for (int i = 0; i < columns.size(); i++) {
            cols = cols + columns.get(i) + ",";
        }
        //terminamos de dar formato a las columnas de la consulta
        cols = "(" + cols.substring(0, cols.length() - 1) + ")";

        String query = "INSERT INTO " + tablaBD + " "
                + querys.columns;
        //se crea una conexion
        Connection conn = obj_bd.conectar("");
        //index para extraer elementos del arraylist con las celdas de excel
        //incia en no_columns para omitir la fila de los títulos
        int index_item = no_columns;
        //almacena los valores de cada fila
        String values;
        //número de filas del documento menos la fila de encabezados
        int no_filas = (items.size() / no_columns) - 1;
        //datos insertados correctamente
        int datos_insertados = 0;
        //formamos la cadena de VALUES
        if (conn != null) {
            for (int i = 0; i < no_filas; i++) {
                values = "";
                //se extraen los elementos del arraylist dependiendo el número de columnas y se les da formato
                for (int j = 0; j < no_columns; j++) {
                    if (index_item < items.size()) {
                        values = values + "\'" + items.get(index_item) + "\',";
                        //index del array list
                        index_item++;
                    }
                }
                //se quita la última coma a values
                values = values.substring(0, values.length() - 1);
                //se realiza la consulta sql
                ResultSet res = obj_bd.insert(query + " VALUES(" + values + ")", conn);
                if (res != null) {
                    datos_insertados++;
                }
            }
            //Alerta sobre datos cargados
            alert("Datos exportados", "Se han cargado " + datos_insertados + " de " + no_filas
                    + " registros en la base de datos", Alert.AlertType.INFORMATION);
            //se reinicia el index del array list
            index_item = no_columns;
            //Cierra a conexión
            obj_bd.cerrar_conexion(conn);
        } else {

        }

    }

    //Método para obtener las hojas disponibles del archivo de Excel
    public ArrayList<String> getSheets() {
        ArrayList<String> sheetNames = new ArrayList<String>();
        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
            sheetNames.add(wb.getSheetName(i));
        }
        return sheetNames;
    }

    //Método para devolver el valor contenido en la columna en string
    String getStringFromCell(Cell cell) {

        String cont_cell = "";
        if (cell != null) {
            switch (cell.getCellType()) {
                case NUMERIC:
                    //si el número corresponde a una fecha entonces le da formato de fecha
                    if (DateUtil.isCellDateFormatted(cell)) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        String s = sdf.format(cell.getDateCellValue());
                        cont_cell = s;
                    } else {
                        cell.setCellType(STRING);
                        String aux = cell.getStringCellValue();
                        //Lo convertimos a número para formatearlo con dos decimales
                        double num_f = Double.parseDouble(aux);
                        aux = df.format(num_f);
                        //Se guarda el dato formateado a 2 decimales
                        cont_cell = (aux);
                    }
                    break;
                case STRING:
                    cont_cell = (cell.getStringCellValue());
                    break;
                case BOOLEAN:
                    cont_cell = (String.valueOf(cell.getBooleanCellValue()));
                    break;
                case FORMULA:
                    switch (cell.getCachedFormulaResultType()) {
                        case NUMERIC:
                            cont_cell = (String.valueOf(df.format(cell.getNumericCellValue())));
                            break;
                        case STRING:
                            cont_cell = (cell.getStringCellValue());
                            break;
                    }
                    break;
                case ERROR:
                    cont_cell = ("vacia");
                    break;
                case BLANK:
                    cont_cell = ("vacia");
                    break;
                case _NONE:
                    cont_cell = ("vacia");
                    break;
                default:
                    cont_cell = ("vacia");
                    break;
            }
        } else {
            cont_cell = ("vacia");
        }
        return cont_cell;
    }

    //Método para mostrar alertas
    void alert(String title, String msg, Alert.AlertType type) {
        Alert alerta_archivo = new Alert(type);
        alerta_archivo.setTitle(title);
        alerta_archivo.setContentText(msg);
        alerta_archivo.showAndWait();
    }

}
