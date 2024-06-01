package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;

public class Representante extends LavendereBaseDomain {

    public static final String TABLE_NAME = "TBLVPREPRESENTANTE";

	public static final String TIPOCADASTRO_SUPERVISOR = "S";
	public static final String TIPOCADASTRO_REPRESENTANTE = "R";
	public static final String NMCOLUNA_VLINDICEFINANCEIRO = "VLINDICEFINANCEIRO";
	public static final String CDREPRESENTANTE_COMBO_DEFAULT = "-1";

    public String cdRepresentante;
    public String nmRepresentante;
    public double vlToleranciaVerba;
    public String flTipoCadastro;
    public String flEspecial;
    public double vlPctDescEspecial;
    public double vlPctMaxDesconto;
    public String nuNotaGeral;
    public String cdRepresentanteErp;
    public double vlIndiceFinanceiro;
    public String flRepresentanteInterno;

    //Não Persistente
    public String flDefault;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Representante) {
            Representante representante = (Representante) obj;
            return
                ValueUtil.valueEquals(cdRepresentante, representante.cdRepresentante);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
    	return StringUtil.getStringValue(cdRepresentante);
    }

    public boolean isSupervisor() {
    	return TIPOCADASTRO_SUPERVISOR.equals(flTipoCadastro);
    }

    public boolean isRepresentante() {
    	return ValueUtil.isEmpty(flTipoCadastro) || (TIPOCADASTRO_REPRESENTANTE.equals(flTipoCadastro));
    }

    public boolean isEspecial() {
    	return ValueUtil.VALOR_SIM.equals(flEspecial);
    }

    public boolean isRepresentanteInterno() {
        return ValueUtil.VALOR_SIM.equals(flRepresentanteInterno);
    }

	public String getCdDomain() {
		return cdRepresentante;
	}

	public String getDsDomain() {
		return nmRepresentante;
	}
	
	public String getCdRepresentanteTxt() {
		return LavenderePdaConfig.usaCodigoRepresentanteErp && ValueUtil.isNotEmpty(cdRepresentanteErp) ? cdRepresentanteErp : cdRepresentante;
	}
}