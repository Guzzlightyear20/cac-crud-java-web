package cac.crud22033.modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Charly Cimino Aprendé más Java en mi canal:
 * https://www.youtube.com/c/CharlyCimino Encontrá más código en mi repo de
 * GitHub: https://github.com/CharlyCimino
 */
public class ModeloMySQL implements Modelo {

    private static final String GET_ALL_QUERY = "SELECT * FROM alumnos";
    private static final String GET_BY_ID_QUERY = "SELECT * FROM alumnos WHERE id_alumno = ?";
    private static final String ADD_QUERY = "INSERT INTO alumnos VALUES (null, ?, ?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE alumnos SET nombre = ?, apellido = ?, fechaNac = ?, mail = ?, fotoBase64 = ? WHERE id_alumno = ?";
    private static final String DELETE_QUERY = "DELETE FROM alumnos WHERE id_alumno = ?";

    @Override
    public List<Alumno> getAlumnos() {
        List<Alumno> alumnos = new ArrayList<>();
        try ( Connection con = Conexion.getConnection();  PreparedStatement ps = con.prepareStatement(GET_ALL_QUERY);  ResultSet rs = ps.executeQuery();) {
            while (rs.next()) {
                alumnos.add(rsToAlumno(rs));
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener alumnos", ex);
        }
        return alumnos;
    }

    @Override
    public Alumno getAlumno(int id) {
        Alumno alu = null;
        try ( Connection con = Conexion.getConnection();  PreparedStatement ps = con.prepareStatement(GET_BY_ID_QUERY);) {
            ps.setInt(1, id);
            try ( ResultSet rs = ps.executeQuery();) {
                rs.next();
                alu = rsToAlumno(rs);
            }
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener alumnos", ex);
        }
        return alu;
    }

    @Override
    public int addAlumno(Alumno alumno) {
        int regsAgregados = 0;
        try ( Connection con = Conexion.getConnection();  PreparedStatement ps = con.prepareStatement(ADD_QUERY);) {
            fillPreparedStatement(ps, alumno);
            regsAgregados = ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener alumnos", ex);
        }
        return regsAgregados;
    }

    @Override
    public int updateAlumno(Alumno alumno) {
        int regsModificados = 0;
        try ( Connection con = Conexion.getConnection();  PreparedStatement ps = con.prepareStatement(UPDATE_QUERY);) {
            fillPreparedStatement(ps, alumno);
            ps.setInt(6, alumno.getId());
            regsModificados = ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener alumnos", ex);
        }
        return regsModificados;
    }

    @Override
    public int removeAlumno(int id) {
        int regsBorrados = 0;
        try ( Connection con = Conexion.getConnection();  PreparedStatement ps = con.prepareStatement(DELETE_QUERY);) {
            ps.setInt(1, id);
            regsBorrados = ps.executeUpdate();
        } catch (SQLException ex) {
            throw new RuntimeException("Error de SQL", ex);
        } catch (Exception ex) {
            throw new RuntimeException("Error al obtener alumnos", ex);
        }
        return regsBorrados;
    }

    private void fillPreparedStatement(PreparedStatement ps, Alumno alumno) throws SQLException {
        ps.setString(1, alumno.getNombre());
        ps.setString(2, alumno.getApellido());
        ps.setString(3, alumno.getFechaNacimiento());
        ps.setString(4, alumno.getMail());
        ps.setString(5, alumno.getFoto());
    }

    private Alumno rsToAlumno(ResultSet rs) throws SQLException {
        int id = rs.getInt(1);
        String nombre = rs.getString(2);
        String apellido = rs.getString(3);
        String fechaNacimiento = rs.getString(4);
        String mail = rs.getString(5);
        String foto = rs.getString(6);
        return new Alumno(id, nombre, apellido, mail, fechaNacimiento, foto);
    }
}
