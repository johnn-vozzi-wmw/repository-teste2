package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class ResumoDia extends BaseDomain {

	public static String TABLE_NAME = "TBLVPRESUMODIA";

    public String cdEmpresa;
    public String cdRepresentante;
    public Date dtResumo;
    public double vlTotalVendido;
    public int qtItensVendidos;
    public int qtPedidos;
    public int qtItensVendidosBonificados;
    public double vlVerbaConsumida;
    public double vlTotalBonificacao;
    public double vlVerbaBonificacao;
    public double vlSaldoAnterior;
    public double vlTotalPagamento;
    public double vlTotalValorizacao;
    public double vlTotalPromissoria;
    public double vlTotalPontuacaoBase;
    public double vlTotalPontuacaoRealizado;
    public double vlSaldoFinal;
    public Date dtUltimofechamento;
    public Date dtAlteracao;
    public String hrAlteracao;
    public double vlTotalPeso;
    public double vlTotalVolume;
    
    //Não persistentes
    public Date dtResumoMenorIgualFilter;

    public boolean equals(Object obj) {
        if (obj instanceof ResumoDia) {
            ResumoDia resumoDia = (ResumoDia) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, resumoDia.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, resumoDia.cdRepresentante) &&
                ValueUtil.valueEquals(dtResumo, resumoDia.dtResumo);
        }
        return false;
    }

    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(DateUtil.formatDateDb(dtResumo));
        return primaryKey.toString();
    }

}