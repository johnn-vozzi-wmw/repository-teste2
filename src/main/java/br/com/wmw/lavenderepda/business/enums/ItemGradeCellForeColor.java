package br.com.wmw.lavenderepda.business.enums;

import totalcross.ui.gfx.Color;

public enum ItemGradeCellForeColor {

	ESTOQUE_NEGATIVO_NAO_PERMITIDO(Color.RED),
	ESTOQUE_NEGATIVO_PERMITIDO(Color.ORANGE);

	private final int color;
	
	ItemGradeCellForeColor(int color) {
		this.color = color;
	}
	
	public int getColor() {
		return color;
	}
	
}
