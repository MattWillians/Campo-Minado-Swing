package br.com.projeto.campominado.visao;

import javax.swing.JFrame;

import br.com.projeto.campominado.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class TelaMain extends JFrame {
	
	public TelaMain() {
		
		Tabuleiro tabuleiro = new Tabuleiro(16, 30, 50);
		add(new PainelTabuleiro(tabuleiro));
		
		setTitle("Campo Minado");
		setSize(1,1);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);
		
	}
	
	public static void main(String[] args) {
		new TelaMain();
	
	}
}
