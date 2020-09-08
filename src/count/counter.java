package count;

/**
 * Created by ronak on 11/18/2015.
 */
public class counter {
    public static int countchunks;
    chunkcounting s = new chunkcounting();
    public void noofchunks() {
        try {
            s.splitfile("src/splitfiles/Test.zip");
          ;
            System.out.println("Total list of chunk server has" +" "+ s.count);
            countchunks=s.count;


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
