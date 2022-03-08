import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

/**
 * @author Alberto Gutiérrez Morán
 */

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        File testfile = new File("D:\\Alberto GM\\ULE\\3º\\CE\\P1\\wine_normalizado_test.data");
        int[] n={0}; int[] s1={0};
        float[][] testdata = getArray(n,s1,testfile);

        File trainfile = new File("D:\\Alberto GM\\ULE\\3º\\CE\\P1\\wine_normalizado_train.data");
        int[] s2={0};
        float[][] traindata = getArray(new int[]{0},s2,trainfile);

        /** PRINT DATA */
        /*System.out.println("COLUMNAS: "+n[0]+" - FILAS: "+s2[0]);
        for(int x=0; x<s2[0]; x++){
            for(int y=0; y<=n[0]; y++){
                System.out.print(testdata[x][y]+" ");
            }
            System.out.println();
        }*/
        /** END */

        inciarSimulacion(traindata,testdata,n[0], new int[]{s1[0], s2[0]});


    }

    private static void inciarSimulacion(float[][] train, float[][] test, int n, int[] s){
        int s1 = s[0]; int s2 = s[1];
        /** SALIDAS ESPERADAS */
        int[] d1 = new int[s[0]]; int[] d2 = new int[s[1]];
        for(int i=0; i<s1; i++){ d1[i] = (int) test[i][n]; }
        for(int i=0; i<s2; i++){ d2[i] = (int) train[i][n];}

        /** INCIO ALEATORIO DE LOS PESOS */
        int t=0;
        float[] w = new float[n+1]; Random rand = new Random();
        for(int i=0; i<=n; i++){
            int randomInt = new Random().nextBoolean() ? (-1) : (1);
            w[i] = randomInt * rand.nextFloat();
        }

        /** ERROR */
        float error=0;
        for(int i=0; i<s2; i++){
            float[] x = new float[n+1]; x[n] = 1;
            for(int y=0; y<n; y++){x[y] = train[i][y];}
            float p = sumatorio(w,x);
            float y = funcion(1,p);
            error+=0.5*(Math.pow((y-d2[i]),2));
        }
        float errorM = error/s2;
        float errorM_aceptable = (float) 0.1;
        int tMax=s2;

    }

    private static float[][] getArray(int[] n, int[] s, File file) throws FileNotFoundException {
        Scanner sc = new Scanner(file);
        Scanner sc2 = new Scanner(file);
        boolean calculateS = true;

        while(true){
            String nxt = sc.nextLine();
            s[0]++;
            if(calculateS){
                n[0] = extractNumbers(nxt).length-1;
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
            float[] values = extractNumbers(nxt);
            for (int i = 0; i < values.length; i++) {
                array[fila][i] = values[i] ;
            }
            fila++;
            if(!sc2.hasNextLine()){
                break;
            }
        }

        return array;
    }

    private static float[] extractNumbers(String str){
        ArrayList<Integer> espacios = new ArrayList<>();
        for(int i=0; i<str.length(); i++) {
            char c = str.charAt(i);
            if(c==' '){
                espacios.add(i);
            }
        }
        int numeros = espacios.size()+1;
        float[] out = new float[numeros];
        out[0] = Float.parseFloat(str.substring(0,espacios.get(0)));
        int i;
        for(i=1; i<espacios.size(); i++) {
            out[i] = Float.parseFloat(str.substring(espacios.get(i-1)+1,espacios.get(i)));
        }
        out[i] = Float.parseFloat(str.substring(espacios.get(i-1)+1));

        return out;
    }

    private static float sumatorio(float[] x, float[] y){
        float sum=0;
        for(int i=0; i<x.length; i++){
            sum+=x[i]*y[i];
        }
        return sum;
    }

    private static float funcion(int op, float x){
        if(op==0) return x;
        if(op==1) return (float) Math.sin(x);

        return 0;
    }

    private static float gamma(int t, int t_max){
        int alfa=4;
        int a = t_max * 7 /8;
        return (float) (-1 / (1 + Math.exp(-alfa*(t-a))) + 1);
    }

}


