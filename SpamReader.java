import java.io.*;
import java.net.Socket;

public class SpamReader {

    static final String hostname = "gdead.berkeley.edu";
    static final int port = 70;
    static final String request = "0/lyrics/more/spam.song";

    public void run() {
        int count = 0;	// number of total lines
        int spam  = 0;  // number of times the word 'spam' occurs

        // define additional variables here, if needed

        boolean done = false;
        Socket socket = null;
        String line = "";
        String Wait = "waitress";
        int waitCount = 0;

        try {
            /* open socket */
            socket = new Socket(hostname,port);

            /* set up BufferedReader for input and a PrintWriter (with autoflush enabled)
             * for output
             */

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "ASCII"));

            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);

            writer.printf("%s\r\n",request);

            while (!done) {
                /* read input until no more text is available, or until you read a single
                 * period by itself
                 */
                while((line = reader.readLine()) != null) {
                    count++;
                    if(line.startsWith("Waitress")) {
                        /* Count the number of lines read, and the number of occurrences of the
                         * word 'spam' in the 10th line of the Waitress
                         */
                        waitCount++;
                        if (waitCount == 10) {
                            String[] spammer = line.split(" ");
                            for(String s : spammer) {
                                if(s.contains("spam")) {
                                    spam++;
                                }
                            }
                        }
                    }

                }
                done = true;
            }

        } catch (IOException e) {
            System.err.println(e);
            return;
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
        System.out.println(String.format("There are %d lines in the song.", count));
        System.out.println(String.format(
                "The waitress used the word 'spam' %d times in her 10th line.",
                spam));
    }

    public static void main(String args[]) {
        SpamReader reader = new SpamReader();
        reader.run();
    }
}
