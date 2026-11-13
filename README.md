# OsoSense Backend

RESTful API de **OsoSense**, el sistema IoT de Oso Terra que monitorea la salinidad del suelo. Recibe las lecturas que el Edge Service envía desde la parcela, las compara con el umbral del cultivo, genera alertas y expone los datos que consumen la Web App y la Mobile App.

- **Swagger UI (producción):** https://osoterra-backend-production.up.railway.app/swagger-ui.html
- **Curso:** Desarrollo de Soluciones IoT · 1ASI0572 · UPC

## Bounded contexts

El código está organizado por bounded context, cada uno en su paquete bajo `com.osoterra.ososense`:

| Paquete | Bounded context | Tipo | Responsabilidad |
|---|---|---|---|
| `soilmonitoring` | Soil Monitoring | Core | Recibe y guarda las lecturas crudas y compensadas del sensor. |
| `salinityalerting` | Salinity Alerting | Core | Evalúa cada lectura contra el umbral del cultivo y genera la alerta. |
| `farmmanagement` | Farm Management | Supporting | Fincas, parcelas y vinculación de sensores. |
| `analyticsreporting` | Analytics and Reporting | Supporting | Tendencias, historial y reportes. |
| `iam` | Identity and Access Management | Generic | Cuentas, autenticación con JWT o Google y vínculos productor–asesor. |
| `subscriptionbilling` | Subscription and Billing | Generic | Planes, límites de parcelas y pagos con Stripe. |
| `shared` | — | — | Clases base del dominio y soporte REST común. |

Dentro de cada contexto se repiten las capas `domain`, `application`, `infrastructure` e `interfaces`.

## Tecnologías

- Java 17 y Spring Boot 4 (Web MVC, Data JPA, Security, Validation, Mail)
- PostgreSQL con migraciones de Flyway (`src/main/resources/db/migration`)
- JWT (jjwt) y Google Sign-In para la autenticación
- Stripe para las suscripciones
- springdoc-openapi para la documentación en Swagger UI
- JUnit 5, AssertJ, embedded-postgres y Cucumber 8 para las pruebas

## Ejecutar en local

Requisitos: JDK 17 o superior y una base PostgreSQL.

1. Crea la base y el usuario que usa la configuración por defecto:

   ```sql
   CREATE USER ososense WITH PASSWORD 'ososense';
   CREATE DATABASE ososense OWNER ososense;
   ```

2. Copia `.env.example` y ajusta los valores si tu base es distinta.
3. Levanta el API:

   ```bash
   ./mvnw spring-boot:run
   ```

Flyway aplica las migraciones al arrancar. El API queda en `http://localhost:8080` y Swagger UI en `http://localhost:8080/swagger-ui.html`.

## Variables de entorno

Todas tienen un valor por defecto para desarrollo local, salvo las de servicios externos.

| Variable | Uso | Valor por defecto |
|---|---|---|
| `PORT` | Puerto HTTP (Railway lo asigna) | `8080` |
| `DB_URL`, `DB_USERNAME`, `DB_PASSWORD` | Conexión a PostgreSQL. Si no están, se usan las variables `PG*` que inyecta Railway | base `ososense` en `localhost:5432` |
| `JWT_SECRET` | Clave para firmar los JWT. **Obligatoria fuera de local** | clave de desarrollo |
| `JWT_EXPIRATION_MINUTES` | Vigencia del token | `60` |
| `EDGE_API_KEY` | Clave que el Edge Service envía en la cabecera `X-Edge-Api-Key` | `local-edge-key` |
| `CORS_ALLOWED_ORIGINS` | Orígenes que pueden llamar al API desde el navegador | localhost, GitHub Pages y Vercel |
| `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`, `MAIL_FROM` | Servidor SMTP para los correos de alertas y de recuperación de contraseña | `localhost:1025` |
| `GOOGLE_CLIENT_ID` | Inicio de sesión con Google | vacío |
| `STRIPE_WEBHOOK_SECRET` | Firma de los webhooks de Stripe | vacío |
