# 🔧 Los Sabinos - Sistema de Gestión de Servicios de Mantenimiento

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
- ✅ **Inyección de dependencias con Hilt**

---

## 🔧 Requisitos

### Mínimos del Sistema
- **Android Studio** 2023.1 o superior
- **JDK 17** o superior
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

## 📁 Estructura del Proyecto

```
app/src/main/java/com/lossabinos/serviceapp/
│
├── LosSabinosApplication.kt       # Entrada de Hilt
├── MainActivity.kt                # Actividad principal
│
├── data/                          # 🗄️ Capa de Datos
│   ├── local/
│   │   ├── database/              # Room Database (próximo)
│   │   ├── dao/                   # Data Access Objects (próximo)
│   │   └── entity/                # Entidades de BD (próximo)
│   ├── remote/
│   │   ├── api/                   # Retrofit APIs (próximo)
│   │   └── dto/                   # Data Transfer Objects (próximo)
│   ├── repository/                # Repositorios (próximo)
│   └── sync/                      # Sincronización (próximo)
│
├── domain/                        # 💼 Capa de Dominio (próximo)
│   ├── model/
│   ├── repository/
│   └── usecase/
│
├── presentation/                  # 🎨 Capa de Presentación
│   ├── viewmodel/
│   │   ├── LoginViewModel.kt      # ✅ Con @HiltViewModel
│   │   └── (otros ViewModels próximo)
│   │
│   └── ui/
│       ├── screens/
│       │   ├── login/
│       │   │   ├── LoginScreen.kt  # ✅ Con hiltViewModel()
│       │   │   ├── LoginForm.kt
│       │   │   └── LoginViewModel.kt (legacy)
│       │   └── (otras pantallas próximo)
│       │
│       ├── components/
│       │   ├── atoms/
│       │   │   ├── PrimaryButton.kt
│       │   │   └── IconTextField.kt
│       │   ├── molecules/
│       │   │   ├── PasswordTextField.kt
│       │   │   └── EmailTextField.kt
│       │   └── organisms/
│       │       └── LoginForm.kt
│       │
│       ├── theme/
│       │   ├── Color.kt
│       │   ├── Type.kt
│       │   └── Theme.kt
│       │
│       └── navigation/
│           └── NavGraph.kt (próximo)
│
├── di/                            # 💉 Inyección de Dependencias (Hilt)
│   ├── AppModule.kt              # ✅ Módulo principal
│   ├── ViewModelModule.kt         # ✅ Módulo de ViewModels
│   └── RepositoryModule.kt        # ✅ Módulo de Repositorios
│
└── utils/                         # 🛠️ Utilidades
    ├── Constants.kt
    ├── ExtensionFunctions.kt
    └── (más próximo)
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
   ViewModel (observa State con Flow)
        ↓
   UseCase (lógica de negocio)
        ↓
   Repository (abstracción de datos)
        ↓
   Local/Remote Data Sources
        ↓
   Retorna datos → ViewModel → UI se actualiza
```

---

## 🛠️ Tecnologías

### UI & Composables
- **Jetpack Compose** - UI declarativa moderna
- **Material Design 3** - Componentes estándar
- **Compose Navigation** - Navegación entre pantallas

### Inyección de Dependencias
- **Hilt** - DI framework basado en Dagger 2 (✅ INTEGRADO)

### Base de Datos
- **Room** - SQLite con abstracción ORM (próximo)
- **SQLite** - BD local persistente (próximo)

### Networking
- **Retrofit** - Cliente HTTP (próximo)
- **OkHttp** - Interceptores y logging (próximo)
- **Gson** - Serialización JSON (próximo)

### Concurrencia
- **Coroutines** - Operaciones asincrónicas
- **Flow** - Streams reactivos

### Sincronización & Background
- **WorkManager** - Tareas en background (próximo)
- **Custom SyncManager** - Sincronización offline-first (próximo)

### Cámara y Escaneo
- **CameraX** - API moderna para cámara (próximo)
- **ML Kit Barcode Scanning** - Escaneo de códigos (próximo)

### Otras Librerías
- **Coil** - Carga de imágenes eficiente (próximo)
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
┌─────────────────────┐
│   LOGIN SCREEN      │  ← Validaciones en cliente
│                     │     • Email no vacío
│                     │     • Email formato válido
│                     │     • Password no vacío
│                     │     • Password >= 6 caracteres
│                     │     • Indicador de carga
└────────┬────────────┘
         │
         ↓
┌─────────────────────┐
│   HOME SCREEN       │  ← Indicadores + Botón escanear
│                     │     (Próximo módulo)
└────────┬────────────┘
         │
         ↓
┌─────────────────────┐
│ ESCANEO QR/BARCODE  │  ← Validar código
│                     │     (Próximo módulo)
└────────┬────────────┘
         │
         ↓
