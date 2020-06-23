public class ValidatorDonatie implements IValidator<Donatie> {
    @Override
    public void validate(Donatie entity) throws ValidationException {
        if (entity == null)
            throw new IllegalArgumentException("Entitatea nu poate fi null");

        String erori = "";
        if (entity.getId()<0)
            erori += "|| Id-ul nu poate fi un numar negativ ||";
        if (entity.getDonatorId()<0)
            erori += "|| Id-ul Donatorului nu poate fi mai mic ca 0 ||";
        if (entity.getCazCaritabilId()<0)
            erori += "|| Id-ul CazuluiCaritabil nu poate fi mai mic ca 0 ||";
        if (entity.getSuma()<0)
            erori += "|| Suma nu poate fi un numar mai mic ca si 0 ||";

        if (erori.length()>0)
            throw new ValidationException(erori);
    }
}
