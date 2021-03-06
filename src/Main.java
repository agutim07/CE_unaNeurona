import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * @author Alberto Gutiérrez Morán
 */

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        boolean aleatorizar=true;

        File testfile = new File("D:\\Alberto GM\\ULE\\3º\\CE\\P1\\wine_norm_test.data");
        int[] n={0}; int[] s1={0};
        float[][] testdata = getArray(n,s1,testfile,aleatorizar);

        File trainfile = new File("D:\\Alberto GM\\ULE\\3º\\CE\\P1\\wine_norm_train.data");
        int[] s2={0};
        float[][] traindata = getArray(new int[]{0},s2,trainfile,aleatorizar);

        inciarSimulacion(traindata,testdata,n[0], new int[]{s1[0], s2[0]});
    }

    private static void inciarSimulacion(float[][] train, float[][] test, int n, int[] s){
        int s1 = s[0]; int s2 = s[1];
        /** SALIDAS ESPERADAS */
        int[] d1 = new int[s[0]]; int[] d2 = new int[s[1]];
        for(int i=0; i<s1; i++){ d1[i] = (int) test[i][n]; }
        for(int i=0; i<s2; i++){ d2[i] = (int) train[i][n];}

        int[] r = new int[n+1];
        for(int i=0; i<n; i++){
            r[i] = (int) (Math.random()*5 + 1);
        }
        r[n]=1;

        /** INCIO ALEATORIO DE LOS PESOS */
        int T=0;
        int t=0;
        float[] w = new float[n+1]; Random rand = new Random();
        for(int i=0; i<=n; i++){
            int randomInt = new Random().nextBoolean() ? (-1) : (1);
            w[i] = randomInt * rand.nextFloat();
        }

        /** ERROR */
        float error1=0, error2=0;
        for(int i=0; i<s1; i++){
            float[] x = new float[n+1]; x[n] = 1;
            for(int y=0; y<n; y++){x[y] = test[i][y];}
            float p = sumatorio(w,x,r);
            float y = funcion(1,p,1);
            error1+=0.5*(Math.pow((y-d1[i]),2));
        }
        for(int i=0; i<s2; i++){
            float[] x = new float[n+1]; x[n] = 1;
            for(int y=0; y<n; y++){x[y] = train[i][y];}
            float p = sumatorio(w,x,r);
            float y = funcion(1,p,1);
            error2+=0.5*(Math.pow((y-d2[i]),2));
        }
        float errorM1 = error1/s1;
        float errorM2 = error2/s2;
        float errorM1_aceptable = (float) 0.2;
        float errorM2_aceptable = (float) 0.1;
        int tMax1=1;
        int tMax2=3*s2;

        int muestrasValidacionCorrectas=0;
        int muestrasValidacionInCorrectas=0;

        /** ENTRENAMIENTO */
        while(errorM1>errorM1_aceptable && T<tMax1){
            /** SUB - ENTRENAMIENTO */
            while(errorM2>errorM2_aceptable && t<tMax2){
                for(int i=0; i<s2; i++){
                    float[] x = new float[n+1]; x[n] = 1;
                    for(int y=0; y<n; y++){x[y] = train[i][y];}
                    float p = sumatorio(w,x,r);
                    float y = funcion(1,p,1);
                    for(int j=0; j<=n; j++){
                        w[j]+= -gamma(t,tMax2) * 0.5 * 2 * (y-d2[i]) * funcion(1,p,2) * (Math.pow(x[j],r[j]));
                    }
                    t++;
                }

                float et2=0;
                for(int i=0; i<s2; i++){
                    float[] x = new float[n+1]; x[n] = 1;
                    for(int y=0; y<n; y++){x[y] = train[i][y];}
                    float p = sumatorio(w,x,r);
                    float y = funcion(1,p,1);
                    et2+=0.5*(Math.pow((y-d2[i]),2));
                }
                errorM2 = et2 /s2;
            }
            /** FIN SUB - ENTRENAMIENTO */
            float et1=0;
            muestrasValidacionCorrectas=0;
            muestrasValidacionInCorrectas=0;
            for(int i=0; i<s1; i++){
                float[] x = new float[n+1]; x[n] = 1;
                for(int y=0; y<n; y++){x[y] = test[i][y];}
                float p = sumatorio(w,x,r);
                float y = funcion(1,p,1);
                et1+=0.5*(Math.pow((y-d1[i]),2));
                /** VALIDACION DE MUESTRAS */
                int aprox=0; /** SI Y<0.1 && Y>0.3 ENTONCES SALIDA=0 */
                if(y>0.35) aprox=1; /** SI Y>0.1 ENTONCES SALIDA=1 */
                if(y<-0.35) aprox=-1; /** SI Y<0.3 ENTONCES SALIDA=-1 */
                //System.out.println(y + " , "+aprox+ " , "+d1[i]);
                if(aprox==d1[i]){
                    muestrasValidacionCorrectas++;
                }else{
                    muestrasValidacionInCorrectas++;
                }
                /** FIN VALIDACION DE MUESTRAS */
            }
            errorM1 = et1/s1;
            T++;
        }
        /** FIN ENTRENAMIENTO */

        System.out.println("Validaciones correctas: "+muestrasValidacionCorrectas + " , Validaciones incorrectas: "+muestrasValidacionInCorrectas);
        System.out.println("Error Medio 1 (test): "+errorM1 + " , Error Medio 2 (train): "+errorM2);
    }

    private static float[][] getArray(int[] n, int[] s, File file, boolean rand) throws FileNotFoundException {
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

        if(rand){
            List<Integer> posArray = new ArrayList<>();
            for(int i=0; i<s[0]; i++){posArray.add(i);}
            Collections.shuffle(posArray);
            float[][] newArray = new float[s[0]][n[0]+1];
            for(int i=0; i<s[0]; i++){
                int row = posArray.get(i);
                for(int x=0; x<=n[0]; x++){
                    newArray[i][x] = array[row][x];
                }
            }
            array = newArray;
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

    private static float sumatorio(float[] x, float[] y, int[] r){
        float sum=0;
        for(int i=0; i<x.length; i++){
            sum+=x[i]*(Math.pow(y[i],r[i]));
        }
        return sum;
    }

    private static float funcion(int op, float x, int modo){
        if(op==0){
            if(modo==1) return x;
            if(modo==2) return 1;
        }
        if(op==1){
            if(modo==1) return (float) Math.sin(x);
            if(modo==2) return (float) Math.cos(x);
        }
        if(op==2){
            if(modo==1) return (float) (2 / ( 1 +Math.exp(-x)) - 1);
            if(modo==2) return (float) ((-2 * Math.exp(-x))/(Math.pow((1+Math.exp(-x)),2)));
        }
        if(op==3){
            if(modo==1) return (float)  (2 * Math.exp(Math.pow(-x,2)) - 1);
            if(modo==2) return (float) (2* Math.exp(Math.pow(-x,2))) * (-2 * x);
        }

        return 0;
    }

    private static float gamma(int t, int t_max){
        int alfa=4;
        int a = t_max * 7 /8;
        return (float) (-1 / (1 + Math.exp(-alfa*(t-a))) + 1);
    }

}


