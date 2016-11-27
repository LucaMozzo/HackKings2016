import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED;

/**
 * The SimpleSoundPlayer encapsulates a sound that can be opened from the file
 * system and later played.
 */

class SimpleSoundPlayer {

    private AudioFormat format;

    private byte[] samples;

    private byte[] full_bfr;
    private File out;
    private AudioInputStream inputStream;


    /**
     * Opens a sound from a file.
     */
    public SimpleSoundPlayer(String filename, int speed, String outputFilePath) {
        try {
            out = new File(outputFilePath);

            // open the audio input inputStream
            inputStream = AudioSystem.getAudioInputStream( new File(filename));

            format = inputStream.getFormat();
            format = new AudioFormat(PCM_SIGNED, speed, 16, format.getChannels(), format.getChannels() * 2, format.getFrameRate(), format.isBigEndian()); ////

            // get the audio samples
            samples = getSamples(inputStream);

        } catch (UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public SimpleSoundPlayer(String filename) {
        try {
            // open the audio input inputStream
            inputStream = AudioSystem.getAudioInputStream(new File(filename));

            format = inputStream.getFormat();

            // get the audio samples
            samples = getSamples(inputStream);
        } catch (UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gets the samples of this sound as a byte array.
     */
    public byte[] getSamples() {
        return samples;
    }

    /**
     * Gets the samples from an AudioInputStream as an array of bytes.
     */
    private byte[] getSamples(AudioInputStream audioStream) {
        // get the number of bytes to read
        int length = (int) (audioStream.getFrameLength() * format
                .getFrameSize());

        // read the entire inputStream
        byte[] samples = new byte[length];
        DataInputStream is = new DataInputStream(audioStream);
        try {
            is.readFully(samples);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // return the samples
        return samples;
    }

    /**
     * Plays a inputStream. This method blocks (doesn't return) until the sound is
     * finished playing.
     */
    public void play_silent(InputStream source) {

        // use a short, 100ms (1/10th sec) buffer for real-time
        // change to the sound inputStream
        int bufferSize = format.getFrameSize()
                * Math.round(format.getSampleRate() / 10);
        byte[] buffer = new byte[bufferSize];

        List<Byte> full_buffer = new ArrayList<>();
        // copy data to the full_buffer
        try {
            int numBytesRead = 0;
            while (numBytesRead != -1) {
                numBytesRead = source.read(buffer, 0, buffer.length);
                if (numBytesRead != -1) {
                    for (int i = 0; i < buffer.length; i++) {
                        full_buffer.add(buffer[i]);
                    }
                }
            }

            byte[] full_buff = new byte[full_buffer.size()];
            for (int i = 0; i < full_buffer.size(); i++) {
                full_buff[i] = full_buffer.get(i);
            }

            full_bfr = full_buff;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void write() {

        long length = inputStream.getFrameLength();

        AudioInputStream appendedFiles = new AudioInputStream(inputStream, format, length);

        try {
            AudioSystem.write(appendedFiles, AudioFileFormat.Type.WAVE, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void play(InputStream source) {

        // use a short, 100ms (1/10th sec) buffer for real-time
        // change to the sound inputStream
        int bufferSize = format.getFrameSize()
                * Math.round(format.getSampleRate() / 10);
        byte[] buffer = new byte[bufferSize];

        // create a line to play to
        SourceDataLine line;
        try {
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
            line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(format, bufferSize);
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
            return;
        }

        // start the line
        line.start();

        // copy data to the line
        try {
            int numBytesRead = 0;
            while (numBytesRead != -1) {
                numBytesRead = source.read(buffer, 0, buffer.length);
                if (numBytesRead != -1) {
                    line.write(buffer, 0, numBytesRead);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // wait until all data is played, then close the line
        line.drain();
        line.close();

    }

}