
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DonatieDBRepository implements IDonatieRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger= LogManager.getLogger();

    public DonatieDBRepository(Properties props){

        logger.info("Initializing Donatie JdbcRepo with properties: {} ",props);

        dbUtils=new JdbcUtils(props);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void save(Donatie entity) {
        logger.traceEntry("saving Donatie {} ",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into donatii values (?,?,?,?)")){
            preStmt.setInt(1,entity.getId());
            preStmt.setInt(2,entity.getDonatorId());
            preStmt.setInt(3,entity.getCazCaritabilId());
            preStmt.setDouble(4,entity.getSuma());

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
    public void update(Donatie entity) {

    }

    @Override
    public Donatie findOne(Integer integer) {
        return null;
    }

    @Override
    public Iterable<Donatie> findAll() {
        //logger.();
        Connection con=dbUtils.getConnection();
        List<Donatie> donatiiList=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from donatii")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    int idD = result.getInt("idD");
                    int idC = result.getInt("idC");
                    double suma = result.getDouble("suma");

                    Donatie donatie=new Donatie(id,idD,idC,suma);
                    donatiiList.add(donatie);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(donatiiList);
        return donatiiList;

    }
}
