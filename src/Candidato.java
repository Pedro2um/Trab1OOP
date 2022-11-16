import java.time.LocalDate;

public class Candidato implements Comparable<Candidato>{
        
        private String          nome="";                           //Nome verdadeiro
        private Character       genero='N';
        private Character       cargo='N'; //F para federal e E para estadual
        private LocalDate       nascimento;  
        private Partido         partido;
        private Integer         numero = -1;
        private Integer         numero_federacao = -2;
        private Integer         votos = 0;
        private Boolean         eleito = false; 

        public Candidato(Character cargo, String nome, Character genero, LocalDate nascimento, Partido partido, Integer numero, Integer numero_federacao, Integer votos, Boolean eleito){
                this.cargo = cargo;
                this.nome = nome;
                this.genero = genero;
                this.nascimento = nascimento;
                this.partido = partido;
                this.numero = numero;
                this.numero_federacao = numero_federacao;
                this.votos = votos;
                this.eleito = eleito;
        }

        //temporário, precisa colocar ponto nos números
        @Override
        public String toString(){
                return nome.toUpperCase() + " " + "(" + getPartido().getSigla().toUpperCase() + ", " + votos + " votos)"; 
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
                return nome;
        }

        public Character getGenero(){
                return genero;
        }

        public Character getCargo(){
                return cargo;
        }

        public Partido getPartido() {
                return partido;
        }

        public Integer getNumero(){
                return numero;
        }

        public Integer getNumeroFederacao(){
                return numero_federacao;
        }

        public void setVotos(Integer votos){
                if(this.votos == 0) this.votos = votos;
        }
        
        public Integer getVotos(){
                return votos;
        }

        public Boolean eleito(){
                return eleito;
        }
}
