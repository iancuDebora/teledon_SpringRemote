import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServicesImplementation2 implements IServices {

    private VoluntarDBRepository volRepo;
    private CazCaritabilDBRepository cazRepo;
    private DonatorDBRepository donatorRepo;
    private DonatieDBRepository donatieRepo;
    private Map<Integer, IObserver> loggedClients;

    public ServicesImplementation2(VoluntarDBRepository volRepo, CazCaritabilDBRepository cazRepo,
                                   DonatorDBRepository donatorRepo, DonatieDBRepository donatieRepo) {

        this.volRepo = volRepo;
        this.cazRepo = cazRepo;
        this.donatorRepo = donatorRepo;
        this.donatieRepo = donatieRepo;
        loggedClients=new ConcurrentHashMap<>();;
    }


    public synchronized void login(Voluntar voluntar, IObserver client) throws MyException {
        Voluntar userR=volRepo.findOne(voluntar.getId());
        if (userR!=null){
            if(loggedClients.get(voluntar.getId())!=null)
                throw new MyException("Voluntarul este deja logat!");
            loggedClients.put(voluntar.getId(), client);

        }else
            throw new MyException("Autentificarea a esuat!");
    }


    private final int defaultThreadsNo=5;

    public void addDonator(Donator donator)
    {
        Iterable<Voluntar> voluntari = volRepo.findAll();
        donatorRepo.save(donator);
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        for (Voluntar vol :voluntari)
        {
            IObserver client=loggedClients.get(vol.getId());
            if (client!=null)
                executor.execute(() -> {

                    try {
                        client.donatorSaved(donator);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
        }

        executor.shutdown();
    }

    public void updateCaz(CazCaritabil nou)
    {
        Iterable<Voluntar> voluntari = volRepo.findAll();
        ExecutorService executor= Executors.newFixedThreadPool(defaultThreadsNo);
        cazRepo.update(nou);
        for (Voluntar vol :voluntari)
        {
            IObserver client=loggedClients.get(vol.getId());
            if (client!=null)
                executor.execute(() -> {
                    try {
                        client.cazUpdated(nou);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
        }

        executor.shutdown();
    }

    public void addDonatie(Donatie donatie)
    {
        donatieRepo.save(donatie);

    }


    public synchronized void logout(Voluntar voluntar, IObserver client) throws MyException {
        IObserver localClient=loggedClients.remove(voluntar.getId());
        if (localClient==null)
            throw new MyException("Voluntarul "+voluntar.getId()+" nu este logat.");
    }
   public synchronized CazCaritabil[] getCazuri()
   {
       List<CazCaritabil> rez = new ArrayList<>();
       for (CazCaritabil caz : cazRepo.findAll())
           rez.add(caz);
       return rez.toArray(new CazCaritabil[rez.size()]);
   }

    public synchronized Donator[] getDonatori()
    {
        List<Donator> rez = new ArrayList<>();
        for (Donator donator : donatorRepo.findAll())
            rez.add(donator);
        return rez.toArray(new Donator[rez.size()]);
    }

    public synchronized Donatie[] getDonatii()
    {
        List<Donatie> rez = new ArrayList<>();
        for (Donatie donatie : donatieRepo.findAll())
            rez.add(donatie);
        return rez.toArray(new Donatie[rez.size()]);
    }
}

