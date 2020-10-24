package com.suarez.webporter.client;

import java.io.*;

import org.springframework.util.ClassUtils;
import org.springframework.util.ResourceUtils;
import sun.audio.*; //一般用这个头文件会报错

public class Music {
    sun.audio.AudioStream as;

    public void music() throws IOException {
        try {
            String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
            java.io.InputStream in = new java.io.FileInputStream(path+"/9414.wav");
            as = new sun.audio.AudioStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Start() {
        AudioPlayer.player.start(as);
    }

    public void Pause() {
        AudioPlayer.player.stop(as);
    }

    public void Continue() {
        AudioPlayer.player.start(as);
    }

    public static void main(String[] args) {
        Music m = new Music();
        try {
            m.music();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        m.Start();
    }

}
