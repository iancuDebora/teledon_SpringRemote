
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CazCaritabilDBRepository implements ICazCaritabilRepository {
    private JdbcUtils dbUtils;

    private static final Logger logger = LogManager.getLogger();

    public CazCaritabilDBRepository(Properties props){

        logger.info("Initializing CazCaritabil JdbcRepo with properties: {} ",props);

        dbUtils=new JdbcUtils(props);
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public void save(CazCaritabil entity) {
        logger.traceEntry("saving CazCaritabil {} ",entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into cazuriCaritabile values (?,?,?)")){
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getDenumire());
            preStmt.setDouble(3,entity.getSumaTotala());

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
    public void update(CazCaritabil entity) {
        logger.traceEntry("updating CazCaritabil with {}",entity.getId());
        Connection con=dbUtils.getConnection();
        if(this.findOne(entity.getId()) == null){
            logger.info("Entity with given id not in repo... {}", entity.getId());
            return;
        }
        try(PreparedStatement preStmt=con.prepareStatement("update cazuriCaritabile set denumire=?,sumaTotala=?"+" where id=?")){
            preStmt.setString(1, entity.getDenumire());
            preStmt.setString(2,entity.getSumaTotala().toString());
            preStmt.setInt(3, entity.getId());

            int ret = preStmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error in update CazCaritabil {}",e);
            e.printStackTrace();
        }

        logger.traceExit("Successfully updated.");
    }

    @Override
    public CazCaritabil findOne(Integer integer) {
        logger.traceEntry("finding CazCaritabil with id {} ",integer);
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from cazuriCaritabile where id=?")){
            preStmt.setInt(1,integer);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String denumire = result.getString("denumire");
                    Double sumaTotala = result.getDouble("sumaTotala");

                    CazCaritabil caz = new CazCaritabil(id,denumire,sumaTotala);
                    logger.traceExit(caz);
                    return caz;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No CazCaritabil found with id {}", integer);

        return null;
    }

    @Override
    public Iterable<CazCaritabil> findAll() {
        Connection con=dbUtils.getConnection();
        List<CazCaritabil> cazCaritabilList=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from cazuriCaritabile")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String denumire = result.getString("denumire");
                    double suma = result.getDouble("sumaTotala");

                   CazCaritabil cazCaritabil=new CazCaritabil(id,denumire,suma);
                    cazCaritabilList.add(cazCaritabil);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(cazCaritabilList);
        return cazCaritabilList;
    }
}
