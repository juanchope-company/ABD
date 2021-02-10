package com.Archivos;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Juanchope Pruebas
 */
public class Datos implements Serializable{
    private final Map<String, String[]> 
                    antecedentes;

        public Datos(Map<String, String[]> subtitulos) {
            this.antecedentes = subtitulos;
        }

        public Map<String, String[]> getAntecedentes() {
            return antecedentes;
        }






        public static void main(String[] args) {
            Map<String, String[]> subtitulos  = new HashMap(){
                    {
                        put(
                                "Antecedentes Medicos", 
                                new String[]{
                                    "Alergias",
                                    "Discrasias sanguíneas",
                                    "Cardiopatías",
                                    "Embarazo",
                                    "Alteraciones presión arterial",
                                    "Toma de medicamentos",
                                    "Tratamiento médico actual",
                                    "Hepatitis",
                                    "Diabetes",
                                    "Fiebre reumática",
                                    "VIH - SIDA",
                                    "Inmunosupresión",
                                    "Patologías renales",
                                    "Patologías respiratorias",
                                    "Transtornos gástricos",
                                    "Transtornos emocionales",
                                    "Sinusitis",
                                    "Cirugías (incluso orales)",
                                    "Exodoncias",
                                    "Enfermedades orales",
                                    "Uso de prótesis o aparatología oral",
                                    "Otras"
                                });
                        put(
                                "Examen articulacion", 
                                new String[]{
                                    "Ruidos",
                                    "Desviación",
                                    "Cambio de volumen",
                                    "Bloqueo mandibular",
                                    "Limitación de apertura",
                                    "Dolor articular",
                                    "Dolor muscular",
                                    "Otros"
                                }
                        );
                        put(
                                "Examen tejidos blandos", 
                                new String[]{
                                "Labio inferior",
                                "Labio superior",
                                "Comisuras",
                                "Mucosa oral",
                                "Surcos yugales",
                                "Frenillos",
                                "Orofaringe",
                                "Paladar",
                                "Glándulas salivales",
                                "Piso de boca",
                                "Dorso de lengua",
                                "Vientre de lengua"
                            }
                        );
                        put(
                                "Examen oclusal",
                                new String[]{
                                "Supernumerarios",
                                "Decoloración",
                                "Descalcificación",
                                "Facetas de desgaste",
                                "Abrasión y/o erosión",
                                "Mal posiciones dentarias",
                                "Otro"
                            }
                        );
                        put(
                                "Examen periodontal", 
                                new String[]{
                                    "Sangrado",
                                    "Exudado",
                                    "Supuración",
                                    "Cálculos",
                                    "Inflamación",
                                    "Retracciones",
                                    "Presencia bolsas",
                                    "Otro"
                                }
                        );
                    }
                };
            Datos obj1 = new Datos(subtitulos);
            Ajustes obj = new Ajustes(obj1);
            Ajustes.setAjustes(obj);

            System.out.println(Ajustes.getAjustes());
        }
}
