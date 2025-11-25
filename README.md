# 🔧 Los Sabinos - Sistema de Gestión de Servicios de Mantenimiento

Aplicación Android nativa para gestionar servicios de mantenimiento con funcionalidad offline-first, captura de evidencia y sincronización automática de datos.

---

## 📋 Tabla de Contenidos

- [Características](#características)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Arquitectura](#arquitectura)
- [Tecnologías](#tecnologías)
- [Estructura del Proyecto](#estructura-del-proyecto)
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
- ✅ **Logging de peticiones CURL** para debugging
- ✅ **Indicadores y métricas** en pantalla Home
- ✅ **Escaneo de códigos de barras/QR** para asignar servicios
- ✅ **Panel de tareas** con checklist interactivo
- ✅ **Captura de evidencia** (imágenes con cámara)
- ✅ **Offline-First** con sincronización automática
- ✅ **Inyección de dependencias con Hilt**
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
│   │       ├── LoginRequestDTO.kt                 ✅ (JSON)
│   │       └── LoginResponseDTO.kt                ✅
│   ├── repository/
│   │   └── AuthenticationRetrofitRepository.kt    ✅
│   ├── utils/
│   │   ├── HeadersMaker.kt                        ✅
│   │   ├── CurlLoggingInterceptor.kt             ✅ (Debugging)
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
│   │   └── UserPreferencesRepository.kt           ✅
│   └── usecases/
│       ├── authentication/
│       │   └── EmailPasswordLoginUseCase.kt       ✅
│       └── preferences/
│           └── GetUserPreferencesUseCase.kt       ✅
│
├── presentation/                      # 🎨 Capa de Presentación
│   ├── viewmodel/
│   │   └── LoginViewModel.kt          ✅ @HiltViewModel + UseCase
│   │
│   └── ui/
│       ├── screens/
│       │   ├── login/
│       │   │   └── LoginScreen.kt     ✅ hiltViewModel()
│       │   └── (home próximo)
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
│       └── templates/
│           └── LoginTemplate.kt
│
├── di/                                # 💉 Inyección de Dependencias
│   ├── AppModule.kt                  ✅
│   ├── NetworkModule.kt              ✅ Retrofit + OkHttp
│   ├── SharedPreferencesModule.kt    ✅ SharedPreferences
│   ├── RepositoryModule.kt           ✅ Repositories
│   └── UseCaseModule.kt              ✅ Use Cases
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
- **Compose Navigation** - Navegación entre pantallas (próximo)

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
- **Coroutines** - Operaciones asincrónicas (✅ INTEGRADO)
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
│   LOGIN SCREEN          │  ← Validaciones cliente + servidor
│                         │     • Email validación formato
│                         │     • Password >= 6 caracteres
│                         │     • Llamada a API backend (JSON)
│                         │     • Guardar preferencias usuario
└────────┬────────────────┘
         │
         ↓ (Si login exitoso)
┌─────────────────────────┐
│   HOME SCREEN           │  ← Indicadores + Botón escanear
│                         │     (Próximo módulo)
└────────┬────────────────┘
         │
         ↓
┌─────────────────────────┐
│ ESCANEO QR/BARCODE      │  ← Validar código
│                         │     (Próximo módulo)
└────────┬────────────────┘
         │
         ↓
┌─────────────────────────┐
│ LISTA SERVICIOS         │  ← Servicios asignados
│                         │     (Próximo módulo)
└────────┬────────────────┘
         │
         ↓
┌─────────────────────────┐
│ PANEL TAREAS            │  ← Checklist + evidencia
│ ├─ Tarea 1 ☑            │     (Próximo módulo)
│ ├─ Tarea 2 (foto)       │
│ └─ Tarea 3              │
└────────┬────────────────┘
         │
         ↓
┌─────────────────────────┐
│ GUARDAR & SINCRONIZAR   │  ← Sync con backend
│                         │     (Próximo módulo)
└─────────────────────────┘
```

---

## 📊 Estado del Proyecto

### ✅ v1.1.1 (Completado) - Backend Integration v2

#### Módulo de Autenticación
- [x] Login UI con Jetpack Compose
- [x] Validaciones en cliente (email, password)
- [x] ViewModel con MVVM pattern
- [x] Manejo de errores con Snackbar
- [x] **Hilt DI completamente integrado**
  - [x] @HiltAndroidApp en LosSabinosApplication
  - [x] 5 módulos Hilt (App, Network, SharedPreferences, Repository, UseCase)
  - [x] @HiltViewModel en LoginViewModel
  - [x] hiltViewModel() en LoginScreen
  - [x] @AndroidEntryPoint en MainActivity
- [x] **Conexión con backend Azure**
  - [x] Retrofit + OkHttp configurado
  - [x] AuthenticationServices para llamadas API
  - [x] HeadersMaker para headers personalizados
  - [x] AuthenticationRepository implementado
  - [x] EmailPasswordLoginUseCase conectado
  - [x] **JSON body serializado** (Content-Type: application/json)
  - [x] LoginRequestDTO para body
  - [x] Manejo de respuestas del servidor
  - [x] Guardado de preferencias después de login
  - [x] **CurlLoggingInterceptor** para debugging
  - [x] OkHttp logging detallado
- [x] SharedPreferences para datos de usuario
- [x] Validación de respuestas con RetrofitResponseValidator
- [x] Manejo de excepciones del servidor
- [x] Kotlin 2.2.21 para estabilidad

### 🚧 v1.2.0 (Próximo)

#### Navegación
- [ ] Crear NavGraph.kt
- [ ] Conectar LoginScreen → HomeScreen
- [ ] Implementar navegación con composables

#### Home Screen
- [ ] Indicadores de servicios
- [ ] Botón escanear QR
- [ ] Mostrar datos del usuario desde preferencias

#### Room Database
- [ ] Crear entidades de datos
- [ ] Implementar DAOs
- [ ] Configurar AppDatabase
- [ ] Crear migraciones

### 🔮 v1.3.0+ (Futuro)

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

## 🐛 Troubleshooting

### Error: "Unable to create @Body converter"
**Solución:** Usar `JsonObject` o `RequestBody` en lugar de `Map`

### Error: "Internal compiler error"
**Solución:** Actualizar Kotlin a 2.2.21 o superior

### Error: "Unresolved reference" en Hilt
**Solución:** 
- Sincronizar Gradle: `./gradlew clean build`
- Verificar que LosSabinosApplication tenga @HiltAndroidApp
- Verificar que AndroidManifest.xml tenga android:name=".LosSabinosApplication"

### Error: "Network request failed"
**Solución:**
- Verificar que la URL del backend sea correcta en NetworkModule
- Verificar permisos de internet en AndroidManifest.xml
- En emulador: verificar que pueda acceder a la red
- Ver logs en Logcat con filtro "CURL_REQUEST"

### Error: Gradle sync failed
**Solución:**
```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

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
- **Screens**: 1 (Login) - 4 más por implementar
- **ViewModels**: 1 (Login) - más por agregar
- **Repositories**: 2 (Authentication, UserPreferences)
- **Use Cases**: 2 (EmailPasswordLogin, GetUserPreferences)
- **Líneas de código**: ~2500 (aproximadamente)

---

## 🔄 Próximos Pasos (Orden de Prioridad)

1. ✅ ~~Setup inicial con Clean Architecture~~
2. ✅ ~~Integración Hilt~~
3. ✅ ~~Backend authentication con JSON~~
4. ✅ ~~Debugging con CurlLoggingInterceptor~~
5. ⏳ **Navegación entre pantallas**
6. ⏳ **Home Screen con indicadores**
7. ⏳ **Módulo de escaneo QR**
8. ⏳ **Room Database**
9. ⏳ **Panel de tareas**
10. ⏳ **Sincronización offline-first**
11. ⏳ **Tests unitarios**

---

## 📧 Contacto

Genaro Velázquez - [@genaro-velazquez](https://github.com/genaro-velazquez)

---

## 📄 Licencia

MIT License - ver archivo LICENSE para detalles.

---

**Última actualización:** Noviembre 2025  
**Versión:** 1.1.1  
**Estado:** Backend authentication completamente integrado con JSON y debugging