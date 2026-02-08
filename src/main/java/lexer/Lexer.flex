package lexer;

import java.util.ArrayList;
import java.util.List;

%%

%class Lexer
%public
%unicode
%line
%column
%type Token
%function nextToken

%{
    private List<String> errores = new ArrayList<>();
    private StringBuffer cadena = new StringBuffer();
    private StringBuffer comentario = new StringBuffer();
    
    public List<String> getErrores() {
        return errores;
    }
    
    public void resetErrores() {
        errores.clear();
    }
    
    private Token crearToken(Token.TipoToken tipo, String lexema) {
        return new Token(tipo, lexema, yyline + 1, yycolumn + 1);
    }
%}

// Definición de estados
%state COMENTARIO_BLOQUE
%state COMENTARIO_LINEA
%state CADENA

// Definición de patrones
DIGITO = [0-9]
LETRA = [a-zA-Z_]
ESPACIO = [ \t]
NUEVA_LINEA = \r|\n|\r\n

// Palabras reservadas
IF = "if"
ELSE = "else"
WHILE = "while"
FOR = "for"
INT = "int"
FLOAT = "float"
STRING = "string"
BOOLEAN = "boolean"
TRUE = "true"
FALSE = "false"
RETURN = "return"
VOID = "void"

// Operadores
OPERADOR_SUMA = "+"
OPERADOR_RESTA = "-"
OPERADOR_MULT = "*"
OPERADOR_DIV = "/"
OPERADOR_ASIG = "="
OPERADOR_IGUAL = "=="
OPERADOR_DIF = "!="
OPERADOR_MENOR = "<"
OPERADOR_MAYOR = ">"
OPERADOR_MENOR_IGUAL = "<="
OPERADOR_MAYOR_IGUAL = ">="

// Delimitadores
PARENTESIS_IZQ = "("
PARENTESIS_DER = ")"
LLAVE_IZQ = "{"
LLAVE_DER = "}"
CORCHETE_IZQ = "["
CORCHETE_DER = "]"
PUNTO_COMA = ";"
COMA = ","

// Comentarios
COMENTARIO_LINEA_INI = "//"
COMENTARIO_BLOQUE_INI = "/*"
COMENTARIO_BLOQUE_FIN = "*/"

// Identificadores
IDENTIFICADOR = {LETRA}({LETRA}|{DIGITO})*

// Números
NUMERO_ENTERO = {DIGITO}+
NUMERO_DECIMAL = {DIGITO}+"."{DIGITO}+

// Cadenas
COMILLA = "\""

%%

