package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;

public class TributacaoVlBase extends BaseDomain {

    public static String TABLE_NAME = "TBLVPTRIBUTACAOVLBASE";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTributacaoCliente;
    public String cdTributacaoProduto;
    public String cdProduto;
    public String cdUf;
    public double vlBaseIcms;
    public double vlBaseIcmsCalcRetido;
    public double vlBaseIcmsRetidoCalcRetido;
    public double vlPrecoMedioPonderado;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TributacaoVlBase) {
            TributacaoVlBase tributacaovlbase = (TributacaoVlBase) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tributacaovlbase.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, tributacaovlbase.cdRepresentante) &&
                ValueUtil.valueEquals(cdTributacaoCliente, tributacaovlbase.cdTributacaoCliente) &&
                ValueUtil.valueEquals(cdTributacaoProduto, tributacaovlbase.cdTributacaoProduto) &&
                ValueUtil.valueEquals(cdProduto, tributacaovlbase.cdProduto) &&
                ValueUtil.valueEquals(cdUf, tributacaovlbase.cdUf);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdTributacaoCliente);
    	strBuffer.append(";");
    	strBuffer.append(cdTributacaoProduto);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
    	strBuffer.append(";");
    	strBuffer.append(cdUf);
        return strBuffer.toString();
    }
    
    public boolean isExistePrecoBaseIcmsCalcRetido() {
    	return vlBaseIcmsCalcRetido != 0;
    }
    
    public boolean isExisteVlBaseIcmsRetidoCalcRetido() {
    	return vlBaseIcmsRetidoCalcRetido != 0;
    }
    
    public double getVlBaseIcmsRetidoCalcRetido(double vlPctDiferenca, double vlNegocioado, double pctMargemAgregada) {
    	if(!LavenderePdaConfig.isPMPFSobreVlBaseRetido()) return vlBaseIcmsRetidoCalcRetido;
    	
    	vlPctDiferenca = vlPctDiferenca == 0.0 ? 1 : vlPctDiferenca;
    	double vlPrecoMedio = vlPrecoMedioPonderado * vlPctDiferenca;
    	double vlMva = ValueUtil.round(vlNegocioado * (1 + pctMargemAgregada / 100), 2);
    	if (vlPrecoMedio != 0) {
    		double vlBase = vlNegocioado < vlPrecoMedio ? vlPrecoMedioPonderado : vlMva;
    		return vlBaseIcmsRetidoCalcRetido >= vlBase ? vlBase : vlBaseIcmsRetidoCalcRetido;
    	}
    	return vlBaseIcmsRetidoCalcRetido < vlMva ? vlBaseIcmsRetidoCalcRetido : vlMva;
    }
    
    

}