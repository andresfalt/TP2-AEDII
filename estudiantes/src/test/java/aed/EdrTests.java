package aed;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import java.util.Arrays;


class EdrTests {
    Edr edr;
    int d_aula;
    int cant_alumnos;
    int[] solucion;

    @BeforeEach
    void setUp(){
        d_aula = 5;
        cant_alumnos = 4;
        solucion = new int[]{0,1,2,3,4,5,6,7,8,9};

        edr = new Edr(d_aula, cant_alumnos, solucion);
    }

    @Test
    void nuevo_edr() {
        double[] notas = edr.notas();
        double[] notas_esperadas = new double[]{0.0, 0.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }
    
    @Test
    void los_alumnos_resuelven_un_problema() {
        double[] notas;
        double[] notas_esperadas;

        edr.resolver(0, 0, 0);
        notas = edr.notas();
        notas_esperadas = new double[]{10.0, 0.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        edr.resolver(1, 0, 2);
        notas = edr.notas();
        notas_esperadas = new double[]{10.0, 0.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        edr.resolver(2, 4, 4);
        notas = edr.notas();
        notas_esperadas = new double[]{10.0, 0.0, 10.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        edr.resolver(3, 9, 8);
        notas = edr.notas();
        notas_esperadas = new double[]{10.0, 0.0, 10.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    @Test
    void los_alumnos_resuelven_varios_problemas() {
        double[] notas;
        double[] notas_esperadas;

        for(int pregunta = 0; pregunta < 5; pregunta++){
            edr.resolver(0, pregunta, pregunta);
            notas = edr.notas();
            notas_esperadas = new double[]{10.0*(pregunta+1), 0.0, 0.0, 0.0};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }

        for(int pregunta = 9; pregunta > 2; pregunta--){
            edr.resolver(1, pregunta, 9-pregunta);
            notas = edr.notas();
            notas_esperadas = new double[]{50.0, 0.0, 0.0, 0.0};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }

        for(int pregunta = 0; pregunta < 10; pregunta+=2){
            edr.resolver(2, pregunta, pregunta);
            notas = edr.notas();
            notas_esperadas = new double[]{50.0, 0.0, 10.0*(pregunta/2+1), 0.0};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }

        double nota_3 = 0.0;
        for(int pregunta = 0; pregunta < 10; pregunta++){
            //si la pregunta es par, responde bien, si no es par responde mal.
            int respuesta = pregunta % 2 == 0 ? pregunta : 9 - pregunta;
            nota_3 += 10.0 * ((pregunta+1)%2);
            edr.resolver(3, pregunta, respuesta);
            notas = edr.notas();
            notas_esperadas = new double[]{50.0, 0.0, 50.0, nota_3};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }

        for(int pregunta = 5; pregunta < 10; pregunta++){
            edr.resolver(0, pregunta, pregunta);
            notas = edr.notas();
            notas_esperadas = new double[]{10.0*(pregunta+1), 0.0, 50.0, 50.0};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }
    }

    @Test
    void revisar_copias_examenes_completos() {
        double[] notas;
        double[] notas_esperadas;

        for(int pregunta = 0; pregunta < 10; pregunta++){
            edr.resolver(0, pregunta, pregunta);
            notas = edr.notas();
            notas_esperadas = new double[]{10.0*(pregunta+1), 0.0, 0.0, 0.0};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }
        edr.entregar(0);

        for(int pregunta = 9; pregunta > -1; pregunta--){
            edr.resolver(1, pregunta, 9-pregunta);
            notas = edr.notas();
            notas_esperadas = new double[]{100.0, 0.0, 0.0, 0.0};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }
        edr.entregar(1);

        double nota_2 = 0.0;
        for(int pregunta = 0; pregunta < 10; pregunta++){
            //solo se responden bien las preguntas pares
            edr.resolver(2, pregunta, (pregunta % 2 == 0 ? pregunta : 0));
            nota_2 += pregunta % 2 == 0 ? 10.0 : 0.0;
            notas = edr.notas();
            notas_esperadas = new double[]{100.0, 0.0, nota_2, 0.0};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }
        edr.entregar(2);

        double nota_3 = 0.0;
        for(int pregunta = 0; pregunta < 10; pregunta++){
            //si la pregunta es par, responde bien, si no es par responde mal.
            int respuesta = pregunta % 2 == 0 ? pregunta : 9 - pregunta;
            nota_3 += 10.0 * ((pregunta+1)%2);
            edr.resolver(3, pregunta, respuesta);
            notas = edr.notas();
            notas_esperadas = new double[]{100.0, 0.0, 50.0, nota_3};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }
        edr.entregar(3);

        int[] copiones = edr.chequearCopias(); 
        int[] copiones_esperados = new int[]{3};
        assertTrue(Arrays.equals(copiones_esperados, copiones));
        
    }

    @Test
    void revisar_copias_examenes_incompletos() {
        double[] notas;
        double[] notas_esperadas;

        for(int pregunta = 0; pregunta < 3; pregunta++){
            edr.resolver(0, pregunta, pregunta);
            notas = edr.notas();
            notas_esperadas = new double[]{10.0*(pregunta+1), 0.0, 0.0, 0.0};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }
        edr.entregar(0);

        for(int pregunta = 9; pregunta > 8; pregunta--){
            edr.resolver(1, pregunta, 9-pregunta);
            notas = edr.notas();
            notas_esperadas = new double[]{30.0, 0.0, 0.0, 0.0};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }
        edr.entregar(1);

        edr.resolver(2, 9, 9);
        notas = edr.notas();
        notas_esperadas = new double[]{30.0, 0.0, 10.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        edr.entregar(2);

        for(int pregunta = 0; pregunta < 5; pregunta++){
            edr.resolver(3, pregunta, pregunta);
            notas = edr.notas();
            notas_esperadas = new double[]{30.0, 0.0, 10.0, 10.0*(pregunta+1)};
            assertTrue(Arrays.equals(notas_esperadas, notas));
        }
        edr.entregar(3);

        int[] copiones = edr.chequearCopias(); 
        int[] copiones_esperados = new int[]{0};
        assertTrue(Arrays.equals(copiones_esperados, copiones));
        
    }

    @Test
    void no_hay_copiones() {
        double[] notas;
        double[] notas_esperadas;

        for(int pregunta = 0; pregunta < 10; pregunta++){
            edr.resolver(0, pregunta, 0);
            edr.resolver(1, pregunta, 1);
            edr.resolver(2, pregunta, 2);
            edr.resolver(3, pregunta, 3);
        }
        for(int alumno = 0; alumno < 4; alumno++){
            edr.entregar(alumno);
        }
        notas = edr.notas();
        notas_esperadas = new double[]{10.0, 10.0, 10.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        int[] copiones = edr.chequearCopias();
        int[] copiones_esperados = new int[]{};
        assertTrue(Arrays.equals(copiones_esperados, copiones));

        NotaFinal[] notas_finales = edr.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[]{
            new NotaFinal(10.0, 3),
            new NotaFinal(10.0, 2),
            new NotaFinal(10.0, 1),
            new NotaFinal(10.0, 0)
        };

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));

    }

    @Test
    void todos_copiones() {    //cambiar este test
        double[] notas;
        double[] notas_esperadas;

        for(int pregunta = 0; pregunta < 3; pregunta+=2){
            edr.resolver(0, pregunta, 5);
            edr.resolver(1, pregunta, 5);
            edr.resolver(2, pregunta, 5);
            edr.resolver(3, pregunta, 5);
        }

        for(int pregunta = 5; pregunta < 8; pregunta+=2){
            edr.resolver(0, pregunta, 5);
            edr.resolver(1, pregunta, 5);
            edr.resolver(2, pregunta, 5);
            edr.resolver(3, pregunta, 5);
        }

        for(int alumno = 0; alumno < 4; alumno++){
            edr.entregar(alumno);
        }

        notas = edr.notas();
        notas_esperadas = new double[]{10.0, 10.0, 10.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        int[] copiones = edr.chequearCopias();
        int[] copiones_esperados = new int[]{0,1,2,3};
        assertTrue(Arrays.equals(copiones_esperados, copiones));

        NotaFinal[] notas_finales = edr.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[]{};

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));

    }

    @Test
    void copias_de_exacto_25_porciento() {
        Edr edr_9 = new Edr(d_aula, 9, solucion);
        double[] notas;
        double[] notas_esperadas;

        for(int pregunta = 0; pregunta < 3; pregunta++){
            edr_9.resolver(0, pregunta, 5);
            edr_9.resolver(1, pregunta, 5);
            edr_9.resolver(2, pregunta, 5);
        }

        for(int pregunta = 5; pregunta < 9; pregunta++){
            edr_9.resolver(3, pregunta, pregunta);
            edr_9.resolver(4, pregunta, pregunta);
            edr_9.resolver(5, pregunta, pregunta);
        }
        edr_9.resolver(3, 9, 9);

        for(int alumno = 0; alumno < 9; alumno++){
            edr_9.entregar(alumno);
        }

        notas = edr_9.notas();
        notas_esperadas = new double[]{0.0, 0.0, 0.0, 50.0, 40.0, 40.0, 0.0, 0.0, 0.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        int[] copiones = edr_9.chequearCopias();
        int[] copiones_esperados = new int[]{0,1,2,4,5};
        assertTrue(Arrays.equals(copiones_esperados, copiones));

        NotaFinal[] notas_finales = edr_9.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[]{
            new NotaFinal(50.0, 3),
            new NotaFinal(0.0, 8),
            new NotaFinal(0.0, 7),
            new NotaFinal(0.0, 6)
        };

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));

    }

    @Test
    void alumnos_se_copian_una_vez(){
        double[] notas;
        double[] notas_esperadas;

        edr.resolver(0, 0, 0);
        edr.resolver(1, 1, 1);
        edr.resolver(2, 2, 2);
        edr.resolver(3, 3, 3);

        notas = edr.notas();
        notas_esperadas = new double[]{10.0, 10.0, 10.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        edr.copiarse(2);
        
        notas = edr.notas();
        notas_esperadas = new double[]{10.0, 10.0, 20.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        
        edr.copiarse(3);
        
        notas = edr.notas();
        notas_esperadas = new double[]{10.0, 10.0, 20.0, 20.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        for(int alumno = 0; alumno < 4; alumno++){
            edr.entregar(alumno);
        }

        int[] copiones = edr.chequearCopias();
        int[] copiones_esperados = new int[]{2,3};
        assertTrue(Arrays.equals(copiones_esperados, copiones));

        NotaFinal[] notas_finales = edr.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[]{
            new NotaFinal(10.0, 1),
            new NotaFinal(10.0, 0)
        };

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));

    }

    @Test
    void alumnos_se_copian_mas_de_una_vez(){
        double[] notas;
        double[] notas_esperadas;

        edr.resolver(0, 0, 0);
        edr.resolver(1, 1, 1);
        edr.resolver(2, 2, 2);
        edr.resolver(3, 3, 3);

        
        notas = edr.notas();
        notas_esperadas = new double[]{10.0, 10.0, 10.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        
        edr.copiarse(1);
        
        notas = edr.notas();
        notas_esperadas = new double[]{10.0, 20.0, 10.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        edr.copiarse(1);
        
        notas = edr.notas();
        notas_esperadas = new double[]{10.0, 30.0, 10.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        
        edr.copiarse(2);
        
        notas = edr.notas();
        notas_esperadas = new double[]{10.0, 30.0, 20.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));

        edr.copiarse(2);
        
        notas = edr.notas();
        notas_esperadas = new double[]{10.0, 30.0, 30.0, 10.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        
        edr.copiarse(3);
        
        notas = edr.notas();
        notas_esperadas = new double[]{10.0, 30.0, 30.0, 20.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        
        edr.resolver(0, 4, 0);
        edr.resolver(1, 5, 1);

        edr.copiarse(2);
        
        notas = edr.notas();
        notas_esperadas = new double[]{10.0, 30.0, 40.0, 20.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
        
        edr.copiarse(3);
        
        notas = edr.notas();
        notas_esperadas = new double[]{10.0, 30.0, 40.0, 30.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));


        for(int alumno = 0; alumno < 4; alumno++){
            edr.entregar(alumno);
        }

        int[] copiones = edr.chequearCopias();
        int[] copiones_esperados = new int[]{2,3};
        assertTrue(Arrays.equals(copiones_esperados, copiones));

        NotaFinal[] notas_finales = edr.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[]{
            new NotaFinal(30.0, 1),
            new NotaFinal(10.0, 0)
        };

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));
    }

    @Test 
    void un_alumno_se_copia_de_la_darkweb(){
        double[] notas;
        double[] notas_esperadas;
        //todes resuelven bien un ejercicio excepto el estudiante 0
        for(int estudiante = 1; estudiante < 4; estudiante++){
            for(int pregunta = 0; pregunta <= estudiante; pregunta++){
                edr.resolver(estudiante, pregunta, estudiante);
            }
        }

        //alguien sube la solución con acceso para una persona, debe acceder el alumno 0
        edr.consultarDarkWeb(1, solucion);

        notas = edr.notas();
        notas_esperadas = new double[]{100.0, 10.0, 10.0, 10.0};

        assertTrue(Arrays.equals(notas, notas_esperadas));

        for(int estudiante = 0; estudiante < 4; estudiante++){
            edr.entregar(estudiante);
        }
        int[] copiones = edr.chequearCopias();
        int[] copiones_esperados = new int[]{};

        assertTrue(Arrays.equals(copiones, copiones_esperados));

        NotaFinal[] notas_finales = edr.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[]{
            new NotaFinal(100.0, 0),
            new NotaFinal(10.0, 3),
            new NotaFinal(10.0, 2),
            new NotaFinal(10.0, 1),
        };

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));
    }

    @Test 
    void varios_alumnos_se_copian_de_la_darkweb(){
        double[] notas;
        double[] notas_esperadas;
        //todes resuelven bien un ejercicio excepto el estudiante 0
        for(int estudiante = 1; estudiante < 4; estudiante++){
            edr.resolver(estudiante, estudiante, estudiante);
        }

        //alguien sube la solución con acceso para una persona, debe acceder el alumno 0
        edr.consultarDarkWeb(3, solucion);

        notas = edr.notas();
        notas_esperadas = new double[]{100.0, 100.0, 100.0, 10.0};

        assertTrue(Arrays.equals(notas, notas_esperadas));

        for(int estudiante = 0; estudiante < 4; estudiante++){
            edr.entregar(estudiante);
        }
        int[] copiones = edr.chequearCopias();
        int[] copiones_esperados = new int[]{0,1,2,3};

        assertTrue(Arrays.equals(copiones, copiones_esperados));

        NotaFinal[] notas_finales = edr.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[]{};

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));
    }

    @Test
    void darkweb_no_incluye_estudiantes_que_entregaron() {
        int[] solucion = new int[]{0, 1, 2, 3, 4};
        Edr edr = new Edr(5, 5, solucion);
        
        // Estudiante 0 entrega primero (con peor nota)
        edr.entregar(0);
        
        // Estudiantes 1-4 tienen notas bajas
        edr.resolver(1, 0, 0);
        edr.resolver(2, 1, 1);
        edr.resolver(3, 2, 2);
        edr.resolver(4, 3, 3);
        
        edr.consultarDarkWeb(2, solucion);
        
        double[] notas = edr.notas();
        double[] notas_esperadas = new double[]{0.0, 100.0, 100.0, 20.0, 20.0};
        assertTrue(Arrays.equals(notas_esperadas, notas));
    }

    @Test
    void varios_alumnos_se_copian_de_varios_examenes(){
        double[] notas;
        double[] notas_esperadas;
        Edr edr_8 = new Edr(d_aula, 8, solucion);
        int[] resolucion_1 = new int[]{9,8,7,6,5,4,3,2,1,0};
        int[] resolucion_2 = new int[]{2,2,2,2,2,2,2,2,2,2};
        int[] resolucion_3 = new int[]{0,0,2,2,5,6,7,0,0,9};

        for(int capa = 7; capa >= 0; capa--){
            for(int estudiante = 0; estudiante <= capa; estudiante++){
                edr_8.resolver(estudiante, capa, capa);
            }
        }

        notas = edr_8.notas();
        notas_esperadas = new double[]{80.0, 70.0, 60.0, 50.0, 40.0, 30.0, 20.0, 10.0};

        assertTrue(Arrays.equals(notas, notas_esperadas));

        edr_8.consultarDarkWeb(3, resolucion_1);

        notas = edr_8.notas();
        notas_esperadas = new double[]{80.0, 70.0, 60.0, 50.0, 40.0, 0.0, 0.0, 0.0};

        assertTrue(Arrays.equals(notas, notas_esperadas));

        edr_8.consultarDarkWeb(5, resolucion_2);

        notas = edr_8.notas();
        notas_esperadas = new double[]{80.0, 70.0, 60.0, 10.0, 10.0, 10.0, 10.0, 10.0};

        assertTrue(Arrays.equals(notas, notas_esperadas));

        edr_8.consultarDarkWeb(2, resolucion_3);

        notas = edr_8.notas();
        notas_esperadas = new double[]{80.0, 70.0, 60.0, 30.0, 30.0, 10.0, 10.0, 10.0};

        assertTrue(Arrays.equals(notas, notas_esperadas));

        for(int estudiante = 0; estudiante < 8; estudiante++){
            edr_8.entregar(estudiante);
        }
        int[] copiones = edr_8.chequearCopias();
        int[] copiones_esperados = new int[]{2,5,6,7};

        assertTrue(Arrays.equals(copiones, copiones_esperados));

        NotaFinal[] notas_finales = edr_8.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[]{
            new NotaFinal(80.0, 0),
            new NotaFinal(70.0, 1),
            new NotaFinal(30.0, 4),
            new NotaFinal(30.0, 3),
            
        };

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));
    }

    @Test 
    void no_hay_aliasing_con_los_examenes_subidos(){
        double[] notas;
        double[] notas_esperadas;
        int[] resolucion_dark = new int[]{0,0,2,2,5,6,7,0,0,9};
        //todes resuelven bien un ejercicio excepto el estudiante 0
        for(int estudiante = 1; estudiante < 4; estudiante++){
            edr.resolver(estudiante, estudiante, estudiante);
        }

        //alguien sube la solución con acceso para una persona, debe acceder el alumno 0
        edr.consultarDarkWeb(1, resolucion_dark);

        notas = edr.notas();
        notas_esperadas = new double[]{30.0, 10.0, 10.0, 10.0};

        assertTrue(Arrays.equals(notas, notas_esperadas));

        //al cambiar la resolución, no debería cambiar la nota ni el resultado del examen
        resolucion_dark[1] = 1;
        resolucion_dark[3] = 3;

        notas = edr.notas();
        double[] notas_erroneas = new double[]{50.0, 10.0, 10.0, 10.0};
        
        assertFalse(Arrays.equals(notas, notas_erroneas));

        for(int estudiante = 0; estudiante < 4; estudiante++){
            edr.entregar(estudiante);
        }

        int[] copiones = edr.chequearCopias();
        int[] copiones_esperados = new int[]{2};

        assertTrue(Arrays.equals(copiones, copiones_esperados));

        NotaFinal[] notas_finales = edr.corregir();
        NotaFinal[] notas_finales_esperadas = new NotaFinal[]{
            new NotaFinal(30.0, 0),
            new NotaFinal(10.0, 3),
            new NotaFinal(10.0, 1),
        };

        assertTrue(Arrays.equals(notas_finales_esperadas, notas_finales));
    }
}