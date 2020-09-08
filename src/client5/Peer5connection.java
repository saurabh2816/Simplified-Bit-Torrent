package client5;

import java.io.*;
import java.net.Socket;

/**
 * Created by ronak on 11/24/2015.
 */
public class Peer5connection implements Runnable {
    public BufferedReader br = null;
    public static Socket csock;
    private String chunklist5[];
    private ObjectOutputStream out;

    public Peer5connection(Socket client, String[] list) {
        this.csock = client;
        this.chunklist5 = list;


    }
    @Override
    public void run() {

        try {
            br = new BufferedReader(new InputStreamReader(csock.getInputStream()));
            out = new ObjectOutputStream(csock.getOutputStream());
            out.writeObject(chunklist5);

        } catch (IOException e) {
            System.out.println("Error" + e);
        }
        String temp;
        temp="hakuna matata";

        for (int i = 0; i < chunklist5.length; i++) {
            try {
                temp=br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (chunklist5[i] != null && temp.equals("yes")) {
                sendfile(chunklist5[i], "Client1");
            }
            else {
                System.out.println("No more new files to upload ");
            }




        }
        System.out.println("DONE uploading!!!");
        System.out.println("Waiting for more new files to arrive");
        // break;
        try {
            csock.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }




    public static void sendfile(String file, String client) {

        try {

            File myFile = new File(file);
            byte[] mybytearray = new byte[(int) myFile.length()];

            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);

            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(mybytearray, 0, mybytearray.length);

            OutputStream os = csock.getOutputStream();

            DataOutputStream dos = new DataOutputStream(os);
            dos.writeUTF(myFile.getName());
            dos.writeLong(mybytearray.length);
            dos.write(mybytearray, 0, mybytearray.length);
            dos.flush();

            System.out.println("File " + file + " send to " + client);

        } catch (Exception e) {
            System.out.println("Erroe"+e);
        }


    }
}
