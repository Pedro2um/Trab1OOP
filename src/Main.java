
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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


        private static void read_input(Map<Integer, Partido> part, Map<Integer,Candidato> cand, List<Candidato> candEleitos,String tipo, String fcand, String fvotos, String data){
                String enconding = "ISO-8859-1";
                Leitura l = new Leitura();

                boolean f = false;
                if(tipo.compareTo(Federal) == 0){
                        f = true;
                }

                l.readCand(f, part, cand, candEleitos,fcand, enconding);
                l.readVotos(f, part, cand, fvotos,enconding);
        }

        private static void solve(Map<Integer, Partido> part, Map<Integer,Candidato> cand){
                candEleitos.sort((Candidato a, Candidato b)->  -1*Integer.valueOf(a.getVotos()).compareTo(b.getVotos()));
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
                return;
        }
        

        private static void write_output(String tipo, String data){
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

                System.out.println("Votação dos partidos e número de candidatos eleitos:");

                /*System.out.println("PARTIDOS:\n");

                List<Partido> p = new ArrayList<>(part.values());
                
                Collections.sort(p, new Comparator<Partido>() {
                        public int compare(Partido a, Partido b){
                                //-1 for reverse order
                                return ( -1 * Integer.valueOf(a.getNumCandidatosEleitos()).compareTo(b.getNumCandidatosEleitos()) );
                        }
                });

                List<Candidato> c = new ArrayList<>(cand.values());
                
                Collections.sort(c, new Comparator<Candidato>() {
                        public int compare(Candidato a, Candidato b){
                                return Integer.valueOf(a.getVotos()).compareTo(b.getVotos());
                        } 
                });

                

                System.out.println("PARTIDOS - " + p.size() + "\n");
                for(var x: p){
                        System.out.println(x);
                }

                System.out.println("CANDIDATOS - " + c.size() + "\n");
                for(var y: c){
                        System.out.println(y);
                }
                */
        }


        public static void main(String[] args) throws Exception{  
                Map<Integer,Partido> part = new HashMap<>(); //checar se o partido existe ou nao, a partir do numero
                Map<Integer,Candidato> cand = new HashMap<>(); //precisamos atualizar os votos dos candidatos
                read_input(part, cand, candEleitos, args[0], args[1], args[2], args[3]);
                solve(part, cand);
                write_output(args[0], args[3]);
        }
                
}

      /*  Partido OzCria = new Partido("OzCria", 27);

                LocalDate temp = LocalDate.of(2002, 12, 19);

                Candidato ped = new Candidato("Pedro", 'M', temp, OzCria, 2791, 10000, true);

                OzCria.addCandidato(ped);

                temp = LocalDate.of(2001, 3, 12);
                Candidato mar = new Candidato("Marlon", 'M', temp, OzCria, 2792, 20000, false);

                OzCria.addCandidato(mar);

                temp = LocalDate.of(2001, 4, 10);
                Candidato mat = new Candidato("Matheus", 'M', temp, OzCria, 2793,30000, true);

                OzCria.addCandidato(mat);

                OzCria.incVotosLegenda(2000);
                
                System.out.println(OzCria);

                for (var x : OzCria.getCandidatos()) {
                        System.out.println(x + "\n");
                }
        */