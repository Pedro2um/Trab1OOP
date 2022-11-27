
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PropertyResourceBundle;
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
        static public final int B30 = 0, B40 = 1, B50 = 2, B60 = 3, U60 = 4;
        static final int SIZE = 5;
        static private int[] qtd = new int[SIZE];
        static private int qtdTotal = 0;
        static public final int MAS = 0, FEM = 1;
        static final int SIZE_GEN = 2;
        static private int[] gen = new int[SIZE_GEN];
        static private int votosValidosTotal = 0;
        static private int votosNominaisTotal = 0;
        static private int votosLegendaTotal = 0; 

        // 0 -> < 30
        // 1 -> >= 30 e < 40
        // 2 -> >= 40 e < 50
        // 3 -> >= 50 e < 60
        // 4 -> >= 60
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

        private static void solve(Map<Integer, Partido> part, Map<Integer,Candidato> cand, String data){
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

                //relatorio 8 e 11
                //da pra otimizar?, mas vou deixar para depois
                
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
                        votosValidosTotal += x.getValue().getVotosTotal();
                        votosNominaisTotal += x.getValue().getVotosNominal();
                        votosLegendaTotal += x.getValue().getVotosLegenda();
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
                
                //relatorio 9 e 10
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                LocalDate ld = LocalDate.parse(data, formatter);

                for(int i = 0; i < SIZE; i++){
                        qtd[i] = 0;
                }
                for(int i = 0; i < SIZE_GEN; i++){
                        gen[i] = 0;
                }

                for(var x: candEleitos){
                        long i = getIdade(ld, x.getNascimento());
                        if( i < 30){
                                qtd[B30]++;
                        }
                        else if( i < 40){
                                qtd[B40]++;
                        }
                        else if( i < 50){
                                qtd[B50]++;
                        }
                        else if( i < 60){
                                qtd[B60]++;
                        }
                        else {
                                qtd[U60]++;
                        }
                        
                        if(x.getGenero().compareTo('M') == 0){
                                gen[MAS]++;
                                System.out.println(x.getGenero());
                        }
                        else if(x.getGenero().compareTo('F') == 0){
                                gen[FEM]++;
                                System.out.println(x.getGenero());
                        }
                } 
                qtdTotal = candEleitos.size();
                return;
        }
        
        
       
        private static int getIdade(LocalDate ld, LocalDate n) {
                return Period.between(n, ld).getYears();  
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

                //relatorio 9
                //TODO: mudar para sout
                System.out.println("\nEleitos, por faixa etária (na data da eleição):");
                System.out.printf("%s %d (%.2f%%)\n", "      Idade < 30:", qtd[B30], proporcao(qtd[B30], qtdTotal));
                System.out.printf("%s %d (%.2f%%)\n", "30 <= Idade < 40:", qtd[B40], proporcao(qtd[B40], qtdTotal));
                System.out.printf("%s %d (%.2f%%)\n", "40 <= Idade < 50:", qtd[B50], proporcao(qtd[B50], qtdTotal));
                System.out.printf("%s %d (%.2f%%)\n", "50 <= Idade < 60:", qtd[B60], proporcao(qtd[B60], qtdTotal));
                System.out.printf("%s %d (%.2f%%)\n", "60 <= Idade     :", qtd[U60], proporcao(qtd[U60], qtdTotal));
                
                //relatorio 10
                //TODO: mudar para sout
                System.out.println("\nEleitos, por gênero:");
                System.out.printf("%s: %d (%.2f%%)\n", "Feminino", gen[FEM], proporcao(gen[FEM], qtdTotal));
                System.out.printf("%s: %d (%.2f%%)\n", "Masculino", gen[MAS], proporcao(gen[MAS], qtdTotal));

                //relatorio 11
                //TODO: mudar formatt
                System.out.println("Total de votos válidos: " + Integer.toString(votosValidosTotal));
                System.out.println("Total de votos nominais: " + Integer.toString(votosNominaisTotal) + " " + Double.toString(proporcao(votosNominaisTotal, votosValidosTotal)));
                System.out.println("Total de votos de legenda: " + Integer.toString(votosLegendaTotal) + " " + Double.toString(proporcao(votosLegendaTotal, votosValidosTotal)));
        }


        

        private static float proporcao(int x, int t) {
                double ans = (double)x / (double)t; 
                return (float)(ans*100);
        }

        public static void main(String[] args) throws Exception{  
                Map<Integer,Partido> part = new HashMap<>(); //checar se o partido existe ou nao, a partir do numero
                Map<Integer,Candidato> cand = new HashMap<>(); //precisamos atualizar os votos dos candidatos
                read_input(part, partRanking, cand, candEleitos, args[0], args[1], args[2], args[3]);
                solve(part, cand, args[3]);
                write_output(part, args[0], args[3]);
        }
                
}