package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.*;

public class GestorEcommerce extends JFrame {
    private JButton btnProducto, btnCliente, btnPedido;
    private JButton prAgregar, prListar, prModificar, prEliminar, prBuscar, cAgregar, cListar, cModificar, cEliminar,
            cBuscar, pCrear, pListar;
    private JPanel panelContenedor;
    private GestorBD dbManager;

    private JTable productosTable;
    private DefaultTableModel productosTableModel;

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

        panel.add(new JScrollPane(resultTable), BorderLayout.CENTER);
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
        pListar.addActionListener(e -> listarTodosPedidos());

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
            JOptionPane.showMessageDialog(this, "Error al listar contactos: " + ex.getMessage(), "Error",
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

    private void mostrarFormularioAgregarCliente() {
    }

    private void listarTodosClientes() {
    }

    private void mostrarFormularioModificarCliente() {
    }

    private void eliminarCliente() {
    }

    private void mostrarFormularioBuscarCliente() {
    }

    private void mostrarFormularioCrearPedido() {
    }

    private void listarTodosPedidos() {
    }

    @Override
    protected void finalize() throws Throwable {
        dbManager.disconnect();
        super.finalize();
    }

}
