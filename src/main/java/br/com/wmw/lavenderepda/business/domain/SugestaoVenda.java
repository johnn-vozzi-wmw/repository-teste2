package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class SugestaoVenda extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPSUGESTAOVENDA";

	public static final String NMCOLUNA_DSSUGESTAOVENDA = "DSSUGESTAOVENDA";
	
	public static String FLTIPOSUGESTAOVENDA_COMQUANTIDADE = "C";
	public static String FLTIPOSUGESTAOVENDA_SEMQUANTIDADE = "S";

	public static String FLTIPOFREQUENCIA_DIARIO = "1";
	public static String FLTIPOFREQUENCIA_SEMANAL = "2";
	public static String FLTIPOFREQUENCIA_MENSAL = "3";
	
	public static final String LOCAL_MENU_PRODUTOS = "1";
	public static final String LOCAL_MENU_INFERIOR = "2";

    public String cdEmpresa;
    public String cdSugestaoVenda;
    public String dsSugestaoVenda;
    public String flTipoSugestaoVenda;
    public String cdRamoAtividade;
    public Date dtFinal;
    public Date dtInicial;
    public String flPossuiVigencia;
    public String flObrigaVenda;
    public String flTipoFrequencia;
    public String flFechamento;
    public String cdSegmento;
    public String cdCanal;
    public String flIndustria;
    public String cdClassificacao;
    
    //Não persistente
    public String flFechamentoDif;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof SugestaoVenda) {
            SugestaoVenda sugestaoVenda = (SugestaoVenda) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, sugestaoVenda.cdEmpresa) &&
                ValueUtil.valueEquals(cdSugestaoVenda, sugestaoVenda.cdSugestaoVenda);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdSugestaoVenda);
        return primaryKey.toString();
    }

	public String getCdDomain() {
		return cdSugestaoVenda;
	}

	public String getDsDomain() {
		return dsSugestaoVenda;
	}

	public boolean isPossuiVigencia() {
		return ValueUtil.VALOR_SIM.equals(flPossuiVigencia);
	}

	public boolean isObrigaVenda() {
		return ValueUtil.VALOR_SIM.equals(flObrigaVenda);
	}

	public boolean isFlFechamento() {
		return ValueUtil.VALOR_SIM.equals(flFechamento);
	}

	public boolean isTipoFrequenciaDiario() {
		return FLTIPOFREQUENCIA_DIARIO.equals(flTipoFrequencia);
	}

	public boolean isTipoFrequenciaSemanal() {
		return FLTIPOFREQUENCIA_SEMANAL.equals(flTipoFrequencia);
	}

	public boolean isTipoFrequenciaMensal() {
		return FLTIPOFREQUENCIA_MENSAL.equals(flTipoFrequencia);
	}

	public boolean isSugestaoVendaSemQuantidade() {
		return FLTIPOSUGESTAOVENDA_SEMQUANTIDADE.equals(flTipoSugestaoVenda);
	}

	public boolean isSugestaoVendaComQuantidade() {
		return FLTIPOSUGESTAOVENDA_COMQUANTIDADE.equals(flTipoSugestaoVenda);
	}

	public boolean isPossuiFrequencia() {
		return isTipoFrequenciaDiario() || isTipoFrequenciaSemanal() || isTipoFrequenciaMensal();
	}

	public boolean isIndustria() {
		return ValueUtil.getBooleanValue(flIndustria);
	}

	@Override
	public String getSortStringValue() {
		if (NMCOLUNA_DSSUGESTAOVENDA.equals(sortAtributte) && this.dsSugestaoVenda != null) {
			return this.dsSugestaoVenda;
		}
		return super.getSortStringValue();
	}

}