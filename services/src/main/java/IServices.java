
public interface IServices {
     void login(Voluntar user, IObserver client) throws MyException;
     void logout(Voluntar voluntar, IObserver client) throws MyException;
     CazCaritabil[] getCazuri() throws MyException;
     Donator[] getDonatori() throws MyException;
     Donatie[] getDonatii() throws MyException;
     void addDonator(Donator donator) throws MyException;
     void addDonatie(Donatie donatie) throws MyException;
     void updateCaz(CazCaritabil caz) throws MyException;

}

