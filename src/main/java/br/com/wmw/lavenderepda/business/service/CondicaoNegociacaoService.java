package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CondicaoNegociacao;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.validation.EstoqueException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CondicaoNegociacaoDao;
import totalcross.sys.Convert;
import totalcross.util.Vector;

public class CondicaoNegociacaoService extends CrudService {

    private static CondicaoNegociacaoService instance = null;
    
    private CondicaoNegociacaoService() {
        //--
    }
    
    public static CondicaoNegociacaoService getInstance() {
        if (instance == null) {
            instance = new CondicaoNegociacaoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CondicaoNegociacaoDao.getInstance();
    }

	@Override
	public void validate(BaseDomain domain) {
	}
	
	private CondicaoNegociacao getNewCondicaoNegociacao(String cdCondicaoNegociacao) {
		CondicaoNegociacao newCondicaoNegociacao = new CondicaoNegociacao();
		newCondicaoNegociacao.cdEmpresa = SessionLavenderePda.cdEmpresa;
		newCondicaoNegociacao.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondicaoNegociacao.class);
		newCondicaoNegociacao.cdCondicaoNegociacao = cdCondicaoNegociacao;
		return newCondicaoNegociacao;
		
	}

	public Vector loadCondicaoNegociacao() throws SQLException {
		return findAllByExample(getNewCondicaoNegociacao(null));
	}
	
	public CondicaoNegociacao findCondicaoNegociacao(String cdCondicaoNegociacao) throws SQLException {
		CondicaoNegociacao condicaoNegociacaoFilter = getNewCondicaoNegociacao(cdCondicaoNegociacao);
		return (CondicaoNegociacao) findByRowKey(condicaoNegociacaoFilter.getRowKey());
	}
	
	public void validaTrocaCondicaoNegociacao(Pedido pedido,CondicaoNegociacao condicaoNegociacaoOld, CondicaoNegociacao condicaoNegociacaoNew) throws SQLException {
		try {
			pedido.setCondicaoNegociacao(condicaoNegociacaoNew);
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
				try {
					EstoqueService.getInstance().validateEstoque(pedido, itemPedido, false);
				} catch (EstoqueException ex) {
					throw new ValidationException(ex.getMessage());
				}
			}
		} finally {
			pedido.setCondicaoNegociacao(condicaoNegociacaoOld);
		}
	}
	
	protected int getQtConsumidaDeEstoque(double qtVendida, CondicaoNegociacao condicaoNegociacao) {
		double vlPctEstoque = condicaoNegociacao.vlPctEstoque;
		if (LavenderePdaConfig.usaEstoqueInternoParcialmenteLocalEstoqueCondNeg && vlPctEstoque > 0) {
			double qtConsumidaDeEstoque = ValueUtil.round(qtVendida * vlPctEstoque / 100);
			if (qtConsumidaDeEstoque % 1 != 0) {
				if (qtConsumidaDeEstoque < 1) {
					throw new ValidationException(MessageUtil.getMessage(Messages.CONDICAONEGOCIACAO_MSG_QTD_ESTOQUE_DECIMAL_COM_UMA_OPCAO,new String[] {condicaoNegociacao.cdCondicaoNegociacao, getQtSugeridaParaNegociacao1(1, vlPctEstoque)}));
				} else {
					String opcao1 = getQtSugeridaParaNegociacao1(qtVendida, vlPctEstoque);
					String opcao2 = getQtSugeridaParaNegociacao2(qtVendida, vlPctEstoque);
					if (ValueUtil.isEmpty(opcao1)) {
						throw new ValidationException(MessageUtil.getMessage(Messages.CONDICAONEGOCIACAO_MSG_QTD_ESTOQUE_DECIMAL_COM_UMA_OPCAO,new String[] {condicaoNegociacao.cdCondicaoNegociacao, opcao2}));
					} else {
						throw new ValidationException(MessageUtil.getMessage(Messages.CONDICAONEGOCIACAO_MSG_QTD_ESTOQUE_DECIMAL_COM_DUAS_OPCOES, new String[] {condicaoNegociacao.cdCondicaoNegociacao, opcao1, opcao2}));
					}
				}
			}
			return ValueUtil.getIntegerValue(qtConsumidaDeEstoque);
		}
		return 0;
	}
	
	protected String getQtSugeridaParaNegociacao1(double qtVendida, double vlPctEstoque) {
		int qtSugeridaParaNegociacao =  (int) qtVendida - 1;
		while (qtSugeridaParaNegociacao > 0) {
			double qtConsumidaDeEstoque = ValueUtil.round(qtSugeridaParaNegociacao * vlPctEstoque / 100);
			if (qtConsumidaDeEstoque % 1 == 0) {
				return Convert.toString(qtSugeridaParaNegociacao);
			}
			qtSugeridaParaNegociacao--;
		}
		return "";
	}
	
	protected String getQtSugeridaParaNegociacao2(double qtVendida, double vlPctEstoque) {
		int qtSugeridaParaNegociacao =  (int) qtVendida + 1;
		boolean  verificar = true;
		String qtSugerida = "";
		while (verificar) {
			double qtConsumidaDeEstoque = ValueUtil.round(qtSugeridaParaNegociacao * vlPctEstoque / 100);
			if (qtConsumidaDeEstoque % 1 == 0) {
				qtSugerida = Convert.toString(qtSugeridaParaNegociacao);
				verificar = false;
			}
			qtSugeridaParaNegociacao++;
		}
		return qtSugerida;
	}

}