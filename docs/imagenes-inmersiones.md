# Sistema Automático de Imágenes para Inmersiones

## Ubicación de las Imágenes

Las imágenes de las inmersiones van en:
```
src/main/resources/static/img/inmersiones/
```

## Cómo Funciona

El sistema normaliza automáticamente el nombre de la inmersión (desde la base de datos) para crear el nombre del archivo de imagen.

### Proceso de Normalización

1. **Elimina acentos**: é → e, ñ → n, á → a, etc.
2. **Convierte a minúsculas**: LA CALA → la cala
3. **Reemplaza espacios por guiones**: la cala → la-cala
4. **Elimina caracteres especiales**: solo permite a-z, 0-9, y guiones
5. **Elimina guiones al inicio/final**: -la-cala- → la-cala

### Ejemplos de Normalización

| Nombre en BD | Archivo Esperado |
|---|---|
| La Cala | `la-cala.webp` |
| Las Cuevas | `las-cuevas.webp` |
| El Calo | `el-calo.webp` |
| La Norte | `la-norte.webp` |
| Faro de la Cerda | `faro-de-la-cerda.webp` |
| Las Lastras del Palacio | `las-lastras-del-palacio.webp` |
| El Buzo's Cove | `el-buzos-cove.webp` |
| San José | `san-jose.webp` |

## Imágenes Actuales

Las siguientes imágenes ya existen en `src/main/resources/static/img/inmersiones/`:

- `la-cala.webp`
- `las-cuevas.webp`
- `el-calo.webp`
- `la-norte.webp`
- `faro-de-la-cerda.webp`
- `las-lastras-del-palacio.webp`

## Agregar una Nueva Inmersión

Cuando agregas una nueva inmersión a la base de datos:

1. **En la BD**: Inserta la nueva inmersión con su nombre
2. **En la carpeta de imágenes**: Crea un archivo `.webp` con el nombre normalizado
3. **Automático**: La página `/inmersiones` cargará la imagen automáticamente

### Ejemplo: Agregar "Punta Somera"

1. Inserta en BD: `INSERT INTO inmersiones (...) VALUES (..., 'Punta Somera', ...)`
2. Crea la imagen: `src/main/resources/static/img/inmersiones/punta-somera.webp`
3. ¡Listo! La página mostrará automáticamente la imagen

## Estructura del Proyecto

```
src/main/
├── java/
│   └── com/example/
│       └── model/
│           └── Inmersion.java          ← Contiene el método getNombreArchivoWebp()
├── resources/
│   ├── templates/
│   │   └── html/
│   │       └── inmersiones.html        ← Usa el método para cargar imágenes
│   └── static/
│       └── img/
│           └── inmersiones/            ← Lugar donde van las imágenes
```

## Código Clave

### En `Inmersion.java`:
```java
@Transient
public String getNombreArchivoWebp() {
    if (nombre == null || nombre.isBlank()) {
        return "default";
    }

    String normalizado = Normalizer.normalize(nombre, Normalizer.Form.NFD)
            .replaceAll("\\p{M}", "")
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z0-9]+", "-")
            .replaceAll("(^-|-$)", "");

    return normalizado.isBlank() ? "default" : normalizado;
}
```

### En `inmersiones.html`:
```html
<img th:src="@{/img/inmersiones/{file}.webp(file=${i.nombreArchivoWebp})}"
     class="card-img-top"
     th:alt="${'Imagen de ' + i.nombre}">
```

## Notas Importantes

- Las imágenes **deben estar en formato `.webp`**
- El nombre debe ser **exactamente el que genera la normalización**
- Si la imagen no existe, mostrará un error 404 en la consola del navegador (pero no romperá la página)
- El sistema es **completamente automático**: no necesitas código adicional

## Ventajas del Sistema

✅ **Automático**: No necesitas actualizar código cuando añades nuevas inmersiones
✅ **Seguro**: Los nombres de archivo se normalizan de forma consistente
✅ **Flexible**: Funciona con cualquier nombre de inmersión (incluso con acentos o caracteres especiales)
✅ **Escalable**: Agregar 100 nuevas inmersiones no requiere cambios de código