<YYINITIAL> {
    // Palabras reservadas
    {IF}           { return crearToken(Token.TipoToken.PALABRA_RESERVADA, yytext()); }
    {ELSE}         { return crearToken(Token.TipoToken.PALABRA_RESERVADA, yytext()); }
    {WHILE}        { return crearToken(Token.TipoToken.PALABRA_RESERVADA, yytext()); }
    {FOR}          { return crearToken(Token.TipoToken.PALABRA_RESERVADA, yytext()); }
    {INT}          { return crearToken(Token.TipoToken.PALABRA_RESERVADA, yytext()); }
    {FLOAT}        { return crearToken(Token.TipoToken.PALABRA_RESERVADA, yytext()); }
    {STRING}       { return crearToken(Token.TipoToken.PALABRA_RESERVADA, yytext()); }
    {BOOLEAN}      { return crearToken(Token.TipoToken.PALABRA_RESERVADA, yytext()); }
    {TRUE}         { return crearToken(Token.TipoToken.BOOLEANO, yytext()); }
    {FALSE}        { return crearToken(Token.TipoToken.BOOLEANO, yytext()); }
    {RETURN}       { return crearToken(Token.TipoToken.PALABRA_RESERVADA, yytext()); }
    {VOID}         { return crearToken(Token.TipoToken.PALABRA_RESERVADA, yytext()); }
    
    // Operadores
    {OPERADOR_MAYOR_IGUAL}  { return crearToken(Token.TipoToken.OPERADOR_MAYOR_IGUAL, yytext()); }
    {OPERADOR_MENOR_IGUAL}  { return crearToken(Token.TipoToken.OPERADOR_MENOR_IGUAL, yytext()); }
    {OPERADOR_IGUAL}        { return crearToken(Token.TipoToken.OPERADOR_IGUAL, yytext()); }
    {OPERADOR_DIF}          { return crearToken(Token.TipoToken.OPERADOR_DIFERENTE, yytext()); }
    {OPERADOR_SUMA}         { return crearToken(Token.TipoToken.OPERADOR_SUMA, yytext()); }
    {OPERADOR_RESTA}        { return crearToken(Token.TipoToken.OPERADOR_RESTA, yytext()); }
    {OPERADOR_MULT}         { return crearToken(Token.TipoToken.OPERADOR_MULTIPLICACION, yytext()); }
    {OPERADOR_DIV}          { return crearToken(Token.TipoToken.OPERADOR_DIVISION, yytext()); }
    {OPERADOR_ASIG}         { return crearToken(Token.TipoToken.OPERADOR_ASIGNACION, yytext()); }
    {OPERADOR_MENOR}        { return crearToken(Token.TipoToken.OPERADOR_MENOR, yytext()); }
    {OPERADOR_MAYOR}        { return crearToken(Token.TipoToken.OPERADOR_MAYOR, yytext()); }
    
    // Delimitadores
    {PARENTESIS_IZQ}  { return crearToken(Token.TipoToken.PARENTESIS_IZQ, yytext()); }
    {PARENTESIS_DER}  { return crearToken(Token.TipoToken.PARENTESIS_DER, yytext()); }
    {LLAVE_IZQ}       { return crearToken(Token.TipoToken.LLAVE_IZQ, yytext()); }
    {LLAVE_DER}       { return crearToken(Token.TipoToken.LLAVE_DER, yytext()); }
    {CORCHETE_IZQ}    { return crearToken(Token.TipoToken.CORCHETE_IZQ, yytext()); }
    {CORCHETE_DER}    { return crearToken(Token.TipoToken.CORCHETE_DER, yytext()); }
    {PUNTO_COMA}      { return crearToken(Token.TipoToken.PUNTO_COMA, yytext()); }
    {COMA}            { return crearToken(Token.TipoToken.COMA, yytext()); }
    
    // Comentarios
    {COMENTARIO_LINEA_INI}  { 
        comentario.setLength(0);
        comentario.append(yytext());
        yybegin(COMENTARIO_LINEA);
    }
    {COMENTARIO_BLOQUE_INI} { 
        comentario.setLength(0);
        comentario.append(yytext());
        yybegin(COMENTARIO_BLOQUE);
    }
    
    // Cadenas de texto
    {COMILLA}  { 
        cadena.setLength(0);
        cadena.append(yytext());
        yybegin(CADENA);
    }
    
    // Números
    {NUMERO_DECIMAL}  { return crearToken(Token.TipoToken.NUMERO_DECIMAL, yytext()); }
    {NUMERO_ENTERO}   { return crearToken(Token.TipoToken.NUMERO_ENTERO, yytext()); }
    
    // Identificadores
    {IDENTIFICADOR}   { return crearToken(Token.TipoToken.IDENTIFICADOR, yytext()); }
    
    // Espacios en blanco (ignorar)
    {ESPACIO}         { /* ignorar */ }
    {NUEVA_LINEA}     { /* ignorar */ }
    
    // Cualquier otro carácter es un error
    .                 { 
        errores.add("Carácter no reconocido: '" + yytext() + "' en línea " + (yyline + 1) + ", columna " + (yycolumn + 1));
        return crearToken(Token.TipoToken.ERROR, yytext());
    }
}

<COMENTARIO_LINEA> {
    {NUEVA_LINEA}       { 
        String comentarioCompleto = comentario.toString();
        yybegin(YYINITIAL);
        return crearToken(Token.TipoToken.COMENTARIO_LINEA, comentarioCompleto);
    }
    .                    { 
        comentario.append(yytext());
    }
    <<EOF>>              { 
        String comentarioCompleto = comentario.toString();
        yybegin(YYINITIAL);
        return crearToken(Token.TipoToken.COMENTARIO_LINEA, comentarioCompleto);
    }
}

<COMENTARIO_BLOQUE> {
    {COMENTARIO_BLOQUE_FIN}  { 
        comentario.append(yytext());
        String comentarioCompleto = comentario.toString();
        yybegin(YYINITIAL);
        return crearToken(Token.TipoToken.COMENTARIO_BLOQUE, comentarioCompleto);
    }
    [^]                { 
        comentario.append(yytext());
    }
    <<EOF>>             { 
        errores.add("Comentario de bloque no cerrado en línea " + (yyline + 1));
        String comentarioIncompleto = comentario.toString();
        yybegin(YYINITIAL);
        return crearToken(Token.TipoToken.ERROR, comentarioIncompleto);
    }
}

<CADENA> {
    {COMILLA}           { 
        cadena.append(yytext());
        String cadenaCompleta = cadena.toString();
        yybegin(YYINITIAL);
        return crearToken(Token.TipoToken.CADENA_TEXTO, cadenaCompleta);
    }
    [^\"]               { 
        cadena.append(yytext());
    }
    {NUEVA_LINEA}       { 
        errores.add("Cadena no cerrada en línea " + (yyline + 1));
        String cadenaIncompleta = cadena.toString();
        yybegin(YYINITIAL);
        return crearToken(Token.TipoToken.ERROR, cadenaIncompleta);
    }
    <<EOF>>             { 
        errores.add("Cadena no cerrada al final del archivo");
        String cadenaIncompleta = cadena.toString();
        yybegin(YYINITIAL);
        return crearToken(Token.TipoToken.ERROR, cadenaIncompleta);
    }
}

<<EOF>>                 { return crearToken(Token.TipoToken.FIN_ARCHIVO, "EOF"); }
