/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package train_by_count_3st;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author lucas
 */
public class Train_by_count_3st {
    String[] obs;
    char[] hid = new char[]{'N', 'C', 'I'};
    HashMap<String, Integer> hidden = new HashMap<String, Integer>(){{
        put("N",0);put("C", 1);put("I", 2);}};
    HashMap<String, Integer> observables = new HashMap<>();
    HashMap<String, String> map6_to_3 = new HashMap<String,String>(){{
        put("S","C");put("B","C");put("M","C");put("E","C");put("N","N");put("I","I");}};
    
    int[] pi;
    int[][] transitions;
    int[][] emissions;
    ArrayList<String> seqs;
    HashMap<String, Integer> names = new HashMap<>();
    String[] states;
    String param = "../../parameters/param_3state.txt";
    String seqFile = "../../Validation/Genie/parts/0.fa";
    String stFile = "../../Validation/Genie/parts/h0.fa";
    String countFile = "../../Validation/Genie/countsJava.txt";
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        if(args.length==3){
            new Train_by_count_3st().Train_by_count_3st(args);
        }
        else{
            System.out.println("3 parameters required!!!");
            System.exit(0);
        }
    }
    
    public void Train_by_count_3st(String[] args){
        param = args[0];
        seqFile = args[1];
        stFile = args[2];
        readMM(param);
        readObs(seqFile);
        readStates(stFile);
        count();
        printCounts();
//        saveCounts(countFile);
    }
     
    private void readMM(String input){
        try{
            BufferedReader br1 = new BufferedReader(new FileReader(input));
            String line = br1.readLine();
            while(line!=null){
//                if(line.contains("hidden")){
//                    String[] l = br1.readLine().split(" ");
//                    hid = new char[l.length];
//                    for(int i=0;i<l.length;i++){
//                        hidden.put(l[i], i);
//                        hid[i] = l[i].charAt(0);
//                    }
//                }
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
        System.out.println("Reading input from: "+ input);

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
    
    private void count(){
        pi = new int[hid.length];
        transitions = new int[hid.length][hid.length];
        emissions = new int[hid.length][obs.length];

        for(int i=0;i<seqs.size();i++){
            pi[hidden.get(map6_to_3.get(states[i].substring(0,1)))]++;
            emissions[hidden.get(map6_to_3.get(states[i].substring(0,1)))][observables.get(seqs.get(i).substring(0, 1))]++;
            
            for(int j=1;j<states[i].length();j++){
                emissions[hidden.get(map6_to_3.get(String.valueOf(states[i].charAt(j))))][observables.get(String.valueOf(seqs.get(i).charAt(j)))]++;
                transitions[hidden.get(map6_to_3.get(String.valueOf(states[i].charAt(j-1))))][hidden.get(map6_to_3.get(String.valueOf(states[i].charAt(j))))]++;

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
        for(int k=0;k<transitions.length;k++){
            System.out.print(String.valueOf(" "+transitions[k][0]));
            for(int i=1;i<transitions[k].length;i++){
                System.out.print(" "+transitions[k][i]);
            }
        }
        System.out.println("\nemissions");
        for(int k=0;k<emissions.length;k++){
            System.out.print(String.valueOf(" "+emissions[k][0]));
            for(int i=1;i<emissions[k].length;i++){
                System.out.print(" "+emissions[k][i]);
            }
        }
        System.out.println("");
        
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
            for(int k=0;k<transitions.length;k++){
                bw.write(String.valueOf(transitions[k][0]));
                for(int i=1;i<transitions[k].length;i++){
                    bw.write(" "+transitions[k][i]);
                }
                bw.newLine();
            }
            bw.newLine();
            bw.newLine();
            bw.write("emissions\n");
            for(int k=0;k<emissions.length;k++){
                bw.write(String.valueOf(emissions[k][0]));
                for(int i=1;i<emissions[k].length;i++){
                    bw.write(" "+emissions[k][i]);
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
