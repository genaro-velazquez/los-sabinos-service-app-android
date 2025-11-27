# 🔧 Los Sabinos - Sistema de Gestión de Servicios de Mantenimiento

Aplicación Android nativa para gestionar servicios de mantenimiento con funcionalidad offline-first, captura de evidencia, sincronización automática de datos y validación automática de sesiones.

---

## 📋 Tabla de Contenidos

- [Características](#características)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Arquitectura](#arquitectura)
- [Tecnologías](#tecnologías)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Flujo de Autenticación](#flujo-de-autenticación)
- [ActionCards - Acciones Rápidas](#actioncards---acciones-rápidas-✨-nuevo)
- [Estado del Proyecto](#estado-del-proyecto)
- [Backend Integration](#backend-integration)
- [Debugging & Logging](#debugging--logging)
- [Cómo Ejecutar](#cómo-ejecutar)
- [Flujo Principal](#flujo-principal)

---

## ✨ Características

- ✅ **Autenticación** con correo y contraseña (validaciones cliente y servidor)
- ✅ **Integración con backend Azure** para autenticación
- ✅ **JSON API** con body serializado (Content-Type: application/json)
- ✅ **Validación automática de sesión** con SplashScreen
- ✅ **Respeto de sesiones guardadas** - Si usuario logado, va directo a Home
- ✅ **Modal de confirmación** elegante para logout
- ✅ **Logout seguro** con limpieza completa de datos
- ✅ **Datos reales del usuario** en HomePage (nombre, ubicación)
- ✅ **ActionCards** - Tarjetas de acciones rápidas (Cámara, Reportes, Ubicación) ✨ NUEVO
- ✅ **Logging de peticiones CURL** para debugging
- ✅ **Indicadores y métricas** en pantalla Home
- ✅ **Escaneo de códigos de barras/QR** para asignar servicios
- ✅ **Panel de tareas** con checklist interactivo
- ✅ **Captura de evidencia** (imágenes con cámara)
- ✅ **Offline-First** con sincronización automática
- ✅ **Inyección de dependencias con Hilt**
- ✅ **Atomic Design** para componentes UI reutilizables
- ✅ **Manejo robusto de errores y reintentos**
- ✅ **UI moderna** con Jetpack Compose

---

## 🔧 Requisitos

### Mínimos del Sistema
- **Android Studio** 2023.1 o superior
- **JDK 17** o superior
- **Kotlin** 2.2.21 o superior
- **Android SDK** API 26+ (Android 8.0 Oreo)
- **Gradle** 8.0+

### Recomendado
- Dispositivo/Emulador con Android 10.0 (API 29) o superior
- 4GB RAM disponible
- Git instalado

---

## 📦 Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/genaro-velazquez/los-sabinos-service-app-android.git
cd los-sabinos-service-app-android
```

### 2. Abrir en Android Studio

```bash
# Opción A: Desde terminal
android-studio . &

# Opción B: Manualmente
# 1. Abre Android Studio
# 2. Selecciona "Open an Existing Project"
# 3. Navega a la carpeta del proyecto
# 4. Espera a que Gradle sincronice automáticamente
```

### 3. Sincronizar Gradle

```bash
./gradlew clean
./gradlew build
```

### 4. Ejecutar en Emulador/Dispositivo

```bash
# Opción A: Desde Android Studio
# Presiona Shift + F10 o Run → Run 'app'

# Opción B: Desde terminal
./gradlew installDebug
```

---

## 🏗️ Arquitectura

### Clean Architecture + MVVM + Repository Pattern + Hilt DI

```
┌────────────────────────────────────────┐
│   PRESENTATION (UI/ViewModel)          │  ← Usuario interactúa
│   (Screens, Components, ViewModels)    │
└────────────────────────────────────────┘
                   ↕
┌────────────────────────────────────────┐
│   DOMAIN (Lógica de Negocio)           │  ← UseCases, Interfaces
│   (UseCases, Modelos, Repositorios)    │
└────────────────────────────────────────┘
                   ↕
┌────────────────────────────────────────┐
│   DATA (Fuentes de Datos)              │  ← API, BD Local
│   (Repositories, DTOs, Entities)       │
└────────────────────────────────────────┘
```

#### **Flujo de Datos:**

```
User Interaction (Tap, Type)
        ↓
   ViewModel (observa State con Flow)
        ↓
   UseCase (lógica de negocio)
        ↓
   Repository (abstracción de datos)
        ↓
   Remote/Local Data Sources
        ↓
   Retorna datos → ViewModel → UI se actualiza
```

#### **Inyección de Dependencias (Hilt):**

```
@HiltAndroidApp
LosSabinosApplication
        ↓
    Módulos Hilt (5):
    ├── AppModule (Context)
    ├── NetworkModule (Retrofit, OkHttp, API)
    ├── SharedPreferencesModule (Storage)
    ├── RepositoryModule (Repositories)
    └── UseCaseModule (Use Cases)
        ↓
   @HiltViewModel / @AndroidEntryPoint
        ↓
   Inyección automática de dependencias
```

---

## 🎨 Atomic Design

La aplicación usa **Atomic Design** para componentes UI reutilizables:

```
ATOMS (12)              → Elementos básicos
├── Avatar, MetricIcon, StatusBadge
├── ActionButton, PrimaryButton, SecondaryButton
├── StatusText, ModalTitle, ModalContent
├── ActionIcon ✨ NUEVO
├── ActionTitle ✨ NUEVO
└── ActionCardContainer ✨ NUEVO
    ↓
MOLECULES (6)         → Componentes simples
├── UserHeader, MetricCard, StatusSection
├── UnsyncSection, ModalButtonGroup
└── ActionCard ✨ NUEVO
    ↓
ORGANISMS (5)         → Componentes complejos
├── HomeHeaderSection, MetricsSection
├── SyncSection, ConfirmationDialog
└── ActionCardsSection ✨ NUEVO
    ↓
TEMPLATES (2)         → Layout sin datos
├── LoginTemplate
└── HomeTemplate ✨ ACTUALIZADO
    ↓
PAGES (3)             → Pantallas completas
├── SplashScreen
├── LoginScreen
└── HomePage ✨ ACTUALIZADO
```

---

## 🔐 Flujo de Autenticación

### 1️⃣ Inicio de la Aplicación - SplashScreen

```
App inicia en MainActivity
    ↓
NavGraph inicia con startDestination = Routes.SPLASH
    ↓
SplashScreen se muestra (spinner de carga)
    ↓
SplashViewModel ejecuta validateSession()
    ↓
GetUserPreferencesUseCase.getIsLogged() 
    ↓ (valida sesión guardada)
    ↓
┌─────────────────────────────┐
│ ¿Usuario está logado?       │
├─────────────────────────────┤
│ SÍ  → Navega a HomePage     │
│ NO  → Navega a LoginScreen  │
└─────────────────────────────┘
```

### 2️⃣ Proceso de Login

```
LoginScreen aparece
    ↓ (usuario ingresa credenciales)
    ↓
LoginViewModel.onEvent(LoginEvent.LoginClicked)
    ↓
validateForm() → Valida campos (email, password)
    ↓
EmailPasswordLoginUseCase.execute(email, password)
    ↓
┌──────────────────────────────┐
│ ¿Credenciales son válidas?   │
├──────────────────────────────┤
│ SÍ  → Guardar token/sesión   │
│       NavigateToHome         │
│ NO  → Mostrar errorMessage   │
└──────────────────────────────┘
    ↓
HomePage se muestra con datos del usuario
```

### 3️⃣ Proceso de Logout - Modal de Confirmación

```
HomePage se muestra
    ↓ (usuario presiona botón logout)
    ↓
HomeViewModel.onEvent(HomeEvent.LogoutClicked)
    ↓
state.showLogoutDialog = true
    ↓
ConfirmationDialog se muestra (modal elegante)
    ↓ (usuario presiona "Cerrar Sesión" o "Cancelar")
    ↓
┌──────────────────────────────────┐
│ ¿Qué presionó?                   │
├──────────────────────────────────┤
│ Cerrar Sesión:                   │
│ → ConfirmLogout event            │
│ → GetUserPreferencesUseCase      │
│    .clear() (limpia sesión)      │
│ → Limpiar datos locales          │
│ → NavigateToLogin                │
│                                  │
│ Cancelar:                        │
│ → CancelLogout event             │
│ → Cerrar modal                   │
│ → Seguir en HomePage             │
└──────────────────────────────────┘
    ↓
LoginScreen o HomePage
```

### 4️⃣ Respeto de Sesiones Guardadas

```
Usuario logado ayer
Cierra la app (en HomePage)
    ↓
Al día siguiente...
App inicia
    ↓
SplashScreen
    ↓
GetUserPreferencesUseCase.getIsLogged() 
    ↓
Sesión guardada existe (token válido)
    ↓
Navega a HomePage (automático)
    ↓
Usuario ve HomePage SIN hacer login de nuevo
```

---

## 🎯 ActionCards - Acciones Rápidas ✨ NUEVO

### Descripción

ActionCards son tarjetas de acciones rápidas que aparecen en el centro de la pantalla Home (entre Sync y Metrics). Permiten al usuario acceder rápidamente a funcionalidades principales.

### Estructura

```
┌────────────────────────────────────┐
│ ACCIONES RÁPIDAS                   │
├────────────────────────────────────┤
│  [🎥 Cámara] [📊 Reportes] [📍 Ubicación] │
└────────────────────────────────────┘
```

### Componentes (Atomic Design)

#### **Atoms** (3) ✨ NUEVO
- **ActionIcon.kt** - Icono circular con fondo primario (56dp)
- **ActionTitle.kt** - Texto centrado del título
- **ActionCardContainer.kt** - Card base con esquinas redondeadas

#### **Molecule** (1) ✨ NUEVO
- **ActionCard.kt** - Combina Icon + Title, clickeable

#### **Organism** (1) ✨ NUEVO
- **ActionCardsSection.kt** - Grid responsivo (2-4 columnas)
  - Usa LazyVerticalGrid con altura definida (150.dp)
  - Espaciado compacto (5.dp)
  - Título opcional
  - Callbacks para clicks

#### **Template** (1) ✨ ACTUALIZADO
- **HomeTemplate.kt**
  - Parámetro: `actionsSection: @Composable (() -> Unit)? = null`
  - Orden: Header → Sync → Actions → Metrics
  - Espaciado configurable

#### **Page** (1) ✨ ACTUALIZADO
- **HomePage.kt**
  - Crea lista de ActionCardModel
  - Configura callbacks
  - Pasa actionsSection a HomeTemplate

### Uso en HomePage

```kotlin
// Definir acciones
val actionCards = listOf(
    ActionCardModel(
        id = "camera",
        title = "Cámara",
        icon = Icons.Filled.Camera,
        onClick = onCameraClick
    ),
    ActionCardModel(
        id = "reports",
        title = "Reportes",
        icon = Icons.Filled.BarChart,
        onClick = onReportsClick
    ),
    ActionCardModel(
        id = "location",
        title = "Ubicación",
        icon = Icons.Filled.LocationOn,
        onClick = onLocationClick
    )
)

// Pasar a HomeTemplate
HomeTemplate(
    headerSection = { ... },
    syncSection = { ... },
    actionsSection = {
        ActionCardsSection(
            actions = actionCards,
            title = "Acciones Rápidas",
            onActionClick = { actionId ->
                // Navegar según actionId
            },
            columns = 3
        )
    },
    metricsSection = { ... }
)
```

### Problemas y Soluciones

#### ⚠️ LazyVerticalGrid Crashing Sin Altura

**Problema:** LazyVerticalGrid sin `.height()` intenta ocupar altura infinita
**Solución:** Agregar `.height(150.dp)` explícitamente

```kotlin
// ❌ INCORRECTO - Crashea
LazyVerticalGrid(
    columns = GridCells.Fixed(columns),
    modifier = Modifier.fillMaxWidth()  // Sin altura
)

// ✅ CORRECTO - Funciona
LazyVerticalGrid(
    columns = GridCells.Fixed(columns),
    modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)  // Altura definida
)
```

#### ⚠️ MaterialTheme vs LosabiosTheme

**Problema:** `LosabiosTheme.colorScheme` no existe (es una función, no una clase)
**Solución:** Usar `MaterialTheme.colorScheme` dentro de @Composable

```kotlin
// ❌ INCORRECTO
color = LosabiosTheme.colorScheme.primary  // Error

// ✅ CORRECTO
color = MaterialTheme.colorScheme.primary  // Funciona
```

### Características

- ✅ Grid responsivo (2, 3, 4+ columnas configurable)
- ✅ Altura fija (150.dp por defecto, configurable)
- ✅ Espaciado compacto (5.dp entre tarjetas)
- ✅ Título opcional
- ✅ Callbacks para manejo de clicks
- ✅ Integrado perfectamente en HomeTemplate
- ✅ Uso de MaterialTheme para consistencia
- ✅ Atomic Design pattern

---

## 📁 Estructura del Proyecto

```
app/src/main/java/com/lossabinos/serviceapp/
│
├── presentation/ui/components/
│   ├── atoms/
│   │   ├── ActionIcon.kt           ✨ NUEVO
│   │   ├── ActionTitle.kt          ✨ NUEVO
│   │   └── ActionCardContainer.kt  ✨ NUEVO
│   ├── molecules/
│   │   └── ActionCard.kt           ✨ NUEVO
│   └── organisms/
│       └── ActionCardsSection.kt   ✨ NUEVO
│
├── presentation/ui/templates/
│   └── HomeTemplate.kt             ✨ ACTUALIZADO
│
├── presentation/screens/home/
│   └── HomePage.kt                 ✨ ACTUALIZADO
│
└── (... resto de estructura igual ...)
```

---

## 🛠️ Tecnologías

### UI & Composables
- **Jetpack Compose** - UI declarativa moderna
- **Material Design 3** - Componentes estándar
- **Compose Navigation** - Navegación entre pantallas

### Inyección de Dependencias
- **Hilt** - DI framework (✅ INTEGRADO)

### Networking
- **Retrofit** - Cliente HTTP (✅ INTEGRADO)
- **OkHttp** - Interceptores y logging (✅ INTEGRADO)
- **Gson** - Serialización JSON (✅ INTEGRADO)

### Almacenamiento Local
- **SharedPreferences** - Preferencias de usuario (✅ INTEGRADO)
- **Room** - BD local SQLite (próximo)

### Concurrencia
- **Kotlin Coroutines** - Operaciones asincrónicas (✅ INTEGRADO)
- **Flow** - Streams reactivos (✅ INTEGRADO)

### Cámara y Escaneo
- **CameraX** - API moderna para cámara (próximo)
- **ML Kit Barcode Scanning** - Escaneo de códigos (próximo)

---

## 📊 Estado del Proyecto

### ✅ v1.3.0 (Completado) - ActionCards Integration ✨ NUEVO

#### ActionCards Module
- [x] ActionIcon.kt - Icono circular con fondo primario
- [x] ActionTitle.kt - Título centrado
- [x] ActionCardContainer.kt - Card base
- [x] ActionCard.kt - Combinación Icon + Title
- [x] ActionCardsSection.kt - Grid responsivo con LazyVerticalGrid
- [x] HomeTemplate.kt actualizado con actionsSection
- [x] HomePage.kt actualizado para pasar ActionCards
- [x] **SOLUCIÓN: LazyVerticalGrid con .height(150.dp)**
- [x] **SOLUCIÓN: Usar MaterialTheme en lugar de LosabiosTheme**
- [x] Espaciado compacto (5.dp entre tarjetas)
- [x] Grid configurable (2, 3, 4 columnas)
- [x] Título opcional en ActionCardsSection

### 🚧 v1.4.0 (Próximo)

#### Conexión ActionCards
- [ ] Conectar callbacks para navegar
- [ ] Crear CameraScreen
- [ ] Crear ReportsScreen
- [ ] Crear LocationScreen

#### Room Database
- [ ] Crear entidades de datos
- [ ] Implementar DAOs
- [ ] Configurar AppDatabase

---

## 🧪 Testing

### Credenciales de Prueba

```
Email:    henry@lossabinos.como.mx
Password: Lossabinos123456789!
```

### Escenarios a Probar

#### ✅ ActionCards Interacción ✨ NUEVO
```
HomePage aparece → ActionCards visible (entre Sync y Metrics)
                 → Grid de 3 columnas con 3 tarjetas
                 → Presionar cualquier tarjeta
                 → Callback ejecuta correctamente
```

---

## 🚀 Cómo Ejecutar

```bash
# 1. Sincronizar Gradle
./gradlew clean build

# 2. Ejecutar en emulador
./gradlew installDebug

# O en Android Studio:
# Shift + F10 o Run → Run 'app'
```

---

## 📊 Métricas del Proyecto

- **Componentes Atomic Design**: 22 (12 Atoms, 6 Molecules, 5 Organisms, 2 Templates)
- **Líneas de código**: ~4500+ (aproximadamente)
- **Versión**: 1.3.0
- **Status**: ActionCards completamente funcionales ✨

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.3.0  
**Estado:** ActionCards completamente integrados en HomeTemplate ✨