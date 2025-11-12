package aed;
/* El handle siempre tendrá la posición correcta del elemento en el heap después de cualquier operación. */
class Handle {
    int posicion;
    
    Handle(int pos) {
        posicion = pos;
    }
}