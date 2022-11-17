default:
	ls
basic:
	ant compile
	ant jar
federall:
	make basic
	java -jar deputados.jar --federal consulta_cand_2022_ES.csv votacao_secao_2022_ES.csv 02/10/2022 > out_federal.csv
estaduall:
	make basic
	java -jar deputados.jar --estadual consulta_cand_2022_ES.csv votacao_secao_2022_ES.csv 02/10/2022 > out_estadual.csv