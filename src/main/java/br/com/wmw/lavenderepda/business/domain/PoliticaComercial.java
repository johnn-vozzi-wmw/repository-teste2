package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class PoliticaComercial extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPPOLITICACOMERCIAL";
    public static String NMCOLUNA_DTINIVIGENCIA = "dtIniVigencia";
    public static String NMCOLUNA_DTFIMVIGENCIA = "dtFimVigencia";

    public String cdEmpresa;
    public String cdPoliticaComercial;
    public double vlIndiceCliente;
    public Date dtIniVigencia;
    public Date dtFimVigencia;
    public String flCampanha;
    public String cdProduto;
    public String cdLinha;
    public String cdClasse;
    public String cdGrupoProduto1;
    public String cdConjunto;
    public String cdMarca;
    public String cdColecao;
    public String cdStatusColecao;
    public String cdFamilia;
    public String cdCentroCusto;
    public String cdRepresentantePolitica;
    public String cdCategoria;
    public String cdCliente;
    public String cdGrupoDescCli;
    public String cdTabelaPreco;
    public String cdCondicaoPagamento;
    public String flAcumulaIndices;
    public String dsReferencia;
    public String flVendaEncerrada;
    public double vlPctPoliticaComercialMin;
    public double vlPctPoliticaComercialMax;
    public String cdGrupoCondPagtoPolitComerc;
    public String dsAgrupadorKit;
    
    
    //Não persistente
    public PlataformaVendaCliFin plataformaVendaCliFin;
    public String cdPlataformaVenda;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PoliticaComercial) {
            PoliticaComercial politicaComercial = (PoliticaComercial) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, politicaComercial.cdEmpresa) && 
                ValueUtil.valueEquals(cdPoliticaComercial, politicaComercial.cdPoliticaComercial);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdPoliticaComercial);
        return primaryKey.toString();
    }
    
    public boolean acumulaIndices() {
    	return ValueUtil.getBooleanValue(flAcumulaIndices);
    }

}
