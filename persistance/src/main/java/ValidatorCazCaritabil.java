public class ValidatorCazCaritabil implements IValidator<CazCaritabil> {
    @Override
    public void validate(CazCaritabil entity) throws ValidationException {
        if (entity == null)
            throw new IllegalArgumentException("Entitatea nu poate fi null");

        String erori = "";
        if (entity.getId()<0)
            erori += "Id-ul nu poate fi un numar negativ";
        if (entity.getDenumire().equals(""))
            erori += "Numele nu poate fi vid";
        if (entity.getSumaTotala()<0)
            erori += "Suma totala nu poate fi un numar mai mic ca si 0";

        if (erori.length()>0)
            throw new ValidationException(erori);
    }
}
