# Pizzatron

Juego de pizzeria de ritmo rapido inspirado en Club Penguin. El jugador prepara pizzas siguiendo ordenes de clientes en tiempo real antes de que se agote el tiempo.

## Requisitos

- Java 21 o superior
- Maven 3.8+
- Conexion a internet la primera vez (para descargar JavaFX via Maven)

## Como ejecutar

```bash
mvn javafx:run
```

> **Importante:** No ejecutes el `.jar` directamente. JavaFX 11+ requiere que el runtime se inyecte via Maven o con flags especiales de modulos. `mvn javafx:run` lo maneja automaticamente.

## Como compilar sin ejecutar

```bash
mvn compile
```

## Mecanica del juego

- Tienes **3 minutos** para entregar **20 pizzas**
- Maximo **5 errores** (pizza incorrecta o tiempo agotado)
- Orden obligatorio al armar una pizza: **Base → Salsa → Toppings**
- Cada receta tiene recompensa en monedas distinta
- Cada 15 segundos llega una nueva orden a la cola

## Controles

Toda la interaccion es con el mouse:

| Accion | Como |
|--------|------|
| Agregar ingrediente | Click en la ficha de la banda inferior |
| Deshacer ultimo ingrediente | Boton **DESHACER** |
| Enviar pizza al horno | Boton **HORNEAR** |

## Recetas disponibles

| Nombre | Base | Salsa | Toppings | Monedas |
|--------|------|-------|----------|---------|
| Clasica Pinguino | Clasica | Normal | Pescado | 40 |
| Especial del Mar | Clasica | Picante | Camarones, Alga | 60 |
| Doble Calamar | Integral | Normal | Calamar x2 | 55 |
| Mar Helado | Clasica | Picante | Pescado, Hielo | 65 |
| Alga Fusion | Integral | Normal | Alga, Camarones, Hielo | 75 |

## Estructura del proyecto

```
src/main/java/pizzeria/
├── controller/
│   ├── JuegoController.java        ← unico punto de contacto con la vista
│   ├── CocinaController.java       ← logica de armado de pizza (Stack)
│   ├── CajaController.java         ← cola de ordenes (Queue FIFO)
│   ├── InventarioController.java   ← stock de ingredientes
│   ├── PuntajeController.java      ← monedas, errores, tiempo global
│   └── PuntajeSnapshot.java        ← estado inmutable del puntaje
├── model/
│   ├── Pizza.java
│   ├── Receta.java
│   ├── Orden.java
│   ├── enums/                      ← EstadoJuego, EstadoCocina, TipoMasa, etc.
│   └── ingredient/                 ← Ingrediente, Base, Salsa, Topping y subclases
├── interfaces/
│   ├── ICocinable.java
│   └── IValidable.java
├── util/
│   ├── RecetaFactory.java          ← definicion de todas las recetas
│   └── GeneradorOrdenes.java       ← generacion aleatoria de ordenes
└── view/
    ├── MainApp.java                ← entrada JavaFX, gestiona navegacion entre pantallas
    ├── MenuView.java               ← pantalla de inicio
    ├── GameView.java               ← pantalla de juego (Timeline de 1s = 1 tick)
    ├── GameOverView.java           ← pantalla de resultados
    ├── assets/
    │   └── AssetManager.java       ← carga imagenes PNG o devuelve placeholder
    └── components/
        ├── PuntajePanel.java       ← barra superior: tiempo, errores, monedas
        ├── OrdenPanel.java         ← panel izquierdo: cola de ordenes con countdown
        ├── CocinaPanel.java        ← panel central: capas de pizza + botones
        └── BandaIngredientes.java  ← barra inferior: fichas clickeables por tipo
```

## Agregar imagenes propias

Coloca archivos PNG en:

```
src/main/resources/assets/ingredientes/
```

El nombre del archivo debe ser el nombre del ingrediente en minusculas con espacios reemplazados por `_` y sin tildes. Ejemplos:

| Ingrediente | Archivo |
|-------------|---------|
| Masa Clasica | `masa_clasica.png` |
| Salsa Picante | `salsa_picante.png` |
| Pescado | `pescado.png` |
| Camarones | `camarones.png` |

Si el archivo no existe, `AssetManager` muestra automaticamente un placeholder de color representativo. No es necesario tener todas las imagenes para que el juego funcione.

## Agregar nuevas recetas

Solo modifica `RecetaFactory.java`:

```java
public static Receta miNuevaReceta() {
    List<Topping> toppings = new ArrayList<>();
    toppings.add(new Pescado());
    toppings.add(new Alga());

    return new Receta(
        "Mi Receta",
        new Base(TipoMasa.CLASICA),
        new Salsa(TipoSalsa.NORMAL),
        toppings,
        50,  // monedas de recompensa
        5    // tiempo de coccion
    );
}
```

Y agregala a `todasLasRecetas()`. No hay que tocar ninguna otra clase.
