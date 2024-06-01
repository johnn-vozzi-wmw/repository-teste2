package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class Unidade extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPUNIDADE";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdUnidade;
    public String dsUnidade;
    public String dsUnidadePlural;
    public String flCalculaPrecoMetroQuadrado;
    public String flIgnoraMultEspecial;
    public String flCalculaPesoGramatura;
    
    public Unidade() {
    }
    
    public Unidade(String cdEmpresa, String cdRepresentante, String cdUnidade) {
    	this.cdEmpresa = cdEmpresa;
    	this.cdRepresentante = cdRepresentante;
    	this.cdUnidade = cdUnidade;
    }

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Unidade) {
            Unidade unidade = (Unidade) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, unidade.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, unidade.cdRepresentante) &&
                ValueUtil.valueEquals(cdUnidade, unidade.cdUnidade);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdUnidade);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdUnidade;
	}

	@Override
	public String getDsDomain() {
		return dsUnidade;
	}

	public boolean ignoraMultEspecial() {
	     return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, this.flIgnoraMultEspecial);
	}
}