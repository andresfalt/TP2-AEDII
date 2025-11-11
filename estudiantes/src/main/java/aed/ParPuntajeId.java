package aed;

class ParPuntajeId implements Comparable<ParPuntajeId> {
    double puntaje;
    int id;
    
    ParPuntajeId(double p, int i) {
        // la nota y el id estudiante
        puntaje = p;
        id = i;
    }
    
    @Override
    public int compareTo(ParPuntajeId otro) {
        
        if (this.puntaje != otro.puntaje) {
            return Double.compare(this.puntaje, otro.puntaje);
        }

        return Integer.compare(this.id, otro.id);
    }
}