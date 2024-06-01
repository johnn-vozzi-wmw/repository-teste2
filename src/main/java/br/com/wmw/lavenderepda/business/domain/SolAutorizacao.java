package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.enums.TipoSolicitacaoAutorizacaoEnum;
import totalcross.util.Date;

public class SolAutorizacao extends BasePersonDomain {

	public static final String TABLE_NAME = "TBLVPSOLAUTORIZACAO";
    public static final String NM_COLUMN_CDSOLAUTORIZACAO = "CDSOLAUTORIZACAO";
	public static final String APPOBJ_CAMPOS_FILTRO_SOL_AUTORIZACAO = Messages.SOL_AUTORIZACAO_APPOBJ_CAMPOS_FILTRO;

	public String cdEmpresa;
    public String cdRepresentante;
    public String cdSolAutorizacao;
    public String flOrigemPedido;
    public String nuPedido;
    public String cdCliente;
    public String cdProduto;
    public String flTipoItemPedido;
    public int nuSeqProduto;
    public TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum;
    public String cdTabelaPreco;
    public double qtItemFisico;
    public double vlItemPedido;
    public double vlTotalItemPedido;
    public double vlOriginal;
    public String flAutorizado;
    public String dsObservacao;
    public String cdUsuarioSolAutorizacao;
    public String nmUsuarioLibSolAutorizacao;
    public Date dtSolAutorizacao;
    public String hrSolAutorizacao;
    public String cdUsuarioLibSolAutorizacao;
    public Date dtLibSolAutorizacao;
    public String hrLibSolAutorizacao;
    public String flVisualizado;
    public String flExcluido;
    public String flAgrupadorSimilaridade;
    public String cdCondicaoPagamento;
    public double vlParcelaMinMax;
    public double vlparcelaPedido;

    //-- Não Persistentes
    public Empresa empresa;
    public Representante representante;
    public Cliente cliente;
    public Produto produto;
    public String nuPedidoLike;
    public boolean filterPermission;
    public String flVisualizadoDifferenceFilter;
	public boolean ignoreRemovidos;
	public boolean maxAutorizacao;
	public String cdAgrupadorSimilaridade;


	public SolAutorizacao() { super(TABLE_NAME); }

    public SolAutorizacao(String flAutorizado) {
        this();
        this.flAutorizado = flAutorizado;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SolAutorizacao)) return false;
        SolAutorizacao solAutorizacao = (SolAutorizacao) obj;
        return ValueUtil.valueEquals(cdEmpresa, solAutorizacao.cdEmpresa)
            && ValueUtil.valueEquals(cdRepresentante, solAutorizacao.cdRepresentante)
            && ValueUtil.valueEquals(cdSolAutorizacao, solAutorizacao.cdSolAutorizacao);
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdSolAutorizacao);
        return primaryKey.toString();
    }

    public boolean isAutorizado() {
        return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flAutorizado);
    }

    public boolean isNaoAutorizado() {
        return ValueUtil.valueEquals(ValueUtil.VALOR_NAO, flAutorizado);
    }

    public boolean isPendente() {
        return ValueUtil.isEmpty(flAutorizado);
    }

    public String getStatus() {
        if (ValueUtil.isEmpty(flAutorizado)) {
            return Messages.STATUS_SOLICITACAO_AUTORIZACAO_PENDENTE;
        } else if (isAutorizado()) {
            return Messages.STATUS_SOLICITACAO_AUTORIZACAO_AUTORIZADO;
        } else {
            return Messages.STATUS_SOLICITACAO_AUTORIZACAO_NEGADO;
        }
    }
    
    public boolean isVisualizado() {
    	return ValueUtil.getBooleanValue(flVisualizado);
    }

}
