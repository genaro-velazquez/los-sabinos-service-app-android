# 🔧 Sistema de Gestión de Servicios de Mantenimiento para Mecánicos

Aplicación Android nativa para gestionar servicios de mantenimiento con funcionalidad offline-first, captura de evidencia y sincronización automática de datos.

---

## 📋 Tabla de Contenidos

- [Características](#características)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Arquitectura](#arquitectura)
- [Tecnologías](#tecnologías)
- [Cómo Ejecutar](#cómo-ejecutar)
- [Flujo Principal](#flujo-principal)
- [Estado del Proyecto](#estado-del-proyecto)

---

## ✨ Características

- ✅ **Autenticación** con correo y contraseña (validaciones cliente)
- ✅ **Indicadores y métricas** en pantalla Home
- ✅ **Escaneo de códigos de barras/QR** para asignar servicios
- ✅ **Panel de tareas** con checklist interactivo
- ✅ **Captura de evidencia** (imágenes con cámara)
- ✅ **Offline-First** con sincronización automática
- ✅ **Manejo robusto de errores y reintentos**
- ✅ **UI moderna** con Jetpack Compose

---

## 🔧 Requisitos

### Mínimos del Sistema
- **Android Studio** 2023.1 o superior
- **JDK 17** o superior
- **Android SDK** API 28+ (Android 9.0 Pie)
- **Gradle** 8.0+

### Recomendado
- Dispositivo/Emulador con Android 10.0 (API 29) o superior
- 4GB RAM disponible
- Git instalado

---

## 📦 Instalación

### 1. Clonar el Repositorio

```bash
git clone https://github.com/tu-usuario/mecanicos-app.git
cd mecanicos-app
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

## 📁 Estructura del Proyecto

```
app/src/main/java/com/lossabinos/serviceapp/
│
├── data/                          # 🗄️ Capa de Datos
│   ├── local/
│   │   ├── database/              # Room Database
│   │   │   ├── AppDatabase.kt
│   │   │   └── dao/               # Data Access Objects
│   │   │       ├── MecanicoDao.kt
│   │   │       ├── ServicioDao.kt
│   │   │       ├── TareaDao.kt
│   │   │       └── EvidenciaDao.kt
│   │   └── entity/                # Entidades de BD
│   │       ├── MecanicoEntity.kt
│   │       ├── ServicioEntity.kt
│   │       ├── TareaEntity.kt
│   │       └── EvidenciaEntity.kt
│   │
│   ├── remote/
│   │   ├── api/                   # Retrofit APIs
│   │   │   ├── AuthApi.kt
│   │   │   ├── ServicioApi.kt
│   │   │   └── EvidenciaApi.kt
│   │   └── dto/                   # Data Transfer Objects
│   │       ├── ServicioDto.kt
│   │       ├── TareaDto.kt
│   │       └── EvidenciaDto.kt
│   │
│   ├── repository/                # Repositorios (Data Layer)
│   │   ├── AuthRepository.kt
│   │   ├── ServicioRepository.kt
│   │   └── TareaRepository.kt
│   │
│   └── sync/                      # Sincronización
│       ├── SyncManager.kt
│       └── SyncWorker.kt
│
├── domain/                        # 💼 Capa de Dominio (Lógica de Negocio)
│   ├── model/                     # Modelos de Dominio
│   │   ├── Mecanico.kt
│   │   ├── Servicio.kt
│   │   ├── Tarea.kt
│   │   └── Evidencia.kt
│   │
│   ├── repository/                # Interfaces de Repositorios
│   │   ├── IAuthRepository.kt
│   │   ├── IServicioRepository.kt
│   │   └── ITareaRepository.kt
│   │
│   └── usecase/                   # Casos de Uso
│       ├── auth/
│       │   ├── LoginUseCase.kt
│       │   └── LogoutUseCase.kt
│       ├── servicio/
│       │   ├── ObtenerServiciosUseCase.kt
│       │   ├── EscanearCodigoBarrasUseCase.kt
│       │   └── ObtenerServicioDetailUseCase.kt
│       └── tarea/
│           ├── ObtenerTareasUseCase.kt
│           ├── CompletarTareaUseCase.kt
│           └── GuardarEvidenciaUseCase.kt
│
├── presentation/                  # 🎨 Capa de Presentación (UI/ViewModel)
│   ├── viewmodel/
│   │   ├── AuthViewModel.kt
│   │   ├── HomeViewModel.kt
│   │   ├── ServiciosViewModel.kt
│   │   ├── TareasViewModel.kt
│   │   └── EscaneoViewModel.kt
│   │
│   └── ui/
│       ├── screens/
│       │   ├── login/
│       │   │   ├── LoginScreen.kt
│       │   │   └── LoginViewModel.kt
│       │   ├── home/
│       │   │   ├── HomeScreen.kt
│       │   │   └── HomeViewModel.kt
│       │   ├── escaneo/
│       │   │   ├── EscaneoScreen.kt
│       │   │   └── EscaneoViewModel.kt
│       │   ├── servicios/
│       │   │   ├── ServiciosScreen.kt
│       │   │   └── ServiciosViewModel.kt
│       │   └── tareas/
│       │       ├── TareasScreen.kt
│       │       ├── CapturadorImagenes.kt
│       │       └── VisorEvidencias.kt
│       │
│       ├── components/
│       │   ├── atoms/             # Botones, Inputs, Icons
│       │   │   ├── PrimaryButton.kt
│       │   │   ├── IconTextField.kt
│       │   │   └── ...
│       │   ├── molecules/         # Componentes medianos
│       │   │   ├── PasswordTextField.kt
│       │   │   ├── EmailTextField.kt
│       │   │   └── ...
│       │   └── organisms/         # Componentes complejos
│       │       ├── LoginForm.kt
│       │       ├── ServiciosList.kt
│       │       └── ...
│       │
│       ├── theme/
│       │   ├── Color.kt
│       │   ├── Type.kt
│       │   └── Theme.kt
│       │
│       └── navigation/
│           └── NavGraph.kt
│
├── di/                            # 💉 Inyección de Dependencias (Hilt)
│   ├── DatabaseModule.kt
│   ├── NetworkModule.kt
│   └── RepositoryModule.kt
│
└── utils/                         # 🛠️ Utilidades
    ├── Constants.kt
    ├── ExtensionFunctions.kt
    ├── ConnectivityManager.kt
    └── CameraUtils.kt
```

---

## 🏗️ Arquitectura

### Clean Architecture + MVVM + Repository Pattern

La aplicación sigue principios de arquitectura limpia con separación clara de responsabilidades:

#### **Capas:**

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
   ViewModel (observa State)
        ↓
   UseCase (lógica de negocio)
        ↓
   Repository (abstracción)
        ↓
   Local/Remote Data Sources
        ↓
   Retorna datos → ViewModel → UI se actualiza
```

#### **Ventajas:**

✅ **Testeable** - Cada capa se puede testear independientemente  
✅ **Mantenible** - Cambios en una capa no afectan otras  
✅ **Escalable** - Fácil agregar nuevas funcionalidades  
✅ **Desacoplado** - Dependencias inyectadas con Hilt  
✅ **Reutilizable** - Componentes pueden usarse en múltiples pantallas

---

## 🛠️ Tecnologías

### UI & Composables
- **Jetpack Compose** - UI declarativa moderna
- **Material Design 3** - Componentes estándar
- **Compose Navigation** - Navegación entre pantallas

### Base de Datos
- **Room** - SQLite con abstracción ORM
- **SQLite** - BD local persistente

### Networking
- **Retrofit** - Cliente HTTP
- **OkHttp** - Interceptores y logging
- **Gson** - Serialización JSON

### Inyección de Dependencias
- **Hilt** - DI framework basado en Dagger 2

### Concurrencia
- **Coroutines** - Operaciones asincrónicas
- **Flow** - Streams reactivos

### Sincronización & Background
- **WorkManager** - Tareas en background
- **Custom SyncManager** - Sincronización offline-first

### Cámara y Escaneo
- **CameraX** - API moderna para cámara
- **ML Kit Barcode Scanning** - Escaneo de códigos

### Otras Librerías
- **Coil** - Carga de imágenes eficiente
- **Lifecycle** - Gestión del ciclo de vida

---

## 🚀 Cómo Ejecutar

### Opción 1: Android Studio (Recomendado)

```bash
1. Abre el proyecto en Android Studio
2. Espera a que Gradle sincronice (File → Sync Now)
3. Presiona Shift + F10 o Run → Run 'app'
4. Selecciona emulador o dispositivo conectado
5. Espera a que la app se compile e instale
```

### Opción 2: Terminal

```bash
# Compilar APK debug
./gradlew assembleDebug

# Instalar en dispositivo/emulador
./gradlew installDebug

# Ejecutar directamente
./gradlew run
```

### Opción 3: Crear Emulador

```bash
# Ver emuladores disponibles
emulator -list-avds

# Crear uno nuevo (si no existe)
avdmanager create avd -n MiEmulador -k "system-images;android-34;default;x86_64"

# Iniciar emulador
emulator -avd MiEmulador
```

---

## 📱 Flujo Principal

```
┌─────────────────┐
│   LOGIN SCREEN  │  ← Validar email + contraseña
│                 │     • Email no vacío
│                 │     • Email válido (formato)
│                 │     • Password no vacío
│                 │     • Password >= 6 caracteres
└────────┬────────┘
         │
         ↓
┌─────────────────┐
│  HOME SCREEN    │  ← Indicadores + Botón escanear
│                 │     • Servicios completados
│                 │     • Servicios pendientes
└────────┬────────┘
         │
         ↓
┌─────────────────────────┐
│ ESCANEO QR/BARCODE      │  ← Validar código en backend
└────────┬────────────────┘
         │
         ↓
┌─────────────────────────┐
│ LISTA SERVICIOS         │  ← Servicios asignados
└────────┬────────────────┘
         │
         ↓
┌─────────────────────────┐
│ DETALLE SERVICIO        │  ← Info y tareas
└────────┬────────────────┘
         │
         ↓
┌─────────────────────────┐
│ PANEL TAREAS            │  ← Checklist + evidencia
│ ├─ Tarea 1 ☑            │
│ ├─ Tarea 2 (foto)       │
│ └─ Tarea 3              │
└────────┬────────────────┘
         │
         ↓
┌─────────────────────────┐
│ GUARDAR & SINCRONIZAR   │  ← Sync con backend
└─────────────────────────┘
```

---

## 🎨 Convenciones de Código

### Naming
- **Variables/Funciones**: `camelCase`
- **Clases**: `PascalCase`
- **Constantes**: `UPPER_SNAKE_CASE`
- **Archivos Composable**: `NombrePantalla.kt`

### Estructura de Archivos
- 1 archivo = 1 clase principal
- Composables relacionados en mismo archivo
- Data classes antes que funciones

### Comentarios
```kotlin
/**
 * Descripción clara de la función
 */
fun miFunction() {
    // TODO: Comentario cuando sea necesario
}
```

---

## 📊 Estado del Proyecto

### ✅ Completado (v1.0.0)
- [x] Estructura base con Clean Architecture
- [x] Sistema de autenticación (UI + ViewModel)
- [x] Validaciones en cliente
- [x] Componentes de UI (atoms + molecules)
- [x] Indicador de carga en login
- [x] Manejo de errores y Snackbars

### 🚧 En Desarrollo
- [ ] Integración con API backend
- [ ] Pantalla Home con indicadores
- [ ] Módulo de escaneo QR/Barcode
- [ ] Lista de servicios
- [ ] Panel de tareas
- [ ] Captura de imágenes
- [ ] Sincronización offline-first
- [ ] WorkManager para background sync

### 📅 Próximas Fases
- Tests unitarios
- Tests de integración
- Mejoras de UI/UX
- Documentación de API
- Optimización de rendimiento

---

## 🐛 Troubleshooting

### Error: Gradle sync failed

```bash
# Solución:
./gradlew clean
./gradlew build --refresh-dependencies
```

### Error: Emulador no aparece

```bash
# Solución:
emulator -list-avds
emulator -avd nombre_emulador
```

### Error: Permisos de cámara

```kotlin
// Necesitas agregar en AndroidManifest.xml:
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.INTERNET" />
```

---

## 📝 Commits y Versionado

Usamos [Conventional Commits](https://www.conventionalcommits.org/):

```bash
git commit -m "feat: nueva funcionalidad"      # Nueva feature
git commit -m "fix: corregir bug"              # Bug fix
git commit -m "docs: actualizar readme"        # Documentación
git commit -m "refactor: optimizar código"     # Refactorización
git commit -m "test: agregar tests"            # Tests
git commit -m "chore: actualizar deps"         # Mantenimiento
```

---

## 📧 Contacto

Para preguntas o sugerencias: [tu-email@empresa.com]

---

## 📄 Licencia

Este proyecto está licenciado bajo la Licencia MIT - ver archivo LICENSE para detalles.

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.0.0-beta
