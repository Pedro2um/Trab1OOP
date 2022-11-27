
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

public class Candidato implements Comparable<Candidato>{
        
        private String          nome="";                           //Nome verdadeiro
        private Character       genero='N';
        private Character       cargo='N'; //F para federal e E para estadual
        private LocalDate       nascimento;  
        private Partido         partido;
        private Integer         numero = -1;
        private Integer         numero_federacao = -1;
        private Integer         votos = 0;
        private Boolean         eleito = false; 
        private Boolean         flagNominal = false; //se falso , então votos direcionados para o partido
        static private final Locale lBR = new Locale("pt", "BR");

        public Candidato(Character cargo, String nome, Character genero, 
                        LocalDate nascimento, Partido partido, Integer numero, 
                        Integer numero_federacao, Integer votos, Boolean nominal, 
                        Boolean eleito){
                this.cargo = cargo;
                this.nome = nome;
                this.genero = genero;
                this.nascimento = nascimento;
                this.partido = partido;
                this.numero = numero;
                this.numero_federacao = numero_federacao;
                this.votos = votos;
                this.flagNominal = nominal;
                this.eleito = eleito;
        }

        //temporário, precisa colocar ponto nos números
        @Override
        public String toString(){
                String s = NumberFormat.getIntegerInstance(lBR).format(votos);
                return nome.toUpperCase() + " " + "(" + getPartido().getSigla().toUpperCase() + ", " + s + " votos)"; 
        }

        //Ordenados por voto nominal
        @Override
        public int compareTo(Candidato b){
                if(votos > b.getVotos()){
                        return 1;
                }
                else if(votos < b.getVotos()){
                        return -1;
                }
                else{
                        return 0;
                }
        }

        //Getters and Setters

        public LocalDate getNascimento() {
                return nascimento;
        }  

        public String getNome() {
                return this.nome;
        }

        /*
          M -> MASCULINO e F -> FEMININO
        */
        public Character getGenero(){
                return this.genero;
        }

        public Character getCargo(){
                return this.cargo;
        }

        public Partido getPartido() {
                return this.partido;
        }

        public Integer getNumero(){
                return this.numero;
        }

        public Integer getNumeroFederacao(){
                return this.numero_federacao;
        }

        public void incVotos(Integer v){
                this.votos += v;
        }


        public Integer getVotos(){
                return this.votos;
        }
        public String getCandNumVoto(){
                String s = NumberFormat.getIntegerInstance(lBR).format(votos);
                return nome.toUpperCase() + " (" + numero.toString() + ", " + s + " votos)";
        }

        public Boolean getFlagNominal(){
                return this.flagNominal;
        }

        public Boolean eleito(){
                return this.eleito;
        }
}
