package com.haven.app.haven.constant;

public enum TrackerStatus {
    USED("USED"),
    NOT_USED("NOT_USED");

    private String status;

    TrackerStatus(String status){
        this.status = status;
    }

    public static TrackerStatus getStatus(String status){
        for(TrackerStatus trackerStatus : TrackerStatus.values()){
            if(trackerStatus.status.equals(status)){
                return trackerStatus;
            }
        }
        return null;
    }
}
