# Analizador Léxico con JFlex + JavaFX

Analizador léxico desarrollado usando **JFlex** (equivalente a FLEX para Java) con interfaz gráfica en **JavaFX**.

## 📋 Descripción

Este proyecto implementa un analizador léxico completo que procesa código fuente de un lenguaje personalizado, identificando y clasificando tokens (palabras reservadas, identificadores, operadores, números, etc.) mediante un autómata finito generado por JFlex.

## ✨ Características

- ✅ Analizador léxico generado con **JFlex**
- ✅ Interfaz gráfica moderna con **JavaFX**
- ✅ Visualización del autómata finito
- ✅ Tabla de tokens con información detallada
- ✅ Detección y reporte de errores léxicos
- ✅ Editor de código con resaltado básico
- ✅ Exportación de resultados

## 🛠️ Requisitos

- Java 11 o superior
- JavaFX 11+ (incluido en Java 11+ o descargar por separado)
- JFlex 1.9.1 (incluido en el proyecto)

## 📦 Instalación

### Requisitos Previos

- Java 11 o superior
- JFlex 1.9.1 (descargar desde https://jflex.de/)
- JavaFX 11+ (incluido en algunas distribuciones de Java)

### Pasos de Instalación

1. **Clonar el repositorio:**
```bash
git clone <url-del-repositorio>
cd AnalizadorLexico
```

2. **Descargar JFlex:**
   - Descargar `jflex-1.9.1.jar` desde https://jflex.de/download.html
   - Colocar el archivo en el directorio `lib/`

3. **Compilar el proyecto:**

   **Opción A: Usar script automático (recomendado)**
   
   Linux/macOS:
   ```bash
   chmod +x build.sh
   ./build.sh
   ```
   
   Windows:
   ```cmd
   build.bat
   ```
   
   **Opción B: Compilación manual**
   
   Ver instrucciones detalladas en [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md)

4. **Ejecutar la aplicación:**
```bash
java -jar AnalizadorLexico.jar
```

## 🚀 Uso

1. Abrir la aplicación
2. Escribir o cargar código fuente en el editor
3. Hacer clic en "Analizar"
4. Ver los tokens identificados en la tabla
5. Revisar errores léxicos (si los hay)
6. Observar la visualización del autómata

## 📚 Documentación

- [Documentación del Lenguaje](docs/LENGUAJE.md) - Gramática y tokens del lenguaje
- [Explicación del Autómata](docs/AUTOMATA.md) - Estados y transiciones del autómata finito
- [Manual de Usuario](docs/MANUAL_USUARIO.md) - Guía completa de uso de la aplicación
- [Instrucciones de Compilación](BUILD_INSTRUCTIONS.md) - Guía detallada para compilar el proyecto

## 📁 Estructura del Proyecto

```
AnalizadorLexico/
├── src/
│   ├── main/java/
│   │   ├── lexer/          # Analizador léxico (JFlex)
│   │   └── ui/             # Interfaz gráfica JavaFX
│   └── resources/           # Recursos (CSS, etc.)
├── lib/                     # Librerías (JFlex, JavaFX)
├── docs/                    # Documentación
└── build/                   # Ejecutables compilados
```

## 📝 Ejemplos de Código

El proyecto incluye varios ejemplos de código en el directorio `ejemplos/`:

- `ejemplo1.code` - Variables y operaciones básicas
- `ejemplo2.code` - Estructuras de control (if, while)
- `ejemplo3.code` - Funciones
- `ejemplo4.code` - Comentarios y cadenas
- `ejemplo5.code` - Operadores y comparaciones

Puedes cargar estos archivos en la aplicación para probar el analizador.

## 🎯 Características Técnicas

- **Herramienta**: JFlex 1.9.1 (equivalente a FLEX para Java)
- **Lenguaje**: Java 11+
- **Interfaz Gráfica**: JavaFX
- **Arquitectura**: Análisis léxico mediante autómata finito determinista (AFD)
- **Tokens soportados**: Palabras reservadas, identificadores, números, operadores, delimitadores, comentarios, cadenas

## 🔍 Análisis Léxico

El analizador reconoce y clasifica los siguientes elementos:

- ✅ Palabras reservadas (if, else, while, for, int, float, etc.)
- ✅ Identificadores (variables, funciones)
- ✅ Números enteros y decimales
- ✅ Operadores aritméticos y de comparación
- ✅ Delimitadores (paréntesis, llaves, corchetes, punto y coma, coma)
- ✅ Comentarios de línea y bloque
- ✅ Cadenas de texto
- ✅ Valores booleanos (true, false)
- ✅ Detección de errores léxicos

## 📊 Visualización

La aplicación incluye:

- Tabla interactiva de tokens con información detallada
- Visualización del autómata finito con estados y transiciones
- Área de errores con mensajes descriptivos
- Estadísticas de tokens procesados

## 🚀 Uso Rápido

1. Abre la aplicación
2. Escribe o carga código en el editor
3. Haz clic en "Analizar"
4. Revisa los tokens identificados en la tabla
5. Observa la visualización del autómata
6. Revisa errores (si los hay) en el área de errores

## 📖 Para Más Información

- Consulta [docs/LENGUAJE.md](docs/LENGUAJE.md) para la gramática completa
- Consulta [docs/AUTOMATA.md](docs/AUTOMATA.md) para entender el autómata
- Consulta [docs/MANUAL_USUARIO.md](docs/MANUAL_USUARIO.md) para guía de uso

## 👨‍💻 Autor

Proyecto desarrollado para el curso de Compiladores.

## 📄 Licencia

Este proyecto es de uso educativo.
# analizador-lexico-compiladores
# analizador-lexico-compiladores
