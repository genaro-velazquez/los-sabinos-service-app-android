# 🔧 Sistema de Gestión de Servicios de Mantenimiento para Mecánicos

Una aplicación Android moderna desarrollada con **Jetpack Compose**, **Clean Architecture** y **MVVM** para gestionar servicios de mantenimiento en campo.

![Android](https://img.shields.io/badge/Android-34A048?style=flat-square&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=flat-square&logo=android&logoColor=white)

## 📸 Características Principales

✅ **Autenticación Segura**
- Login con email/contraseña
- Validación de sesión automática al abrir la app
- Logout con confirmación modal
- Respeto de sesiones guardadas

✅ **Gestión de Sesiones**
- SplashScreen que valida sesión automáticamente
- Si usuario logado → va a Home
- Si no logado → va a Login
- Limpia datos correctamente al cerrar sesión

✅ **Panel de Control (Home)**
- Información real del usuario (nombre, ubicación)
- Indicadores de servicios (completados, pendientes, en proceso)
- Métricas de eficiencia
- Estado de sincronización
- Botón de logout con modal de confirmación

✅ **Modal de Confirmación**
- Confirmación elegante antes de cerrar sesión
- Diseño intuitivo y responsivo
- Ancho personalizable
- Botones primario y secundario

✅ **Arquitectura Limpia**
- Separación clara de responsabilidades
- Atomic Design para componentes UI
- MVVM con StateFlow reactivos
- Inyección de dependencias con Hilt
- Clean Architecture en 3 capas

---

## 🏗️ Arquitectura

### Estructura del Proyecto

```
app/src/main/java/com/lossabinos/serviceapp/
├── data/
│   ├── local/
│   │   ├── database/
│   │   │   ├── AppDatabase.kt
│   │   │   └── dao/
│   │   │       ├── MecanicoDao.kt
│   │   │       ├── ServicioDao.kt
│   │   │       ├── TareaDao.kt
│   │   │       └── EvidenciaDao.kt
│   │   └── entity/
│   │       ├── MecanicoEntity.kt
│   │       ├── ServicioEntity.kt
│   │       ├── TareaEntity.kt
│   │       └── EvidenciaEntity.kt
│   ├── remote/
│   │   ├── api/
│   │   │   ├── AuthApi.kt
│   │   │   ├── ServicioApi.kt
│   │   │   └── EvidenciaApi.kt
│   │   └── dto/
│   │       ├── ServicioDto.kt
│   │       ├── TareaDto.kt
│   │       └── EvidenciaDto.kt
│   ├── repository/
│   │   ├── AuthRepository.kt
│   │   ├── ServicioRepository.kt
│   │   └── TareaRepository.kt
│   └── sync/
│       ├── SyncManager.kt
│       └── SyncWorker.kt
│
├── domain/
│   ├── model/
│   │   ├── Mecanico.kt
│   │   ├── Servicio.kt
│   │   ├── Tarea.kt
│   │   └── Evidencia.kt
│   ├── repository/
│   │   ├── IAuthRepository.kt
│   │   ├── IServicioRepository.kt
│   │   └── ITareaRepository.kt
│   └── usecase/
│       ├── auth/
│       │   ├── LoginUseCase.kt
│       │   └── LogoutUseCase.kt
│       ├── user/
│       │   └── GetUserPreferencesUseCase.kt
│       ├── servicio/
│       │   ├── ObtenerServiciosUseCase.kt
│       │   ├── EscanearCodigoBarrasUseCase.kt
│       │   └── ObtenerServicioDetailUseCase.kt
│       └── tarea/
│           ├── ObtenerTareasUseCase.kt
│           ├── CompletarTareaUseCase.kt
│           └── GuardarEvidenciaUseCase.kt
│
├── presentation/
│   ├── screens/
│   │   ├── splash/
│   │   │   └── SplashScreen.kt              ✨ NUEVO
│   │   ├── login/
│   │   │   ├── LoginScreen.kt
│   │   │   └── LoginState.kt
│   │   └── home/
│   │       └── HomePage.kt
│   │
│   ├── viewmodel/
│   │   ├── SplashViewModel.kt              ✨ NUEVO
│   │   ├── AuthViewModel.kt
│   │   ├── HomeViewModel.kt                ✨ NUEVO
│   │   ├── ServiciosViewModel.kt
│   │   ├── TareasViewModel.kt
│   │   └── EscaneoViewModel.kt
│   │
│   ├── ui/
│   │   ├── components/
│   │   │   ├── atoms/
│   │   │   │   ├── Avatar.kt
│   │   │   │   ├── MetricIcon.kt
│   │   │   │   ├── StatusBadge.kt
│   │   │   │   ├── ActionButton.kt
│   │   │   │   ├── PrimaryButton.kt
│   │   │   │   ├── SecondaryButton.kt      ✨ NUEVO
│   │   │   │   ├── StatusText.kt
│   │   │   │   ├── ModalTitle.kt           ✨ NUEVO
│   │   │   │   └── ModalContent.kt         ✨ NUEVO
│   │   │   ├── molecules/
│   │   │   │   ├── UserHeader.kt
│   │   │   │   ├── MetricCard.kt
│   │   │   │   ├── StatusSection.kt
│   │   │   │   ├── UnsyncSection.kt
│   │   │   │   └── ModalButtonGroup.kt     ✨ NUEVO
│   │   │   └── organisms/
│   │   │       ├── HomeHeaderSection.kt
│   │   │       ├── MetricsSection.kt
│   │   │       ├── SyncSection.kt
│   │   │       └── ConfirmationDialog.kt   ✨ NUEVO
│   │   ├── templates/
│   │   │   └── HomeTemplate.kt             📝 ACTUALIZADO
│   │   ├── theme/
│   │   │   ├── Color.kt
│   │   │   ├── Type.kt
│   │   │   └── Theme.kt
│   │   └── screens/
│   │       ├── login/
│   │       │   └── LoginScreen.kt
│   │       ├── home/
│   │       │   └── HomePage.kt
│   │       └── splash/
│   │           └── SplashScreen.kt
│   │
│   └── navigation/
│       ├── NavGraph.kt                     📝 ACTUALIZADO
│       ├── NavigationEvent.kt              📝 ACTUALIZADO
│       └── Routes.kt                       📝 ACTUALIZADO
│
├── di/
│   ├── DatabaseModule.kt
│   ├── NetworkModule.kt
│   └── RepositoryModule.kt
│
└── MainActivity.kt
```

### Capas de la Arquitectura

**Presentation Layer (UI + State)**
- Composables (Screens, Templates, Organisms, Molecules, Atoms)
- ViewModels (manejo de estado con StateFlow)
- Navigation (orquestación de rutas con NavGraph)

**Domain Layer (Lógica de Negocio)**
- Use Cases (ejecutan lógica de negocio)
- Models (entidades de dominio)
- Repository Interfaces (contratos)

**Data Layer (Acceso a Datos)**
- Repositories (implementación)
- Remote API (backend - Retrofit)
- Local Database (Room - SQLite)
- Sincronización (WorkManager)

---

## 🎨 Atomic Design

La aplicación usa **Atomic Design** para componentes UI reutilizables:

### Jerarquía

```
ATOMS (9)              → Elementos básicos reutilizables
├── Avatar
├── MetricIcon
├── StatusBadge
├── ActionButton
├── PrimaryButton
├── SecondaryButton        ✨ NUEVO
├── StatusText
├── ModalTitle             ✨ NUEVO
└── ModalContent           ✨ NUEVO
    ↓
MOLECULES (5)         → Componentes simples combinando atoms
├── UserHeader
├── MetricCard
├── StatusSection
├── UnsyncSection
└── ModalButtonGroup       ✨ NUEVO
    ↓
ORGANISMS (4)         → Componentes complejos combinando molecules
├── HomeHeaderSection
├── MetricsSection
├── SyncSection
└── ConfirmationDialog     ✨ NUEVO
    ↓
TEMPLATES (1)         → Estructura/Layout sin datos
└── HomeTemplate          📝 ACTUALIZADO (parámetros spacing)
    ↓
PAGES (1)             → Pantalla completa con datos
└── HomePage              📝 ACTUALIZADO (con HomeViewModel)
```

---

## 🔐 Flujo de Autenticación y Sesiones

### 1️⃣ Inicio de la Aplicación

```
App inicia en MainActivity
    ↓
NavGraph inicia con startDestination = Routes.SPLASH
    ↓
SplashScreen se muestra (spinner de carga)
    ↓
SplashViewModel ejecuta validateSession()
    ↓
GetUserPreferencesUseCase.getIsLogged() → API/Caché
    ↓
┌─────────────────────────────┐
│ ¿Usuario está logado?       │
├─────────────────────────────┤
│ SÍ  → NavigateToHome        │
│ NO  → NavigateToLogin       │
└─────────────────────────────┘
    ↓
Splash desaparece y muestra HomePage o LoginScreen
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

### 3️⃣ Proceso de Logout (Lo Nuevo)

```
HomePage se muestra
    ↓ (usuario presiona botón logout/flecha)
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
│    .clear()                      │
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
Navigator a HomePage (automático)
    ↓
Usuario ve HomePage SIN hacer login
```

---

## 📱 Pantallas Implementadas

### 🎬 SplashScreen (✨ NUEVO)
```kotlin
SplashScreen()
├── Muestra spinner de carga
├── Valida sesión en background
├── Redirige automáticamente a:
│   ├── HomePage (si está logado)
│   └── LoginScreen (si no está logado)
└── Tiempo de visualización: ~1 segundo
```

**Ubicación:** `presentation/screens/splash/SplashScreen.kt`

### 🔑 LoginScreen
```kotlin
LoginScreen(viewModel: LoginViewModel)
├── Campo email con validación
├── Campo password con validación
├── Botón "Iniciar Sesión"
├── Link "¿Olvidaste tu contraseña?"
├── Indicador de carga
└── Mostrador de errores
```

**Ubicación:** `presentation/screens/login/LoginScreen.kt`

### 🏠 HomePage (📝 ACTUALIZADO)
```kotlin
HomePage(
    onLogoutConfirmed: () -> Unit,
    onSettingsClick: () -> Unit,
    onSyncClick: () -> Unit,
    onSyncNowClick: () -> Unit,
    viewModel: HomeViewModel
)
├── HomeHeaderSection
│   ├── Avatar del usuario (foto)
│   ├── Nombre del usuario (datos reales) ← GetUserPreferencesUseCase
│   ├── Ubicación del usuario ← GetUserPreferencesUseCase
│   ├── Estado online/offline
│   └── Botón logout (con confirmación)
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

**Ubicación:** `presentation/screens/home/HomePage.kt`

---

## 🛠️ Tecnologías Utilizadas

### UI & Compose
- **Jetpack Compose** - Framework UI declarativo
- **Material 3** - Diseño moderno
- **Compose Navigation** - Navegación entre pantallas

### Architecture & Design Patterns
- **Clean Architecture** - 3 capas bien definidas
- **MVVM** - Model-View-ViewModel pattern
- **Atomic Design** - Componentes escalables y reutilizables

### State Management
- **Kotlin Flow** - Streams reactivos
- **StateFlow** - State management
- **ViewModel** - Lifecycle aware

### Dependency Injection
- **Hilt** - DI framework
- **Dagger** - Dependency injection

### Database
- **Room** - SQLite wrapper
- **SQLite** - Local storage

### Networking
- **Retrofit** - HTTP client
- **OkHttp** - HTTP interceptor
- **Gson** - JSON serialization

### Async Programming
- **Kotlin Coroutines** - Async/await
- **Flow** - Reactive streams
- **viewModelScope** - Lifecycle-aware coroutines

### Background Work
- **WorkManager** - Scheduled tasks
- **SyncManager** - Custom sync manager

### Scanning
- **ML Kit** - Machine Learning Kit
- **Barcode Scanning** - QR/Barcode reading

---

## 📋 Requisitos

- **Android Studio:** 2023.1 o superior
- **Android SDK:** 34 o superior
- **Kotlin:** 1.9 o superior
- **Gradle:** 8.0 o superior
- **JDK:** 11 o superior

---

## 🚀 Instalación

### 1️⃣ Clonar el repositorio

```bash
git clone https://github.com/LosabinOS/serviceapp.git
cd serviceapp
```

### 2️⃣ Sincronizar dependencias

```bash
./gradlew build
```

### 3️⃣ Configurar credenciales (si es necesario)

```gradle
// En local.properties (crear si no existe)
sdk.dir=/path/to/android-sdk
api_key=TU_API_KEY
```

### 4️⃣ Ejecutar en emulador o dispositivo

```bash
./gradlew installDebug
```

O desde Android Studio:
- Click en "Run" → "Run 'app'"

---

## 📖 Cómo Usar la Aplicación

### 🎯 Primer Inicio

1. **App inicia y muestra SplashScreen**
2. **Valida si hay sesión guardada**
3. **Si no hay → Muestra LoginScreen**
4. **Ingresa las credenciales de prueba:**
   ```
   Email:    henry@lossabinos.como.mx
   Password: Lossabinos123456789!
   ```
5. **Presiona "Iniciar Sesión"**
6. **Espera a que se valide en backend**
7. **Navega automáticamente a HomePage** ✅

### 🏠 En HomePage

1. **Ver datos del usuario**
   - Nombre real (obtenido del backend)
   - Ubicación real
   - Avatar/foto

2. **Ver indicadores de servicios**
   - Servicios completados
   - Servicios en proceso
   - Servicios pendientes
   - % de eficiencia

3. **Ver estado de sincronización**
   - Última sincronización
   - Servicios sin sincronizar
   - Botones para sincronizar

4. **Cerrar sesión**
   - Presiona el botón logout (flecha arriba a la derecha)
   - Se muestra ConfirmationDialog elegante
   - Presiona "Cerrar Sesión"
   - Se limpia la sesión completamente
   - Vuelve a LoginScreen

### 🔄 Próximos Inicios (Si mantiene sesión)

- **App inicia → SplashScreen**
- **Valida sesión guardada**
- **Si sesión es válida → HomePage (automático)**
- **No necesita hacer login de nuevo**

### 🔄 Próximos Inicios (Si hace logout)

- **App inicia → SplashScreen**
- **Valida sesión (fue limpiada)**
- **No hay sesión → LoginScreen**
- **Necesita hacer login de nuevo**

---

## 🧪 Testing

### Credenciales de Prueba

```
Email:    henry@lossabinos.como.mx
Password: Lossabinos123456789!
```

### Escenarios a Probar

#### ✅ Login válido
```
Entrada:  Email válido + Password válido
Resultado: ✅ Login exitoso → HomePage muestra datos
```

#### ✅ Login inválido
```
Entrada:  Email inválido o Password incorrecto
Resultado: ❌ Muestra error en pantalla
```

#### ✅ Validación de campos
```
Email vacío    → "Por favor ingresa un email"
Email inválido → "Email inválido"
Pass < 6 chars → "La contraseña debe tener al menos 6 caracteres"
```

#### ✅ SplashScreen
```
App abre → Muestra spinner ~1 segundo
         → Si logado → HomePage
         → Si no logado → LoginScreen
```

#### ✅ HomePage
```
Muestra nombre real del usuario (del backend)
Muestra ubicación real
Muestra indicadores
Botón logout funciona
```

#### ✅ Modal de confirmación
```
Presiona logout      → Modal aparece
Presiona Cancelar   → Sigue en Home
Presiona Confirmar  → LoginScreen
```

#### ✅ Respeto de sesiones
```
Login → Cierra app → Abre app → HomePage (automático)
Logout → Cierra app → Abre app → LoginScreen
```

---

## 📊 State Management

### SplashViewModel
```kotlin
data class SplashState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

// Ejecuta en init
init {
    validateSession() // GetUserPreferencesUseCase.getIsLogged()
}
```

**Ubicación:** `presentation/viewmodel/SplashViewModel.kt`

### LoginViewModel
```kotlin
data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isError: Boolean = false
)

// Eventos
sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    object LoginClicked : LoginEvent()
    object ForgotPasswordClicked : LoginEvent()
    object ClearError : LoginEvent()
}

// Método nuevo
fun clearState() {
    getUserPreferencesUseCase.clear() // Limpia sesión
}
```

**Ubicación:** `presentation/viewmodel/LoginViewModel.kt`

### HomeViewModel
```kotlin
data class HomeState(
    val showLogoutDialog: Boolean = false,
    val userName: String = "Cargando...", // Del backend
    val userLocation: String = "Mexico City", // Del backend
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

// Eventos
sealed class HomeEvent {
    object LogoutClicked : HomeEvent()
    object ConfirmLogout : HomeEvent()
    object CancelLogout : HomeEvent()
}

// Cargar datos
init {
    loadUserPreferences() // GetUserPreferencesUseCase.execute()
}
```

**Ubicación:** `presentation/viewmodel/HomeViewModel.kt`

---

## 🔌 APIs Utilizadas

### Autenticación
```
POST /api/auth/login
Body: { email: String, password: String }
Response: { token: String, user: User }
Status: 200 (OK) o 401 (Unauthorized)
```

### Obtener Preferencias del Usuario
```
GET /api/user/preferences
Headers: Authorization: Bearer {token}
Response: {
    id: String,
    name: String,
    email: String,
    location: String,
    avatar: String,
    lastLogin: Long
}
Status: 200 (OK) o 401 (Unauthorized)
```

### Limpiar Sesión
```
POST /api/user/logout
Headers: Authorization: Bearer {token}
Response: { success: Boolean }
Status: 200 (OK)
```

---

## 🐛 Debugging

### Logs Disponibles

En `NavGraph.kt`:
```kotlin
println("Navigate to ForgotPassword")
println("Settings clicked")
println("Sync clicked")
println("Logout confirmed - navegación manejada por NavGraph")
```

En `SplashViewModel.kt`:
```kotlin
val isLogged = getUserPreferencesUseCase.getIsLogged()
println("Is logged in: $isLogged")
```

### Debugging Avanzado

Usa Logcat en Android Studio:
```
adb logcat | grep "serviceapp"
```

---

## 📝 Cambios Recientes (v1.0.0)

### ✨ Nuevas Características

- ✅ **SplashScreen** - Validación automática de sesión
- ✅ **HomeViewModel** - Manejo de estado de Home
- ✅ **ConfirmationDialog** - Modal de confirmación elegante
- ✅ **Atomic Design Components** - 5 componentes nuevos (3 Atoms, 1 Molecule, 1 Organism)
- ✅ **GetUserPreferencesUseCase Integration** - Carga datos reales del usuario
- ✅ **Session Management** - Respeto de sesiones guardadas
- ✅ **Logout Seguro** - Limpieza completa de datos

### 📝 Modificaciones

- 📝 **LoginViewModel** - Método `clearState()` para logout
- 📝 **NavGraph** - Reestructurado con SPLASH como inicio
- 📝 **NavigationEvent** - Nuevos eventos para logout
- 📝 **HomeTemplate** - Parámetros de spacing personalizable
- 📝 **HomePage** - Observa HomeViewModel con datos reales
- 📝 **README.md** - Documentación completa (este archivo)

### 🎯 Pantallas Implementadas

1. **SplashScreen** - Validación
2. **LoginScreen** - Autenticación
3. **HomePage** - Panel de control

### 💻 Componentes Totales

- **9 Atoms** (elementos básicos)
- **5 Molecules** (combinaciones simples)
- **4 Organisms** (componentes complejos)
- **1 Template** (layout)
- **Total: 19 componentes UI**

---

## 🔮 Próximos Features (Roadmap)

### Phase 2
- [ ] **ForgotPasswordScreen** - Recuperar contraseña
- [ ] **SettingsScreen** - Configuración de usuario
- [ ] **ProfileScreen** - Editar perfil

### Phase 3
- [ ] **ScanQRScreen** - Escanear códigos de servicios
- [ ] **ServiciosScreen** - Listado de servicios
- [ ] **TareasScreen** - Panel de tareas del servicio

### Phase 4
- [ ] **EvidenciaScreen** - Captura de fotos
- [ ] **SyncScreen** - Gestión de sincronización
- [ ] **OfflineSupport** - Funcionamiento sin internet

---

## 🤝 Contribuir

1. **Fork** el proyecto
2. **Crea una rama** para tu feature (`git checkout -b feature/AmazingFeature`)
3. **Commit** tus cambios (`git commit -m 'Add AmazingFeature'`)
4. **Push** a la rama (`git push origin feature/AmazingFeature`)
5. **Abre un Pull Request**

---

## 📄 Licencia

Este proyecto está bajo licencia **MIT**. Ver `LICENSE` para más detalles.

---

## 👨‍💼 Equipo de Desarrollo

- **Desarrollador Principal:** LosabinOS
- **Arquitectura:** Clean Architecture + MVVM
- **Diseño UI:** Atomic Design System

---

## 📞 Contacto & Soporte

Para reportar bugs o sugerencias:
- **Issues:** [GitHub Issues](https://github.com/LosabinOS/serviceapp/issues)
- **Email:** soporte@lossabinos.com

---

## 📚 Documentación Adicional

- [CHANGELOG.md](CHANGELOG.md) - Historial de cambios
- [GUIA_VALIDACION_SESION.md](docs/GUIA_VALIDACION_SESION.md) - Validación automática
- [GUIA_MODAL_CONFIRMACION.md](docs/GUIA_MODAL_CONFIRMACION.md) - Modal de logout
- [GUIA_NAVEGACION.md](docs/GUIA_NAVEGACION.md) - Flujo de navegación

---

## 🙏 Agradecimientos

- Jetpack Compose Team
- Clean Architecture Community
- Atomic Design System
- Kotlin Community

---

## 📊 Estadísticas del Proyecto

```
Total de archivos:        150+
Líneas de código:         5000+
Componentes UI:           19
Pantallas:                3
ViewModels:               3
Use Cases:                10+
APIs integradas:          3
```

---

**Última actualización:** 25 de Noviembre, 2025

**Versión:** 1.0.0 - Release

**Estado:** ✅ Listo para producción
