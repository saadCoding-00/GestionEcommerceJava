package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.HashMap;


public class GestorEcommerce extends JFrame {
    private JButton btnProducto, btnCliente, btnPedido;
    private JButton prAgregar, prListar, prModificar, prEliminar, prBuscar, cAgregar, cListar, cModificar, cEliminar,
            cBuscar, pCrear, pListar;
    private JPanel panelContenedor;
    private GestorBD dbManager;

    private JTable productosTable;
    private DefaultTableModel productosTableModel;

    private JTable clientesTable;
    private DefaultTableModel clientesTableModel;

    private JTable resultTable;
    private DefaultTableModel tableModel;

    public GestorEcommerce() {
        dbManager = new GestorBD();
        initializeComponents();
        setupLayout();
        setupListeners();
    }

    private void initializeComponents() {

        btnProducto = new JButton("Productos");
        btnCliente = new JButton("Clientes");
        btnPedido = new JButton("Pedidos");

        prAgregar = new JButton("Agregar Producto");
        prListar = new JButton("Listar Productos");
        prModificar = new JButton("Modificar Producto");
        prEliminar = new JButton("Eliminar Producto");
        prBuscar = new JButton("Buscar Producto");

        cAgregar = new JButton("Añadir Cliente");
        cListar = new JButton("Listar Clientes");
        cModificar = new JButton("Modificar Cliente");
        cEliminar = new JButton("Eliminar Cliente");
        cBuscar = new JButton("Buscar Cliente");

        pCrear = new JButton("Crear Pedidos");
        pListar = new JButton("Listar Pedidos");

        productosTableModel = new DefaultTableModel();
        productosTable = new JTable(productosTableModel);

        clientesTableModel = new DefaultTableModel();
        clientesTable = new JTable(clientesTableModel);

        // Tabla de resultados
        tableModel = new DefaultTableModel();
        resultTable = new JTable(tableModel);
    }

    private void setupLayout() {
        setTitle("Gestor de E-commerce");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLayout(new BorderLayout(10, 10));
        setLocationRelativeTo(null);

        // Panel superior (buttones principales)
        JPanel manejarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        manejarPanel.add(btnProducto);
        manejarPanel.add(btnCliente);
        manejarPanel.add(btnPedido);

        // Panel contenedor con CardLayout
        panelContenedor = new JPanel(new CardLayout());

        // Añadir paneles personalizados con botones únicos
        panelContenedor.add(crearPanelProductos(), "PRODUCTOS");
        panelContenedor.add(crearPanelClientes(), "CLIENTES");
        panelContenedor.add(crearPanelPedidos(), "PEDIDOS");

        // Acciones
        btnProducto.addActionListener(e -> mostrarPanel("PRODUCTOS"));
        btnCliente.addActionListener(e -> mostrarPanel("CLIENTES"));
        btnPedido.addActionListener(e -> mostrarPanel("PEDIDOS"));

        // Layout principal
        setLayout(new BorderLayout());
        add(manejarPanel, BorderLayout.NORTH);
        add(panelContenedor, BorderLayout.CENTER);

    }

    private void mostrarPanel(String nombrePanel) {
        CardLayout cl = (CardLayout) panelContenedor.getLayout();
        cl.show(panelContenedor, nombrePanel);
    }

    // Productos:
    private JPanel crearPanelProductos() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.add(prAgregar);
        panelBotones.add(prListar);
        panelBotones.add(prModificar);
        panelBotones.add(prEliminar);
        panelBotones.add(prBuscar);
        panel.add(panelBotones, BorderLayout.NORTH);

        panel.add(new JScrollPane(productosTable), BorderLayout.CENTER);

