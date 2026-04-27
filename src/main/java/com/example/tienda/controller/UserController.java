package com.example.tienda.controller;

import com.example.tienda.model.Usuario;
import com.example.tienda.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

// @Controller indica a Spring MVC que esta clase maneja peticiones HTTP
@Controller
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }
    // GET / : Página de inicio.
    // Simplemente devuelve el nombre de la vista "index",
    // lo que hace que Thymeleaf renderice templates/index.html.
    @GetMapping("/")
    public String index() {
        return "index";
    }
    // GET /listausuarios : listado de todos los usuarios.
    // Model es el objeto que actúa de "puente" entre el controlador y la vista.
    // addAttribute("todos", ...) añade la lista con clave "todos",
    // que Thymeleaf usará en listatodos.html con th:each="usu : ${todos}".
    @GetMapping("/listausuarios")
    public String listar(Model model) {
        model.addAttribute("todos", service.obtenerTodos());
        return "listatodos";
    }
    // GET /nuevousuario : Formulario de alta vacío.
    // Creamos un objeto Usuario vacío y lo pasamos al modelo con clave "usaux".
    // Thymeleaf enlaza ese objeto con los campos del formulario (th:object="${usaux}").
    // Como id es null, al guardar se ejecutará un INSERT.
    @GetMapping("/nuevousuario")
    public String form(Model model) {
        model.addAttribute("usaux", new Usuario());
        return "form";
    }
    // GET /editarusuario/{id} : Formulario pre-rellenado con datos existentes.
    // @PathVariable extrae el {id} de la URL y lo convierte a Long automáticamente.
    // Cargamos el usuario de la BD y lo pasamos al modelo.
    // Como id tiene valor, al guardar se ejecutará un UPDATE.
    @GetMapping("/editarusuario/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("usaux", service.obtenerPorId(id));
        return "form";
    }

    // GET /borrarusuario/{id} : Elimina el usuario y redirige al listado.
    // Usamos redirect: para que el navegador haga una nueva petición GET
    // al listado, en lugar de volver a renderizar la misma URL de borrado.
    @GetMapping("/borrarusuario/{id}")
    public String borrar(@PathVariable Long id) {
        service.borrarUsuario(id);
        return "redirect:/listausuarios";
    }

    // POST /guardarusuario : Recibe el formulario y guarda el usuario.
    // Spring mapea automáticamente los campos del formulario HTML
    // (nombre, email, id oculto) al objeto Usuario gracias a los setters.
    // SI campo id viene relleno hace un UPDATE, si viene vacío hace un INSERT.
    @PostMapping("/guardarusuario")
    public String guardar(Usuario u) {
        service.guerdarUsuario(u);
        return "redirect:/listausuarios";
    }
}