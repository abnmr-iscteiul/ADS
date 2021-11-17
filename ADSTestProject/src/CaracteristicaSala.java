
public class CaracteristicaSala {
	
	private String nome;
	private String importancia;
	
	public CaracteristicaSala(String nome, String importancia) {
		this.nome = nome;
		this.importancia = importancia;
	}

	@Override
	public String toString() {
		return "CaracteristicaSala [nome=" + nome + ", importancia=" + importancia + "]";
	}

}
