# Instrucciones de Compilación

## Requisitos Previos

1. **Java Development Kit (JDK) 11 o superior**
   - Descargar desde: https://adoptium.net/ o https://www.oracle.com/java/
   - Verificar instalación: `java -version`

2. **JFlex 1.9.1**
   - Descargar desde: https://jflex.de/download.html
   - Colocar el archivo `jflex-1.9.1.jar` en el directorio `lib/`

3. **JavaFX 11+** (si no está incluido en tu JDK)
   - Descargar desde: https://openjfx.io/
   - O usar un JDK que incluya JavaFX (como OpenJDK con JavaFX)

## Pasos de Compilación

### Opción 1: Usar Scripts Automáticos

#### En Linux/macOS:
```bash
chmod +x build.sh
./build.sh
```

#### En Windows:
```cmd
build.bat
```

### Opción 2: Compilación Manual

#### Paso 1: Generar el Lexer con JFlex

```bash
java -jar lib/jflex-1.9.1.jar -d src/generated src/main/java/lexer/Lexer.flex
```

Esto generará el archivo `Lexer.java` en `src/generated/lexer/`.

#### Paso 2: Compilar el Código Java

```bash
# Linux/macOS
javac -cp "lib/jflex-1.9.1.jar:/ruta/a/javafx/lib/*" \
      -d build \
      src/main/java/**/*.java \
      src/generated/**/*.java

# Windows
javac -cp "lib\jflex-1.9.1.jar;%JAVA_HOME%\lib\*" \
      -d build \
      -sourcepath src\main\java \
      src\main\java\**\*.java \
      src\generated\**\*.java
```

#### Paso 3: Copiar Recursos

```bash
# Linux/macOS
cp -r src/main/resources/* build/resources/

# Windows
xcopy /E /I /Y src\main\resources build\resources
```

#### Paso 4: Crear JAR Ejecutable

```bash
# Crear manifest
echo "Manifest-Version: 1.0" > MANIFEST.MF
echo "Main-Class: Main" >> MANIFEST.MF
echo "Class-Path: lib/jflex-1.9.1.jar" >> MANIFEST.MF

# Crear JAR
cd build
jar cvfm ../AnalizadorLexico.jar ../MANIFEST.MF *
cd ..
```

## Ejecutar la Aplicación

### Opción 1: Ejecutar JAR directamente

```bash
java -jar AnalizadorLexico.jar
```

### Opción 2: Ejecutar desde clases compiladas

```bash
# Linux/macOS
java -cp "build:lib/jflex-1.9.1.jar:/ruta/a/javafx/lib/*" Main

# Windows
java -cp "build;lib\jflex-1.9.1.jar;%JAVA_HOME%\lib\*" Main
```

## Solución de Problemas

### Error: "JFlex no encontrado"
- Asegúrate de que `lib/jflex-1.9.1.jar` existe
- Descarga JFlex desde https://jflex.de/

### Error: "JavaFX no encontrado"
- Si usas Java 11+, JavaFX puede no estar incluido
- Opciones:
  1. Usar un JDK que incluya JavaFX (como Liberica JDK)
  2. Descargar JavaFX por separado y agregarlo al classpath
  3. Usar Java 17+ que puede incluir JavaFX dependiendo de la distribución

### Error: "No se puede encontrar la clase Main"
- Verifica que la compilación fue exitosa
- Asegúrate de que el MANIFEST.MF tiene la clase correcta
- Verifica que todas las clases están en el JAR

### Error: "Lexer.java no generado"
- Verifica que JFlex se ejecutó correctamente
- Revisa el archivo `Lexer.flex` por errores de sintaxis
- Verifica que el directorio `src/generated` existe

## Estructura del Proyecto Después de Compilar

```
AnalizadorLexico/
├── src/
│   ├── main/java/          # Código fuente
│   ├── generated/          # Código generado por JFlex
│   └── resources/          # Recursos (CSS, etc.)
├── build/                  # Clases compiladas
├── lib/                    # Librerías (JFlex)
├── AnalizadorLexico.jar    # Ejecutable final
└── MANIFEST.MF             # Manifest del JAR
```

## Notas Adicionales

- El código generado por JFlex se coloca en `src/generated/`
- Los archivos compilados se colocan en `build/`
- El JAR final se crea en la raíz del proyecto
- Asegúrate de tener permisos de escritura en los directorios

## Usar con IDE (IntelliJ IDEA, Eclipse, etc.)

1. Importar el proyecto como proyecto Java
2. Configurar JFlex:
   - Agregar `lib/jflex-1.9.1.jar` a las librerías
   - Configurar JFlex para generar código en `src/generated`
3. Configurar JavaFX:
   - Agregar módulos de JavaFX al classpath
   - Configurar VM options si es necesario: `--module-path /ruta/a/javafx/lib --add-modules javafx.controls,javafx.fxml`
4. Compilar y ejecutar
