
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class VoluntarDBRepository implements IVoluntarRepository {
    private JdbcUtils dbUtils;
    private ValidatorVoluntar validatorVoluntar;

    private static final Logger logger= LogManager.getLogger();

    public VoluntarDBRepository(Properties props, ValidatorVoluntar validatorVoluntar){

        logger.info("Initializing Voluntar JdbcRepo with properties: {} ",props);

        dbUtils=new JdbcUtils(props);
        this.validatorVoluntar = validatorVoluntar;
    }

    public VoluntarDBRepository(JdbcUtils dbUtils, ValidatorVoluntar validatorVoluntar) {
        this.dbUtils = dbUtils;
        this.validatorVoluntar = validatorVoluntar;
    }

    @Override
    public int size() {
        logger.traceEntry("calculating size");
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("select count(*) as [SIZE] from voluntari")) {
            try(ResultSet result = preStmt.executeQuery()) {
                if (result.next()) {
                    logger.traceExit(result.getInt("SIZE"));
                    return result.getInt("SIZE");
                }
            }
        }catch(SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        return 0;
    }

    @Override
    public void save(Voluntar entity) {
        logger.traceEntry("saving Voluntar {} ",entity);
        validatorVoluntar.validate(entity);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("insert into voluntari values (?,?,?,?)")){
            preStmt.setInt(1,entity.getId());
            preStmt.setString(2,entity.getNume());
            preStmt.setString(3,entity.getEmail());
            preStmt.setString(4,entity.getParola());

            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();

    }

    @Override
    public void delete(Integer integer) {
        logger.traceEntry("deleting Voluntar with {}",integer);
        Connection con=dbUtils.getConnection();
        try(PreparedStatement preStmt=con.prepareStatement("delete from voluntari where id=?")){
            preStmt.setInt(1,integer);
            int result=preStmt.executeUpdate();
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit();
    }

    @Override
    public void update(Voluntar entity) {
        logger.traceEntry("updating Person with {}",entity.getId());
        Connection con=dbUtils.getConnection();
        if(this.findOne(entity.getId()) == null){
            logger.info("Entity with given id not in repo... {}", entity.getId());
            return;
        }
        try(PreparedStatement preStmt=con.prepareStatement("update voluntari set nume=?,email=?,parola=?"+" where id=?")){
            preStmt.setString(1, entity.getNume());
            preStmt.setString(2,entity.getEmail());
            preStmt.setString(3, entity.getParola());
            preStmt.setInt(4, entity.getId());


            int ret = preStmt.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error in update Voluntar {}",e);
            e.printStackTrace();
        }

        logger.traceExit("Successfully updated.");
    }

    @Override
    public Voluntar findOne(Integer integer) {
        logger.traceEntry("finding Voluntar with id {} ",integer);
        Connection con=dbUtils.getConnection();

        try(PreparedStatement preStmt=con.prepareStatement("select * from voluntari where id=?")){
            preStmt.setInt(1,integer);
            try(ResultSet result=preStmt.executeQuery()) {
                if (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    String email = result.getString("email");
                    String parola = result.getString("parola");

                    Voluntar voluntar = new Voluntar(id, nume,email,parola);
                    logger.traceExit(voluntar);
                    return voluntar;
                }
            }
        }catch (SQLException ex){
            logger.error(ex);
            System.out.println("Error DB "+ex);
        }
        logger.traceExit("No Voluntar found with id {}", integer);

        return null;
    }

    @Override
    public Iterable<Voluntar> findAll() {
        //logger.();
        Connection con=dbUtils.getConnection();
        List<Voluntar> voluntarList=new ArrayList<>();
        try(PreparedStatement preStmt=con.prepareStatement("select * from voluntari")) {
            try(ResultSet result=preStmt.executeQuery()) {
                while (result.next()) {
                    int id = result.getInt("id");
                    String nume = result.getString("nume");
                    String email = result.getString("email");
                    String parola = result.getString("parola");
                    Voluntar voluntar = new Voluntar(id, nume,email,parola);
                    voluntarList.add(voluntar);
                }
            }
        } catch (SQLException e) {
            logger.error(e);
            System.out.println("Error DB "+e);
        }
        logger.traceExit(voluntarList);
        return voluntarList;
    }
}
