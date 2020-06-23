public class ValidatorDonator implements IValidator<Donator> {
    @Override
    public void validate(Donator entity) throws ValidationException {
        if (entity == null)
            throw new IllegalArgumentException("Entitatea nu poate fi null");

        String erori = "";
        if (entity.getId()<0)
            erori += "Id-ul nu poate fi un numar negativ";
        if (entity.getNume().equals(""))
            erori += "Numele nu poate fi vid";
        if (entity.getAdresa().equals(""))
            erori += "Adresa nu poate fi vid";
        if (entity.getNrTelefon()<0)
            erori += "Nr de telefon nu poate fi un numar mai mic ca si 0";

        if (erori.length()>0)
            throw new ValidationException(erori);
    }
}
