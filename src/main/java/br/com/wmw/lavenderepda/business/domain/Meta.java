package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;
import totalcross.util.Vector;

public class Meta extends BaseDomain {

    public static String TABLE_NAME = "TBLVPMETA";

    public String cdEmpresa;
    public String cdRepresentante;
    public String dsPeriodo;
    public int qtPedidos;
    public double vlRealizadoVendas;
    public double vlMetaVendas;
    public Date dtInicial;
    public Date dtFinal;
    public String nuSequencia;
    public double qtUnidadeMeta;
    public double qtCaixaMeta;
    public double qtMixClienteMeta;
    public double qtMixProdutoMeta;
    //--
    public String nmRepresentante;

    //Não persistente
    public Vector metaAcompanhamentoList;
    public VerbaSaldo metaFlexAtual;
    public Date dtUltimoDia;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Meta) {
            Meta meta = (Meta) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, meta.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, meta.cdRepresentante) &&
            	ValueUtil.valueEquals(dsPeriodo, meta.dsPeriodo);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(dsPeriodo);
        return strBuffer.toString();
    }

    //@Override
    public String toString() {
    	return dsPeriodo;
    }

    public double getPctRealizadoMeta() {
    	double pctRealizadoMeta = 0;
    	if (vlMetaVendas > 0) {
    		pctRealizadoMeta = (vlRealizadoVendas * 100) / vlMetaVendas;
    	}
    	return pctRealizadoMeta;
    }

    public Vector getDiasUteisPeriodo() {
    	Vector result = new Vector();
    	if (ValueUtil.isNotEmpty(dtInicial)) {
    		Date dtBase = DateUtil.getDateValue(StringUtil.getStringValue(dtInicial));
    		if (ValueUtil.isNotEmpty(dtBase) && ValueUtil.isNotEmpty(dtFinal)) {
    			while (!dtBase.isAfter(dtFinal)) {
    				if ((dtBase.getDayOfWeek() != 0) && (dtBase.getDayOfWeek() != 6)) {
    					result.addElement(DateUtil.getDateValue(StringUtil.getStringValue(dtBase)));
    				}
    				dtBase.advance(1);
    			}
    		}
    	}
    	return result;
    }

    public int getNuDiasUteisPeriodo() {
    	int result = 0;
    	if (ValueUtil.isNotEmpty(dtInicial)) {
    		Date dtBase = DateUtil.getDateValue(StringUtil.getStringValue(dtInicial));
    		if (ValueUtil.isNotEmpty(dtBase) && ValueUtil.isNotEmpty(dtFinal)) {
    			while (!dtBase.isAfter(dtFinal)) {
    				if ((dtBase.getDayOfWeek() != 0) && (dtBase.getDayOfWeek() != 6)) {
    					result++;
    				}
    				dtBase.advance(1);
    			}
    		}
    	}
    	return result;
    }

    public int getNuDiaUtil(Date data) {
    	int result = 0;
    	if (ValueUtil.isNotEmpty(dtInicial) && ValueUtil.isNotEmpty(data)) {
    		Date dtBase = DateUtil.getDateValue(StringUtil.getStringValue(dtInicial));
    		if (ValueUtil.isNotEmpty(dtBase) && ValueUtil.isNotEmpty(dtFinal)) {
    			while (!dtBase.isAfter(dtFinal)) {
    				if ((dtBase.getDayOfWeek() != 0) && (dtBase.getDayOfWeek() != 6)) {
    					result++;
    				}
    				if (dtBase.equals(data)) {
    					return result;
    				}
    				dtBase.advance(1);
    			}
    		}
    	}
    	return result;
    }

    public double getPctProporcionalVenda() {
    	int nuUltimoDiaUtil = getNuDiaUtil(dtUltimoDia);
    	double vlMetaDiaria = vlMetaVendas / getNuDiasUteisPeriodo();
    	double vlMetaAteUltimoDiaUtil = vlMetaDiaria * (nuUltimoDiaUtil != 0 ? nuUltimoDiaUtil : 1);
    	double vlRealizadoAteUltimoDia = vlRealizadoVendas;
    	return ((vlRealizadoAteUltimoDia * 100) / vlMetaAteUltimoDiaUtil) - 100;
    }



    public double getVlProporcionalFlex() {
    	double vlMetaAteUltimoDiaUtil = getVlMetaFlexUltimoDiaUtil();
    	double vlRealizadoAteUltimoDia = metaFlexAtual.vlSaldoInicial - metaFlexAtual.vlSaldo;
    	return vlMetaAteUltimoDiaUtil - vlRealizadoAteUltimoDia;
    }

    public double getVlMetaFlexUltimoDiaUtil() {
    	int nuUltimoDiaUtil = getNuDiaUtil(dtUltimoDia);
    	double vlMetaDiaria = metaFlexAtual.vlSaldoInicial / getNuDiasUteisPeriodo();
    	return vlMetaDiaria * nuUltimoDiaUtil;
    }

//    public void loadUltimoDiaUtilComMetaAcompanhamento() {
//    	dtUltimoDia = DateUtil.getDateValue(StringUtil.getStringValue(dtInicial));
//    	for (int i = 0; i < metaAcompanhamentoList.size(); i++) {
//			MetaAcompanhamento metaAcompanhamento = (MetaAcompanhamento)metaAcompanhamentoList.items[i];
//			if ((metaAcompanhamento.dtRegistro != null) && metaAcompanhamento.dtRegistro.isAfter(dtUltimoDia)) {
//				dtUltimoDia = DateUtil.getDateValue(StringUtil.getStringValue(metaAcompanhamento.dtRegistro));
//			}
//		}
//    }

}