package com.threeknowbigdata.flink.icemarker;



public class IceMarker {
    private Long iceId;
    private String electric;
    private Long timestamp;

    public IceMarker(Long aLong, String field, Long aLong1) {
    }

    public IceMarker(Long iceId, String electric, String txId, Long timestamp) {
        this.iceId = iceId;
        this.electric = electric;
        this.timestamp = timestamp;
    }

    public Long getIceId() {
        return iceId;
    }

    public void setIceId(Long iceId) {
        this.iceId = iceId;
    }

    public String getElectric() {
        return electric;
    }

    public void setElectric(String electric) {
        this.electric = electric;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "IceMarker{" +
                "iceId=" + iceId +
                ", electric='" + electric + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
