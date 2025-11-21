# Dragon Stats ⚽

Aplicación Android moderna para gestión y visualización de estadísticas de torneos de fútbol. Construida con Jetpack Compose, Firebase y arquitectura MVVM.

## 📱 Características Principales

- **Calendario Completo**: Navegación entre jornadas con visualización de resultados en tiempo real
- **Gestión de Grupos**: Tablas de posiciones y bracket de eliminatorias interactivo
- **Equipos y Jugadores**: Sistema de favoritos, ordenamiento múltiple y estadísticas detalladas
- **Estadísticas Avanzadas**: Top goleadores por fase, máximos goleadores por equipo
- **Tema Personalizable**: Modo oscuro/claro con persistencia en DataStore
- **Detalles de Partidos**: Eventos en tiempo real (goles, tarjetas amarillas/rojas)

## Estructura del Proyecto

### Mapa
```
app/
├── build.gradle.kts                                    ← Dependencias (Firebase, Compose, etc.)
├── google-services.json                                ← Configuración Firebase
├── src/
│   └── main/
│       ├── java/com/example/dragonstats/
│       │   ├── MainActivity.kt                         ← Activity principal (Compose + Theme)
│       │   │
│       │   ├── data/
│       │   │   ├── local/
│       │   │   │   └── FavoritosDataStore.kt          ← Manejo de favoritos (DataStore)
│       │   │   │
│       │   │   ├── model/                              ← Modelos de datos
│       │   │   │   ├── CalendarioData.kt              ← Datos de calendario y eventos
│       │   │   │   ├── Encuentro.kt                   ← Modelo de encuentro/partido
│       │   │   │   ├── Equipo.kt                      ← Modelo de equipo (Parcelable)
│       │   │   │   ├── Goleador.kt                    ← Modelo de goleador y estadísticas
│       │   │   │   ├── Grupo.kt                       ← Modelo de grupo
│       │   │   │   ├── Jugador.kt                     ← Modelo de jugador (Parcelable)
│       │   │   │   ├── Match.kt                       ← Modelo auxiliar de partido
│       │   │   │   ├── Round.kt                       ← Modelo de ronda/fase
│       │   │   │   └── TorneoData.kt                  ← Datos estáticos del torneo
│       │   │   │
│       │   │   └── repository/                         ← Repositorios (conexión Firebase)
│       │   │       ├── EncuentroRepository.kt         ← Obtención de partidos desde Firestore
│       │   │       ├── EquipoRepository.kt            ← Obtención de equipos desde Firestore
│       │   │       └── EstadisticasRepository.kt      ← Obtención de estadísticas desde Firestore
│       │   │
│       │   ├── ui/
│       │   │   ├── components/                         ← Componentes reutilizables
│       │   │   │   ├── BottomNavigationBar.kt         ← Navegación inferior (4 tabs)
│       │   │   │   └── TopAppBarWithTheme.kt          ← AppBar con toggle de tema
│       │   │   │
│       │   │   ├── navigation/                         ← Sistema de navegación
│       │   │   │   └── AppNavHost.kt                  ← Navigation Compose + rutas
│       │   │   │
│       │   │   ├── screens/                            ← Pantallas principales
│       │   │   │   ├── CalendarioScreen.kt            ← Calendario con tabs de jornadas
│       │   │   │   ├── EquiposScreen.kt               ← Lista de equipos con favoritos
│       │   │   │   ├── EstadisticasScreen.kt          ← Top goleadores (grupos y finales)
│       │   │   │   ├── GruposScreen.kt                ← Grupos con 2 tabs (grupos/bracket)
│       │   │   │   ├── ListadoScreen.kt               ← Detalle de jugadores de equipo
│       │   │   │   ├── PartidoScreen.kt               ← Detalle de partido con eventos
│       │   │   │   │
│       │   │   │   └── tabs/                          ← Tabs de GruposScreen
│       │   │   │       ├── FaseGruposTab.kt           ← Tablas por grupo (horizontal scroll)
│       │   │   │       └── BracketStageTab.kt         ← Bracket interactivo de eliminatorias
│       │   │   │
│       │   │   ├── theme/                              ← Temas y estilos
│       │   │   │   ├── Color.kt                       ← Paleta de colores (verde principal)
│       │   │   │   ├── Theme.kt                       ← Tema Material3 (Light/Dark)
│       │   │   │   ├── ThemeManager.kt                ← Gestión de tema con DataStore
│       │   │   │   ├── ThemeViewModel.kt              ← ViewModel para tema
│       │   │   │   └── Type.kt                        ← Tipografía
│       │   │   │
│       │   │   └── viewmodel/                          ← ViewModels (MVVM)
│       │   │       ├── CalendarioViewModel.kt         ← Lógica de calendario
│       │   │       ├── EquiposListadoViewModel.kt     ← Lógica de equipos + favoritos
│       │   │       ├── EstadisticasViewModel.kt       ← Lógica de estadísticas
│       │   │       ├── GruposViewModel.kt             ← Lógica de grupos + bracket
│       │   │       └── PartidoViewModel.kt            ← Lógica de detalles de partido
│       │   │
│       │   └── utils/
│       │       └── EquipoLogoHelper.kt                 ← Mapeo de nombres a recursos de logos
│       │
│       └── res/
│           ├── drawable/                               ← Recursos gráficos
│           │   ├── ic_calendar.xml                    ← Icono calendario
│           │   ├── ic_teams.xml                       ← Icono equipos
│           │   ├── ic_schedule.xml                    ← Icono grupos
│           │   ├── ic_stats.xml                       ← Icono estadísticas
│           │   ├── ic_favorite_screen.xml             ← Icono favorito vacío
│           │   ├── ic_favoritefilled.xml              ← Icono favorito lleno
│           │   ├── ic_dark_mode.xml                   ← Icono modo oscuro
│           │   ├── ic_light_mode.xml                  ← Icono modo claro
│           │   ├── ic_equipo_default.xml              ← Icono por defecto de equipo
│           │   └── [logos de equipos].png             ← Logos individuales (25 equipos)
│           │
│           └── values/
│               ├── strings.xml                        ← Textos de la aplicación
│               └── themes.xml                         ← Configuración de temas base
```

