# Kata 3: transformación y serialización de usuarios a JSON

**Tecnologías:** Java · Maven · Google Gson 2.14.0
**Autora:** Sofía Rivero Perea

---

## 1. Objetivo de la entrega

Leer un conjunto de usuarios desde una URL, interpretar el JSON de entrada, validar sus registros y transformarlos en objetos de dominio.
El resultado se serializa como un documento JSON con una colección de usuarios y un bloque de metadatos calculados.
La versión final corresponde a develp_third e incluye el total de usuarios y su edad media, redondeada a dos decimales.

---

## 2. Cómo compilar y ejecutar

1. Clonar o descargar el repositorio y abrir su carpeta raíz en **IntelliJ IDEA** como proyecto **Maven**.
2. Configurar un **JDK 21 o superior** y cargar las dependencias de `pom.xml` mediante **Load Maven Changes**.
3. Para compilar, abrir el panel **Maven** y ejecutar **Lifecycle → compile**.
4. Abrir `src/main/java/software/ulpgc/katas/Main.java` y ejecutar su método `main` mediante el **triángulo verde**.
La ejecución normal necesita conexión a Internet.
El resultado aparece en consola y se guarda en `users-output.json`, en la raíz del proyecto. Este archivo generado está incluido en `.gitignore`.

### Datos de entrada
https://gist.githubusercontent.com/THowland1/a527d341bf9d96542a7ddd7574f8fffe/raw/people.json

La entrada es una lista JSON con seis registros. De cada registro se utilizan los campos `name`, `gender` y `age`; los campos adicionales se ignoran.
Al tratarse de JSON, no hay una fila de cabecera que omitir.
Los archivos `users.json` y `users-expected.json` incluyen un ejemplo local de entrada y su salida esperada. `Main` utiliza la URL durante su ejecución normal.

---

## 3. Dependencias y versión de JDK

| Tecnología | Uso |
| --- | --- |
| **JDK 21 o superior** | Compilar y ejecutar el proyecto, configurado para Java 21. |
| **Maven** | Gestionar la compilación y las dependencias. |
| **Google Gson 2.14.0** | Interpretar el JSON de entrada y generar el JSON de salida. |
| **IntelliJ IDEA** | Entorno utilizado para desarrollar y ejecutar la práctica. |

Se ha elegido Gson porque permite trabajar con objetos y colecciones y gestionar el escape de comillas, barras invertidas y caracteres de control.
Esto facilita la generación de JSON válido sin construirlo mediante concatenaciones manuales.
La opción `setPrettyPrinting()` produce una salida con sangría para facilitar su lectura y revisión.

---

## 4. Estructura de la entrega y clases principales

El código fuente está en `src/main/java`, bajo el paquete base `software.ulpgc.katas`.
En la raíz se encuentran `pom.xml`, `.gitignore`, `README.md` y los ejemplos `users.json` y `users-expected.json`.
| Clase o interfaz | Responsabilidad |
| --- | --- |
| **`model.User`** | Record que representa un usuario mediante nombre, género y edad. |
| **`model.Gender`** | Enum que define los valores `male` y `female`. |
| **`adapters.dtos.UserDto`** | Representación intermedia de los campos del JSON de entrada. |
| **`ports.UserDeserializer`** | Interfaz que define la transformación de texto en una lista de usuarios. |
| **`adapters.StreamTextReader`** | Convierte un `InputStream` en texto UTF-8, como apoyo para otros orígenes de entrada. |
| **`adapters.JsonUserDeserializer`** | Interpreta la lista JSON, valida los datos y construye los objetos `User`. |
| **`adapters.JsonUserSerializer`** | Genera la colección de usuarios y sus metadatos en un `OutputStream`. |
| **`Main`** | Coordina la petición HTTP, la transformación y la escritura del resultado. |

