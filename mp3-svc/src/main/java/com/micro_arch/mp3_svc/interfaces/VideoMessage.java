package com.micro_arch.mp3_svc.interfaces;

import lombok.Data;

@Data
public class VideoMessage {
  private String video_id;
  private String mp3_fid;
  private String username;
}
