package aed;
import java.util.ArrayList;

public class Edr {

    private Estudiante[] _estudiantes;
    private MinHeap _puntajes;
    private int[][] _aula;
    private int[] _solucionCanonica;
    private boolean[] _yaEntregaron;
    private boolean[] _esSospechoso;
    private int _cantRespuestas;
    private int _ladoAula;

    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
        // O(E * R)
        _cantRespuestas = ExamenCanonico.length;
        _ladoAula = LadoAula;

        _estudiantes = new Estudiante[Cant_estudiantes];
        _yaEntregaron = new boolean[Cant_estudiantes];
        _esSospechoso = new boolean[Cant_estudiantes];
        _aula = new int[LadoAula][LadoAula];
        _solucionCanonica = ExamenCanonico;
    
        // la matriz empieza con -1 en los asientos 
        for (int i = 0; i < LadoAula; i++) {
            for (int j = 0; j < LadoAula; j++) {
                _aula[i][j] = -1;
            }
        }
        
        // le asignamos los lugares a los estudiantes saltando de a dos en las columnas 
        int id = 0;
        for (int fila = 0; fila < LadoAula && id < Cant_estudiantes; fila++) {
            for (int col = 0; col < LadoAula && id < Cant_estudiantes; col += 2) {

                Estudiante e = new Estudiante(_cantRespuestas, fila, col);
                _estudiantes[id] = e;
                _aula[fila][col] = id;
                id++;
            }
        }
        
        // Construir heap con todos los estudiantes (puntaje 0)
        _puntajes = new MinHeap(Cant_estudiantes);
        for (int i = 0; i < Cant_estudiantes; i++) {
            _estudiantes[i]._handle = _puntajes.insertar(0, i);
        }
    }

//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){
        //Devuelve una secuencia de las notas de todos los estudiantes ordenada por id.
        // O(E)
        double[] resultado = new double[_estudiantes.length];
        for (int i = 0; i < _estudiantes.length; i++) {
            resultado[i] = _estudiantes[i]._puntaje;
        }
        return resultado;
    }

//------------------------------------------------COPIARSE------------------------------------------------------------------------
    public void copiarse(int estudiante) {
        // El/la estudiante se copia del vecino que mas respuestas
        // completadas tenga que el/ella no tenga; se copia solamente la
        // primera de esas respuestas. Desempata por id menor.
        // O(R + log E)
        Estudiante e = _estudiantes[estudiante];
        int fila = e._fila;
        int col = e._col;
        
        // Buscar vecinos: izquierda, derecha, adelante
        ArrayList<Integer> vecinosLista = new ArrayList<>();
        
        // Izquierda
        if (col > 0 && _aula[fila][col - 1] != -1) {
            vecinosLista.add(_aula[fila][col - 1]);
        }
        // Derecha
        if (col < _ladoAula - 1 && _aula[fila][col + 1] != -1) {
            vecinosLista.add(_aula[fila][col + 1]);
        }
        // Adelante
        if (fila > 0 && _aula[fila - 1][col] != -1) {
            vecinosLista.add(_aula[fila - 1][col]);
        }
        
        if (vecinosLista.isEmpty()) return;
        
        // Encontrar vecino con más respuestas que yo no tengo
        int mejorVecino = -1;
        int maxRespuestas = -1;
        
        for (int idVecino : vecinosLista) {
            Estudiante v = _estudiantes[idVecino];
            int respuestasVecino = contarRespuestasQueNoTengo(e, v);
            
            // Actualizar mejor vecino (más respuestas, desempate por menor id)
            if (respuestasVecino > maxRespuestas || 
                (respuestasVecino == maxRespuestas && idVecino < mejorVecino)) {
                mejorVecino = idVecino;
                maxRespuestas = respuestasVecino;
            }
        }
        
        if (mejorVecino == -1 || maxRespuestas == 0) return;
        
        // Copiar primera respuesta que el vecino tenga y yo no O(R)
        Estudiante vecino = _estudiantes[mejorVecino];
        // aca no se como buscarlo 
    }
    private int contarRespuestasQueNoTengo(Estudiante yo, Estudiante otro) {
        // O(R)
        int count = 0;
        for (int i = 0; i < _cantRespuestas; i++) {
            if (yo._examen[i] == -1 && otro._examen[i] != -1) {
                count++;
            }
        }
        return count;
    }

//-----------------------------------------------RESOLVER----------------------------------------------------------------
    public void resolver(int estudiante, int NroEjercicio, int res) {
        //El/la estudiante resuelve un ejercicio
        // O(log E)
        Estudiante e = _estudiantes[estudiante];
        e._examen[NroEjercicio] = res;
        e._puntaje = calcularPuntaje(e._examen);
        _puntajes.actualizarPrioridad(e._handle, e._puntaje, estudiante);
    }
    
    private double calcularPuntaje(int[] examen) {
        // O(R)
        int correctas = 0;
        for (int i = 0; i < _cantRespuestas; i++) {
            if (examen[i] != -1 && examen[i] == _solucionCanonica[i]) {
                correctas++;
            }
        }
        return Math.floor(100.0 * correctas / _cantRespuestas);
    }

//------------------------------------------------CONSULTAR DARK WEB-------------------------------------------------------
    public void consultarDarkWeb(int n, int[] examenDW) {
        // Los/as k estudiantes que tengan el
        // peor puntaje (hasta el momento) reemplazan completamente su 
        // examen por el examenDW. Nota: en caso de empate en el puntaje,
        // se desempata por menor id de estudiante.
        // O(k * (R + log E)) - peor caso O(E * (R + log E))        
        // IMPORTANTE: Solo extraer y procesar, no reinsertar los que ya entregaron
        // Si ya entregó, simplemente no lo reinsertamos
        // (queda fuera del heap permanentemente)
        
    }

//-------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        // El/la estudiante entrega su examen
        // O(log E) según enunciado actualizado
        _yaEntregaron[estudiante] = true;
        // Extraer del heap para que no sea considerado en consultarDarkWeb
        // (alternativa: mantener en heap y filtrar en consultarDarkWeb)
    }
//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        // Devuelve las notas de los examenes de los estudiantes 
        // que no se hayan copiado ordenada por NotaFinal.nota de forma
        // decreciente. En caso de empate, se desempata 
        // por mayor NotaFinal.id de estudiante
        ArrayList<NotaFinal> temp = new ArrayList<>();
        ArrayList<ParPuntajeId> todosLosElementos = new ArrayList<>();
        return null;  // PUSE ESTO PARA QUE NO TIRE ERROR
    }

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    public int[] chequearCopias() {
        // Devuelve la lista de los estudiantes
        // sospechosos de haberse copiado ordenada por id de estudiante.
        // Encontrar el valor máximo de respuesta para dimensionar el array
        return null; // PUSE ESTO PARA QUE NO TIRE ERROR
    }
}
