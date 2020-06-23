
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DonatorDBRepository implements IDonatorRepository {
    private JdbcUtils dbUtils;
    private ValidatorDonator validatorDonator;

    private static final Logger logger= LogManager.getLogger();

    public DonatorDBRepository(Properties props, ValidatorDonator validatorDonator){

        logger.info("Initializing Donator JdbcRepo with properties: {} ",props);

        dbUtils=new JdbcUtils(props);
        this.validatorDonator = validatorDonator;
    }
    @Override
    public int size() {
        return 0;
    }

    @Override
    public void save(Donator entity) {
        logger.traceEntry("saving Donator {} ",entity);
        validatorDonator.validate(entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into donatori values (?,?,?,?)")){
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getNume());
            preStmt.setString(3,entity.getAdresa());
            preStmt.setLong(4,entity.getNrTelefon());

            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void update(Donator entity) {

    }

    @Override
    public Donator findOne(Integer integer) {
        logger.traceEntry("finding Donator with id {} ",integer);
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from donatori where id=?")){
            preStmt.setInt(1,integer);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    String adresa = result.getString("adresa");
                    Long nrTel=result.getLong("nrTelefon");

                    Donator donator = new Donator(id, nume,adresa,nrTel);
                    logger.traceExit(donator);
                    return donator;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No Donator found with id {}", integer);

        return null;
    }

    @Override
    public Iterable<Donator> findAll() {
        Connection con=dbUtils.getConnection();
        List<Donator> donatoriList=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from donatori")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    String adresa = result.getString("adresa");
                    Long nrTelefon = result.getLong("nrTelefon");

                    Donator donator = new Donator(id,nume,adresa,nrTelefon);
                    donatoriList.add(donator);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(donatoriList);
        return donatoriList;

    }
}
