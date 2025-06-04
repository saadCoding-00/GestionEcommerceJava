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
//AQUI CONECTAMOS CON LA BASE DE DATOS CREADA EN LA TAREA DE BASE DE DATOS CON NUESTRO USUARIO Y CONTRASEÑA//
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
//MANEJAR PRODUCTOS//
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
//OBTENER PRODUCTO POR SU ID//
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
    
//MODIFICAR LOS CAMPOS DE PRODUCTOS//
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
    
//AQUI ELIMINAR EL PRODUCTO DEPENDE DEL ID ELEGIDO POR EL USUARIO//
    public boolean eliminarProducto(int id) throws SQLException {
        String query = "DELETE FROM productos WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }
//AQUI BUSCAR EL PRODUCTO CON UN CRITERIO QUE ES EL NOMBRE EL USUARIO ESCRIBA EL NOMBRE Y LISTAMOS EL PRODUCTO CON ESTE NOMBRE//
    public Object[][] buscarProductos(String criterio) throws SQLException {
        String query = "SELECT id, nombre, descripcion, precio, categoria_id, stock FROM productos WHERE nombre LIKE ?";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + criterio + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                return processResultSet(rs);
            }
        }
    }

    //OBTENER CATEGORIAS DESDE LA TABLA SQL Y PONERLA COMO MAPA PARA TENER EL ID Y EL NOMBRE Y RELLENAR EL JCOMBOBOX CON EL NOMBRE Y CON EL NOMBRE OBTENEMOS EL ID CON EL KEY//
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
//MANEJAR CLIENTES//
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
//OBTENER EL CLIENTE POR UN ID ELEGIDO POR EL USUARIO//
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
//MODIFICAR LOS CAMPOS DE CLIENTE//
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
//ELIMINAR UN CLIENTE CON EL ID ELEGIDO POR USUARIO//
public boolean eliminarCliente(int id) throws SQLException {
    String query = "DELETE FROM clientes WHERE id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, id);
        int affectedRows = stmt.executeUpdate();
        return affectedRows > 0;
    }
}
//BUSCAR CLIENTE POR SU NOMBRE Y LISTAR INFORMACIONES DE ESTE CLIENTE//
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
//MANEJAR PEDIDOS//
/////////////////

//OBTENER LOS CLIENTES EN UNA LISTA PARA PONERLOS EN JCOMBOBOX Y EL USUARIO PUEDA ELEJIR EL CLIENTE A QUEL ASIGNA EL PEDIDO//
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
//OBTENER LOS PRODUCTOS EN UNA LISTA PARA PONERLOS EN JCOMBOBOX Y EL USUARIO PUEDA ELEJIR EL PRODUCTO A QUEL ASIGNA EL PEDIDO//
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
//AQUI USAMOS LA TABLA PEDIDOS PARA INSERTAR UN PEDIDO USAMOS EL CLIENTE_ID//
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
//AQUI USAMOS LA TABLA DETALLES_PEDIDO PARA INSERTAR LOS DETALLES CON EL PEDIDO_ID Y PRODUCTO_ID//
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
//AQUI POR FIN OBTENEMOS LA LISTA DE TODOS LOS PEDIDOS PARA INSERTARLA EN LA TABLA DE NUESTRA INTERFACE CON TABLE MODEL//
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
