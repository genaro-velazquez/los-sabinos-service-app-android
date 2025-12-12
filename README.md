# 🔧 Los Sabinos - Sistema de Gestión de Servicios de Mantenimiento

Aplicación Android nativa para gestionar servicios de mantenimiento con funcionalidad offline-first, captura de evidencia, sincronización automática de datos, validación automática de sesiones e integración con backend en tiempo real.

---

## ✨ Características Principales

### Core Features
- ✅ **Autenticación** con correo y contraseña
- ✅ **Integración con backend Azure** para autenticación
- ✅ **Validación automática de sesión** con SplashScreen
- ✅ **Logout seguro** con limpieza completa de datos

### Room Database & Offline-First ✨ v1.7.0
- ✅ **Room Database** - Persistencia local con SQLite
- ✅ **10 Entidades** - 6 base + 4 checklist
- ✅ **Sincronización automática** - API → Room al hacer login
- ✅ **Lectura offline** - App funciona sin conexión

### Checklist Progress Implementation ✨ v1.8.0
- ✅ **4 Nuevas Entidades** - ActivityProgress, ActivityEvidence, ObservationResponse, ServiceFieldValue
- ✅ **ChecklistRepository** - Gestión de tareas y progreso
- ✅ **@Serializable Domain Models** - Template, Section, Activity, Observation, ServiceField
- ✅ **Marcado de tareas** - Completar activities con timestamp
- ✅ **Captura de evidencia** - Guardar fotos/videos
- ✅ **Respuestas a observaciones** - Guardar respuestas a preguntas
- ✅ **Cálculo de progreso** - Porcentaje de tareas completadas (0-100%)

---

## 🗄️ Base de Datos - v1.8.0

### 10 Entidades (6 base + 4 checklist)

#### Base (v1.7.0)
- **Mechanic** - Datos del mecánico
- **AssignedService** - Servicios asignados
- **ServiceType** - Tipos de servicio
- **WorkOrder** - Órdenes de trabajo
- **Zone** - Zonas de servicio
- **Vehicle** - Vehículos

#### Checklist (v1.8.0) ✨
- **ActivityProgress** - Progreso de tareas
- **ActivityEvidence** - Fotos/videos de tareas
- **ObservationResponse** - Respuestas a preguntas
- **ServiceFieldValue** - Valores ingresados

### Migration 2 → 3

```sql
-- Tabla: activity_progress
CREATE TABLE activity_progress (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    assignedServiceId TEXT NOT NULL,
    sectionIndex INTEGER NOT NULL,
    activityIndex INTEGER NOT NULL,
    activityDescription TEXT NOT NULL,
    requiresEvidence INTEGER NOT NULL,
    completed INTEGER DEFAULT 0,
    completedAt TEXT
);

-- Tabla: activity_evidence
CREATE TABLE activity_evidence (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    activityProgressId INTEGER NOT NULL,
    filePath TEXT NOT NULL,
    fileType TEXT DEFAULT 'image',
    timestamp TEXT NOT NULL
);

-- Tabla: observation_response
CREATE TABLE observation_response (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    assignedServiceId TEXT NOT NULL,
    sectionIndex INTEGER NOT NULL,
    observationIndex INTEGER NOT NULL,
    observationDescription TEXT NOT NULL,
    response TEXT,
    timestamp TEXT
);

-- Tabla: service_field_value
CREATE TABLE service_field_value (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    assignedServiceId TEXT NOT NULL,
    fieldIndex INTEGER NOT NULL,
    fieldLabel TEXT NOT NULL,
    fieldType TEXT NOT NULL,
    required INTEGER NOT NULL,
    value TEXT,
    timestamp TEXT
);
```

---

## 📁 Estructura del Proyecto

```
app/src/main/java/com/lossabinos/serviceapp/
│
├── data/
│   ├── local/
│   │   ├── database/
│   │   │   ├── AppDatabase.kt (v3)
│   │   │   ├── dao/
│   │   │   │   ├── InitialDataDao.kt
│   │   │   │   └── ChecklistDao.kt ✨
│   │   │   └── entity/
│   │   │       ├── MechanicEntity.kt
│   │   │       ├── AssignedServiceEntity.kt
│   │   │       ├── ServiceTypeEntity.kt
│   │   │       ├── ZoneEntity.kt
│   │   │       ├── VehicleEntity.kt
│   │   │       ├── WorkOrderEntity.kt
│   │   │       └── ChecklistEntities.kt ✨
│   │   └── mappers/
│   │
│   ├── repositories/
│   │   ├── MechanicsRetrofitRepository.kt
│   │   └── local/
│   │       └── ChecklistRepository.kt ✨
│   │
│   └── remote/
│
├── domain/
│   ├── models/
│   │   ├── Mechanic.kt
│   │   ├── Template.kt (@Serializable) ✨
│   │   ├── Section.kt (@Serializable) ✨
│   │   ├── Activity.kt (@Serializable) ✨
│   │   ├── Observation.kt (@Serializable) ✨
│   │   └── ServiceField.kt (@Serializable) ✨
│   │
│   ├── usecases/
│   │   ├── GetMechanicsServicesUseCase.kt
│   │   ├── GetDetailedServiceUseCase.kt
│   │   └── GetLocalInitialDataUseCase.kt
│   │
│   └── repositories/
│
├── presentation/
│   ├── viewmodels/
│   │   ├── MechanicsViewModel.kt
│   │   └── ChecklistViewModel.kt ✨
│   │
│   └── screens/
│
├── di/
│   ├── DatabaseModule.kt (MIGRATION_2_TO_3) ✨
│   ├── RepositoryModule.kt ✨
│   └── ...
│
└── app/
    └── LosSabinosApplication.kt
```

---


## 📊 Estado del Proyecto

**Versión:** 1.8.0  
**Estado:** Checklist Progress Implementation ✨ Completo  
**Base de Datos:** 10 entidades, migration 2→3  
**Arquitectura:** Clean Architecture + MVVM + Repository + Offline-First

### ✅ Implementado
- [x] 4 nuevas entities Room
- [x] ChecklistRepository completo
- [x] ChecklistDao con 4 sub-DAOs
- [x] Domain models @Serializable
- [x] Migration 2→3
- [x] RepositoryModule actualizado
- [x] DatabaseModule actualizado

### 🚧 Próximo (v1.9.0)
- [ ] UI Panel de tareas visual
- [ ] Camera integration
- [ ] QR/Barcode scanning
- [ ] Sincronización de imágenes

---

**Última actualización:** Diciembre 11, 2025  
**Autor:** Equipo Los Sabinos