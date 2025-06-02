package com.example;

import java.sql.*;
import java.util.*;
import java.util.List;

public class GestorBD {
    private Connection conn;
    private String[] columnNames;

    public GestorBD() {
        connect();
    }

    private boolean connect() {
        try {
            String url = "jdbc:mysql://localhost:3306/gestion_ecommerce";
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
/////////////////
//PRODUCTOS//
/////////////////
    public void agregarProducto(String nombre, String descripcion, double precio, int categoria_id, int stock) throws SQLException {
        String query = "INSERT INTO productos (nombre, descripcion, precio, categoria_id, stock) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setDouble(3, precio);
            stmt.setInt(4, categoria_id);
            stmt.setInt(5, stock);
            stmt.executeUpdate();
        }
    }

    public Object[][] listarProductos() throws SQLException {
        String query = "SELECT id, nombre, descripcion, precio, categoria_id, stock FROM productos";
        return executeQuery(query);
    }

    public Object[] obtenerProductoPorId(int id) throws SQLException {
        String query = "SELECT id, nombre, descripcion, precio, categoria_id, stock FROM productos WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Object[]{
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio"),
                        rs.getInt("categoria_id"),
                        rs.getInt("stock")
                    };
                }
                return null;
            }
        }
    }
    

    public void modificarProducto(int id, String nombre, String descripcion, double precio, int categoria_id, int stock) throws SQLException {
        String query = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, categoria_id = ?, stock = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setDouble(3, precio);
            stmt.setInt(4, categoria_id);
            stmt.setInt(5, stock);
            stmt.setInt(6, id);
            stmt.executeUpdate();
        }
    }
    

    public boolean eliminarProducto(int id) throws SQLException {
        String query = "DELETE FROM productos WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    public Object[][] buscarProductos(String criterio) throws SQLException {
        String query = "SELECT id, nombre, descripcion, precio, categoria_id, stock FROM productos WHERE nombre LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + criterio + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                return processResultSet(rs);
            }
        }
    }

    public Map<String, Integer> obtenerCategorias() throws SQLException {
        Map<String, Integer> categorias = new HashMap<>();
        String query = "SELECT id, nombre FROM categorias";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                categorias.put(rs.getString("nombre"), rs.getInt("id"));
            }
        }
        return categorias;
    }
    
/////////////////
//CLIENTES//
/////////////////

public void agregarCliente(String nombre, String email, String telefono, String direccion) throws SQLException {
    String query = "INSERT INTO clientes (nombre,email,telefono,direccion) VALUES (?, ?, ?, ?)";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, nombre);
        stmt.setString(2, email);
        stmt.setString(3, telefono);
        stmt.setString(4, direccion);
        stmt.executeUpdate();
    }
}
public Object[][] listarClientes() throws SQLException {
    String query = "SELECT id,nombre,email,telefono,direccion FROM clientes";
    return executeQuery(query);
}
public Object[] obtenerClientePorId(int id) throws SQLException {
    String query = "SELECT id,nombre,email,telefono,direccion FROM clientes WHERE id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, id);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("email"),
                    rs.getString("telefono"),
                    rs.getString("direccion")
                };
            }
            return null;
        }
    }
}
public void modificarCliente(int id, String nombre, String email, String telefono, String direccion) throws SQLException {
    String query = "UPDATE clientes SET nombre = ?, email = ?, telefono = ?, direccion = ? WHERE id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, nombre);
        stmt.setString(2, email);
        stmt.setString(3, telefono);
        stmt.setString(4, direccion);
        stmt.setInt(5, id);
        stmt.executeUpdate();
    }
}
public boolean eliminarCliente(int id) throws SQLException {
    String query = "DELETE FROM clientes WHERE id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, id);
        int affectedRows = stmt.executeUpdate();
        return affectedRows > 0;
    }
}
public Object[][] buscarClientes(String criterio) throws SQLException {
    String query = "SELECT id,nombre,email,telefono,direccion FROM clientes WHERE nombre LIKE ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setString(1, "%" + criterio + "%");
        try (ResultSet rs = stmt.executeQuery()) {
            return processResultSet(rs);
        }
    }
}


/////////////////
//PEDIDOS//
/////////////////

public List<Object[]> obtenerTodosLosClientes() throws SQLException {
    List<Object[]> lista = new ArrayList<>();
    String query = "SELECT id, nombre FROM clientes";
    try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
        while (rs.next()) {
            lista.add(new Object[]{rs.getInt("id"), rs.getString("nombre")});
        }
    }
    return lista;
}

public List<Object[]> obtenerTodosLosProductos() throws SQLException {
    List<Object[]> lista = new ArrayList<>();
    String query = "SELECT id, nombre, precio FROM productos";
    try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
        while (rs.next()) {
            lista.add(new Object[]{rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio")});
        }
    }
    return lista;
}

public int insertarPedido(int clienteId, double total) throws SQLException {
    String query = "INSERT INTO pedidos (cliente_id, estado, total) VALUES (?, 'pendiente', ?)";
    try (PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setInt(1, clienteId);
        stmt.setDouble(2, total);
        stmt.executeUpdate();
        try (ResultSet rs = stmt.getGeneratedKeys()) {
            if (rs.next()) return rs.getInt(1);
        }
    }
    throw new SQLException("No se pudo insertar el pedido");
}

public void insertarDetallePedido(int pedidoId, int productoId, int cantidad, double precioUnitario) throws SQLException {
    String query = "INSERT INTO detalles_pedido (pedido_id, producto_id, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, pedidoId);
        stmt.setInt(2, productoId);
        stmt.setInt(3, cantidad);
        stmt.setDouble(4, precioUnitario);
        stmt.executeUpdate();
    }
}
public List<Object[]> obtenerTodosLosPedidos() throws SQLException {
    List<Object[]> lista = new ArrayList<>();
    String query = "SELECT id, cliente_id, fecha_pedido, estado, total FROM pedidos";

    try (Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            lista.add(new Object[]{
                rs.getInt("id"),
                rs.getInt("cliente_id"),
                rs.getString("fecha_pedido"),
                rs.getString("estado"),
                rs.getDouble("total")
            });
        }
    }

    return lista;
}



    private Object[][] executeQuery(String query) throws SQLException {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return processResultSet(rs);
        }
    }

    private Object[][] processResultSet(ResultSet rs) throws SQLException {
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
