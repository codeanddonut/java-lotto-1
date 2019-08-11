package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String server = "localhost";
    private static final String database = "woowa";
    private static final String userName = "donut";
    private static final String password = "qwer1234";

    private static DBConnection instance = null;

    private Connection con = null;

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    private DBConnection() {}

    public Connection connect() {
        if (this.con == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                this.con = DriverManager.getConnection(
                        "jdbc:mysql://" + server + "/" + database +
                        "?serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false",
                        userName,
                        password
                );
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return con;
    }

    public void close() {
        if (this.con != null) {
            try {
                this.con.close();
                this.con = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}