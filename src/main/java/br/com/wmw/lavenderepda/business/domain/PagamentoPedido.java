package br.com.wmw.lavenderepda.business.domain;

import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.BigDecimal;
import totalcross.util.Date;

public class PagamentoPedido extends BaseDomain {

	public static String NM_COLUNA_VLPAGAMENTOPEDIDO = "vlPagamentoPedido";
	public static final String TABLE_NAME = "TBLVPPAGAMENTOPEDIDO";
	public static final String TABLE_NAME_ERP = "TBLVPPAGAMENTOPEDIDOERP";
	
	public static final String CODIGO_BANCO = "1";
	public static final String COMPLEMENTO = "2";
	public static final String AGENCIA = "3";
	public static final String CONTA = "4";
	public static final String CHEQUE = "5";
	public static final String CHEQUE_TERCEIRO = "6";
	public static final String EMITENTE = "7";
	public static final String REFERENTE_A = "8";
	public static final String DESCRICAO_BANCO = "9";
	public static final String DATA_CHEQUE = "10";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdPagamentoPedido;
    public String nuPedido;
    public String flOrigemPedido;
    public String cdTipoPagamento;
    public String cdCondicaoPagamento;
    public double vlPagamentoPedido;
    public double vlDesconto;
    public String nuBanco;
    public String nuComplemento;
    public String nuAgencia;
    public String nuConta;
    public String nuCheque;
    public String flChequeTerceiro;
    public String dsEmitente;
    public String dsReferenteCheque;
    public Date dtVencimento;
    public double vlPagamentoBruto;
    public double vlIndicePagamento;
    public String dsBanco;
    public Date dtCheque;
    public String cdVencimentoAdicBoleto;
    
    //Nao persistentes
    public double vlTotalPedido;
    public double vlPedidoAberto;
    public double vlMinimo;
    public double vlPctDescontoPagamento;
    public double vlPctDescontoMaxCondicaoPagamento;
    public boolean validaInformacoesRelacionadasACheque;
    public boolean validaInformacoesRelacionadasAoVencimento;
    public boolean isNovo;
    public Date dtVencimentoBase;
    public BigDecimal nuDocumento;
	public Date dtVencimentoBoleto;
    public boolean houveErro;
    
    public String cdcliente;
    public String nmRazaoSocial;
    public String nuNfe;
    
    public List<VenctoPagamentoPedido> venctoPagamentoPedidos;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof PagamentoPedido) {
            PagamentoPedido pagamentoPedido = (PagamentoPedido) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, pagamentoPedido.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, pagamentoPedido.cdRepresentante) && 
                ValueUtil.valueEquals(cdPagamentoPedido, pagamentoPedido.cdPagamentoPedido) && 
                ValueUtil.valueEquals(nuPedido, pagamentoPedido.nuPedido) && 
                ValueUtil.valueEquals(flOrigemPedido, pagamentoPedido.flOrigemPedido);
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
        primaryKey.append(cdPagamentoPedido);
        primaryKey.append(";");
        primaryKey.append(nuPedido);
        primaryKey.append(";");
        primaryKey.append(flOrigemPedido);
        return primaryKey.toString();
    }

}