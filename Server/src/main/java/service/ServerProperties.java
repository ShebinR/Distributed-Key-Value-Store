package service;

public class ServerProperties {
    private int port;
    private String serverId;
    private String host;
    private boolean isMaster;
    private boolean isAlive;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServerId() {
        return serverId;
    }

    public void setServerId(String serverId) {
        this.serverId = serverId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean getIsMaster() {
        return isMaster;
    }

    public void setIsMaster(boolean master) {
        isMaster = master;
    }

    public ServerProperties(){}

    public ServerProperties(String serverId, String host, int port, boolean isMaster){
        this.port = port;
        this.host = host;
        this.serverId = serverId;
        this.isMaster = isMaster;
        this.isAlive = true;
    }

    public void printServerProperties(){
        System.out.println( this.getServerId() + "\t" + this.getHost() + "\t"
                + this.getPort() + "\t " + this.getIsMaster());
    }
}
