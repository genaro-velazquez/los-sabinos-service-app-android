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
ATOMS (9)              → Elementos básicos
├── Avatar, MetricIcon, StatusBadge
├── ActionButton, PrimaryButton, SecondaryButton
├── StatusText, ModalTitle, ModalContent
    ↓
MOLECULES (5)         → Componentes simples
├── UserHeader, MetricCard, StatusSection
├── UnsyncSection, ModalButtonGroup
    ↓
ORGANISMS (4)         → Componentes complejos
├── HomeHeaderSection, MetricsSection
├── SyncSection, ConfirmationDialog ✨ NUEVO
    ↓
TEMPLATES (1)         → Layout sin datos
└── HomeTemplate (con parámetros spacing)
    ↓
PAGES (3)             → Pantallas completas
├── SplashScreen ✨ NUEVO
├── LoginScreen
└── HomePage ✨ ACTUALIZADO
```

---

## 🔐 Flujo de Autenticación

### 1️⃣ Inicio de la Aplicación - SplashScreen ✨ NUEVO

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

### 3️⃣ Proceso de Logout ✨ NUEVO - Modal de Confirmación

```
HomePage se muestra
    ↓ (usuario presiona botón logout)
    ↓
HomeViewModel.onEvent(HomeEvent.LogoutClicked)
    ↓
state.showLogoutDialog = true
    ↓
ConfirmationDialog ✨ NUEVO se muestra (modal elegante)
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

### 4️⃣ Respeto de Sesiones Guardadas ✨ NUEVO

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

## 📁 Estructura del Proyecto

```
app/src/main/java/com/lossabinos/serviceapp/
│
├── LosSabinosApplication.kt           ✅ @HiltAndroidApp
├── MainActivity.kt                    ✅ @AndroidEntryPoint
│
├── data/                              # 🗄️ Capa de Datos
│   ├── local/
│   │   └── UserSharedPreferencesRepositoryImpl.kt  ✅
│   ├── remote/
│   │   ├── api/
│   │   │   └── AuthenticationServices.kt          ✅
│   │   └── dto/
│   │       ├── LoginRequestDTO.kt                 ✅
│   │       └── LoginResponseDTO.kt                ✅
│   ├── repository/
│   │   ├── AuthenticationRetrofitRepository.kt    ✅
│   │   └── UserPreferencesRepository.kt           ✅ NUEVO
│   ├── utils/
│   │   ├── HeadersMaker.kt                        ✅
│   │   ├── CurlLoggingInterceptor.kt             ✅
│   │   └── RetrofitResponseValidator.kt           ✅
│   └── sync/
│       └── (próximo)
│
├── domain/                            # 💼 Capa de Dominio
│   ├── model/
│   │   ├── User.kt
│   │   └── LoginResponse.kt
│   ├── repositories/
│   │   ├── AuthenticationRepository.kt            ✅
│   │   └── UserPreferencesRepository.kt           ✅ NUEVO
│   └── usecases/
│       ├── authentication/
│       │   └── EmailPasswordLoginUseCase.kt       ✅
│       └── user/
│           └── GetUserPreferencesUseCase.kt       ✅ NUEVO
│
├── presentation/                      # 🎨 Capa de Presentación
│   ├── viewmodel/
│   │   ├── SplashViewModel.kt         ✅ NUEVO - Valida sesión
│   │   ├── LoginViewModel.kt          ✅ - Maneja autenticación
│   │   └── HomeViewModel.kt           ✅ NUEVO - Maneja home
│   │
│   └── ui/
│       ├── screens/
│       │   ├── splash/
│       │   │   └── SplashScreen.kt   ✅ NUEVO - Validación
│       │   ├── login/
│       │   │   └── LoginScreen.kt    ✅ - Autenticación
│       │   └── home/
│       │       └── HomePage.kt       ✅ NUEVO - Panel control
│       │
│       ├── components/
│       │   ├── atoms/
│       │   │   ├── PrimaryButton.kt
│       │   │   ├── SecondaryButton.kt ✅ NUEVO
│       │   │   ├── IconTextField.kt
│       │   │   ├── ModalTitle.kt     ✅ NUEVO
│       │   │   └── ModalContent.kt   ✅ NUEVO
│       │   ├── molecules/
│       │   │   ├── PasswordTextField.kt
│       │   │   ├── EmailTextField.kt
│       │   │   └── ModalButtonGroup.kt ✅ NUEVO
│       │   └── organisms/
│       │       ├── LoginForm.kt
│       │       ├── HomeHeaderSection.kt ✅ NUEVO
│       │       ├── MetricsSection.kt ✅ NUEVO
│       │       ├── SyncSection.kt    ✅ NUEVO
│       │       └── ConfirmationDialog.kt ✅ NUEVO
│       │
│       ├── theme/
│       │   ├── Color.kt
│       │   ├── Type.kt
│       │   └── Theme.kt
│       │
│       └── templates/
│           ├── LoginTemplate.kt
│           └── HomeTemplate.kt       ✅ NUEVO
│
├── navigation/                        # 🧭 Navegación NUEVO
│   ├── NavGraph.kt                   ✅ NUEVO
│   ├── NavigationEvent.kt            ✅ NUEVO
│   └── Routes.kt                     ✅ NUEVO
│
├── di/                                # 💉 Inyección de Dependencias
│   ├── AppModule.kt                  ✅
│   ├── NetworkModule.kt              ✅
│   ├── SharedPreferencesModule.kt    ✅
│   ├── RepositoryModule.kt           ✅
│   └── UseCaseModule.kt              ✅
│
└── utils/                             # 🛠️ Utilidades
    ├── Constants.kt
    ├── ExtensionFunctions.kt
    └── RetrofitResponseValidator.kt
```

