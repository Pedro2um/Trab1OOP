import java.util.ArrayList;
import java.util.List;

public class Partido implements Comparable<Partido>{
        private Integer votosLegenda;
        private Integer votosNominais;
        private String sigla;
        private Integer num;
        private List<Candidato> c = new ArrayList<Candidato>();
        private Integer numCandidatosEleitos;
        //Public
        public Partido(String sigla, Integer num){
                votosLegenda = 0;
                votosNominais = 0;
                this.sigla = sigla;
                this.num = num;
                numCandidatosEleitos = 0;
        }
        
        
        public ArrayList<Candidato> getCandidatos(){
                return new ArrayList<Candidato>(c);
        }
        
        //Ordenados por votos de legenda
        @Override
        public int compareTo(Partido b){
                if(votosLegenda > b.getVotosLegenda()){
                        return 1;
                }
                else if(votosLegenda < b.getVotosLegenda()){
                        return -1;
                }
                else{
                        return 0;
                }
        }

        @Override
        public String toString(){
                return   getSigla() + " - " + getNumPartido() + ", " +
                         getVotosTotal() + " votos (" + getVotosNominais() + " nominais e " + getVotosLegenda() + " de legenda), " + 
                         getNumCandidatosEleitos() + " candidatos eleitos";
        }

        public void incVotosLegenda(Integer a){
                votosLegenda +=  a;
        }

        //Restrição: usar somente quando candidato não estiver na lista do partido
        public void addCandidato(Candidato a){
                c.add(a);
                votosNominais += a.getVotos();
                if(a.eleito()){
                        numCandidatosEleitos++;
                }
        }

        //Getters
        public Integer getVotosLegenda(){
                return votosLegenda;
        }

        public Integer getVotosNominais(){
                return votosNominais;
        }

        public Integer getVotosTotal(){
                return getVotosLegenda() + getVotosNominais();
        }

        public Integer getNumPartido(){
                return num;
        }

        public Integer getNumCandidatosEleitos(){
                return numCandidatosEleitos;
        }

        public String getSigla(){
                return sigla;
        }
        
}
