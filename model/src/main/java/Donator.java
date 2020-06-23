import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name="donatori")
public class Donator implements HasId<Integer>, Serializable {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Integer id;

    @Column(name="nume")
    private String nume;

    @Column(name="adresa")
    private String adresa;

    @Column(name = "nrTelefon")
    private Long nrTelefon;

    public Donator(Integer id, String nume, String adresa, Long nrTelefon) {
        this.id = id;
        this.nume = nume;
        this.adresa = adresa;
        this.nrTelefon = nrTelefon;
    }

    public Donator() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer integer) {
        this.id=integer;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getAdresa() {
        return adresa;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public Long getNrTelefon() {
        return nrTelefon;
    }

    public void setNrTelefon(Long nrTelefon) {
        this.nrTelefon = nrTelefon;
    }

    @Override
    public String toString() {
        return "Donator{" +
                "id=" + id +
                ", nume='" + nume + '\'' +
                ", adresa='" + adresa + '\'' +
                ", nrTelefon=" + nrTelefon +
                '}';
    }
}
