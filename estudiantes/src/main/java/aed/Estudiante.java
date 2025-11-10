package aed;


class Estudiante {
    int[] _examen;
    double _puntaje;
    Handle _handle;
    int _fila;
    int _col;
    
    Estudiante(int cantRespuestas, int fila, int col) {
        _examen = new int[cantRespuestas];
        for (int i = 0; i < cantRespuestas; i++) {
            _examen[i] = -1;
        }
        _puntaje = 0;
        _fila = fila;
        _col = col;
    }
}