---

## 🛠️ Tecnologías

### UI & Composables
- **Jetpack Compose** - UI declarativa moderna
- **Material Design 3** - Componentes estándar
- **Compose Navigation** - Navegación entre pantallas ✅ NUEVO

### Inyección de Dependencias
- **Hilt** - DI framework (✅ INTEGRADO)

### Networking
- **Retrofit** - Cliente HTTP (✅ INTEGRADO)
- **OkHttp** - Interceptores y logging (✅ INTEGRADO)
- **OkHttp Logging Interceptor** - HTTP logging (✅ INTEGRADO)
- **Gson** - Serialización JSON (✅ INTEGRADO)

### Almacenamiento Local
- **SharedPreferences** - Preferencias de usuario (✅ INTEGRADO)
- **Room** - BD local SQLite (próximo)

### Concurrencia
- **Kotlin Coroutines** - Operaciones asincrónicas (✅ INTEGRADO)
- **Flow** - Streams reactivos (✅ INTEGRADO)

### Sincronización & Background
- **WorkManager** - Tareas en background (próximo)
- **Custom SyncManager** - Sincronización offline-first (próximo)

### Cámara y Escaneo
- **CameraX** - API moderna para cámara (próximo)
- **ML Kit Barcode Scanning** - Escaneo de códigos (próximo)

### Otras Librerías
- **Coil** - Carga de imágenes eficiente (próximo)
- **Lifecycle** - Gestión del ciclo de vida (✅ INTEGRADO)

---

## 🌐 Backend Integration

### URL Base (Azure)
```
https://lossabinos-e9gvbjfrf9h5dphf.eastus2-01.azurewebsites.net
```

### Endpoints Actuales
- **POST** `/api/v1/auth/login` - Login con email y password

### Configuración API

**Content-Type:** `application/json`

**Request Format:**
```json
{
  "email": "usuario@example.com",
  "password": "password123"
}
```

**Response Format:**
```json
{
  "data": {
    "tenant": {
      "name": "Nombre App",
      "brandingConfig": {
        "primaryColor": "#FF5722",
        "secondaryColor": "#2196F3"
      }
    },
    "user": {
      "id": "user-123",
      "email": "usuario@example.com",
      "firstName": "Juan",
      "lastName": "Pérez",
      "isAdmin": false,
      "rol": {
        "code": "MECANICO",
        "id": "rol-123",
        "name": "Mecánico"
      }
    },
    "permissions": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
  }
}
```

