package lexer;

/**
 * Clase que representa un token identificado por el analizador léxico.
 * Contiene información sobre el tipo de token, su lexema, y su posición en el código.
 */
public class Token {
    
    /**
     * Enum que define los tipos de tokens posibles
     */
    public enum TipoToken {
        // Palabras reservadas
        PALABRA_RESERVADA,
        
        // Identificadores y literales
        IDENTIFICADOR,
        NUMERO_ENTERO,
        NUMERO_DECIMAL,
        CADENA_TEXTO,
        BOOLEANO,
        
        // Operadores
        OPERADOR_SUMA,
        OPERADOR_RESTA,
        OPERADOR_MULTIPLICACION,
        OPERADOR_DIVISION,
        OPERADOR_ASIGNACION,
        OPERADOR_IGUAL,
        OPERADOR_DIFERENTE,
        OPERADOR_MENOR,
        OPERADOR_MAYOR,
        OPERADOR_MENOR_IGUAL,
        OPERADOR_MAYOR_IGUAL,
        
        // Delimitadores
        PARENTESIS_IZQ,
        PARENTESIS_DER,
        LLAVE_IZQ,
        LLAVE_DER,
        CORCHETE_IZQ,
        CORCHETE_DER,
        PUNTO_COMA,
        COMA,
        
        // Comentarios
        COMENTARIO_LINEA,
        COMENTARIO_BLOQUE,
        
        // Especiales
        FIN_ARCHIVO,
        ERROR
    }
    
    private TipoToken tipo;
    private String lexema;
    private int linea;
    private int columna;
    
    /**
     * Constructor de Token
     * @param tipo Tipo de token
     * @param lexema Texto del token
     * @param linea Número de línea
     * @param columna Número de columna
     */
    public Token(TipoToken tipo, String lexema, int linea, int columna) {
        this.tipo = tipo;
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
    }
    
    // Getters
    public TipoToken getTipo() {
        return tipo;
    }
    
    public String getLexema() {
        return lexema;
    }
    
    public int getLinea() {
        return linea;
    }
    
    public int getColumna() {
        return columna;
    }
    
    /**
     * Obtiene una representación legible del tipo de token
     */
    public String getTipoString() {
        return tipo.toString().replace("_", " ");
    }
    
    @Override
    public String toString() {
        return String.format("Token[%s, '%s', L:%d, C:%d]", 
                           getTipoString(), lexema, linea, columna);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Token token = (Token) obj;
        return tipo == token.tipo &&
               linea == token.linea &&
               columna == token.columna &&
               lexema.equals(token.lexema);
    }
}
