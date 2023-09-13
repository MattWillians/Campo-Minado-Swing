package br.com.projeto.campominado.visao;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import br.com.projeto.campominado.modelo.Campo;
import br.com.projeto.campominado.modelo.EventoCampo;
import br.com.projeto.campominado.modelo.ObserverCampos;

@SuppressWarnings("serial")
public class BotaoCampo extends JButton implements ObserverCampos, MouseListener{
	
	private Campo campo;
	
	private final Color BG_PADRAO = new Color(184,184,184);
	private final Color BG_MARCAR = new Color(8,179,247);
	private final Color BG_EXPLODIR = new Color(189,66,68);
	private final Color TEXTO_VERDE = new Color(0,100,0);
	
	public BotaoCampo(Campo campo) {
	
		this.campo = campo;
		setBorder(BorderFactory.createBevelBorder(0));
		setBackground(BG_PADRAO);
		setOpaque(true);
		
		addMouseListener(this);
		campo.registrarNovoObserver(this);
	}

	public void eventoOcorrido(Campo campo, EventoCampo eventoCampo) {
		switch (eventoCampo) {
		case EXPLODIR:
			System.out.println("ENTREI NO EXPLODIR");
			aplicarEstiloExplodir();
			break;
		case MARCAR: 
			System.out.println("ENTREI NO MARCAR");
			aplicarEstiloMarcar();
			break;
	    case ABRIR:
	    	System.out.println("ENTREI NO ABRIR");
	    	aplicarEstiloAbrir();
	    	break;
	    default:
	    	System.out.println("ENTREI NO DEFAULT");
	    	aplicarEstiloPadrao();
	    	break;
		}
	}
	
	

	private void aplicarEstiloPadrao() {
		setBorder(BorderFactory.createBevelBorder(0));
		setBackground(BG_PADRAO);
		setText("");
	}


	private void aplicarEstiloExplodir() {
		System.out.println("Estou colocando ESTILO EXPLOSAO");
		setBackground(BG_EXPLODIR);
		setText("X");
	}

	private void aplicarEstiloMarcar() {
		setBackground(BG_MARCAR);
		setText("M");
	}


	private void aplicarEstiloAbrir() {
		System.out.println("Estou colocando ESTILO ABRIR");	
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		if (campo.isMinado() == false) {

			setBackground(BG_PADRAO);
			
			
			switch ((int) campo.minasNasVizinhancas()) {
				
					case 1:
						setForeground(TEXTO_VERDE);
						break;
					
					case 2:
						setForeground(Color.BLUE);
						break;
					
					case 3:
						setForeground(Color.ORANGE);
						break;
						
					case 4,5,6:
						setForeground(Color.RED);
						break;
						
					default:
						setForeground(Color.PINK);
						break;
				}
			
			String valor = !campo.vizinhosSeguros() ? campo.minasNasVizinhancas() + "" : "";
		
			setText(valor);
			setFont(new Font("Times New Roman", Font.PLAIN, 20));
			
		} else {
			setBackground(BG_EXPLODIR);
			setText("X");
			
		}
	}
	

	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1) {

			campo.abrirCampo();
			eventoOcorrido(this.campo, EventoCampo.ABRIR);
			
		} else if (e.getButton() == 3) {
			
			eventoOcorrido(campo, EventoCampo.MARCAR);
			System.out.println("Botão Direito");
		}
	}
	
	public void mouseReleased(MouseEvent e) {}
	public void mouseEntered(MouseEvent e) {}
	public void mouseExited(MouseEvent e) {}
	public void mouseClicked(MouseEvent e) {}	
}
