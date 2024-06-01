package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import totalcross.util.Date;

public class Kit extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPKIT";

	public static final String CD_TABELA_PRECO_ZERO = "0";

	public String cdEmpresa;
	public String cdRepresentante;
	public String cdKit;
	public String dsKit;
	public String cdTabelaPreco;
	public Date dtVigenciaInicial;
	public Date dtVigenciaFinal;
	public int qtMinItensKit;
	public String flEditaDesconto;
	public String flReplicaParcial;
	public String flValidaEstoque;
	public String dsAgrupadorKit;
	
	//Nao persistente
	public boolean kitNaoExiste;
	public String cdLocalEstoque;

	public Kit() {
		
	}
	
	public Kit(String cdKit) {
		this.cdKit = cdKit;
		kitNaoExiste = true;
	}
	
	public Kit(String cdEmpresa, String cdRepresentante, String cdKit) {
		this.cdEmpresa = cdEmpresa;
		this.cdRepresentante = cdRepresentante;
		this.cdKit = cdKit;
		this.cdTabelaPreco = CD_TABELA_PRECO_ZERO;
	}
	
	//Override
	public boolean equals(Object obj) {
		if (obj instanceof Kit) {
			Kit kit = (Kit) obj;
			return
			ValueUtil.valueEquals(cdEmpresa, kit.cdEmpresa) &&
			ValueUtil.valueEquals(cdRepresentante, kit.cdRepresentante) &&
			ValueUtil.valueEquals(cdKit, kit.cdKit) &&
			ValueUtil.valueEquals(cdTabelaPreco, kit.cdTabelaPreco);
		}
		return false;
	}

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdKit);
    	strBuffer.append(";");
		strBuffer.append(cdTabelaPreco);
        return strBuffer.toString();
    }

	public boolean isVigente() {
		Date dtInicial = DateUtil.getCurrentDate();
		dtInicial.advance(1);
		Date dtFinal = DateUtil.getCurrentDate();
		dtFinal.advance(-1);
		return (ValueUtil.isEmpty(dtVigenciaInicial) || dtVigenciaInicial.isBefore(dtInicial)) && (ValueUtil.isEmpty(dtVigenciaFinal) || dtVigenciaFinal.isAfter(dtFinal));
	}

	public String getCdDomain() {
		return cdKit;
	}

	public String getDsDomain() {
		return dsKit;
	}
	
	public boolean isEditaDesconto() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flEditaDesconto);
	}
	
	public boolean isValidaEstoque() {
		return !LavenderePdaConfig.isUsaValidaEstoqueKit() || ValueUtil.getBooleanValue(flValidaEstoque);
	}

}