### Headers Personalizados
```
X-LOS-SABINOS-PLATFORM-TYPE: "app"
X-LOS-SABINOS-PLATFORM-name: "Android"
Content-Type: "application/json"
```

---

## 🐛 Debugging & Logging

### Ver peticiones CURL en Logcat

La app incluye **CurlLoggingInterceptor** que imprime las peticiones en formato CURL.

**Para ver los logs:**

1. Abre Android Studio
2. Ve a `View → Tool Windows → Logcat`
3. Filtra por: `CURL_REQUEST`
4. Ejecuta login

**Verás:**
```
D/CURL_REQUEST: curl -X POST \
  -H "Content-Type: application/json" \
  -H "X-LOS-SABINOS-PLATFORM-TYPE: app" \
  -H "X-LOS-SABINOS-PLATFORM-name: Android" \
  -d '{"email":"usuario@example.com","password":"password123"}' \
  "https://lossabinos-e9gvbjfrf9h5dphf.eastus2-01.azurewebsites.net/api/v1/auth/login"

D/CURL_RESPONSE: Status: 200 OK
```

### Copiar CURL para Postman/Terminal

Puedes copiar el CURL de Logcat y probarlo directamente:

```bash
curl -X POST \
  -H "Content-Type: application/json" \
  -H "X-LOS-SABINOS-PLATFORM-TYPE: app" \
  -H "X-LOS-SABINOS-PLATFORM-name: Android" \
  -d '{"email":"usuario@example.com","password":"password123"}' \
  "https://lossabinos-e9gvbjfrf9h5dphf.eastus2-01.azurewebsites.net/api/v1/auth/login"
```

### OkHttp Logging Interceptor

Además de CURL, también tienes logs detallados de OkHttp:

```
D/OkHttp: --> POST /api/v1/auth/login http/1.1
D/OkHttp: X-LOS-SABINOS-PLATFORM-TYPE: app
D/OkHttp: X-LOS-SABINOS-PLATFORM-name: Android
D/OkHttp: Content-Type: application/json
D/OkHttp: {"email":"usuario@example.com","password":"password123"}
D/OkHttp: --> END POST (45-byte body)
D/OkHttp: <-- 200 OK /api/v1/auth/login (500ms)
D/OkHttp: {"data":{...}}
```

---

## 📱 Pantallas Implementadas

### 🎬 SplashScreen ✨ NUEVO
```
SplashScreen
├── Muestra spinner de carga
├── Valida sesión en background
├── GetUserPreferencesUseCase.getIsLogged()
└── Navega a:
    ├── HomePage (si está logado)
    └── LoginScreen (si no está logado)
```

### 🔑 LoginScreen
```
LoginScreen
├── Campo email con validación
├── Campo password con validación
├── Botón "Iniciar Sesión"
├── Link "¿Olvidaste tu contraseña?"
├── Indicador de carga
└── Mostrador de errores
```

### 🏠 HomePage ✨ NUEVO
```
HomePage
├── HomeHeaderSection
│   ├── Avatar del usuario
│   ├── Nombre real del usuario (del backend)
│   ├── Ubicación del usuario
│   ├── Estado online/offline
│   └── Botón logout con ConfirmationDialog ✨ NUEVO
│
├── SyncSection
│   ├── Estado de sincronización
│   ├── Última sincronización
│   ├── Servicios sin sincronizar
│   └── Botones de sincronización
│
└── MetricsSection (Grid 2x2)
    ├── Servicios completados
    ├── Servicios en proceso
    ├── Servicios pendientes
    └── % de eficiencia
```

---

## 🧪 Testing

### Credenciales de Prueba

```
Email:    henry@lossabinos.como.mx
Password: Lossabinos123456789!
```

### Escenarios a Probar

#### ✅ Validación de Sesión (SplashScreen) ✨ NUEVO
```
App abre → Muestra SplashScreen (~1 segundo)
         → Si tiene sesión válida → HomePage (automático)
         → Si no tiene sesión → LoginScreen
```

