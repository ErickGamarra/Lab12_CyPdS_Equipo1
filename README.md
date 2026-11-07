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

## Laboratorio 12 · Actualización y borrado de dueños

Erick Gamarra (integrante C) implementó el bloque de actualización y borrado en
`Lab12-IntegranteC-Gamarra`, partiendo de la base de OwnerController integrada
de los bloques A y B. Cada escenario se ejecuta en `OwnerControllerTest` contra
H2 y en `OwnerControllerMockitoTest` con el servicio simulado.

| Prueba | Petición | Resultado esperado |
| --- | --- | --- |
| `testUpdateOwner` | `POST /owners` + `PUT /owners/{id}` | 200 y `GET` confirma el cambio |
| `testDeleteOwner` | `POST /owners` + `DELETE /owners/{id}` | 200 y el dueño ya no existe |
| `testDeleteOwnerKO` | `DELETE /owners/1000` | 404 |

Con Java 21, desde la terminal de IntelliJ en Windows:

```powershell
.\mvnw.cmd test "-Dtest=OwnerControllerTest,OwnerControllerMockitoTest" "-Dspring.profiles.active=h2"
```

## Laboratorio 12 · Resumen del equipo

Grupo 1, `OwnerController` (`/owners`). Cada escenario se probó dos veces:
integración real contra H2 en `OwnerControllerTest` y servicio simulado con
`@MockitoBean` en `OwnerControllerMockitoTest`.

### Integrantes y pruebas

| Integrante | Rama | Pruebas |
| --- | --- | --- |
| A · José Contreras | `Lab12-IntegranteA-Contreras` (PR #2) | `testFindAllOwners`, `testFindOwnerOK`, `testFindOwnerKO` |
| B · Jesús Rocha | `Lab12-IntegranteB-Rocha` (PR #3) | `testCreateOwner`, `testCreateOwner_BadRequest`, `testFindByLastName` |
| C · Erick Gamarra | `Lab12-IntegranteC-Gamarra` (PR #1) | `testUpdateOwner`, `testDeleteOwner`, `testDeleteOwnerKO` |

Cada archivo de prueba tiene 9 pruebas, ejecutadas con Java 21 y el perfil `h2`. Con las tres ramas integradas en `main`,
`./mvnw clean test -Dspring.profiles.active=h2` ejecuta 45 pruebas sin fallos.

### Conflictos y reversiones

- La rama C se actualizó con `main` mediante `git merge origin/main` después de
  fusionar los PR #2 y #3. Git no reportó conflictos de contenido en ese merge.
- El historial conserva el commit `Revert "Lab12 - Integración de cambios del
  integrante A - Contreras"` (`6433db0`) y la rama `revert-2-Lab12-IntegranteA-Contreras`,
  generados al revertir el PR #2 durante la integración. El contenido del bloque A
  permanece en `main` y sus pruebas pasan.
