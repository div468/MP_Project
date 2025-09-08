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

### 📱 Desarrollo - Jetpack Compose
#### 🎯 Código Kotlin (java/com/example/dragonstats/)
##### MainActivity.kt

* Compose Activity: Extiende ComponentActivity
* Setup UI: Usa setContent para configurar interfaz
* Theme Integration: Integra DragonStatsTheme automáticamente

#### data/

* Equipo.kt: Modelo de datos del equipo
* Grupo.kt: Modelo de datos del grupo
* TorneoData.kt: Datos estáticos con equipos realistas y ordenamiento

#### ui/components/

* BottomNavigationBar.kt: Navegación inferior completamente en Compose
* Componentes reutilizables para toda la app

#### ui/navigation/

* AppNavHost.kt: Configuración de navegación con Navigation Compose
* Manejo de rutas y transiciones entre pantallas

#### ui/screens/

* CalendarioScreen.kt: Pantalla principal de calendario (placeholder)
* EquiposScreen.kt: Pantalla de equipos (placeholder)
* GruposScreen.kt: Pantalla de grupos con sistema de tabs

#### ui/screens/tabs/

* FaseGruposTab.kt: Tabla completa de grupos con: Cards por grupo ordenadas por puntos Colores por posición (🟢 clasifican, 🟠 peligro, 🔴 eliminados)

* BracketStageTab.kt: Tab para fase de eliminatorias (placeholder)

#### ui/theme/

* Theme.kt: Temas personalizados
* Color.kt: Definiciones de colores
* Type.kt: Configuración de tipografía
