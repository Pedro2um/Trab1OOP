import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Eleicao {
        final private            String Federal                                         =  "--federal";
        private                  List<Candidato> candEleitos                            = new ArrayList<>();
        private                  List<Candidato> candMaisVotados                        = new ArrayList<>();
        private                  Map<Integer,Integer> m                                 = new HashMap<>(); //idx no ranking de mais votados
        private                  List<Candidato> candSeriamEleitos                      = new ArrayList<>();
        private                  List<Candidato> candBeneficiados                       = new ArrayList<>();
        private                  List<Partido> partRanking                              = new ArrayList<>();
        private                  Map<Partido,ArrayList<Candidato>> partCandHiLow        = new TreeMap<>();
        protected                Map< Partido, ArrayList<Candidato>> partCHL            = new HashMap<>();
        public final int         B30                                                    = 0,
                                 B40                                                    = 1, 
                                 B50                                                    = 2, 
                                 B60                                                    = 3, 
                                 U60                                                    = 4;
        final int                SIZE                                                   = 5;
        private int[]            qtd                                                    = new int[SIZE];
        private int              qtdTotal                                               = 0;
        public final int         MAS                                                    = 0, 
                                 FEM                                                    = 1;
        final int                SIZE_GEN                                               = 2;
        private int[]            gen                                                    = new int[SIZE_GEN];
        private int              votosValidosTotal                                      = 0;
        private int              votosNominaisTotal                                     = 0;
        private int              votosLegendaTotal                                      = 0;

        Locale                   locale                                                 = new Locale("pt", "BR");
        DateTimeFormatter        formatter                                              = DateTimeFormatter.ofPattern("d/MM/yyyy");
        LocalDate                localDate;


        public Eleicao(Locale ld, String data){
                locale = ld;
               // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                localDate = LocalDate.parse(data,formatter);
        } 

        public void readInput(  Map<Integer, Partido> part, Map<Integer,Candidato> cand,  String tipo, String fcand, String fvotos, String data){
                String enconding = "ISO-8859-1";
                Leitura l = new Leitura();

                boolean f = false;
                if(tipo.compareTo(Federal) == 0){
                        f = true;
                }

                l.readCand(f, part, partRanking,cand, candEleitos,fcand, enconding);
                l.readVotos(f, part, partRanking,cand, fvotos,enconding);
        }

        public void writeOutput(Map<Integer,Partido> part, String tipo, String data){
                relatorio1();
                relatorio2(tipo);
                relatorio3();
                relatorio4();
                relatorio5();
                relatorio6();
                relatorio8();
                relatorio9();
                relatorio10();
                relatorio11();
                /*
                System.out.println("Número de vagas = " + candEleitos.size() + "\n");//relatorio 1
                String dep = (tipo.compareTo(Federal))==0?"federais":"estaduais";
                System.out.println("Deputados " + dep + " eleitos:");

                //relatorio 2
                Integer cnt = 1;
                for(var x: candEleitos){
                        System.out.println( Integer.toString(cnt) + " - " +  (x.getNumeroFederacao() != -1? "*":"") + x);
                        cnt = cnt + 1;
                }

                System.out.println("\n");

                //relatorio 3
                System.out.println("Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
                cnt = 1;
                
                for(var x: candMaisVotados){
                        System.out.println( Integer.toString(cnt) + " - " +  (x.getNumeroFederacao() != -1? "*":"") + x);
                        cnt = cnt + 1;
                        if(cnt > candEleitos.size()) break;
                }

                //relatorio 4
                System.out.println("\nTeriam sido eleitos se a votação fosse majoritária, e não foram eleitos:\n(com sua posição no ranking de mais votados)");

                //cnt = 1;
               // int i = 1;
                for(var x: candSeriamEleitos){ 
                        if( m.get(x.getNumero()) > candEleitos.size()) break;
                        System.out.println( m.get(x.getNumero()) + " - " +  (x.getNumeroFederacao() != -1? "*":"") + x);
                       // i++; 
                       
                }

                //relatorio 5
                System.out.println("\nEleitos, que se beneficiaram do sistema proporcional:\n(com sua posição no ranking de mais votados)");

                for(var x: candBeneficiados){  
                        System.out.println( m.get(x.getNumero()) + " - " +  (x.getNumeroFederacao() != -1? "*":"") + x); 
                }


                cnt = 1;
                //relatorio 6
                System.out.println("\nVotação dos partidos e número de candidatos eleitos:");
                for(var x: partRanking){
                        System.out.println(Integer.toString(cnt) + " - " + x);
                        cnt++;
                }
                
                //relatorio 8
                System.out.println("\nPrimeiro e último colocados de cada partido:"); 
                cnt = 1;
                for(var x: partCandHiLow.entrySet()){
                        System.out.println(
                                Integer.toString(cnt) + " - " + x.getKey().getSigla() + " - " + x.getKey().getNumPartido() 
                                + " " + x.getValue().get(0).getCandNumVoto() + " / " + x.getValue().get(1).getCandNumVoto());
                        cnt++;
                }

                //relatorio 9
                System.out.println("\nEleitos, por faixa etária (na data da eleição):");
                System.out.printf("%s %d (%.2f%%)\n", "      Idade < 30:", qtd[B30], proporcao(qtd[B30], qtdTotal));
                System.out.printf("%s %d (%.2f%%)\n", "30 <= Idade < 40:", qtd[B40], proporcao(qtd[B40], qtdTotal));
                System.out.printf("%s %d (%.2f%%)\n", "40 <= Idade < 50:", qtd[B50], proporcao(qtd[B50], qtdTotal));
                System.out.printf("%s %d (%.2f%%)\n", "50 <= Idade < 60:", qtd[B60], proporcao(qtd[B60], qtdTotal));
                System.out.printf("%s %d (%.2f%%)\n", "60 <= Idade     :", qtd[U60], proporcao(qtd[U60], qtdTotal));
                
                //relatorio 10
                System.out.println("\nEleitos, por gênero:");
                System.out.printf("%s: %d (%.2f%%)\n", "Feminino ", gen[FEM], proporcao(gen[FEM], qtdTotal));
                System.out.printf("%s: %d (%.2f%%)\n", "Masculino", gen[MAS], proporcao(gen[MAS], qtdTotal));

                //relatorio 11
                NumberFormat aFormat = NumberFormat.getInstance(locale);
                NumberFormat bFormat = NumberFormat.getInstance(locale);
                bFormat.setMaximumFractionDigits(0);
                aFormat.setMaximumFractionDigits(2);
                aFormat.setMinimumFractionDigits(2);
                System.out.println("Total de votos válidos:    " + bFormat.format(votosValidosTotal) );
                System.out.println("Total de votos nominais:   " 
                                        + bFormat.format(votosNominaisTotal) + " (" 
                                        + aFormat.format(proporcaoDouble(votosNominaisTotal, votosValidosTotal)) + "%)");
                
                
                System.out.println("Total de votos de legenda: " 
                                        + bFormat.format(votosLegendaTotal) + " (" 
                                        + aFormat.format( proporcaoDouble(votosLegendaTotal, votosValidosTotal) ) + "%)");
                                        */
        }

        public void solve(Map<Integer, Partido> part, Map<Integer,Candidato> cand, String data){

                solve1();
                solve2();
                solve3(cand);
                //acelerar verificações
               // Set<Candidato> candEleitosTemp = new HashSet<>(candEleitos);
                Set<Candidato> candMaisVotadosTemp = new HashSet<>();
                solve4(candMaisVotadosTemp);
                solve5();
                solve6();
                solve8and11(part);
                solve9and10(data);

               /*  candEleitos.sort((Candidato a, Candidato b)->  (        -1*Integer.valueOf(a.getVotos()).compareTo(b.getVotos())==0?
                                                                        -1*a.getNascimento().compareTo(b.getNascimento()): 
                                                                        -1*Integer.valueOf(a.getVotos()).compareTo(b.getVotos())) 
                );
                candMaisVotados.addAll(cand.values());
                candMaisVotados.sort((Candidato a, Candidato b)->  -1*Integer.valueOf(a.getVotos()).compareTo(b.getVotos()));

                
                //acelerar verificações
                Set<Candidato> candEleitosTemp = new HashSet<>(candEleitos);
                Set<Candidato> candMaisVotadosTemp = new HashSet<>();
                int cnt = 1; 
                //ranking de mais votados
                for(var x: candMaisVotados){
                        m.put(x.getNumero(), cnt);
                        candMaisVotadosTemp.add(x);
                        cnt++;
                }
                cnt = 1; 
                //relatorio 4
                for(var x: candMaisVotados){
                        if(candEleitosTemp.contains(x)==false){
                                candSeriamEleitos.add(x);
                            
                        }
                        cnt++;
                        if(cnt > candEleitos.size()) break;
                        
                }
                //relatorio 5
                for(var x: candEleitos){
                        if(m.get(x.getNumero()) > candEleitos.size()){
                                candBeneficiados.add(x);
                        }
                }

                //relatorio 6
                partRanking.sort((Partido a, Partido b)->       -1*Integer.valueOf(a.getVotosTotal()).compareTo(b.getVotosTotal()) == 0?
                                                                   Integer.valueOf(a.getNumPartido()).compareTo(b.getNumPartido()):
                                                                -1*Integer.valueOf(a.getVotosTotal()).compareTo(b.getVotosTotal())
                );

                //relatorio 8 e 11
                //da pra otimizar?, mas vou deixar para depois
                
                for(var x: part.entrySet()){
                        //apenas partido com algum candidato
                        if(x.getValue().getQtdCandidatos() > 0) {
                                ArrayList<Candidato> temp = x.getValue().getArrayListCandidatos();
                                temp.sort((Candidato a, Candidato b) ->         -1*Integer.valueOf(a.getVotos()).compareTo(b.getVotos())==0?
                                                                                -1*a.getNascimento().compareTo(b.getNascimento()): 
                                                                                -1*Integer.valueOf(a.getVotos()).compareTo(b.getVotos()));
                                ArrayList<Candidato> temp2 = new ArrayList<>();
                                temp2.add(temp.get(0));//max
                                temp2.add(temp.get(temp.size()-1));//min 
                                partCHL.put(x.getValue(), temp2);
                                
                        }
                        votosValidosTotal += x.getValue().getVotosTotal();
                        votosNominaisTotal += x.getValue().getVotosNominal();
                        votosLegendaTotal += x.getValue().getVotosLegenda();
                }

                partCandHiLow = new TreeMap<Partido, ArrayList<Candidato>>(
                        new Comparator<Partido>() {
                                @Override
                                public int compare(Partido a, Partido b){
                                        long x = partCHL.get(a).get(0).getVotos();
                                        long y = partCHL.get(b).get(0).getVotos();
                                        if( x < y ){
                                                return 1;
                                        }
                                        else if( x > y){
                                                return -1;
                                        }
                                        else {
                                                if( a.getNumPartido() < b.getNumPartido() ) {
                                                        return 1;
                                                }
                                                else if( a.getNumPartido() > b.getNumPartido()){
                                                        return -1;
                                                }
                                                else {
                                                        return 0;
                                                }
                                                
                                        }
                                }
                        }
                );

                partCandHiLow.putAll(partCHL);  
                
                //relatorio 9 e 10
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                LocalDate ld = LocalDate.parse(data, formatter);

                for(int i = 0; i < SIZE; i++){
                        qtd[i] = 0;
                }
                for(int i = 0; i < SIZE_GEN; i++){
                        gen[i] = 0;
                }

                for(var x: candEleitos){
                        long i = getIdade(ld, x.getNascimento());
                        if( i < 30){
                                qtd[B30]++;
                        }
                        else if( i < 40){
                                qtd[B40]++;
                        }
                        else if( i < 50){
                                qtd[B50]++;
                        }
                        else if( i < 60){
                                qtd[B60]++;
                        }
                        else {
                                qtd[U60]++;
                        }
                        
                        if(x.getGenero().compareTo('M') == 0){
                                gen[MAS]++;
                        }
                        else if(x.getGenero().compareTo('F') == 0){
                                gen[FEM]++;    
                        }
                } 
                qtdTotal = candEleitos.size();
                return;
                */
        }

        private void solve1(){
                return;
        }
        private void solve2(){
                candEleitos.sort((Candidato a, Candidato b)->  (        -1*Integer.valueOf(a.getVotos()).compareTo(b.getVotos())==0?
                                                                        -1*a.getNascimento().compareTo(b.getNascimento()): 
                                                                        -1*Integer.valueOf(a.getVotos()).compareTo(b.getVotos())) 
                );
        }
        private void solve3(Map<Integer,Candidato> cand){
                candMaisVotados.addAll(cand.values());
                candMaisVotados.sort((Candidato a, Candidato b)->  -1*Integer.valueOf(a.getVotos()).compareTo(b.getVotos()));
        }
        private void solve4(Set<Candidato> candMaisVotadosTemp){
                int cnt = 1; 
                //ranking de mais votados
                for(var x: candMaisVotados){
                        m.put(x.getNumero(), cnt);
                        candMaisVotadosTemp.add(x);
                        cnt++;
                }

        }
        private void solve5(){
                 //relatorio 5
                for(var x: candEleitos){
                        if(m.get(x.getNumero()) > candEleitos.size()){
                                candBeneficiados.add(x);
                        }
                }
        }
        private void solve6(){
                partRanking.sort((Partido a, Partido b)->       -1*Integer.valueOf(a.getVotosTotal()).compareTo(b.getVotosTotal()) == 0?
                                                                   Integer.valueOf(a.getNumPartido()).compareTo(b.getNumPartido()):
                                                                -1*Integer.valueOf(a.getVotosTotal()).compareTo(b.getVotosTotal())
                );
        }
        private void solve7(){
                return;
        }
        private void solve8(){
                return;
        }
        
        private void solve9(){
                return;
        }
        private void solve10(){
                return;
        }
        private void solve11(){
                return;
        }
        //otimizacoes
        private void solve8and11(Map<Integer, Partido> part){    
                for(var x: part.entrySet()){
                        //apenas partido com algum candidato
                        if(x.getValue().getQtdCandidatos() > 0) {
                                ArrayList<Candidato> temp = x.getValue().getArrayListCandidatos();
                                temp.sort((Candidato a, Candidato b) ->         -1*Integer.valueOf(a.getVotos()).compareTo(b.getVotos())==0?
                                                                                -1*a.getNascimento().compareTo(b.getNascimento()): 
                                                                                -1*Integer.valueOf(a.getVotos()).compareTo(b.getVotos()));
                                ArrayList<Candidato> temp2 = new ArrayList<>();
                                temp2.add(temp.get(0));//max
                                temp2.add(temp.get(temp.size()-1));//min 
                                partCHL.put(x.getValue(), temp2);
                                
                        }
                        votosValidosTotal += x.getValue().getVotosTotal();
                        votosNominaisTotal += x.getValue().getVotosNominal();
                        votosLegendaTotal += x.getValue().getVotosLegenda();
                }

                partCandHiLow = new TreeMap<Partido, ArrayList<Candidato>>(
                        new Comparator<Partido>() {
                                @Override
                                public int compare(Partido a, Partido b){
                                        long x = partCHL.get(a).get(0).getVotos();
                                        long y = partCHL.get(b).get(0).getVotos();
                                        if( x < y ){
                                                return 1;
                                        }
                                        else if( x > y){
                                                return -1;
                                        }
                                        else {
                                                if( a.getNumPartido() < b.getNumPartido() ) {
                                                        return 1;
                                                }
                                                else if( a.getNumPartido() > b.getNumPartido()){
                                                        return -1;
                                                }
                                                else {
                                                        return 0;
                                                }
                                                
                                        }
                                }
                        }
                );

                partCandHiLow.putAll(partCHL);  
        } 
        private void solve9and10(String data){
                 //relatorio 9 e 10
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");
                LocalDate ld = LocalDate.parse(data, formatter);

                for(int i = 0; i < SIZE; i++){
                        qtd[i] = 0;
                }
                for(int i = 0; i < SIZE_GEN; i++){
                        gen[i] = 0;
                }

                for(var x: candEleitos){
                        long i = getIdade(ld, x.getNascimento());
                        if( i < 30){
                                qtd[B30]++;
                        }
                        else if( i < 40){
                                qtd[B40]++;
                        }
                        else if( i < 50){
                                qtd[B50]++;
                        }
                        else if( i < 60){
                                qtd[B60]++;
                        }
                        else {
                                qtd[U60]++;
                        }
                        
                        if(x.getGenero().compareTo('M') == 0){
                                gen[MAS]++;
                        }
                        else if(x.getGenero().compareTo('F') == 0){
                                gen[FEM]++;    
                        }
                } 
                qtdTotal = candEleitos.size();
        }
        
        
        //Exibir Relatorios
        
        private void relatorio1(){
                System.out.println("Número de vagas = " + candEleitos.size() + "\n");
        } 
        private void relatorio2(String tipo){
                String dep = (tipo.compareTo(Federal))==0?"federais":"estaduais";
                System.out.println("Deputados " + dep + " eleitos:");
                Integer cnt = 1;
                for(var x: candEleitos){
                        System.out.println( Integer.toString(cnt) + " - " +  (x.getNumeroFederacao() != -1? "*":"") + x);
                        cnt = cnt + 1;
                }
        }
        private void relatorio3(){
                //relatorio 3
                System.out.println("Candidatos mais votados (em ordem decrescente de votação e respeitando número de vagas):");
                int cnt = 1;
                
                for(var x: candMaisVotados){
                        System.out.println( Integer.toString(cnt) + " - " +  (x.getNumeroFederacao() != -1? "*":"") + x);
                        cnt = cnt + 1;
                        if(cnt > candEleitos.size()) break;
                }
        }
        private void relatorio4(){
                System.out.println("\nTeriam sido eleitos se a votação fosse majoritária, e não foram eleitos:\n(com sua posição no ranking de mais votados)");
                for(var x: candSeriamEleitos){ 
                        if( m.get(x.getNumero()) > candEleitos.size()) break;
                        System.out.println( m.get(x.getNumero()) + " - " +  (x.getNumeroFederacao() != -1? "*":"") + x);     
                }
        }
        private void relatorio5(){
                System.out.println("\nEleitos, que se beneficiaram do sistema proporcional:\n(com sua posição no ranking de mais votados)");
                for(var x: candBeneficiados){  
                        System.out.println( m.get(x.getNumero()) + " - " +  (x.getNumeroFederacao() != -1? "*":"") + x); 
                }
        }
        private void relatorio6(){
                int cnt = 1;
                //relatorio 6
                System.out.println("\nVotação dos partidos e número de candidatos eleitos:");
                for(var x: partRanking){
                        System.out.println(Integer.toString(cnt) + " - " + x);
                        cnt++;
                }
        }
        private void relatorio7(){
                return;
        }
        private void relatorio8(){
                System.out.println("\nPrimeiro e último colocados de cada partido:"); 
                int cnt = 1;
                for(var x: partCandHiLow.entrySet()){
                        System.out.println(
                                Integer.toString(cnt) + " - " + x.getKey().getSigla() + " - " + x.getKey().getNumPartido() 
                                + " " + x.getValue().get(0).getCandNumVoto() + " / " + x.getValue().get(1).getCandNumVoto());
                        cnt++;
                }
        }
        private void relatorio9(){
                System.out.println("\nEleitos, por faixa etária (na data da eleição):");
                System.out.printf("%s %d (%.2f%%)\n", "      Idade < 30:", qtd[B30], proporcao(qtd[B30], qtdTotal));
                System.out.printf("%s %d (%.2f%%)\n", "30 <= Idade < 40:", qtd[B40], proporcao(qtd[B40], qtdTotal));
                System.out.printf("%s %d (%.2f%%)\n", "40 <= Idade < 50:", qtd[B50], proporcao(qtd[B50], qtdTotal));
                System.out.printf("%s %d (%.2f%%)\n", "50 <= Idade < 60:", qtd[B60], proporcao(qtd[B60], qtdTotal));
                System.out.printf("%s %d (%.2f%%)\n", "60 <= Idade     :", qtd[U60], proporcao(qtd[U60], qtdTotal));
        }
        private void relatorio10(){
                System.out.println("\nEleitos, por gênero:");
                System.out.printf("%s: %d (%.2f%%)\n", "Feminino ", gen[FEM], proporcao(gen[FEM], qtdTotal));
                System.out.printf("%s: %d (%.2f%%)\n", "Masculino", gen[MAS], proporcao(gen[MAS], qtdTotal));
        }
        private void relatorio11(){
                 //relatorio 11
                NumberFormat aFormat = NumberFormat.getInstance(locale);
                NumberFormat bFormat = NumberFormat.getInstance(locale);
                bFormat.setMaximumFractionDigits(0);
                aFormat.setMaximumFractionDigits(2);
                aFormat.setMinimumFractionDigits(2);
                System.out.println("Total de votos válidos:    " + bFormat.format(votosValidosTotal) );
                System.out.println("Total de votos nominais:   " 
                                        + bFormat.format(votosNominaisTotal) + " (" 
                                        + aFormat.format(proporcaoDouble(votosNominaisTotal, votosValidosTotal)) + "%)");
                
                
                System.out.println("Total de votos de legenda: " 
                                        + bFormat.format(votosLegendaTotal) + " (" 
                                        + aFormat.format( proporcaoDouble(votosLegendaTotal, votosValidosTotal) ) + "%)");
        }
        
        //funcoes auxiliares
        private static int getIdade(LocalDate ld, LocalDate n) {
                return Period.between(n, ld).getYears();  
        }
        private static float proporcao(int x, int t) {
                double ans = (double)x / (double)t; 
                return (float)(ans*100);
        }
        private static double proporcaoDouble(int x, int t){
                double ans = (double)x / (double)t; 
                return (ans*100);
        }
}
