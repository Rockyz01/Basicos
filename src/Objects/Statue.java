package Objects;

import Juegos.Juego;
import java.util.Random; 

public class Statue extends GameObject {
    
    private String pregunta;
    private String respuestaCorrecta;
    private boolean acertijoResuelto = false;

    private String[] listaPreguntas = {
        "¿Cuál es el planeta más cercano al Sol?",
        "¿Qué gas invisible necesitamos respirar para vivir?",
        "¿Qué fuerza invisible nos mantiene pegados al suelo?",
        "¿Cuál es la estrella luminosa en el centro de nuestro sistema solar?",
        "¿Cuál es la fórmula química del agua?"
    };

    private String[] listaRespuestas = {
        "mercurio",
        "oxigeno",
        "gravedad",
        "sol",
        "h2o"
    };
    // --------------------------

    public Statue(int x, int y, int objType) {
        super(x, y, objType);
        
        initHitBox(32, 40); 
        xDrawOffset = (int) (0 * Juego.SCALE);
        yDrawOffset = (int) (0 * Juego.SCALE);


        Random rand = new Random();
        int indiceAleatorio = rand.nextInt(listaPreguntas.length);
        this.pregunta = listaPreguntas[indiceAleatorio];
        this.respuestaCorrecta = listaRespuestas[indiceAleatorio];
    }

    public String getPregunta() { return pregunta; }
    public String getRespuestaCorrecta() { return respuestaCorrecta; }
    public boolean isAcertijoResuelto() { return acertijoResuelto; }
    public void setAcertijoResuelto(boolean resuelto) { this.acertijoResuelto = resuelto; }
}