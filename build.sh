#!/bin/bash

# Script de compilación para el Analizador Léxico
# Requiere: Java 11+, JFlex

echo "========================================="
echo "Compilando Analizador Léxico"
echo "========================================="

# Crear directorios necesarios
mkdir -p src/generated
mkdir -p build
mkdir -p lib

# Verificar que JFlex esté disponible
if [ ! -f "lib/jflex-1.9.1.jar" ]; then
    echo "ERROR: JFlex no encontrado en lib/jflex-1.9.1.jar"
    echo "Por favor, descarga JFlex desde: https://jflex.de/"
    echo "Y colócalo en el directorio lib/"
    exit 1
fi

# Paso 1: Generar el lexer desde Lexer.flex
echo ""
echo "Paso 1: Generando lexer con JFlex..."
java -jar lib/jflex-1.9.1.jar -d src/generated src/main/java/lexer/Lexer.flex

if [ $? -ne 0 ]; then
    echo "ERROR: Fallo al generar el lexer"
    exit 1
fi

# Paso 2: Compilar el proyecto
echo ""
echo "Paso 2: Compilando código Java..."

# Detectar JavaFX (soporta SDK path o ubicaciones comunes de Homebrew)
JAVAFX_LIB=""
if [ -d "lib/javafx-sdk-17.0.18/lib" ]; then
    JAVAFX_LIB="lib/javafx-sdk-17.0.18/lib"
elif [ -n "$JAVAFX_SDK" ] && [ -d "$JAVAFX_SDK/lib" ]; then
    JAVAFX_LIB="$JAVAFX_SDK/lib"
elif [ -d "/usr/lib/jvm/java-11-openjdk/lib" ]; then
    JAVAFX_LIB="/usr/lib/jvm/java-11-openjdk/lib"
elif [ -d "$JAVA_HOME/lib" ]; then
    JAVAFX_LIB="$JAVA_HOME/lib"
elif [ -d "/usr/local/opt/openjfx/lib" ]; then
    JAVAFX_LIB="/usr/local/opt/openjfx/lib"
elif [ -d "/opt/homebrew/opt/openjfx/lib" ]; then
    JAVAFX_LIB="/opt/homebrew/opt/openjfx/lib"
fi

# Compilar - usar find para encontrar archivos Java
JAVA_FILES=$(find src/main/java -name "*.java" 2>/dev/null)
GENERATED_FILES=$(find src/generated -name "*.java" 2>/dev/null)

if [ -z "$JAVA_FILES" ]; then
    echo "ERROR: No se encontraron archivos Java en src/main/java"
    exit 1
fi

# Construir classpath y opciones para JavaFX si está disponible
CLASSPATH="lib/jflex-1.9.1.jar"
JAVAC_EXTRA=""
JAVA_RUN_EXTRA=""
if [ -n "$JAVAFX_LIB" ]; then
    # Si existe la carpeta lib del SDK, preferimos usar module-path
    if ls "$JAVAFX_LIB"/javafx*.jar >/dev/null 2>&1; then
        CLASSPATH="$CLASSPATH:$JAVAFX_LIB/*"
        JAVAC_EXTRA="--module-path $JAVAFX_LIB --add-modules javafx.controls,javafx.fxml,javafx.graphics"
        JAVA_RUN_EXTRA="--module-path $JAVAFX_LIB --add-modules javafx.controls,javafx.fxml,javafx.graphics"
    fi
fi

# Compilar (soporta pasar opciones adicionales a javac)
if [ -n "$JAVAC_EXTRA" ]; then
    javac $JAVAC_EXTRA -cp "$CLASSPATH" -d build $JAVA_FILES $GENERATED_FILES 2>&1
else
    javac -cp "$CLASSPATH" -d build $JAVA_FILES $GENERATED_FILES 2>&1
fi

if [ $? -ne 0 ]; then
    echo "ERROR: Fallo en la compilación"
    echo "Nota: Si hay errores relacionados con JavaFX, asegúrate de tener JavaFX instalado"
    exit 1
fi

# Paso 3: Copiar recursos
echo ""
echo "Paso 3: Copiando recursos..."
# Copiar recursos a la raíz de build para que estén en el classpath
cp -r src/main/resources/* build/ 2>/dev/null || true

# Paso 4: Crear JAR ejecutable
echo ""
echo "Paso 4: Creando JAR ejecutable..."

# Crear manifest
echo "Manifest-Version: 1.0" > build/MANIFEST.MF
echo "Main-Class: Main" >> build/MANIFEST.MF
echo "Class-Path: lib/jflex-1.9.1.jar" >> build/MANIFEST.MF

# Crear JAR con estructura correcta (incluyendo css y otros recursos)
cd build
# Buscamos clases y recursos (css, fxml, png, etc)
find . \( -name "*.class" -o -name "*.css" -o -name "*.fxml" -o -name "*.png" -o -name "*.jpg" \) | xargs jar cvfm ../AnalizadorLexico.jar ../MANIFEST.MF
cd ..

echo ""
echo "========================================="
echo "Compilación completada!"
echo "========================================="
echo ""
echo "Ejecutable creado: AnalizadorLexico.jar"
echo ""
echo "Para ejecutar:"
echo "  java -jar AnalizadorLexico.jar"
echo ""
