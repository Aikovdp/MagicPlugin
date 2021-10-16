package com.elmakers.mine.bukkit.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.bukkit.configuration.ConfigurationSection;

import com.elmakers.mine.bukkit.api.magic.MageController;

public class MySQLMageDataStore extends SQLMageDataStore {
    private String connectionString;
    private String user;
    private String password;

    @Override
    public void initialize(MageController controller, ConfigurationSection configuration) {
        super.initialize(controller, configuration);
        String host = configuration.getString("host", "localhost");
        int port = configuration.getInt("port", 3306);
        String database = configuration.getString("database", "magic");
        user = configuration.getString("user");
        password = configuration.getString("password");
        connectionString = "jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=false&useUnicode=true&characterEncoding=utf-8&autoReconnect=true";
        controller.info("Using mysql connection for player data: " + connectionString, 10);

        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            controller.getLogger().severe("MySQL library not found! " + e.getMessage());
        }
    }

    @Override
    public String getTextFieldType() {
        return "LONGTEXT";
    }

    @Override
    public String getTableEncoding() {
        return "charset = utf8mb4;";
    }

    @Override
    protected Connection createConnection() throws SQLException {
        return DriverManager.getConnection(connectionString, user, password);
    }
}
