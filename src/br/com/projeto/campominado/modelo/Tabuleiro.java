package br.com.projeto.campominado.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class Tabuleiro implements ObserverCampos {
	
	private int qtdLinhas;
	private int qtdColunas;
	private int totMinas;
	
	private final List<Campo> camposDoTabuleiro = new ArrayList<>();
	
	private final List<Consumer<Boolean>> observeadores = new ArrayList<Consumer<Boolean>>();

	public Tabuleiro(int qtdLinhas, int qtdColunas, int totMinas) {
		
		this.qtdLinhas = qtdLinhas;
		this.qtdColunas = qtdColunas;
		this.totMinas = totMinas;
	
		gerarTabuleiro();
		associarVizinhos();
		sortearCamposMinados();
	}
	
	public void registrarObservador(Consumer<Boolean> obs) {
		observeadores.add(obs);
	}
	
	public void notificarObservers(Boolean result) {
		observeadores.stream().forEach(o -> o.accept(result));
	}
	
	public void abrirCampo(int linha, int coluna) {
		
		camposDoTabuleiro.stream()
		.filter(cm -> cm.getLinha() == linha && cm.getColuna() == coluna)
		.findFirst()
		.ifPresent(c -> c.abrirCampo());
	}
	
	private void abrirMinas() {
		camposDoTabuleiro.stream()
		.filter(c -> c.isMinado())
		.filter(e -> !e.isMarcado())
		.forEach(c -> c.setAberto(true));
	}
	
	public void marcarCampo(int linha, int coluna) {
		camposDoTabuleiro.stream()
		.filter(cm -> cm.getLinha() == linha && cm.getColuna() == coluna)
		.findFirst()
		.ifPresent(c -> c.alterarMarcacao());
	}

	private void sortearCamposMinados() {
		
		long totalMinasArmadas = 0;
		Predicate<Campo> minado = c -> c.isMinado();
		
		while (totalMinasArmadas < totMinas) {
			int posAleatoria = (int) (Math.random() * camposDoTabuleiro.size());
			camposDoTabuleiro.get(posAleatoria).minar();
			totalMinasArmadas = camposDoTabuleiro.stream().filter(minado).count();
		}
	}

	private void associarVizinhos() {
		for (Campo campo : camposDoTabuleiro) {
			for (Campo campoVizinho : camposDoTabuleiro) {
				campo.addVizinho(campoVizinho);
			}
		}
		
	}

	private void gerarTabuleiro() {
		for (int linha = 0; linha < qtdLinhas; linha++) {
			for (int coluna = 0; coluna < qtdColunas; coluna++) {
				
				Campo campo = new Campo(linha, coluna);
				campo.registrarNovoObserver(this);
				camposDoTabuleiro.add(campo);
			}
		}
		
	}
	
	public boolean zerouJogo() {
		return camposDoTabuleiro.stream().allMatch(c -> c.objetivoAlcancado());
	}
	
	public void reiniciarJogo() {
		camposDoTabuleiro.stream().forEach(e -> e.reiniciarAtributos());
		sortearCamposMinados();
	}

	public void paraCada(Consumer<Campo> funcao) {
		camposDoTabuleiro.forEach(funcao);
	}
	
	public int getQtdLinhas() {
		return qtdLinhas;
	}

	public int getQtdColunas() {
		return qtdColunas;
	}

	@Override
	public void eventoOcorrido(Campo campo, EventoCampo eventoCampo) {
		
		if (eventoCampo == EventoCampo.EXPLODIR) {
			abrirMinas();
			notificarObservers(false);
			
		} else if (zerouJogo()) {
			System.out.println("Ganhou!");
			notificarObservers(true);
			
		
		} else if (eventoCampo == EventoCampo.DESMARCAR || eventoCampo == EventoCampo.MARCAR) {
			marcarCampo(campo.getLinha(), campo.getColuna());
		
		} else if (eventoCampo == EventoCampo.ABRIR && campo.isMarcado() == false) {
			abrirCampo(campo.getLinha(), campo.getColuna());
		
		} 
	}
}