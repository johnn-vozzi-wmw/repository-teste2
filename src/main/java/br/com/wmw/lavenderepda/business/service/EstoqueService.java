package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.CondicaoNegociacao;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoRemessa;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProducaoProd;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoErro;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.domain.dto.EstoqueDto;
import br.com.wmw.lavenderepda.business.domain.dto.ItemPedidoEstoqueDto;
import br.com.wmw.lavenderepda.business.validation.EstoqueException;
import br.com.wmw.lavenderepda.business.validation.EstoquePrevistoException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.EstoquePdbxDao;
import totalcross.json.JSONFactory;
import totalcross.util.Vector;


public class EstoqueService extends CrudService {

    private static EstoqueService instance;
    public Vector itensAdvertencia;
    
    private EstoqueService() {
        //--
    }

    public static EstoqueService getInstance() {
        if (instance == null) {
            instance = new EstoqueService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return EstoquePdbxDao.getInstance();
    }

    @Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public double getSumEstoqueGradeProduto(String cdProduto, String cdLocalEstoque) throws SQLException {
    	return getSumEstoqueGradeProdutoNivel1(cdProduto, null, cdLocalEstoque);
    }

    public double getSumEstoqueGradeProdutoNivel1(String cdProduto, String cdItemGrade1, String cdLocalEstoque) throws SQLException {
    	return getSumEstoqueGradeProduto(cdProduto, cdItemGrade1, cdLocalEstoque, null);
    }
    
    public double getSumEstoqueGradeProduto(String cdProduto, String cdItemGrade1, String cdLocalEstoque, String cdItemGrade2) throws SQLException {
    	Estoque estoqueFilter = new Estoque();
    	estoqueFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	estoqueFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	estoqueFilter.cdProduto = cdProduto;
    	estoqueFilter.cdItemGrade1 = cdItemGrade1;
		if (LavenderePdaConfig.isGradeProdutoModoLista()) {
    		estoqueFilter.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(cdItemGrade2) ? null : cdItemGrade2;
    	}
    	estoqueFilter.cdLocalEstoque = cdLocalEstoque;
    	estoqueFilter.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_ERP;
    	return sumByExample(estoqueFilter, "QTESTOQUE");
    }
    
    public double getSumEstoqueEstoquePrevistoGradeProduto(String cdProduto, String cdItemGrade1, String cdLocalEstoque, String cdItemGrade2) throws SQLException {
    	Estoque estoqueFilter = new Estoque();
    	estoqueFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	estoqueFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	estoqueFilter.cdProduto = cdProduto;
    	estoqueFilter.cdItemGrade1 = cdItemGrade1;
		if (LavenderePdaConfig.isGradeProdutoModoLista()) {
    		estoqueFilter.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(cdItemGrade2) ? null : cdItemGrade2;
    	}
    	estoqueFilter.cdLocalEstoque = cdLocalEstoque;
    	estoqueFilter.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_ERP;
    	Estoque estoque = EstoquePdbxDao.getInstance().getSumEstoqueEstoquePrevistoGradeProduto(estoqueFilter);
    	estoqueFilter.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_PDA;
    	double somaEstPda = super.sumByExample(estoqueFilter, "QTESTOQUE");
    	double qtEstoque = Math.max(0d, estoque.qtEstoque - somaEstPda - estoque.qtEstoquePrevisto);
    	return ValueUtil.round(qtEstoque);
    }
    
    public Estoque getEstoqueByRepresentante(String cdProduto, String flOrigemEstoque, String cdRepresentante) throws SQLException {
		Estoque estoqueFilter = new Estoque();
    	estoqueFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	estoqueFilter.cdRepresentante = cdRepresentante;
    	estoqueFilter.cdProduto = cdProduto;
		estoqueFilter.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		estoqueFilter.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		estoqueFilter.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		estoqueFilter.cdLocalEstoque = Estoque.CD_LOCAL_ESTOQUE_PADRAO;
    	estoqueFilter.flOrigemEstoque = flOrigemEstoque;
		Estoque estoque = (Estoque)findByRowKey(estoqueFilter.getRowKey());
		if (estoque == null) {
			return new Estoque();
		}
		if (LavenderePdaConfig.atualizarEstoqueInterno && Estoque.FLORIGEMESTOQUE_ERP.equals(flOrigemEstoque)) {
			estoque.qtEstoque = ValueUtil.round(estoque.qtEstoque) - ValueUtil.round(getQtEstoqueConsumidoPda(cdProduto, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, Estoque.CD_LOCAL_ESTOQUE_PADRAO, null));
		}
		return estoque;
    }
    
    public double getEstoqueProduto(Produto produto) throws SQLException {
 		if (produto.estoque == null) {
 			return 0;
 		}
 		return produto.estoque.qtEstoque;
 	}
    
    public Estoque getEstoque(String cdProduto, String flOrigemEstoque) throws SQLException {
    	return getEstoque(cdProduto, Estoque.CD_LOCAL_ESTOQUE_PADRAO, flOrigemEstoque);
    }
    
    public Estoque getEstoque(String cdProduto, String cdLocalEstoque, String flOrigemEstoque) throws SQLException {
    	return getEstoque(cdProduto, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, cdLocalEstoque, flOrigemEstoque, null);
    }

    public Estoque getEstoque(String cdProduto, String cdLocalEstoque, String flOrigemEstoque, String flModoEstoque) throws SQLException {
    	return getEstoque(cdProduto, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, cdLocalEstoque, flOrigemEstoque, flModoEstoque);
    }

    public Estoque getEstoque(String cdProduto, String cdItemGrade1, String cdItemGrade2, String cdItemGrade3, String cdLocalEstoque, String flOrigemEstoque, String flModoEstoque) throws SQLException {
    	return getEstoque(cdProduto, cdItemGrade1, cdItemGrade2, cdItemGrade3, cdLocalEstoque, flOrigemEstoque, true, flModoEstoque);
    }
    
    public Estoque getEstoque(String cdProduto, String cdItemGrade1, String cdItemGrade2, String cdItemGrade3, String cdLocalEstoque, String flOrigemEstoque, boolean flDescontoEstoqueInterno, String flModoEstoque) throws SQLException {
		Estoque estoqueFilter = getEstoqueDomainExample(cdProduto, cdItemGrade1, cdItemGrade2, cdItemGrade3, cdLocalEstoque, flOrigemEstoque);
		Estoque estoque = null;
		estoque = (Estoque)findByRowKey(estoqueFilter.getRowKey());
		if (estoque == null) {
			return new Estoque();
		}
		if (LavenderePdaConfig.atualizarEstoqueInterno && Estoque.FLORIGEMESTOQUE_ERP.equals(flOrigemEstoque) && flDescontoEstoqueInterno) {
			estoque.qtEstoque = ValueUtil.round(estoque.qtEstoque) - ValueUtil.round(getQtEstoqueConsumidoPda(cdProduto, cdItemGrade1, cdItemGrade2, cdItemGrade3, cdLocalEstoque, flModoEstoque));
		}
		return estoque;
    }
    
    public Estoque getEstoqueIgnoraLocal(String cdProduto) throws SQLException {
    	Estoque estoque = getEstoque(cdProduto, Estoque.FLORIGEMESTOQUE_ERP);
    	Estoque estoqueFilter = getEstoqueDomainExample(cdProduto, Estoque.FLORIGEMESTOQUE_ERP);
    	if (estoque == null || estoque.cdEmpresa == null) {
    		estoqueFilter.cdLocalEstoque = null;
    		estoque = EstoquePdbxDao.getInstance().findEstoqueLimitOne(estoqueFilter);
    		if (LavenderePdaConfig.atualizarEstoqueInterno && estoque != null) {
				deduzEstoquePda(estoque, estoqueFilter);
			}
    	}
    	if (estoque != null) {
			if (LavenderePdaConfig.usaControleEstoquePrevistoParcial()) {
				setDadosParcialPrevisto(estoque);
			}
		}
    	if (estoque == null) {
    		estoque = new Estoque();
    	}
    	return estoque;
    }
    
    public void setDadosParcialPrevisto(Estoque estoque) {
    	double qtEstoqueParcialPrevisto = estoque.qtEstoque;
    	double qtEstoquePrevisto = estoque.qtEstoquePrevisto;
    	qtEstoqueParcialPrevisto = qtEstoqueParcialPrevisto - estoque.qtEstoquePrevisto;
		if (qtEstoqueParcialPrevisto < 0) {
			estoque.qtEstoquePrevisto += qtEstoqueParcialPrevisto;
		}
		estoque.qtEstoque = Math.max(0d, estoque.qtEstoque - qtEstoquePrevisto);
    }

	private void deduzEstoquePda(Estoque estoque, Estoque estoqueFilter) throws SQLException {
		estoqueFilter.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_PDA;
		Estoque estoquePda = EstoquePdbxDao.getInstance().findEstoqueLimitOne(estoqueFilter);
		if (estoquePda != null) {
			estoque.qtEstoque -= estoquePda.qtEstoque;
		}
	}
    
    public Estoque getEstoqueAgrupadorGrade(String cdLocalEstoque, String flOrigemEstoque, String dsAgrupadorGrade, ItemPedido itemPedido) throws SQLException{
    	Estoque estoqueFilter = getEstoqueDomainExample(null, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, cdLocalEstoque, flOrigemEstoque);
    	estoqueFilter.dsAgrupadorGrade = dsAgrupadorGrade;
    	estoqueFilter.setProduto(new Produto());
    	estoqueFilter.getProduto().itemTabelaPreco = itemPedido.getItemTabelaPreco();
    	estoqueFilter.getProduto().cdTipoPedidoFilter = itemPedido.pedido.cdTipoPedido;
    	estoqueFilter.getProduto().cdPlataformaVendaFilter = itemPedido.pedido.cdPlataformaVenda;
    	estoqueFilter.getProduto().cdCentroCustoProdFilter = itemPedido.pedido.cdCentroCusto;
    	estoqueFilter.getProduto().flExcecaoProduto = ValueUtil.isNotEmpty(itemPedido.pedido.getTipoPedido().flExcecaoProduto) ? itemPedido.pedido.getTipoPedido().flExcecaoProduto : ValueUtil.VALOR_NAO;
    	if (!LavenderePdaConfig.usaPrecoPorUf) {
    		estoqueFilter.getProduto().itemTabelaPreco.cdUf = ItemTabelaPreco.CDUF_VALOR_PADRAO;
    	}
    	Estoque estoque = EstoquePdbxDao.getInstance().findEstoqueAgrupadorGrade(estoqueFilter);
    	return estoque == null ? new Estoque() : estoque;
    }

    public double getQtEstoque(String cdProduto, String cdLocalEstoque) throws SQLException {
		if (LavenderePdaConfig.isConfigGradeProduto()) {
			return getSumEstoqueGradeProduto(cdProduto, cdLocalEstoque);
		} else {
    		ItemPedido itemPedido = new ItemPedido();
    		itemPedido.cdProduto = cdProduto;
    		itemPedido.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
    		itemPedido.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
    		itemPedido.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
    		return getQtEstoqueErpPda(itemPedido, cdLocalEstoque);
    	}
    }
    
    public double getQtEstoqueErpPdaComParcialPrevisto(ItemPedido itemPedido) throws SQLException {
    	double qtEstoqueErp = 0;
    	ItemPedido item = getItemPedidoValidateEstoque(itemPedido, LavenderePdaConfig.usaGradeProduto5());
		if (LavenderePdaConfig.isConfigGradeProduto()) {
    		Estoque estoqueFilter = new Estoque();
    		if(LavenderePdaConfig.isUsaLocalEstoque()) {
        		 estoqueFilter = getEstoqueDomainExample(item.cdProduto, null, null, null, itemPedido.getCdLocalEstoque(), Estoque.FLORIGEMESTOQUE_ERP);
        	}
    		else {
    			 estoqueFilter = getEstoqueDomainExample(item.cdProduto, null, null, null, null, Estoque.FLORIGEMESTOQUE_ERP);
    		}
    		Estoque sumEstoque = EstoquePdbxDao.getInstance().getSumEstoqueEstoquePrevistoGradeProduto(estoqueFilter);
    		estoqueFilter.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_PDA;
    		double somaEstoquePda = super.sumByExample(estoqueFilter, "QTESTOQUE");
    		return Math.max(0, sumEstoque.qtEstoque - somaEstoquePda - sumEstoque.qtEstoquePrevisto);
    	} else {
    		Estoque estoqueErp = getEstoque(item.cdProduto, item.cdItemGrade1, item.cdItemGrade2, item.cdItemGrade3, itemPedido.getCdLocalEstoque(), Estoque.FLORIGEMESTOQUE_ERP, null);
    		qtEstoqueErp = Math.max(0, estoqueErp.qtEstoque - estoqueErp.qtEstoquePrevisto);
    		return ValueUtil.round(qtEstoqueErp);
    	}
    }
    
    public Estoque setEstoqueItemComParcialPrevisto(Estoque estoque) {
    	estoque.qtEstoque = Math.max(0d, estoque.qtEstoque - estoque.qtEstoquePrevisto);
    	return estoque;
    }
    
    public double getQtEstoqueAgrupadorGrade(String cdLocalEstoque, String dsAgrupadorGrade) throws SQLException {
    	Estoque estoqueFilter = getEstoqueDomainExample(null, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, cdLocalEstoque, Estoque.FLORIGEMESTOQUE_ERP);
    	estoqueFilter.dsAgrupadorGrade = dsAgrupadorGrade;
    	double qtEstoque = EstoquePdbxDao.getInstance().sumQtEstoqueGrade(estoqueFilter);
    	estoqueFilter.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_PDA;
    	return qtEstoque - EstoquePdbxDao.getInstance().sumQtEstoqueGrade(estoqueFilter);
    }

    public double getQtEstoqueErp(String cdProduto) throws SQLException {
    	return getQtEstoqueErp(cdProduto, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, Estoque.CD_LOCAL_ESTOQUE_PADRAO);
    }
    
    public double getQtEstoqueErp(String cdProduto, String cdItemGrade1, String cdItemGrade2, String cdItemGrade3, String cdLocalEstoque) throws SQLException {
		Estoque estoqueFilter = getEstoqueDomainExample(cdProduto, cdItemGrade1, cdItemGrade2, cdItemGrade3, cdLocalEstoque, Estoque.FLORIGEMESTOQUE_ERP);
		double estoque = ValueUtil.getDoubleValue(findColumnByRowKey(estoqueFilter.getRowKey(), "QTESTOQUE"));

		return estoque;
    }

	public double getQtEstoqueErpRemessa(ItemPedido itemPedido, String cdItemGrade1, String cdItemGrade2, String cdItemGrade3, String cdLocalEstoque) throws SQLException {
		cdLocalEstoque = getCdLocalEstoque(cdLocalEstoque, LavenderePdaConfig.usaModoControleEstoquePorTipoPedido);
		return EstoquePdbxDao.getInstance().getSumQtEstoqueSaidaPor(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, itemPedido, cdLocalEstoque, Estoque.FLORIGEMESTOQUE_ERP);
	}

    public double getQtEstoqueConsumidoPda(String cdProduto, String cdItemGrade1, String cdItemGrade2, String cdItemGrade3, String cdLocalEstoque, String flModoEstoque) throws SQLException {
    	Estoque estoqueFilter = getEstoqueDomainExample(cdProduto, cdItemGrade1, cdItemGrade2, cdItemGrade3, cdLocalEstoque, Estoque.FLORIGEMESTOQUE_PDA, flModoEstoque, false);
    	return ValueUtil.getDoubleValue(findColumnByRowKey(estoqueFilter.getRowKey(), "QTESTOQUE"));
    }

	public double getQtEstoqueConsumidoPdaRemessa(ItemPedido itemPedido, String cdItemGrade1, String cdItemGrade2, String cdItemGrade3, String cdLocalEstoque, String flModoEstoque) throws SQLException {
		final boolean isUtilizaModoControleEstoquePorTipoPedido = LavenderePdaConfig.usaControleEstoquePorRemessa && LavenderePdaConfig.usaModoControleEstoquePorTipoPedido;
		final boolean isUtilizaEstoquePorLocalEstoqueDaEmpresa = isUtilizaModoControleEstoquePorTipoPedido && Pedido.isUtilizaLocalEstoqueDaEmpresa(flModoEstoque);
		cdLocalEstoque = getCdLocalEstoque(cdLocalEstoque, isUtilizaModoControleEstoquePorTipoPedido);

		if (LavenderePdaConfig.usaControleEstoquePorRemessa && !isUtilizaEstoquePorLocalEstoqueDaEmpresa) {
			return EstoquePdbxDao.getInstance().getSumQtEstoqueSaidaPor(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, itemPedido, cdLocalEstoque, Estoque.FLORIGEMESTOQUE_PDA);
		}
		Estoque estoqueFilter = getEstoqueDomainExample(itemPedido.cdProduto, cdItemGrade1, cdItemGrade2, cdItemGrade3, cdLocalEstoque, Estoque.FLORIGEMESTOQUE_PDA, flModoEstoque, false);
		return ValueUtil.getDoubleValue(findColumnByRowKey(estoqueFilter.getRowKey(), "QTESTOQUE"));
	}

	private String getCdLocalEstoque(String cdLocalEstoque, final boolean isUtilizaModoControleEstoquePorTipoPedido)
			throws SQLException {
		if (isUtilizaModoControleEstoquePorTipoPedido) {
    		Empresa empresa = new Empresa();
    		empresa.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		empresa = (Empresa) EmpresaService.getInstance().findByPrimaryKey(empresa);
    		cdLocalEstoque = empresa.cdLocalEstoque;
    	}
		return cdLocalEstoque;
	}

    public void updateEstoqueByEstoqueOnLine(Vector estoques) throws SQLException {
    	int size = estoques.size();
    	CrudDao estoqueDao = getCrudDao();
    	for (int i = 0; i < size; i++) {
    		Estoque estoqueNew = (Estoque) estoques.items[i];
    		Estoque estoqueOld = (Estoque) findByRowKey(estoqueNew.getRowKey());
			if (estoqueOld != null) {
    			if (BaseDomain.FLTIPOALTERACAO_EXCLUIDO.equals(estoqueNew.flTipoAlteracao)) {
    				delete(estoqueOld);
    			} else {
    				estoqueDao.update(estoqueNew);
    			}
    		} else {
    			estoqueDao.insert(estoqueNew);
    		}
		}
    }

    public int getMaxNuCarimbo() throws SQLException {
		return EstoquePdbxDao.getInstance().getMaxNuCarimbo();
    }

    public void updateEstoqueInterno(ItemPedido itemPedido, double qtUpdate, boolean flIncrementa, String flOrigemEstoque) throws SQLException {
    	updateEstoqueInterno(itemPedido, qtUpdate, flIncrementa, flOrigemEstoque, false);
    }
    
    public void updateEstoqueInterno(ItemPedido itemPedido, double qtUpdate, boolean flIncrementa, String flOrigemEstoque, boolean insereEstoqueRep) throws SQLException {
		updateEstoqueInterno(itemPedido, qtUpdate, flIncrementa, flOrigemEstoque, insereEstoqueRep, false);
    }
    
	public void updateEstoqueInterno(ItemPedido itemPedido, double qtUpdate, boolean flIncrementa, String flOrigemEstoque, boolean insereEstoqueRep, boolean isFromSync) throws SQLException {
		if ((LavenderePdaConfig.atualizarEstoqueInterno && !LavenderePdaConfig.usaControleEstoquePorRemessa) || itemPedido.pedido.utilizaEstoquePorLocalEstoqueDaEmpresa()) {
			if ((flIncrementa && qtUpdate < 0) || (!flIncrementa && qtUpdate > 0)) {
				qtUpdate *= -1d;
			}
			String cdLocalEstoque = getLocalEstoque(itemPedido);
			updateEstoqueInterno(itemPedido, qtUpdate, flOrigemEstoque, insereEstoqueRep, cdLocalEstoque);
			updateEstoqueInternoCondicaoNegociacao(itemPedido, qtUpdate, flOrigemEstoque, insereEstoqueRep);
		} else if (LavenderePdaConfig.atualizarEstoqueInterno && LavenderePdaConfig.usaControleEstoquePorRemessa && isFromSync) {
			Vector itemPedidoRemessaList = ItemPedidoRemessaService.getInstance().findAllItemPedidoRemessa(itemPedido);
			if (itemPedidoRemessaList != null) {
				for (int i = 0; i  < itemPedidoRemessaList.size(); i++) {
					ItemPedidoRemessa itemPedidoRemessa = (ItemPedidoRemessa) itemPedidoRemessaList.items[i];
					if ((flIncrementa && qtUpdate < 0) || (!flIncrementa && qtUpdate > 0)) {
						qtUpdate *= -1d;
					}
					updateEstoqueInterno(itemPedido, itemPedidoRemessa.qtEstoqueConsumido, flOrigemEstoque, insereEstoqueRep, itemPedidoRemessa.cdLocalEstoque);
				}
			}
		}
	}

	private String getLocalEstoque(ItemPedido itemPedido) throws SQLException {
		String cdLocalEstoque = null;
		if (LavenderePdaConfig.isUsaValidacaoEstoqueLocalEstCondNegociacao()) {
			if (itemPedido.pedido == null) {
				Pedido pedido = PedidoService.getInstance().findPedidoByItemPedido(itemPedido);
				itemPedido.pedido = pedido;
				itemPedido.pedido.setCliente(SessionLavenderePda.getCliente());
				itemPedido.pedido.setCondicaoNegociacao(CondicaoNegociacaoService.getInstance().findCondicaoNegociacao(pedido.cdCondNegociacao));
			}
			cdLocalEstoque = itemPedido.pedido.getCliente().cdLocalEstoque;
		} else {
			try {
				if (itemPedido.pedido == null) {
					Pedido pedido = PedidoService.getInstance().findPedidoByItemPedido(itemPedido);
					itemPedido.pedido = pedido;
				}
				cdLocalEstoque = itemPedido.getCdLocalEstoque();
			} catch (Throwable e) {
				throw new ValidationException(e.getMessage());
			}
		}
		if (cdLocalEstoque == null) {
			Pedido pedido = itemPedido.pedido;
			if (pedido.utilizaEstoquePorLocalEstoqueDaEmpresa()) {
				Empresa empresa = EmpresaService.getInstance().getEmpresa(pedido.cdEmpresa);
				cdLocalEstoque = empresa.cdLocalEstoque;
			}
		}
		return cdLocalEstoque == null ? Estoque.CD_LOCAL_ESTOQUE_PADRAO : cdLocalEstoque;
	}

	private void updateEstoqueInterno(ItemPedido itemPedido, double qtUpdate, String flOrigemEstoque, boolean insereEstoqueRep, String cdLocalEstoque) throws SQLException {
		String cdItemGrade1 = LavenderePdaConfig.usaGradeProduto5() ? ProdutoGrade.CD_ITEM_GRADE_PADRAO : itemPedido.cdItemGrade1;
		String cdItemGrade2 = LavenderePdaConfig.usaGradeProduto5() ? ProdutoGrade.CD_ITEM_GRADE_PADRAO : itemPedido.cdItemGrade2;
		String cdItemGrade3 = LavenderePdaConfig.usaGradeProduto5() ? ProdutoGrade.CD_ITEM_GRADE_PADRAO : itemPedido.cdItemGrade3;
		updateEstoqueInterno(itemPedido.cdProduto, cdItemGrade1, cdItemGrade2, cdItemGrade3, qtUpdate, flOrigemEstoque, insereEstoqueRep, cdLocalEstoque);
	}

	protected void updateEstoqueInterno(String cdProduto, String cdItemGrade1, String cdItemGrade2, String cdItemGrade3,  double qtUpdate, String flOrigemEstoque, boolean insereEstoqueRep, String cdLocalEstoque) throws SQLException {
		Estoque estoque = getEstoque(cdProduto, cdItemGrade1, cdItemGrade2, cdItemGrade3, cdLocalEstoque, flOrigemEstoque, false, null);
		if (estoque.cdProduto == null) {
			estoque = getEstoqueDomainExample(cdProduto, cdItemGrade1, cdItemGrade2, cdItemGrade3, cdLocalEstoque, flOrigemEstoque);
			estoque.qtEstoque = qtUpdate;
			insert(estoque);
		} else {
			estoque.qtEstoque += ValueUtil.round(qtUpdate);
			update(estoque);
		}
		if (insereEstoqueRep && !SessionLavenderePda.isDevolverEstoqueAtual) {
			EstoqueRepService.getInstance().insereEstoqueRepByEstoque(estoque);
		}
		if (estoque.qtEstoque < 0) {
			LogAppService.getInstance().logEstoque(cdProduto, "O estoque ficou negativo ao atualizar o estoque interno");
		}
	}

	private void updateEstoqueInternoCondicaoNegociacao(ItemPedido itemPedido, double qtUpdate, String flOrigemEstoque, boolean insereEstoqueRep) throws SQLException {
		CondicaoNegociacao condicaoNegociacao = itemPedido.pedido.getCondicaoNegociacao();
		if (LavenderePdaConfig.usaCondicaoNegociacaoNoPedido && condicaoNegociacao != null) {
			String cdLocalizacaoEstoque = condicaoNegociacao.cdLocalEstoque; 
			String cdLocalizacaoCliente = itemPedido.pedido.getCliente().cdLocalEstoque;
			Estoque estoque = null;
			if (ValueUtil.isNotEmpty(cdLocalizacaoEstoque) && !ValueUtil.valueEquals(cdLocalizacaoEstoque, cdLocalizacaoCliente))  {
				estoque = getEstoqueDomainExample(itemPedido.cdProduto, itemPedido.cdItemGrade1, itemPedido.cdItemGrade2, itemPedido.cdItemGrade3, cdLocalizacaoEstoque, Estoque.FLORIGEMESTOQUE_ERP);
				estoque = (Estoque)findByRowKey(estoque.getRowKey());
			}
			if (estoque != null) {
				if (LavenderePdaConfig.usaEstoqueInternoParcialmenteLocalEstoqueCondNeg) {
					qtUpdate = CondicaoNegociacaoService.getInstance().getQtConsumidaDeEstoque(qtUpdate, condicaoNegociacao);
				}
				updateEstoqueInterno(itemPedido, qtUpdate, flOrigemEstoque, insereEstoqueRep, cdLocalizacaoEstoque);
			}
			if (estoque.qtEstoque < 0) {
				LogAppService.getInstance().logEstoque(itemPedido.cdProduto, "O estoque ficou negativo ao atualizar o estoque interno por condição de negociação");
			}
		}
	}

	public void updateEstoqueByEstoqueOnLine(String cdProduto, String cdItemGrade1, String cdItemGrade2, String cdItemGrade3, double novaQtde) throws SQLException {
    	Estoque estoque = getEstoque(cdProduto, cdItemGrade1, cdItemGrade2, cdItemGrade3, Estoque.CD_LOCAL_ESTOQUE_PADRAO, Estoque.FLORIGEMESTOQUE_ERP, null);
    	if (estoque.cdProduto == null) {
    		estoque = getEstoqueDomainExample(cdProduto, Estoque.FLORIGEMESTOQUE_ERP);
    		estoque.qtEstoque = novaQtde;
    		insert(estoque);
    	} else {
    		estoque.qtEstoque = novaQtde;
    		update(estoque);
    	}
		if (estoque.qtEstoque < 0) {
			LogAppService.getInstance().logEstoque(cdProduto, "O estoque ficou negativo ao atualizar o estoque interno por estoque online");
		}
    }
    
    private Estoque getEstoqueDomainExample(String cdProduto, String flOrigemEstoque) {
    	return getEstoqueDomainExample(cdProduto, null, flOrigemEstoque);
    }
    
    private Estoque getEstoqueDomainExample(String cdProduto, String cdLocalEstoque, String flOrigemEstoque) {
    	return getEstoqueDomainExample(cdProduto, null, null, null, cdLocalEstoque, flOrigemEstoque);
    }
    
    private Estoque getEstoqueDomainExample(String cdProduto, String cdItemGrade1, String cdItemGrade2, String cdItemGrade3, String cdLocalEstoque, String flOrigemEstoque) {
    	return getEstoqueDomainExample(cdProduto, cdItemGrade1, cdItemGrade2, cdItemGrade3, cdLocalEstoque, flOrigemEstoque, null, false);
    }
    
    
    private Estoque getEstoqueDomainExample(String cdProduto, String cdItemGrade1, String cdItemGrade2, String cdItemGrade3, String cdLocalEstoque, String flOrigemEstoque, String flModoEstoque, boolean estoqueEmpresa) {
    	Estoque estoqueFilter = new Estoque();
    	estoqueFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	estoqueFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	estoqueFilter.cdProduto = cdProduto;
		estoqueFilter.cdItemGrade1 = ValueUtil.isEmpty(cdItemGrade1) ?  ProdutoGrade.CD_ITEM_GRADE_PADRAO : cdItemGrade1;
		estoqueFilter.cdItemGrade2 = ValueUtil.isEmpty(cdItemGrade2) ?  ProdutoGrade.CD_ITEM_GRADE_PADRAO : cdItemGrade2;
		estoqueFilter.cdItemGrade3 = ValueUtil.isEmpty(cdItemGrade3) ?  ProdutoGrade.CD_ITEM_GRADE_PADRAO : cdItemGrade3;
		estoqueFilter.cdLocalEstoque = ValueUtil.isEmpty(cdLocalEstoque) ?  Estoque.CD_LOCAL_ESTOQUE_PADRAO : cdLocalEstoque;
    	estoqueFilter.flOrigemEstoque = flOrigemEstoque;
    	estoqueFilter.estoqueEmpresa = estoqueEmpresa;
    	if (LavenderePdaConfig.isFilterFlModoEstoque()) {
    		estoqueFilter.flModoEstoque = flModoEstoque;
    	}
    	return estoqueFilter;
    }

    public void validateEstoque(Pedido pedido, ItemPedido itemPedido, boolean editing) throws SQLException {
    	if (LavenderePdaConfig.usaControleEstoquePorEstoquePrevisto()) {
    		Double qtdEstoquePrevisto = EstoquePrevistoService.getInstance().validaEstoquePrevisto(pedido.dtEntrega, itemPedido);
			if (qtdEstoquePrevisto != null && qtdEstoquePrevisto > 0) return;
    	}
    	
    	if (isNotTipoPedidoRemessa(pedido) && ((LavenderePdaConfig.usaControleEstoqueProdutoRateioProducao && ProducaoProdService.getInstance().hasProducaoProduto(itemPedido)) || (LavenderePdaConfig.isAvisaVendaProdutoSemEstoque() && itemPedido.getProduto() != null && !itemPedido.getProduto().isIgnoraValidacao()) || LavenderePdaConfig.bloquearVendaProdutoSemEstoque || LavenderePdaConfig.usaRegistroProdutoFaltante || LavenderePdaConfig.isPermiteDecidirModoRegistroFaltaEstoqueProduto() || LavenderePdaConfig.bloqueiaFechamentoPedidoProdutoSemEstoque)) {
    		double qtEstoque = 0d;
    		itemPedido.getItemPedidoGradeErroList().clear();	
    		try {
    			if (pedido.isIgnoraControleEstoque(itemPedido) || pedido.ignoraValidacaoEstoque ) {
    				return;
    			}
				if (LavenderePdaConfig.isUsaGradeProduto1A4()) {
    				int size = itemPedido.itemPedidoGradeList.size();
    				if (size > 0) {
    					ItemPedidoGrade itemPedidoGrade = null;
    					for (int i = 0; i < size; i++) {
    						itemPedidoGrade = (ItemPedidoGrade)itemPedido.itemPedidoGradeList.items[i];
    						qtEstoque = getQtEstoqueErpPda(itemPedidoGrade, itemPedido.getCdLocalEstoque());
    						double qtEstoqueFuturo = ValueUtil.round(calcSaldoEstoque(itemPedido, itemPedidoGrade.qtItemFisico, qtEstoque));
    						if (qtEstoqueFuturo < 0) {
    							itemPedidoGrade.dsMotivoGradeNaoInserida = MessageUtil.getMessage(Messages.MSG_ESTOQUE_GRADE_INSUFICIENTE_INDIVIDUAL, new String[]{getSaldoEstoque((qtEstoque + qtEstoqueFuturo))});
    							itemPedido.getItemPedidoGradeErroList().add(itemPedidoGrade);
    						}
    					}
    					if (itemPedido.getItemPedidoGradeErroList().size() > 0) {
    						if (!LavenderePdaConfig.isAvisaVendaProdutoSemEstoqueComDetalhes()) {
    							throw new ValidationException(Messages.MSG_ESTOQUE_GRADE_INSUFICIENTE);
    						}
    					}
    				} else {
    					qtEstoque = validaEstoqueItemPedido(itemPedido, editing, qtEstoque);
    				}
    			} else {
    				if (LavenderePdaConfig.isUsaValidacaoEstoqueLocalEstCondNegociacao()) {
    					validaEstoqueEmMultiplosLocais(itemPedido, editing);
    				} else {
	    				qtEstoque = getQtEstoqueErpPda(itemPedido, itemPedido.getCdLocalEstoque());
					    double qtItemFisico = itemPedido.getQtItemFisicoOrg() == 0 && itemPedido.isGondola() ? itemPedido.qtItemGondola : itemPedido.getQtItemFisicoOrg();
					    double saldoEstoque = calcSaldoEstoque(itemPedido, qtItemFisico, qtEstoque);
	    				if (LavenderePdaConfig.atualizarEstoqueInterno && (editing || (LavenderePdaConfig.isPermiteEditarDescontoOuQuantidade() && itemPedido.itemUpdated))) {
	    					saldoEstoque += itemPedido.oldQtEstoqueConsumido;
	    				}
	    				Produto produto = itemPedido.getProduto();
	    				if (isSaldoEstoqueNegativo(qtItemFisico, saldoEstoque, produto) && LavenderePdaConfig.bloquearVendaProdutoSemEstoque && !produto.isPermiteEstoqueNegativo() || isUsaRegistroProdutoFaltante(itemPedido)) {
						    ValidationException validationException = new ValidationException(getMensagemEstoqueInsuficiente(itemPedido, qtEstoque, saldoEstoque));
						    validationException.extraParams = Messages.ITEM_KIT_IDENTIFY_ERRO_ESTOQUE;
						    throw validationException;
						    
	    				} else if (isSaldoEstoqueNegativo(qtItemFisico, saldoEstoque, produto) && !LavenderePdaConfig.bloquearVendaProdutoSemEstoque && !produto.isPermiteEstoqueNegativo()) {
    					    throw new ValidationException(getMensagemEstoqueInsuficiente(itemPedido, qtEstoque, saldoEstoque));
	    					  
	    				} else if (isSaldoEstoqueNegativo(qtItemFisico, saldoEstoque, produto) && LavenderePdaConfig.isAvisaVendaProdutoSemEstoqueSemDetalhes() && !produto.isPermiteEstoqueNegativo()) {
							throw new ValidationException(getMensagemEstoqueInsuficiente(itemPedido, qtEstoque, saldoEstoque));
	    				}
	    			}
    			}
			} catch (Throwable e) {
				if ((LavenderePdaConfig.usaControleEstoqueProdutoRateioProducao && ProducaoProdService.getInstance().hasProducaoProduto(itemPedido) || LavenderePdaConfig.bloquearVendaProdutoSemEstoque) && LavenderePdaConfig.liberaComSenhaVendaProdutoSemEstoque) {
					throw new EstoqueException(e.getMessage(), new Object[]{qtEstoque});
				} else {
					throw e;
				}
			}
    	}
    }

	private boolean isSaldoEstoqueNegativo(double qtItemFisico, double saldoEstoque, Produto produto) {
		return ValueUtil.round(saldoEstoque) < 0 && qtItemFisico > 0 && produto != null;
	}
	
	private boolean isUsaRegistroProdutoFaltante(ItemPedido itemPedido) {
		return LavenderePdaConfig.usaRegistroProdutoFaltante && itemPedido.fromProdutoFaltaWindow;
	}

	public void apresentaEstoquePrevisto(ItemPedido itemPedido, double saldoEstoque, boolean onReplicacao, boolean isPedidoMultiplaInsercao, Vector itensInseridosAdvertencia) throws SQLException {
		Estoque estoque = EstoqueService.getInstance().findDadosPrevistoParaEstoque(itemPedido);
		saldoEstoque+=itemPedido.getOldQtItemFisico();
		if (saldoEstoque >= itemPedido.getQtItemFisico()) return;
		
		double saldoRestante = itemPedido.getQtItemFisico() - saldoEstoque ;
		itemPedido.dtEstoquePrevisto = null;
		String qtVendida = getEstoqueToString(itemPedido.getQtItemFisico());
		String estoqueAtual = getEstoqueToString(saldoEstoque);
		String estoquePrevisto = getEstoquePrevistoToString(estoque.qtEstoquePrevisto);
		String mensagem = ValueUtil.VALOR_NI;
		if (estoque.qtEstoquePrevisto >= saldoRestante) {
			itemPedido.qtEstoquePrevisto = saldoRestante;
			itemPedido.dtEstoquePrevisto = estoque.dtEstoquePrevisto;
			String dtEntrega = StringUtil.getStringValue(itemPedido.dtEstoquePrevisto);
			mensagem = MessageUtil.getMessage(Messages.MGS_POSSUI_ESTOQUE_PREVISTO, new String[]{qtVendida, estoqueAtual, estoquePrevisto, dtEntrega});
		} else {
			itemPedido.qtEstoquePrevisto = estoque.qtEstoquePrevisto - saldoEstoque*-1;
			mensagem = MessageUtil.getMessage(Messages.MGS_NAO_POSSUI_ESTOQUE_PREVISTO, new String[]{qtVendida, estoqueAtual, estoquePrevisto});
		}
		if (onReplicacao) {
			throw new EstoquePrevistoException(itemPedido.cdProduto + " - " + mensagem);
		} else if (isPedidoMultiplaInsercao) {
			itensInseridosAdvertencia.addElement(new ProdutoErro(itemPedido.getProduto(), itemPedido.cdProduto, mensagem));
		} else {
			UiUtil.showInfoMessage(mensagem);
		}
	}
	
	private boolean isNotTipoPedidoRemessa(Pedido pedido) {
		return !LavenderePdaConfig.usaControleEstoquePorRemessa || pedido.utilizaEstoquePorLocalEstoqueDaEmpresa();
	}

	private double validaEstoqueItemPedido(ItemPedido itemPedido, boolean editing, double qtEstoque) throws SQLException {
		if (LavenderePdaConfig.isUsaValidacaoEstoqueLocalEstCondNegociacao()) {
			validaEstoqueEmMultiplosLocais(itemPedido, editing);
		} else {
			qtEstoque = getQtEstoqueErpPda(itemPedido, itemPedido.getCdLocalEstoque());
			double saldoEstoque = calcSaldoEstoque(itemPedido, itemPedido.getQtItemFisicoOrg(), qtEstoque);
			if (LavenderePdaConfig.atualizarEstoqueInterno && editing) {
				saldoEstoque += itemPedido.oldQtEstoqueConsumido;
			}
			if (ValueUtil.round(saldoEstoque) < 0 && itemPedido.getQtItemFisico() > 0) {
				if (!LavenderePdaConfig.isAvisaVendaProdutoSemEstoqueComDetalhes()) {
					throw new ValidationException(getMensagemEstoqueInsuficiente(itemPedido, qtEstoque, saldoEstoque));
				}
			}
		}
		return qtEstoque;
	}
    
    private String getSaldoEstoque(double saldoEstoque) {
    	saldoEstoque = saldoEstoque < 0 ? saldoEstoque * -1 : saldoEstoque;
    	return StringUtil.getStringValueToInterface(saldoEstoque, isEstoqueInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
	}
    
    private boolean isEstoqueInteiro() {
    	if (ValueUtil.VALOR_SIM.equals(LavenderePdaConfig.usaQtdItemPedidoInteiro) || "1".equals(LavenderePdaConfig.usaQtdItemPedidoInteiro)) {
    		return true;
    	}
    	return false;
    }

	public double calcSaldoEstoque(ItemPedido itemPedido, double qtItemFisico, double qtEstoque) throws SQLException {
		return ValueUtil.round(qtEstoque) - ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, qtItemFisico, false);
	}
    
    
    private void validaEstoqueEmMultiplosLocais(ItemPedido itemPedido, boolean editing) throws SQLException {
    	if (itemPedido.getQtItemFisico() > 0) {
	    	String cdLocalEstoqueCliente = itemPedido.pedido.getCliente().cdLocalEstoque;
			double qtEstoqueCliente = getQtEstoqueErpPda(itemPedido, cdLocalEstoqueCliente);
			double qtEstoqueCondicaoNegociacao = 0;
			String cdLocalEstoqueNegociacao = itemPedido.pedido.getCondicaoNegociacao().cdLocalEstoque;
			Estoque estoqueCondicaoNegociacao = null; 
			if (ValueUtil.isNotEmpty(cdLocalEstoqueNegociacao) && !ValueUtil.valueEquals(cdLocalEstoqueCliente, cdLocalEstoqueNegociacao)) {
				estoqueCondicaoNegociacao = getEstoqueDomainExample(itemPedido.cdProduto, itemPedido.cdItemGrade1, itemPedido.cdItemGrade2, itemPedido.cdItemGrade3, cdLocalEstoqueNegociacao, Estoque.FLORIGEMESTOQUE_ERP);
				estoqueCondicaoNegociacao = (Estoque)findByRowKey(estoqueCondicaoNegociacao.getRowKey());
			}
			double saldoEstoque = ValueUtil.round(qtEstoqueCliente) - ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico());
			if (LavenderePdaConfig.atualizarEstoqueInterno && editing) {
				saldoEstoque += itemPedido.oldQtEstoqueConsumido;
			}
			if (ValueUtil.round(saldoEstoque) < 0) {
				throw new ValidationException(getMensagemEstoqueInsuficiente(itemPedido, Cliente.class.getSimpleName(), cdLocalEstoqueCliente, qtEstoqueCliente, saldoEstoque));
			}
			if (estoqueCondicaoNegociacao != null) {
				qtEstoqueCondicaoNegociacao = getQtEstoqueErpPda(itemPedido, cdLocalEstoqueNegociacao);;
				int qtConsumidaDeEstoque = CondicaoNegociacaoService.getInstance().getQtConsumidaDeEstoque(itemPedido.getQtItemFisico(), itemPedido.pedido.getCondicaoNegociacao());
				if (qtConsumidaDeEstoque > 0) {
					saldoEstoque = ValueUtil.round(qtEstoqueCondicaoNegociacao) - ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, qtConsumidaDeEstoque);
				} else {
					saldoEstoque = ValueUtil.round(qtEstoqueCondicaoNegociacao) - ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico());
				}
				if (LavenderePdaConfig.atualizarEstoqueInterno && editing) {
					saldoEstoque += itemPedido.oldQtEstoqueConsumido;
				}
				if (ValueUtil.round(saldoEstoque) < 0) {
					throw new ValidationException(getMensagemEstoqueInsuficiente(itemPedido, CondicaoNegociacao.class.getSimpleName(), StringUtil.getStringValue(cdLocalEstoqueNegociacao), qtEstoqueCondicaoNegociacao, saldoEstoque));
				}
			}
    	}
	}

    public double getQtEstoqueErpPda(BaseDomain domain, String cdLocalEstoque) throws SQLException {
    	double qtEstoqueErp = 0;
    	ItemPedido item = getItemPedidoValidateEstoque(domain, LavenderePdaConfig.usaGradeProduto5());
		if (LavenderePdaConfig.usaControleEstoquePorRemessa) {
			qtEstoqueErp = getQtEstoqueErpRemessa(item, item.cdItemGrade1, item.cdItemGrade2, item.cdItemGrade3, cdLocalEstoque);
		} else {
			qtEstoqueErp = getQtEstoqueErp(item.cdProduto, item.cdItemGrade1, item.cdItemGrade2, item.cdItemGrade3, cdLocalEstoque);
		}
    	if (LavenderePdaConfig.atualizarEstoqueInterno) {
    		qtEstoqueErp = qtEstoqueErp - getQtEstoqueConsumidoPda(item.cdProduto, item.cdItemGrade1, item.cdItemGrade2, item.cdItemGrade3, cdLocalEstoque, null);
    	}
    	return ValueUtil.round(qtEstoqueErp);
    }

	private ItemPedido getItemPedidoValidateEstoque(BaseDomain domain, boolean usaGradeProduto5) {
		ItemPedido item = new ItemPedido();
    	if (domain instanceof ItemPedidoGrade) {
    		ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade) domain;
			item.cdProduto = itemPedidoGrade.cdProduto;
    		item.cdItemGrade1 = itemPedidoGrade.cdItemGrade1;
    		item.cdItemGrade2 = itemPedidoGrade.cdItemGrade2;
    		item.cdItemGrade3 = itemPedidoGrade.cdItemGrade3;
    	} else if (domain instanceof ItemPedido) {
    		ItemPedido itemPedido = (ItemPedido) domain;
			item.cdProduto = itemPedido.cdProduto;
    		item.cdItemGrade1 = usaGradeProduto5 ? ProdutoGrade.CD_ITEM_GRADE_PADRAO : itemPedido.cdItemGrade1;
    		item.cdItemGrade2 = usaGradeProduto5 ? ProdutoGrade.CD_ITEM_GRADE_PADRAO : itemPedido.cdItemGrade2;
    		item.cdItemGrade3 = usaGradeProduto5 ? ProdutoGrade.CD_ITEM_GRADE_PADRAO : itemPedido.cdItemGrade3;
    	}
		return item;
	}

    public String getMensagemEstoqueInsuficiente(ItemPedido itemPedido, double qtEstoque, double saldoEstoque) throws SQLException {
    	String msg = "";
	    double saldoEstoqueNegativo = saldoEstoque * -1;
		if (LavenderePdaConfig.bloqueiaFechamentoPedidoProdutoSemEstoque) {
			double qtItemFisico = itemPedido.getQtItemFisico() == 0 && itemPedido.isGondola() ? itemPedido.qtItemGondola : itemPedido.getQtItemFisico();
			String dsQtEstoque = LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? StringUtil.getStringValueToInterface((int) qtEstoque) : StringUtil.getStringValueToInterface(qtEstoque);
			String dsQtItemFisico = LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? StringUtil.getStringValueToInterface((int) qtItemFisico) : StringUtil.getStringValueToInterface(qtItemFisico);
			msg += MessageUtil.getMessage(Messages.ESTOQUE_INSUFICIENTE_FECHARPEDIDO,
				new String[] { dsQtItemFisico,
					LavenderePdaConfig.usaLocalEstoque() ?	Messages.MSG_LOCAL_ESTOQUE + " " + itemPedido.getCdLocalEstoque() : "",
					itemPedido.cdProduto,
					ProdutoService.getInstance().getDsProduto(itemPedido.cdProduto),
					dsQtEstoque});
		} else {
			msg = MessageUtil.getMessage(Messages.MSG_ESTOQUE_INSUFICIENTE, StringUtil.getStringValueToInterface(
						saldoEstoqueNegativo, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface
					));
		}
    	return msg;
    }

    private String getMensagemEstoqueInsuficiente(ItemPedido itemPedido, String nmEntidade, String cdLocalEstoque, double qtEstoque, double saldoEstoque) throws SQLException {
	    String dsQtEstoque = LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? StringUtil.getStringValueToInterface((int) qtEstoque) : StringUtil.getStringValueToInterface(qtEstoque);
	    String dsQtItemFisico = LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? StringUtil.getStringValueToInterface((int) itemPedido.getQtItemFisico()) : StringUtil.getStringValueToInterface(itemPedido.getQtItemFisico());
	    return MessageUtil.getMessage(Messages.ESTOQUE_INSUFICIENTE_FECHARPEDIDO,
				new String[] { dsQtItemFisico,
					Messages.MSG_LOCAL_ESTOQUE + nmEntidade + " " + cdLocalEstoque,
					itemPedido.cdProduto,
					ProdutoService.getInstance().getDsProduto(itemPedido.cdProduto),
					dsQtEstoque});
    }

    public double calculaEstoqueByProdutoUnidade(ItemTabelaPreco itemTabelaPreco, ProdutoUnidade produtoUnidade, double qtEstoque) {
		if (LavenderePdaConfig.usaUnidadeAlternativa && qtEstoque != 0) {
			if (produtoUnidade != null) {
				produtoUnidade.nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemTabelaPreco, produtoUnidade);
				if (produtoUnidade.isMultiplica()) {
					qtEstoque = ValueUtil.round(qtEstoque / produtoUnidade.nuConversaoUnidade);
				} else {
					qtEstoque = qtEstoque * produtoUnidade.nuConversaoUnidade;
				}
			}
		}
		if (LavenderePdaConfig.usaUnidadeAlternativa && (LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO))) {
			return ValueUtil.getIntegerValueTruncated(qtEstoque);
		} else if (LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO)) {
			return ValueUtil.getIntegerValue(qtEstoque);
		}
		return qtEstoque;
    }

    public void ajustaEstoqueItemPedido(Pedido pedido, ItemPedido itemPedido, boolean usaEstoqueDisponivel) throws SQLException {
    	if (LavenderePdaConfig.bloquearVendaProdutoSemEstoque || LavenderePdaConfig.bloqueiaFechamentoPedidoProdutoSemEstoque || (LavenderePdaConfig.isAvisaVendaProdutoSemEstoque() && !itemPedido.getProduto().isIgnoraValidacao())) {
    		try {
    			validateEstoque(pedido, itemPedido, false);
    		} catch (ValidationException e) {
				if (itemPedido.getProduto().isPermiteEstoqueNegativo()) {
					pedido.itemPedidoInseridoDivergList.addElement(itemPedido);
					itemPedido.dsMotivoItemNaoInseridoSugestaoPedido = e.getMessage();
				} else {
					throw e;
				}
			}
			if (LavenderePdaConfig.isConfigGradeProduto()) {
    			if (itemPedido.itemPedidoGradeList.size() > 0) {
    				ItemPedidoGrade itemPedidoGrade = null;
    				int size = itemPedido.itemPedidoGradeList.size();
    				double qtEstoqueAjuste = 0d;
    				for (int i = 0; i < size; i++) {
    					itemPedidoGrade = (ItemPedidoGrade)itemPedido.itemPedidoGradeList.items[i];
    					qtEstoqueAjuste += itemPedidoGrade.qtItemFisico;
    				}
    				itemPedido.setQtItemFisico(qtEstoqueAjuste);
    			} else {
    				ajustaEstoqueItemPedidoSemGrade(pedido, itemPedido, usaEstoqueDisponivel);
    			}
    		} else {
    			ajustaEstoqueItemPedidoSemGrade(pedido, itemPedido, usaEstoqueDisponivel);
    		}
    	}
	}

	private void ajustaEstoqueItemPedidoSemGrade(Pedido pedido, ItemPedido itemPedido, boolean usaEstoqueDisponivel) throws SQLException {
		double qtEstoque = EstoqueService.getInstance().getQtEstoqueErpPda(itemPedido, itemPedido.getCdLocalEstoque());
		qtEstoque = EstoqueService.getInstance().calculaEstoqueByProdutoUnidade(itemPedido.getItemTabelaPreco(), itemPedido.getProdutoUnidade(), qtEstoque);
		double saldoEstoque = ValueUtil.round(qtEstoque) - itemPedido.getQtItemFisico();
		if (saldoEstoque < 0 && itemPedido.getQtItemFisico() > 0) {
			if (usaEstoqueDisponivel && qtEstoque > 0) {
				itemPedido.qtItemFisicoNaoInseridoSugestaoPedido = itemPedido.getQtItemFisico();
				itemPedido.setQtItemFisico(qtEstoque);
				pedido.itemPedidoDivergenciaEstoqueSugestaoPedList.addElement(itemPedido);
			} else if (!ProdutoService.getInstance().getProduto(itemPedido.cdProduto).isPermiteEstoqueNegativo() && !pedido.inserindoFromSugestaoPedido) {
				throw new ValidationException(MessageUtil.getMessage(Messages.MSG_ESTOQUE_INSUFICIENTE, StringUtil.getStringValue(saldoEstoque * -1)));
			}
		}
	}
    

    public void recalculaEstoqueConsumido(String cdProduto) throws SQLException {
    	recalculaEstoqueConsumido(cdProduto, false);
    }

    public void recalculaEstoqueConsumido(String cdProduto, boolean isFromSync) throws SQLException {
    	ItemPedido itemPedidoFilter = new ItemPedido();
		if (ValueUtil.isNotEmpty(cdProduto)) {
			itemPedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			itemPedidoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			try {
				Estoque estoque = new Estoque();
				estoque.cdEmpresa = SessionLavenderePda.cdEmpresa;
				estoque.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
				estoque.cdProduto = cdProduto;
				estoque.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_PDA;
				deleteAllByExample(estoque);
				if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido()) {
					estoque.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_TROCA;
					deleteAllByExample(estoque);
				}
			} catch (Throwable e) { }
		} else {
	    	Estoque estoque = new Estoque();
	    	estoque.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_PDA;
	    	deleteAllByExample(estoque);
	    	if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido()) {
	    		estoque.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_TROCA;
	    		deleteAllByExample(estoque);
	    	}
    	}
		itemPedidoFilter.flOrigemPedido = Estoque.FLORIGEMESTOQUE_PDA;
		itemPedidoFilter.cdProduto = cdProduto;
		Vector listItemPedido = ItemPedidoService.getInstance().findAllByExampleUnique(itemPedidoFilter);
		if (LavenderePdaConfig.isAtualizaEstoqueComReservaCentralizada()) {
			listItemPedido = getListProdutoSemFlReserva(listItemPedido); 
		}
		for (int i = 0; i < listItemPedido.size(); i++) { 
			ItemPedido itemPedido = (ItemPedido) listItemPedido.items[i];
			Vector itemPedidoGradeList = LavenderePdaConfig.isConfigGradeProduto() ? ItemPedidoGradeService.getInstance().getItemPedidoGradeByItemPedido(itemPedido) : null;
			if (ValueUtil.isNotEmpty(itemPedidoGradeList)) {
				atualizaEstoqueGrade(itemPedido, itemPedidoGradeList, isFromSync);
			} else {
				if (isEstoqueConsumidoValido(itemPedido)) {
					String flOrigemEstoque = LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && itemPedido.isItemTrocaRecolher() ? Estoque.FLORIGEMESTOQUE_TROCA : Estoque.FLORIGEMESTOQUE_PDA;
					itemPedido.pedido = PedidoService.getInstance().findPedidoByItemPedido(itemPedido);
					updateEstoqueInterno(itemPedido, ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico()), true, flOrigemEstoque, false, true);
				}
			}
		}
    }
    
    private boolean isEstoqueConsumidoValido(ItemPedido itemPedido) throws SQLException {
    	if (SessionLavenderePda.usuarioPdaRep == null) {
    		return false;
    	}
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		pedidoFilter.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		pedidoFilter.flOrigemPedido = Estoque.FLORIGEMESTOQUE_PDA;
		pedidoFilter.nuPedido = itemPedido.nuPedido;
		Pedido pedido = (Pedido) PedidoService.getInstance().findByRowKey(pedidoFilter.getRowKey());

		if (pedido == null) {
			return false;
		} else if (LavenderePdaConfig.bloquearVendaProdutoSemEstoque && pedido.isIgnoraControleEstoque()) {
			return false;
		} else if ((LavenderePdaConfig.isUsaReservaEstoqueCorrente() || LavenderePdaConfig.isUsaReservaEstoqueCentralizado()) && !pedido.isPedidoSemReservaEstoque()) {
			return false;
		} else if (!(pedido.isPedidoAberto() || pedido.isPedidoFechado())) {
			return false;
		}

		return LavenderePdaConfig.bloquearVendaProdutoSemEstoque && !pedido.isIgnoraControleEstoque() || !LavenderePdaConfig.bloquearVendaProdutoSemEstoque;
	}
    
    public void updateEstoquePdaToERP(Pedido pedido) throws SQLException {
    	ItemPedido itemPedidoFilter = new ItemPedido();
		itemPedidoFilter.cdEmpresa = pedido.cdEmpresa;
		itemPedidoFilter.cdRepresentante = pedido.cdRepresentante;
		itemPedidoFilter.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		itemPedidoFilter.flOrigemPedido = Estoque.FLORIGEMESTOQUE_PDA;
		itemPedidoFilter.nuPedido = pedido.nuPedido;
		Vector listItemPedido = ItemPedidoService.getInstance().findAllByExampleUnique(itemPedidoFilter);
		for (int i = 0; i < listItemPedido.size(); i++) { 
			ItemPedido itemPedido = (ItemPedido) listItemPedido.items[i];
			itemPedido.setPedido(pedido);
			boolean isItemTroca = isUsaTrocaRecolherComoDescontoPagamentoPedido(pedido, itemPedido);
			String flOrigemEstoquePda = isItemTroca ? Estoque.FLORIGEMESTOQUE_TROCA : Estoque.FLORIGEMESTOQUE_PDA;
			double qtItemFisicoConversaoUnidade = ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico());
			if (LavenderePdaConfig.isUsaGradeProduto1A4() && ItemPedidoGradeService.getInstance().hasItemPedidoGrade(itemPedido)) {
				ItemPedidoService.getInstance().atualizaEstoqueGrade(itemPedido, false, flOrigemEstoquePda);
				ItemPedidoService.getInstance().atualizaEstoqueGrade(itemPedido, isItemTroca, Estoque.FLORIGEMESTOQUE_ERP);
			} else {
			updateEstoqueInterno(itemPedido, qtItemFisicoConversaoUnidade, false, flOrigemEstoquePda);
			updateEstoqueInterno(itemPedido, qtItemFisicoConversaoUnidade, isItemTroca, Estoque.FLORIGEMESTOQUE_ERP, LavenderePdaConfig.isEnviaEstoqueRepParaServidor() && !LavenderePdaConfig.isEnviaEstoqueRepParaServidorDevolveEstoqueAtual());
		}
    }
    }

	private boolean isUsaTrocaRecolherComoDescontoPagamentoPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		return LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && itemPedido.isItemTrocaRecolher() && pedido.isPedidoVenda();
	}

    private void atualizaEstoqueGrade(ItemPedido itemPedido, Vector itemPedidoGradeList, boolean isFromSync) throws SQLException {
    	for (int j = 0; j < itemPedidoGradeList.size(); j++) { 
			ItemPedidoGrade itemGrade = (ItemPedidoGrade) itemPedidoGradeList.items[j];
			if (itemPedido.nuPedido.equals(itemGrade.nuPedido) && itemPedido.cdProduto.equals(itemGrade.cdProduto)) {
				itemPedido.cdItemGrade1 = itemGrade.cdItemGrade1;
				itemPedido.cdItemGrade2 = itemGrade.cdItemGrade2;
				itemPedido.cdItemGrade3 = itemGrade.cdItemGrade3;
				itemPedido.setQtItemFisico(itemGrade.qtItemFisico);
				updateEstoqueInterno(itemPedido, itemPedido.getQtItemFisico(), true, Estoque.FLORIGEMESTOQUE_PDA, isFromSync);
			}
		}
    }

    //@Override
    public double sumByExample(BaseDomain domain, String column) throws SQLException {
    	double somaEstoque =  super.sumByExample(domain, column);
    	double somaEstoquePda = 0;
    	if (LavenderePdaConfig.atualizarEstoqueInterno) {
    		Estoque estoque = (Estoque) domain;
    		estoque.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_PDA;
    		somaEstoquePda = super.sumByExample(estoque, column);
    	}
    	return somaEstoque - somaEstoquePda;
    }

	public void devolveEstoqueAtual() throws SQLException {
		if (SessionLavenderePda.isDevolverEstoqueAtual) {
			LogSync.info(Messages.DEVOLVENDO_ESTOQUE_ATUAL);
			Estoque estoqueFilter = new Estoque();
			estoqueFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			estoqueFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			estoqueFilter.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_ERP;
			if (LavenderePdaConfig.usaModoControleEstoquePorTipoPedido) {
				estoqueFilter.cdLocalEstoqueDif = EmpresaService.getInstance().getLocalEstoqueEmpresa();
			}
			Vector estoqueList = findAllByExample(estoqueFilter);
			int size = estoqueList.size();
			for (int i = 0; i < size; i++) {
				Estoque estoque = (Estoque) estoqueList.items[i];
				EstoqueRepService.getInstance().insereEstoqueRepByEstoque(estoque);
				estoque.qtEstoque = 0;
				estoque.nuCarimbo = ValueUtil.getIntegerValue(findColumnByRowKey(estoque.getRowKey(), Estoque.COLUMN_NAME_NUCARIMBO)); 
				update(estoque);
			}
		}
	}

	public double getQtEstoqueNivel1(String cdProduto, String cdItemGrade1) throws SQLException {
		return getQtEstoqueNivel2(cdProduto, cdItemGrade1, ProdutoGrade.CD_ITEM_GRADE_PADRAO);
	}
	
	public double getQtEstoqueNivel2(String cdProduto, String cdItemGrade1, String cdItemGrade2) throws SQLException {
		return getQtEstoqueNivel3(cdProduto, cdItemGrade1, cdItemGrade2, ProdutoGrade.CD_ITEM_GRADE_PADRAO);
	}
	
	public double getQtEstoqueNivel3(String cdProduto, String cdItemGrade1, String cdItemGrade2, String cdItemGrade3) throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.cdProduto = cdProduto;
		itemPedido.cdItemGrade1 = cdItemGrade1;
		itemPedido.cdItemGrade2 = cdItemGrade2;
		itemPedido.cdItemGrade3 = cdItemGrade3;
		return getQtEstoqueErpPda(itemPedido, Estoque.CD_LOCAL_ESTOQUE_PADRAO);
	}
	
	protected boolean isListaEstoqueProdutoSemEstoque(Vector list) {
		if (ValueUtil.isNotEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				if (((Estoque)list.items[i]).qtEstoque <= 0) {
					return true;
				}
			}
			return false;
		}
		return true;
	}
	
	public double getSumQtEstoqueGradeNivel1PorLocalPorTabPreco(String cdProduto, String cdLocalEstoque, Vector itemTabPrecoList) throws SQLException {
		return EstoquePdbxDao.getInstance().getSumQtEstoqueGradeNivel1PorLocalPorTabPreco(cdProduto, cdLocalEstoque, itemTabPrecoList);
	}

	protected boolean isListaEstoqueProdutoGradeComEstoque(Vector list) {
		if (ValueUtil.isNotEmpty(list)) {
			for (int i = 0; i < list.size(); i++) {
				if (((Estoque)list.items[i]).qtEstoque > 0) {
					return true;
				}
			}
		}
			return false;
		}
	
	public void deleteByProducaoProd(ProducaoProd producaoProd) throws SQLException {
		Estoque estoqueFilter = new Estoque();
		estoqueFilter.cdEmpresa = producaoProd.cdEmpresa;
		estoqueFilter.cdProduto = producaoProd.cdProduto;
		estoqueFilter.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		estoqueFilter.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		estoqueFilter.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		estoqueFilter.cdLocalEstoque = Estoque.CD_LOCAL_ESTOQUE_PADRAO;
		estoqueFilter.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_ERP;
		deleteAllByExample(estoqueFilter);
	}
	
	private Vector getListProdutoSemFlReserva(Vector itemPedidoList) throws SQLException {
		int size = itemPedidoList.size();
		Vector listItemPedido = new Vector();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if (PedidoService.getInstance().isPossuiPedidosSemFlReserva(itemPedido)) {
				listItemPedido.addElement(itemPedido);
			}
		}
		return listItemPedido;
	}
	
	public double getEstoqueFaltante(Estoque estoque, double qtItemFisico) {
		double estoqueFaltante = 0d;
		if (LavenderePdaConfig.atualizarEstoqueInterno) {
			double qtEstoque = ValueUtil.isEmpty(estoque.cdProduto) ? qtItemFisico * (-1) : estoque.qtEstoque;
			if (qtEstoque < 0) {
				estoqueFaltante = qtEstoque * (-1);
			}
		} else {
			if (estoque.qtEstoque - qtItemFisico < 0) {
				estoqueFaltante += (estoque.qtEstoque - qtItemFisico)  * (-1);
			}
		}
		return estoqueFaltante;
	}
	
	protected Estoque getNewEstoque(String cdLocalEstoque) {
		Estoque estoqueFilter = new Estoque();
		estoqueFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		estoqueFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Estoque.class);
		estoqueFilter.cdLocalEstoque = cdLocalEstoque;
		estoqueFilter.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_ERP;
		return estoqueFilter;
	}
	
	public Vector findEstoqueByCdLocal(String cdLocalEstoque) throws SQLException {
		Estoque estoqueFilter = getNewEstoque(cdLocalEstoque);
		return EstoquePdbxDao.getInstance().findEstoqueByCdLocal(estoqueFilter);
	}
	
	protected void deleteByLocalEstoque(String cdLocalEstoque) throws SQLException {
		Estoque estoqueFilter = getNewEstoque(cdLocalEstoque);
		deleteAllByExample(estoqueFilter);
	}

	public Vector findAllEstoqueRemessaLiberadaErp(ItemPedido itemPedido) throws SQLException {
		return EstoquePdbxDao.getInstance().findAllEstoqueRemessaLiberada(itemPedido);
	}

	public double getQtdEstoqueRemessa(ItemPedido itemPedido, String flOrigemEstoque) throws SQLException {
		return EstoquePdbxDao.getInstance().getQtdEstoqueRemessaProduto(itemPedido, flOrigemEstoque);
	}

	public Estoque getEstoqueByItemPedidoRemessa(ItemPedidoRemessa itemPedidoRemessa, String florigemestoquePda) throws SQLException {
		Estoque estoqueFilter = new Estoque();
		estoqueFilter.cdEmpresa = itemPedidoRemessa.cdEmpresa;      
		estoqueFilter.cdRepresentante = itemPedidoRemessa.cdRepresentante;
		estoqueFilter.cdProduto = itemPedidoRemessa.cdProduto;      
		estoqueFilter.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;   
		estoqueFilter.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;   
		estoqueFilter.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;   
		estoqueFilter.cdLocalEstoque = itemPedidoRemessa.cdLocalEstoque; 
		estoqueFilter.flOrigemEstoque = florigemestoquePda; 
		return (Estoque) findByRowKey(estoqueFilter.getRowKey());
	}
	
	public Estoque findDadosPrevistoParaEstoque(ItemPedido itemPedido) throws SQLException {
		Estoque estoqueFilter = getEstoqueDomainExample(itemPedido.cdProduto, itemPedido.cdItemGrade1, itemPedido.cdItemGrade2, itemPedido.cdItemGrade3, itemPedido.getCdLocalEstoque(), Estoque.FLORIGEMESTOQUE_ERP);
		Estoque estoque = EstoquePdbxDao.getInstance().findDadosPrevistoParaEstoque(estoqueFilter);
		return estoque;
	}
	
	public Estoque findDadosPrevistoParaEstoqueToInterface(ItemPedido itemPedido) throws SQLException {
		Estoque estoque = findDadosPrevistoParaEstoque(itemPedido);
		if (LavenderePdaConfig.usaControleEstoquePrevistoParcial()) {
			setDadosEstoqueParcialPrevisto(itemPedido, estoque);
		}
		return estoque;
	}
	
	public Estoque setDadosEstoqueParcialPrevisto(ItemPedido itemPedido, Estoque estoque) throws SQLException {
		double qtEstoqueParcialPrevisto = estoque.qtEstoque;
		if (LavenderePdaConfig.atualizarEstoqueInterno && itemPedido != null) {
			ItemPedido item = getItemPedidoValidateEstoque(itemPedido, LavenderePdaConfig.usaGradeProduto5());
			item.pedido = itemPedido.pedido;
			qtEstoqueParcialPrevisto -= getQtEstoqueConsumidoPda(item.cdProduto, item.cdItemGrade1, item.cdItemGrade2, item.cdItemGrade3, item.getCdLocalEstoque(), null);
    	}
		qtEstoqueParcialPrevisto = qtEstoqueParcialPrevisto - estoque.qtEstoquePrevisto;
		if (qtEstoqueParcialPrevisto < 0) {
			estoque.qtEstoquePrevisto += qtEstoqueParcialPrevisto;
		}
		return estoque;
	}

	public List<Object[]> buscaSomaEstoqueLocalPorProduto(BaseDomain domainFilter) throws SQLException {
		return EstoquePdbxDao.getInstance().buscaSomaEstoqueLocalPorProduto(domainFilter);
	}

	protected double qtdRemessaSemEstoqueParaDevolver(String cdLocalEstoque) throws SQLException {
		Estoque estoqueFilter = getNewEstoque(cdLocalEstoque);
		return (double) EstoquePdbxDao.getInstance().qtdRemessaSemEstoqueParaDevolver(estoqueFilter);
	}
	
	public double getQtEstoqueDisponivel(String cdProduto) throws SQLException {
		double qtEstoque = getQtEstoqueErp(cdProduto);
    	if (LavenderePdaConfig.atualizarEstoqueInterno) {
    		qtEstoque = qtEstoque - getQtEstoqueConsumidoPda(cdProduto, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, ProdutoGrade.CD_ITEM_GRADE_PADRAO, Estoque.CD_LOCAL_ESTOQUE_PADRAO, null);
    	}
    	return qtEstoque;
    }
	public List<Estoque> getEstoqueListDevolucao(boolean parcial) throws SQLException {
		return EstoquePdbxDao.getInstance().getEstoqueListDevolucao(parcial);
	}
	
	public void deleteEstoqueDevolucaoRemessa() throws SQLException {
		EstoquePdbxDao.getInstance().deleteEstoqueDevolucaoRemessa();
	}
	
	public void updateEstoqueAposDevolucao() throws SQLException {
		EstoquePdbxDao.getInstance().updateEstoqueAposDevolucao();
	}
	
	public Vector findEstoquesParaAjusteRemessa(ItemPedidoEstoqueDto itemPedidoEstoque) throws SQLException {
		return EstoquePdbxDao.getInstance().findEstoquesParaAjusteRemessa(itemPedidoEstoque);
	}
	
	public void deletaEstoqueAposDevolucaoTotal() throws SQLException {
		Estoque filter = new Estoque();
		filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		filter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Estoque.class);
		EstoquePdbxDao.getInstance().deletaEstoqueAposDevolucaoTotal(filter);
	}
	
	public void atualizaEstoquePedido() throws SQLException {
		if (LavenderePdaConfig.atualizarEstoqueInterno) {
			Vector cdProdutoList = ItemPedidoService.getInstance().findCdProdutoComPedidoNaoEnviadoList();
			int size = cdProdutoList.size();
			for (int i = 0; i < size; i++) {
				String cdProduto = (String) cdProdutoList.items[i];
				EstoqueService.getInstance().recalculaEstoqueConsumido(cdProduto);
			}
		}
	}
	
	public Estoque getEstoqueCentroCusto(Produto produto) throws SQLException {
		return EstoquePdbxDao.getInstance().findEstoqueCentroCusto(produto);
	}

	public String getDsOrigemEstoque(String flOrigemEstoque) {
		if (ValueUtil.valueEquals(flOrigemEstoque, Estoque.FLORIGEMESTOQUE_PDA)) {
			return Messages.ORIGEMESTOQUE_PDA;
		} else if (ValueUtil.valueEquals(flOrigemEstoque, Estoque.FLORIGEMESTOQUE_ERP)) {
			return Messages.ORIGEMESTOQUE_ERP;
		} else if (ValueUtil.valueEquals(flOrigemEstoque, Estoque.FLORIGEMESTOQUE_WEB)) {
			return Messages.ORIGEMESTOQUE_WEB;
		} else if (ValueUtil.valueEquals(flOrigemEstoque, Estoque.FLORIGEMESTOQUE_TROCA)) {
			return Messages.ORIGEMESTOQUE_TROCA;
		}
		return flOrigemEstoque;
	}

	public String getEstoqueToString(double qtEstoque) {
		return (LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? StringUtil.getStringValueToInterface((int) qtEstoque) : StringUtil.getStringValueToInterface(qtEstoque));
	}

	public String getEstoquePrevistoToString(double qtEstoquePrevisto) {
		return (LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPREVISTO) ? StringUtil.getStringValueToInterface((int) qtEstoquePrevisto) : StringUtil.getStringValueToInterface(qtEstoquePrevisto));
	}
	
	public Map<String, Estoque> montaHashEstoqueAgrupadorGrade(String dsAgrupadorGrade, String cdItemGrade1, String cdLocalEstoque) throws SQLException {
		Estoque filter = new Estoque();
		filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		filter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		filter.dsAgrupadorGrade = dsAgrupadorGrade;
		filter.cdItemGrade1 = cdItemGrade1;
		filter.cdLocalEstoque = cdLocalEstoque;
		filter.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_ERP;
		Vector listEstoque = EstoquePdbxDao.getInstance().findEstoqueGradeAgrupador(filter);
		int size = listEstoque.size();
		HashMap<String, Estoque> hashEstoqueGrade = new HashMap<>(size);
		for (int i = 0; i < size; i++) {
			Estoque estoque = (Estoque) listEstoque.items[i];
			hashEstoqueGrade.put(estoque.getKeyItemGradeEstoque(), estoque);
		}
		return hashEstoqueGrade;
	}
	
	public double getSumEstoqueAgrupadorGrade(String dsAgrupadorGrade, String cdItemGrade1, String cdLocalEstoque, boolean filtraDisponibilidade) throws SQLException {
		Estoque filter = new Estoque();
		filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		filter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		filter.dsAgrupadorGrade = dsAgrupadorGrade;
		filter.cdItemGrade1 = cdItemGrade1;
		filter.cdLocalEstoque = cdLocalEstoque;
		filter.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_ERP;
		double sumEstoque = EstoquePdbxDao.getInstance().getSumEstoqueAgrupadorGrade(filter, filtraDisponibilidade);
		if (LavenderePdaConfig.usaControleEstoquePrevistoParcial()) {
			double sumEstoquePrevisto = EstoquePdbxDao.getInstance().getSumEstoquePrevistoAgrupadorGrade(filter);
			return Math.max(0d, sumEstoque - sumEstoquePrevisto);
		}
		return sumEstoque;
	}
	
	public double getSumEstoquePrevistoAgrupadorGrade(String dsAgrupadorGrade, String cdItemGrade1, String cdLocalEstoque) throws SQLException {
		Estoque filter = new Estoque();
		filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		filter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		filter.dsAgrupadorGrade = dsAgrupadorGrade;
		filter.cdItemGrade1 = cdItemGrade1;
		filter.cdLocalEstoque = cdLocalEstoque;
		filter.flOrigemEstoque = Estoque.FLORIGEMESTOQUE_ERP;
		double sumEstoquePrevisto = EstoquePdbxDao.getInstance().getSumEstoquePrevistoAgrupadorGrade(filter);
		if (LavenderePdaConfig.usaControleEstoquePrevistoParcial()) {
			double sumEstoque = EstoquePdbxDao.getInstance().getSumEstoqueAgrupadorGrade(filter, false);
			sumEstoque -= sumEstoquePrevisto;
			if (sumEstoque < 0d) {
				sumEstoquePrevisto += sumEstoque;
			}
		}
		return sumEstoquePrevisto;
	}

	public Vector getEstoqueListByJoson(String estoqueJsonList) throws Exception {
		if (ValueUtil.isNotEmpty(estoqueJsonList)) {
			return new Vector(JSONFactory.parse(estoqueJsonList, EstoqueDto[].class));
		}
		return new Vector();
	}
}