package br.com.projeto.campominado.visao;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.projeto.campominado.modelo.EventoCampo;
import br.com.projeto.campominado.modelo.Tabuleiro;

@SuppressWarnings("serial")
public class PainelTabuleiro extends JPanel {
	
	public PainelTabuleiro(Tabuleiro tabuleiro) {
		
		setLayout(new GridLayout(tabuleiro.getQtdLinhas(), tabuleiro.getQtdColunas()));
		
		tabuleiro.paraCada(c ->add(new BotaoCampo(c)));
		
		
		tabuleiro.registrarObservador(c -> {
			SwingUtilities.invokeLater(() -> {
				if (c) {
					JOptionPane.showMessageDialog(this, "Ganhou");
				} else {
					JOptionPane.showMessageDialog(this, "Perdeu");
				}
				
				
				tabuleiro.reiniciarJogo();
			});
		    
		});
	}
}