#### ✅ Login válido
```
Entrada:  Email válido + Password válido
Resultado: ✅ Login exitoso → HomePage con datos reales
```

#### ✅ Logout con Confirmación ✨ NUEVO
```
En HomePage → Presiona logout
            → Modal de confirmación aparece
            → Presiona "Cerrar Sesión"
            → GetUserPreferencesUseCase.clear() ejecuta
            → Navega a LoginScreen
            → Próxima vez: va a LoginScreen (sesión limpiada)
```

#### ✅ Cancelar Logout ✨ NUEVO
```
En HomePage → Presiona logout
            → Modal de confirmación aparece
            → Presiona "Cancelar"
            → Sigue en HomePage
```

#### ✅ Respeto de Sesiones ✨ NUEVO
```
1. Hacer login exitoso
2. Cerrar la app completamente
3. Abrir la app de nuevo
4. Resultado: Va directo a HomePage (sesión guardada)
```

#### ✅ Login inválido
```
Entrada:  Email inválido o Password incorrecto
Resultado: ❌ Muestra error en pantalla
```

---

## 📊 Estado del Proyecto

### ✅ v1.2.0 (Completado) - Session Management + Modal ✨ NUEVO

#### Módulo de Autenticación (v1.1.1)
- [x] Login UI con Jetpack Compose
- [x] Validaciones en cliente
- [x] ViewModel con MVVM pattern
- [x] Hilt DI completamente integrado (5 módulos)
- [x] Conexión con backend Azure
- [x] JSON body serializado
- [x] CurlLoggingInterceptor para debugging
- [x] SharedPreferences para datos de usuario

#### Módulo de Sesiones ✨ NUEVO
- [x] SplashScreen con validación automática
- [x] SplashViewModel para lógica de validación
- [x] GetUserPreferencesUseCase.getIsLogged()
- [x] Respeto de sesiones guardadas
- [x] Navegación automática basada en sesión
- [x] HomeViewModel para gestionar Home
- [x] HomePage con datos reales del usuario
- [x] ConfirmationDialog elegante para logout
- [x] GetUserPreferencesUseCase.clear() en logout
- [x] Limpieza completa de datos al salir
- [x] NavGraph con múltiples rutas (SPLASH, LOGIN, HOME)
- [x] NavigationEvent para manejo de eventos
- [x] Atomic Design Components (5 nuevos)

### 🚧 v1.3.0 (Próximo)

#### Room Database
- [ ] Crear entidades de datos
- [ ] Implementar DAOs
- [ ] Configurar AppDatabase
- [ ] Crear migraciones

#### Home Screen (Ampliación)
- [ ] Indicadores avanzados
- [ ] Botón escanear QR
- [ ] Actualización en tiempo real

### 🔮 v1.4.0+ (Futuro)

#### Módulo de Escaneo
- [ ] Integrar ML Kit Barcode Scanning
- [ ] Pantalla de escaneo con CameraX

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
┌─────────────────────────┐
│   SPLASH SCREEN        │  ← Valida sesión (✨ NUEVO)
│   • Muestra spinner     │     • Si logado → Home
│   • Valida sesión       │     • Si no logado → Login
└────────┬────────────────┘
         │
         ├─────────────────────────────┐
         │                             │
         ↓ (No logado)         ↓ (Logado)
┌─────────────────────────┐  ┌──────────────────────┐
│   LOGIN SCREEN          │  │  HOME SCREEN        │  ← Datos reales (✨ NUEVO)
│ • Email                 │  │ • Nombre usuario    │     • Modal logout
│ • Password              │  │ • Ubicación         │     • Confirmación
│ • Validaciones          │  │ • Indicadores       │     • Limpieza datos
└────────┬────────────────┘  │ • Sincronización    │
         │                    └─────────┬────────────┘
         ↓ (Exitoso)                    │
         └──────────────────────────────┘
                   │
                   ↓
         ┌─────────────────────────┐
         │ ESCANEO QR/BARCODE      │  ← Validar código (Próximo)
         └────────┬────────────────┘
                  │
                  ↓
         ┌─────────────────────────┐
         │ LISTA SERVICIOS         │  ← Servicios asignados (Próximo)
         └────────┬────────────────┘
                  │
                  ↓
         ┌─────────────────────────┐
         │ PANEL TAREAS            │  ← Checklist (Próximo)
         │ ├─ Tarea 1 ☑            │
         │ ├─ Tarea 2 (foto)       │
         │ └─ Tarea 3              │
         └────────┬────────────────┘
                  │
                  ↓
         ┌─────────────────────────┐
         │ GUARDAR & SINCRONIZAR   │  ← Sync con backend (Próximo)
         └─────────────────────────┘
