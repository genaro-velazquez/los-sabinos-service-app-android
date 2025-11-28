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
- [Service List - Listado de Servicios](#service-list---listado-de-servicios-✨-nuevo)
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
- ✅ **Service List** - Listado de servicios asignados con UI adaptable ✨ NUEVO
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
ATOMS (15)              → Elementos básicos
├── Avatar, MetricIcon, StatusBadge
├── ActionButton, PrimaryButton, SecondaryButton
├── StatusText, ModalTitle, ModalContent
├── ActionIcon, ActionTitle, ActionCardContainer ✨
├── ServiceIcon, ServiceTitle, ServiceBadge, ServiceSubtitle ✨ MEJORADOS
    ↓
MOLECULES (7)         → Componentes simples
├── UserHeader, MetricCard, StatusSection
├── UnsyncSection, ModalButtonGroup
├── ActionCard, ServiceHeaderMolecule ✨ MEJORADO
    ↓
ORGANISMS (6)         → Componentes complejos
├── HomeHeaderSection, MetricsSection
├── SyncSection, ConfirmationDialog
├── ActionCardsSection, ServiceListSectionOrganism ✨ NUEVO
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

---

## 📋 Service List - Listado de Servicios ✨ NUEVO

### Descripción

Service List es una sección completa para mostrar servicios asignados al mecánico. Incluye tarjetas de servicio con información detallada, acciones rápidas y manejo de estados.

### Estructura

```
┌────────────────────────────────────────────┐
│ SERVICIOS ASIGNADOS                        │
├────────────────────────────────────────────┤
│ ┌──────────────────────────────────────┐  │
│ │ 🔨 Mantenimiento         Programado  │  │
│ │    Preventivo                        │  │
│ │ Cliente: Global Logistics            │  │
│ │ 13:00 - 14:00 (1 hr)                │  │
│ │ Calle Falsa 123 • Media              │  │
│ │ Nota: No olvidar equipo de seguridad │  │
│ │ [Completar] [Reprogramar]            │  │
│ └──────────────────────────────────────┘  │
│ ... (más servicios) ...                    │
└────────────────────────────────────────────┘
```

### Componentes (Atomic Design) ✨ NUEVO

#### **Atoms** (3) ✨ NUEVO MEJORADOS
- **ServiceIcon.kt** - Icono circular con fondo primario
- **ServiceTitle.kt** - Título que soporta múltiples líneas
- **ServiceBadge.kt** - Badge flexible sin corte de texto
- **ServiceSubtitle.kt** - Subtítulo del cliente
- **LocationAtom.kt** - Ubicación con icono
- **TimeSlotAtom.kt** - Rango horario
- **PriorityBadgeAtom.kt** - Indicador de prioridad
- **NoteBoxAtom.kt** - Caja de notas
- **ActionButtonAtom.kt** - Botones de acción

#### **Molecules** (3) ✨ NUEVO
- **ServiceHeaderMolecule.kt** - Encabezado con icono + título + badge (✨ MEJORADO con texto multilinea)
- **ServiceTimeLineMolecule.kt** - Información horaria
- **ServiceDetailsMolecule.kt** - Ubicación + prioridad
- **ServiceInfoRowMolecule.kt** - Tiempo + ubicación
- **ServiceNoteMolecule.kt** - Notas del servicio
- **ActionButtonsGroupMolecule.kt** - Grupo de botones

#### **Organism** (1) ✨ NUEVO
- **ServiceListSectionOrganism.kt** - Sección completa
  - Column (NO LazyColumn) para evitar anidamiento problemático
  - Manejo de lista vacía
  - Tarjetas con formato responsive
  - Callbacks para completar y reprogramar

#### **Data Model** ✨ NUEVO
- **ServiceCardData.kt** - Modelo con todos los datos necesarios
- **ActionCardModel.kt** - Modelo para tarjetas de acción

### Características ✨ NUEVO

- ✅ **Texto Adaptable** - Títulos largos se parten en 2 líneas
- ✅ **Badges Sin Corte** - "Reprogramado" no se corta a "Progra..."
- ✅ **Column Normal** - NO usa LazyColumn anidado (evita conflictos)
- ✅ **Responsive** - Se adapta a diferentes tamaños de pantalla
- ✅ **Callbacks** - onServiceClick, onCompleteClick, onRescheduleClick
- ✅ **Estado Vacío** - Mensaje cuando no hay servicios
- ✅ **Atomic Design** - Componentes reutilizables
- ✅ **Integrado en HomeTemplate** - Sección opcional configurable

### Mejoras de Texto Multilinea ✨ MEJORADO

```kotlin
// ✅ Cambios realizados:
Text(
    text = title,
    maxLines = 2,                    // Permite 2 líneas
    overflow = TextOverflow.Ellipsis // ... si muy largo
)

// ✅ Row/Column adaptables
Row(
    modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()  // Crece en altura
)
```

---

## 📁 Estructura del Proyecto

```
app/src/main/java/com/lossabinos/serviceapp/
│
├── presentation/ui/components/
│   ├── atoms/
│   │   ├── ActionIcon.kt
│   │   ├── ActionTitle.kt
│   │   ├── ActionCardContainer.kt
│   │   ├── ServiceIcon.kt           ✨ MEJORADO
│   │   ├── ServiceTitle.kt          ✨ MEJORADO
│   │   ├── ServiceBadge.kt          ✨ MEJORADO
│   │   ├── ServiceSubtitle.kt       ✨ NUEVO
│   │   ├── LocationAtom.kt          ✨ NUEVO
│   │   ├── TimeSlotAtom.kt          ✨ NUEVO
│   │   ├── PriorityBadgeAtom.kt     ✨ NUEVO
│   │   ├── NoteBoxAtom.kt           ✨ NUEVO
│   │   └── ActionButtonAtom.kt      ✨ NUEVO
│   │
│   ├── molecules/
│   │   ├── ActionCard.kt
│   │   ├── ServiceHeaderMolecule.kt ✨ MEJORADO (multilinea)
│   │   ├── ServiceTimeLineMolecule.kt
│   │   ├── ServiceDetailsMolecule.kt
│   │   ├── ServiceInfoRowMolecule.kt
│   │   ├── ServiceNoteMolecule.kt
│   │   └── ActionButtonsGroupMolecule.kt
│   │
│   └── organisms/
│       ├── ActionCardsSection.kt
│       ├── ServiceListSectionOrganism.kt ✨ NUEVO
│       ├── HomeHeaderSection.kt
│       ├── MetricsSection.kt
│       ├── SyncSection.kt
│       └── ConfirmationDialog.kt
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

### ✅ v1.4.0 (Completado) - Service List Integration ✨ NUEVO

#### Service List Components
- [x] ServiceIcon.kt - Icono circular mejorado
- [x] ServiceTitle.kt - Título con soporte multilinea
- [x] ServiceBadge.kt - Badge flexible sin corte de texto
- [x] ServiceSubtitle.kt - Subtítulo del cliente
- [x] LocationAtom.kt - Ubicación con icono
- [x] TimeSlotAtom.kt - Rango horario
- [x] PriorityBadgeAtom.kt - Indicador de prioridad
- [x] NoteBoxAtom.kt - Caja de notas
- [x] ActionButtonAtom.kt - Botones de acción

#### Service List Molecules
- [x] ServiceHeaderMolecule.kt - ✨ MEJORADO con wrapContentHeight() y maxLines
- [x] ServiceTimeLineMolecule.kt - Timeline de servicio
- [x] ServiceDetailsMolecule.kt - Detalles completos
- [x] ServiceInfoRowMolecule.kt - Información resumida
- [x] ServiceNoteMolecule.kt - Notas del servicio
- [x] ActionButtonsGroupMolecule.kt - Grupo de botones

#### Service List Organism
- [x] ServiceListSectionOrganism.kt - Sección completa con Column (NO LazyColumn)
- [x] **SOLUCIÓN: Column en lugar de LazyColumn anidado**
- [x] **SOLUCIÓN: wrapContentHeight() para adaptar altura**
- [x] **SOLUCIÓN: maxLines para texto multilinea**
- [x] Estado vacío manejado
- [x] Callbacks completos

#### Integration
- [x] HomeTemplate.kt actualizado con serviceListSection
- [x] HomePage.kt actualizado para pasar servicios
- [x] ServiceCardData.kt - Modelo de datos
- [x] ActionCardModel.kt - Modelo de tarjetas

#### Mejoras Aplicadas
- [x] ✨ Texto en múltiples líneas (maxLines = 2)
- [x] ✨ Badge sin corte de texto
- [x] ✨ Row/Column adaptables (wrapContentHeight())
- [x] ✨ Padding para mejor espaciado
- [x] ✨ overflow = TextOverflow.Ellipsis

### 🚧 v1.5.0 (Próximo)

#### Room Database
- [ ] Crear entidades de datos
- [ ] Implementar DAOs
- [ ] Configurar AppDatabase

#### Integración con Backend
- [ ] Conectar servicios reales desde API
- [ ] Sincronización automática
- [ ] Offline-first caching

---

## 🧪 Testing

### Credenciales de Prueba

```
Email:    henry@lossabinos.como.mx
Password: Lossabinos123456789!
```

### Escenarios a Probar

#### ✅ Service List Visualización ✨ NUEVO
```
HomePage aparece
    ↓
Service List visible (entre Metrics y final)
    ↓
Título "Servicios Asignados" visible
    ↓
3+ tarjetas de servicio mostradas
    ↓
Títulos largos (2 líneas) se adaptan
    ↓
Badges no cortan "Reprogramado"
    ↓
Botones [Completar] [Reprogramar] funcionales
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

- **Componentes Atomic Design**: 28+ (15 Atoms, 7 Molecules, 6 Organisms, 2 Templates)
- **Líneas de código**: ~6000+ (aproximadamente)
- **Versión**: 1.4.0
- **Status**: ServiceList completamente funcional con UI adaptable ✨

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.4.0  
**Estado:** Service List completamente integrado en HomeTemplate ✨