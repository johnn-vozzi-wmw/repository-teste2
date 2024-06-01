package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.dto.NfeEstoqueDto;
import totalcross.util.Date;

public class NfeEstoque extends BaseDomain {
	
	public static final String TABLE_NAME = "TBLVPNFEESTOQUE";
	public static final String TIPONFE_ENTRADA = "0";
	public static final String TIPONFE_SAIDA = "1";
	
	public String cdEmpresa;
	public String cdRepresentante;
	public String nuNotaRemessa;
	public String nuSerieRemessa;
	public String cdPedidoEstoque;
	public String cdStatusNfe;
	public String dsNaturezaOperacao;
	public String nuCfop;
	public Date dtSolicitacao;
	public String vlChaveAcesso;
	public String dsTipoEmissao;
	public String dsObservacao;
	public String cdTipoOperacaoNfe;
	public Date dtEmissao;
	public String hrEmissao;
	public Date dtResposta;
	public String hrResposta;
	public Date dtSaida;
	public String hrSaida;
	public double vlTotalNfe;
	public double vlTotalIcms;
	public double vlTotalSt;
	public double vlTotalIpi;
	public double vlTotalFrete;
	public double vlTotalSeguro;
	public double vlIbpt;
	public String cdTransportadora;
	public String flTipoNfe;
	
	//Nao persistente
	public Date dtEmissaoInicial;
	public Date dtEmissaoFinal;
	
	public NfeEstoque() {}
	
	public NfeEstoque(NfeEstoqueDto nfeEstoqueDto) {
		try {
			FieldMapper.copy(nfeEstoqueDto, this);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}
	
	@Override
	public String getPrimaryKey() {
		return cdEmpresa + ";" + cdRepresentante + ";" + nuSerieRemessa + ";" + nuNotaRemessa;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof NfeEstoque) {
			NfeEstoque nfeEstoque = (NfeEstoque) obj;
			return ValueUtil.valueEquals(cdEmpresa, nfeEstoque.cdEmpresa) &&
					ValueUtil.valueEquals(cdRepresentante, nfeEstoque.cdRepresentante) &&
					ValueUtil.valueEquals(nuSerieRemessa, nfeEstoque.nuSerieRemessa) &&
					ValueUtil.valueEquals(nuNotaRemessa, nfeEstoque.nuNotaRemessa);
		}
		return false;
	}

}
