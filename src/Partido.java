import java.util.HashSet;

public class Partido implements Comparable<Partido>{
        private Integer votosLegenda = 0;
        private Integer votosNominais = 0;
        private String sigla = "NENHUMA";
        private Integer num = -1;
        private HashSet<Candidato> c = new HashSet<Candidato>();
        //private List<Candidato> c = new ArrayList<Candidato>();
        private Integer numCandidatosEleitos=0;
        //Public
        public Partido(String sigla, Integer num){
                votosLegenda = 0;
                votosNominais = 0;
                this.sigla = sigla;
                this.num = num;
                numCandidatosEleitos = 0;
        }
        
        
        public HashSet<Candidato> getCandidatos(){
                return new HashSet<Candidato>(c);
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
                return   getSigla().toUpperCase() + " - " + getNumPartido() + ", " +
                         getVotosTotal() + " votos (" + getVotosNominais() + " nominais e " + getVotosLegenda() + " de legenda), " + 
                         getNumCandidatosEleitos() + " candidatos eleitos";
        }

        public void incVotosLegenda(Integer a){
                votosLegenda +=  a;
        }

        
        public void addCandidato(Candidato a){
                if(c.contains(a) == false){
                        c.add(a);
                        votosNominais += a.getVotos();
                        if(a.eleito() == true){
                                numCandidatosEleitos++;
                        }
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
