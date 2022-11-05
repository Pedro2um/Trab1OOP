import java.time.LocalDate;

public class Main{

        public static void main(String[] args){
                Partido OzCria = new Partido("OzCria", 27);

                LocalDate temp = LocalDate.of(2002, 12, 19);

                Candidato ped = new Candidato("Pedro", 'M', temp, OzCria, 2791, 10000, false);

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
        }
        
}