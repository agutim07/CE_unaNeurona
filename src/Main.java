import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Alberto Gutiérrez Morán
 */

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        File trainfile = new File("D:\\ULE\\3º\\CE\\P1\\wine_normalizado_train.data");
        int[] n={0}; int[] s1={0};
        float[][] traindata = getArray(n,s1,trainfile);

        File testfile = new File("D:\\ULE\\3º\\CE\\P1\\wine_normalizado_test.data");
        int[] s2={0};
        float[][] testdata = getArray(new int[]{0},s2,testfile);
        System.out.println("COLUMNAS: "+n[0]+" - FILAS: "+s2[0]);
        for(int x=0; x<s2[0]; x++){
            for(int y=0; y<=n[0]; y++){
                System.out.print(testdata[x][y]+" ");
            }
            System.out.println();
        }

    }

    private static float[][] getArray(int[] n, int[] s, File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        Scanner sc2 = new Scanner(file);
        boolean calculateS = true;

        while(true){
            String nxt = sc.nextLine();
            s[0]++;
            if(calculateS){
                String[] columns = nxt.split("\\s+");
                n[0] = columns.length-1;
                calculateS=false;
            }

            if(!sc.hasNextLine()){
                break;
            }
        }

        float[][] array = new float[s[0]][n[0]+1];
        int fila=0;

        while (true) {
            String nxt = sc2.nextLine();
            String[] values = nxt.split("\\s+");
            for (int i = 0; i < values.length; i++) {
                values[i] = values[i].replaceAll("[^\\w]", "");
                if(fila==0) System.out.println(values[i]);
                array[fila][i] = Float.parseFloat(values[i]) ;
            }
            fila++;
            if(!sc2.hasNextLine()){
                break;
            }
        }

        return array;
    }

}