---

## 🛠️ Tecnologías Utilizadas

### Frontend
- **Kotlin** - Lenguaje principal
- **Jetpack Compose** - UI declarativa moderna
- **Material Design 3** - Sistema de diseño
- **Navigation Compose** - Navegación entre pantallas
- **ViewModel & StateFlow** - Arquitectura MVVM reactiva
- **Coroutines** - Programación asíncrona

### Backend & Storage
- **Firebase Firestore** - Base de datos en tiempo real
- **DataStore** - Almacenamiento de preferencias (favoritos, tema)
- **Repository Pattern** - Separación de lógica de datos

### Arquitectura
- **MVVM** (Model-View-ViewModel)
- **Single Activity** con Compose
- **State Management** con StateFlow
- **Navigation Graph** compartido con ViewModels

---

## 📋 Desarrollo - Componentes Detallados

### 🎯 MainActivity.kt
- ComponentActivity con Compose
- Integración de ThemeViewModel
- Setup de DragonStatsTheme con modo oscuro/claro
- Scaffold con TopBar y BottomNavigation

### 📦 data/model/
**Encuentro.kt**
- Modelo completo de partido
- Propiedades: equipos, resultado, penales, eventos, jornada
- Property computada `tieneResultado`

**Equipo.kt**
- Modelo Parcelable para navegación
- Estadísticas completas: puntos, ganados, empatados, perdidos
- Lista de jugadores integrada
- Properties computadas: `partidos`, `golDiferencia`

**Goleador.kt**
- Modelo de goleador individual
- EstadisticasData: wrapper para múltiples estadísticas

**Jugador.kt**
- Modelo Parcelable de jugador
- Propiedades: nombre, goles, asistencias, posición

### 🗄️ data/repository/
**EncuentroRepository.kt**
- Conexión con Firestore (tournaments/2025/jornadas)
- Métodos: `getEncuentrosPorJornada()`, `getEncuentroPorId()`, `getBracketMatches()`
- Parser de eventos (goles, tarjetas)
- Manejo de fases: jornadas regulares + cuartos + semis + final

**EquipoRepository.kt**
- Conexión con Firestore (tournaments/2025/groups)
- Métodos: `getGrupos()`, `getEquiposOrdenados()`
- Carga de jugadores por equipo
- Ordenamiento automático por puntos y diferencia de goles

**EstadisticasRepository.kt**
- Métodos: `getEstadisticas()`
- Cálculo de top goleadores por fase
- Máximo goleador por equipo
- Procesamiento de eventos de fase final

### 🎨 ui/screens/
**CalendarioScreen.kt**
- LazyRow de tabs de jornadas + fases finales
- LazyColumn de encuentros por jornada seleccionada
- Estados: Loading, Success, Error con retry
- EncuentroCard con logos de equipos
- Navegación a detalles de partido

**EquiposScreen.kt**
- Sistema de favoritos con DataStore
- DropdownMenu de ordenamiento (5 opciones)
- Estados: Loading, Success, Error
- EquipoCard con botón "Ver Jugadores"
- Toggle de favoritos con persistencia

**EstadisticasScreen.kt**
- 3 secciones: Max goleador por equipo, Top 10 grupos, Top 10 finales
- Secciones expandibles (mostrar 3 o todos)
- GoleadorCard con medallas (oro, plata, bronce)
- Estados: Loading, Success, Error

**GruposScreen.kt**
- Sistema de tabs custom (Fase Grupos / Bracket Stage)
- ViewModel compartido entre tabs
- Tab selection persistente

**ListadoScreen.kt**
- Header con datos del equipo y logo
- Toggle de favoritos
- Tabla scrolleable de jugadores
- Columnas: Nombre, Goles, Asistencias, Posición

