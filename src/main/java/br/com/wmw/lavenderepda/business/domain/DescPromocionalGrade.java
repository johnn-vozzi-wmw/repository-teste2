package br.com.wmw.lavenderepda.business.domain;

import totalcross.util.Vector;

public class DescPromocionalGrade extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPDESCPROMOCIONALGRADE";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public DescPromocional descPromocional;
	public String cdItemGrade2;
	public int nuMultiploEspecial;
	
	//Nao persistente
	public Vector itemGradeList;

	@Override
	public String getCdDomain() {
		return null;
	}

	@Override
	public String getDsDomain() {
		return null;
	}

	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + descPromocional.cdCliente + ";" + descPromocional.cdProduto + ";" + descPromocional.cdGrupoDescCli + ";" + descPromocional.cdGrupoDescProd + ";" 
			+ descPromocional.qtItem + ";" + descPromocional.cdTabelaPreco + ";" + descPromocional.cdCondicaoComercial + ";" + cdItemGrade2;
	}

}
