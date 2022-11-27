
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class Main{
        
        static final private String Federal =  "--federal";

        static private List<Candidato> candEleitos = new ArrayList<>();
        static private List<Candidato> candMaisVotados = new ArrayList<>();

        //idx no ranking de mais votados
        static private Map<Integer,Integer> m = new HashMap<>();

        //static private List<Integer> idxSeriamEleitos = new ArrayList<>();
        static private List<Candidato> candSeriamEleitos = new ArrayList<>();

        //static private List<Integer> idxBeneficiados = new ArrayList<>();
        static private List<Candidato> candBeneficiados = new ArrayList<>();

        static private List<Partido> partRanking = new ArrayList<>();

        static private Map<Partido,ArrayList<Candidato>> partCandHiLow = new TreeMap<>();
        static protected Map< Partido, ArrayList<Candidato>> partCHL = new HashMap<>();
       

        private static void read_input(Map<Integer, Partido> part, List<Partido> partRanking, Map<Integer,Candidato> cand, List<Candidato> candEleitos,String tipo, String fcand, String fvotos, String data){
                String enconding = "ISO-8859-1";
                Leitura l = new Leitura();

                boolean f = false;
                if(tipo.compareTo(Federal) == 0){
                        f = true;
                }

                l.readCand(f, part, partRanking,cand, candEleitos,fcand, enconding);
                l.readVotos(f, part, partRanking,cand, fvotos,enconding);
        }

        private static void solve(Map<Integer, Partido> part, Map<Integer,Candidato> cand){
                candEleitos.sort((Candidato a, Candidato b)->  (        -1*Integer.valueOf(a.getVotos()).compareTo(b.getVotos())==0?
                                                                        -1*a.getNascimento().compareTo(b.getNascimento()): 
                                                                        -1*Integer.valueOf(a.getVotos()).compareTo(b.getVotos())) 
                );
                candMaisVotados.addAll(cand.values());
                candMaisVotados.sort((Candidato a, Candidato b)->  -1*Integer.valueOf(a.getVotos()).compareTo(b.getVotos()));

                
                //acelerar verificações
                Set<Candidato> candEleitosTemp = new HashSet<>(candEleitos);
                Set<Candidato> candMaisVotadosTemp = new HashSet<>();
                int cnt = 1; 
                //ranking de mais votados
                for(var x: candMaisVotados){
                        m.put(x.getNumero(), cnt);
                        candMaisVotadosTemp.add(x);
                        cnt++;
                }
                cnt = 1; 
                //relatorio 4
                for(var x: candMaisVotados){
                        if(candEleitosTemp.contains(x)==false){
                                candSeriamEleitos.add(x);
                            
                        }
                        cnt++;
                        if(cnt > candEleitos.size()) break;
                        
                }
                //relatorio 5
                for(var x: candEleitos){
                        if(m.get(x.getNumero()) > candEleitos.size()){
                                candBeneficiados.add(x);
                        }
                }

                //relatorio 6
                partRanking.sort((Partido a, Partido b)->       -1*Integer.valueOf(a.getVotosTotal()).compareTo(b.getVotosTotal()) == 0?
                                                                   Integer.valueOf(a.getNumPartido()).compareTo(b.getNumPartido()):
                                                                -1*Integer.valueOf(a.getVotosTotal()).compareTo(b.getVotosTotal())
                );

                //relatorio 8
                //da pra otimizar, mas vou deixar para depois
                
                for(var x: part.entrySet()){
                        //apenas partido com algum candidato
                        if(x.getValue().getVotosTotal() != 0) {
                                ArrayList<Candidato> temp = x.getValue().getArrayListCandidatos();
                                temp.sort((Candidato a, Candidato b) ->         -1*Integer.valueOf(a.getVotos()).compareTo(b.getVotos())==0?
                                                                                -1*a.getNascimento().compareTo(b.getNascimento()): 
                                                                                -1*Integer.valueOf(a.getVotos()).compareTo(b.getVotos()));
                                ArrayList<Candidato> temp2 = new ArrayList<>();
                                temp2.add(temp.get(0));//max
                                temp2.add(temp.get(temp.size()-1));//min 
                                partCHL.put(x.getValue(), temp2);
                                
                        }
                }

                partCandHiLow = new TreeMap<Partido, ArrayList<Candidato>>(
                        new Comparator<Partido>() {
                                @Override
                                public int compare(Partido a, Partido b){
                                        long x = partCHL.get(a).get(0).getVotos();
                                        long y = partCHL.get(b).get(0).getVotos();
                                        if( x < y ){
                                                return 1;
                                        }
                                        else if( x > y){
                                                return -1;
                                        }
                                        else {
                                                if( a.getNumPartido() < b.getNumPartido() ) {
                                                        return 1;
                                                }
                                                else if( a.getNumPartido() > b.getNumPartido()){
                                                        return -1;
                                                }
                                                else {
                                                        return 0;
                                                }
                                                
                                        }
                                }
                        }
                );

                partCandHiLow.putAll(partCHL);
                

                return;
        }
        
        
       
        private static void write_output(Map<Integer,Partido> part, String tipo, String data){
                System.out.println("Número de vagas = " + candEleitos.size() + "\n");//relatorio 1
                String dep = (tipo.compareTo(Federal))==0?"federais":"estaduais";
                System.out.println("Deputados " + dep + " eleitos:");

                //relatorio 2
                Integer cnt = 1;
                for(var x: candEleitos){
                        System.out.println( Integer.toString(cnt) + " - " +  (x.getNumeroFederacao() != -1? "*":"") + x);
                        cnt = cnt + 1;
                }

                System.out.println("\n");

                //relatorio 3
                System.out.println("Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
                cnt = 1;
                
                for(var x: candMaisVotados){
                        System.out.println( Integer.toString(cnt) + " - " +  (x.getNumeroFederacao() != -1? "*":"") + x);
                        cnt = cnt + 1;
                        if(cnt > candEleitos.size()) break;
                }

                //relatorio 4
                System.out.println("\nTeriam sido eleitos se a votação fosse majoritária, e não foram eleitos:\n(com sua posição no ranking de mais votados)");

                //cnt = 1;
               // int i = 1;
                for(var x: candSeriamEleitos){ 
                        if( m.get(x.getNumero()) > candEleitos.size()) break;
                        System.out.println( m.get(x.getNumero()) + " - " +  (x.getNumeroFederacao() != -1? "*":"") + x);
                       // i++; 
                       
                }

                //relatorio 5
                System.out.println("\nEleitos, que se beneficiaram do sistema proporcional:\n(com sua posição no ranking de mais votados)");

                for(var x: candBeneficiados){  
                        System.out.println( m.get(x.getNumero()) + " - " +  (x.getNumeroFederacao() != -1? "*":"") + x); 
                }


                cnt = 1;
                //relatorio 6
                System.out.println("\nVotação dos partidos e número de candidatos eleitos:");
                for(var x: partRanking){
                        System.out.println(Integer.toString(cnt) + " - " + x);
                        cnt++;
                }
                
                //relatorio 8
                System.out.println("\nPrimeiro e último colocados de cada partido:"); 
                cnt = 1;
                for(var x: partCandHiLow.entrySet()){
                        System.out.println(
                                Integer.toString(cnt) + " - " + x.getKey().getSigla() + " - " + x.getKey().getNumPartido() 
                                + " " + x.getValue().get(0).getCandNumVoto() + " / " + x.getValue().get(1).getCandNumVoto());
                        cnt++;
                }

        }


        

        public static void main(String[] args) throws Exception{  
                Map<Integer,Partido> part = new HashMap<>(); //checar se o partido existe ou nao, a partir do numero
                Map<Integer,Candidato> cand = new HashMap<>(); //precisamos atualizar os votos dos candidatos
                read_input(part, partRanking, cand, candEleitos, args[0], args[1], args[2], args[3]);
                solve(part, cand);
                write_output(part, args[0], args[3]);
        }
                
}