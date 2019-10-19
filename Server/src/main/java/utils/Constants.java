package utils;

public class Constants {

    public static final String TABLE_NAME = "KeyValueSnapshot";
    public static final String DB_NAME_PREFIX = "KVStoreWithTS-";
    public static final String JDBC_CONN_PREFIX = "jdbc:sqlite:" +
            "./db/";
    public static final String DB_EXTENSION = ".db";
    public static final String CONFIG_FILE = "ServerConfig.csv";
    public static final int OPERATION_SUCCESS_STATUS_CODE = 0;
    public static final int OPERATION_FAILURE_STATUS_CODE = -1;
    public static final int SERVER_UNAVAILABLE_STATUS_CODE = -2;

}