```

---

## 🐛 Troubleshooting

### Error: "Unable to create @Body converter"
**Solución:** Usar `JsonObject` o `RequestBody` en lugar de `Map`

### Error: "Internal compiler error"
**Solución:** Actualizar Kotlin a 2.2.21 o superior

### Error: "Unresolved reference" en Hilt
**Solución:** 
- Sincronizar Gradle: `./gradlew clean build`
- Verificar que LosSabinosApplication tenga @HiltAndroidApp
- Verificar que AndroidManifest.xml tenga `android:name=".LosSabinosApplication"`

### Error: "Network request failed"
**Solución:**
- Verificar que la URL del backend sea correcta
- Verificar permisos de internet en AndroidManifest.xml
- En emulador: verificar que pueda acceder a la red
- Ver logs en Logcat con filtro "CURL_REQUEST"

### Error: "Gradle sync failed"
**Solución:**
```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

### Error: "Splash no valida sesión correctamente"
**Solución:**
- Verificar que GetUserPreferencesUseCase tenga método `getIsLogged()`
- Verificar que el token se guarde correctamente después del login
- Ver logs de SplashViewModel en Logcat

---

## 👨‍💻 Desarrollo

### Convenciones de Código
- **Variables/Funciones**: `camelCase`
- **Clases**: `PascalCase`
- **Constantes**: `UPPER_SNAKE_CASE`
- **Archivos Composable**: `NombrePantalla.kt`
- **ViewModels**: `NombrePantallaViewModel.kt`

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
- Comentarios en métodos complejos

---

## 📊 Métricas del Proyecto

- **Módulos Hilt**: 5 (App, Network, SharedPreferences, Repository, UseCase)
- **Interceptores**: 2 (HttpLoggingInterceptor, CurlLoggingInterceptor)
- **Screens**: 3 (Splash ✨, Login, Home ✨)
- **ViewModels**: 3 (Splash ✨, Login, Home ✨)
- **Repositories**: 3 (Authentication, UserPreferences ✨, más por venir)
- **Use Cases**: 2 (EmailPasswordLogin, GetUserPreferences ✨)
- **Componentes Atomic Design**: 19 (9 Atoms, 5 Molecules, 4 Organisms, 1 Template)
- **Líneas de código**: ~4000+ (aproximadamente)

---

## 🔄 Próximos Pasos (Orden de Prioridad)

1. ✅ ~~Setup inicial con Clean Architecture~~
2. ✅ ~~Integración Hilt~~
3. ✅ ~~Backend authentication con JSON~~
4. ✅ ~~Debugging con CurlLoggingInterceptor~~
5. ✅ ~~Navegación entre pantallas~~
6. ✅ ~~Home Screen con indicadores y datos reales~~
7. ✅ ~~SplashScreen y validación de sesión~~
8. ✅ ~~Modal de confirmación para logout~~
9. ⏳ **Room Database**
10. ⏳ **Módulo de escaneo QR**
11. ⏳ **Panel de tareas**
12. ⏳ **Sincronización offline-first**
13. ⏳ **Tests unitarios**

---

## 📧 Contacto

Genaro Velázquez - [@genaro-velazquez](https://github.com/genaro-velazquez)

---

## 📄 Licencia

MIT License - ver archivo LICENSE para detalles.

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.2.0  
**Estado:** Session management completamente integrado con SplashScreen, Modal de confirmación y datos reales del usuario