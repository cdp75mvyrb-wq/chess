package dataaccess.sql;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MySqlDataAccessTest {
    @Test
    void constructorCreatesTables() {
        assertDoesNotThrow(() -> new MySqlDataAccess());
    }
}