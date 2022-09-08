package net.defade.bismuth.core.utils;

public class NetworkInfos {
    private final String mongoHost;
    private final int mongoPort;
    private final String mongoUsername;
    private final String mongoDatabase;
    private final String mongoPassword;

    private final double defaultEndermites;
    private final double defaultCrystals;

    public NetworkInfos(String mongoHost, int mongoPort, String mongoUsername, String mongoDatabase, String mongoPassword, double defaultEndermites, double defaultCrystals) {
        this.mongoHost = mongoHost;
        this.mongoPort = mongoPort;
        this.mongoUsername = mongoUsername;
        this.mongoDatabase = mongoDatabase;
        this.mongoPassword = mongoPassword;
        this.defaultEndermites = defaultEndermites;
        this.defaultCrystals = defaultCrystals;
    }

    public NetworkInfos(BismuthByteBuf byteBuf) {
        this(byteBuf.readUTF(), byteBuf.readInt(), byteBuf.readUTF(), byteBuf.readUTF(), byteBuf.readUTF(), byteBuf.readDouble(), byteBuf.readDouble());
    }

    public void write(BismuthByteBuf byteBuf) {
        byteBuf.writeUTF(mongoHost);
        byteBuf.writeInt(mongoPort);
        byteBuf.writeUTF(mongoUsername);
        byteBuf.writeUTF(mongoDatabase);
        byteBuf.writeUTF(mongoPassword);
        byteBuf.writeDouble(defaultEndermites);
        byteBuf.writeDouble(defaultCrystals);
    }

    public String getMongoHost() {
        return mongoHost;
    }

    public int getMongoPort() {
        return mongoPort;
    }

    public String getMongoUsername() {
        return mongoUsername;
    }

    public String getMongoDatabase() {
        return mongoDatabase;
    }

    public String getMongoPassword() {
        return mongoPassword;
    }

    public double getDefaultEndermites() {
        return defaultEndermites;
    }

    public double getDefaultCrystals() {
        return defaultCrystals;
    }
}