**PartidoScreen.kt**
- Header con resultado y logos
- Marcador de penales (si aplica)
- Lista de eventos ordenados por minuto
- EventIcon diferenciado (⚽ gol, 🟨 amarilla, 🟥 roja)
- Nombres acortados de jugadores

### 📑 ui/screens/tabs/
**FaseGruposTab.kt**
- Cards por grupo con tabla horizontal scrolleable
- Columnas: Equipo, J, G, E, P, GF, GC, DG, PTS
- Colores diferenciados por posición
- Logos de equipos en cada fila
- Estados: Loading, Success, Error

**BracketStageTab.kt**
- Diseño de bracket vertical
- 4 cuartos → 2 semis → 1 final
- Trofeo en partido final
- EncuentroCard clickeable para ver detalles
- CenteredMatchRow con LazyRow

### 🎯 ui/viewmodel/
**CalendarioViewModel**
- StateFlow<CalendarioUiState>
- Carga de encuentros por jornada
- Manejo de totalJornadas dinámico
- Retry en caso de error

**EquiposListadoViewModel**
- StateFlow<EquiposUiState>
- StateFlow<Set<String>> para favoritos
- StateFlow<TipoOrden> para tipo de ordenamiento
- Métodos: `toggleFavorito()`, `cambiardeOrden()`
- Reordenamiento reactivo automático

**EstadisticasViewModel**
- StateFlow<EstadisticasUiState>
- Carga de estadísticas desde repository
- Retry en caso de error

**GruposViewModel**
- StateFlow<GruposUiState>
- StateFlow<GruposTab> para tab seleccionado
- Carga paralela de grupos y bracket (async/await)
- Método: `onTabSelected()`

**PartidoViewModel**
- StateFlow<PartidoUiState>
- Carga dinámica por matchId
- Método: `loadPartido(matchId)`

### 🎨 ui/theme/
**Theme.kt**
- DarkColorScheme: Negro con verde primario
- LightColorScheme: Blanco con verde primario
- StatusBar color adaptativo
- SideEffect para cambio de tema

**ThemeManager.kt**
- DataStore para persistencia de tema
- Flow reactivo de isDarkMode
- Método: `toggleTheme()`

**ThemeViewModel.kt**
- AndroidViewModel con ThemeManager
- StateFlow<Boolean> para isDarkMode
- Carga automática de preferencia guardada

### 🔧 utils/
**EquipoLogoHelper.kt**
- Mapeo de nombres de equipos a recursos drawable
- Método: `getLogoResource(nombreEquipo): Int`
- Método: `getInitials(nombreEquipo): String`
- Soporte para 25 equipos + logo default

---

## 🔥 Firebase - Estructura Firestore
```
tournaments/
  └── 2025/
      ├── groups/
      │   ├── A/
      │   │   └── teams/
      │   │       ├── [teamDoc]/
      │   │       │   ├── nombre: String
      │   │       │   ├── puntos: Number
      │   │       │   ├── ganados: Number
      │   │       │   ├── empatados: Number
      │   │       │   ├── perdidos: Number
      │   │       │   ├── golesFavor: Number
      │   │       │   ├── golesContra: Number
      │   │       │   ├── logoUrl: String
      │   │       │   └── jugadores: Array<Map>
      │   │       │       ├── id: Number
      │   │       │       ├── nombre: String
      │   │       │       ├── goles: Number
      │   │       │       ├── asistencias: Number
      │   │       │       └── posicion: String
      │   └── [B, C, D]...
      │
      └── jornadas/
          ├── Jornada 1/
          │   └── encuentros: Array<Map>
          │       ├── equipo1_id: String
          │       ├── equipo2_id: String
          │       ├── date: String
          │       ├── hora: String
          │       ├── goles1: Number
          │       ├── goles2: Number
          │       ├── grupo: String
          │       └── events: Array<Map>
          │           ├── minute: Number
          │           ├── player: String
          │           ├── eventType: String (goal/yellow/red)
          │           └── team: String
          ├── Jornada 2/
          ├── [...]/
          ├── Cuartos de final/
          ├── Semifinales/
          └── Final/
```

---

## 🚀 Instalación y Configuración

### Requisitos
- Android Studio Hedgehog o superior
- JDK 17+
- SDK mínimo: Android 7.0 (API 24)
- SDK objetivo: Android 14 (API 34)

### Configuración Firebase
1. Crear proyecto en [Firebase Console](https://console.firebase.google.com/)
2. Agregar aplicación Android con package name: `com.example.dragonstats`
3. Descargar `google-services.json` y colocarlo en `app/`
4. Configurar Firestore según estructura indicada arriba

---

## 👥 Autores

Desarrollado por estudiantes de 4to semestre de Ingeniería en Ciencias de la Computación y TI - Universidad del Valle de Guatemala.

Marcelo Detlefsen - 24554

Julián Divas - 24687

Alejándro Jeréz - 24678

Jackeline Girón - 24737

Sergio Tan - 24759

---