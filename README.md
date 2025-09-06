# MP_Project

## Estructura

### Mapa
```
app/
├── build.gradle                                 ← Dependencias
├── src/
│   └── main/
│       ├── java/com/example/torneofutbol/
│       │   ├── MainActivity.kt
│       │   ├── data/                            ← Clases y datos estaticos
│       │   │   ├── Equipo.kt
│       │   │   ├── Grupo.kt
│       │   │   ├── Jugador.kt
│       │   │   ├── Partido.kt
│       │   │   └── TorneoData.kt
│       │   └── ui/
│       │      ├── fragments/                    ← Pantallas
│       │      │   ├── FaseGruposFragment.kt
│       │      │   ├── BracketStageFragment.kt
│       │      │   ├── EquiposFragment.kt
│       │      │   ├── CalendarioFragment.kt
│       │      │   ├── JugadoresFragment.kt
│       │      │   └── PartidosFragment.kt
│       │      └── adapters/                     ← puente entre los datos y la interfaz visual
│       │         ├── GruposAdapter.kt
│       │         ├── EquiposAdapter.kt
│       │         ├── JugadoresAdapter.kt
│       │         ├── PartidosAdapter.kt
│       │         └── BracketAdapter.kt
│       │   
│       └── res/
│           ├── layout/                          ← cómo se ven las pantallas
│           │   ├── activity_main.xml
│           │   ├── fragment_fase_grupos.xml
│           │   ├── fragment_grupos_container.xml
│           │   ├── fragment_placeholder.xml
│           │   ├── item_grupo.xml
│           │   └── item_equipo.xml
│           ├── navigation/
│           │   └── nav_graph.xml                ← Navegación
│           ├── menu/
│           │   └── bottom_nav_menu.xml          ← Menú navegación inferior
│           ├── drawable/                        ← Refursos graficos
│           │   ├── ic_calendar.xml
│           │   ├── ic_teams.xml
│           │   ├── ic_schedule.xml
│           │   ├── ic_equipo_default.xml
│           │   ├── tab_selected_background.xml
│           │   └── tab_unselected_background.xml
│           ├── values/                          ← Colores, textos, temas del programa general
│           │   ├── colors.xml ← Colores
│           │   ├── strings.xml ← Textos
│           │   └── themes.xml ← Temas
│           └── color/
│               └── bottom_nav_color_selector.xml ← Selector colores (navegacion inferior)
```

### Desarrollo

#### 📁 Código Kotlin (java/com/example/torneofutbol/):

* MainActivity.kt - Actividad principal
* data/ - Modelos de datos y clases de datos
* ui/fragments/ - Todos los fragments (pantallas)
* ui/adapters/ - Adaptadores para RecyclerViews

#### 📁 Layouts (res/layout/):

* activity_main.xml - Layout principal con bottom navigation
* fragment_*.xml - Layout de cada fragment/pantalla
* item_*.xml - Layouts para items de RecyclerView

#### 📁 Navegación (res/navigation/):

* nav_graph.xml - Grafo de navegación entre fragments

#### 📁 Menús (res/menu/):

* bottom_nav_menu.xml - Menú de navegación inferior

#### 📁 Drawables (res/drawable/):

* ic_*.xml - Iconos vectoriales
* tab_*.xml - Backgrounds para pestañas superiores

#### 📁 Valores (res/values/):

* colors.xml - Definición de colores
* strings.xml - Textos de la aplicación
* themes.xml - Temas de la aplicación

#### 📁 Selectores de Color (res/color/):

* bottom_nav_color_selector.xml - Colores para navegación

#### 📁 Dependencias:

* build.gradle (Module: app) - Todas las dependencias de librerías

### Descripcion
#### Adapter
Un Adapter para RecyclerView es una clase que actúa como intermediario entre tus datos y la vista visual. Es como un "traductor" que convierte información en elementos visuales.
##### ¿Qué hace exactamente?
Imagina que tienes una lista de 1000 equipos de fútbol, pero tu pantalla solo puede mostrar 10 a la vez. El adapter:

* Toma tus datos (lista de equipos)
* Los convierte en vistas visuales (tarjetas con nombre, logo, etc.)
* Optimiza la memoria reutilizando vistas que salen de pantalla
* Maneja el scroll mostrando nuevos elementos según necesites

#### Layout
Un Layout es un archivo XML que define cómo se organizan y ven los elementos en una pantalla. Es como el "plano arquitectónico" de tu interfaz de usuario.

#### Drawable
Un Drawable es cualquier recurso gráfico que se puede "dibujar" en la pantalla. Son como los "elementos visuales" de tu app: íconos, formas, colores, efectos, etc.
