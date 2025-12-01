# 🔧 Los Sabinos - Sistema de Gestión de Servicios de Mantenimiento

Aplicación Android nativa para gestionar servicios de mantenimiento con funcionalidad offline-first, captura de evidencia, sincronización automática de datos, validación automática de sesiones e integración con backend en tiempo real.

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
- [Service List - Listado de Servicios](#service-list---listado-de-servicios-✨-mejorado)
- [Backend Integration - Carga en Tiempo Real](#backend-integration---carga-en-tiempo-real-✨-nuevo)
- [Estado del Proyecto](#estado-del-proyecto)
- [Debugging & Logging](#debugging--logging)
- [Cómo Ejecutar](#cómo-ejecutar)
- [Testing](#testing)
- [Flujo Principal](#flujo-principal)

---

## ✨ Características

### Core Features
- ✅ **Autenticación** con correo y contraseña (validaciones cliente y servidor)
- ✅ **Integración con backend Azure** para autenticación
- ✅ **JSON API** con body serializado (Content-Type: application/json)
- ✅ **Validación automática de sesión** con SplashScreen
- ✅ **Respeto de sesiones guardadas** - Si usuario logado, va directo a Home
- ✅ **Modal de confirmación** elegante para logout
- ✅ **Logout seguro** con limpieza completa de datos
- ✅ **Datos reales del usuario** en HomePage (nombre, ubicación)

### API & Backend Integration ✨ NUEVO
- ✅ **Carga de servicios en tiempo real** desde API
- ✅ **Bearer Token Authentication** - Headers con token automático
- ✅ **WorkOrders & AssignedServices** - Estructura compleja flattened
- ✅ **Manejo de estados** (Loading, Success, Error) con Flow reactivo
- ✅ **Reintentos automáticos** en caso de error
- ✅ **Logging CURL** completo para debugging
- ✅ **AppVersion & AndroidVersion** en headers

### UI Components
- ✅ **ActionCards** - Tarjetas de acciones rápidas (Cámara, Reportes, Ubicación) ✨ NUEVO
- ✅ **Service List** - Listado de servicios asignados con UI adaptable ✨ MEJORADO
- ✅ **Indicadores y métricas** en pantalla Home
- ✅ **Atomic Design** para componentes UI reutilizables
- ✅ **UI moderna** con Jetpack Compose

### Future Features
- ⏳ **Escaneo de códigos de barras/QR** para asignar servicios
- ⏳ **Panel de tareas** con checklist interactivo
- ⏳ **Captura de evidencia** (imágenes con cámara)
- ⏳ **Offline-First** con sincronización automática
- ⏳ **Room Database** para caching local

### Foundation
- ✅ **Inyección de dependencias con Hilt**
- ✅ **Clean Architecture + MVVM + Repository Pattern**
- ✅ **Manejo robusto de errores y reintentos**
- ✅ **Coroutines + Flow** para operaciones asincrónicas

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
- Backend Azure accesible

### Credenciales Backend
```
URL Base: https://lossabinos-e9gvbjfrf9h5dphf.eastus2-01.azurewebsites.net
Endpoint: GET /api/v1/mechanics/me/assigned-services
Autenticación: Bearer Token (obtenido en login)
```

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
   Remote/Local Data Sources (API/DB)
        ↓
   Retorna datos → ViewModel → UI se actualiza
```

#### **Inyección de Dependencias (Hilt):**

```
@HiltAndroidApp
LosSabinosApplication
        ↓
    Módulos Hilt (6):
    ├── AppModule (Context)
    ├── NetworkModule (Retrofit, OkHttp, API Services)
    ├── SharedPreferencesModule (Storage)
    ├── RepositoryModule (Repositories + HeadersMaker)
    ├── UseCaseModule (Use Cases)
    └── AppInfoModule (Version Info) ✨ NUEVO
        ↓
   @HiltViewModel / @AndroidEntryPoint
        ↓
   Inyección automática de dependencias
```

---

## 🎨 Atomic Design

La aplicación usa **Atomic Design** para componentes UI reutilizables:

```
ATOMS (15+)             → Elementos básicos
├── Avatar, MetricIcon, StatusBadge
├── ActionButton, PrimaryButton, SecondaryButton
├── StatusText, ModalTitle, ModalContent
├── ActionIcon, ActionTitle, ActionCardContainer ✨
├── ServiceIcon, ServiceTitle, ServiceBadge ✨ MEJORADOS
├── ServiceSubtitle, LocationAtom, TimeSlotAtom ✨ NUEVO
├── PriorityBadgeAtom, NoteBoxAtom, ActionButtonAtom ✨ NUEVO
    ↓
MOLECULES (7+)        → Componentes simples
├── UserHeader, MetricCard, StatusSection
├── UnsyncSection, ModalButtonGroup
├── ActionCard ✨
├── ServiceHeaderMolecule ✨ MEJORADO (multilinea)
├── ServiceTimeLineMolecule, ServiceDetailsMolecule ✨ NUEVO
├── ServiceInfoRowMolecule, ServiceNoteMolecule ✨ NUEVO
├── ActionButtonsGroupMolecule ✨ NUEVO
    ↓
ORGANISMS (6+)        → Componentes complejos
├── HomeHeaderSection, MetricsSection
├── SyncSection, ConfirmationDialog
├── ActionCardsSection ✨
├── ServiceListSectionOrganism ✨ NUEVO
    ↓
TEMPLATES (2+)        → Layout sin datos
├── LoginTemplate
└── HomeTemplate ✨ ACTUALIZADO
    ↓
PAGES (3+)            → Pantallas completas
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
│       Guardar en SharedPrefs │
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
│ → Limpiar token de SharedPrefs   │
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
Token guardado en SharedPreferences
    ↓
Al día siguiente...
App inicia
    ↓
SplashScreen
    ↓
GetUserPreferencesUseCase.getIsLogged() 
    ↓
Token existe y es válido
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
  - Callbacks para clicks en acciones

---

## 📋 Service List - Listado de Servicios ✨ MEJORADO

### Descripción

Service List es una sección completa para mostrar servicios asignados al mecánico. Ahora carga **datos reales desde la API** con:
- Tarjetas de servicio con información detallada
- Estados reactivos (Loading, Success, Error)
- Acciones rápidas (Completar, Reprogramar)
- Manejo de null-safety

### Estructura

```
┌────────────────────────────────────────────┐
│ SERVICIOS ASIGNADOS (desde API)            │
├────────────────────────────────────────────┤
│ [Loading] Cargando servicios...            │
│                                            │
│ O después de cargar:                       │
│                                            │
│ ┌──────────────────────────────────────┐  │
│ │ 🔧 Transmisión Freightliner          │  │
│ │    Preventive                        │  │
│ │ Vehículo: ABC-BBY (Toyota Camry)     │  │
│ │ 14:00 - 14:30 (30 min)               │  │
│ │ Monterrey • Normal                   │  │
│ │ Nota: Revisar sistema de transmisión │  │
│ │ [Completar] [Reprogramar]            │  │
│ └──────────────────────────────────────┘  │
│ ... (más servicios) ...                    │
│                                            │
│ O en error:                                │
│ ⚠️ Error al cargar servicios               │
│    [Reintentar]                            │
└────────────────────────────────────────────┘
```

### Componentes (Atomic Design) ✨ MEJORADO

#### **Atoms** (9) ✨ MEJORADOS
- **ServiceIcon.kt** - Icono circular con fondo primario
- **ServiceTitle.kt** - Título con soporte multilinea
- **ServiceBadge.kt** - Badge flexible sin corte de texto
- **ServiceSubtitle.kt** - Subtítulo del cliente/vehículo
- **LocationAtom.kt** - Ubicación con icono
- **TimeSlotAtom.kt** - Rango horario (start-end)
- **PriorityBadgeAtom.kt** - Indicador de prioridad
- **NoteBoxAtom.kt** - Caja de notas
- **ActionButtonAtom.kt** - Botones de acción

#### **Molecules** (6) ✨ MEJORADAS
- **ServiceHeaderMolecule.kt** - Encabezado con icono + título + badge
  - ✨ Soporte multilinea (maxLines = 2)
  - ✨ wrapContentHeight() para altura adaptable
- **ServiceTimeLineMolecule.kt** - Información horaria
- **ServiceDetailsMolecule.kt** - Ubicación + prioridad
- **ServiceInfoRowMolecule.kt** - Información resumida
- **ServiceNoteMolecule.kt** - Notas del servicio
- **ActionButtonsGroupMolecule.kt** - Grupo de botones (Completar, Reprogramar)

#### **Organism** (1) ✨ NUEVO
- **ServiceListSectionOrganism.kt** - Sección completa
  - Column (NO LazyColumn) para evitar anidamiento problemático
  - Manejo de lista vacía
  - Tarjetas con formato responsive
  - Callbacks para completar y reprogramar
  - Integrado en HomeTemplate

#### **Data Models** ✨ NUEVO
- **ServiceCardData.kt** - Modelo con todos los datos necesarios
- **ActionCardModel.kt** - Modelo para tarjetas de acción

### Características ✨ MEJORADO

- ✅ **Texto Adaptable** - Títulos largos se parten en 2 líneas
- ✅ **Badges Sin Corte** - "Reprogramado" no se corta
- ✅ **Column Normal** - NO usa LazyColumn anidado (evita conflictos)
- ✅ **Responsive** - Se adapta a diferentes tamaños de pantalla
- ✅ **Callbacks Completos** - onServiceClick, onCompleteClick, onRescheduleClick
- ✅ **Estados Vacíos** - Mensaje cuando no hay servicios
- ✅ **Atomic Design** - Componentes reutilizables
- ✅ **Integrado en HomeTemplate** - Sección configurable
- ✅ **Datos Reales desde API** - Carga en tiempo real ✨ NUEVO

### Mapeo de Datos: WorkOrder → ServiceCardData ✨ NUEVO

```
API Response (workOrder array):
{
    "work_orders": [
        {
            "work_order_id": "...",
            "vehicle": {
                "license_plate": "ABC-BBY",
                "model": { "make": "Toyota", "model": "Camry" }
            },
            "zone": { "name": "Monterrey" },
            "assigned_services": [
                {
                    "service_id": "...",
                    "service_type": {
                        "name": "Transmisión Freightliner",
                        "estimated_duration_minutes": 30
                    },
                    "status": "pending",
                    "priority": "normal",
                    "notes": "Revisar sistema"
                }
            ]
        }
    ]
}

↓ flatMap() aplana la estructura ↓

ServiceCardData:
{
    id = "service_id",
    title = "Transmisión Freightliner",
    clientName = "ABC-BBY (Toyota Camry)",
    status = "Pending",
    address = "Monterrey",
    duration = "30 min",
    priority = "Normal",
    note = "Revisar sistema"
}
```

---

## 🌐 Backend Integration - Carga en Tiempo Real ✨ NUEVO

### MechanicsViewModel - Gestión de Estado

```kotlin
@HiltViewModel
class MechanicsViewModel @Inject constructor(
    private val getMechanicsServicesUseCase: GetMechanicsServicesUseCase
) : ViewModel() {

    private val _assignedServices = MutableStateFlow<Result<AssignedServicesResponse>>(Result.Loading)
    val assignedServices: StateFlow<Result<AssignedServicesResponse>> = _assignedServices.asStateFlow()

    fun loadAssignedServices() {
        viewModelScope.launch {
            try {
                _assignedServices.value = Result.Loading
                val response = getMechanicsServicesUseCase.execute()
                _assignedServices.value = Result.Success(response)
            } catch (e: Exception) {
                _assignedServices.value = Result.Error(e)
            }
        }
    }
}
```

### Flujo Completo: API → ViewModel → UI

```
HomePage inicia
    ↓
LaunchedEffect { mechanicsViewModel.loadAssignedServices() }
    ↓
MechanicsViewModel expone StateFlow<Result<AssignedServicesResponse>>
    ↓
GetMechanicsServicesUseCase.execute()
    ↓
MechanicsRepository.assignedServices()
    ↓
MechanicsServices (Retrofit)
    GET /api/v1/mechanics/me/assigned-services
    Headers:
        - Authorization: Bearer {token}
        - X-App-Version: 1.0.0
        - X-Android-Version: 14
        - X-LOS-SABINOS-PLATFORM-TYPE: app
    ↓
RetrofitResponseValidator.validate(response)
    ↓
AssignedServicesResponseDTO (mapea JSON)
    ↓
dto.toEntity() (convierte a dominio)
    ↓
Retorna AssignedServicesResponse
    ↓
ViewModel recibe en Result.Success
    ↓
HomePage observa StateFlow
    ↓
flatMap() aplana workOrders → ServiceCardData
    ↓
ServiceListSectionOrganism renderiza servicios
```

### Headers de Autenticación ✨ NUEVO

```kotlin
// HeadersMaker.kt
class HeadersMaker(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val versionName: String,
    private val androidVersion: String,
    private val language: String
) {
    fun build(): Map<String, String> {
        val map = HashMap<String, String>()

        map["X-LOS-SABINOS-PLATFORM-TYPE"] = "app"
        map["X-LOS-SABINOS-PLATFORM-name"] = "Android"
        map["Authorization"] = "Bearer ${userPreferencesRepository.getToken() ?: ""}" // ✨
        map["X-App-Version"] = versionName // ✨
        map["X-Android-Version"] = androidVersion // ✨

        return map
    }
}
```

**Headers enviados automáticamente:**
```
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
X-App-Version: 1.0.0
X-Android-Version: 14
X-LOS-SABINOS-PLATFORM-TYPE: app
X-LOS-SABINOS-PLATFORM-name: Android
```

### Result Sealed Class ✨ NUEVO

```kotlin
// domain/common/Result.kt
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
}
```

### HomePage - Integración Completa ✨ MEJORADO

```kotlin
@Composable
fun HomePage(
    // ... callbacks ...
    homeViewModel: HomeViewModel = hiltViewModel(),
    mechanicsViewModel: MechanicsViewModel = hiltViewModel() // ✨ NUEVO
) {
    val homeState = homeViewModel.state.collectAsState().value
    val servicesState = mechanicsViewModel.assignedServices.collectAsState().value // ✨

    // ✨ Cargar servicios al abrir pantalla
    LaunchedEffect(Unit) {
        mechanicsViewModel.loadAssignedServices()
    }

    // ✨ Convertir datos reales a ServiceCardData con flatMap
    val services = when (servicesState) {
        is Result.Loading -> {
            emptyList<ServiceCardData>()
        }
        is Result.Success -> {
            // flatMap: aplana workOrders → servicios
            servicesState.data.workOrder
                ?.flatMap { workOrder ->
                    workOrder.assignedServices?.map { service ->
                        ServiceCardData(
                            id = service.serviceId ?: "service_${service.serviceExecutionId}",
                            title = service.serviceType?.name ?: "Servicio",
                            clientName = workOrder.vehicle?.licensePlate ?: "Cliente",
                            status = service.status?.replaceFirstChar { it.uppercase() } ?: "Pendiente",
                            startTime = service.scheduledStart ?: "N/A",
                            endTime = service.scheduledEnd ?: "N/A",
                            duration = "${service.serviceType?.estimatedDurationMinutes ?: 0} min",
                            address = workOrder.zone?.name ?: "Sin dirección",
                            priority = service.priority?.replaceFirstChar { it.uppercase() } ?: "Media",
                            note = service.notes ?: "Sin notas",
                            onCompleteClick = { onServiceComplete(service.serviceId ?: "") },
                            onRescheduleClick = { onServiceReschedule(service.serviceId ?: "") }
                        )
                    } ?: emptyList()
                } ?: emptyList<ServiceCardData>()
        }
        is Result.Error -> emptyList<ServiceCardData>()
        else -> emptyList<ServiceCardData>()
    }

    // ✨ Mostrar estados (Loading, Success, Error)
    HomeTemplate(
        // ... otras secciones ...
        serviceListSection = {
            when (servicesState) {
                is Result.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                is Result.Success -> {
                    if (services.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay servicios asignados")
                        }
                    } else {
                        ServiceListSectionOrganism(
                            title = "Servicios Asignados",
                            services = services,
                            onServiceClick = { serviceId ->
                                println("Service clicked: $serviceId")
                            },
                            onCompleteClick = { serviceId ->
                                println("Service completed: $serviceId")
                                onServiceComplete(serviceId)
                            },
                            onRescheduleClick = { serviceId ->
                                println("Service rescheduled: $serviceId")
                                onServiceReschedule(serviceId)
                            }
                        )
                    }
                }
                is Result.Error -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Error al cargar servicios",
                                color = MaterialTheme.colorScheme.error
                            )
                            Text(
                                text = servicesState.exception.message ?: "Error desconocido",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                            )
                            Button(
                                onClick = {
                                    mechanicsViewModel.loadAssignedServices()
                                },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }
        }
    )
}
```

---

## 📁 Estructura del Proyecto - Actualizada ✨

```
app/src/main/java/com/lossabinos/serviceapp/
│
├── data/
│   ├── repositories/
│   │   ├── AuthenticationRetrofitRepository.kt
│   │   ├── MechanicsRetrofitRepository.kt          ✨ NUEVO
│   │   └── UserSharedPreferencesRepositoryImpl.kt
│   │
│   ├── services/
│   │   ├── AuthenticationServices.kt
│   │   └── MechanicsServices.kt                    ✨ NUEVO
│   │
│   ├── mappers/
│   │   ├── LoginResponseDTO.kt
│   │   └── AssignedServicesResponseDTO.kt          ✨ NUEVO
│   │
│   └── utils/
│       ├── HeadersMaker.kt                         ✨ ACTUALIZADO
│       ├── RetrofitResponseValidator.kt
│       └── CurlLoggingInterceptor.kt
│
├── domain/
│   ├── repositories/
│   │   ├── AuthenticationRepository.kt
│   │   ├── MechanicsRepository.kt                  ✨ NUEVO
│   │   └── UserPreferencesRepository.kt
│   │
│   ├── usecases/
│   │   ├── EmailPasswordLoginUseCase.kt
│   │   ├── GetAssignedServicesUseCase.kt           ✨ NUEVO
│   │   ├── GetMechanicsServicesUseCase.kt          ✨ NUEVO
│   │   └── GetUserPreferencesUseCase.kt
│   │
│   ├── models/
│   │   ├── LoginResponse.kt
│   │   ├── AssignedServicesResponse.kt             ✨ NUEVO
│   │   └── UserData.kt
│   │
│   └── common/
│       ├── Exception.kt
│       └── Result.kt                               ✨ NUEVO
│
├── presentation/
│   ├── viewmodels/
│   │   ├── LoginViewModel.kt
│   │   ├── HomeViewModel.kt
│   │   ├── SplashViewModel.kt
│   │   ├── MechanicsViewModel.kt                   ✨ NUEVO
│   │   └── BaseViewModel.kt
│   │
│   ├── screens/
│   │   ├── splash/
│   │   │   └── SplashScreen.kt
│   │   ├── login/
│   │   │   └── LoginScreen.kt
│   │   └── home/
│   │       └── HomePage.kt                         ✨ ACTUALIZADO
│   │
│   ├── ui/
│   │   ├── atoms/ (9+ componentes)
│   │   ├── molecules/ (6+ componentes)
│   │   ├── organisms/ (6+ componentes)
│   │   └── templates/
│   │       ├── LoginTemplate.kt
│   │       └── HomeTemplate.kt                     ✨ ACTUALIZADO
│   │
│   ├── navigation/
│   │   ├── NavGraph.kt                             ✨ ACTUALIZADO
│   │   ├── NavigationEvent.kt
│   │   └── Routes.kt
│   │
│   └── theme/
│       ├── Color.kt
│       ├── Typography.kt
│       └── Theme.kt
│
├── di/
│   ├── AppModule.kt
│   ├── NetworkModule.kt                            ✨ ACTUALIZADO
│   ├── SharedPreferencesModule.kt
│   ├── RepositoryModule.kt                         ✨ ACTUALIZADO
│   ├── UseCaseModule.kt                            ✨ ACTUALIZADO
│   └── AppInfoModule.kt                            ✨ NUEVO
│
└── app/
    ├── LosSabinosApplication.kt
    └── MainActivity.kt
```

---

## 🛠️ Tecnologías

### UI & Composables
- **Jetpack Compose** 1.5+ - UI declarativa moderna
- **Material Design 3** - Componentes estándar
- **Compose Navigation** - Navegación entre pantallas

### Inyección de Dependencias
- **Hilt** 2.48+ - DI framework (✅ INTEGRADO)

### Networking
- **Retrofit** 2.9+ - Cliente HTTP (✅ INTEGRADO)
- **OkHttp** 4.11+ - Interceptores y logging (✅ INTEGRADO)
- **Gson** 2.10+ - Serialización JSON (✅ INTEGRADO)

### Almacenamiento Local
- **SharedPreferences** - Preferencias de usuario (✅ INTEGRADO)
- **Room** 2.5+ - BD local SQLite (próximo)

### Concurrencia & Reactividad
- **Kotlin Coroutines** 1.7+ - Operaciones asincrónicas (✅ INTEGRADO)
- **Flow** - Streams reactivos (✅ INTEGRADO)
- **StateFlow** - Estado observable (✅ INTEGRADO)

### Cámara y Escaneo
- **CameraX** - API moderna para cámara (próximo)
- **ML Kit Barcode Scanning** - Escaneo de códigos (próximo)

### Debugging & Logging
- **Logcat** - Logging integrado (✅ INTEGRADO)
- **CURL Interceptor** - Logging de peticiones (✅ INTEGRADO)

---

## 📊 Estado del Proyecto

### ✅ v1.5.0 (Completado) - Backend Integration ✨ NUEVO

#### Data Layer - Backend
- [x] MechanicsServices.kt - Interface Retrofit para API
- [x] MechanicsRetrofitRepository.kt - Implementación del repositorio
- [x] AssignedServicesResponseDTO.kt - Mapeo JSON → Entidades
- [x] Validación de respuesta con RetrofitResponseValidator
- [x] HeadersMaker actualizado - Bearer token automático

#### Domain Layer
- [x] MechanicsRepository.kt - Interface de repositorio
- [x] GetMechanicsServicesUseCase.kt - Caso de uso
- [x] AssignedServicesResponse.kt - Modelo de dominio
- [x] Result.kt sealed class - Manejo de estados

#### Presentation Layer
- [x] MechanicsViewModel.kt - ViewModel con Flow reactivo
- [x] HomePage actualizado - Integración con MechanicsViewModel
- [x] NavGraph actualizado - Callbacks completos
- [x] Manejo de estados (Loading, Success, Error)
- [x] Botón Reintentar en caso de error

#### Dependency Injection
- [x] NetworkModule - provideMechanicsServices()
- [x] RepositoryModule - provideMechanicsRepository()
- [x] UseCaseModule - provideGetMechanicsServicesUseCase()
- [x] AppInfoModule - versionName y androidVersion

#### Características Implementadas
- [x] ✨ Bearer Token en headers automático
- [x] ✨ flatMap para aplanar workOrders → servicios
- [x] ✨ Manejo de null-safety con operadores seguros
- [x] ✨ LaunchedEffect para cargar servicios al iniciar
- [x] ✨ Estados reactivos (Loading, Success, Error)
- [x] ✨ Botón Reintentar para errores
- [x] ✨ Logging CURL completo en Logcat
- [x] ✨ AppVersion en headers

### 🚧 v1.6.0 (Próximo)

#### Room Database
- [ ] Crear entidades de datos
- [ ] Implementar DAOs para servicios
- [ ] Configurar AppDatabase
- [ ] Migrations automáticas

#### Sincronización
- [ ] Sincronización automática de servicios
- [ ] Caching offline-first
- [ ] Conflicto resolution

#### UI Enhancements
- [ ] Detalle de servicio (nueva pantalla)
- [ ] Completar/Reprogramar servicio desde API
- [ ] Actualización en tiempo real con WebSocket

---

## 🧪 Testing

### Credenciales de Prueba

```
Email:    henry@lossabinos.como.mx
Password: Lossabinos123456789!

Nota: El backend debe estar accesible en:
https://lossabinos-e9gvbjfrf9h5dphf.eastus2-01.azurewebsites.net
```

### Escenarios a Probar - Backend Integration ✨ NUEVO

#### ✅ Carga de Servicios en Tiempo Real
```
1. Login con credenciales
    ↓
2. HomePage carga automáticamente
    ↓
3. LaunchedEffect ejecuta loadAssignedServices()
    ↓
4. CircularProgressIndicator aparece (Loading)
    ↓
5. API retorna work_orders con servicios
    ↓
6. flatMap aplana datos correctamente
    ↓
7. ServiceCardData se renderiza en UI
    ↓
8. Ver en Logcat:
   D/OkHttp: --> GET /api/v1/mechanics/me/assigned-services
   D/OkHttp: Authorization: Bearer eyJhbGc...
   D/OkHttp: <-- 200 OK
   D/MechanicsViewModel: ✅ Servicios cargados: X items
```

#### ✅ Manejo de Errores
```
1. Sin conexión a internet
    ↓
2. MechanicsViewModel recibe Result.Error
    ↓
3. Se muestra mensaje de error
    ↓
4. Click en "Reintentar"
    ↓
5. Se intenta cargar nuevamente
```

#### ✅ Datos Reales Mostrados
```
Verificar que ServiceCardData contiene:
- ✅ ID del servicio (service_id)
- ✅ Nombre del tipo (service_type.name)
- ✅ Placa del vehículo (vehicle.license_plate)
- ✅ Zona (zone.name)
- ✅ Duración estimada (estimated_duration_minutes)
- ✅ Estado (status)
- ✅ Prioridad (priority)
- ✅ Notas (notes)
```

---

## 🔍 Debugging & Logging

### CURL Logging Interceptor

Toda petición a la API se loguea como CURL:

```bash
# Ejemplo en Logcat:
curl -X GET 'https://lossabinos-e9gvbjfrf9h5dphf.eastus2-01.azurewebsites.net/api/v1/mechanics/me/assigned-services' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...' \
  -H 'X-App-Version: 1.0.0' \
  -H 'X-Android-Version: 14' \
  -H 'X-LOS-SABINOS-PLATFORM-TYPE: app' \
  -H 'X-LOS-SABINOS-PLATFORM-name: Android'
```

### ViewModel Logging

```kotlin
// MechanicsViewModel.kt
fun loadAssignedServices() {
    viewModelScope.launch {
        try {
            _assignedServices.value = Result.Loading
            println("🔄 Iniciando carga de servicios...")
            val response = getMechanicsServicesUseCase.execute()
            println("✅ Servicios cargados: ${response.workOrder?.size} items")
            _assignedServices.value = Result.Success(response)
        } catch (e: Exception) {
            println("❌ Error al cargar servicios: ${e.message}")
            e.printStackTrace()
            _assignedServices.value = Result.Error(e)
        }
    }
}
```

### LogCat Filters

```bash
# Ver solo logs de la app
adb logcat | grep "lossabinos"

# Ver solo errores
adb logcat | grep "Error"

# Ver OkHttp requests/responses
adb logcat | grep "OkHttp"
```

---

## 🚀 Cómo Ejecutar

### 1. Clona el repositorio
```bash
git clone https://github.com/genaro-velazquez/los-sabinos-service-app-android.git
cd los-sabinos-service-app-android
```

### 2. Sincroniza Gradle
```bash
./gradlew clean build
```

### 3. Ejecuta en emulador o dispositivo
```bash
# Opción A: Android Studio
Shift + F10 o Run → Run 'app'

# Opción B: Terminal
./gradlew installDebug
```

### 4. Login
```
Email: henry@lossabinos.como.mx
Password: Lossabinos123456789!
```

### 5. Verifica en HomePage
```
- Debe cargar servicios automáticamente
- Ver CircularProgressIndicator mientras carga
- Ver servicios en Service List después
- Verificar CURL logs en Logcat
```

---

## 📊 Métricas del Proyecto

- **ViewModels**: 4 (Splash, Login, Home, Mechanics)
- **UseCases**: 4+ (Authentication, Preferences, Mechanics)
- **Repositories**: 4 (Authentication, UserPreferences, Mechanics, Local)
- **Componentes Atomic Design**: 28+ (9 Atoms, 6 Molecules, 6+ Organisms)
- **Servicios Retrofit**: 2 (Authentication, Mechanics)
- **Líneas de código**: ~8000+ 
- **Versión**: 1.5.0
- **Status**: Integración Backend completada con flow reactivo ✨

---

**Última actualización:** Noviembre 30, 2025  
**Versión:** 1.5.0  
**Estado:** Servicios cargándose en tiempo real desde API con manejo de estados ✨