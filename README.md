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
- [ActionCards - Acciones Rápidas](#actioncards---acciones-rápidas)
- [Service List - Listado de Servicios](#service-list---listado-de-servicios)
- [Service Detail - Detalles del Servicio](#service-detail---detalles-del-servicio-✨-nuevo)
- [Backend Integration - Carga en Tiempo Real](#backend-integration---carga-en-tiempo-real)
- [Estado del Proyecto](#estado-del-proyecto)
- [Debugging & Logging](#debugging--logging)
- [Cómo Ejecutar](#cómo-ejecutar)
- [Conventional Commits](#-conventional-commits---tipos-de-commits-✨-nuevo)
- [Git Workflow](#git-workflow---subir-cambios-a-github-✨-nuevo)
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

### API & Backend Integration
- ✅ **Carga de servicios en tiempo real** desde API
- ✅ **Bearer Token Authentication** - Headers con token automático
- ✅ **WorkOrders & AssignedServices** - Estructura compleja flattened
- ✅ **Manejo de estados** (Loading, Success, Error, Idle) con Flow reactivo
- ✅ **Reintentos automáticos** en caso de error
- ✅ **Logging CURL** completo para debugging
- ✅ **AppVersion & AndroidVersion** en headers
- ✅ **Detalle de Servicio** - Carga datos específicos con modal ✨ NUEVO

### UI Components
- ✅ **ActionCards** - Tarjetas de acciones rápidas (Cámara, Reportes, Ubicación)
- ✅ **Service List** - Listado de servicios asignados con UI adaptable
- ✅ **Service Detail Modal** - Modal elegante con detalles del servicio ✨ NUEVO
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
- ✅ **Callbacks en Composables** - No en Data Classes (Clean Architecture) ✨ NUEVO

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

---

## 📋 Service List - Listado de Servicios

### Descripción

Service List es una sección que muestra servicios asignados al mecánico cargados **en tiempo real desde la API**. Cada tarjeta permite ver información detallada e interactuar con botones de acción.

### Características
- ✅ **Datos reales desde API** con estados reactivos
- ✅ **Texto adaptable** - Títulos largos se parten en múltiples líneas
- ✅ **Botones de acción** - Completar y Reprogramar servicio
- ✅ **Manejo de estados** - Loading, Success, Error
- ✅ **Atomic Design** - Componentes reutilizables
- ✅ **Callbacks en Composables** - NO en Data Classes ✨ NUEVO

---

## 🎯 Service Detail - Detalles del Servicio ✨ NUEVO

### Descripción

Cuando el usuario hace click en "Completar" en una tarjeta de servicio, se cargan los **detalles específicos** del servicio y se muestra en un **modal elegante** con toda la información.

### Flujo Completo

```
HomePage - Service List
    ↓
Usuario hace click en "Completar"
    ↓
onCompleteClick callback se ejecuta
    ↓
homeScreen.onCompleteClick = { serviceId ->
    selectedServiceId = serviceId
    mechanicsViewModel.loadDetailedService(serviceId)  ✨ AQUÍ
}
    ↓
MechanicsViewModel.loadDetailedService(idService)
    ↓ (_detailedService.value = Result.Loading)
    ↓
GetDetailedServiceUseCase.execute(idService)
    ↓
MechanicsRepository.detailedService(idService)
    ↓
GET /api/v1/mechanics/me/assigned-services/{idService}
    (Con Bearer Token en headers)
    ↓
Backend retorna JSON con detalles
    ↓
DetailedServiceResponseDTO mapea la respuesta
    ↓
dto.toEntity() convierte a DetailedServiceResponse
    ↓
_detailedService.value = Result.Success(response)
    ↓
LaunchedEffect detecta cambio
    ↓
showDetailModal = true
    ↓
ServiceDetailModal se abre ✨
    ↓
Usuario ve:
├─ ID Ejecución
├─ ID Servicio
├─ Tipo de Servicio
├─ Progreso (items completados / total)
└─ Información del servicio
```

### Implementación - Backend

#### 1. Interface Retrofit (MechanicsServices.kt)

```kotlin
@GET("api/v1/mechanics/me/assigned-services/{idService}")
suspend fun detailedService(
    @HeaderMap headers: Map<String, String>,
    @Path("idService") idService: String
): Response<ResponseBody>
```

#### 2. Repository Interface (MechanicsRepository.kt)

```kotlin
interface MechanicsRepository {
    suspend fun assignedServices(): AssignedServicesResponse
    suspend fun detailedService(idService: String): DetailedServiceResponse  // ✨ NUEVO
}
```

#### 3. Repository Implementation (MechanicsRetrofitRepository.kt)

```kotlin
override suspend fun detailedService(idService: String): DetailedServiceResponse {
    val response = assignedServices.detailedService(
        headers = headersMaker.build(), 
        idService = idService
    )
    val json = RetrofitResponseValidator.validate(response = response)
    val dto = DetailedServiceResponseDTO(json = json)
    return dto.toEntity()
}
```

#### 4. Data Transfer Object (DetailedServiceResponseDTO.kt)

```kotlin
open class DetailedServiceResponseDTO(json: JSONObject) : 
    GetBaseResponseDTO<DetailedServiceResponse>(json = json) {
    
    val serviceExecutionId = json.asJSONObject("data").asString("service_execution_id")
    val serviceId = json.asJSONObject("data").asString("service_id")
    val serviceType = ServiceTypeDTO(json.asJSONObject("data").asJSONObject("service_type"))
    val template = TemplateDTO(json.asJSONObject("data").asJSONObject("template"))
    val currentProgress = CurrentProgressDTO(json.asJSONObject("data").asJSONObject("current_progress"))
    val serviceInfo = ServiceInfoDTO(json.asJSONObject("data").asJSONObject("service_info"))

    override fun toEntity(): DetailedServiceResponse = DetailedServiceResponse(
        serviceExecutionId = serviceExecutionId,
        serviceId = serviceId,
        serviceType = serviceType.toEntity(),
        template = template.toEntity(),
        currentProgress = currentProgress.toEntity(),
        serviceInfo = serviceInfo.toEntity()
    )
}
```

#### 5. Domain Model (DetailedServiceResponse.kt)

```kotlin
class DetailedServiceResponse(
    val serviceExecutionId: String,
    val serviceId: String,
    val serviceType: ServiceType,
    val template: Template,
    val currentProgress: CurrentProgress,
    val serviceInfo: ServiceInfo
): DomainEntity()
```

### Implementación - Frontend

#### 1. Use Case (GetDetailedServiceUseCase.kt)

```kotlin
class GetDetailedServiceUseCase(
    private val mechanicsRepository: MechanicsRepository
) {
    suspend fun execute(idService: String) = 
        mechanicsRepository.detailedService(idService = idService)
}
```

#### 2. ViewModel (MechanicsViewModel.kt) ✨ ACTUALIZADO

```kotlin
@HiltViewModel
class MechanicsViewModel @Inject constructor(
    private val getMechanicsServicesUseCase: GetMechanicsServicesUseCase,
    private val getDetailedServiceUseCase: GetDetailedServiceUseCase  // ✨ NUEVO
) : ViewModel() {

    // ==========================================
    // ASSIGNED SERVICES (Lista de servicios)
    // ==========================================
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

    // ==========================================
    // DETAILED SERVICE (Detalles de un servicio) ✨ NUEVO
    // ==========================================
    /**
     * StateFlow para almacenar los detalles de un servicio específico
     * 
     * Estados posibles:
     * - Loading: Cargando datos del servicio
     * - Success: Datos cargados exitosamente
     * - Error: Error al cargar datos
     * - Idle: Estado inicial (sin cargar nada aún)
     */
    private val _detailedService = MutableStateFlow<Result<DetailedServiceResponse>>(Result.Idle)
    val detailedService: StateFlow<Result<DetailedServiceResponse>> = _detailedService.asStateFlow()

    /**
     * Carga los detalles de un servicio específico
     * 
     * @param idService ID del servicio a cargar
     * 
     * Uso en HomeScreen:
     * ```
     * onCompleteClick = { serviceId ->
     *     mechanicsViewModel.loadDetailedService(serviceId)
     * }
     * ```
     */
    fun loadDetailedService(idService: String) {
        viewModelScope.launch {
            try {
                _detailedService.value = Result.Loading
                val response = getDetailedServiceUseCase.execute(idService = idService)
                _detailedService.value = Result.Success(response)
            } catch (e: Exception) {
                _detailedService.value = Result.Error(e)
            }
        }
    }
}
```

#### 3. StateFlow Update (Result.kt) ✨ ACTUALIZADO

```kotlin
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
    object Loading : Result<Nothing>()
    object Idle : Result<Nothing>()  // ✨ NUEVO: Estado inicial
}
```

#### 4. HomeScreen Integration ✨ ACTUALIZADO

```kotlin
@Composable
fun HomeScreen(
    ...
    mechanicsViewModel: MechanicsViewModel = hiltViewModel()
) {
    // Observar estados
    val servicesState = mechanicsViewModel.assignedServices.collectAsState().value
    val detailedServiceState = mechanicsViewModel.detailedService.collectAsState().value

    // Variables de estado local
    var selectedServiceId by remember { mutableStateOf<String?>(null) }
    var showDetailModal by remember { mutableStateOf(false) }

    // Cargar servicios al abrir
    LaunchedEffect(Unit) {
        mechanicsViewModel.loadAssignedServices()
    }

    // Detectar cambios en detailedService
    LaunchedEffect(detailedServiceState) {
        when (detailedServiceState) {
            is Result.Success -> {
                showDetailModal = true  // Abrir modal
            }
            is Result.Error -> {
                println("Error: ${detailedServiceState.exception.message}")
            }
            else -> {}
        }
    }

    // Modal de detalles
    if (showDetailModal && detailedServiceState is Result.Success) {
        ServiceDetailModal(
            detailedService = detailedServiceState.data,
            onDismiss = {
                showDetailModal = false
                selectedServiceId = null
            }
        )
    }

    // HomeTemplate con sección de servicios
    HomeTemplate(
        serviceListSection = {
            when (servicesState) {
                is Result.Loading -> CircularProgressIndicator()
                is Result.Success -> {
                    val services = servicesState.data.workOrder.flatMap { workOrder ->
                        workOrder.assignedServices.map { service ->
                            ServiceCardData(
                                id = service.id,
                                title = service.serviceType.name,
                                // ... más datos ...
                                // ❌ SIN: onCompleteClick, onRescheduleClick (callbacks en composable)
                            )
                        }
                    }
                    
                    ServiceListSectionOrganism(
                        services = services,
                        onCompleteClick = { serviceId ->
                            selectedServiceId = serviceId
                            mechanicsViewModel.loadDetailedService(serviceId)  // ✨ CARGAR DETALLES
                        },
                        onRescheduleClick = { serviceId ->
                            onServiceReschedule(serviceId)
                        }
                    )
                }
                is Result.Error -> Text("Error al cargar")
                else -> {}
            }
        }
    )
}

// Modal de detalles del servicio ✨ NUEVO
@Composable
fun ServiceDetailModal(
    detailedService: DetailedServiceResponse,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Detalles del Servicio",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("ID Ejecución: ${detailedService.serviceExecutionId}")
                Text("ID Servicio: ${detailedService.serviceId}")
                Text("Tipo: ${detailedService.serviceType.name}")
                Text("Progreso: ${detailedService.currentProgress.itemsCompleted}/${detailedService.currentProgress.itemTotal}")
                
                Text(
                    text = "Información",
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = detailedService.serviceInfo.status,
                    fontSize = 12.sp
                )
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Cerrar")
            }
        }
    )
}
```

#### 5. Dependency Injection (UseCaseModule.kt) ✨ ACTUALIZADO

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideGetMechanicsServicesUseCase(
        mechanicsRepository: MechanicsRepository
    ): GetMechanicsServicesUseCase {
        return GetMechanicsServicesUseCase(mechanicsRepository = mechanicsRepository)
    }

    @Singleton
    @Provides
    fun provideGetDetailedServiceUseCase(
        mechanicsRepository: MechanicsRepository
    ): GetDetailedServiceUseCase {
        return GetDetailedServiceUseCase(mechanicsRepository = mechanicsRepository)  // ✨ NUEVO
    }
}
```

---

## 🏗️ Arquitectura - Callbacks en Composables ✨ ACTUALIZADO

### Anti-Patrón: Callbacks en Data Class ❌

```kotlin
// ❌ INCORRECTO - Mezcla datos con lógica
data class ServiceCardData(
    val id: String,
    val title: String,
    val onCompleteClick: () -> Unit,      // ❌ Lógica aquí
    val onRescheduleClick: () -> Unit     // ❌ Lógica aquí
)
```

### Best Practice: Callbacks en Composables ✅

```kotlin
// ✅ CORRECTO - Data class solo datos
data class ServiceCardData(
    val id: String,
    val title: String,
    val clientName: String,
    // ... más datos sin callbacks
)

// ✅ CORRECTO - Composable recibe callbacks
@Composable
fun ServiceListSectionOrganism(
    services: List<ServiceCardData>,
    onCompleteClick: (String) -> Unit = {},      // ✅ Callbacks aquí
    onRescheduleClick: (String) -> Unit = {}     // ✅ Callbacks aquí
) {
    services.forEach { service ->
        ServiceCardOrganism(
            service = service,
            onCompleteClick = { onCompleteClick(service.id) },
            onRescheduleClick = { onRescheduleClick(service.id) }
        )
    }
}
```

### Flujo Completo ✨ MEJORADO

```
HomeScreen (tienes ViewModel y contexto)
    ↓
Crea: List<ServiceCardData> (solo datos)
    ↓
Pasa callbacks en: ServiceListSectionOrganism(
    services = services,
    onCompleteClick = { serviceId ->
        mechanicsViewModel.loadDetailedService(serviceId)  // ✨
    }
)
    ↓
ServiceCardOrganism(service, onCompleteClick, ...)
    ↓
ActionButtonsGroupMolecule(onCompleteClick, ...)
    ↓
ActionButtonAtom(onClick)
    ↓
Usuario hace click
    ↓
Callback ejecuta: mechanicsViewModel.loadDetailedService() ✨
```

---

## 📁 Estructura del Proyecto - Actualizada ✨

```
app/src/main/java/com/lossabinos/serviceapp/
│
├── data/
│   ├── repositories/
│   │   ├── AuthenticationRetrofitRepository.kt
│   │   ├── MechanicsRetrofitRepository.kt          ✨ ACTUALIZADO
│   │   └── UserSharedPreferencesRepositoryImpl.kt
│   │
│   ├── services/
│   │   ├── AuthenticationServices.kt
│   │   └── MechanicsServices.kt                    ✨ ACTUALIZADO
│   │
│   ├── mappers/
│   │   ├── LoginResponseDTO.kt
│   │   ├── AssignedServicesResponseDTO.kt
│   │   └── DetailedServiceResponseDTO.kt           ✨ NUEVO
│   │
│   └── utils/
│       ├── HeadersMaker.kt
│       ├── RetrofitResponseValidator.kt
│       └── CurlLoggingInterceptor.kt
│
├── domain/
│   ├── repositories/
│   │   ├── AuthenticationRepository.kt
│   │   ├── MechanicsRepository.kt                  ✨ ACTUALIZADO
│   │   └── UserPreferencesRepository.kt
│   │
│   ├── usecases/
│   │   ├── EmailPasswordLoginUseCase.kt
│   │   ├── GetMechanicsServicesUseCase.kt
│   │   ├── GetDetailedServiceUseCase.kt            ✨ NUEVO
│   │   └── GetUserPreferencesUseCase.kt
│   │
│   ├── models/
│   │   ├── LoginResponse.kt
│   │   ├── AssignedServicesResponse.kt
│   │   ├── DetailedServiceResponse.kt              ✨ NUEVO
│   │   └── UserData.kt
│   │
│   └── common/
│       ├── Exception.kt
│       └── Result.kt                               ✨ ACTUALIZADO (Idle)
│
├── presentation/
│   ├── viewmodels/
│   │   ├── LoginViewModel.kt
│   │   ├── HomeViewModel.kt
│   │   ├── SplashViewModel.kt
│   │   ├── MechanicsViewModel.kt                   ✨ ACTUALIZADO
│   │   └── BaseViewModel.kt
│   │
│   ├── screens/
│   │   ├── splash/
│   │   │   └── SplashScreen.kt
│   │   ├── login/
│   │   │   └── LoginScreen.kt
│   │   └── home/
│   │       └── HomeScreen.kt                       ✨ ACTUALIZADO
│   │
│   ├── ui/
│   │   ├── atoms/ (9+ componentes)
│   │   ├── molecules/ (6+ componentes)
│   │   ├── organisms/ (6+ componentes)
│   │   └── templates/
│   │       ├── LoginTemplate.kt
│   │       └── HomeTemplate.kt
│   │
│   ├── navigation/
│   │   ├── NavGraph.kt
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
│   ├── NetworkModule.kt
│   ├── SharedPreferencesModule.kt
│   ├── RepositoryModule.kt
│   ├── UseCaseModule.kt                            ✨ ACTUALIZADO
│   └── AppInfoModule.kt
│
└── app/
    ├── LosSabinosApplication.kt
    └── MainActivity.kt
```

---

## 📊 Estado del Proyecto - Actualizado ✨

### ✅ v1.6.0 (Completado) - Service Detail & Clean Architecture

#### Implementación Backend
- [x] MechanicsServices.kt - Endpoint GET /api/v1/mechanics/me/assigned-services/{idService}
- [x] MechanicsRetrofitRepository.kt - Método detailedService(idService)
- [x] DetailedServiceResponseDTO.kt - Mapeo JSON → Entidades
- [x] RetrofitResponseValidator - Validación de respuesta

#### Implementación Domain
- [x] MechanicsRepository.kt - Interfaz con detailedService()
- [x] GetDetailedServiceUseCase.kt - Caso de uso
- [x] DetailedServiceResponse.kt - Modelo de dominio
- [x] Result.kt - Estado Idle agregado

#### Implementación Frontend
- [x] MechanicsViewModel.kt - StateFlow detailedService + loadDetailedService()
- [x] HomeScreen.kt - Modal AlertDialog integrado
- [x] ServiceDetailModal.kt - Componente modal del detalle
- [x] LaunchedEffect - Detecta cambios en detailedService

#### Mejoras de Arquitectura
- [x] ✨ Callbacks SOLO en Composables (NO en Data Classes)
- [x] ✨ ServiceCardData solo contiene datos puros
- [x] ✨ Callbacks se pasan como parámetros en composables
- [x] ✨ Clean Architecture respetada (Separation of Concerns)

#### Dependency Injection
- [x] UseCaseModule.kt - provideGetDetailedServiceUseCase()
- [x] Inyección automática en MechanicsViewModel

#### Características Implementadas
- [x] ✨ Cargar detalles de servicio desde API
- [x] ✨ Modal elegante con AlertDialog
- [x] ✨ Manejo de estados (Loading, Success, Error, Idle)
- [x] ✨ Callbacks sin redefinir en múltiples lugares
- [x] ✨ Data classes puros sin lógica
- [x] ✨ Flow reactivo completo
- [x] ✨ Logging completo en Logcat

### 🚧 v1.7.0 (Próximo)

#### Room Database
- [ ] Crear entidades de datos
- [ ] Implementar DAOs para servicios
- [ ] Configurar AppDatabase
- [ ] Migrations automáticas

#### Sincronización
- [ ] Sincronización automática de servicios
- [ ] Caching offline-first
- [ ] Conflicto resolution

#### Task Management
- [ ] Panel de tareas (checklist)
- [ ] Captura de evidencia (imágenes)
- [ ] Completar servicio con datos

---

## 📝 Conventional Commits - Tipos de Commits ✨ NUEVO

### Estándar de Mensajes de Commit

Usamos **Conventional Commits** para mantener un historial limpio y consistente.

#### **Formato Base**

```
tipo(alcance): descripción breve

[cuerpo opcional - descripción detallada]

[pie opcional - información adicional, breaking changes, etc]
```

#### **Tipos de Commits**

| Tipo | Descripción | Ejemplo |
|------|-------------|---------|
| **feat** | Nueva característica | `feat(auth): Implementar login con Azure` |
| **fix** | Corrección de bug | `fix(ui): Corregir altura de LazyColumn` |
| **refactor** | Cambio de código sin características nuevas | `refactor(callbacks): Mover callbacks a Composables` |
| **docs** | Cambios en documentación | `docs: Actualizar README con v1.6.0` |
| **test** | Cambios en tests | `test(viewmodel): Agregar pruebas a MechanicsViewModel` |
| **chore** | Cambios en config, dependencias | `chore(gradle): Actualizar Compose a 1.6.0` |
| **style** | Cambios de formato y estilos | `style: Formatear código según ktlint` |
| **perf** | Mejoras de performance | `perf(list): Optimizar renderizado de servicios` |
| **ci** | Cambios en CI/CD | `ci: Configurar GitHub Actions` |

#### **Alcance (Scope) Recomendado**

```
Alcances comunes en este proyecto:
- auth          : Autenticación y login
- api           : Integración con backend/API
- ui            : Componentes de UI
- viewmodel     : ViewModels y lógica
- database      : Room y persistencia
- navigation    : Navegación entre pantallas
- theme         : Temas y estilos
- callbacks     : Manejo de callbacks
- service-list  : Listado de servicios
- service-detail: Detalles del servicio ✨ NUEVO
- di            : Inyección de dependencias
- readme        : Documentación
```

#### **Ejemplos Prácticos para Este Proyecto**

##### ✅ **Commits Bien Formados**

```bash
# Característica nueva
git commit -m "feat(service-detail): Implementar carga y modal de detalles

- Agregar GetDetailedServiceUseCase
- Implementar endpoint detailedService() en repositorio
- Agregar StateFlow detailedService en MechanicsViewModel
- Crear modal AlertDialog en HomeScreen"

# Corrección de bug
git commit -m "fix(ui): Corregir texto cortado en ServiceBadge

El badge 'Reprogramado' se cortaba en algunos dispositivos.
Agregado overflow: TextOverflow.Ellipsis y maxLines = 1"

# Refactorización
git commit -m "refactor(callbacks): Mover callbacks de Data Class a Composable

BREAKING CHANGE: ServiceCardData ahora no contiene onCompleteClick
Los callbacks ahora son parámetros en ServiceListSectionOrganism"

# Documentación
git commit -m "docs(readme): Actualizar documentación para v1.6.0

- Agregar sección Service Detail
- Agregar Git Workflow con pasos detallados
- Actualizar estructura del proyecto
- Agregar checklist de cambios"

# Optimización de dependencias
git commit -m "chore(deps): Actualizar Compose a 1.6.0"

# Mejora de performance
git commit -m "perf(service-list): Reducir recomposiciones en LazyColumn

Usar remember para prevenir recomposiciones innecesarias
Resultó en reducción de 40% en CPU"

# Cambio de estilos
git commit -m "style: Formatear código con ktlint

Aplicar reglas de linting a todas las clases"

# Cambios en pruebas
git commit -m "test(viewmodel): Agregar pruebas a MechanicsViewModel

- Test para loadAssignedServices()
- Test para loadDetailedService()
- Mock de API responses"
```

##### ❌ **Commits Mal Formados (evitar)**

```bash
# ❌ Sin tipo
git commit -m "Agregar feature"

# ❌ Demasiado genérico
git commit -m "fix: arreglar cosas"

# ❌ Muy largo sin saltos
git commit -m "feat: implementar login con azure integracion..."

# ❌ Mayúsculas excesivas
git commit -m "FEAT: IMPLEMENTAR NUEVA CARACTERISTICA"

# ❌ Sin descripción clara
git commit -m "update"

# ❌ Sin alcance cuando es necesario
git commit -m "feat: cambios varios"
```

#### **BREAKING CHANGES**

Si tu cambio rompe compatibilidad con versiones anteriores:

```bash
# Opción 1: Con ! después del tipo
git commit -m "refactor(callbacks)!: Mover callbacks a Composables

Detalles: ServiceCardData ya no contiene callbacks.
Los callbacks ahora son parámetros en el Composable.

BREAKING CHANGE: onCompleteClick removido de ServiceCardData"

# Opción 2: En pie de página
git commit -m "refactor: Cambiar estructura de ServiceCardData

BREAKING CHANGE: onCompleteClick y onRescheduleClick removidos"
```

#### **Tips para Mejores Commits**

1. **Sé específico** - Describe QUÉ cambió, no solo CÓMO
   ```bash
   ❌ git commit -m "feat: cambios"
   ✅ git commit -m "feat(service-detail): Agregar modal con detalles"
   ```

2. **Usa imperativo** - "Agregar" no "Agregado" o "Agregué"
   ```bash
   ❌ git commit -m "Agregué la función loadDetailedService"
   ✅ git commit -m "feat: Agregar función loadDetailedService"
   ```

3. **Limita primera línea a 50 caracteres**
   ```bash
   ❌ git commit -m "feat(service-detail): Implementar carga de detalles del servicio con modal y manejo de errores completo"
   ✅ git commit -m "feat(service-detail): Implementar carga y modal de detalles"
   ```

4. **Agrupa cambios relacionados**
   ```bash
   ✅ Un commit por feature/fix pequeño
   ❌ Mezclar auth + ui + database en un solo commit
   ```

5. **Revisa antes de commitear**
   ```bash
   git diff --staged  # Ver exactamente qué va en el commit
   ```

---

## 🚀 Git Workflow - Subir Cambios a GitHub ✨ NUEVO

### Pasos Paso a Paso para Subir a GitHub

#### **Paso 1: Verificar Estado de Cambios**

```bash
# Ver archivos modificados
git status

# Ver diferencias detalladas
git diff

# Ver diferencias de archivos específicos
git diff app/src/main/java/com/lossabinos/serviceapp/presentation/
```

#### **Paso 2: Agregar Cambios al Stage**

```bash
# Opción A: Agregar todos los cambios
git add .

# Opción B: Agregar archivos específicos
git add app/src/main/java/com/lossabinos/serviceapp/viewmodel/MechanicsViewModel.kt
git add app/src/main/java/com/lossabinos/serviceapp/screens/home/HomeScreen.kt
git add README.md

# Opción C: Agregar directorios específicos
git add app/src/main/java/com/lossabinos/serviceapp/domain/usecases/
git add app/src/main/java/com/lossabinos/serviceapp/data/mappers/
```

#### **Paso 3: Ver Cambios en Stage**

```bash
# Ver archivos que serán commiteados
git status

# Ver diferencias en staging
git diff --staged
```

#### **Paso 4: Crear Commit con Mensaje Descriptivo**

```bash
# Commit simple
git commit -m "Agregar servicio de detalles del servicio"

# Commit con descripción detallada (recomendado)
git commit -m "feat: Implementar Service Detail con modal

- Agregar endpoint GET /api/v1/mechanics/me/assigned-services/{idService}
- Crear GetDetailedServiceUseCase
- Implementar DetailedServiceResponseDTO
- Agregar StateFlow detailedService en MechanicsViewModel
- Crear modal AlertDialog en HomeScreen
- Agregar estado Idle a Result sealed class
- Mejorar callbacks: solo en Composables (Clean Architecture)
- Actualizar DI con provideGetDetailedServiceUseCase()

BREAKING CHANGE: ServiceCardData ahora no contiene callbacks (callbacks en Composable)"
```

#### **Paso 5: Verificar Log de Commits**

```bash
# Ver últimos commits
git log --oneline -10

# Ver commit específico
git log -1
git show HEAD
```

#### **Paso 6: Subir a GitHub**

```bash
# Opción A: Push a rama actual (main/develop)
git push

# Opción B: Push explícito
git push origin main

# Opción C: Si es la primera vez en esa rama
git push -u origin main

# Opción D: Forzar push (⚠️ cuidado)
git push --force
```

#### **Paso 7: Verificar en GitHub**

```bash
# Abrir en navegador
https://github.com/genaro-velazquez/los-sabinos-service-app-android

# Ver commits
https://github.com/genaro-velazquez/los-sabinos-service-app-android/commits

# Ver cambios en rama
https://github.com/genaro-velazquez/los-sabinos-service-app-android/tree/main
```

---

### 📋 Flujo Completo Recomendado

```bash
# 1. Verificar cambios
git status

# 2. Agregar cambios
git add .

# 3. Revisar staging
git status

# 4. Crear commit con mensaje descriptivo
git commit -m "feat: Implementar Service Detail con modal

Agregar funcionalidad para cargar detalles específicos de un servicio
y mostrarlos en un modal elegante con AlertDialog."

# 5. Ver log
git log --oneline -5

# 6. Subir a GitHub
git push

# 7. Verificar en GitHub (abrir navegador)
```

---

### 🔄 Comandos Útiles Adicionales

```bash
# Ver ramas disponibles
git branch -a

# Cambiar de rama
git checkout develop
git checkout -b feature/new-feature

# Ver cambios no commiteados
git diff HEAD

# Revertir cambios de un archivo
git checkout -- app/src/main/java/...

# Eliminar cambios no staged
git restore app/src/main/java/...

# Ver historial detallado
git log --oneline --graph --all

# Comparar ramas
git diff main develop

# Ver quién cambió qué
git blame app/src/main/java/...

# Ver cambios de un archivo específico
git log --oneline -- app/src/main/java/...
```

---

### 📝 Ejemplo Completo: Tu Caso

```bash
# 1. Verificar estado
git status
# On branch main
# Changes not staged for commit:
#   modified:   README.md
#   modified:   app/src/.../MechanicsViewModel.kt
#   modified:   app/src/.../HomeScreen.kt
#   new file:   app/src/.../GetDetailedServiceUseCase.kt
#   new file:   app/src/.../DetailedServiceResponseDTO.kt

# 2. Agregar cambios
git add .

# 3. Commit
git commit -m "feat(service-detail): Implementar carga y modal de detalles del servicio

- Agregar GetDetailedServiceUseCase para cargar detalles
- Implementar detailedService() en repositorio
- Agregar StateFlow detailedService en MechanicsViewModel
- Crear modal AlertDialog en HomeScreen
- Mejorar callbacks: solo en Composables
- Actualizar README con documentación completa"

# 4. Push
git push

# 5. Ver en GitHub (abrir en navegador)
open "https://github.com/genaro-velazquez/los-sabinos-service-app-android"
```

---

## 🔍 Debugging & Logging

### CURL Logging para Service Detail

```bash
# En Logcat buscar:
curl -X GET 'https://lossabinos-e9gvbjfrf9h5dphf.eastus2-01.azurewebsites.net/api/v1/mechanics/me/assigned-services/SERVICE_ID_HERE' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...' \
  -H 'X-App-Version: 1.0.0' \
  -H 'X-Android-Version: 14' \
  -H 'X-LOS-SABINOS-PLATFORM-TYPE: app'
```

### ViewModel Logging

```kotlin
// MechanicsViewModel.kt
fun loadDetailedService(idService: String) {
    viewModelScope.launch {
        try {
            _detailedService.value = Result.Loading
            println("🔄 Cargando detalles del servicio: $idService")
            val response = getDetailedServiceUseCase.execute(idService = idService)
            println("✅ Detalles cargados: ${response.serviceExecutionId}")
            _detailedService.value = Result.Success(response)
        } catch (e: Exception) {
            println("❌ Error: ${e.message}")
            _detailedService.value = Result.Error(e)
        }
    }
}
```

---

## 📊 Métricas del Proyecto - Actualizado ✨

- **ViewModels**: 4 (Splash, Login, Home, Mechanics)
- **UseCases**: 5+ (Authentication, Preferences, Mechanics Services, Detailed Service)
- **Repositories**: 4 (Authentication, UserPreferences, Mechanics, Local)
- **Componentes Atomic Design**: 28+ (9 Atoms, 6 Molecules, 6+ Organisms)
- **Servicios Retrofit**: 2 (Authentication, Mechanics)
- **Endpoints Implementados**: 3 (Login, AssignedServices, DetailedService)
- **Líneas de código**: ~9000+ 
- **Versión**: 1.6.0
- **Status**: Service Detail implementado con arquitectura limpia ✨

---

**Última actualización:** Noviembre 30, 2025  
**Versión:** 1.6.0  
**Estado:** Service Detail implementado con modal y callbacks en Composables ✨