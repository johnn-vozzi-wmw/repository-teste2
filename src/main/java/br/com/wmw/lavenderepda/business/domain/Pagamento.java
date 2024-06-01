package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class Pagamento extends BaseDomain {

	public static String TABLE_NAME = "TBLVPPAGAMENTO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdPagamento;
    public String cdTipoPagamento;
    public Date dtPagamento;
    public double vlPago;
    public double vlAdicionalPago;
    public String dsObservacao;

    //--Não Persistente
    public Date dtPagamentoInicialFilter;
    public Date dtPagamentoFinalFilter;
    public boolean flFilterTipoAlteracaoOriginal;
    public boolean flFiltraFechamentoDiario = false;
    public boolean editing;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Pagamento) {
            Pagamento pagamento = (Pagamento) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, pagamento.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, pagamento.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, pagamento.cdCliente) &&
                ValueUtil.valueEquals(cdPagamento, pagamento.cdPagamento);
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
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdPagamento);
        return primaryKey.toString();
    }
}