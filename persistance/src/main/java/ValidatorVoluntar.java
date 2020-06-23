public class ValidatorVoluntar implements IValidator<Voluntar> {
    @Override
    public void validate(Voluntar entity) throws ValidationException {
        if (entity == null)
            throw new IllegalArgumentException("Entitatea nu poate fi null");

        String erori = "";
        if (entity.getId()<0)
            erori += "Id-ul nu poate fi un numar negativ";
        if (entity.getNume().equals(""))
            erori += "Numele nu poate fi vid";
        if (entity.getEmail().equals(""))
            erori += "Emailul nu poate fi vid";
        if (entity.getParola().equals(""))
            erori += "Parola nu poate fi vid";

        if (erori.length()>0)
            throw new ValidationException(erori);

    }
}
