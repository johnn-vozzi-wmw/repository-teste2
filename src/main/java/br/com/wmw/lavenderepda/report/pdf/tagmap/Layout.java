package br.com.wmw.lavenderepda.report.pdf.tagmap;

import java.util.ArrayList;
import java.util.List;

import totalcross.sql.ResultSet;

public class Layout extends GenericTag {
	
	public static final String ATTR_ID = "id";
	public static final String ATTR_LARGURA = "largura";
	public static final String ATTR_ALTURA = "altura";
	public static final String ATTR_MARGEMX = "margemX";
	public static final String ATTR_MARGEMY = "margemY";
	
	public String id;
	public List<GenericTag> genericTagList = new ArrayList<GenericTag>();
	public ResultSet rs;
	
	public int largura;
	public int altura;
	public int margemX;
	public int margemY;
	
	public Elementos getElementos() {
		Elementos e = findElementos();
		if (e == null) {
			e = new Elementos();
			genericTagList.add(e);
		}
		return e;
	}
	
	public Elementos findElementos() {
		for (GenericTag obj : genericTagList) {
			if (obj instanceof Elementos) {
				return (Elementos) obj;
			}
		}
		return null;
	}

}
