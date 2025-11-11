package aed;
import java.util.ArrayList;

public class Edr {

    private Estudiante[] _estudiantes; // varia su puntaje y su examen 
    //    int[] _examen; para mi es fijo con la cant de rtas de canonico
    //    double _puntaje; va variando de acuerdo a como resuelve el alumno
    //    Handle _handle; // tiene una lista de puntaje de estudiantes ordenados por id 
    //    int _fila;
    //    int _col;
    private MinHeap _puntajes;
    //     private ParPuntajeId[] heap; // MinHeap de ParPuntajeId ordenados por Puntaje. El puntaje es la parte entera del porcentaje de respuestas correctas
    //                   → double puntaje;
    //                   → int id;
    //     private Handle[] handles; // Array de handles (nuestro heap) ordenado por IDs, es un array de posiciones 
    // si el handle en este caso es una lista entonces la posicion te da el dato de la ubicacion de ese id ?
    //     private int size; // Tamaño del heap, para mi aca es siempre la cant de estudiantes 
    //     private int capacidad; // Capacidad máxima del heap, para mi acalo mismo, es cant de estudiantes asi que solo usariamos uno solo?  size o capacidad
    private int[][] _aula;
    /* cada alumno tendra asigada una posison  */
    private int[] _solucionCanonica;// en dato
    private boolean[] _yaEntregaron; // varia durante el examen, no en tamaño sino en sus valores
    private boolean[] _esSospechoso; //  varia durante el examen, no en tamaño sino en sus valores
    private int _cantRespuestas; // varia durante el examen
    private int _ladoAula; // es dato
    private int[][] _conteoRespuestas;// varia durante el examen
    

    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
        // O(E * R)
        _cantRespuestas = ExamenCanonico.length;
        _ladoAula = LadoAula;
        _estudiantes = new Estudiante[Cant_estudiantes];
        // arranca con una lista de estudiantes dado, puede ser cero ya que en ese caso no se presenta nadie
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
        
        // le asignamos los lugares a los estudiantes saltando de a dos en las columnas y su examen inicial tiene valor -1 en sus respuestas
        int id = 0;
        for (int fila = 0; fila < LadoAula && id < Cant_estudiantes; fila++) {
            for (int col = 0; col < LadoAula && id < Cant_estudiantes; col += 2) {

                Estudiante e = new Estudiante(_cantRespuestas, fila, col);
                _estudiantes[id] = e;
                _aula[fila][col] = id;
                id++;
            }
        }
        
        // Construir minheap con todos los estudiantes (iniciando en puntaje 0)
        _puntajes = new MinHeap(Cant_estudiantes);
        for (int i = 0; i < Cant_estudiantes; i++) {
            // clase Estudiantes = MinHeap > insertar 
             // tiene una lista de puntaje de estudiantes ordenados por id 
            _estudiantes[i]._handle = _puntajes.insertar(0, i);
        }
    }

//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){
        //Devuelve una secuencia de las notas de todos los estudiantes ordenada por id.
        // O(E)
        double[] resultado = new double[_estudiantes.length];
        for (int i = 0; i < _estudiantes.length; i++) {
            // recorre la lista estudiantes y retorna el atributo puntaje guardado en la lista resultado 
            resultado[i] = _estudiantes[i]._puntaje;
        }
        return resultado;
    }

