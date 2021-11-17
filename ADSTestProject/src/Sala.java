import java.util.List;

public class Sala {

	private String edificio;
	private String nome;
	private int capacidadeNormal;
	private int capacidadeExame;
	private int nCaracteristicas;
    private List<CaracteristicaSala> listaCaracteristicas;


	public Sala(String edificio, String nome, int capacidadeNormal, int capacidadeExame, int nCaracteristicas, List<CaracteristicaSala> listaCaracteristicas) {
		this.edificio = edificio;
		this.nome = nome;
		this.capacidadeNormal = capacidadeNormal;
		this.capacidadeExame = capacidadeExame;
		this.nCaracteristicas = nCaracteristicas;
		this.listaCaracteristicas = listaCaracteristicas;
	}
	
	@Override
	public String toString() {
		return "Sala [edificio=" + edificio + ", nome=" + nome + ", capacidadeNormal=" + capacidadeNormal
				+ ", capacidadeExame=" + capacidadeExame + ", nCaracteristicas=" + nCaracteristicas
				+ ", listaCaracteristicas=" + listaCaracteristicas + "]";
	}

}
