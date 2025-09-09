# Regata Online - Sistema de Juego Multijugador

## Descripción

Sistema web de regatas online desarrollado en Spring Boot que permite a múltiples jugadores competir en carreras de barcos en tiempo real. Los jugadores controlan barcos que se mueven por vectores de velocidad en un mapa bidimensional con obstáculos.

## 🚀 Versión Actual: 1.2.0

### 📋 Estado Actual del Proyecto
- ✅ **Backend completo** con Spring Boot
- ✅ **Sistema de autenticación** implementado
- ✅ **Gestión de partidas y mapas** funcional
- ✅ **Interfaz de usuario** con Thymeleaf y Bootstrap
- ✅ **Base de datos H2** configurada
- 🔄 **Sistema de turnos** en desarrollo
- 🔄 **WebSockets** para tiempo real pendiente

### ✅ Funcionalidades Implementadas

#### 🔐 Sistema de Autenticación
- **Login de usuarios** con roles diferenciados (Jugador/Administrador)
- **Interceptor de autenticación** que controla el acceso a rutas protegidas
- **Gestión de sesiones** con validación de roles
- **Restricciones de acceso** basadas en permisos de usuario

#### 🎮 Mecánicas de Juego
- **Movimiento vectorial de barcos** con sistema de velocidad (vx, vy)
- **Cambios de velocidad** permitidos: +1, 0, -1 en cada componente
- **Validación de límites del mapa** para evitar salidas del área de juego
- **Sistema de colisiones** con detección de obstáculos
- **Estados de barco**: Activo, Destruido
- **Estados de partida**: Creada, En Curso, Finalizada

#### 🗺️ Sistema de Mapas
- **Mapas configurables** con diferentes tipos de celdas:
  - **AGUA**: Navegable
  - **PARED**: Obstáculo que destruye barcos
  - **PARTIDA**: Posición inicial
  - **META**: Objetivo final
- **Validación de posiciones** según tipo de celda
- **Carga de mapas** desde base de datos

#### 🎯 Gestión de Partidas
- **Creación de partidas** por administradores
- **Unión de jugadores** a partidas existentes
- **Visualización de estado** de partidas activas
- **Sistema de participaciones** que vincula jugadores con partidas

#### 🖥️ Interfaz de Usuario
- **Dashboard principal** con listado de partidas
- **Vista de juego en tiempo real** con tablero visual
- **Formularios de creación** de partidas y mapas
- **Navegación intuitiva** con Bootstrap
- **Indicadores visuales** de estado (badges, colores)

#### 🗄️ Persistencia de Datos
- **Base de datos H2** para desarrollo
- **Entidades JPA** bien estructuradas:
  - Jugador (con roles)
  - Partida (con estados)
  - Mapa (con dimensiones)
  - Celda (con tipos y coordenadas)
  - Barco (con posición y velocidad)
  - Participacion (relación jugador-partida)
- **Repositorios Spring Data** con consultas personalizadas
- **Inicialización automática** con datos de prueba

### 🔧 Arquitectura Técnica

#### Backend
- **Spring Boot 3.2.0** como framework principal
- **Spring MVC** para controladores web
- **Spring Data JPA** para persistencia
- **Thymeleaf** como motor de plantillas
- **H2 Database** para almacenamiento
- **Lombok** para reducir código boilerplate

#### Frontend
- **Thymeleaf** con sintaxis moderna
- **Bootstrap 5** para estilos responsivos
- **JavaScript vanilla** para interactividad
- **CSS personalizado** para elementos específicos del juego

#### Estructura del Proyecto
```
src/main/java/com/regata/
├── config/          # Configuración de seguridad e inicialización
│   ├── SecurityConfig.java
│   ├── GlobalExceptionHandler.java
│   └── DataInitializer.java
├── domain/
│   ├── entity/      # Entidades JPA
│   │   ├── Jugador.java
│   │   ├── Partida.java
│   │   ├── Mapa.java
│   │   ├── Celda.java
│   │   ├── Barco.java
│   │   └── Participacion.java
│   ├── repository/  # Repositorios Spring Data
│   └── service/     # Lógica de negocio
└── web/
    ├── controller/  # Controladores MVC
    └── interceptor/ # Interceptores de autenticación

src/main/resources/
├── templates/       # Plantillas Thymeleaf
├── static/         # Recursos estáticos (CSS, JS, imágenes)
├── application.properties
└── ValidationMessages.properties
```

