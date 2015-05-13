
package miny;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

/**
 *
 * @author Jirka
 */

public class Miny {
    
    public static void main(String[] args) {
        Random random = new Random();
        
        //velikost hracího pole + 2(osy)
        int rozloha = 9 + 2;
        //pocet min
        int pocet = 10;
        String mina = "M";
        String mlha = "-";
        String praporek = "P";
        String nula = " ";
        String[][] miny = new String[rozloha][rozloha];
        String[][] pole = new String[rozloha][rozloha];
        ArrayList<Integer> seznam = new ArrayList<>();
        
        //grafické vyplnění pole znaky
        for(int i=0;i!=rozloha;i++)
            for(int j=0;j!=rozloha;j++)
            {
                //svislé osy
                if(j==0 && i!=0 && i!=rozloha-1 || j==rozloha-1 && i!=0 && i!=rozloha-1)
                    miny [i][j] = String.format("%2d.",i);
                //vodorovné osy
                else if(i==0 && j!=0 && j!=rozloha-1 || i==rozloha-1&& j!=0 && j!=rozloha-1)
                    miny [i][j] = Character.toString((char)(96+j));
                //mlha
                else if(i!=0 && i!=rozloha-1 && j!=0 && j!=rozloha-1)
                    miny [i][j] = mlha;
                //levý horní a pravý dolní roh
                else if(i==0 && j==0 || i==rozloha-1 && j==rozloha-1)
                    miny [i][j] = "// ";
                //pravý horní a levý dolní roh
                else if(i==0 && j==rozloha-1 || i==rozloha-1 && j==0)
                    miny [i][j] = "\\\\ ";
            }
        //duplikace pole
        for(int i=0;i!=rozloha;i++)
            for(int j=0;j!=rozloha;j++)
                pole[i][j]=miny[i][j];
        
        //rozmístění min
        for(int i=0, x, y;i!=pocet;i++)
        {
            x=random.nextInt(rozloha-2)+1;
            y=random.nextInt(rozloha-2)+1;
            //ošetření proti umístění miny do místa, kde se mina již nachází
            if (miny [x][y].equals(mina))
                i--;
            else
                miny [x][y] = mina;
        }
        //spočítání min v okolí
        for(int i=1, cislo;i!=rozloha-1;i++)
            for(int j=1;j!=rozloha-1;j++)
            {
                cislo=0;
                for(int k=0;k!=3;k++)
                    for(int l=0;l!=3;l++)
                        if(miny[i+k-1][j+l-1].equals(mina))
                            cislo+=1;
                //zapsání min v okolí
                if(!miny[i][j].equals(mina))
                {
                    if(cislo!=0)
                        miny[i][j] = Integer.toString(cislo);
                    else
                        miny[i][j] = nula;
                }
            }
        Scanner sc = new Scanner(System.in, "UTF-8");
        int odkryto = 0;
        String volba;        
        System.out.println("Konzolová verze hry \"Miny\"\n" +
            "Políčko odhalíte zadáním souředanic (b3)\n" +
            "Políčko označíte zadáním souřadnice a k tomu písmeno 'P' (b3P)");
        //řídící cyklus
        do{
            //výpis
            for(int i=0;i!=rozloha;i++)
            {
                for(int j=0;j!=rozloha;j++)
                    System.out.printf("%s  ", pole [i][j]);
                System.out.printf("\n");
            }
            //ošetření vstupu uživatele
            do{
                System.out.println("Zadejte souřadnice ve tvaru \"B3\": ");
                volba = sc.nextLine() + "  ";
            }
            while(volba.charAt(0)-96 < 1 || volba.charAt(0)-96 > rozloha-2 ||
                    volba.charAt(1)-48 < 1 || volba.charAt(1)-48 > rozloha);
            
            //praporek
            if(volba.charAt(2)=='P' || volba.charAt(2)=='p')                    
            {
                if(pole[volba.charAt(1)-48][(int)(volba.charAt(0)-96)].equals(praporek))
                    pole[volba.charAt(1)-48][(int)(volba.charAt(0)-96)] = mlha;
                else
                    pole[volba.charAt(1)-48][(int)(volba.charAt(0)-96)] = praporek; 
            }
            //ověřování místa zásahu
            else
            {
                //přenesení informace i místě zásahu
                pole[volba.charAt(1)-48][(int)(volba.charAt(0)-96)] = 
                        miny[volba.charAt(1)-48][(int)(volba.charAt(0)-96)];
                //prohra, byla zasažena mina
                if(miny[volba.charAt(1)-48][(int)(volba.charAt(0)-96)].equals(mina))
                {
                    //předání min na výpis
                    for(int i=1;i!=rozloha-1;i++)                               
                        for(int j=1;j!=rozloha-1;j++)
                            if(miny[i][j].equals(mina))
                                pole[i][j] = miny[i][j];
                    //výpis min
                    for(int i=0;i!=rozloha;i++)
                    {
                        for(int j=0;j!=rozloha;j++)
                            System.out.printf("%s  ", pole [i][j]);
                        System.out.printf("\n");
                    }
                    System.out.println("Prohra! Šlápl jsi na minu!");
                }
                else
                    odkryto++;
                //odkrytí políček, pokud je zasažena "0"
                if(miny[volba.charAt(1)-48][(int)(volba.charAt(0)-96)].equals(nula))
                {
                    seznam.add(volba.charAt(1)-48);
                    seznam.add((int)(volba.charAt(0)-96));
                    while ((seznam.size())>0)
                    {
                        for(int i = 0; i < seznam.size(); i = i + 2)
                        {
                            for(int j=0;j!=3;j++)
                                for(int k=0;k!=3;k++)
                                {
                                    if(pole[seznam.get(i)+j-1][seznam.get(i+1)+k-1].equals(mlha) && miny[seznam.get(i)+j-1][seznam.get(i+1)+k-1].equals(nula))
                                    {
                                        seznam.add(seznam.get(i)+j-1);
                                        seznam.add(seznam.get(i+1)+k-1);
                                    }
                                    pole[seznam.get(i)+j-1][seznam.get(i+1)+k-1] =
                                        miny[seznam.get(i)+j-1][seznam.get(i+1)+k-1];
                                    odkryto++;
                                }
                            seznam.remove(i);
                            seznam.remove(i);
                        }
                    }
                }
                
                //výhra
                if(odkryto==(rozloha-2)*(rozloha-2)-pocet)
                {
                    //předání min na výpis
                    for(int i=1;i!=rozloha-1;i++)                               
                        for(int j=1;j!=rozloha-1;j++)
                            if(miny[i][j].equals(mina))
                                pole[i][j] = miny[i][j];
                    //výpis min
                    for(int i=0;i!=rozloha;i++)
                    {
                        for(int j=0;j!=rozloha;j++)
                            System.out.printf("%s  ", pole [i][j]);
                        System.out.printf("\n");
                    }
                    System.out.println("Výhra! Gratuluji, právě jsi dokončil hru Miny!");
                }                
            }            
        }       
        while(!pole[volba.charAt(1)-48][(int)(volba.charAt(0)-96)].equals(mina)
                && odkryto!=(rozloha-2)*(rozloha-2)-pocet);
    }  
}
