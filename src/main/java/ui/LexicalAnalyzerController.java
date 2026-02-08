package ui;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lexer.Lexer;
import lexer.Token;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador principal de la interfaz gráfica del analizador léxico
 */
public class LexicalAnalyzerController {
    
    private TextArea codeEditor;
    private TableView<TokenTableRow> tokensTable;
    private TextArea errorsArea;
    private AutomataVisualization automataViz;
    private ObservableList<TokenTableRow> tokensData;
    private Label statusLabel;
    
    /**
     * Clase auxiliar para mostrar tokens en la tabla
     */
    public static class TokenTableRow {
        private String tipo;
        private String lexema;
        private String linea;
        private String columna;
        
        public TokenTableRow(String tipo, String lexema, String linea, String columna) {
            this.tipo = tipo;
            this.lexema = lexema;
            this.linea = linea;
            this.columna = columna;
        }
        
        public String getTipo() { return tipo; }
        public String getLexema() { return lexema; }
        public String getLinea() { return linea; }
        public String getColumna() { return columna; }
    }
    
    /**
     * Crea y configura la interfaz gráfica completa
     */
    public Parent createUI() {
        // Contenedor principal
        BorderPane root = new BorderPane();
        
        // Menú superior
        MenuBar menuBar = createMenuBar();
        root.setTop(menuBar);
        
        // Panel central dividido
        SplitPane centerPane = new SplitPane();
        centerPane.setDividerPositions(0.5);
        
        // Panel izquierdo: Editor de código
        VBox leftPanel = createCodeEditorPanel();
        
        // Panel derecho: Resultados
        VBox rightPanel = createResultsPanel();
        
        centerPane.getItems().addAll(leftPanel, rightPanel);
        root.setCenter(centerPane);
        
        // Barra de estado inferior
        statusLabel = new Label("Listo");
        statusLabel.setPadding(new Insets(5));
        root.setBottom(statusLabel);
        
        return root;
    }
    
    /**
     * Crea el panel del editor de código
     */
    private VBox createCodeEditorPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-background-color: #f5f5f5;");
        
        Label title = new Label("Código Fuente");
        title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        codeEditor = new TextArea();
        codeEditor.setWrapText(false);
        codeEditor.setFont(javafx.scene.text.Font.font("Consolas", 12));
        codeEditor.setStyle("-fx-font-family: 'Consolas', monospace;");
        VBox.setVgrow(codeEditor, Priority.ALWAYS);
        
        // Botones de control
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        
        Button analyzeBtn = new Button("Analizar");
        analyzeBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold;");
        analyzeBtn.setOnAction(e -> analyzeCode());
        
        Button clearBtn = new Button("Limpiar");
        clearBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
        clearBtn.setOnAction(e -> clearAll());
        
        // Button loadBtn = new Button("Cargar Archivo");
        // loadBtn.setOnAction(e -> loadFile());
        
        // Button saveBtn = new Button("Guardar Tokens");
        // saveBtn.setOnAction(e -> saveTokens());
        
        // buttonBox.getChildren().addAll(analyzeBtn, clearBtn, loadBtn, saveBtn);

        buttonBox.getChildren().addAll(analyzeBtn, clearBtn);

        
        panel.getChildren().addAll(title, codeEditor, buttonBox);
        
