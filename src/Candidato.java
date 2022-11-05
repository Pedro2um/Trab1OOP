import java.text.DecimalFormat;
import java.time.LocalDate;

public class Candidato {
        

        private Integer         CD_CARGO = -1;                       // 6 ou 7
        private Boolean         CD_DETALHE_SITUACAO_CAND = false;       // candidatura deferida ou não
        private Integer         NR_CANDIDATO = -1;
        private String          NM_URNA_CANDIDATO = "NEGATIVO";
        private Integer         NR_PARTIDO = -1;
        private String          SG_PARTIDO = "NENHUMA";
        private Integer         NR_FEDERACAO = -1;                   // -1 if partido isolado (não participa de federação)
        private LocalDate       DT_NASCIMENTO;                  // data de nascimento do candidato no formato dd/mm/aaaa;
        private Integer         CD_SIT_TOT_TURNO = -1;               //
        private Integer         CD_GENERO =-1;                      //Masculino ou Feminino

        private String          nome;                           //Nome verdadeiro
        private Character       genero; 
        private LocalDate       nascimento;  
        private Partido         partido;
        private Integer         numero;
        private Integer         votos;
        private Boolean         eleito; 

        public Candidato(String nome, Character genero, LocalDate nascimento, Partido partido, Integer numero, Integer votos, Boolean eleito){
                this.nome = nome;
                this.genero = genero;
                this.nascimento = nascimento;
                this.partido = partido;
                this.votos = votos;
                this.eleito = eleito;
        }

        @Override
        public String toString(){
                // Ex: Lula(PT, 60345999 votos)
                //@TODO :falta resolver problema da pontuação no número
                return nome + " (" + partido.getSigla() + ", " + votos + " votos)";  
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

        public Partido getPartido() {
                return partido;
        }
        
        public Integer getVotos(){
                return votos;
        }

        public Boolean eleito(){
                return eleito;
        }
}
