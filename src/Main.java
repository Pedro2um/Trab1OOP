
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Main{
        //Colunas das informacoes no arquivo csv - 1
        static final private Integer CD_CARGO = 13; //14 - 1
        static final private Integer CD_DETALHE_SITUACAO_CAND = 24; //25 - 1
        static final private Integer NR_CANDIDATO = 16; // 17 - 1
        static final private Integer NM_URNA_CANDIDATO = 18; //19 - 1
        static final private Integer NR_PARTIDO = 27; //28 - 1
        static final private Integer SG_PARTIDO = 28; //29 - 1
        static final private Integer NR_FEDERACAO = 30; //31 - 1
        static final private Integer DT_NASCIMENTO = 42; //43 - 1
        static final private Integer CD_SIT_TOT_TURNO = 56; //57 - 1
        static final private Integer CD_GENERO = 45; //46 - 1

        //Constantes  relevantes para o trab
        static final Integer FEDERAL = 6; // F in implementation
        static final Integer ESTADUAL = 7; // E in implementation
        static final Integer[] DEFERIDO = {2, 16};
        static final Integer ISOLADO = -1;
        static final Integer[] ELEITO = {2, 3};
        static final String MASCULINO = "2", FEMININO = "4";

        //Colunas irrelevantes
        static final Integer[] IGNORAR_NR_VOTAVEL = {95,96,97,98}; 
        
        //Informacoes
        //
        static HashMap<Integer,Partido> part; //checar se o partido existe ou nao, a partir do numero
        static HashMap<Integer,Candidato> cand; //precisamos atualizar os votos dos candidatos
        static String enconding = "ISO-8859-1";

        public static void read_input(String fcand, String fvotos, String data){
                FileInputStream c;
                //FileInputStream v;
                BufferedReader inputc;
               // BufferedReader inputv;
                String line = "";
                //ler candidatos
                try{    
                        c = new FileInputStream(fcand);
                        inputc = new BufferedReader(new InputStreamReader(c, enconding));
                        line = inputc.readLine();
                        String rep = "\"";
                        String rrep = "";
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

                        while((line = inputc.readLine()) != null){
                                line = line.replace(rep,rrep); 
                                
                                String[] row = line.split(";");

                                Integer situacao = Integer.parseInt(row[CD_DETALHE_SITUACAO_CAND]);
                                if(situacao != DEFERIDO[0] && situacao != DEFERIDO[1]){
                                        //candidatura indeferida
                                        continue;
                                }
                                
                                
                                Character cargo = Integer.parseInt(row[CD_CARGO]) == FEDERAL ? 'F' : 'E';
                                //Informacoes do candidato atual
                                String nome = row[NM_URNA_CANDIDATO];
                                Character genero = (row[CD_GENERO] == MASCULINO ? 'M':'F');
                                LocalDate nascimento = LocalDate.parse(row[DT_NASCIMENTO], formatter);
                                Partido partido;
                                if(part.containsKey(Integer.parseInt( row[NR_PARTIDO] ) ) == true ){
                                        partido = part.get( Integer.parseInt( row[NR_PARTIDO] ) );
                                }
                                else {
                                        part.put( Integer.parseInt(row[NR_PARTIDO]) , new Partido(row[SG_PARTIDO], Integer.parseInt(row[NR_PARTIDO]) ) );
                                        partido = part.get( Integer.parseInt( row[NR_PARTIDO] ) );
                                }
                                Integer numero = Integer.parseInt(row[NR_CANDIDATO]);
                                Integer numero_federacao = Integer.parseInt(row[NR_FEDERACAO]);
                                Integer votos = 0;
                                Boolean eleito = (Integer.parseInt(row[CD_SIT_TOT_TURNO]) == ELEITO[0] || Integer.parseInt(row[CD_SIT_TOT_TURNO]) == ELEITO[1] ? true: false);
                                
                                Candidato curr = new Candidato(cargo, nome, genero, nascimento, partido, numero, numero_federacao, votos, eleito);
                                cand.put(curr.getNumero(), curr);

                        }
                        for(var x: cand.entrySet()){
                                System.out.println(x.getValue() + "\n");
                        }
                }
                catch(FileNotFoundException e){
                        System.out.println("Arquivo " + fcand + " nao encontrado");
                        System.exit(1);
                }
                catch(IOException e){
                        System.out.println("Erro ao ler arquivo " + fcand + "!\n");
                        System.exit(1);
                }

               
                //ler votos
                /*try{
                        v = new FileInputStream(fvotos);
                        inputv = new BufferedReader(new InputStreamReader(v, enconding));
                       
                }
                catch(FileNotFoundException e){
                        System.out.println("Arquivo " + fvotos + " nao encontrado\n");
                        System.exit(1);
                } catch (IOException e) {
                        System.out.println("Erro ao ler arquivo " + fvotos + "!\n");
                        System.exit(1);
                }*/




        }

        public static void solve(){

        }
        

        public static void write_output(){

        }

        

        public static void main(String[] args) throws Exception{  
               read_input(args[1], args[2], args[3]);
               solve();
               write_output();
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

        
}