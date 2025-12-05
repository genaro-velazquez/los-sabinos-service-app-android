# 🔧 Los Sabinos - Sistema de Gestión de Servicios de Mantenimiento

Aplicación Android nativa para gestionar servicios de mantenimiento con funcionalidad offline-first, captura de evidencia, sincronización automática de datos, validación automática de sesiones e integración con backend en tiempo real.

---

## 📋 Tabla de Contenidos

- [Características](#características)
- [Room Database - Offline-First](#-room-database---offline-first-✨-nuevo)
- [Arquitectura](#arquitectura)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Flujo de Autenticación](#flujo-de-autenticación)
- [Service Detail - Detalles del Servicio](#service-detail---detalles-del-servicio-✨-nuevo)
- [Estado del Proyecto](#estado-del-proyecto)
- [Git Workflow](#git-workflow---subir-cambios-a-github)
- [Conventional Commits](#conventional-commits---tipos-de-commits)

---

## ✨ Características

### Core Features
- ✅ **Autenticación** con correo y contraseña
- ✅ **Integración con backend Azure** para autenticación
- ✅ **Validación automática de sesión** con SplashScreen
- ✅ **Modal de confirmación** elegante para logout
- ✅ **Logout seguro** con limpieza completa de datos
- ✅ **Datos reales del usuario** en HomePage (nombre, ubicación)

### API & Backend Integration
- ✅ **Carga de servicios en tiempo real** desde API
- ✅ **Bearer Token Authentication** - Headers con token automático
- ✅ **Manejo de estados** (Loading, Success, Error, Idle) con Flow reactivo
- ✅ **Logging CURL** completo para debugging
- ✅ **Detalle de Servicio** - Carga datos específicos con modal ✨ NUEVO

### Room Database & Offline-First ✨ NUEVO v1.7.0
- ✅ **Room Database** - Persistencia local con SQLite
- ✅ **6 Entidades** - Mecánico, Servicio, Tipo, Zona, Vehículo, Órdenes
- ✅ **Sincronización automática** - API → Room al hacer login
- ✅ **Lectura offline** - HomeScreen lee datos de Room (sin conexión)
- ✅ **Mappers automáticos** - DTO → Entity → Domain Model
- ✅ **Migrations transparentes** - fallbackToDestructiveMigration para desarrollo
- ✅ **UseCase consolidado** - GetLocalInitialDataUseCase para traer todo
- ✅ **Estados de sincronización** - SYNCED, PENDING, ERROR
- ✅ **Arquitectura offline-first** - App funciona sin internet

### UI Components
- ✅ **ActionCards** - Tarjetas de acciones rápidas
- ✅ **Service List** - Listado de servicios asignados con UI adaptable
- ✅ **Service Detail Modal** - Modal elegante con detalles del servicio ✨ NUEVO
- ✅ **Indicadores y métricas** en pantalla Home
- ✅ **Atomic Design** para componentes UI reutilizables

### Foundation
- ✅ **Inyección de dependencias con Hilt**
- ✅ **Clean Architecture + MVVM + Repository Pattern**
- ✅ **Coroutines + Flow** para operaciones asincrónicas
- ✅ **Callbacks en Composables** - No en Data Classes ✨ NUEVO

---

## 🗄️ Room Database - Offline-First ✨ NUEVO

### Descripción

**Room Database** implementa una arquitectura **offline-first** que permite a la aplicación funcionar sin conexión a internet. Los datos se sincronizan automáticamente cuando hay conexión.

### Flujo de Sincronización

```
┌─────────────────────────────────────────────────────────────┐
│                    ARQUITECTURA OFFLINE-FIRST               │
└─────────────────────────────────────────────────────────────┘

1. LOGIN EXITOSO
   ├─ loadInitialData() ejecuta
   ├─ GET /api/v1/mechanics/me/initial-data (API)
   ├─ Response → InitialDataResponse
   └─ Guardar en Room (Database)
      ├─ mechanics tabla
      ├─ assigned_services tabla
      ├─ service_types tabla
      ├─ zones tabla
      ├─ vehicles tabla
      └─ work_orders tabla

2. HOMEPAGE ABIERTO (SIN CONEXIÓN ✨)
   ├─ loadLocalData() ejecuta
   ├─ Leer desde Room (Database)
   │  ├─ SELECT * FROM assigned_services
   │  ├─ SELECT * FROM service_types
   │  └─ SELECT * FROM mechanics
   ├─ Mapear Room Entities → Domain Models
   └─ Mostrar datos offline

3. SINCRONIZACIÓN MANUAL (OPCIONAL)
   ├─ Usuario presiona "Sincronizar"
   ├─ loadInitialData() → API
   └─ Actualizar Room con datos nuevos
```

### 6 Entidades (Tablas)

| Tabla | Campos | Relación |
|-------|--------|----------|
| **Mechanic** | id, name, email, company_id | 1 a N con Servicios |
| **AssignedService** | id, work_order_id, service_type_id, status, priority, scheduled_start/end | N a 1 con WorkOrder, ServiceType |
| **ServiceType** | id, name, estimated_duration | 1 a N con Servicios |
| **WorkOrder** | id, mechanic_id, status, priority | 1 a N con Servicios |
| **Zone** | id, name, code, region | Referencia en WorkOrder |
| **Vehicle** | id, plate, model, mechanic_id | 1 a N con Mecánico |

### Implementación - Archivos Principales

#### 1. Room Entities (data/local/database/entity/)
```kotlin
@Entity(tableName = "mechanics")
data class MechanicEntity(id: String, name: String, email: String, companyId: String)

@Entity(tableName = "assigned_services")
data class AssignedServiceEntity(id: String, workOrderId: String, serviceTypeId: String, 
    status: String, priority: String, scheduledStart: String?, scheduledEnd: String?)

// ServiceTypeEntity, ZoneEntity, VehicleEntity, WorkOrderEntity...
```

#### 2. DAOs (data/local/database/dao/InitialDataDao.kt)
```kotlin
@Dao
interface InitialDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMechanic(mechanic: MechanicEntity)
    
    @Query("SELECT * FROM assigned_services")
    suspend fun getAllAssignedServices(): List<AssignedServiceEntity>
    
    @Query("SELECT * FROM mechanics LIMIT 1")
    suspend fun getMechanic(): MechanicEntity?
    
    @Query("SELECT * FROM service_types")
    suspend fun getAllServiceTypes(): List<ServiceTypeEntity>
}
```

#### 3. AppDatabase (data/local/database/AppDatabase.kt)
```kotlin
@Database(
    entities = [MechanicEntity::class, AssignedServiceEntity::class, 
        ServiceTypeEntity::class, ZoneEntity::class, VehicleEntity::class, WorkOrderEntity::class],
    version = 2  // ✨ Incrementada para migration
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun initialDataDao(): InitialDataDao
}
```

#### 4. Repository - Guardar en Room (MechanicsRetrofitRepository.kt)
```kotlin
override suspend fun saveToRoom(response: InitialDataResponse) {
    val mechanicEntity = response.mechanic.toEntity()
    val serviceEntities = response.assignedServices.map { it.toEntity() }
    val typeEntities = response.serviceTypes.map { it.toEntity() }
    
    initialDataDao.insertMechanic(mechanicEntity)
    initialDataDao.insertAssignedServices(serviceEntities)
    // ... guardar más datos
    println("✅ Datos guardados en Room")
}
```

#### 5. Repository - Leer desde Room (MechanicsRetrofitRepository.kt)
```kotlin
override suspend fun getLocalInitialData(): InitialDataResponse {
    val mechanic = initialDataDao.getMechanic()?.let { Mechanic(id = it.id, ...) }
    val assignedServices = initialDataDao.getAllAssignedServices().map { it.toDomain() }
    val serviceTypes = initialDataDao.getAllServiceTypes().map { it.toDomain() }
    
    return InitialDataResponse(mechanic, assignedServices, serviceTypes, syncMetadata)
}
```

#### 6. Use Case (GetLocalInitialDataUseCase.kt)
```kotlin
class GetLocalInitialDataUseCase(private val repository: MechanicsRepository) {
    suspend operator fun invoke(): InitialDataResponse? {
        return try {
            repository.getLocalInitialData()
        } catch (e: Exception) {
            println("❌ Error: ${e.message}")
            null
        }
    }
}
```

#### 7. ViewModel (MechanicsViewModel.kt) ✨ ACTUALIZADO
```kotlin
@HiltViewModel
class MechanicsViewModel @Inject constructor(
    private val getMechanicsServicesUseCase: GetMechanicsServicesUseCase,
    private val getLocalInitialDataUseCase: GetLocalInitialDataUseCase
) : ViewModel() {
    
    private val _localInitialData = MutableStateFlow<Result<InitialDataResponse>>(Result.Idle)
    val localInitialData: StateFlow<Result<InitialDataResponse>> = _localInitialData.asStateFlow()
    
    fun loadLocalData() {
        viewModelScope.launch {
            try {
                _localInitialData.value = Result.Loading
                val response = getLocalInitialDataUseCase()
                _localInitialData.value = Result.Success(data = response!!)
                println("✅ Datos de Room cargados")
            } catch (e: Exception) {
                _localInitialData.value = Result.Error(exception = e)
            }
        }
    }
}
```

#### 8. HomeScreen (Lectura Offline) ✨ ACTUALIZADO
```kotlin
@Composable
fun HomeScreen(mechanicsViewModel: MechanicsViewModel = hiltViewModel()) {
    val localInitialDataState = mechanicsViewModel.localInitialData.collectAsState().value
    
    LaunchedEffect(Unit) {
        mechanicsViewModel.loadLocalData()  // ✨ Lee de Room
    }
    
    when {
        localInitialDataState is Result.Success -> {
            val data = (localInitialDataState as Result.Success).data
            HomeHeaderSection(userName = data.mechanic.name)
            MetricsSection(inProgressCount = data.syncMetadata.inProgressServices.toString())
            // ... mostrar servicios...
        }
        localInitialDataState is Result.Loading -> CircularProgressIndicator()
        localInitialDataState is Result.Error -> Text("Error cargando datos")
    }
}
```

#### 9. DI (DatabaseModule.kt) ✨ NUEVO
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "los_sabinos.db")
            .fallbackToDestructiveMigration(true)  // ✨ Migrations automáticas
            .build()
    }
    
    @Singleton
    @Provides
    fun provideInitialDataDao(database: AppDatabase): InitialDataDao {
        return database.initialDataDao()
    }
}
```

---

## 🏗️ Arquitectura

**Clean Architecture + MVVM + Repository Pattern + Offline-First**

```
Presentation Layer (UI)
    ↓ (observa estados)
    ↓
ViewModel (MechanicsViewModel)
    ↓ (ejecuta casos de uso)
    ↓
Domain Layer (UseCases)
    ↓ (abstracción)
    ↓
Repository Interface (IMechanicsRepository)
    ↓ (implementación)
    ↓
Data Layer
├─ Remote (Retrofit API)
└─ Local (Room Database)
```

### Características de Arquitectura
- **Offline-First**: Datos se guardan localmente primero
- **Reactive**: Flow y StateFlow para estados reactivos
- **Clean**: Separación clara de capas
- **Testeable**: Inyección de dependencias con Hilt

---

## 📁 Estructura del Proyecto - Actualizada ✨

```
app/src/main/java/com/lossabinos/serviceapp/
│
├── data/
│   ├── local/                              ✨ NUEVO
│   │   ├── database/
│   │   │   ├── AppDatabase.kt
│   │   │   ├── dao/
│   │   │   │   └── InitialDataDao.kt
│   │   │   └── entity/ (6 entities)
│   │   └── mappers/
│   │       └── InitialDataMappers.kt
│   │
│   ├── remote/
│   │   ├── services/ (Retrofit)
│   │   └── dto/ (Data Transfer Objects)
│   │
│   ├── repositories/
│   │   └── MechanicsRetrofitRepository.kt  ✨ ACTUALIZADO
│   │
│   └── utils/
│       ├── HeadersMaker.kt
│       └── RetrofitResponseValidator.kt
│
├── domain/
│   ├── repositories/
│   │   └── MechanicsRepository.kt          ✨ ACTUALIZADO
│   │
│   ├── usecases/
│   │   ├── GetMechanicsServicesUseCase.kt
│   │   ├── GetDetailedServiceUseCase.kt
│   │   └── GetLocalInitialDataUseCase.kt   ✨ NUEVO
│   │
│   ├── models/ (Domain entities)
│   │   └── InitialDataResponse.kt          ✨ NUEVO
│   │
│   └── common/
│       └── Result.kt (sealed class con Idle)
│
├── presentation/
│   ├── viewmodels/
│   │   └── MechanicsViewModel.kt           ✨ ACTUALIZADO
│   │
│   ├── screens/
│   │   └── home/
│   │       └── HomeScreen.kt               ✨ ACTUALIZADO
│   │
│   └── ui/
│       ├── atoms, molecules, organisms
│       └── templates
│
├── di/
│   ├── DatabaseModule.kt                   ✨ NUEVO
│   ├── UseCaseModule.kt                    ✨ ACTUALIZADO
│   └── ... otros modules
│
└── app/
    └── LosSabinosApplication.kt
```

---

## 🔐 Flujo de Autenticación - Actualizado ✨

### Login con Sincronización Automática

```
LoginScreen
    ↓
Usuario ingresa credenciales
    ↓
LoginViewModel.onEvent(LoginEvent.LoginClicked)
    ↓
validateForm() ✓
    ↓
EmailPasswordLoginUseCase.execute()
    ↓
API validación ✓
    ↓
Token guardado en SharedPrefs
    ↓
loadInitialData() ejecuta  ✨ SINCRONIZACIÓN
│  ├─ GET /api/v1/mechanics/me/initial-data
│  ├─ Response → InitialDataResponse
│  └─ saveToRoom(response)
│     ├─ Insert mechanics
│     ├─ Insert assigned_services
│     └─ Insert service_types
│
✅ Todos los datos en Room
    ↓
NavigateToHome
```

### HomeScreen con Lectura Offline ✨ NUEVO

```
HomeScreen abierto
    ↓
LaunchedEffect detecta inicio
    ↓
loadLocalData() ejecuta  ✨ LECTURA OFFLINE
│  ├─ GetLocalInitialDataUseCase()
│  └─ initialDataDao.getMechanic()
│     initialDataDao.getAllAssignedServices()
│     initialDataDao.getAllServiceTypes()
│
✅ Datos de Room cargados
    ↓
UI muestra datos (CON O SIN CONEXIÓN)
```

---

## 🎯 Service Detail - Detalles del Servicio ✨ NUEVO

### Flujo Completo

```
HomePage - Service List
    ↓
Usuario hace click en "Completar"
    ↓
onCompleteClick callback
    ↓
mechanicsViewModel.loadDetailedService(serviceId)
    ↓
GetDetailedServiceUseCase.execute()
    ↓
GET /api/v1/mechanics/me/assigned-services/{idService}
    ↓
DetailedServiceResponseDTO mapea JSON
    ↓
_detailedService.value = Result.Success(response)
    ↓
LaunchedEffect abre modal
    ↓
ServiceDetailModal se muestra ✨
```

---

## 📊 Estado del Proyecto - Actualizado ✨

### ✅ v1.7.0 (Completado) - Room Database & Offline-First

#### Implementación Database
- [x] Room Database configurado (SQLite)
- [x] 6 Entities (Mechanic, Service, Type, Zone, Vehicle, WorkOrder)
- [x] InitialDataDao - CRUD operations
- [x] AppDatabase version = 2
- [x] fallbackToDestructiveMigration (migrations automáticas)

#### Implementación Data Layer
- [x] InitialDataMappers (Entity → Domain)
- [x] saveToRoom() - Guardar API response en Room
- [x] getLocalInitialData() - Lectura offline
- [x] Repository con métodos locales

#### Implementación Domain Layer
- [x] GetLocalInitialDataUseCase
- [x] InitialDataResponse modelo consolidado
- [x] Repository interface actualizada

#### Implementación Presentation Layer
- [x] MechanicsViewModel.loadLocalData()
- [x] HomeScreen lee de Room
- [x] Estados separados (API vs Local)
- [x] LaunchedEffect para cargar datos

#### DI & Configuration
- [x] DatabaseModule.kt (provideAppDatabase)
- [x] UseCaseModule actualizado
- [x] RepositoryModule ligado a Room

#### Features Implementadas
- [x] Sincronización automática (API → Room) al login
- [x] Lectura offline (Room → UI) en HomeScreen
- [x] Mappers automáticos (DTO → Entity → Domain)
- [x] Migrations transparentes
- [x] Estados de sincronización
- [x] App funciona sin conexión ✨

### ✅ v1.6.0 (Completado) - Service Detail & Clean Architecture

- [x] DetailedServiceResponseDTO
- [x] GetDetailedServiceUseCase
- [x] ServiceDetailModal con AlertDialog
- [x] MechanicsViewModel.loadDetailedService()
- [x] Callbacks en Composables (Clean Architecture)

### 🚧 v1.8.0 (Próximo)

- [ ] Panel de tareas (checklist) con progreso
- [ ] Captura de evidencia (imágenes)
- [ ] Sincronización de imágenes
- [ ] Escaneo QR/Barcode

---

## 📝 Conventional Commits - Tipos de Commits

### Formato Base

```
tipo(alcance): descripción breve

[cuerpo opcional - descripción detallada]

[pie opcional - información adicional, breaking changes, etc]
```

### Tipos Principales

| Tipo | Descripción | Ejemplo |
|------|-------------|---------|
| **feat** | Nueva característica | `feat(database): Implementar Room Database` |
| **fix** | Corrección de bug | `fix(ui): Corregir altura de componente` |
| **refactor** | Cambio sin nuevas características | `refactor(callbacks): Mover a Composables` |
| **docs** | Cambios en documentación | `docs: Actualizar README` |
| **chore** | Cambios en config/deps | `chore(gradle): Actualizar dependencias` |

### Alcances Recomendados
```
- auth           : Autenticación
- api            : Backend integration
- database       : Room & persistencia        ✨ NUEVO
- sync           : Sincronización de datos   ✨ NUEVO
- ui             : Componentes UI
- viewmodel      : ViewModels
- service-detail : Detalles del servicio
- di             : Inyección de dependencias
- readme         : Documentación
```

---

## 🚀 Git Workflow - Subir Cambios a GitHub

### Pasos Completos

```bash
# 1. Ver cambios
git status
git diff

# 2. Agregar cambios
git add .

# 3. Revisar staging
git status

# 4. Crear commit
git commit -m "feat(database): Implementar Room Database offline-first

Cambios principales:
- Agregar 6 entities (Mechanic, Service, Type, Zone, Vehicle, WorkOrder)
- Implementar InitialDataDao con operaciones CRUD
- Crear AppDatabase con version = 2
- Agregar InitialDataMappers (Entity → Domain)
- Implementar saveToRoom() en MechanicsRepository
- Implementar getLocalInitialData() para lectura offline
- Crear GetLocalInitialDataUseCase consolidado
- Agregar loadLocalData() en MechanicsViewModel
- Actualizar HomeScreen para leer de Room
- Configurar DatabaseModule con fallbackToDestructiveMigration
- Sincronización automática (API → Room) al login
- App ahora funciona sin conexión a internet ✨"

# 5. Ver log
git log --oneline -5

# 6. Subir a GitHub
git push

# 7. Verificar en GitHub
```

### Comandos Útiles

```bash
# Ver ramas
git branch -a

# Ver cambios específicos
git diff app/src/main/java/com/lossabinos/serviceapp/data/

# Revertir cambios
git checkout -- archivo.kt

# Ver histórico
git log --oneline --graph --all

# Comparar con rama anterior
git diff main develop
```

---

## 📊 Métricas del Proyecto

- **ViewModels**: 4 (Splash, Login, Home, Mechanics)
- **UseCases**: 6+ (Auth, Preferences, Services, DetailedService, LocalData)
- **Room Entities**: 6 (Mechanic, Service, Type, Zone, Vehicle, WorkOrder)
- **DAOs**: 1+ (InitialDataDao)
- **Servicios Retrofit**: 2 (Authentication, Mechanics)
- **Endpoints**: 3 (Login, AssignedServices, DetailedService)
- **UI Componentes**: 28+ (Atomic Design)
- **Líneas de código**: ~12000+
- **Versión**: 1.7.0
- **Status**: Room Database + Offline-First completo ✨

---

**Última actualización:** Diciembre 5, 2025  
**Versión:** 1.7.0  
**Estado:** Room Database & Offline-First implementado ✨  
**Arquitectura:** Clean Architecture + MVVM + Repository + Offline-First