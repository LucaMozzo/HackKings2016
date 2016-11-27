import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Play {

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {


        String inDirectoryPath = "C:\\Users\\Cristian\\Desktop\\HackKing-s - Copy\\src\\in\\";
        String outDirectoryPath = "C:\\Users\\Cristian\\Desktop\\HackKing-s - Copy\\src\\out\\";

        long recordTime = 10000;
        String inVoiceFile = "C:\\Users\\Cristian\\Desktop\\HackKing-s - Copy\\src\\in\\inRecordAudio.wav";

        try {
            Files.delete(Paths.get(inDirectoryPath + "desktop.ini"));
        } catch (Exception e) {}


        final JavaSoundRecorder recorder = new JavaSoundRecorder(inVoiceFile);

        // creates a new thread that waits for a specified
        // of time before stopping
        Thread stopper = new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(recordTime);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                recorder.finish();
            }
        });
        stopper.start();
        // start recording
        recorder.start();




        File dir = new File(inDirectoryPath);
        File[] ins = dir.listFiles();
        String[] outs = new String[ins.length];

        SimpleSoundPlayer[] inSounds = new SimpleSoundPlayer[ins.length];
        SimpleSoundPlayer[] outSounds = new SimpleSoundPlayer[ins.length];

        InputStream[] inputStreams = new ByteArrayInputStream[ins.length];
        InputStream[] outputStreams = new ByteArrayInputStream[ins.length];

        int speed = 60000;

        for (int i = 0; i < ins.length; i++) {
            int indexNameStart = ins[i].toString().lastIndexOf("\\") + 1;
            int indexToAddOut =  ins[i].toString().indexOf(".wav");
            outs[i] = outDirectoryPath + ins[i].toString().substring(indexNameStart, indexToAddOut) + "OUT" + ins[i].toString().substring(indexToAddOut);
        }


        for (int i = 0; i < ins.length; i++) {
            inSounds[i] = new SimpleSoundPlayer(ins[i].toString(), speed, outs[i]);
            inputStreams[i] = new ByteArrayInputStream(inSounds[i].getSamples());
            inSounds[i].play(inputStreams[i]);
            inSounds[i].write();
        }


        for (int i = 0; i < ins.length; i++) {
            outSounds[i] = new SimpleSoundPlayer(outs[i]);
            outputStreams[i] = new ByteArrayInputStream(outSounds[i].getSamples());
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < ins.length; i++) {
            outSounds[i].play(outputStreams[i]);
        }


        /*
        String inputLink1 = "C:\\Users\\Cristian\\Desktop\\HackKing-s\\src\\filehost_Florin_Salam_-_Saint_Tropez_PeMuzica.wav";
        String outputLink1 = "C:\\Users\\Cristian\\Desktop\\HackKing-s\\src\\amy2.wav";

        String inputLink2 = "C:\\Users\\Cristian\\Desktop\\HackKing-s\\src\\Inspector_Gadget.wav";
        String outputLink2 = "C:\\Users\\Cristian\\Desktop\\HackKing-s\\src\\eric2.wav";


        int speed1 = 30000;
        int speed2 = 85000;

        // load a sound
        SimpleSoundPlayer sound1 = new SimpleSoundPlayer(inputLink1, speed1, outputLink1);
        SimpleSoundPlayer sound2 = new SimpleSoundPlayer(inputLink2, speed2, outputLink2);

        // create the inputStream to play_silent
        InputStream stream1 = new ByteArrayInputStream(sound1.getSamples());
        InputStream stream2 = new ByteArrayInputStream(sound2.getSamples());

        // play_silent the sound
        sound1.play_silent(stream1);
        sound1.write();

        sound2.play_silent(stream2);
        sound2.write();

        SimpleSoundPlayer sound3 = new SimpleSoundPlayer(outputLink1);
        SimpleSoundPlayer sound4 = new SimpleSoundPlayer(outputLink2);
        InputStream stream3 = new ByteArrayInputStream(sound3.getSamples());
        InputStream stream4 = new ByteArrayInputStream(sound4.getSamples());
        sound3.play(stream3);
        sound4.play(stream4);


        // exit
        */
        System.exit(0);
    }
}