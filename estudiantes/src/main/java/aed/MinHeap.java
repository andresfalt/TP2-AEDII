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
        // charly: tu minheap arranca en 0
        size = 0;
    }
    
    Handle insertar(double puntaje, int id) {
        if (size >= capacidad) {
            // charly: seria si nadie se presenta? la unica manera que sea cero, es mejor > ?
            int nuevaCapacidad = capacidad * 2; // Al hacer capacidad * 2 nos ahorramos hacerlo cada vez que agregamos algo

            ParPuntajeId[] nuevoHeap = new ParPuntajeId[nuevaCapacidad];

            // Copiar elementos de heap[]
            for (int i = 0; i < size; i++) {
                nuevoHeap[i] = heap[i];
            }

            heap = nuevoHeap;
            capacidad = nuevaCapacidad;
        }

        // Hasta aca es O(n) si se redimensiona, O(1) cuando no
        
        ParPuntajeId elem = new ParPuntajeId(puntaje, id);
        heap[size] = elem; // Agregamos el elemento nuevo al final
        // charly : creo que se agrega al principio porque size arranca en 0 
        Handle h = new Handle(size);

        if (id >= handles.length) {
            // charly: handles.legth te retorna el tamaño de estudiantes que seria lo que nos dan de dato?
            // charly: pero id solo va a ser =< a handles.length?
            // La razon de usar .max es para ahorrarnos futuros redimensionamientos
            // Si el ID es cercano a la longitud actual, agrandamos el doble para no tener que hacerlo de nuevo pronto
            // Si el ID es un numero muy grande, lo redimensionamos para que entre justo y evitar agrandar demasiado
            int nuevaLongitud = Math.max(handles.length * 2, id + 1);
            Handle[] nuevoHandles = new Handle[nuevaLongitud];

            for (int i = 0; i < handles.length; i++) {
                nuevoHandles[i] = handles[i];
            }

            handles = nuevoHandles;
        }

        // Esto me queda O(m) si se redimensiona, si no O(1)
        // m siendo handles.length, que alternativa hay?

        handles[id] = h;

        subir(size);
        size++; // aca incrementa el tamaño size 
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
        // este es el principal para ir actualizando el heap a medida que se copia el alumno y cambia su puntaje
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