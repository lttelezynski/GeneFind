/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package train_by_count_cod53st;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author lucas
 */
public class Train_by_count_cod53st {
    static final int numStates = 53;
    
    String[] obs;
    char[] hid;
    HashMap<String, Integer> observables = new HashMap<>();

    int[] pi;
    int[][] trans;
    int[][] em;

    ArrayList<String> seqs;
    HashMap<String, Integer> names = new HashMap<>();
    String[] states;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length==3){
            new Train_by_count_cod53st().Train_by_count_cod53st(args);
        }
        else{
            System.out.println("3 parameters required!!!");
            System.exit(0);
        }
        // TODO code application logic here
    }
    public void Train_by_count_cod53st(String[] args){
        readMM(args[0]);
        readObs(args[1]);
        readStates(args[2]);
        count();
        printCounts();
        
    }

    private void readMM(String input){
        try{
            BufferedReader br1 = new BufferedReader(new FileReader(input));
            String line = br1.readLine();
            while(line!=null){
                if(line.contains("hidden")){
                    String[] l = br1.readLine().split(" ");
                    hid = new char[l.length];
                    for(int i=0;i<l.length;i++){
                        hid[i] = l[i].charAt(0);
                    }
                }
                if(line.contains("observables")){
                    String[] l = br1.readLine().split(" ");
                    obs = new String[l.length];
                    for(int i=0;i<l.length;i++){
                        observables.put(l[i], i);
                        obs[i] = l[i];
                    }
                }
                line = br1.readLine();
            }
        }catch(Exception e){
            e.printStackTrace();
        }                
    }

    private void readObs(String input){
        System.out.println("Reading input from: "+ input);

        try{
            BufferedReader br1 = new BufferedReader(new FileReader(input));
            String line = br1.readLine();
            seqs = new ArrayList<>();
            while(line!=null){
                if(line.startsWith(">")){
                    String[] l = line.split(" ");
                    names.put(l[0], seqs.size());
                    line = br1.readLine();
                }
                else{
                    String l = line;
                    line = br1.readLine();
                    while(line!=null && !line.startsWith(">")){
                        l+=line;
                        line = br1.readLine();
                    }
                    while(l.indexOf('N')!=-1){
                        l=l.replaceFirst("N", randomChar('N'));
                    }
                    while(l.indexOf('Y')!=-1){
                        l=l.replaceFirst("Y", randomChar('Y'));
                    }
                    while(l.indexOf('K')!=-1){
                        l=l.replaceFirst("K", randomChar('K'));
                    }
                    while(l.indexOf('R')!=-1){
                        l=l.replaceFirst("R", randomChar('R'));
                    }
                    while(l.indexOf('M')!=-1){
                        l=l.replaceFirst("M", randomChar('M'));
                    }
                    while(l.indexOf('S')!=-1){
                        l=l.replaceFirst("S", randomChar('S'));
                    }
                    while(l.indexOf('W')!=-1){
                        l=l.replaceFirst("W", randomChar('W'));
                    }
                    while(l.indexOf('B')!=-1){
                        l=l.replaceFirst("B", randomChar('B'));
                    }
                    while(l.indexOf('D')!=-1){
                        l=l.replaceFirst("D", randomChar('D'));
                    }
                    while(l.indexOf('H')!=-1){
                        l=l.replaceFirst("H", randomChar('H'));
                    }
                    while(l.indexOf('V')!=-1){
                        l=l.replaceFirst("V", randomChar('V'));
                    }
                    seqs.add(l);
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }                
    }

    private void readStates(String input){
        System.out.println("Reading states from: "+ input);

        try{
            BufferedReader br1 = new BufferedReader(new FileReader(input));
            String line = br1.readLine();
            states = new String[seqs.size()];
            int p=-1;
            while(line!=null){
                if(line.startsWith(">")){
                    String[] l = line.split(" ");
                    p = names.get(l[0]);
                    line = br1.readLine();
                }
                else{
                    String l = line;
                    line = br1.readLine();
                    while(line!=null && !line.startsWith(">")){
                        l+=line;
                        line = br1.readLine();
                    }
                    states[p] = l;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }                
    }
    
    private String randomChar(char c){
        Random r = new Random();
        String n="";
        if(c=='N'){
            n = String.valueOf(obs[r.nextInt(4)]);
        }
        if(c=='Y'){
            char[] nuc = new char[]{'C', 'T'};
            n = String.valueOf(nuc[r.nextInt(2)]);
        }
        if(c=='K'){
            char[] nuc = new char[]{'G', 'T'};
            n = String.valueOf(nuc[r.nextInt(2)]);
        }
        if(c=='R'){
            char[] nuc = new char[]{'G', 'A'};
            n = String.valueOf(nuc[r.nextInt(2)]);
        }
        if(c=='M'){
            char[] nuc = new char[]{'A', 'C'};
            n = String.valueOf(nuc[r.nextInt(2)]);
        }
        if(c=='S'){
            char[] nuc = new char[]{'G', 'C'};
            n = String.valueOf(nuc[r.nextInt(2)]);
        }
        if(c=='W'){
            char[] nuc = new char[]{'A', 'T'};
            n = String.valueOf(nuc[r.nextInt(2)]);
        }
        if(c=='B'){
            char[] nuc = new char[]{'G', 'T', 'C'};
            n = String.valueOf(nuc[r.nextInt(3)]);
        }
        if(c=='D'){
            char[] nuc = new char[]{'G', 'A', 'T'};
            n = String.valueOf(nuc[r.nextInt(3)]);
        }
        if(c=='H'){
            char[] nuc = new char[]{'A', 'C', 'T'};
            n = String.valueOf(nuc[r.nextInt(3)]);
        }
        if(c=='V'){
            char[] nuc = new char[]{'G', 'C', 'A'};
            n = String.valueOf(nuc[r.nextInt(3)]);
        }
        return n;
    }
    
    private int[] states2int(char[] X, char[] Z){
        int[] iZ = new int[X.length];
        
        if(Z[0]=='N'){
            iZ[0]=0;
        }
        else if(Z[0]=='I'){
            iZ[0]=99;
        }
        else{
            iZ[0]=99;
        }
        
        if(Z[1]=='N'){
            iZ[1]=0;
        }
        else if(Z[1]=='I'){
            if(iZ[0]==99){
                iZ[1]=39;
            }
            else{
                iZ[1]=99;
            }
        }
        else{
            iZ[1]=99;
        }
        
        //0-start; 1-internal; 2-stop
        int whichCodon=-1;
        int lastC=-1;
        int prevC=-1;
        for(int j=2;j<Z.length;j++){
            if(Z[j]=='N'){
                iZ[j]=0;
                if(j!=Z.length-1 && Z[j+1]!='N'){
                    for(int i=0;i<6;i++){
                        if(j-i>=0){
                            iZ[j-i]=52-i;
                        }
                    }
                }
            }
            else if(Z[j]=='S'){
                if(iZ[j-1]==99 && iZ[j-2]==99){
                    if(j==2 || iZ[j-3]==52){
                        iZ[j]=1;
                    }
                    else if(iZ[j-3]==1){
                        iZ[j]=2;
                    }
                    else{
                        iZ[j]=3;
                    }
                }
                else{
                    iZ[j]=99;
                }
                if(j==Z.length-1 || Z[j+1]=='N'){
                    iZ[j]=4;
                }
            }
            else if(Z[j]=='B'){
                if((j==2 || iZ[j-3]==52) && iZ[j-1]==99 && iZ[j-2]==99){
                    iZ[j]=5;
                }
                else if(iZ[j-3]==5){
                    iZ[j]=6;
                }
                else if(iZ[j-3]==7 || iZ[j-3]==6){
                    iZ[j]=7;
                }
                else{
                    iZ[j]=99;
                }
                if(j==Z.length-1 || Z[j+1]=='I'){
                    if(iZ[j-1]==99 && iZ[j-2]==99){
                        iZ[j]=10;
                    }
                    else if(iZ[j-1]==99){
                        iZ[j]=9;
                    }
                    else{
                        iZ[j]=8;
                    }
                }
            }
            else if(Z[j]=='M'){
                if(iZ[j-1]==38){
                    iZ[j]=11;
                }
                else if(iZ[j-2]==30 && iZ[j-1]==99){
                    iZ[j]=12;
                }
                else if(iZ[j-3]==46 && iZ[j-1]==99 && iZ[j-2]==99){
                    iZ[j]=13;
                }
                else if(iZ[j-1]==99 && iZ[j-2]==99){
                    iZ[j]=14;
                }
                else{
                    iZ[j]=99;
                }
                if(j==Z.length-1 || Z[j+1]=='I'){
                    if(iZ[j-1]==99 && iZ[j-2]==99){
                        iZ[j]=17;
                    }
                    else if(iZ[j-1]==99){
                        iZ[j]=16;
                    }
                    else{
                        iZ[j]=15;
                    }
                }
            }
            else if(Z[j]=='E'){
                if(iZ[j-1]==38){
                    iZ[j]=18;
                }
                else if(iZ[j-2]==30 && iZ[j-1]==99){
                    iZ[j]=19;
                }
                else if(iZ[j-3]==46 && iZ[j-1]==99 && iZ[j-2]==99){
                    iZ[j]=20;
                }
                else if(iZ[j-1]==99 && iZ[j-2]==99){
                    iZ[j]=21;
                }
                else{
                    iZ[j]=99;
                }
                if(j==Z.length-1 || Z[j+1]=='N'){
                    iZ[j]=22;
                }
            }
            else if(Z[j]=='I'){
                if(iZ[j-1]==99 && (iZ[j-2]==8 || iZ[j-2]==15)){
                    iZ[j]=23;
                }
                else if(iZ[j-1]==23){
                    iZ[j]=24;
                }
                else if(iZ[j-1]==24){
                    iZ[j]=25;
                }
                else if(iZ[j-1]==25){
                    iZ[j]=26;
                }
                else if(iZ[j-1]==26){
                    iZ[j]=27;
                }
                else if(iZ[j-1]==28 || iZ[j-1]==27){
                    iZ[j]=28;
                }
                else if(iZ[j-1]==99 && (iZ[j-2]==9 || iZ[j-2]==16)){
                    iZ[j]=31;
                }
                else if(iZ[j-1]==31){
                    iZ[j]=32;
                }
                else if(iZ[j-1]==32){
                    iZ[j]=33;
                }
                else if(iZ[j-1]==33){
                    iZ[j]=34;
                }
                else if(iZ[j-1]==34){
                    iZ[j]=35;
                }
                else if(iZ[j-1]==36 || iZ[j-1]==35){
                    iZ[j]=36;
                }
                else if(iZ[j-1]==99 && (iZ[j-2]==5 || iZ[j-2]==10 || iZ[j-2]==17)){
                    iZ[j]=39;
                }
                else if(iZ[j-1]==39){
                    iZ[j]=40;
                }
                else if(iZ[j-1]==40){
                    iZ[j]=41;
                }
                else if(iZ[j-1]==41){
                    iZ[j]=42;
                }
                else if(iZ[j-1]==42){
                    iZ[j]=43;
                }
                else if(iZ[j-1]==44 || iZ[j-1]==43){
                    iZ[j]=44;
                }
                else{
                    iZ[j]=99;
                }
                
                if(j==Z.length-1 || Z[j+1]!='I'){
                    if(iZ[j-1]==28){
                        iZ[j]=30;
                        iZ[j-1]=99;
                        iZ[j-2]=29;
                    }
                    if(iZ[j-1]==36){
                        iZ[j]=38;
                        iZ[j-1]=99;
                        iZ[j-2]=37;
                    }
                    if(iZ[j-1]==44){
                        iZ[j]=46;
                        iZ[j-1]=99;
                        iZ[j-2]=45;
                    }
                }
                
            }
        }        

        return iZ;
    }

    private void count(){
        pi = new int[numStates];
        trans = new int[numStates][numStates];
        em = new int[numStates][obs.length];
        
        for(int i=0;i<seqs.size();i++){
            char[] X = seqs.get(i).toCharArray();
            char[] Z = states[i].toCharArray();
            int[] iZ = states2int(X, Z);
//            saveStates(iZ, Z);
            System.out.println(i+". "+Z.length);
            for(int el:iZ){
                System.out.print(" "+el);
            }
            System.out.println("");
            System.out.println("");
            
            if(iZ[0]!=99){
                pi[iZ[0]]++;
                em[iZ[0]][observables.get(String.valueOf(X[0]))]++;
            }
            
            if(iZ[1]!=99){
                trans[iZ[0]][iZ[1]]++;
                em[iZ[1]][observables.get(String.valueOf(X[1]))]++;

            }
            
            for(int n=2;n<X.length;n++){
                if(iZ[n]!=99){
                    if((iZ[n]>=1 && iZ[n]<=7) || iZ[n]==10 || iZ[n]==13 || iZ[n]==14 || iZ[n]==17 || (iZ[n]>=20 && iZ[n]<=22)){
                        if(n>2)
                            trans[iZ[n-3]][iZ[n]]++;
                        em[iZ[n]][observables.get(String.valueOf(X[n-2])+String.valueOf(X[n-1])+String.valueOf(X[n]))]++;
                    }
                    else if(iZ[n]==9 || iZ[n]==12 || iZ[n]==16 || iZ[n]==19 || iZ[n]==23 || iZ[n]==30 || iZ[n]==31 || iZ[n]==38 || iZ[n]==39 || iZ[n]==46){
                        trans[iZ[n-2]][iZ[n]]++;
                        em[iZ[n]][observables.get(String.valueOf(X[n-1])+String.valueOf(X[n]))]++;
                    }
                    else{
                        try{
                            trans[iZ[n-1]][iZ[n]]++;
                        }
                        catch(Exception e){
                            System.out.println(i+" "+Z.length+" "+n+" "+iZ[n]+" "+Z[n]+" "+iZ[n-1]+" "+Z[n-1]);
                        }
                        em[iZ[n]][observables.get(String.valueOf(X[n]))]++;
                    }
                }
                
                
            }            
        }
    }
    
    private void printCounts(){
        System.out.println("Init");
        for(int i=0;i<pi.length;i++){
            System.out.print(" "+pi[i]);
        }
        System.out.println();
        System.out.println("Transitions");
        for(int k=0;k<trans.length;k++){
            System.out.print(String.valueOf(" "+trans[k][0]));
            for(int i=1;i<trans[k].length;i++){
                System.out.print(" "+trans[k][i]);
            }
        }
        System.out.println("\nemissions");
        for(int k=0;k<em.length;k++){
            System.out.print(String.valueOf(" "+em[k][0]));
            for(int i=1;i<em[k].length;i++){
                System.out.print(" "+em[k][i]);
            }
        }
        System.out.println("\nhidden");
        for(int k=0;k<hid.length;k++){
            System.out.print(String.valueOf(" "+hid[k]));
        }
        System.out.println("\nobservables");
        for(int k=0;k<obs.length;k++){
            System.out.print(String.valueOf(" "+obs[k]));
        }
        System.out.println("");
        
    }
    
    private void saveStates(int[] iZ, char[] Z){
        try {
            File file = new File("check.txt");
            FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(">seq len: "+Z.length);
            bw.newLine();
            
            for(int i=0;i<Z.length/100;i++){
                for(int j=i*100;j<i*100+100;j++){
                    bw.write(" "+Z[j]);
                }
                bw.newLine();
                for(int j=i*100;j<i*100+100;j++){
                    if(iZ[j]<10)
                        bw.write(" "+iZ[j]);
                    else
                        bw.write(""+iZ[j]);
                }
                bw.newLine();
            }
            bw.newLine();
            bw.newLine();
            
            bw.close();
        } catch (IOException e) {
                e.printStackTrace();
        }               
        
    }

    private void saveCounts(String output){
        try {
            File file = new File(output);
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Init\n");
            bw.write(String.valueOf(pi[0]));
            for(int i=1;i<pi.length;i++){
                bw.write(" "+pi[i]);
            }
            bw.newLine();
            bw.write("Transitions\n");
            for(int k=0;k<trans.length;k++){
                bw.write(String.valueOf(trans[k][0]));
                for(int i=1;i<trans[k].length;i++){
                    bw.write(" "+trans[k][i]);
                }
                bw.newLine();
            }
            bw.newLine();
            bw.newLine();
            bw.write("emissions\n");
            for(int k=0;k<em.length;k++){
                bw.write(String.valueOf(em[k][0]));
                for(int i=1;i<em[k].length;i++){
                    bw.write(" "+em[k][i]);
                }
                bw.newLine();
            }
            bw.newLine();
            bw.newLine();
            
            bw.close();
        } catch (IOException e) {
                e.printStackTrace();
        }               
        
    }    
    
}
