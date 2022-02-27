package factuyaenvio.herramientas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.commons.dbutils.DbUtils;

public class clsConexion {

    private static Connection varConexion;

    public static Connection obtenerConexion() {
        try {
            InitialContext initialContext = new InitialContext();
            Context context = (Context) initialContext.lookup("java:comp/env");
            //The JDBC Data source that we just created
            //  DataSource ds = (DataSource) context.lookup("jdbc/pgpool_comet");
            DataSource ds = (DataSource) context.lookup("jdbc/pgpool_factuya");
            varConexion = ds.getConnection();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            return varConexion;
        }
    }

    public static Connection obtenerConexion(String url, String usuario, String contraseña) {
        try {

            try {
                Class.forName("org.postgresql.Driver");
            } catch (ClassNotFoundException ex) {
                System.out.println("Error al registrar el driver de PostgreSQL: " + ex);
            }

            varConexion = DriverManager.getConnection(
                    //   "jdbc:postgresql://" + host + ":" + puerto + "/" + baseDatos + "",
                    url,
                    usuario,
                    contraseña);
        } catch (SQLException ex) {
            Logger.getLogger(clsConexion.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(clsConexion.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            return varConexion;
        }
    }

    public static Connection obtenerConexionExterna() {
        try {
            InitialContext initialContext = new InitialContext();
            Context context = (Context) initialContext.lookup("java:comp/env");
            //The JDBC Data source that we just created
            //  DataSource ds = (DataSource) context.lookup("jdbc/pgpool_comet");
            DataSource ds = (DataSource) context.lookup("jdbc/pgpool_factuya_servidor_externo");
            varConexion = ds.getConnection();

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            return varConexion;
        }
    }

    public static void cerrarConexion() {
        try {
            DbUtils.closeQuietly(varConexion);
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("error cerrando conexion");
        }
    }

    public static void cerrarConexion(Connection varConexionNew) {
        try {
            DbUtils.closeQuietly(varConexionNew);
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("error cerrando conexion");
        }
    }

    public static void inicarTransaccion(Connection varConexion) {
        if (varConexion != null) {
            try {
                if (varConexion.getAutoCommit()) {
                    varConexion.setAutoCommit(false);
                }
            } catch (SQLException ex) {
                String.valueOf(ex.getErrorCode());
                ex.printStackTrace();
            }
        }
    }

    public static void deshacerTransaccion(Connection varConexion) {
        if (varConexion != null) {
            try {
                varConexion.rollback();
                varConexion.setAutoCommit(true);
            } catch (SQLException ex) {
                String.valueOf(ex.getErrorCode());
                ex.printStackTrace();
            }
        }
    }

    public static void finalizarTransaccion(Connection varConexion) {
        if (varConexion != null) {
            try {
                varConexion.commit();
                varConexion.setAutoCommit(true);
            } catch (SQLException ex) {
                String.valueOf(ex.getErrorCode());
                ex.printStackTrace();
            }
        }
    }

    public static void inicarTransaccion() {
        if (varConexion != null) {
            try {
                if (varConexion.getAutoCommit()) {
                    varConexion.setAutoCommit(false);
                }
            } catch (SQLException ex) {
                String.valueOf(ex.getErrorCode());
                ex.printStackTrace();
            }
        }
    }

    public static void deshacerTransaccion() {
        if (varConexion != null) {
            try {
                varConexion.rollback();
                varConexion.setAutoCommit(true);
            } catch (SQLException ex) {
                String.valueOf(ex.getErrorCode());
                ex.printStackTrace();
            }
        }
    }

    public static void finalizarTransaccion() {
        if (varConexion != null) {
            try {
                varConexion.commit();
                varConexion.setAutoCommit(true);
            } catch (SQLException ex) {
                String.valueOf(ex.getErrorCode());
                ex.printStackTrace();
            }
        }
    }

}
