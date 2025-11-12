package aed;

class MinHeap {
    private ParPuntajeId[] heap; // MinHeap de ParPuntajeId ordenados por Puntaje. El puntaje es la parte entera del porcentaje de respuestas correctas
    private Handle[] handles; // Array de handles (nuestro heap) ordenado por IDs, es un array de posiciones 
    private int size; // Tamaño del heap
    private int capacidad; // Capacidad máxima del heap
    // charly: para mi size y capacidad es el mismo valor 

    MinHeap(int cap) {
        capacidad = cap;
        heap = new ParPuntajeId[capacidad];
        handles = new Handle[cap];
        size = 0;
    }
    
    Handle insertar(double puntaje, int id) {
        if (size >= capacidad) {
            // Redimensionar heap
            int nuevaCapacidad = capacidad * 2;
            ParPuntajeId[] nuevoHeap = new ParPuntajeId[nuevaCapacidad];
            for (int i = 0; i < size; i++) {
                nuevoHeap[i] = heap[i];
            }
            heap = nuevoHeap;
            capacidad = nuevaCapacidad;
        }

        // CORRECCIÓN: Redimensionar handles si es necesario
        if (id >= handles.length) {
            int nuevaLongitud = Math.max(handles.length * 2, id + 1);
            Handle[] nuevoHandles = new Handle[nuevaLongitud];
            for (int i = 0; i < handles.length; i++) {
                nuevoHandles[i] = handles[i];
            }
            handles = nuevoHandles;
        }

        ParPuntajeId elem = new ParPuntajeId(puntaje, id);
        heap[size] = elem;
        Handle h = new Handle(size);
        handles[id] = h;  // CORRECCIÓN: Asignar el handle

        subir(size);
        size++;
        return h;
    }
    
    ParPuntajeId extraerMin() {
        if (size == 0) return null;
        
        ParPuntajeId min = heap[0];
        
        // CORRECCIÓN: Limpiar el handle del elemento extraído
        if (min.id < handles.length) {
            handles[min.id] = null;
        }

        size--;
        if (size > 0) {
            heap[0] = heap[size];
            heap[size] = null; // Limpiar referencia

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
        
        // CORRECCIÓN: Verificar que la posición sea válida
        if (pos < 0 || pos >= size) return;
        
        // CORRECCIÓN: Verificar que el ID coincida
        if (heap[pos].id != id) return;
        
        double oldPuntaje = heap[pos].puntaje;
        heap[pos].puntaje = nuevoPuntaje;
        
        if (nuevoPuntaje < oldPuntaje) {
            subir(pos);
        } else if (nuevoPuntaje > oldPuntaje) {
            bajar(pos);
        }
        // Si son iguales, no hacer nada
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