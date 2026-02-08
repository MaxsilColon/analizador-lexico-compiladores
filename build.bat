@echo off
REM Script de compilación para Windows
REM Requiere: Java 11+, JFlex

echo =========================================
echo Compilando Analizador Lexico
echo =========================================

REM Crear directorios necesarios
if not exist "src\generated" mkdir src\generated
if not exist "build" mkdir build
if not exist "lib" mkdir lib

REM Verificar que JFlex este disponible
if not exist "lib\jflex-1.9.1.jar" (
    echo ERROR: JFlex no encontrado en lib\jflex-1.9.1.jar
    echo Por favor, descarga JFlex desde: https://jflex.de/
    echo Y colocalo en el directorio lib\
    exit /b 1
)

REM Paso 1: Generar el lexer desde Lexer.flex
echo.
echo Paso 1: Generando lexer con JFlex...
java -jar lib\jflex-1.9.1.jar -d src\generated src\main\java\lexer\Lexer.flex

if errorlevel 1 (
    echo ERROR: Fallo al generar el lexer
    exit /b 1
)

REM Paso 2: Compilar el proyecto
echo.
echo Paso 2: Compilando codigo Java...

REM Compilar todos los archivos Java
javac -cp "lib\jflex-1.9.1.jar;%JAVA_HOME%\lib\*" -d build -sourcepath src\main\java src\main\java\**\*.java src\generated\**\*.java

if errorlevel 1 (
    echo ERROR: Fallo en la compilacion
    echo Nota: Si hay errores relacionados con JavaFX, asegurate de tener JavaFX instalado
    exit /b 1
)

REM Paso 3: Copiar recursos
echo.
echo Paso 3: Copiando recursos...
xcopy /E /I /Y src\main\resources build\resources

REM Paso 4: Crear JAR ejecutable
echo.
echo Paso 4: Creando JAR ejecutable...

REM Crear manifest
echo Manifest-Version: 1.0 > build\MANIFEST.MF
echo Main-Class: Main >> build\MANIFEST.MF
echo Class-Path: lib\jflex-1.9.1.jar >> build\MANIFEST.MF

REM Crear JAR
cd build
jar cvfm ..\AnalizadorLexico.jar MANIFEST.MF *.*
cd ..

echo.
echo =========================================
echo Compilacion completada!
echo =========================================
echo.
echo Ejecutable creado: AnalizadorLexico.jar
echo.
echo Para ejecutar:
echo   java -jar AnalizadorLexico.jar
echo.
pause
