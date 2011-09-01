package org.miloss;

import java.io.IOException;

public interface Communicator {

    void send(SoundBite soundBite) throws IOException;

    void addSoundBiteListener(SoundBiteListener listener);

    void removeSoundBiteListener(SoundBiteListener listener);
}
