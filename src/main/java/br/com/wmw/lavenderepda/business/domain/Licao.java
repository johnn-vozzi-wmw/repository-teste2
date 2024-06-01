package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.BasePersonDomain;

public class Licao extends BasePersonDomain{
	
	public Licao() {
		super(TABLE_NAME);
	}

	public static String TABLE_NAME = "TBLVPLICAO";
	
	public String cdLicao;
	public String titulo;
	public String cdLicaoDependente;
	public String conteudo;
	
	@Override
	public String getPrimaryKey() {
		// TODO Auto-generated method stub
		return cdLicao;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder description = new StringBuilder();
		description.append(titulo);
		description.append(" [").append(cdLicao).append("]");
		return description.toString();
	}
}

