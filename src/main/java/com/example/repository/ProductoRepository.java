package com.example.repository;
import com.example.model.Reservas;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository  extends JpaRepository<Reservas, Long>{
}
