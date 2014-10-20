package lessmoon.nlp.util;

import lessmoon.nlp.adt.*;
import lessmoon.nlp.statistic.*;
import lessmoon.nlp.term.*;

import java.util.*;


abstract class TermGraphEdgeBasic {
    public abstract int getWeight();
    public abstract DataNode getTerm();
}

abstract class MainPathNodeBasic {
    public abstract void addEdge(TermGraphEdgeBasic e);
}


class TermGraphEdge extends TermGraphEdgeBasic {
    final int         weight;
    MainPathNodeBasic nextnode;
    final DataNode    term;

    public TermGraphEdge(DataNode t,int w,MainPathNodeBasic next){
        term = t;
        weight = w;
        nextnode = next;
    }

    public TermGraphEdge(DataNode t,int w){
        term = t;
        weight = w;
        nextnode = null;
    }

    public void setNext(MainPathNodeBasic next){
        nextnode = next;
    }
    
    public MainPathNodeBasic getNext(){
        return nextnode;
    }

    public int getWeight(){
        return weight;
    }
    
    public DataNode getTerm(){
        return term;
    }
    
    public String toString(){
        return "" + term + ":" + weight + "->" + nextnode;
    }
}

class MainPathNode extends MainPathNodeBasic {
    ArrayList<TermGraphEdge> edgelist = new ArrayList<TermGraphEdge>();

    public MainPathNode(){
    }

    public void addEdge(TermGraphEdgeBasic e){
        edgelist.add((TermGraphEdge)e);
    }
    
    public ArrayList<TermGraphEdge> getEdgeList(){
        return edgelist;
    }
    
    public String toString(){
        return edgelist.toString();
    }
}

class TermPathNode {
    DataNode term ;
    TermPathNode next;
    //int weight;
    
    TermPathNode(DataNode t){
        term = t;
        next = null;
    }
    
    TermPathNode(DataNode t,TermPathNode n){
        term = t;
        next = n;
    }

    TermPathNode getNext(){
        return next;
    }
    
    DataNode getTerm(){
        return term;
    }
/*
    void setWeight(int w){
        weight = w;
    }
    
    int getWeight(){
        return weight;
    }
*/   
    public String toString(){
        return term.toString() + (next != null?next.toString():"");
    }
}

class MaxPathGenerator {
    Map<MainPathNodeBasic,Integer> pimap = new HashMap<MainPathNodeBasic,Integer>();
    ArrayList<TermPathNode> ipmap = new ArrayList<TermPathNode>();
    int[] pweights;

    MaxPathGenerator(){
        
    }
    
    int countNumber(MainPathNodeBasic s,MainPathNodeBasic d){
        parseGraph(s,d);/*Give each node a number to identify them*/
        return pimap.size();
    }

    void parseGraph(MainPathNodeBasic s,MainPathNodeBasic d){
        assert(s != null):"from node is null";
        assert(d != null):"to node is null";
        pimap.put(s,pimap.size());
        ipmap.add(null);
        if(s == d){
            return;
        } else {
            for(TermGraphEdge e : ((MainPathNode)s).getEdgeList()){
                if(pimap.get(e.getNext()) == null){
                    parseGraph(e.getNext(),d);
                }
            }
        }
    }
    
    public TermPathNode maxPath(MainPathNodeBasic s,MainPathNodeBasic d){
        pweights = new int[countNumber(s,d)];
        for(int i = 0 ; i < pweights.length;i++){
            pweights[i] = 1;
        }

        return getPath(s,d);
    }

    TermPathNode getPath(MainPathNodeBasic s,MainPathNodeBasic d){
        if(s == d){
            return null;
        }
        int max = 1;
        int idx_s = pimap.get(s).intValue();
        assert(idx_s >= 0 && idx_s <  pimap.size()):"out of edge!" + idx_s;
        TermPathNode mid = null;
        TermGraphEdge bestedge = null;
        for(TermGraphEdge e : ((MainPathNode)s).getEdgeList()){
            int idx = pimap.get(e.getNext()).intValue();

            if(pweights[idx] > 0){/*all the weight should <= 0*/
                ipmap.set(idx,getPath(e.getNext(),d));
            }

            if(max > 0 || max <=  pweights[idx] + e.getWeight()){
                max = pweights[idx] + e.getWeight() ;
                mid = ipmap.get(idx);
                bestedge = e;
            }
            //System.out.println(e);
        }
        //System.out.println(s + "\n");
        assert(bestedge != null):"bestedge is null!";
        pweights[idx_s] = max;
        
        return new TermPathNode(bestedge.getTerm(),mid);
    }
}

public class TermStreamGraph {
    ArrayList< MainPathNode > naturalterms;
    MainPathNode  end,start;

    public TermStreamGraph(final String sentence,TermWeightGetter twg){
        naturalterms = new ArrayList< MainPathNode >();

        naturalterms.add(end = start = new MainPathNode());
        MainPathNode tmp = start;
        for(char c : sentence.toCharArray()){
            
            /*TODO:Choose the single-letter-term info with MAX weight*/
            TermEntry e = twg.getTermBestWeight(String.valueOf(c));
            naturalterms.add(end = new MainPathNode());
            tmp.addEdge(new TermGraphEdge(e.dn,e.weight,end));
            tmp = end;
        }
        /*
         * Insert every possible term
         */
        
        for(int p = 0; p < sentence.length() - 1;p++){
            for( int i = 2; i <= sentence.length() - p ; i++){
                String subterm = sentence.substring(p,p + i);
                TermEntry e = twg.getTermBestWeight(subterm);
                if(e.weight > twg.MIN_LEVEL) {/*If it is valid term*/
                    /*Possible term*/
                    //System.out.println("Possible term:" + e.dn);
                    addTerm(p,e.dn,e.weight);
                }
            }
        }
        //start.addNextNode(end);
    }
    
    private void addTerm(final int pos,DataNode d,final int weight){
        naturalterms.get(pos).addEdge(new TermGraphEdge(d,weight,naturalterms.get(d.term.length() + pos)));
    }

    public TermPathNode getBestPath(){
        return (new MaxPathGenerator()).maxPath(start,end);
    }
    
    public static void main(String[] args) throws Exception{
        Dictionary dic = new Dictionary();
        dic.LoadData(new InputStream(){
            java.io.BufferedReader bf = new java.io.BufferedReader(new java.io.FileReader("dic.txt"));
            public int getch() throws Exception {
                return bf.read();
            }
        });

        DataReader datareader = new DataReader(new InputStream(){
            java.io.BufferedReader bf = new java.io.BufferedReader( new java.io.FileReader("data.txt"));
            public int getch() throws Exception{
                return bf.read();
            }
        });

        TermTrieTree ttt = new TermTrieTree();
 
        DataNode n = null;
        while((n = datareader.readDataNode()) != null){
            ttt.insert(n.term,n.type);
        }
        TermStreamGraph tsg = null;
        Scanner scanner = new Scanner(System.in);
        TermWeightGetter twg = new TermWeightGetter(ttt,dic);
        while(true){
            System.out.println((new TermStreamGraph(scanner.nextLine(),twg)).getBestPath());
        }
    }
}