### 🚧 Funcionalidades Pendientes

#### 🔄 Sistema de Turnos
- **Turnos secuenciales** entre jugadores
- **Tiempo límite** por turno
- **Notificaciones** de turno actual
- **Sincronización** en tiempo real

#### 🌐 Comunicación en Tiempo Real
- **WebSockets** para actualizaciones instantáneas
- **Notificaciones push** de eventos del juego
- **Chat en vivo** entre jugadores
- **Sincronización automática** del estado del juego

#### 🏆 Sistema de Puntuación
- **Cálculo de puntajes** basado en tiempo y eficiencia
- **Ranking de jugadores** por partida
- **Historial de partidas** y estadísticas
- **Logros y medallas** por rendimiento

#### 🎨 Mejoras de UI/UX
- **Animaciones** de movimiento de barcos
- **Efectos visuales** para colisiones y eventos
- **Sonidos** y feedback auditivo
- **Modo oscuro** y temas personalizables
- **Responsive design** mejorado para móviles

#### 🔒 Seguridad Avanzada
- **Autenticación JWT** para APIs
- **Validación de movimientos** en servidor
- **Prevención de trampas** y validaciones adicionales
- **Rate limiting** para prevenir spam

#### 📊 Administración
- **Panel de administración** completo
- **Gestión de usuarios** (crear, editar, eliminar)
- **Editor visual de mapas** con drag & drop
- **Monitoreo de partidas** en tiempo real
- **Logs y auditoría** de acciones

#### 🗄️ Base de Datos
- **Migración a PostgreSQL** para producción
- **Optimización de consultas** y índices
- **Backup automático** y recuperación
- **Escalabilidad horizontal** con clustering

## 🛠️ Instalación y Ejecución

### Prerrequisitos
- Java 17 o superior
- Maven 3.6+

### Pasos
1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/SantiAvilal13/Proyecto-Desarollo-Web-D.git
   cd "Proyecto desarrollo web"
   ```

2. **Compilar el proyecto**
   ```bash
   mvn compile
   ```

3. **Ejecutar la aplicación**
   ```bash
   mvn spring-boot:run
   ```

4. **Acceder a la aplicación**
   - URL: http://localhost:8080
   - Usuarios de prueba:
     - **Administrador**: admin / admin123
     - **Jugador**: player1 / player123

### 🔧 Tecnologías Utilizadas
- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Security** para autenticación
- **Spring Data JPA** para persistencia
- **H2 Database** (base de datos en memoria)
- **Thymeleaf** para templates
- **Bootstrap 5** para estilos
- **Maven** para gestión de dependencias

## 🎯 Casos de Uso Principales

1. **Crear Partida** (Administrador)
   - Seleccionar mapa
   - Configurar parámetros
   - Iniciar partida

2. **Unirse a Partida** (Jugador)
   - Ver partidas disponibles
   - Seleccionar partida
   - Esperar inicio

3. **Jugar Partida** (Jugador)
   - Mover barco por turnos
   - Evitar obstáculos
   - Llegar a la meta

4. **Administrar Sistema** (Administrador)
   - Gestionar usuarios
   - Crear mapas
   - Monitorear partidas



## 📄 Licencia

Proyecto académico - Pontificia Universidad Javeriana

---

**Estado del Proyecto**: ✅ Funcional - En desarrollo activo
**Última Actualización**: Septiembre 2025
**Repositorio**: https://github.com/SantiAvilal13/Proyecto-Desarollo-Web-D.git

## 👥 Desarrolladores
- Santiago Ávila 
-Vaneza Mayorga
-Fernanda Rios
