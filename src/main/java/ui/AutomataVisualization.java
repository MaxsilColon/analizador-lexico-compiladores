package ui;

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import lexer.Token;

import java.util.ArrayList;
import java.util.List;

/**
 * Componente para visualizar el autómata finito del analizador léxico
 */
public class AutomataVisualization {
    
    private Pane pane;
    private Canvas canvas;
    private GraphicsContext gc;
    private List<Token> currentTokens;
    
    public AutomataVisualization() {
        pane = new VBox();
        pane.setPadding(new Insets(10));
        pane.setStyle("-fx-background-color: #fafafa;");
        
        canvas = new Canvas(500, 300);
        gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);
        
        currentTokens = new ArrayList<>();
        drawInitialAutomata();
    }
    
    /**
     * Dibuja el autómata inicial (sin tokens procesados)
     */
    private void drawInitialAutomata() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // Título
        gc.setFill(Color.BLACK);
        gc.setFont(javafx.scene.text.Font.font("Arial", 12));
        gc.fillText("Autómata Finito - Estados", 10, 20);
        
        // Dibujar estados básicos
        drawState(100, 100, "INICIAL", Color.LIGHTBLUE);
        drawState(250, 100, "COMENTARIO", Color.LIGHTGREEN);
        drawState(400, 100, "CADENA", Color.LIGHTYELLOW);
        
        // Dibujar transiciones
        drawArrow(150, 100, 220, 100, "//");
        drawArrow(300, 100, 370, 100, "\"");
        
        // Leyenda
        gc.setFill(Color.GRAY);
        gc.fillText("Estados del autómata:", 10, 250);
        gc.setFill(Color.LIGHTBLUE);
        gc.fillRect(10, 260, 15, 15);
        gc.setFill(Color.BLACK);
        gc.fillText("Estado inicial", 30, 272);
        
        gc.setFill(Color.LIGHTGREEN);
        gc.fillRect(150, 260, 15, 15);
        gc.setFill(Color.BLACK);
        gc.fillText("Comentarios", 170, 272);
        
        gc.setFill(Color.LIGHTYELLOW);
        gc.fillRect(280, 260, 15, 15);
        gc.setFill(Color.BLACK);
        gc.fillText("Cadenas", 300, 272);
    }
    
    /**
     * Actualiza la visualización con los tokens procesados
     */
    public void updateAutomata(List<Token> tokens) {
        currentTokens = new ArrayList<>(tokens);
        drawAutomataWithTokens();
    }
    
    /**
     * Dibuja el autómata mostrando el flujo de tokens procesados
     */
    private void drawAutomataWithTokens() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        
        // Título
        gc.setFill(Color.BLACK);
        gc.setFont(javafx.scene.text.Font.font("Arial", 12));
        gc.fillText("Autómata Finito - Procesamiento", 10, 20);
        gc.fillText("Tokens procesados: " + currentTokens.size(), 10, 40);
        
        // Dibujar estados principales
        int yPos = 80;
        drawState(80, yPos, "INICIAL", Color.LIGHTBLUE);
        drawState(220, yPos, "COMENTARIO", Color.LIGHTGREEN);
        drawState(360, yPos, "CADENA", Color.LIGHTYELLOW);
        drawState(500, yPos, "FINAL", Color.LIGHTCORAL);
        
        // Dibujar transiciones
        drawArrow(130, yPos, 190, yPos, "//");
        drawArrow(270, yPos, 330, yPos, "\"");
        drawArrow(410, yPos, 470, yPos, "EOF");
        
        // Mostrar estadísticas de tokens
        int yStats = 180;
        gc.setFill(Color.BLACK);
        gc.setFont(javafx.scene.text.Font.font("Arial", 10));
        gc.fillText("Estadísticas:", 10, yStats);
        
        // Contar tipos de tokens
        long palabrasReservadas = currentTokens.stream()
            .filter(t -> t.getTipo() == Token.TipoToken.PALABRA_RESERVADA)
            .count();
        long identificadores = currentTokens.stream()
            .filter(t -> t.getTipo() == Token.TipoToken.IDENTIFICADOR)
            .count();
        long numeros = currentTokens.stream()
            .filter(t -> t.getTipo() == Token.TipoToken.NUMERO_ENTERO || 
                        t.getTipo() == Token.TipoToken.NUMERO_DECIMAL)
            .count();
        long operadores = currentTokens.stream()
            .filter(t -> t.getTipo().toString().startsWith("OPERADOR"))
            .count();
        
        yStats += 15;
        gc.fillText("Palabras reservadas: " + palabrasReservadas, 10, yStats);
        yStats += 15;
        gc.fillText("Identificadores: " + identificadores, 10, yStats);
        yStats += 15;
        gc.fillText("Números: " + numeros, 10, yStats);
        yStats += 15;
        gc.fillText("Operadores: " + operadores, 10, yStats);
        
        // Mostrar últimos tokens procesados
        if (!currentTokens.isEmpty()) {
            yStats += 20;
            gc.fillText("Últimos tokens:", 10, yStats);
            yStats += 15;
            int start = Math.max(0, currentTokens.size() - 5);
            for (int i = start; i < currentTokens.size() && i < start + 5; i++) {
                Token t = currentTokens.get(i);
                gc.setFill(Color.DARKGRAY);
                gc.fillText(String.format("%s: %s", t.getTipoString(), t.getLexema()), 
                           10, yStats);
                yStats += 12;
            }
        }
    }
    
    /**
     * Dibuja un estado del autómata
     */
    private void drawState(double x, double y, String label, Color color) {
        // Círculo del estado
        gc.setFill(color);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.fillOval(x - 30, y - 15, 60, 30);
        gc.strokeOval(x - 30, y - 15, 60, 30);
        
        // Etiqueta
        gc.setFill(Color.BLACK);
        gc.setFont(javafx.scene.text.Font.font("Arial", 10));
        double textWidth = gc.getFont().getSize() * label.length() / 2;
        gc.fillText(label, x - textWidth/2, y + 5);
    }
    
    /**
     * Dibuja una flecha (transición)
     */
    private void drawArrow(double x1, double y1, double x2, double y2, String label) {
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(1.5);
        gc.strokeLine(x1, y1, x2, y2);
        
        // Punta de la flecha
        double angle = Math.atan2(y2 - y1, x2 - x1);
        double arrowLength = 10;
        double arrowAngle = Math.PI / 6;
        
        double x3 = x2 - arrowLength * Math.cos(angle - arrowAngle);
        double y3 = y2 - arrowLength * Math.sin(angle - arrowAngle);
        double x4 = x2 - arrowLength * Math.cos(angle + arrowAngle);
        double y4 = y2 - arrowLength * Math.sin(angle + arrowAngle);
        
        gc.strokeLine(x2, y2, x3, y3);
        gc.strokeLine(x2, y2, x4, y4);
        
        // Etiqueta de la transición
        if (label != null && !label.isEmpty()) {
            gc.setFill(Color.DARKBLUE);
            gc.setFont(javafx.scene.text.Font.font("Arial", 9));
            double midX = (x1 + x2) / 2;
            double midY = (y1 + y2) / 2 - 10;
            gc.fillText(label, midX - 10, midY);
        }
    }
    
    /**
     * Limpia la visualización
     */
    public void clear() {
        currentTokens.clear();
        drawInitialAutomata();
    }
    
    /**
     * Obtiene el panel de visualización
     */
    public Pane getPane() {
        return pane;
    }
}
