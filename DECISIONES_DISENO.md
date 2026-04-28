# Decisiones de diseno — BiblioTech

Este documento resume algunas decisiones que tome para organizar el proyecto

## Separacion por capas

Dividi el proyecto en capas para que cada parte tenga una responsabilidad:

- **model**: tiene las clases principales del sistema, como recursos, socios, prestamos y sanciones.
- **repository**: guarda y busca datos en memoria.
- **service**: contiene las reglas de negocio y las validaciones.
- **exception**: tiene las excepciones propias del sistema.

Esto ayuda a que el codigo no quede todo mezclado.

## Interfaces y repositorios

Use interfaces para que el codigo no dependa directamente de una implementacion concreta.

Tambien agregue un repositorio generico `Repository<T, ID>` con metodos comunes como guardar, buscar por ID y listar todos. Despues, cada repositorio especifico agrega sus propias busquedas.

## Records, clases y enums

Use `record` para `LibroFisico` y `Ebook` porque son datos simples.

Use clases normales para `Prestamo`, `Socio` y `Sancion`. En especial, `Prestamo` necesitaba ser una clase porque cambia de estado cuando se devuelve.

Tambien use enums para datos fijos, como `Categoria` y `TipoSocio`, para evitar escribir textos sueltos y posibles errores.

## Optional y excepciones

En las busquedas use `Optional` para evitar devolver `null`.

Tambien cree excepciones personalizadas para manejar errores propios del sistema, como recurso no encontrado, socio invalido, limite de prestamos o socio sancionado.

## Prestamos, historial y sanciones

El prestamo guarda la informacion necesaria para saber si esta activo, cuando vence, cuando se devolvio y si hubo retraso.

No cree una clase separada para historial porque los prestamos ya guardan esos datos.

Como bonus, agregue sanciones: si un socio devuelve tarde, queda bloqueado por la misma cantidad de dias de retraso.

## Persistencia CSV

Tambien agregue persistencia en CSV para recursos y socios.

## Main CLI

Se creo un `Main.java` como interfaz simple por consola.

El menu permite probar las operaciones principales del sistema:

- ejecutar una demo completa;
- registrar libros fisicos y e-books;
- registrar socios;
- buscar recursos;
- registrar prestamos;
- registrar devoluciones;
- ver historial;
- guardar y cargar datos CSV.

La logica importante no esta en el `Main`, sino en los services. El `Main` solo se encarga de pedir datos por consola y llamar a los metodos correspondientes.
