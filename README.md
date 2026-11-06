# Spring Boot : Application PetClinic

## Features  

### 1.  Unit Test  --> Tag v1.0.0
### 2.  Integration Test  --> Tag v2.0.0
mvn clean test -Dspring.profiles.active=h2

## Laboratorio 12 · Listado y consulta de dueños

Jose Contreras Cabrera (integrante A) implementó el bloque de lectura en
`Lab12-IntegranteA-Contreras`. Cada escenario se ejecuta en
`OwnerControllerTest` con los datos semilla de H2 y en
`OwnerControllerMockitoTest` con el servicio simulado.

| Prueba | Petición | Resultado esperado |
| --- | --- | --- |
| `testFindAllOwners` | `GET /owners` | 200, arreglo JSON y primer ID igual a 1 |
| `testFindOwnerOK` | `GET /owners/1` | 200, ID 1 y apellido Franklin |
| `testFindOwnerKO` | `GET /owners/666` | 404 |

Con Java 21, desde la terminal de IntelliJ en Windows:

```powershell
.\mvnw.cmd clean test "-Dspring.profiles.active=h2"
.\mvnw.cmd test "-Dtest=OwnerControllerTest,OwnerControllerMockitoTest" "-Dspring.profiles.active=h2"
.\mvnw.cmd spring-boot:run
```

El perfil predeterminado es H2 en memoria. El puerto predeterminado es 8080.
Durante la preparación de esta rama individual no se produjeron conflictos.