┌─────────────────────┐
│ LISTA SERVICIOS     │  ← Servicios asignados
│                     │     (Próximo módulo)
└────────┬────────────┘
         │
         ↓
┌─────────────────────┐
│ DETALLE SERVICIO    │  ← Info y tareas
│                     │     (Próximo módulo)
└────────┬────────────┘
         │
         ↓
┌─────────────────────┐
│ PANEL TAREAS        │  ← Checklist + evidencia
│ ├─ Tarea 1 ☑        │     (Próximo módulo)
│ ├─ Tarea 2 (foto)   │
│ └─ Tarea 3          │
└────────┬────────────┘
         │
         ↓
┌─────────────────────┐
│ GUARDAR & SINCRONIZAR   │  ← Sync con backend
│                         │     (Próximo módulo)
└─────────────────────┘
```

---

## 👨‍💻 Desarrollo

### Convenciones de Código
- **Variables/Funciones**: `camelCase`
- **Clases**: `PascalCase`
- **Constantes**: `UPPER_SNAKE_CASE`
- **Archivos Composable**: `NombrePantalla.kt`

### Commits
```bash
git commit -m "feat: nueva funcionalidad"      # Nueva feature
git commit -m "fix: corregir bug"              # Bug fix
git commit -m "docs: actualizar readme"        # Documentación
git commit -m "refactor: optimizar código"     # Refactorización
git commit -m "test: agregar tests"            # Tests
git commit -m "chore: actualizar deps"         # Mantenimiento
```

### Estructura de Archivos
- 1 archivo = 1 clase principal
- Composables relacionados pueden estar juntos
- Data classes antes que funciones

---

## 📊 Estado del Proyecto

### ✅ v1.0.0-beta (Completado)

#### Módulo de Autenticación
- [x] Estructura Clean Architecture implementada
- [x] Sistema de autenticación (UI + ViewModel)
- [x] Validaciones en cliente (email, password)
- [x] Componentes de UI reutilizables (atoms, molecules, organisms)
- [x] Indicador de carga en LoginButton
- [x] Manejo de errores con Snackbar
- [x] **Inyección de dependencias con Hilt** ✨ NUEVO
  - [x] @HiltAndroidApp en LosSabinosApplication
  - [x] @HiltViewModel en LoginViewModel
  - [x] hiltViewModel() en LoginScreen
  - [x] @AndroidEntryPoint en MainActivity
  - [x] Módulos Hilt (AppModule, ViewModelModule, RepositoryModule)

### 🚧 v1.1.0 (Próximo)

#### Conexión a Backend (Login Service)
- [ ] Crear interfaz AuthApi con Retrofit
- [ ] Implementar AuthRepository en data layer
- [ ] Crear LoginUseCase en domain layer
- [ ] Inyectar AuthRepository en LoginViewModel
- [ ] Conectar LoginViewModel con AuthApi
- [ ] Manejar respuestas del servidor (success, error, timeout)
- [ ] Guardar token JWT después de login exitoso
- [ ] Implementar interceptor OkHttp para agregar token en headers

#### Navegación
- [ ] Crear NavGraph.kt
- [ ] Conectar LoginScreen → HomeScreen
- [ ] Implementar navegación con composables

### 🔮 v1.2.0+ (Futuro)

#### Room Database
- [ ] Crear entidades de datos
- [ ] Implementar DAOs
- [ ] Configurar AppDatabase
- [ ] Crear migraciones

#### Home Screen
- [ ] Indicadores de servicios
- [ ] Botón escanear QR

#### Módulo de Escaneo
- [ ] Integrar ML Kit Barcode Scanning
- [ ] Pantalla de escaneo

#### Panel de Tareas
- [ ] Lista de tareas
- [ ] Checklist interactivo
- [ ] Captura de imágenes con CameraX

#### Sincronización
- [ ] Implementar SyncManager
- [ ] WorkManager para background sync
- [ ] Sincronización offline-first

#### Testing
- [ ] Tests unitarios
- [ ] Tests de integración
- [ ] Tests de UI

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

### Error: Hilt no inyecta dependencias

```
Asegúrate que:
- ✅ LosSabinosApplication tiene @HiltAndroidApp
- ✅ MainActivity tiene @AndroidEntryPoint
- ✅ AndroidManifest.xml tiene android:name=".LosSabinosApplication"
- ✅ LoginViewModel tiene @HiltViewModel
- ✅ LoginScreen usa hiltViewModel()
```

### Error: "Unresolved reference 'hiltViewModel'"

```bash
# Solución: Agregar dependencia en build.gradle.kts
implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
```

---

## 📧 Contacto

Genaro Velázquez - [@genaro-velazquez](https://github.com/genaro-velazquez)

---

## 📄 Licencia

MIT License - ver archivo LICENSE para detalles.

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.0.0-beta  
**Estado:** Hilt integrado, listo para conectar backend