//------------------------------------------------COPIARSE------------------------------------------------------------------------
    public void copiarse(int estudiante) {
        // El/la estudiante se copia del vecino que mas respuestas
        // completadas tenga que el/ella no tenga; se copia solamente la
        // primera de esas respuestas. Desempata por id menor.
        // busco el id mas chico de los vecinos mas cercanos con mas respuestas que el no tenga.
        // busco por id y accedo a su examen en ese momento , se copia del nuemero de respuesta mas chico 
        // actualizo el puntaje de ese estudiante que se copio en mi minheap 
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
    
    // Buscar la primera respuesta a copiar
    for (int i = 0; i < _cantRespuestas; i++) {
        if (e._examen[i] == -1 && vecino._examen[i] != -1) {
            // Copiar la respuesta
            e._examen[i] = vecino._examen[i];
            
            // SIEMPRE actualizar el puntaje (no solo si es correcta)
            e._puntaje = actualizarPuntaje(e);
            // Actualizar en el heap (O(log E))
            _puntajes.actualizarPrioridad(e._handle, e._puntaje, estudiante);
            
            // Solo copiar UNA respuesta (la primera encontrada)
            break;
        }
    }
}

    private double actualizarPuntaje(Estudiante e) {
        // O(R)
        int correctas = 0;
        for (int i = 0; i < _cantRespuestas; i++) {
            if (e._examen[i] != -1 && e._examen[i] == _solucionCanonica[i]) {
                correctas++;
            }
        }
        return (correctas * 100.0) / _cantRespuestas;
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
    public void consultarDarkWeb(int k, int[] examenDW) {
        // Los/as k estudiantes que tengan el
        // peor puntaje (hasta el momento) reemplazan completamente su 
        // examen por el examenDW. Nota: en caso de empate en el puntaje,
        // se desempata por menor id de estudiante.
        // O(k * (R + log E)) - peor caso O(E * (R + log E))        
        // IMPORTANTE: Solo extraer y procesar, no reinsertar los que ya entregaron
        // Si ya entregó, simplemente no lo reinsertamos
        // (queda fuera del heap permanentemente)

        int procesados = 0;
        double puntajeDW = calcularPuntaje(examenDW); // Lo calculamos una vez, siempre va a ser igual

        while (procesados < k && _puntajes.size() > 0) {
            ParPuntajeId peorParPuntajeId = _puntajes.extraerMin();
            int idEstudiante = peorParPuntajeId.id;
            Estudiante e = _estudiantes[idEstudiante];

            // Si ya entrego, lo saltamos
            if (_yaEntregaron[idEstudiante]) {
                continue;
            }

            // Reemplazo todo el examen
            e._examen = new int[examenDW.length];
            for (int i = 0; i < examenDW.length; i++) {
                e._examen[i] = examenDW[i];
            }         
            // Actualizar puntaje
            e._puntaje = puntajeDW;
        

            // Reinsertamos en el heap con el nuevo puntaje
            e._handle = _puntajes.insertar(puntajeDW, idEstudiante);
            procesados++;
        }
        
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
    // O(E log E)
    
    // Crear un MinHeap temporal con los no sospechosos
    // Insertamos con puntajes negativos para simular MaxHeap
    MinHeap heapTemp = new MinHeap(_estudiantes.length);
    
    for (int id = 0; id < _estudiantes.length; id++) {
        if (!_esSospechoso[id]) {
            // Insertar con puntaje negativo para orden decreciente
            // Para desempate por mayor id, usamos id negativo
            double puntajeParaHeap = -_estudiantes[id]._puntaje;
            heapTemp.insertar(puntajeParaHeap, -id);
        }
    }
    
    // Extraer todos del heap (ya salen ordenados)
    ArrayList<NotaFinal> listaTemp = new ArrayList<>();
    while (heapTemp.size() > 0) {
        ParPuntajeId par = heapTemp.extraerMin();
        double nota = -par.puntaje; // Revertir el signo
        int id = -par.id; // Revertir el signo
        listaTemp.add(new NotaFinal(nota, id));
    }
    
    // Convertir a array
    NotaFinal[] resultado = new NotaFinal[listaTemp.size()];
    for (int i = 0; i < listaTemp.size(); i++) {
        resultado[i] = listaTemp.get(i);
    }
    
    return resultado;
}
//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

public int[] chequearCopias() {
        // Devuelve la lista de los estudiantes sospechosos de haberse copiado ordenada por id.
        // Criterio: Un estudiante es sospechoso si CADA respuesta que dio (no en blanco)
        // es igual a al menos el 25% de todos los estudiantes (sin contarlo a él/ella)
        // O(E * R)
    // O(E * R)
    // Encontrar el valor máximo de respuesta
    int maxRespuesta = 0;
    for (int i = 0; i < _estudiantes.length; i++) {
        for (int j = 0; j < _cantRespuestas; j++) {
            if (_estudiantes[i]._examen[j] > maxRespuesta) {
                maxRespuesta = _estudiantes[i]._examen[j];
            }
        }
    }
    
    // Inicializar matriz de conteo
    _conteoRespuestas = new int[_cantRespuestas][maxRespuesta + 1];
    
    // Contar respuestas de todos los estudiantes O(E * R)
    for (int idEst = 0; idEst < _estudiantes.length; idEst++) {
        for (int numEj = 0; numEj < _cantRespuestas; numEj++) {
            int respuesta = _estudiantes[idEst]._examen[numEj];
            if (respuesta != -1) {
                _conteoRespuestas[numEj][respuesta]++;
            }
        }
    }
    
    // Verificar cada estudiante O(E * R)
    for (int idEst = 0; idEst < _estudiantes.length; idEst++) {
        Estudiante e = _estudiantes[idEst];
        boolean todasCumplen = true;
        boolean tieneAlgunaRespuesta = false;
        
        // Verificar cada respuesta del estudiante
        for (int numEj = 0; numEj < _cantRespuestas; numEj++) {
            int miRespuesta = e._examen[numEj];
            
            // Si no respondí, saltar
            if (miRespuesta == -1) continue;
            
            tieneAlgunaRespuesta = true;
            
            // Contar otros estudiantes con esta respuesta (sin contarme)
            int totalConEstaRespuesta = _conteoRespuestas[numEj][miRespuesta];
            int otrosConEstaRespuesta = totalConEstaRespuesta - 1;
            int otrosEstudiantes = _estudiantes.length - 1;
            
            // Verificar si al menos el 25% de otros tienen esta respuesta
            // otrosConEstaRespuesta * 4 >= otrosEstudiantes
            boolean cumple25Porciento = (otrosConEstaRespuesta * 4 >= otrosEstudiantes);
            
            if (!cumple25Porciento) {
                todasCumplen = false;
                break;
            }
        }
        
        // Es sospechoso si tiene respuestas Y todas cumplen el 25%
        if (tieneAlgunaRespuesta && todasCumplen) {
            _esSospechoso[idEst] = true;
        }
    }
    
    // Recolectar IDs de sospechosos
    ArrayList<Integer> sospechososList = new ArrayList<>();
    for (int id = 0; id < _estudiantes.length; id++) {
        if (_esSospechoso[id]) {
            sospechososList.add(id);
        }
    }
    
    // Convertir a array
    int[] resultado = new int[sospechososList.size()];
    for (int i = 0; i < sospechososList.size(); i++) {
        resultado[i] = sospechososList.get(i);
    }
    
    return resultado;
}
}
