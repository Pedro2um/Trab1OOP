
import java.util.HashMap;
import java.util.Map;

public class Main{
        
        
        private static void read_input(Map<Integer, Partido> part, Map<Integer,Candidato> cand, String fcand, String fvotos, String data){
                String enconding = "ISO-8859-1";
                Leitura l = new Leitura();
                l.read_cand(part, cand, fcand, enconding);
                l.read_votos(part, cand, fvotos,enconding);
        }

        private static void solve(Map<Integer, Partido> part, Map<Integer,Candidato> cand){

        }
        

        private static void write_output(){

        }

        

        public static void main(String[] args) throws Exception{  
                Map<Integer,Partido> part = new HashMap<>(); //checar se o partido existe ou nao, a partir do numero
                Map<Integer,Candidato> cand = new HashMap<>(); //precisamos atualizar os votos dos candidatos
                read_input(part, cand, args[1], args[2], args[3]);
                solve(part, cand);
                write_output();
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