import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Main{
        public static void main(String[] args) throws Exception{  
                Map<Integer,Partido> part = new HashMap<>(); 
                Map<Integer,Candidato> cand = new HashMap<>(); 
                Locale localeBR = new Locale("pt", "BR");
                Eleicao eleicao = new Eleicao(localeBR, args[3]);
                eleicao.readInput(part, cand,  args[0], args[1], args[2], args[3]);
                eleicao.solve(part, cand, args[3]);
                eleicao.writeOutput(part, args[0], args[3]);
        }          
}