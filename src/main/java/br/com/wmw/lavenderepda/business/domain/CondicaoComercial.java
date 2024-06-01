package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class CondicaoComercial extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPCONDICAOCOMERCIAL";
	public static final String CDCONDICAOCOMERCIAL_VALOR_PADRAO = "0";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCondicaoComercial;
    public String dsCondicaoComercial;
    public double vlIndiceFinanceiro;
    public double vlIndiceVerba;
    public String flIndiceVerbaSequencial;
    public String cdTabelaPreco;
    public String flAcessaOutrasCond;
    public String flAcessivelOutrasCond;

    //Não persistente
    public String flDefault;
    public String cdCliente;
    public CondComCondPagto condComCondPagtoFilter;
    public CondComSegCli condComSegCliFilter;
    public CondComCliente condComClienteFilter;
    public boolean flCondicaoComercialParaItensPedido;
    public String cdCondicaoComercialPedido;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CondicaoComercial) {
            CondicaoComercial condicaoComercial = (CondicaoComercial) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, condicaoComercial.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, condicaoComercial.cdRepresentante) &&
                ValueUtil.valueEquals(cdCondicaoComercial, condicaoComercial.cdCondicaoComercial);
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
        primaryKey.append(cdCondicaoComercial);
        return primaryKey.toString();
    }

    public boolean isFlDefault() {
    	return ValueUtil.VALOR_SIM.equals(flDefault);
    }

	public String getCdDomain() {
		return cdCondicaoComercial;
	}

	public String getDsDomain() {
		return dsCondicaoComercial;
	}

	public boolean isIndiceVerbaSequencial() {
		return ValueUtil.VALOR_SIM.equals(flIndiceVerbaSequencial);
	}
	
	public boolean isFlAcessaOutrasCond() {
		return !ValueUtil.VALOR_NAO.equals(flAcessaOutrasCond);
	}
	
	public boolean isFlAcessivelOutrasCond() {
		return !ValueUtil.VALOR_NAO.equals(flAcessivelOutrasCond);
	}
	
	public double getVlIndiceFinanceiro() {
		return vlIndiceFinanceiro <= 0 ? 1 : vlIndiceFinanceiro;
	}
	
}