        return panel;
    }

    // Clientes:
    private JPanel crearPanelClientes() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.add(cAgregar);
        panelBotones.add(cListar);
        panelBotones.add(cModificar);
        panelBotones.add(cEliminar);
        panelBotones.add(cBuscar);
        panel.add(panelBotones, BorderLayout.NORTH);

        panel.add(new JScrollPane(clientesTable), BorderLayout.CENTER);
        return panel;
    }

    // Pedidos:
    private JPanel crearPanelPedidos() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.add(pCrear);
        panelBotones.add(pListar);
        panel.add(panelBotones, BorderLayout.NORTH);

        panel.add(new JScrollPane(resultTable), BorderLayout.CENTER);
        return panel;
    }

    private void setupListeners() {
        prAgregar.addActionListener(e -> mostrarFormularioAgregarProducto());
        prListar.addActionListener(e -> listarTodosProductos());
        prModificar.addActionListener(e -> mostrarFormularioModificarProducto());
        prEliminar.addActionListener(e -> eliminarProducto());
        prBuscar.addActionListener(e -> mostrarFormularioBuscarProducto());

        cAgregar.addActionListener(e -> mostrarFormularioAgregarCliente());
        cListar.addActionListener(e -> listarTodosClientes());
        cModificar.addActionListener(e -> mostrarFormularioModificarCliente());
        cEliminar.addActionListener(e -> eliminarCliente());
        cBuscar.addActionListener(e -> mostrarFormularioBuscarCliente());

        pCrear.addActionListener(e -> mostrarFormularioCrearPedido());
        pListar.addActionListener(e -> listarPedidos());

    }

    private void mostrarFormularioAgregarProducto() {
        JDialog agregarDialog = new JDialog(this, "Agregar Producto", true);
        JPanel panel = new JPanel(new GridLayout(10, 2, 5, 5));
    
        JTextField nombreField = new JTextField(20);
        JTextField descripcionField = new JTextField(20);
        JTextField precioField = new JTextField(20);
        JTextField stockField = new JTextField(20);
    
        final Map<String, Integer> categorias = new HashMap<>();
        JComboBox<String> categoriaComboBox = new JComboBox<>();
    
        try {
            categorias.putAll(dbManager.obtenerCategorias());
            for (String nombre : categorias.keySet()) {
                categoriaComboBox.addItem(nombre);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(agregarDialog, "Error al obtener categorías: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return; // No seguir si no se pueden cargar las categorías
        }
    
        panel.add(new JLabel("Nombre (obligatorio):"));
        panel.add(nombreField);
        panel.add(new JLabel("Descripción:"));
        panel.add(descripcionField);
        panel.add(new JLabel("Precio (obligatorio):"));
        panel.add(precioField);
        panel.add(new JLabel("Stock (obligatorio):"));
        panel.add(stockField);
        panel.add(new JLabel("Categoría (obligatorio):"));
        panel.add(categoriaComboBox);
    
        JButton guardarButton = new JButton("Guardar");
        guardarButton.addActionListener(e -> {
            String nombre = nombreField.getText().trim();
            String descripcion = descripcionField.getText().trim();
            double precio;
            int stock;
    
            try {
                precio = Double.parseDouble(precioField.getText().trim());
                stock = Integer.parseInt(stockField.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(agregarDialog, "Precio y stock deben ser numéricos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            if (nombre.isEmpty() || precio == 0 || stock == 0) {
                JOptionPane.showMessageDialog(agregarDialog, "Campos obligatorios vacíos o inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
    
            String categoriaSeleccionada = (String) categoriaComboBox.getSelectedItem();
            int categoria_id = categorias.get(categoriaSeleccionada);
    
            try {
                dbManager.agregarProducto(nombre, descripcion, precio, categoria_id, stock);
                JOptionPane.showMessageDialog(agregarDialog, "Producto agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                listarTodosProductos();
                agregarDialog.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(agregarDialog, "Error al agregar producto: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    
        panel.add(new JLabel());
        panel.add(guardarButton);
    
        agregarDialog.add(panel);
        agregarDialog.pack();
        agregarDialog.setLocationRelativeTo(this);
        agregarDialog.setVisible(true);
    }
    
    

    private void listarTodosProductos() {
        productosTableModel.setRowCount(0);
        productosTableModel.setColumnCount(0);
        try {
            Object[][] data = dbManager.listarProductos();
            String[] columnNames = dbManager.getColumnNames();
            if (columnNames != null) {
                productosTableModel.setColumnIdentifiers(columnNames);
            }
            if (data != null) {
                for (Object[] row : data) {
                    productosTableModel.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al listar productos: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void mostrarFormularioModificarProducto() {
        String idStr = JOptionPane.showInputDialog(this, "Ingrese el ID del producto a modificar:",
                "Modificar Producto", JOptionPane.QUESTION_MESSAGE);
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                Object[] producto = dbManager.obtenerProductoPorId(id);
                if (producto != null) {
                    JDialog modificarDialog = new JDialog(this, "Modificar Producto", true);
                    JPanel panel = new JPanel(new GridLayout(10, 2, 5, 5));

                    JTextField idField = new JTextField(String.valueOf(producto[0]), 5);
                    idField.setEditable(false);
                    JTextField nombreField = new JTextField((String) producto[1], 20);
                    JTextField descripcionField = new JTextField((String) producto[2], 20);
                    JTextField precioField = new JTextField(String.valueOf(producto[3]), 20);
                    Map<String, Integer> categorias = dbManager.obtenerCategorias();
                    JComboBox<String> categoriaComboBox = new JComboBox<>(categorias.keySet().toArray(new String[0]));

                    // Selecciona la categoría actual del producto
                    for (Map.Entry<String, Integer> entry : categorias.entrySet()) {
                        if (entry.getValue().equals((Integer) producto[4])) {
                            categoriaComboBox.setSelectedItem(entry.getKey());
                            break;
                        }
                    }
                    JTextField stockField = new JTextField(String.valueOf(producto[5]), 20);

                    panel.add(new JLabel("ID:"));
                    panel.add(idField);
                    panel.add(new JLabel("Nombre (obligatorio):"));
                    panel.add(nombreField);
                    panel.add(new JLabel("Descripcion :"));
                    panel.add(descripcionField);
                    panel.add(new JLabel("Precio (obligatorio):"));
                    panel.add(precioField);
                    panel.add(new JLabel("Categoria_id (obligatorio):"));
                    panel.add(categoriaComboBox);
                    panel.add(new JLabel("Stock (obligatorio):"));
                    panel.add(stockField);

                    JButton guardarButton = new JButton("Guardar Cambios");
                    guardarButton.addActionListener(e2 -> {
                        String nombre = nombreField.getText().trim();
                        String descripcion = descripcionField.getText().trim();
                        double precio;
                        int categoria_id;
                        int stock;

                        try {
                            precio = Double.parseDouble(precioField.getText().trim());
                            String categoriaSeleccionada = (String) categoriaComboBox.getSelectedItem();
                            categoria_id = categorias.get(categoriaSeleccionada);

                            stock = Integer.parseInt(stockField.getText().trim());
                        } catch (NumberFormatException nfe) {
                            JOptionPane.showMessageDialog(modificarDialog,
                                    "Precio, Categoría y Stock deben ser números válidos.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (nombre.isEmpty()) {
                            JOptionPane.showMessageDialog(modificarDialog,
                                    "El nombre es obligatorio.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        try {
                            dbManager.modificarProducto(id, nombre, descripcion, precio, categoria_id, stock);
                            JOptionPane.showMessageDialog(modificarDialog, "Producto modificado correctamente.",
                                    "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            listarTodosProductos();
                            modificarDialog.dispose();
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(modificarDialog,
                                    "Error al modificar el producto: " + ex.getMessage(), "Error",
                                    JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                    });

                    JPanel buttonPanel = new JPanel();
                    buttonPanel.add(guardarButton);
                    panel.add(buttonPanel);

                    modificarDialog.add(panel);
                    modificarDialog.pack();
                    modificarDialog.setLocationRelativeTo(this);
                    modificarDialog.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró ningún producto con el ID: " + id, "Error",
                            JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID válido.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al buscar el producto: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void eliminarProducto() {
        String idStr = JOptionPane.showInputDialog(this, "Ingrese el ID del producto a eliminar:", "Eliminar Producto",
                JOptionPane.QUESTION_MESSAGE);
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                int confirmacion = JOptionPane.showConfirmDialog(this,
                        "¿Está seguro de que desea eliminar el producto con ID " + id + "?", "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    boolean eliminado = dbManager.eliminarProducto(id);
                    if (eliminado) {
                        JOptionPane.showMessageDialog(this, "Producto eliminado correctamente.", "Éxito",
                                JOptionPane.INFORMATION_MESSAGE);
                        listarTodosProductos();
                    } else {
                        JOptionPane.showMessageDialog(this, "No se encontró ningún producto con el ID: " + id, "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID válido.", "Error",
                        JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el producto: " + ex.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void mostrarFormularioBuscarProducto() {
        JPanel buscarPanel = new JPanel(new FlowLayout());
        JTextField buscarField = new JTextField(20);
        JButton buscarButton = new JButton("Buscar");
        buscarPanel.add(new JLabel("Buscar por Nombre :"));
        buscarPanel.add(buscarField);
        buscarPanel.add(buscarButton);

        JDialog buscarDialog = new JDialog(this, "Buscar Producto", true);
        buscarDialog.setLayout(new BorderLayout());
        buscarDialog.add(buscarPanel, BorderLayout.NORTH);

        DefaultTableModel buscarTableModel = new DefaultTableModel();
        JTable buscarResultTable = new JTable(buscarTableModel);
        JScrollPane buscarScrollPane = new JScrollPane(buscarResultTable);
        buscarDialog.add(buscarScrollPane, BorderLayout.CENTER);

        buscarButton.addActionListener(e -> {
            String criterio = buscarField.getText().trim();
            if (!criterio.isEmpty()) {
                buscarTableModel.setRowCount(0);
                buscarTableModel.setColumnCount(0);
                try {
                    Object[][] resultados = dbManager.buscarProductos(criterio);
                    String[] columnNames = dbManager.getColumnNames();
                    if (columnNames != null) {
                        buscarTableModel.setColumnIdentifiers(columnNames);
                    }
                    if (resultados != null) {
                        for (Object[] row : resultados) {
                            buscarTableModel.addRow(row);
                        }
                    }
                    if (buscarTableModel.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(buscarDialog, "No se encontraron productos con ese criterio.",
                                "Información", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(buscarDialog, "Error al buscar productos: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(buscarDialog, "Ingrese un nombre  para buscar.", "Advertencia",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        buscarDialog.pack();
        buscarDialog.setLocationRelativeTo(this);
        buscarDialog.setVisible(true);
    }


    /////////////
    /////////////
    ////////////
    //Clientes//
    /////////////
    ////////////
    private void mostrarFormularioAgregarCliente() {
        JDialog agregarDialog = new JDialog(this, "Agregar Cliente", true);
        JPanel panel = new JPanel(new GridLayout(10, 2, 5, 5));
    
        JTextField nombreField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JTextField telefonoField = new JTextField(20);
        JTextField direccionField = new JTextField(20);
    
        
    
        panel.add(new JLabel("Nombre (obligatorio):"));
        panel.add(nombreField);
        panel.add(new JLabel("email (obligatorio):"));
        panel.add(emailField);
        panel.add(new JLabel("telefono (obligatorio):"));
        panel.add(telefonoField);
        panel.add(new JLabel("direccion :"));
        panel.add(direccionField);
        
       
    
        JButton guardarButton = new JButton("Guardar");
        guardarButton.addActionListener(e -> {
            String nombre = nombreField.getText().trim();
            String email = emailField.getText().trim();
            String telefono = telefonoField.getText().trim();
            String direccion = direccionField.getText().trim();
    
           
    
            if (nombre.isEmpty() || email.isEmpty() || telefono.isEmpty()) {
                JOptionPane.showMessageDialog(agregarDialog, "Campos obligatorios vacíos o inválidos.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                JOptionPane.showMessageDialog(agregarDialog, "Formato de email no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (!telefono.matches("^\\d{3}-\\d{4}$")) {
                JOptionPane.showMessageDialog(agregarDialog, "Formato de teléfono inválido. Usa formato 555-1234.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
    
        
            try {
                dbManager.agregarCliente(nombre, email, telefono, direccion);
                JOptionPane.showMessageDialog(agregarDialog, "Cliente agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                listarTodosClientes();
                agregarDialog.dispose();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(agregarDialog, "Error al agregar Cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });
    
        panel.add(new JLabel());
        panel.add(guardarButton);
    
        agregarDialog.add(panel);
        agregarDialog.pack();
        agregarDialog.setLocationRelativeTo(this);
        agregarDialog.setVisible(true);
    }

    private void listarTodosClientes() {
        clientesTableModel.setRowCount(0);
        clientesTableModel.setColumnCount(0);
        try {
            Object[][] data = dbManager.listarClientes();
            String[] columnNames = dbManager.getColumnNames();
            if (columnNames != null) {
                clientesTableModel.setColumnIdentifiers(columnNames);
            }
            if (data != null) {
                for (Object[] row : data) {
                    clientesTableModel.addRow(row);
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al listar clientes: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void mostrarFormularioModificarCliente() {
        String idStr = JOptionPane.showInputDialog(this, "Ingrese el ID del cliente a modificar:", "Modificar Cliente", JOptionPane.QUESTION_MESSAGE);
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                Object[] cliente = dbManager.obtenerClientePorId(id);
                if (cliente != null) {
                    JDialog modificarDialog = new JDialog(this, "Modificar Cliente", true);
                    JPanel panel = new JPanel(new GridLayout(10, 2, 5, 5));

                    JTextField idField = new JTextField(String.valueOf(cliente[0]), 5);
                    idField.setEditable(false);
                    JTextField nombreField = new JTextField((String) cliente[1], 20);
                    JTextField emailField = new JTextField((String) cliente[2], 20);
                    JTextField telefonoField = new JTextField((String) cliente[3], 20);
                    JTextField direccionField = new JTextField((String) cliente[4], 20);
                    

                    panel.add(new JLabel("ID:"));
                    panel.add(idField);
                    panel.add(new JLabel("Nombre (obligatorio):"));
                    panel.add(nombreField);
                    panel.add(new JLabel("email (obligatorio):"));
                    panel.add(emailField);
                    panel.add(new JLabel("Teléfono  (obligatorio):"));
                    panel.add(telefonoField);
                    panel.add(new JLabel("Direccion (opcional):"));
                    panel.add(direccionField);
                    

                    JButton guardarButton = new JButton("Guardar Cambios");
                    guardarButton.addActionListener(e2 -> {
                        String nombre = nombreField.getText().trim();
                        String email = emailField.getText().trim();
                        String telefono = telefonoField.getText().trim();
                        String direccion = direccionField.getText().trim();
                        

                        if (nombre.isEmpty() || email.isEmpty() || telefono.isEmpty() ) {
                            JOptionPane.showMessageDialog(modificarDialog, "Los campos Nombre, Email, Teléfono  son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                            JOptionPane.showMessageDialog(modificarDialog, "Formato de email no válido.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        if (!telefono.matches("^\\d{3}-\\d{4}$")) {
                            JOptionPane.showMessageDialog(modificarDialog, "Formato de teléfono inválido. Usa formato 555-1234.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        

                        try {
                            dbManager.modificarCliente(id, nombre,  email,telefono, direccion);
                            JOptionPane.showMessageDialog(modificarDialog, "Cliente modificado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            listarTodosClientes();
                            modificarDialog.dispose();
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(modificarDialog, "Error al modificar el cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                    });

                    JPanel buttonPanel = new JPanel();
                    buttonPanel.add(guardarButton);
                    panel.add(buttonPanel, BorderLayout.SOUTH);

                    modificarDialog.add(panel);
                    modificarDialog.pack();
                    modificarDialog.setLocationRelativeTo(this);
                    modificarDialog.setVisible(true);

                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró ningún cliente con el ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al buscar el cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void eliminarCliente() {
        String idStr = JOptionPane.showInputDialog(this, "Ingrese el ID del cliente a eliminar:", "Eliminar Cliente", JOptionPane.QUESTION_MESSAGE);
        if (idStr != null && !idStr.isEmpty()) {
            try {
                int id = Integer.parseInt(idStr);
                int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar el cliente con ID " + id + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
                if (confirmacion == JOptionPane.YES_OPTION) {
                    boolean eliminado = dbManager.eliminarCliente(id);
                    if (eliminado) {
                        JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        listarTodosClientes();
                    } else {
                        JOptionPane.showMessageDialog(this, "No se encontró ningún cliente con el ID: " + id, "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Por favor, ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    private void mostrarFormularioBuscarCliente() {
        JPanel buscarPanel = new JPanel(new FlowLayout());
        JTextField buscarField = new JTextField(20);
        JButton buscarButton = new JButton("Buscar");
        buscarPanel.add(new JLabel("Buscar por Nombre :"));
        buscarPanel.add(buscarField);
        buscarPanel.add(buscarButton);

        JDialog buscarDialog = new JDialog(this, "Buscar Cliente", true);
        buscarDialog.setLayout(new BorderLayout());
        buscarDialog.add(buscarPanel, BorderLayout.NORTH);

        DefaultTableModel buscarTableModel = new DefaultTableModel();
        JTable buscarResultTable = new JTable(buscarTableModel);
        JScrollPane buscarScrollPane = new JScrollPane(buscarResultTable);
        buscarDialog.add(buscarScrollPane, BorderLayout.CENTER);

        buscarButton.addActionListener(e -> {
            String criterio = buscarField.getText().trim();
            if (!criterio.isEmpty()) {
                buscarTableModel.setRowCount(0);
                buscarTableModel.setColumnCount(0);
                try {
                    Object[][] resultados = dbManager.buscarClientes(criterio);
                    String[] columnNames = dbManager.getColumnNames();
                    if (columnNames != null) {
                        buscarTableModel.setColumnIdentifiers(columnNames);
                    }
                    if (resultados != null) {
                        for (Object[] row : resultados) {
                            buscarTableModel.addRow(row);
                        }
                    }
                    if (buscarTableModel.getRowCount() == 0) {
                        JOptionPane.showMessageDialog(buscarDialog, "No se encontraron clientes con ese criterio.", "Información", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(buscarDialog, "Error al buscar clientes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(buscarDialog, "Ingrese un nombre  para buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });

        buscarDialog.pack();
        buscarDialog.setLocationRelativeTo(this);
        buscarDialog.setVisible(true);
    }
    
    private void listarPedidos() {
        try {
            List<Object[]> pedidos = dbManager.obtenerTodosLosPedidos();
    
            DefaultTableModel model = new DefaultTableModel(new String[]{"ID", "Cliente ID", "Fecha", "Estado", "Total"}, 0);
    
            for (Object[] pedido : pedidos) {
                model.addRow(pedido);
            }
    
            resultTable.setModel(model);
    
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al listar pedidos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    private void mostrarFormularioCrearPedido() {
        JDialog dialog = new JDialog(this, "Nuevo Pedido", true);
        dialog.setLayout(new BorderLayout());
    
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
    
        JComboBox<Object> clienteCombo = new JComboBox<>();
        JComboBox<Object> productoCombo = new JComboBox<>();
        JTextField cantidadField = new JTextField();
    
        // Cargar clientes
        try {
            for (Object[] cliente : dbManager.obtenerTodosLosClientes()) {
                clienteCombo.addItem(cliente[0] + " - " + cliente[1]); 
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar clientes");
            return;
        }
    
        // Cargar productos
        try {
            for (Object[] producto : dbManager.obtenerTodosLosProductos()) {
                productoCombo.addItem(producto[0] + " - " + producto[1] + " - $" + producto[2]);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar productos");
            return;
        }
    
        formPanel.add(new JLabel("Cliente:"));
        formPanel.add(clienteCombo);
        formPanel.add(new JLabel("Producto:"));
        formPanel.add(productoCombo);
        formPanel.add(new JLabel("Cantidad:"));
        formPanel.add(cantidadField);
    
        DefaultTableModel carritoModel = new DefaultTableModel(new String[]{"ID", "Nombre", "Cantidad", "Precio"}, 0);
        JTable carritoTable = new JTable(carritoModel);
    
        JButton agregarBtn = new JButton("Agregar al carrito");
        agregarBtn.addActionListener(e -> {
            if (productoCombo.getSelectedItem() == null) return;
    
            String item = (String) productoCombo.getSelectedItem();
            int id = Integer.parseInt(item.split(" - ")[0]);
            String nombre = item.split(" - ")[1];
            double precio = Double.parseDouble(item.split("\\$")[1]);
    
            int cantidad = 0;
            try {
                cantidad = Integer.parseInt(cantidadField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Cantidad no válida");
                return;
            }
    
            carritoModel.addRow(new Object[]{id, nombre, cantidad, precio});
        });
    
        JButton guardarBtn = new JButton("Guardar Pedido");
        guardarBtn.addActionListener(e -> {
            if (clienteCombo.getSelectedItem() == null || carritoModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(dialog, "Falta cliente o productos");
                return;
            }
    
            int clienteId = Integer.parseInt(clienteCombo.getSelectedItem().toString().split(" - ")[0]);
            double total = 0;
            List<Object[]> detalles = new ArrayList<>();
    
            for (int i = 0; i < carritoModel.getRowCount(); i++) {
                int productoId = (int) carritoModel.getValueAt(i, 0);
                int cantidad = (int) carritoModel.getValueAt(i, 2);
                double precio = (double) carritoModel.getValueAt(i, 3);
                total += cantidad * precio;
                detalles.add(new Object[]{productoId, cantidad, precio});
            }
    
            try {
                int pedidoId = dbManager.insertarPedido(clienteId, total);
                for (Object[] d : detalles) {
                    dbManager.insertarDetallePedido(pedidoId, (int) d[0], (int) d[1], (double) d[2]);
                }
    
                JOptionPane.showMessageDialog(dialog, "Pedido guardado con éxito");
                dialog.dispose();
                listarPedidos(); // refrescar tabla
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(dialog, "Error al guardar pedido: " + ex.getMessage());
            }
        });
    
        JPanel botonesPanel = new JPanel(new FlowLayout());
        botonesPanel.add(agregarBtn);
        botonesPanel.add(guardarBtn);
    
        dialog.add(formPanel, BorderLayout.NORTH);
        dialog.add(new JScrollPane(carritoTable), BorderLayout.CENTER);
        dialog.add(botonesPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    


    @Override
    protected void finalize() throws Throwable {
        dbManager.disconnect();
        super.finalize();
    }

}
