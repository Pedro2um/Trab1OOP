
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;


public class Partido implements Comparable<Partido>{
        private Integer votosLegenda = 0;
        private Integer votosNominal = 0;
        private String sigla = "NENHUMA";
        private Integer num = -1;
        private Set<Candidato> c = new HashSet<Candidato>();
        private Integer qtdCandidatos = 0;
        //private List<Candidato> c = new ArrayList<Candidato>();
        private Integer numCandidatosEleitos=0;
        static private final Locale lBR = new Locale("pt", "BR");
        //Public
        public Partido(String sigla, Integer num){
                this.sigla = sigla;
                this.num = num;
        }
        
        
        public HashSet<Candidato> getHashSetCandidatos(){
                return new HashSet<Candidato>(c);
        }
        public ArrayList<Candidato> getArrayListCandidatos(){
                ArrayList<Candidato> n = new ArrayList<>();
                for(var x: c){
                        if(x.getFlagNominal() != Candidato.OUTRO){
                                n.add(x);
                        }
                }
                return n;
        }
        //Ordenados por votos de legenda
        /*@Override
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
        }*/

        @Override
        public int compareTo(Partido b){
                if(this.getNumPartido() > b.getNumPartido()){
                        return 1;
                }
                else if(this.getNumPartido() < b.getNumPartido()){
                        return -1;
                }
                else{
                        return 0;
                }
        }

        @Override
        public String toString(){
                String total = NumberFormat.getIntegerInstance(lBR).format(getVotosTotal());
                String nominal = NumberFormat.getIntegerInstance(lBR).format(getVotosNominal());
                String legenda = NumberFormat.getIntegerInstance(lBR).format(getVotosLegenda());
                return   getSigla() + " - " + getNumPartido() + ", " +
                         total + " voto"+ (getVotosTotal()>1?"s":"") + " (" + nominal + " nomina" +(getVotosNominal() <= 1?"l":"is") + " e " + legenda + " de legenda), " + 
                         getNumCandidatosEleitos() + " candidato" + (getNumCandidatosEleitos()>1?"s":"")  + " eleito" + (getNumCandidatosEleitos() > 1?"s":"");
        }

        public void incVotosLegenda(Integer a){
                this.votosLegenda +=  a;
        }

        public void incVotosNominais(Integer a){
                this.votosNominal +=  a;
        }
        
        public void addCandidato(Candidato a){
                if(c.contains(a) == false){
                        qtdCandidatos++;
                        c.add(a);
                        if(a.eleito() == true){
                                this.numCandidatosEleitos++;
                        }
                }
        }

        //Getters
        public Integer getVotosLegenda(){
                return this.votosLegenda;
        }

        public Integer getVotosNominal(){
                return this.votosNominal;
        }

        public Integer getVotosTotal(){
                return getVotosLegenda() + getVotosNominal();
        }

        public Integer getQtdCandidatos(){
                return qtdCandidatos;
        }

        public Integer getNumPartido(){
                return this.num;
        }

        public Integer getNumCandidatosEleitos(){
                return this.numCandidatosEleitos;
        }

        public String getSigla(){
                return this.sigla;
        }
        
}

