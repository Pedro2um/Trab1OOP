import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Leitura {
        //Constantes  relevantes para o trab
        static final int                FEDERAL                         = 6,
                                        ESTADUAL                        = 7,
                                        ISOLADO                         = -1;

        static final int[]              DEFERIDO                        = {2, 16},
                                        ELEITO                          = {2, 3};   

        static final String             MASCULINO                       = "2", 
                                        FEMININO                        = "4",
                                        FORMATO_DATA                    = "d/MM/yyyy",
                                        SEPARADOR_CSV_CD                = ";", 
                                        SEPARADOR_CSV_VOT               = ";",
                                        VALIDO_LEGENDA                  = "Válido (legenda)";

        static final char               FED                             = 'F', 
                                        EST                             = 'E', 
                                        MAS                             = 'M', 
                                        FEM                             = 'F';
                                       


        //Colunas do arquivo de "candidato.csv" 
        // Valor da coluna do arquivo csv - 1
        static final private int        CD_CARGO_CAND                   = 13,
                                        CD_SITUACAO_CANDIDADO_TOT       = 68,
                                        NR_CANDIDATO                    = 16,
                                        NM_URNA_CANDIDATO               = 18,
                                        NR_PARTIDO                      = 27,
                                        SG_PARTIDO                      = 28,
                                        NR_FEDERACAO                    = 30,
                                        DT_NASCIMENTO                   = 42,
                                        CD_SIT_TOT_TURNO                = 56,
                                        CD_GENERO                       = 45,
                                        NM_TIPO_DESTINACAO_VOTOS        = 67;

        //Colunas do arquivo de "votos.csv"
        // Valor da coluna do arquivo csv - 1
        static final private int        CD_CARGO_VOT                    = 17;
        static final int[]              IGNORAR_NR_VOTAVEL              = {95,96,97,98}; //Colunas irrelevantes
        static final int                NR_VOTAVEL                      = 19,
                                        QT_VOTOS                        = 21,
                                        NM_VOTAVEL                      = 20;
        private                         Map<Integer,Candidato> extra    = new HashMap<>();//candidatos (do cargo de opcao) não deferidos e que os votos sao direcionados para legenda do partido
        
        static boolean                  NOMINAL                         = true,
                                        LEGENDA                         = false;

        private boolean ignorarNrVotavel(Integer x){
                for(int i = 0; i < 3; i++){
                        if(x == IGNORAR_NR_VOTAVEL[i]) return true;
                }
                return false;
        }


        private boolean deferido(Integer x ) {
                if(x != DEFERIDO[0] && x != DEFERIDO[1]){
                        return false;
                }
                return true;
        }

        public void readCand(boolean federal, Map<Integer,Partido> part, List<Partido> partRanking, Map<Integer,Candidato> cand, List<Candidato> candEleitos,String fcand, String enconding){
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
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(FORMATO_DATA);

                        while((line = inputc.readLine()) != null){
                                line = line.replace(rep,rrep); 
                                
                                String[] row = line.split(SEPARADOR_CSV_CD);

                                Integer situacao = Integer.parseInt(row[CD_SITUACAO_CANDIDADO_TOT]);

                                Partido partido;
                                //verifica se o partido existe
                                if(part.containsKey(Integer.parseInt( row[NR_PARTIDO] ) ) == true ){
                                        partido = part.get( Integer.parseInt( row[NR_PARTIDO] ) );
                                }
                                else {
                                        partido = cPartido(row);
                                        part.put( Integer.parseInt(row[NR_PARTIDO]) , partido);
                                        partRanking.add(partido);
                                }               
                               
                                int cd = Integer.parseInt(row[CD_CARGO_CAND]);

                                if( cd != FEDERAL && cd != ESTADUAL ){
                                        //nao necessario para o processamento atual
                                        continue;
                                }
                                else if( cd == FEDERAL && federal == false){
                                        continue;
                                }
                                else if( cd == ESTADUAL && federal == true){
                                        continue;
                                }
                                Character cargo = (cd == FEDERAL?FED:EST);
                               
                                if(deferido(situacao)==false ){
                                        if(row[NM_TIPO_DESTINACAO_VOTOS].compareTo(VALIDO_LEGENDA) == 0 ){
                                                Candidato curr = cCandidato(row, cargo, partido, formatter);
                                                extra.put(curr.getNumero(), curr);
                                        }
                                        continue;
                                }
                                else {
                                        Candidato curr = cCandidato(row, cargo, partido, formatter);
                                        curr.getPartido().addCandidato(curr);
                                        if(curr.eleito()) candEleitos.add(curr);
                                        cand.put(curr.getNumero(), curr);
                                }
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

        private Partido cPartido(String[] row){
                return  new Partido(row[SG_PARTIDO].trim(), Integer.parseInt(row[NR_PARTIDO]) );
        }

        private Candidato cCandidato(String[] row, Character cargo, Partido partido, DateTimeFormatter formatter){ 
                String nome = row[NM_URNA_CANDIDATO].trim();
                Character genero = (row[CD_GENERO].compareTo(MASCULINO) == 0 ? MAS:FEM);
                LocalDate nascimento = LocalDate.parse(row[DT_NASCIMENTO], formatter);
                Integer numero = Integer.parseInt(row[NR_CANDIDATO]);;
                Integer numero_federacao = Integer.parseInt(row[NR_FEDERACAO]);
                Integer votos = 0;
                //verifica o direcionamento de votos
                //String vl = ;
                boolean nominal;
                if(row[NM_TIPO_DESTINACAO_VOTOS].compareTo(VALIDO_LEGENDA) == 0) nominal = LEGENDA;
                else nominal = NOMINAL;

                Boolean eleito = (      (Integer.parseInt(row[CD_SIT_TOT_TURNO]) == ELEITO[0] || Integer.parseInt(row[CD_SIT_TOT_TURNO]) == ELEITO[1]) ? true: false);

                return new Candidato(cargo, nome, genero, nascimento, partido, numero, numero_federacao, votos, nominal, eleito);
        }

        public void readVotos(boolean federal, Map<Integer, Partido> part, List<Partido> partRanking,Map<Integer,Candidato> cand, String fvotos, String enconding){
                FileInputStream v;
                BufferedReader inputv;
                //ler votos
                try{
                        v = new FileInputStream(fvotos);
                        inputv = new BufferedReader(new InputStreamReader(v, enconding));
                        String rep = "\"";
                        String rrep = "";
                        String line = "";
                        line = inputv.readLine();
                        while( (line = inputv.readLine()) != null){
                                line = line.replace(rep,rrep); 
                                String[] row = line.split(SEPARADOR_CSV_VOT);
                                
                                Integer num = Integer.parseInt(row[NR_VOTAVEL]);

                                if(ignorarNrVotavel(num) == true){
                                        continue;
                                }
             
                                int cd = Integer.parseInt(row[CD_CARGO_VOT]);

                                if( cd != FEDERAL && cd != ESTADUAL ){
                                        //nao necessario para o processamento atual
                                        continue;
                                }
                                else if( ( cd == FEDERAL && federal==false) || (cd == ESTADUAL && federal) ){
                                        //nao necessario para o processamento atual
                                        continue;
                                }
        
                                Integer votos = Integer.parseInt(row[QT_VOTOS]);

                                pAll(part, cand, num, votos);
  
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


        private void pAll(Map<Integer, Partido> part, Map<Integer,Candidato> cand, Integer num, Integer votos){ 
                //candidatos não deferidos com direcionamento de votos para legenda 
                if(extra.containsKey(num) == true){
                        pCandidatoIndeferido(part,num,votos);
                } 
                //candidatos deferidos
                else if( cand.containsKey(num) == true ){
                        pCandidatoDeferido(part,cand, num, votos);    
                }
                //votos em legenda
                else if (part.containsKey(num) == true){
                        pPartido(part, num, votos);
                }
        }
        
        private void pCandidatoIndeferido(Map<Integer, Partido> part, Integer num, Integer votos){
                extra.get(num).getPartido().incVotosLegenda(votos);
        }
        private void pCandidatoDeferido(Map<Integer, Partido> part, Map<Integer,Candidato> cand, Integer num, Integer votos){
                //trocar para boolean
                if(  cand.get(num).getFlagNominal() == LEGENDA) {
                        cand.get(num).getPartido().incVotosLegenda(votos);
                }
                else {
                        cand.get(num).incVotos(votos);
                        cand.get(num).getPartido().incVotosNominais(votos);
                }
        }
        private void pPartido(Map<Integer, Partido> part, Integer num, Integer votos){
                part.get(num).incVotosLegenda(votos);
        }
}
