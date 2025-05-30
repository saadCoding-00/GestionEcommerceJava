package com.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GestorEcommerce extends JFrame {
    private JButton btnProducto, btnCliente, btnPedido;
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
        panelBotones.add(new JButton("Agregar"));
        panelBotones.add(new JButton("Modificar"));
        panelBotones.add(new JButton("Eliminar"));
        panelBotones.add(new JButton("Buscar"));
        panel.add(panelBotones, BorderLayout.NORTH);

        return panel;
    }

    // Clientes:
    private JPanel crearPanelClientes() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.add(new JButton("Agregar"));
        panelBotones.add(new JButton("Modificar"));
        panelBotones.add(new JButton("Eliminar"));
        panelBotones.add(new JButton("Buscar"));
        panel.add(panelBotones, BorderLayout.NORTH);


        return panel;
    }

    // Pedidos: 
    private JPanel crearPanelPedidos() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBotones.add(new JButton("Modificar"));
        panelBotones.add(new JButton("Buscar"));
        panel.add(panelBotones, BorderLayout.NORTH);


        return panel;
    }


    private void setupListeners() {

       
    }

}
