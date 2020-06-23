import java.io.Serializable;

public class Donatie implements HasId<Integer>, Serializable {
    private Integer id;
    private Integer donatorId;
    private Integer cazCaritabilId;
    private Double suma;

    public Donatie(Integer id, Integer donatorId, Integer cazCaritabilId, Double suma) {
        this.id = id;
        this.donatorId = donatorId;
        this.cazCaritabilId = cazCaritabilId;
        this.suma = suma;
    }


    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer integer) {
        this.id=integer;
    }

    public Integer getDonatorId() {
        return donatorId;
    }

    public void setDonatorId(Integer donatorId) {
        this.donatorId = donatorId;
    }

    public Integer getCazCaritabilId() {
        return cazCaritabilId;
    }

    public void setCazCaritabilId(Integer cazCaritabilId) {
        this.cazCaritabilId = cazCaritabilId;
    }

    public Double getSuma() {
        return suma;
    }

    public void setSuma(Double suma) {
        this.suma = suma;
    }

    @Override
    public String toString() {
        return "Donatie{" +
                "id=" + id +
                ", donatorId=" + donatorId +
                ", cazCaritabilId=" + cazCaritabilId +
                ", suma=" + suma +
                '}';
    }
}
