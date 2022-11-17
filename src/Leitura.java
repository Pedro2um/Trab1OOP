import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class Leitura {

        //Constantes  relevantes para o trab
        static final int FEDERAL = 6; // F in implementation
        static final int ESTADUAL = 7; // E in implementation
        static final int[] DEFERIDO = {2, 16};
        static final int ISOLADO = -1;
        static final int[] ELEITO = {2, 3};
        static final String MASCULINO = "2", FEMININO = "4";


        //Colunas do arquivo de "candidato.csv"
        static final private int CD_CARGO_CAND = 13; //14 - 1
        static final private int CD_DETALHE_SITUACAO_CAND = 24; //25 - 1
        static final private int NR_CANDIDATO = 16; // 17 - 1
        static final private int NM_URNA_CANDIDATO = 18; //19 - 1
        static final private int NR_PARTIDO = 27; //28 - 1
        static final private int SG_PARTIDO = 28; //29 - 1
        static final private int NR_FEDERACAO = 30; //31 - 1
        static final private int DT_NASCIMENTO = 42; //43 - 1
        static final private int CD_SIT_TOT_TURNO = 56; //57 - 1
        static final private int CD_GENERO = 45; //46 - 1

        //Colunas do arquivo de "votos.csv"
        static final private int CD_CARGO_VOT = 17; //18 - 1
        static final int[] IGNORAR_NR_VOTAVEL = {95,96,97,98}; //Colunas irrelevantes
        static final int   NR_VOTAVEL   = 19; // 20 - 1
        static final int QT_VOTOS = 21; // 22 - 1
        
        private boolean ignorar_nr_votavel(Integer x){
                for(int i = 0; i < 3; i++){
                        if(x == IGNORAR_NR_VOTAVEL[i]) return false;
                }
                return true;
        }


        public void read_cand(Map<Integer,Partido> part, Map<Integer,Candidato> cand, String fcand, String enconding){
                FileInputStream c;
                        
                BufferedReader inputc;
        
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
                                
                                
                                Character cargo = Integer.parseInt(row[CD_CARGO_CAND]) == FEDERAL ? 'F' : 'E';
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
                                System.out.println(x.getValue());
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
        }
        public void read_votos(Map<Integer, Partido> part, Map<Integer,Candidato> cand, String fvotos, String enconding){
                FileInputStream v;
                BufferedReader inputv;
                //ler votos
                try{
                        v = new FileInputStream(fvotos);
                        inputv = new BufferedReader(new InputStreamReader(v, enconding));
                        String rep = "\"";
                        String rrep = "";
                        String line = "";
                        while( (line = inputv.readLine()) != null){
                                line = line.replace(rep,rrep); 
                                String[] row = line.split(line);
                                
                                Integer num = Integer.parseInt(row[NR_VOTAVEL]);
                                if(ignorar_nr_votavel(num)){
                                        continue;
                                }

                                Character cargo = Integer.parseInt(row[CD_CARGO_VOT]) == FEDERAL ? 'F' : 'E';

                                
                                
                                
                                
                        }
                       
                }
                catch(FileNotFoundException e){
                        System.out.println("Arquivo " + fvotos + " nao encontrado\n");
                        System.exit(1);
                } catch (IOException e) {
                        System.out.println("Erro ao ler arquivo " + fvotos + "!\n");
                        System.exit(1);
                }
        }
}
