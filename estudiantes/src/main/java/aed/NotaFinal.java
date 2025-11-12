package aed;

public class NotaFinal implements Comparable<NotaFinal> {
    public double _nota;
    public int _id;

    public NotaFinal(double nota, int id){
        _nota = nota;
        _id = id;
    }

    public int compareTo(NotaFinal otra){
        if (otra._id != this._id){
            return this._id - otra._id;
        }
        return Double.compare(this._nota, otra._nota);
    }

// agregar metodo equals
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false; 

        NotaFinal other = (NotaFinal) obj;
        return _nota == other._nota && _id == other._id;
    }

}