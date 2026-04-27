package com.example.tienda.model;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_producto;
    private String nombre;
    private double precio;

    @ManyToMany(mappedBy = "productos")
    private List<Pedido> pedidos = new ArrayList<>();

    //getter y setter
    public Long getId(){return id_producto;}
    public void setId(Long id){this.id_producto=id;}
    public String getNombre(){return nombre;}
    public void setNombre(String nom){this.nombre=nom;}
    public double getPrecio(){return precio;}
    public void setPrecio(double precio){this.precio = precio;}
    public List<Pedido> getPedidos(){return pedidos;}
    public void setPedidos(List<Pedido> pedidos){this.pedidos = pedidos;}

}
