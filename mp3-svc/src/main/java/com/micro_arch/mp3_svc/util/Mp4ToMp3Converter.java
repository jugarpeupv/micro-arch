
package com.micro_arch.mp3_svc.util;

import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.MultimediaObject;

import java.io.File;

public class Mp4ToMp3Converter {

    public static void convertMp4ToMp3(File source, File target) throws EncoderException {
        // Set audio attributes
        AudioAttributes audio = new AudioAttributes();
        audio.setCodec("libmp3lame");
        audio.setBitRate(128000);
        audio.setChannels(2);
        audio.setSamplingRate(44100);

        // Set encoding attributes
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setOutputFormat("mp3");
        attrs.setAudioAttributes(audio);

        // Encode
        Encoder encoder = new Encoder();
        encoder.encode(new MultimediaObject(source), target, attrs);
    }
}
