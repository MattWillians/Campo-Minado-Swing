package br.com.projeto.campominado.modelo;

import java.util.ArrayList;
import java.util.List;

public class Campo {
	
	private boolean isAberto = false;
	private boolean isMinado = false;
	private boolean isMarcado = false;
	
	private final int linha;
	private final int coluna;
	
	private List<Campo> camposVizinhos = new ArrayList<>();
	
	private List<ObserverCampos> observadores = new ArrayList<>();
	
	public Campo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}
	
	public void registrarNovoObserver(ObserverCampos observerCampos) {
		observadores.add((observerCampos));
	}
	
	private void notificarObservadores(EventoCampo eventoCampo) {
		observadores.stream()
		.forEach(o -> o.eventoOcorrido(this, eventoCampo));
	}
	
	public boolean addVizinho(Campo campoVizinho) {
		
		//verificamos se o Vizinho esta na diagonal, verificando se o mesmo
		//esta em linhas diferentes do que estamos verificando
		
		boolean isMesmaLinha = this.getLinha() != campoVizinho.getLinha();
		boolean isMesmaColuna = this.getColuna() != campoVizinho.getColuna();
		boolean isEmDiagonal = isMesmaColuna && isMesmaLinha;
		
		int deltaLinha = Math.abs(this.getLinha() - campoVizinho.getLinha()); // retorna 1 se for mesma linha, 0 se não.
		int deltaColuna = Math.abs(this.getColuna()  - campoVizinho.getColuna()); // retorna 1 se for mesma linha, 0 se não.
		int deltaFinal = deltaColuna + deltaLinha; // Retorna 1 se for mesma linha, 2 se for diagonal
		
		if ((isEmDiagonal && deltaFinal == 2) || (!isEmDiagonal && deltaFinal == 1)) {
			this.camposVizinhos.add(campoVizinho);
			return true;
		}
		
		return false;
	}
	
	public void alterarMarcacao() {
		if (!this.isAberto ) {
			this.isMarcado = !this.isMarcado;
			
			if(this.isMarcado) {
				notificarObservadores(EventoCampo.MARCAR);
			} else {
				notificarObservadores(EventoCampo.DESMARCAR);
			}
		} 
	}
	
	public boolean abrirCampo() {
		
		if (!this.isAberto && !this.isMarcado) {
			System.out.println("ESTOU FECHADO E NÃO MARCADO");
			
			if (isMinado) {
				System.out.println("ESTOU MINADO");
				notificarObservadores(EventoCampo.EXPLODIR);
				return true;
			}
			
			System.out.println("ESTOU SETANDO ABERTO");
			setAberto(true);
			if (vizinhosSeguros()) {
				camposVizinhos.forEach(v -> v.abrirCampo());
			}
			
			return true;
		
		} else {
			return false;
		}
		
	}
	
	public boolean vizinhosSeguros() {
		return camposVizinhos.stream().noneMatch(e -> e.isMinado);
	}
	
	boolean objetivoAlcancado() {
		boolean desvendado = !isMinado && isAberto;
		boolean protegido = isMinado && isMarcado;
		
		return desvendado || protegido;
	}
	
	public long minasNasVizinhancas() {
		return camposVizinhos.stream().filter(vz -> vz.isMinado).count();
	}
	
	void reiniciarAtributos() {
		this.isAberto = false;
		this.isMarcado = false;
		this.isMinado = false;
		notificarObservadores(EventoCampo.FINALIZAR);
	}
	
	public void minar() {
		this.isMinado = true;
	}
	
	public boolean isMinado() {
		return isMinado;
	}

	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}
	
	public boolean isMarcado() {
		return isMarcado;
	}
	
	public boolean isAberto() {
		return isAberto;
	}
	
	void setAberto(boolean setAberto) {
		this.isAberto = setAberto;
		
		if (isAberto) {
			notificarObservadores(EventoCampo.ABRIR);
		}
	}
	
}
