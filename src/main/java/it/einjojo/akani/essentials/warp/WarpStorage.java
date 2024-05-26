package it.einjojo.akani.essentials.warp;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.einjojo.akani.core.api.network.NetworkLocation;
import it.einjojo.akani.core.api.util.HikariDataSourceProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public record WarpStorage(HikariDataSourceProxy akaniDataSource, Gson gson) {
    private static final String TABLE_NAME = "essentials_warps";
    private static final Logger log = LoggerFactory.getLogger(WarpStorage.class);

    public void init() {
        try (var conn = akaniDataSource.getConnection()) {
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS %s (name VARCHAR(100), icon JSON, location JSON, PRIMARY KEY (name), CHECK(JSON_VALID(icon)), CHECK(JSON_VALID(location)));".formatted(TABLE_NAME));
        } catch (SQLException e) {
            log.error("Error creating warps table", e);
        }
    }

    public List<Warp> loadWarps() {
        try (var conn = akaniDataSource.getConnection(); var ps = conn.prepareStatement("SELECT * FROM %s".formatted(TABLE_NAME))) {
            var rs = ps.executeQuery();
            var warps = new ArrayList<Warp>();
            while (rs.next()) {
                var warp = parseWarpFromResultSet(rs);
                if (warp != null) warps.add(warp);
            }
            log.debug("Loaded {} warps from database.", warps.size());
            rs.close();
            return warps;
        } catch (SQLException e) {
            log.error("Error loading warps", e);
            return List.of();
        }
    }


    public Warp parseWarpFromResultSet(ResultSet rs) {
        try {
            JsonObject iconJson = gson.fromJson(rs.getString("icon"), JsonObject.class);
            NetworkLocation loc = gson.fromJson(rs.getString("location"), NetworkLocation.class);
            return new Warp(rs.getString("name"), WarpIcon.fromJsonObject(iconJson), loc);
        } catch (Exception e) {
            log.warn("Error parsing warp from result set", e);
            return null;
        }

    }

    public boolean createWarp(Warp warp) {
        try (var conn = akaniDataSource.getConnection();
             var ps = conn.prepareStatement("INSERT INTO %s (name, icon, location) VALUES (?, ?, ?)".formatted(TABLE_NAME))) {
            ps.setString(1, warp.name());
            ps.setString(2, gson.toJson(warp.icon().toJsonObject()));
            ps.setString(3, gson.toJson(warp.networkLocation()));
            ps.execute();
            return true;

        } catch (SQLException e) {
            log.error("Error creating warp", e);
            return false;
        }
    }

    public void deleteWarp(String warpName) {
        try (var conn = akaniDataSource.getConnection()) {
            try (var ps = conn.prepareStatement("DELETE FROM %s WHERE name = ?".formatted(TABLE_NAME))) {
                ps.setString(1, warpName);
                ps.execute();
            }
        } catch (SQLException e) {
            log.error("Error deleting warp", e);
        }
    }

    public void updateWarp(Warp warp) {
        try (var conn = akaniDataSource.getConnection()) {
            try (var ps = conn.prepareStatement("UPDATE %s SET icon = ?, location = ? WHERE name = ?".formatted(TABLE_NAME))) {
                ps.setString(1, gson.toJson(warp.icon().toJsonObject()));
                ps.setString(2, gson.toJson(warp.networkLocation()));
                ps.setString(3, warp.name());
                ps.execute();
            }
        } catch (SQLException e) {
            log.error("Error updating warp", e);
        }
    }
}
