import com.opencsv.bean.CsvBindByPosition;

public class Sala {

	@CsvBindByPosition(position = 0)
	private String edificio;
	
	@CsvBindByPosition(position = 1)
	private String nome;
	
	@CsvBindByPosition(position = 2)
	private String capacidadeNormal;
	
	@CsvBindByPosition(position = 3)
	private String capacidadeExame;
	
	@CsvBindByPosition(position = 4) 
	int nCaracteristicas;
	
	@CsvBindByPosition(position = 5)
	boolean anfiteatroAulas;
	
	@CsvBindByPosition(position = 6)
	boolean apoioTecnicoEventos;
	
	@CsvBindByPosition(position = 7)
	boolean Arq1;
	@CsvBindByPosition(position = 8)
	boolean Arq2;
	@CsvBindByPosition(position = 9)
	boolean Arq3;
	@CsvBindByPosition(position = 10)
	boolean Arq4;
	@CsvBindByPosition(position = 11)
	boolean Arq5;
	@CsvBindByPosition(position = 12)
	boolean Arq6;
	@CsvBindByPosition(position = 13)
	boolean Arq9;
	@CsvBindByPosition(position = 14)
	boolean BYOD;
	@CsvBindByPosition(position = 15)
	boolean focusGroup;
	@CsvBindByPosition(position = 16)
	boolean horarioSalaVisivelPortalPublico;
	@CsvBindByPosition(position = 17)
	boolean laboratorioDeArquitecturaDeComputadoresI;
	@CsvBindByPosition(position = 18)
	boolean laboratorioDeArquitecturaDeComputadoresII;
	@CsvBindByPosition(position = 19)
	boolean laboratorioDeBasesDeEngenharia;
	@CsvBindByPosition(position = 20)
	boolean laboratorioDeElectronica;
	@CsvBindByPosition(position = 21)
	boolean laboratorioDeInformatica;
	@CsvBindByPosition(position = 22)
	boolean laboratorioDeJornalismo;
	@CsvBindByPosition(position = 23)
	boolean laboratorioDeRedesDeComputadoresI;
	@CsvBindByPosition(position = 24)
	boolean laboratorioDeRedesDeComputadoresII;
	@CsvBindByPosition(position = 25)
	boolean laboratorioDeTelecomunicacoes;
	@CsvBindByPosition(position = 26)
	boolean salaAulasMestrado;
	@CsvBindByPosition(position = 27)
	boolean salaAulasMestradoPlus;
	@CsvBindByPosition(position = 28)
	boolean salaNEE;
	@CsvBindByPosition(position = 29)
	boolean salaProvas;
	@CsvBindByPosition(position = 30)
	boolean salaReuniao;
	@CsvBindByPosition(position = 31)
	boolean salaDeArquitectura;
	@CsvBindByPosition(position = 32)
	boolean salaDeAulasNormal;
	@CsvBindByPosition(position = 33)
	boolean videoconferencia;
	@CsvBindByPosition(position = 34)
	boolean atrio;
	
	@Override
	public String toString() {
		return "Sala [edificio=" + edificio + ", nome=" + nome + ", capacidadeNormal=" + capacidadeNormal
				+ ", capacidadeExame=" + capacidadeExame + ", nCaracteristicas=" + nCaracteristicas
				+ ", anfiteatroAulas=" + anfiteatroAulas + ", apoioTecnicoEventos=" + apoioTecnicoEventos + ", Arq1="
				+ Arq1 + ", Arq2=" + Arq2 + ", Arq3=" + Arq3 + ", Arq4=" + Arq4 + ", Arq5=" + Arq5 + ", Arq6=" + Arq6
				+ ", Arq9=" + Arq9 + ", BYOD=" + BYOD + ", focusGroup=" + focusGroup
				+ ", horarioSalaVisivelPortalPublico=" + horarioSalaVisivelPortalPublico
				+ ", laboratorioDeArquitecturaDeComputadoresI=" + laboratorioDeArquitecturaDeComputadoresI
				+ ", laboratorioDeArquitecturaDeComputadoresII=" + laboratorioDeArquitecturaDeComputadoresII
				+ ", laboratorioDeBasesDeEngenharia=" + laboratorioDeBasesDeEngenharia + ", laboratorioDeElectronica="
				+ laboratorioDeElectronica + ", laboratorioDeInformatica=" + laboratorioDeInformatica
				+ ", laboratorioDeJornalismo=" + laboratorioDeJornalismo + ", laboratorioDeRedesDeComputadoresI="
		 + "]";
	}

}