        return panel;
    }
    
    /**
     * Crea el panel de resultados
     */
    private VBox createResultsPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));
        panel.setStyle("-fx-background-color: #ffffff;");
        
        // Tabla de tokens
        Label tokensTitle = new Label("Tokens Identificados");
        tokensTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        tokensTable = new TableView<>();
        tokensData = FXCollections.observableArrayList();
        tokensTable.setItems(tokensData);
        
        TableColumn<TokenTableRow, String> tipoCol = new TableColumn<>("Tipo");
        tipoCol.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        tipoCol.setPrefWidth(200);
        
        TableColumn<TokenTableRow, String> lexemaCol = new TableColumn<>("Lexema");
        lexemaCol.setCellValueFactory(new PropertyValueFactory<>("lexema"));
        lexemaCol.setPrefWidth(150);
        
        TableColumn<TokenTableRow, String> lineaCol = new TableColumn<>("Línea");
        lineaCol.setCellValueFactory(new PropertyValueFactory<>("linea"));
        lineaCol.setPrefWidth(80);
        
        TableColumn<TokenTableRow, String> columnaCol = new TableColumn<>("Columna");
        columnaCol.setCellValueFactory(new PropertyValueFactory<>("columna"));
        columnaCol.setPrefWidth(80);
        
        tokensTable.getColumns().addAll(tipoCol, lexemaCol, lineaCol, columnaCol);
        VBox.setVgrow(tokensTable, Priority.ALWAYS);
        
        // Visualización del autómata
        Label automataTitle = new Label("Autómata Finito");
        automataTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        
        automataViz = new AutomataVisualization();
        VBox.setVgrow(automataViz.getPane(), Priority.ALWAYS);
        
        // Área de errores
        Label errorsTitle = new Label("Errores Léxicos");
        errorsTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #d32f2f;");
        
        errorsArea = new TextArea();
        errorsArea.setEditable(false);
        errorsArea.setPrefHeight(100);
        errorsArea.setStyle("-fx-font-family: 'Consolas', monospace; -fx-text-fill: #d32f2f;");
        
        panel.getChildren().addAll(
            tokensTitle, tokensTable,
            new Separator(),
            automataTitle, automataViz.getPane(),
            new Separator(),
            errorsTitle, errorsArea
        );
        
        return panel;
    }
    
    /**
     * Crea la barra de menú
     */
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        
        Menu fileMenu = new Menu("Archivo");
        MenuItem loadItem = new MenuItem("Cargar archivo...");
        loadItem.setOnAction(e -> loadFile());
        MenuItem exitItem = new MenuItem("Salir");
        exitItem.setOnAction(e -> Platform.exit());
        fileMenu.getItems().addAll(loadItem, new SeparatorMenuItem(), exitItem);
        
        Menu helpMenu = new Menu("Ayuda");
        MenuItem aboutItem = new MenuItem("Acerca de");
        aboutItem.setOnAction(e -> showAbout());
        helpMenu.getItems().add(aboutItem);
        
        menuBar.getMenus().addAll(fileMenu, helpMenu);
        return menuBar;
    }
    
    /**
     * Analiza el código fuente y muestra los tokens
     */
    private void analyzeCode() {
        String code = codeEditor.getText();
        if (code.isEmpty()) {
            statusLabel.setText("Error: No hay código para analizar");
            return;
        }
        
        try {
            // Limpiar resultados anteriores
            tokensData.clear();
            errorsArea.clear();
            
            // Crear el lexer
            StringReader reader = new StringReader(code);
            Lexer lexer = new Lexer(reader);
            lexer.resetErrores();
            
            // Analizar tokens
            List<Token> tokens = new ArrayList<>();
            Token token;
            int tokenCount = 0;
            
            while ((token = lexer.nextToken()) != null && 
                   token.getTipo() != Token.TipoToken.FIN_ARCHIVO) {
                tokens.add(token);
                tokenCount++;
                
                // Agregar a la tabla
                tokensData.add(new TokenTableRow(
                    token.getTipoString(),
                    token.getLexema(),
                    String.valueOf(token.getLinea()),
                    String.valueOf(token.getColumna())
                ));
            }
            
            // Mostrar errores
            List<String> errores = lexer.getErrores();
            if (!errores.isEmpty()) {
                StringBuilder errorText = new StringBuilder();
                for (String error : errores) {
                    errorText.append(error).append("\n");
                }
                errorsArea.setText(errorText.toString());
            }
            
            // Actualizar visualización del autómata
            automataViz.updateAutomata(tokens);
            
            // Actualizar estado
            statusLabel.setText(String.format("Análisis completado: %d tokens encontrados", tokenCount));
            
        } catch (Exception e) {
            statusLabel.setText("Error al analizar: " + e.getMessage());
            errorsArea.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Limpia el editor y los resultados
     */
    private void clearAll() {
        codeEditor.clear();
        tokensData.clear();
        errorsArea.clear();
        automataViz.clear();
        statusLabel.setText("Listo");
    }
    
    /**
     * Carga un archivo de código fuente
     */
    private void loadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Cargar archivo de código fuente");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Archivos de texto", "*.txt", "*.code", "*.*")
        );
        
        Stage stage = (Stage) codeEditor.getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);
        
        if (file != null) {
            try {
                String content = Files.readString(file.toPath());
                codeEditor.setText(content);
                statusLabel.setText("Archivo cargado: " + file.getName());
            } catch (IOException e) {
                statusLabel.setText("Error al cargar archivo: " + e.getMessage());
            }
        }
    }
    
    /**
     * Guarda los tokens en un archivo
     */
    private void saveTokens() {
        if (tokensData.isEmpty()) {
            statusLabel.setText("No hay tokens para guardar");
            return;
        }
        
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar tokens");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Archivos de texto", "*.txt")
        );
        
        Stage stage = (Stage) codeEditor.getScene().getWindow();
        File file = fileChooser.showSaveDialog(stage);
        
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                writer.println("TOKENS IDENTIFICADOS");
                writer.println("===================");
                writer.println();
                
                for (TokenTableRow row : tokensData) {
                    writer.printf("%-30s %-20s Línea: %-5s Columna: %-5s%n",
                        row.getTipo(), row.getLexema(), row.getLinea(), row.getColumna());
                }
                
                statusLabel.setText("Tokens guardados en: " + file.getName());
            } catch (IOException e) {
                statusLabel.setText("Error al guardar: " + e.getMessage());
            }
        }
    }
    
    /**
     * Muestra el diálogo "Acerca de"
     */
    private void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acerca de");
        alert.setHeaderText("Analizador Léxico");
        alert.setContentText(
            "Analizador léxico desarrollado con JFlex y JavaFX\n\n" +
            "Versión: 1.0\n" +
            "Desarrollado para el curso de Compiladores"
        );
        alert.showAndWait();
    }
}
