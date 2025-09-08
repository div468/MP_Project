# MP_Project

## Estructura

### Mapa
```
app/
├── build.gradle.kts                             ← Dependencias
├── src/
│   └── main/
│       ├── java/com/example/dragonstats/
│       │   ├── MainActivity.kt                  ← Activity principal (ComponentActivity + Compose)
│       │   │
│       │   ├── data/                            ← Clases y datos estáticos
│       │   │   ├── Equipo.kt                   ← Modelo de equipo
│       │   │   ├── Grupo.kt                    ← Modelo de grupo
│       │   │   ├── Jugador.kt                  ← Modelo de jugador
│       │   │   ├── Partido.kt                  ← Modelo de partido
│       │   │   └── TorneoData.kt               ← Datos del torneo
│       │   │
│       │   └── ui/
│       │       ├── components/                  ← Componentes reutilizables
│       │       │   └── BottomNavigationBar.kt  ← Navegación inferior en Compose
│       │       │
│       │       ├── navigation/                  ← Sistema de navegación
│       │       │   └── AppNavHost.kt           ← Configuración Navigation Compose
│       │       │
│       │       ├── screens/                     ← Pantallas principales
│       │       │   ├── CalendarioScreen.kt     ← Pantalla calendario
│       │       │   ├── EquiposScreen.kt        ← Pantalla equipos
│       │       │   ├── GruposScreen.kt         ← Pantalla grupos (con tabs)
│       │       │   │
│       │       │   └── tabs/                   ← Tabs de la pantalla grupos
│       │       │       ├── FaseGruposTab.kt    ← Tab fase de grupos
│       │       │       └── BracketStageTab.kt  ← Tab bracket stage
│       │       │
│       │       └── theme/                       ← Temas y estilos
│       │           ├── Color.kt                ← Colores
│       │           ├── Theme.kt                ← Tema Material3 + DragonStats
│       │           └── Type.kt                 ← Tipografía
│       │
│       └── res/
│           ├── drawable/                        ← Recursos gráficos
│           │   ├── ic_calendar.xml             ← Icono calendario
│           │   ├── ic_teams.xml                ← Icono equipos
│           │   ├── ic_schedule.xml             ← Icono grupos/horarios
│           │   └── ic_equipo_default.xml       ← Icono por defecto equipos
│           │
│           └── values/                          ← Valores de configuración
│               ├── strings.xml                 ← Textos de la aplicación
│               └── themes.xml                  
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
