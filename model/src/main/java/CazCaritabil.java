import java.io.Serializable;

public class CazCaritabil implements HasId<Integer>, Serializable {
    private int id;
    private String denumire;
    private Double sumaTotala;

    public CazCaritabil(int id, String denumire, Double sumaTotala) {
        this.id = id;
        this.denumire = denumire;
        this.sumaTotala = sumaTotala;
    }

    public String getDenumire() {
        return denumire;
    }

    public void setDenumire(String denumire) {
        this.denumire = denumire;
    }

    public Double getSumaTotala() {
        return sumaTotala;
    }

    public void setSumaTotala(Double sumaTotala) {
        this.sumaTotala = sumaTotala;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer integer) {
        this.id=integer;
    }


    @Override
    public String toString() {
        return "CazCaritabil{" +
                "id=" + id +
                ", denumire='" + denumire + '\'' +
                ", sumaTotala=" + sumaTotala +
                '}';
    }
}