### Separación de responsabilidades
La **lectura** se coordina en `Main`, que realiza la petición GET y comprueba el código HTTP 200.
La **validación** está en `validate(...)` y en la conversión del género. Se comprueba que existan los campos obligatorios, que la edad no sea negativa y que el género pertenezca al enum.
La **transformación** se realiza en `deserialize(UserDto user)`, donde se construye el objeto `User`.
La **serialización** corresponde a `JsonUserSerializer`, que recibe los objetos ya transformados y calcula los metadatos.
El deserializador recibe texto y el modelo no conoce su origen. El serializador utiliza un **`OutputStream`**, lo que permite reutilizarlo para escribir en:

- **Consola**, mediante `System.out`.
- **Fichero**, mediante `FileOutputStream`.

---

## 5. Vídeo explicativo
https://www.youtube.com/watch?v=TEV8t9-RHgU&feature=youtu.be
El vídeo muestra la entrada, la salida JSON, la transformación de registros, la separación de responsabilidades, un dataset vacío y la utilización de Gson.
Las variaciones de develop_second y develop_third se describen en el siguiente apartado.

---

## 6. Repeticiones de la kata

La kata se ha reconstruido tres veces, volviendo a crear las clases y manteniendo la misma estructura de lectura, validación, transformación y serialización.
Las variaciones pequeñas se introdujeron en los metadatos.
| Repetición | Rama de consulta | Variación |
| --- | --- | --- |
| **R1** | **`develop_one`** | Colección de usuarios y total de registros. |
| **R2** | **`develop_second`** | Total y recuento por género mediante `byGender`. |
| **R3** | **`develop_third`** | Total y edad media mediante `averageAge`, redondeada a dos decimales. |

El desarrollo se realizó en **`develop`**.
Las ramas de consulta apuntan al **commit final de cada repetición**.
La rama **`master`** contiene la **versión final develop_third** y su documentación.

---

## 7. Evidencias de verificación o pruebas realizadas

El vídeo muestra la ejecución con seis usuarios, la revisión de la salida JSON y una prueba de lista vacía con total igual a cero.

### 7.1. Salida del programa en R3

Los seis usuarios tienen edades **39, 24, 39, 34, 35 y 35**. La suma es 206 y la media redondeada es 34.33.
Para verificar la versión final, se compara el contenido de **`users-output.json`** con **`users-expected.json`**, cuyo resultado esperado es:

```json
{
  "users": [
    {
      "name": "Allen Mendez",
      "gender": "male",
      "age": 39
    },
    {
      "name": "Cook Malone",
      "gender": "male",
      "age": 24
    },
    {
      "name": "Thornton Wells",
      "gender": "male",
      "age": 39
    },
    {
      "name": "Solis Stephens",
      "gender": "male",
      "age": 34
    },
    {
      "name": "Sherrie Walters",
      "gender": "female",
      "age": 35
    },
    {
      "name": "Tricia Mcdowell",
      "gender": "female",
      "age": 35
    }
  ],
  "metadata": {
    "total": 6,
    "averageAge": 34.33
  }
}
```

### 7.2. Dataset vacío
Para simular una entrada vacía, se sustituye temporalmente la asignación del texto de entrada por:
```java
String json = "[]";
```
La ejecución debe finalizar correctamente y generar:

```json
{
  "users": [],
  "metadata": {
    "total": 0,
    "averageAge": 0.0
  }
}
```

### 7.3. Registro incompleto

Para comprobar un registro sin edad se utiliza temporalmente:
```java
String json = "[{\"name\":\"Prueba\",\"gender\":\"female\"}]";
```
**Resultado esperado:**

```text
Error: Registro 1 invalido: falta la edad.
```

### 7.4. JSON mal formado

Para comprobar una entrada con sintaxis incorrecta se utiliza:

```java
String json = "[";
```

**Resultado esperado:**

```text
Error: JSON invalido: comprueba su formato.
```

Al terminar estas comprobaciones se restaura:

```java
String json = response.body();
```

Después se ejecuta de nuevo para dejar generada la salida normal de seis usuarios.
