package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestorEcommerce extends JFrame {
    private JButton btnProducto, btnCliente, btnPedido;
    private JButton prAgregar,prListar, prModificar, prEliminar, prBuscar, cAgregar, cListar, cModificar, cEliminar, cBuscar, pCrear , pListar;
    private JPanel panelContenedor;
    private GestorBD dbManager;

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

        panel.add(new JScrollPane(resultTable), BorderLayout.CENTER);

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

    private void mostrarFormularioAgregarProducto() {}

    private void listarTodosProductos() {}

    private void mostrarFormularioModificarProducto() {}

    private void eliminarProducto() {}

    private void mostrarFormularioBuscarProducto() {}

    private void mostrarFormularioAgregarCliente() {}

    private void listarTodosClientes() {}

    private void mostrarFormularioModificarCliente() {}

    private void eliminarCliente() {}

    private void mostrarFormularioBuscarCliente() {}

    private void mostrarFormularioCrearPedido() {}

    private void listarTodosPedidos() {}

    


}
