package aed;

class MinHeap {
    private ParPuntajeId[] heap; // MinHeap de ParPuntajeId ordenados por Puntaje
    private Handle[] handles; // Array de handles (nuestro heap) ordenado por IDs
    private int size; // Tamaño del heap
    private int capacidad; // Capacidad máxima del heap
    
    MinHeap(int cap) {
        capacidad = cap;
        heap = new ParPuntajeId[capacidad];
        handles = new Handle[cap];
        size = 0;
    }
    
    Handle insertar(double puntaje, int id) {
        if (size >= capacidad) {
            int nuevaCapacidad = capacidad * 2; // Al hacer capacidad * 2 nos ahorramos hacerlo cada vez que agregamos algo

            ParPuntajeId[] nuevoHeap = new ParPuntajeId[nuevaCapacidad];
            Handle[] nuevosHandles = new Handle[nuevaCapacidad];

            // Copiar elementos de heap[]
            for (int i = 0; i < size; i++) {
                nuevoHeap[i] = heap[i];
            }

            // Copiar elementos de handles[]
            for (int i = 0; i < handles.length; i++) {
                nuevosHandles[i] = handles[i];
            }

            heap = nuevoHeap;
            handles = nuevosHandles;
            capacidad = nuevaCapacidad;
        }
        
        ParPuntajeId elem = new ParPuntajeId(puntaje, id);
        heap[size] = elem; // Agregamos el elemento nuevo al final
        Handle h = new Handle(size);

        // No me queda claro esto, que pasa si nuestro id = 100, necesitamos handles.length = 101.
        // No se estaria guardando esto en todo caso, redimensionamos a parte del heap?

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