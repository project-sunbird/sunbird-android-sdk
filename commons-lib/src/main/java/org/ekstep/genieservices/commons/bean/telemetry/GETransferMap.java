package org.ekstep.genieservices.commons.bean.telemetry;

/**
 * Created by swayangjit on 2/5/17.
 */

public class GETransferMap {
    private String identifier;
    private Double pkgVersion;
    private int transferCount;
    private String origin;
    private String transferId;

    private GETransferMap(String identifier, Double pkgVersion, int transferCount, String origin) {
        this.identifier = identifier;
        this.pkgVersion = pkgVersion;
        this.transferCount = transferCount;
        this.origin = origin;
    }

    private GETransferMap(String origin, String transferId, int transferCount) {
        this.origin = origin;
        this.transferId = transferId;
        this.transferCount = transferCount;
    }

    public static GETransferMap createMapForContent(String identifier, Double pkgVersion, int transferCount, String origin) {
        return new GETransferMap(identifier, pkgVersion, transferCount, origin);
    }

    public static GETransferMap createMapForTelemetry(String origin, String importedId, int count) {
        return new GETransferMap(origin, importedId, count);
    }

    public String identifier() {
        return identifier;
    }

    public int transferCount() {
        return transferCount;
    }

    public Double pkgVersion() {
        return pkgVersion;
    }

    public String origin() {
        return origin;
    }

    public String transferId() {
        return transferId;
    }
}

