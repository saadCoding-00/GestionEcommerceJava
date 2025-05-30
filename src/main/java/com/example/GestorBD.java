package com.example;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class GestorBD {
    private Connection conn;
    private String[] columnNames;

    public boolean connect() {
        try {
            String url = "jdbc:mysql://localhost:3306/gestion_contactos";
            String user = "root";
            String password = "root";
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión a la base de datos exitosa.");
            return true;
        } catch (SQLException e) {
            System.err.println("Error al conectar a la base de datos: " + e.getMessage());
            return false;
        }
    }

        public void agregarContacto(String nombre, String apellidos, String telefonoMovil, String telefonoFijo, String email, String direccion, String fechaNacimiento, String notas) throws SQLException {
        String query = "INSERT INTO contactos (nombre, apellidos, telefono_movil, telefono_fijo, email, direccion, fecha_nacimiento, notas) VALUES ('" +
                       nombre + "', '" + apellidos + "', '" + telefonoMovil + "', '" + telefonoFijo + "', '" + email + "', '" +
                       direccion + "', '" + fechaNacimiento + "', '" + notas + "')";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
        }
    }

    public Object[][] listarContactos() throws SQLException {
        String query = "SELECT id, nombre, apellidos, telefono_movil, telefono_fijo, email, direccion, fecha_nacimiento, notas FROM contactos";
        return executeQuery(query);
    }

    public Object[] obtenerContactoPorId(int id) throws SQLException {
        String query = "SELECT id, nombre, apellidos, telefono_movil, telefono_fijo, email, direccion, fecha_nacimiento, notas FROM contactos WHERE id = " + id;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return new Object[]{
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        rs.getString("telefono_movil"),
                        rs.getString("telefono_fijo"),
                        rs.getString("email"),
                        rs.getString("direccion"),
                        rs.getString("fecha_nacimiento"),
                        rs.getString("notas")
                };
            }
            return null;
        }
    }

    public void modificarContacto(int id, String nombre, String apellidos, String telefonoMovil, String telefonoFijo, String email, String direccion, String fechaNacimiento, String notas) throws SQLException {
       
        String query = "UPDATE contactos SET nombre = '" + nombre + "', apellidos = '" + apellidos + "', telefono_movil = '" +
                       telefonoMovil + "', telefono_fijo = '" + telefonoFijo + "', email = '" + email + "', direccion = '" +
                       direccion + "', fecha_nacimiento = '" + fechaNacimiento + "', notas = '" + notas + "' WHERE id = " + id;
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(query);
        }
    }

    public boolean eliminarContacto(int id) throws SQLException {
        
        String query = "DELETE FROM contactos WHERE id = " + id;
        try (Statement stmt = conn.createStatement()) {
            int affectedRows = stmt.executeUpdate(query);
            return affectedRows > 0;
        }
    }

    public Object[][] buscarContactos(String criterio) throws SQLException {
        String query = "SELECT id, nombre, apellidos, telefono_movil, telefono_fijo, email, direccion, fecha_nacimiento, notas FROM contactos WHERE nombre LIKE '%" + criterio + "%' OR apellidos LIKE '%" + criterio + "%'";
        return executeQuery(query);
    }

    
    private Object[][] executeQuery(String query) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

           
            if (columnNames == null) {
                columnNames = new String[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    columnNames[i - 1] = metaData.getColumnName(i);
                }
            }

            List<Object[]> rows = new ArrayList<>();
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                rows.add(row);
            }

            Object[][] data = new Object[rows.size()][columnCount];
            for (int i = 0; i < rows.size(); i++) {
                data[i] = rows.get(i);
            }
            return data;
        }
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void disconnect() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Conexión a la base de datos cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
