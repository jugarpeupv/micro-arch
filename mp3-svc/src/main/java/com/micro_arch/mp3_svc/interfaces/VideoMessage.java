package com.micro_arch.mp3_svc.interfaces;

public class VideoMessage {
    private String video_id;
    private String mp3_fid;
    private String username;

    // Getters and setters
    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getMp3_fid() {
        return mp3_fid;
    }

    public void setMp3_fid(String mp3_fid) {
        this.mp3_fid = mp3_fid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
