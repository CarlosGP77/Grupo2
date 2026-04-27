package com.example.tienda.model;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_pedido;
    private String cliente;
    private LocalDate fecha;

    @ManyToMany
    @JoinTable(
            name = "pedido_producto",
            joinColumns = @JoinColumn(name = "id_pedido"),
            inverseJoinColumns = @JoinColumn(name = "id_producto")
    )
    private List<Producto> productos = new ArrayList<>();

    //getter y setter
    public Long getId(){return id_pedido;}
    public void setId(Long id){this.id_pedido = id;}
    public String getCliente(){return cliente;}
    public void setCliente(String cliente){this.cliente = cliente;}
    public LocalDate getFecha(){return fecha;}
    public void setFecha(LocalDate fecha){this.fecha = fecha;}

    public List<Producto> getProductos(){return productos;}
    public void setProductos(List<Producto> productos){
        this.productos = productos;
    }
    public void addProducto(Producto producto) {
        this.productos.add(producto);
        producto.getPedidos().add(this);
    }
    public void removeProducto(Producto producto) {
        this.productos.remove(producto);
        producto.getPedidos().remove(this);
    }
}
