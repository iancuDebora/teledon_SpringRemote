import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.List;

public class DonatorHibernateRepository implements IDonatorRepository {

    private static SessionFactory sessionFactory;

    static void initialize() {

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.out.println("Exceptie "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close() {
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }
    }

    public DonatorHibernateRepository() {

    }

    @Override
    public Iterable<Donator> findAll()
    {
        initialize();
        Iterable<Donator> result= null;
        try(Session session = sessionFactory.openSession()){
            session.beginTransaction();
            result = session.createQuery(" from Donator").list();
            for (Donator event : (List<Donator>) result) {
                System.out.println("Donator (" + event.getId() + ") : " + event.getNume());
            }
            session.getTransaction().commit();
        }
        close();
        return result;

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void save(Donator entity) {
        initialize();
        try(Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(entity);
            session.getTransaction().commit();
        }
        close();
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Donator entity) {

    }

    @Override
    public Donator findOne(Integer integer) {
        return null;
    }


}
