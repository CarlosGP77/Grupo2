# Guía de Uso de Services

Esta guía explica cómo usar los Services creados para gestionar las entidades de la aplicación.

## UsuarioService

El `UsuarioService` proporciona métodos para gestionar usuarios.

### Inyectar el servicio

```java
@Autowired
private UsuarioService usuarioService;
```

### Métodos disponibles

#### Obtener todos los usuarios
```java
List<Usuario> usuarios = usuarioService.obtenerTodos();
```

#### Obtener un usuario por DNI
```java
Optional<Usuario> usuario = usuarioService.obtenerPorDni("12345678A");
```

#### Obtener un usuario por email
```java
Usuario usuario = usuarioService.obtenerPorEmail("juan@example.com");
```

#### Registrar un nuevo usuario
```java
Usuario nuevoUsuario = new Usuario("12345678A", "Juan García", "juan@example.com");
Usuario guardado = usuarioService.registrar(nuevoUsuario);
```

#### Guardar o actualizar
```java
Usuario actualizado = usuarioService.guardar(usuario);
```

#### Eliminar un usuario
```java
usuarioService.eliminar("12345678A");
```

#### Verificar si existe
```java
boolean existe = usuarioService.existe("12345678A");
```

---

## CursoService

El `CursoService` proporciona métodos para gestionar cursos.

### Inyectar el servicio

```java
@Autowired
private CursoService cursoService;
```

### Métodos disponibles

#### Obtener todos los cursos
```java
List<Curso> cursos = cursoService.obtenerTodos();
```

#### Obtener un curso por ID
```java
Optional<Curso> curso = cursoService.obtenerPorId(1);
```

#### Buscar cursos por nombre
```java
List<Curso> cursos = cursoService.buscarPorNombre("Java");
```

#### Crear un nuevo curso
```java
Curso nuevoCurso = new Curso("Curso de Python", "Aprende Python desde cero");
Curso guardado = cursoService.crear(nuevoCurso);
```

#### Actualizar un curso
```java
Curso actualizado = new Curso("Curso de Python 3.11", "Contenido actualizado");
Curso guardado = cursoService.actualizar(1, actualizado);
```

#### Eliminar un curso
```java
cursoService.eliminar(1);
```

#### Contar total de cursos
```java
long total = cursoService.contar();
```

---

## ReservaService

El `ReservaService` proporciona métodos para gestionar reservas de cursos.

### Inyectar el servicio

```java
@Autowired
private ReservaService reservaService;
```

### Métodos disponibles

#### Obtener todas las reservas
```java
List<Reserva> reservas = reservaService.obtenerTodas();
```

#### Obtener una reserva por ID
```java
Optional<Reserva> reserva = reservaService.obtenerPorId(1L);
```

#### Crear una nueva reserva
```java
Reserva nuevaReserva = new Reserva(usuario, curso);
Reserva guardada = reservaService.crear(nuevaReserva);
```

#### Confirmar una reserva
```java
Reserva confirmada = reservaService.confirmar(1L);
```

#### Cancelar una reserva
```java
Reserva cancelada = reservaService.cancelar(1L);
```

#### Obtener reservas de un usuario
```java
List<Reserva> reservas = reservaService.obtenerPorUsuario(usuario);
```

#### Obtener reservas de un curso
```java
List<Reserva> reservas = reservaService.obtenerPorCurso(curso);
```

#### Obtener reservas por estado
```java
List<Reserva> reservas = reservaService.obtenerPorEstado("confirmada");
```

#### Eliminar una reserva
```java
reservaService.eliminar(1L);
```

---

## Ejemplo de uso en un Controller

```java
@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private CursoService cursoService;

    @GetMapping
    public List<Reserva> obtenerTodas() {
        return reservaService.obtenerTodas();
    }

    @PostMapping
    public Reserva crear(@RequestBody Reserva reserva) {
        return reservaService.crear(reserva);
    }

    @PutMapping("/{id}/confirmar")
    public Reserva confirmar(@PathVariable Long id) {
        return reservaService.confirmar(id);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        reservaService.eliminar(id);
    }
}
```

---

## Notas Importantes

1. Los Services están anotados con `@Service` para que Spring los detecte automáticamente.
2. Utilizan `@Autowired` para inyectar los Repositories.
3. Contienen la lógica de negocio y validaciones.
4. Los Repositories proporcionan acceso directo a la base de datos.
5. Los métodos retornan tipos seguro de null, como `Optional<T>`, cuando es apropiado.

## Próximos pasos

1. Crear Controllers REST que usen estos Services.
2. Agregar más métodos de búsqueda y filtrado según necesidades.
3. Implementar validaciones más complejas en los Services.
4. Añadir manejo de excepciones personalizado.

