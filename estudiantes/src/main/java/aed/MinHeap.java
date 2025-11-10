package aed;

class MinHeap {
    private ParPuntajeId[] heap;
    private Handle[] handles;
    private int size;
    private int capacidad;
    
    MinHeap(int cap) {
        capacidad = cap;
        heap = new ParPuntajeId[capacidad];
        handles = new Handle[cap];
        size = 0;
    }
    
    Handle insertar(double puntaje, int id) {
        if (size >= capacidad) {
            ParPuntajeId[] nuevoHeap = new ParPuntajeId[capacidad];
            for (int i = 0; i < size; i++) {
                nuevoHeap[i] = heap[i];
            }
            heap = nuevoHeap;
        }
        
        ParPuntajeId elem = new ParPuntajeId(puntaje, id);
        heap[size] = elem;
        Handle h = new Handle(size);
        if (id < handles.length) {
            handles[id] = h;
        }
        subir(size);
        size++;
        return h;
    }
    
    ParPuntajeId extraerMin() {
        if (size == 0) return null;
        ParPuntajeId min = heap[0];
        size--;
        if (size > 0) {
            heap[0] = heap[size];
            if (heap[0].id < handles.length) {
                handles[heap[0].id].posicion = 0;
            }
            bajar(0);
        }
        return min;
    }
    
    void actualizarPrioridad(Handle h, double nuevoPuntaje, int id) {
        if (h == null) return;
        int pos = h.posicion;
        if (pos >= size) return;
        
        heap[pos].puntaje = nuevoPuntaje;
        
        int padre = (pos - 1) / 2;
        if (pos > 0 && heap[pos].compareTo(heap[padre]) < 0) {
            subir(pos);
        } else {
            bajar(pos);
        }
    }
    
    private void subir(int i) {
        while (i > 0) {
            int padre = (i - 1) / 2;
            if (heap[i].compareTo(heap[padre]) < 0) {
                intercambiar(i, padre);
                i = padre;
            } else {
                break;
            }
        }
    }
    
    private void bajar(int i) {
        while (true) {
            int izq = 2 * i + 1;
            int der = 2 * i + 2;
            int menor = i;
            
            if (izq < size && heap[izq].compareTo(heap[menor]) < 0) {
                menor = izq;
            }
            if (der < size && heap[der].compareTo(heap[menor]) < 0) {
                menor = der;
            }
            if (menor != i) {
                intercambiar(i, menor);
                i = menor;
            } else {
                break;
            }
        }
    }
    
    private void intercambiar(int i, int j) {
        ParPuntajeId temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
        
        if (heap[i].id < handles.length) {
            handles[heap[i].id].posicion = i;
        }
        if (heap[j].id < handles.length) {
            handles[heap[j].id].posicion = j;
        }
    }
    
    int size() {
        return size;
    }
}