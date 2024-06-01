package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ClienteProduto;
import br.com.wmw.lavenderepda.business.domain.CondicaoComercial;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.GiroProduto;
import br.com.wmw.lavenderepda.business.domain.IndiceGrupoProd;
import br.com.wmw.lavenderepda.business.domain.ItemCombo;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoErpDif;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Marcador;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConfig;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoCliente;
import br.com.wmw.lavenderepda.business.domain.ProdutoCondPagto;
import br.com.wmw.lavenderepda.business.domain.ProdutoSimilar;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.business.domain.ProdutoTipoPed;
import br.com.wmw.lavenderepda.business.domain.ProdutoTipoRelacaoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.domain.SugestaoVendaProd;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.validation.ProdutoTipoRelacaoException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoPdbxDao;
import totalcross.ui.image.Image;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class ProdutoService extends CrudPersonLavendereService {

    public static final String DESCRICAO_SEPARATOR = " - ";

    private static ProdutoService instance;

    private Produto lastResultProduct;

    private ProdutoService() {
        //--
    }

    public static ProdutoService getInstance() {
        if (instance == null) {
            instance = new ProdutoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ProdutoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public Produto getProduto(String cdProduto) throws SQLException {
    	return getProduto(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, cdProduto);
    }
    
    public Produto getProduto(String cdEmpresa, String cdRepresentante, String cdProduto) throws SQLException {
    	Produto produtoFilter = new Produto();
    	produtoFilter.cdEmpresa = ValueUtil.isEmpty(cdEmpresa) ? SessionLavenderePda.cdEmpresa : cdEmpresa;
    	produtoFilter.cdRepresentante = ValueUtil.isEmpty(cdRepresentante) ? SessionLavenderePda.usuarioPdaRep.cdRepresentante : cdRepresentante;
    	produtoFilter.cdProduto = cdProduto;
    	if (lastResultProduct != null && ValueUtil.isNotEmpty(lastResultProduct.cdEmpresa) && ValueUtil.isNotEmpty(lastResultProduct.cdRepresentante) && ValueUtil.isNotEmpty(lastResultProduct.cdProduto) &&
			    lastResultProduct.isValidateRowkey() && ValueUtil.valueEquals(lastResultProduct.getRowKey(), produtoFilter.getRowKey())) return lastResultProduct;
    	Produto produto = (Produto) findByRowKey(produtoFilter.getRowKey());
    	if (produto == null) {
    		produto = new Produto();
    	}
    	lastResultProduct = produto;
    	return produto;
    }
    
    public Produto getProduto(String cdEmpresa, String cdRepresentante, String cdProduto, boolean usaMarcadorProduto) throws SQLException {
    	Produto produto = getProduto(cdEmpresa, cdRepresentante, cdProduto);
    	if (usaMarcadorProduto) {
    		Produto produtoMarcador = produto;
    		if (ValueUtil.isEmpty(produto.cdProduto)) {
    			produtoMarcador = new Produto();
			    produtoMarcador.cdEmpresa = cdEmpresa;
			    produtoMarcador.cdRepresentante = cdRepresentante;
    			produtoMarcador.cdProduto = cdProduto;
		    }
    		produto.cdMarcadores = MarcadorProdutoService.getInstance().findMarcadoresByProduto(produtoMarcador, SessionLavenderePda.getCliente() != null ? SessionLavenderePda.getCliente().cdCliente : null);
    	}
    	return produto;
    }
    
    public Produto getProdutoComTributacao(String cdEmpresa, String cdRepresentante, Produto produto, boolean fromSugestaoPedido, String cdTributacaoCliente, String cdTributacaoCliente2, String cdCliente, String cdTipoPedido, String cdUf) throws SQLException {
    	Produto produtoFilter = new Produto();
    	produtoFilter.cdEmpresa = ValueUtil.isEmpty(cdEmpresa) ? SessionLavenderePda.cdEmpresa : cdEmpresa;
    	produtoFilter.cdRepresentante = ValueUtil.isEmpty(cdRepresentante) ? SessionLavenderePda.usuarioPdaRep.cdRepresentante : cdRepresentante;
    	produtoFilter.cdProduto = produto.cdProduto;
    	produtoFilter.estoque = produto.estoque;
    	if (fromSugestaoPedido) {
    		produtoFilter.fromSugestaoPedido = fromSugestaoPedido;
    		produtoFilter.fromListItemPedido = fromSugestaoPedido;
    		produtoFilter.cdTributacaoClienteFilter = cdTributacaoCliente;
    		produtoFilter.cdTributacaoClienteFilter2 = cdTributacaoCliente2;
    		produtoFilter.cdClienteFilter = cdCliente;
    		produtoFilter.cdTipoPedidoFilter = cdTipoPedido;
    		produtoFilter.cdUFFilter = cdUf;
    	}
    	produtoFilter = ProdutoPdbxDao.getInstance().findProdutoComTributacao(produtoFilter);
    	if (produtoFilter == null) {
    		produtoFilter = new Produto();
    	}
    	return produtoFilter;
    }

    public Produto getProdutoDyn(String cdEmpresa, String cdRepresentante, String cdProduto) throws SQLException {
    	Produto produtoFilter = new Produto();
		produtoFilter.cdEmpresa = ValueUtil.isEmpty(cdEmpresa) ? SessionLavenderePda.cdEmpresa : cdEmpresa;
		produtoFilter.cdRepresentante = ValueUtil.isEmpty(cdRepresentante) ? SessionLavenderePda.usuarioPdaRep.cdRepresentante : cdRepresentante;
    	produtoFilter.cdProduto = cdProduto;
    	Produto produto = (Produto) findByRowKeyDyn(produtoFilter.getRowKey());
    	if (produto == null) {
    		produto = new Produto();
    	}
    	return produto;
    }

    public Vector findProdutosByPrincipioAtivo(String dsFiltro, String cdProdutoAtual) throws SQLException {
		StringBuffer strBuffer = new StringBuffer();
		strBuffer.append("%");
		strBuffer.append(dsFiltro);
		strBuffer.append("%");
		//--
		Produto p = new Produto();
		p.cdEmpresa = SessionLavenderePda.cdEmpresa;
		p.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	p.dsPrincipioAtivo = strBuffer.toString();
    	Vector list = findAllByExampleSummary(p);
    	p.cdProduto = cdProdutoAtual;
    	list.removeElement(p);
    	return list;
    }

    public Vector findProdutos(String dsFiltro, String cdTabelaPreco, String cdFornecedor, boolean filterByPrincipioAtivo, BaseDomain domain, boolean onlyStartString, boolean filterByCodigoProduto) throws SQLException {
    	return findProdutos(dsFiltro, cdTabelaPreco, cdFornecedor, filterByPrincipioAtivo, false, domain, onlyStartString, filterByCodigoProduto);
    }

    public Vector findProdutos(String dsFiltro, String cdTabelaPreco, String cdFornecedor, boolean filterByPrincipioAtivo, boolean filterByAplicacaoProduto, BaseDomain domain, boolean onlyStartString, boolean filterByCodigoProduto) throws SQLException {
    	return findProdutos(dsFiltro, cdTabelaPreco, cdFornecedor, filterByPrincipioAtivo, filterByAplicacaoProduto, domain, null, onlyStartString, filterByCodigoProduto);
    }
    
    public Vector findProdutos(String dsFiltro, String cdTabelaPreco, String cdFornecedor, boolean filterByPrincipioAtivo,boolean filterByAplicacaoProduto, BaseDomain domain, ItemPedido itemPedido, boolean onlyStartString, boolean filterByCodigoProduto) throws SQLException {
    	boolean onlyStart = onlyStartString;
    	if (LavenderePdaConfig.usaPesquisaInicioString) {
    		if (dsFiltro.startsWith("*")) {
        		dsFiltro = dsFiltro.substring(1);
    		} else {
        		onlyStart = true;
    		}
    	}

    	ProdutoBase produtoFilter = (ProdutoBase) domain;
    	if (LavenderePdaConfig.usaCampanhaDeVendasPorCestaDeProdutos() && ValueUtil.isNotEmpty(produtoFilter.cdCesta) && !LavenderePdaConfig.permiteFiltroCestaEmConjuntoOutrosFiltros()) { 
			return CestaProdutoService.getInstance().getProdutosInCesta(produtoFilter, dsFiltro, filterByPrincipioAtivo, cdFornecedor, onlyStartString); 
		}
    	return getProdutosByFilters(dsFiltro, cdTabelaPreco, cdFornecedor, filterByPrincipioAtivo, filterByAplicacaoProduto, produtoFilter, itemPedido, onlyStart, filterByCodigoProduto);
    }
    
    private Vector getProdutosByFilters(String dsFiltro, String cdTabelaPreco, String cdFornecedor,	boolean filterByPrincipioAtivo, boolean filterByAplicacaoProduto, ProdutoBase domain, ItemPedido itemPedido, boolean onlyStartString , boolean filterByCodigoProduto) throws SQLException {
		Vector list;
		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() || LavenderePdaConfig.usaListaColunaPorTabelaPreco || (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoTipo3() && ValueUtil.isNotEmpty(cdTabelaPreco))) {
			if (domain.itemTabelaPreco == null) {
				domain.itemTabelaPreco = new ItemTabelaPreco();
			}
			domain.itemTabelaPreco.cdTabelaPreco = cdTabelaPreco;
		}
		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() && domain instanceof ProdutoTabPreco) {
			list = getListaProdutosTabelaPreco(dsFiltro, cdFornecedor, filterByPrincipioAtivo, filterByAplicacaoProduto, domain, itemPedido, onlyStartString, filterByCodigoProduto);
		} else {
			list = getListaProdutos(dsFiltro, cdFornecedor, filterByPrincipioAtivo, filterByAplicacaoProduto, domain, itemPedido, onlyStartString, filterByCodigoProduto);
		}
		return list;
	}

	private Vector getListaProdutos(String dsFiltro, String cdFornecedor, boolean filterByPrincipioAtivo, boolean filterByAplicacaoProduto, BaseDomain domain, ItemPedido itemPedido, boolean onlyStartString, boolean filterByCodigoProduto) throws SQLException {
		StringBuffer strBuffer = strBufferLikeDesc(dsFiltro, onlyStartString);
		Vector list;
		Produto produto = (Produto) domain;
		if (produto.itemTabelaPreco != null) {
			if (LavenderePdaConfig.usaPrecoPorUf) {
				produto.itemTabelaPreco.cdUf = produto.cdUFFilter;
			} else {
				produto.itemTabelaPreco.cdUf = ValueUtil.VALOR_ZERO;
			}
		}
		if (ValueUtil.isNotEmpty(cdFornecedor)) {
			produto.cdFornecedor = cdFornecedor;
		}
		produto.filtraFornecedorRep = LavenderePdaConfig.usaFiltroFornecedor && FornecedorRepService.getInstance().possuiFornRep();
		if (!ValueUtil.isEmpty(dsFiltro)) {
			if (filterByPrincipioAtivo) {
				produto.dsPrincipioAtivo = strBuffer.toString();
			} else if (filterByAplicacaoProduto) {
				produto.dsAplicacaoProduto = strBuffer.toString();
			} else if (filterByCodigoProduto) {
				if (LavenderePdaConfig.usaFiltraProdutoCodigoExato() && ValueUtil.isValidNumber(dsFiltro)) {
					produto.cdProduto = dsFiltro;
			} else {
					produto.cdProdutoLikeFilter = strBuffer.toString();
				}
			} else {
				produto.dsProduto = LavenderePdaConfig.usaFiltraProdutoCodigoExato() && ValueUtil.isValidNumber(dsFiltro) ? "" : strBuffer.toString();
				if (!LavenderePdaConfig.usaFiltroSomenteDescricaoProduto) {
					produto.nuCodigoBarras = dsFiltro;
					if (LavenderePdaConfig.usaFiltraProdutoCodigoExato() && ValueUtil.isValidNumber(dsFiltro)) {
						produto.cdProduto = dsFiltro;
					} else {
						produto.cdProdutoLikeFilter = strBuffer.toString();
					}
					if (LavenderePdaConfig.usaFiltroMarcaProduto) {
						produto.dsMarca = strBuffer.toString();
					}
					if (LavenderePdaConfig.usaFiltroCodigoAlternativoProduto) {
			    		produto.dsCodigoAlternativo = strBuffer.toString();
			    	}
			if (LavenderePdaConfig.isMostraDescricaoReferencia()) {
						if (LavenderePdaConfig.usaFiltraProdutoCodigoExato() && ValueUtil.isValidNumber(dsFiltro)) {
					produto.dsReferencia = dsFiltro;
				} else {
					produto.dsReferenciaLikeFilter = strBuffer.toString();
				}
			}
				}
			}
			if (LavenderePdaConfig.isUsaFiltroAplicacaoDoProduto() && !LavenderePdaConfig.isUsaFiltroAplicacaoDoProdutoSeparado()) {
	    		produto.dsAplicacaoProduto = strBuffer.toString();
	    	}
			if (LavenderePdaConfig.usaGradeProduto5() && produto.filtrandoAgrupadorGrade) {
				produto.setDsAgrupadorGrade(strBuffer.toString());
			}
			
		}
		if (itemPedido != null) {
			produto.cdFatorComum = itemPedido.getProduto().cdFatorComum;
			produto.forceCdFatorComumFilter = true;
		}
		return findAllByExampleSummary(produto);
	}

	public StringBuffer strBufferLikeDesc(String dsFiltro, boolean onlyStartString) {
		StringBuffer strBuffer = new StringBuffer();
		if (!onlyStartString) {
			strBuffer.append("%");
		}
		strBuffer.append(dsFiltro);
		strBuffer.append("%");
		return strBuffer;
	}

	private Vector getListaProdutosTabelaPreco(String dsFiltro, String cdFornecedor, boolean filterByPrincipioAtivo, boolean filterByAplicacaoProduto, BaseDomain domain, ItemPedido itemPedido, boolean onlyStartString, boolean filterByCodigoProduto) throws SQLException {
		StringBuffer strBuffer = strBufferLikeDesc(dsFiltro, onlyStartString);
		Vector list;
		ProdutoTabPreco produtoTabPrecoFilter = (ProdutoTabPreco) domain;
		if (LavenderePdaConfig.isFiltraItemTabelaPrecoListaProduto() || LavenderePdaConfig.usaFiltroComissao) {
			if (LavenderePdaConfig.usaPrecoPorUf) {
				produtoTabPrecoFilter.itemTabelaPreco.cdUf = produtoTabPrecoFilter.cdUFFilter;
			} else {
				produtoTabPrecoFilter.itemTabelaPreco.cdUf = ValueUtil.VALOR_ZERO;
			}
		}
		if (ValueUtil.isNotEmpty(cdFornecedor)) {
			produtoTabPrecoFilter.cdFornecedor = cdFornecedor;
		}
		produtoTabPrecoFilter.filtraFornecedorRep = LavenderePdaConfig.usaFiltroFornecedor && FornecedorRepService.getInstance().possuiFornRep();
		String cdTabelaPreco = produtoTabPrecoFilter.itemTabelaPreco.cdTabelaPreco;
		if (ValueUtil.isNotEmpty(cdTabelaPreco)) {
			if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoSelecionada()) {
				produtoTabPrecoFilter.dsTabPrecoList = getLikeByCdTabelaPreco(cdTabelaPreco);
				produtoTabPrecoFilter.itemTabelaPreco.cdTabelaPreco = cdTabelaPreco;
			}
			//--
			if (ValueUtil.VALOR_SIM.equals(produtoTabPrecoFilter.flFiltraProdutoPromocional)) {
				produtoTabPrecoFilter.dsTabPrecoPromoList = getLikeByCdTabelaPreco(cdTabelaPreco);
			}
			if (ValueUtil.VALOR_SIM.equals(produtoTabPrecoFilter.flFiltraProdutoDescPromocional) && !LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional()) {
				produtoTabPrecoFilter.dsTabPrecoDescPromocionalList = getLikeByCdTabelaPreco(cdTabelaPreco);
			}
			if (ValueUtil.VALOR_SIM.equals(produtoTabPrecoFilter.flFiltraProdutoOportunidade)) {
				produtoTabPrecoFilter.dsTabPrecoOportunidadeList = getLikeByCdTabelaPreco(cdTabelaPreco);
			}
		} else {
			if (ValueUtil.VALOR_SIM.equals(produtoTabPrecoFilter.flFiltraProdutoPromocional)) {
				produtoTabPrecoFilter.dsTabPrecoPromoList = null;
			}
			if (ValueUtil.VALOR_SIM.equals(produtoTabPrecoFilter.flFiltraProdutoDescPromocional)) {
				produtoTabPrecoFilter.dsTabPrecoDescPromocionalList = null;
			}
		}
		if (!ValueUtil.isEmpty(dsFiltro)) {
			if (filterByPrincipioAtivo) {
				produtoTabPrecoFilter.dsPrincipioAtivo = strBuffer.toString();
			} else if (filterByAplicacaoProduto) {
				produtoTabPrecoFilter.dsAplicacaoProduto = strBuffer.toString();
			} else if (filterByCodigoProduto) {
				if (LavenderePdaConfig.usaFiltraProdutoCodigoExato() && ValueUtil.isValidNumber(dsFiltro)) {
					produtoTabPrecoFilter.cdProduto = dsFiltro;
			} else {
					produtoTabPrecoFilter.cdProdutoLikeFilter = strBuffer.toString();
				}
			} else {
				produtoTabPrecoFilter.dsProduto = LavenderePdaConfig.usaFiltraProdutoCodigoExato() && ValueUtil.isValidNumber(dsFiltro) ? "" : strBuffer.toString();
				if (!LavenderePdaConfig.usaFiltroSomenteDescricaoProduto) {
					produtoTabPrecoFilter.nuCodigoBarras = dsFiltro;
					if (LavenderePdaConfig.usaFiltraProdutoCodigoExato() && ValueUtil.isValidNumber(dsFiltro)) {
						produtoTabPrecoFilter.cdProduto = dsFiltro;
					} else {
						produtoTabPrecoFilter.cdProdutoLikeFilter = strBuffer.toString();
					}
					if (LavenderePdaConfig.usaFiltroMarcaProduto) {
						produtoTabPrecoFilter.dsMarca = strBuffer.toString();
					}
				}
				if(LavenderePdaConfig.usaFiltroCodigoAlternativoProduto) {
					produtoTabPrecoFilter.dsCodigoAlternativo = strBuffer.toString();
				}
			if (LavenderePdaConfig.isMostraDescricaoReferencia()) {
					if (LavenderePdaConfig.usaFiltraProdutoCodigoExato() && ValueUtil.isValidNumber(dsFiltro)) {
					produtoTabPrecoFilter.dsReferencia = dsFiltro;
				} else {
					produtoTabPrecoFilter.dsReferenciaLikeFilter = strBuffer.toString();
				}
			}
			}
			if(LavenderePdaConfig.isUsaFiltroAplicacaoDoProduto() && !LavenderePdaConfig.isUsaFiltroAplicacaoDoProdutoSeparado()) {
				produtoTabPrecoFilter.dsAplicacaoProduto = strBuffer.toString();
			}
		}
		if (itemPedido != null) {
			produtoTabPrecoFilter.cdFatorComum = itemPedido.getProduto().cdFatorComum;
			produtoTabPrecoFilter.forceCdFatorComumFilter = true;
		}
		list = ProdutoTabPrecoService.getInstance().findAllByExampleSummary(produtoTabPrecoFilter);
		return list;
	}
    
	private String getLikeByCdTabelaPreco(String cdTabelaPreco) {
		StringBuffer strBufferTabPreco = new StringBuffer();
		strBufferTabPreco.append("%");
		strBufferTabPreco.append(ProdutoTabPreco.SEPARADOR_CAMPOS);
		strBufferTabPreco.append(cdTabelaPreco);
		strBufferTabPreco.append(ProdutoTabPreco.SEPARADOR_CAMPOS);
		strBufferTabPreco.append("%");
		return strBufferTabPreco.toString();
	}
/*
    public ResultSet findProdutosToGrid(String dsFiltro, String cdTabelaPreco, String cdFornecedor, boolean filterByPrincipioAtivo, boolean fromItemPedido, boolean filterByPreAltaCusto) throws SQLException {
    	boolean onlyStartString = false;
    	dsFiltro = dsFiltro == null ? "" : dsFiltro;
    	if (LavenderePdaConfig.usaPesquisaInicioString) {
    		if (dsFiltro.startsWith("*")) {
    			dsFiltro = dsFiltro.substring(1);
    		} else {
    			onlyStartString = true;
    		}
    	}
    	StringBuffer strBuffer = new StringBuffer();
    	if (!onlyStartString) {
    		strBuffer.append("%");
    	}
    	strBuffer.append(dsFiltro);
    	strBuffer.append("%");
    	//--
    	if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
    		ProdutoTabPreco produtoTabPrecoFilter = new ProdutoTabPreco();
    		produtoTabPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		produtoTabPrecoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    		if (ValueUtil.isNotEmpty(cdFornecedor)) {
    			produtoTabPrecoFilter.cdFornecedor = cdFornecedor;
    		}
    		if (filterByPreAltaCusto) {
        		produtoTabPrecoFilter.flPreAltaCusto = StringUtil.getStringValue(filterByPreAltaCusto);
        	}
    		if (ValueUtil.isNotEmpty(cdTabelaPreco)) {
    			produtoTabPrecoFilter.dsTabPrecoList = getLikeByCdTabelaPreco(cdTabelaPreco);
    		}
    		if (!ValueUtil.isEmpty(dsFiltro)) {
    			if (filterByPrincipioAtivo) {
    				produtoTabPrecoFilter.dsPrincipioAtivo = strBuffer.toString();
    			} else {
    				produtoTabPrecoFilter.dsProduto = strBuffer.toString();
    				if (!LavenderePdaConfig.usaFiltroSomenteDescricaoProduto) {
    					if (LavenderePdaConfig.usaFiltraProdutoCodigoExato()) {
    						produtoTabPrecoFilter.cdProduto = dsFiltro;
    					} else {
    						produtoTabPrecoFilter.cdProdutoLikeFilter = strBuffer.toString();
    					}
    					produtoTabPrecoFilter.nuCodigoBarras = dsFiltro;
    				}
    			}
    		}
    		produtoTabPrecoFilter.filterToItemPedido = fromItemPedido;
    		return ProdutoTabPrecoService.getInstance().findAllByExampleToGrid(produtoTabPrecoFilter);
    	} else {
    		Produto produto = new Produto();
    		produto.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		produto.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    		if (ValueUtil.isNotEmpty(cdFornecedor)) {
    			produto.cdFornecedor = cdFornecedor;
    		}
    		if (filterByPreAltaCusto) {
    			produto.flPreAltaCusto = StringUtil.getStringValue(filterByPreAltaCusto);
        	}
    		if (!ValueUtil.isEmpty(dsFiltro)) {
    			if (filterByPrincipioAtivo) {
    				produto.dsPrincipioAtivo = strBuffer.toString();
    			} else {
    				produto.dsProduto = strBuffer.toString();
    				if (!LavenderePdaConfig.usaFiltroSomenteDescricaoProduto) {
    					if (LavenderePdaConfig.usaFiltraProdutoCodigoExato()) {
    						produto.cdProduto = dsFiltro;
    					} else {
    						produto.cdProdutoLikeFilter = strBuffer.toString();
    					}
    				}
    			}
    		}
    		produto.filterToItemPedido = fromItemPedido;
    		return findAllByExampleToGrid(produto);
    	}
    }
    */
    
    private Produto getProdutoFilter(String cdEmpresa, String cdRepresentante, String cdProduto) {
    	Produto produtoFilter = new Produto();
    	produtoFilter.cdEmpresa = ValueUtil.isEmpty(cdEmpresa) ? SessionLavenderePda.cdEmpresa : cdEmpresa;
		produtoFilter.cdRepresentante = ValueUtil.isEmpty(cdRepresentante) ? SessionLavenderePda.usuarioPdaRep.cdRepresentante : cdRepresentante;
		produtoFilter.cdProduto = cdProduto;
		return produtoFilter;
    }
    
    public String getDsProduto(ProdutoBase produto) throws SQLException {
    	return getDsProduto(produto, null);
    }
    
    public String getDsProduto(ProdutoBase produto, ItemPedido itemPedido) throws SQLException {
    	if (produto == null && itemPedido == null) return ValueUtil.VALOR_NI;

    	if ((produto != null) && produto.dsProduto != null) {
    		return produto.getDescription();
    	} else if ((itemPedido != null) && itemPedido.dsProduto != null) {
    		return itemPedido.dsProduto;
    	}
	    String cdProduto = produto != null ? produto.cdProduto : itemPedido.cdProduto;
    	if (ValueUtil.isEmpty(cdProduto)) return ValueUtil.VALOR_NI;

	    return getDsProduto(cdProduto);
    }

    public String getDsProduto(String cdProduto) throws SQLException {
    	return getDsProduto(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, cdProduto);
    }
    
    public String getDsProduto(String cdEmpresa, String cdRepresentante, String cdProduto) throws SQLException {
    	return getVlColumn(cdEmpresa, cdRepresentante, cdProduto, "DSPRODUTO");
    }
    
    public String getDsPrincipioAtivo(String cdProduto) throws SQLException {
    	return getVlColumn(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, cdProduto, "DSPRINCIPIOATIVO");
    }
    
    public String getDsReferencia(String cdProduto) throws SQLException {
    	return getVlColumn(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, cdProduto, "DSREFERENCIA");
    }
    
    private String getVlColumn(String cdEmpresa, String cdRepresentante, String cdProduto, String dsColumn) throws SQLException {
    	if (ValueUtil.isNotEmpty(cdProduto)) {
	    	Produto produtoFilter = getProdutoFilter(cdEmpresa, cdRepresentante, cdProduto);
	    	String vlColumn = ProdutoPdbxDao.getInstance().findColumnByRowKey(produtoFilter.getRowKey(), dsColumn);
	    	if (ValueUtil.isNotEmpty(vlColumn)) {
	        	return vlColumn;
			}
    	}
    	return StringUtil.getStringValue(cdProduto);
    }
    
    public String getDescriptionWithId(ProdutoBase produto, String cdProduto) throws SQLException {
    	return getDescriptionWithId(produto, cdProduto, null);
    }
    
    public String getDescriptionWithId(ProdutoBase produto, String cdProduto, ItemPedido itemPedido) throws SQLException {
    	if (itemPedido != null) {
    		return getDescricaoProdutoReferenciaConsideraCodigo(produto, itemPedido);
    	}
    	if (produto != null) {
    		if (ValueUtil.isEmpty(produto.cdProduto)) {
    			produto.cdProduto = cdProduto;
    		}
    		return getDescricaoProdutoReferenciaConsideraCodigo(produto);
    	}
    	return ProdutoService.getInstance().getDescriptionWithId(cdProduto);
    }

    public String getDescriptionWithId(String cdProduto) throws SQLException {
    	if (!ValueUtil.isEmpty(cdProduto)) {
    		Produto produtoFilter = new Produto();
    		produtoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		produtoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    		produtoFilter.cdProduto = cdProduto;
    		produtoFilter.dsProduto = ProdutoPdbxDao.getInstance().findColumnByRowKey(produtoFilter.getRowKey(), "DSPRODUTO");
    		if (LavenderePdaConfig.isMostraDescricaoReferencia()) {
				produtoFilter.dsReferencia = ProdutoPdbxDao.getInstance().findColumnByRowKey(produtoFilter.getRowKey(), "DSREFERENCIA");
    		}
    		if (produtoFilter.dsProduto == null) {
    			if (ValueUtil.isNotEmpty(produtoFilter.dsReferencia)) {
    				if (LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
    					return "[" + produtoFilter.dsReferencia + "] " + cdProduto;
    				} else if (LavenderePdaConfig.isMostraDescricaoReferenciaAposDsProduto()) {
    					return cdProduto + " [" + produtoFilter.dsReferencia + "]";
    				}
    			}
    			return cdProduto;
    		}
    		if (ValueUtil.isNotEmpty(produtoFilter.dsReferencia)) {
    			return getDescricaoProdutoReferenciaConsideraCodigo(produtoFilter);
    		}
    		return produtoFilter.toString();
    	}
    	return StringUtil.getStringValue(cdProduto);
    }

    public Vector findProdutosByGrupoProduto(String cdGrupoProduto1, String cdGrupoProduto2, String cdGrupoProduto3, String cdGrupoProduto4, Vector listProdutos) throws SQLException {
    	Vector result = new Vector();
    	int size = listProdutos.size();
		Vector listCdProduto = ProdutoPdbxDao.getInstance().findAllCdProdutoWhereGrupoProduto(cdGrupoProduto1,  cdGrupoProduto2, cdGrupoProduto3, cdGrupoProduto4);
		ProdutoBase produto;
    	for (int i = 0; i < size; i++) {
			produto = (ProdutoBase)listProdutos.items[i];
			if (listCdProduto.indexOf(produto.cdProduto) != -1) {
				result.addElement(produto);
			}
    	}
    	listCdProduto = null;
    	return result;
    }
    
    public Vector findProdutosByGrupoDescProd(String cdGrupoDescProd, Vector listProdutos) throws SQLException {
    	Vector result = new Vector();
		Vector listCdProduto = ProdutoPdbxDao.getInstance().findAllCdProdutoWhereGrupoDescProd(cdGrupoDescProd);
		ProdutoBase produto;
    	for (int i = 0; i < listProdutos.size(); i++) {
			produto = (ProdutoBase) listProdutos.items[i];
			if (listCdProduto.indexOf(produto.cdProduto) != -1) {
				result.addElement(produto);
			}
    	}
    	listCdProduto = null;
    	return result;
    }

    public Vector findProdutosPorTabelaPrecoDisponivel(Hashtable tabelasPrecoDisponiveis, Vector produtoList) {
    	Vector produtoFinalList = new Vector();
    	if (ValueUtil.isNotEmpty(produtoList) && tabelasPrecoDisponiveis != null) {
    		ProdutoTabPreco produtoTabPreco;
    		for (int i = 0; i < produtoList.size(); i++) {
    			produtoTabPreco = (ProdutoTabPreco) produtoList.items[i];
    			String[] dsTabPrecoList = StringUtil.split(StringUtil.getStringValue(produtoTabPreco.dsTabPrecoList), ProdutoTabPreco.SEPARADOR_CAMPOS);
    			for (int j = 0; j < dsTabPrecoList.length; j++) {
    				String cdTabelaPrecoProduto = dsTabPrecoList[j];
    				if (tabelasPrecoDisponiveis.get(cdTabelaPrecoProduto) != null) {
    					produtoFinalList.addElement(produtoTabPreco);
    					break;
    				}
    			}
			}
    	}
    	return produtoFinalList;
    }
    
	public Vector findProdutosPorTipoPedido(TipoPedido tipoPedido, Vector listProdutos) throws SQLException {
		Vector listProdutoTipoPed = getProdutoPendenteListByTipoPedido(tipoPedido);
		if (ValueUtil.isEmpty(listProdutoTipoPed)) {
			return listProdutos;
		}
		HashMap<String, String> listProdutoTipoPedHash = montaHashProdutosPendentes(listProdutoTipoPed);
		listProdutoTipoPed = null;
		int size = listProdutos.size();
		Vector result = new Vector(size);
		for (int i = 0; i < size; i++) {
			BasePersonDomain basePersonDomain = (BasePersonDomain) listProdutos.items[i];
			if (listProdutoTipoPedHash.containsKey(getCdProduto(basePersonDomain)) ^ tipoPedido.isFlExcecaoProduto()) {
				result.addElement(basePersonDomain);
			}
		}
		return result;
	}

	private String getCdProduto(BasePersonDomain basePersonDomain) {
		if (basePersonDomain instanceof ProdutoBase) {
			return ((ProdutoBase) basePersonDomain).cdProduto;
		} else if (basePersonDomain instanceof ItemTabelaPreco) {
			return ((ItemTabelaPreco) basePersonDomain).cdProduto;
		}
		return "";
	}
	
	public HashMap<String, String> montaHashProdutosPendentes(Vector listProdutoTipoPed) {
		HashMap<String, String> listProdutoTipoPedHash = new HashMap<String, String>(127);
		if (ValueUtil.isEmpty(listProdutoTipoPed)) return listProdutoTipoPedHash;
		int size = listProdutoTipoPed.size();
		for (int i = 0; i < size; i++) {
			ProdutoTipoPed produtoTipoPed = (ProdutoTipoPed) listProdutoTipoPed.items[i];
			listProdutoTipoPedHash.put(produtoTipoPed.cdProduto, produtoTipoPed.cdProduto);
		}
		return listProdutoTipoPedHash;
	}

	public Vector getProdutoPendenteListByTipoPedido(TipoPedido tipoPedido) throws SQLException {
		ProdutoTipoPed produtoTipoPedFilter = new ProdutoTipoPed();
		produtoTipoPedFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoTipoPedFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		produtoTipoPedFilter.cdTipoPedido = tipoPedido.cdTipoPedido;
		return ProdutoTipoPedService.getInstance().findAllByExample(produtoTipoPedFilter);
	}

	/**
	 * TODO: Unificar em uma só consulta SQL a lista de Produtos de Sugestão para descontinuar esse método. 
	 * Assim como o método findClienteProdutos, esse método está para ser descontinuado onde deve ser substituído o filtro pelo SQL do Produto/ProdutoTabPreco.
	 * Foi mantido até então devido a funcionalidade de Sugestão onde não é uma query ainda.
	 * @see     #findClienteProdutos(String, Vector)
	 */
	public Vector findProdutosCliente(String cdCliente, Vector listProdutos) throws SQLException {
		ProdutoCliente produtoClienteFilter = new ProdutoCliente();
	    produtoClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
	    produtoClienteFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
	    produtoClienteFilter.cdCliente = cdCliente;
	    produtoClienteFilter.flTipoRelacao = ProdutoCliente.RELACAOEXCLUSIVA;
	    produtoClienteFilter.cdProdutoFilterList = getCdProdutoList(listProdutos);
	    Vector cdProdutoClienteList = ProdutoClienteService.getInstance().findProdutoClienteListProdutoFilter(produtoClienteFilter);
	    int size = cdProdutoClienteList.size();
	    if (size > 0 || ProdutoClienteService.getInstance().isPossuiRegistroExclusivo()) {
	    	for (int i = 0; i < size; i++) {
	    		ProdutoBase produtoExample = getProdutoCliente(cdProdutoClienteList, i);
	    		int index = listProdutos.indexOf(produtoExample);
	    		if (index >= 0) {
    				listProdutos.removeElement(produtoExample);
	    		}
	    	}
    		return listProdutos;
	    }
	    produtoClienteFilter.flTipoRelacao = ProdutoCliente.RELACAOEXCECAO;
    	cdProdutoClienteList = ProdutoClienteService.getInstance().findProdutoClienteListProdutoFilter(produtoClienteFilter);
	    size = cdProdutoClienteList.size();
	    if (size > 0) {
	    	for (int i = 0; i < size; i++) {
	    		ProdutoBase produtoExample = getProdutoCliente(cdProdutoClienteList, i);
	    		int index = listProdutos.indexOf(produtoExample);
	    		if (index >= 0) {
	    		listProdutos.removeElement(produtoExample);
	    	}
	    	}
    		return listProdutos;
	    } else {
	    	Vector produtoFinalList = new Vector();
	    	produtoClienteFilter.flTipoRelacao = ProdutoCliente.RELACAORESTRICAO;
	    	cdProdutoClienteList = ProdutoClienteService.getInstance().findProdutoClienteListProdutoFilter(produtoClienteFilter);
	    	size = cdProdutoClienteList.size();
	    	if (size > 0) {
	    		for (int i = 0; i < size; i++) {
	    			ProdutoBase produtoExample = getProdutoCliente(cdProdutoClienteList, i);
	    			int indexProduto = listProdutos.indexOf(produtoExample);
	    			if (indexProduto != -1) {
	    				produtoFinalList.addElement(listProdutos.items[indexProduto]);
	    			}
	    		}
	    		return produtoFinalList;
	    	}
	    }
	    return listProdutos;
	}
	
	/**
	 * TODO: Unificar em uma só consulta SQL a lista de Produtos de Sugestão para descontinuar esse método. 
	 * Assim como o método findClienteProdutos, esse método está para ser descontinuado onde deve ser substituído o filtro pelo SQL do Produto/ProdutoTabPreco.
	 * Foi mantido até então devido a funcionalidade de Sugestão onde não é uma query ainda.
	 * @see     #findClienteProdutos(String, Vector)
	 */
	public Vector findProdutosCondPagto(String cdCondicaoPagto, Vector listProdutos) throws SQLException {
		ProdutoCondPagto produtoCondPagtoFilter = new ProdutoCondPagto();
		produtoCondPagtoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoCondPagtoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		produtoCondPagtoFilter.cdCondicaoPagamento = cdCondicaoPagto;
		produtoCondPagtoFilter.flTipoRelacao = ProdutoCondPagto.RELACAOEXCLUSIVA;
		produtoCondPagtoFilter.cdProdutoFilterList = getCdProdutoList(listProdutos);
		Vector cdProdutoClienteList = ProdutoCondPagtoService.getInstance().findProdutoCondPagtoListProdutoFilter(produtoCondPagtoFilter);
		int size = cdProdutoClienteList.size();
		if (size > 0 || ProdutoCondPagtoService.getInstance().isPossuiRegistroExclusivo()) {
			for (int i = 0; i < size; i++) {
				ProdutoBase produtoExample = getProdutoCliente(cdProdutoClienteList, i);
				int index = listProdutos.indexOf(produtoExample);
				if (index >= 0) {
					listProdutos.removeElement(produtoExample);
				}
			}
			return listProdutos;
		}
		produtoCondPagtoFilter.flTipoRelacao = ProdutoCondPagto.RELACAOEXCECAO;
		cdProdutoClienteList = ProdutoCondPagtoService.getInstance().findProdutoCondPagtoListProdutoFilter(produtoCondPagtoFilter);
		size = cdProdutoClienteList.size();
		if (size > 0) {
			for (int i = 0; i < size; i++) {
				ProdutoBase produtoExample = getProdutoCliente(cdProdutoClienteList, i);
				int index = listProdutos.indexOf(produtoExample);
				if (index >= 0) {
					listProdutos.removeElement(produtoExample);
				}
			}
			return listProdutos;
		} else {
			Vector produtoFinalList = new Vector();
			produtoCondPagtoFilter.flTipoRelacao = ProdutoCondPagto.RELACAORESTRICAO;
			cdProdutoClienteList = ProdutoCondPagtoService.getInstance().findProdutoCondPagtoListProdutoFilter(produtoCondPagtoFilter);
			size = cdProdutoClienteList.size();
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					ProdutoBase produtoExample = getProdutoCliente(cdProdutoClienteList, i);
					int indexProduto = listProdutos.indexOf(produtoExample);
					if (indexProduto != -1) {
						produtoFinalList.addElement(listProdutos.items[indexProduto]);
					}
				}
				return produtoFinalList;
			}
		}
		return listProdutos;
	}

	private Vector getCdProdutoList(Vector listProdutos) {
		int size = listProdutos.size();
		Vector cdProdutoList = new Vector();
		for (int i = 0; i < size; i++) {
			ProdutoBase produto = (ProdutoBase) listProdutos.items[i];
			cdProdutoList.addElement(produto.cdProduto);
		}
		return cdProdutoList;
	}

	private ProdutoBase getProdutoCliente(Vector cdProdutoClienteList, int i) {
		ProdutoBase produtoExample = new ProdutoBase("");
		produtoExample.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoExample.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		produtoExample.cdProduto = String.valueOf(cdProdutoClienteList.items[i]);
		return produtoExample;
	}
	
	/**
	 * TODO: Unificar em uma só consulta SQL a lista de Produtos de Sugestão para descontinuar esse método. 
	 * Assim como o método findProdutosCliente, esse método está para ser descontinuado onde deve ser substituído o filtro pelo SQL do Produto/ProdutoTabPreco.
	 * Foi mantido até então devido a funcionalidade de Sugestão onde não é uma query ainda.
	 * @see     #findProdutosCliente(String, Vector)
	 */
	public Vector findClienteProdutos(String cdCliente, Vector listProdutos) throws SQLException {
	    Vector clienteProdutoList = ClienteProdutoService.getInstance().findClienteProdutoList(cdCliente, ClienteProduto.RELACAOEXCLUSIVA);
	    int size = clienteProdutoList.size();
	    if (size > 0 || ClienteProdutoService.getInstance().isPossuiRegistroExclusivo()) {
	    	for (int i = 0; i < size; i++) {
	    		ProdutoBase produtoExample = getClienteProduto(clienteProdutoList, i);
	    		listProdutos.removeElement(produtoExample);
	    	}
	    	return listProdutos;
	    }
	    clienteProdutoList = ClienteProdutoService.getInstance().findClienteProdutoList(cdCliente, ClienteProduto.RELACAORESTRICAO);
	    size = clienteProdutoList.size();
	    if (clienteProdutoList.size() > 0) {
	    	Vector produtoFinalList = new Vector();
	    	for (int i = 0; i < size; i++) {
    			ProdutoBase produtoExample = getClienteProduto(clienteProdutoList, i);
    			int indexProduto = listProdutos.indexOf(produtoExample);
    			if (indexProduto != -1) {
    				produtoFinalList.addElement((ProdutoBase)listProdutos.items[indexProduto]);
    			}
    		}
    		return produtoFinalList;
	    } else {
	    	clienteProdutoList = ClienteProdutoService.getInstance().findClienteProdutoList(cdCliente, ProdutoCliente.RELACAOEXCECAO);
	    	size = clienteProdutoList.size();
	    	if (clienteProdutoList.size() > 0) {
	    		for (int i = 0; i < size; i++) {
	    			ProdutoBase produtoExample = getClienteProduto(clienteProdutoList, i);
		    		listProdutos.removeElement(produtoExample);
		    	} 	
	    	}
	    }
	    return listProdutos;
	}

	private ProdutoBase getClienteProduto(Vector clienteProdutoList, int i) {
		    		ClienteProduto clienteProduto = (ClienteProduto) clienteProdutoList.items[i];
		    		ProdutoBase produtoExample = new ProdutoBase("");
		    		produtoExample.cdEmpresa = clienteProduto.cdEmpresa;
		    		produtoExample.cdRepresentante = clienteProduto.cdRepresentante;
		    		produtoExample.cdProduto = clienteProduto.cdProduto;
		return produtoExample;
		    	} 	
	//--
	public boolean hasSugestaoVendaDifPedido(Pedido pedido) throws SQLException {
		return !pedido.isPedidoBonificacao() && getSugestaoVendaDifPedidoDomainList(pedido).size() > 0;
	}

	public boolean isGrifaProdutoSemEstoqueNaLista(ProdutoBase produtoBase, String cdLocalEstoque) throws SQLException {
		if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() && !produtoBase.isIgnoraValidacao() && !produtoBase.possuiGrade) {
			return produtoSemEstoque(produtoBase, cdLocalEstoque);
		}
		return false;
	}

	public boolean produtoSemEstoque(ProdutoBase produtoBase, String cdLocalEstoque) throws SQLException {
		cdLocalEstoque = ValueUtil.isNotEmpty(cdLocalEstoque) ? cdLocalEstoque : Estoque.CD_LOCAL_ESTOQUE_PADRAO;
		double qtEstoque = 0;
		if (produtoBase.estoque == null) {
			qtEstoque = EstoqueService.getInstance().getQtEstoque(produtoBase.cdProduto, cdLocalEstoque);
		} else {
			qtEstoque = produtoBase.estoque.qtEstoque;
		}
		return qtEstoque <= 0;
	}
	
	public boolean isGrifaProdutoSemEstoqueNaRemessa(ProdutoBase produtoBase, Pedido pedido) throws SQLException {
		double qtEstoque = 0;
		if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() && !produtoBase.isIgnoraValidacao() && !produtoBase.possuiGrade) {
			ItemPedido item = new ItemPedido();
			item.pedido = pedido;
			item.cdProduto = produtoBase.cdProduto;
			qtEstoque = RemessaEstoqueService.getInstance().getEstoqueDisponivelProduto(item);
		}else {
			qtEstoque = produtoBase.estoque.qtEstoque;
		}
		return qtEstoque <= 0;
	}
	
	public Vector getSugestaoVendaDifPedidoDomainList(Pedido pedido) throws SQLException {
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdCliente = pedido.cdCliente;
		pedidoFilter.cdEmpresa = pedido.cdEmpresa;
		pedidoFilter.cdRepresentante = pedido.cdRepresentante;
		pedidoFilter.cdStatusPedidoDif = LavenderePdaConfig.cdStatusPedidoAberto;
		Vector pedidoList = PedidoPdbxDao.getInstance().findAllPedidosSugestao(pedidoFilter);
		//--
		Vector listFinal = new Vector();
		for (int i = 0; i < pedidoList.size(); i++) {
			Pedido pedidoTemp = (Pedido)pedidoList.items[i];
			pedidoTemp.cdCliente = pedido.cdCliente;
			pedidoTemp.cdEmpresa = pedido.cdEmpresa;
			pedidoTemp.cdRepresentante = pedido.cdRepresentante;
			Vector itemPedidoErpDifPedido = ItemPedidoErpDifService.getInstance().findAllByPedido(pedidoTemp);
			//--
			forSugestoes: for (int j = 0; j < itemPedidoErpDifPedido.size(); j++) {
				ItemPedidoErpDif itemPedidoErpDif = (ItemPedidoErpDif)itemPedidoErpDifPedido.items[j];
				for (int x = 0; x < pedido.itemPedidoList.size(); x++) {
					if (itemPedidoErpDif.cdProduto.equals(((ItemPedido)pedido.itemPedidoList.items[x]).cdProduto)) {
						continue forSugestoes;
					}
				}
				itemPedidoErpDif.dtEmissaoPedido = pedidoTemp.dtEmissao;
				listFinal.addElement(itemPedidoErpDif);
			}
			//--
			if (i + 1 == LavenderePdaConfig.usaSugestaoVendaBaseadaDifPedidos) {
				break;
			}
		}
		listFinal.qsort();
		return listFinal;
	}

	public Vector getProdutosHistoricoNaoInseridosPedido(Pedido pedido) throws SQLException {
    	return 	ItemPedidoService.getInstance().findProdutosPendentesByCliente(pedido, pedido.cdCliente, pedido.itemPedidoList, true);
	}
	
	public String getDescricaoProdutoComReferencia(ProdutoBase produto) {
    	return getDescricaoProdutoComReferencia(produto, null);
	}

	public String getDescricaoProdutoComReferencia(ProdutoBase produto, ItemPedido itemPedido) {
		String dsProduto = null;
		if (produto != null) {
			if (ValueUtil.isNotEmpty(produto.dsReferencia) && LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
				dsProduto = "[" + StringUtil.getStringValue(produto.dsReferencia) + "] ".concat(StringUtil.getStringValue(produto.dsProduto));
			} else if (ValueUtil.isNotEmpty(produto.dsReferencia) && LavenderePdaConfig.isMostraDescricaoReferenciaAposDsProduto()) {
				dsProduto = StringUtil.getStringValue(produto.dsProduto).concat(" [" + StringUtil.getStringValue(produto.dsReferencia) + "]");
			} else {
				dsProduto = StringUtil.getStringValue(produto.dsProduto);
				}
			}
		if (ValueUtil.isEmpty(dsProduto) && itemPedido != null && ValueUtil.isNotEmpty(itemPedido.dsProduto)) {
			dsProduto = StringUtil.getStringValue(itemPedido.dsProduto);
		}
		return dsProduto;
	}
	
	public String getDescricaoProdutoReferenciaConsideraCodigo(ProdutoBase produto) throws SQLException {
		return getDescricaoProdutoReferenciaConsideraCodigo(produto, null);
	}
	
	public String getDescricaoProdutoReferenciaConsideraCodigo(ProdutoBase produto, ItemPedido itemPedido) throws SQLException {
		String refertext = LavenderePdaConfig.isMostraDescricaoReferencia() ? getDescricaoProdutoComReferencia(produto, itemPedido) : getDsProduto(produto, itemPedido);
		if (LavenderePdaConfig.ocultaColunaCdProduto) {
			return refertext;
		} else if (produto != null && ValueUtil.isNotEmpty(produto.cdProduto)) {
			if (ValueUtil.isNotEmpty(refertext)) {
				return produto.cdProduto + DESCRICAO_SEPARATOR + refertext;
			}
		} else if (itemPedido != null && ValueUtil.isNotEmpty(itemPedido.cdProduto)) {
			if (ValueUtil.isNotEmpty(refertext)) {
				return itemPedido.cdProduto + DESCRICAO_SEPARATOR + refertext;
		}
	}
		return refertext;
			}

	public boolean isProdutoHaveDescPromocional(ProdutoBase produto, String cdTabelaPreco) {
		if (produto instanceof ProdutoTabPreco) {
			ProdutoTabPreco produtoTabPreco = (ProdutoTabPreco) produto;
			String[] tabelasPromocionais = StringUtil.split(StringUtil.getStringValue(produtoTabPreco.dsTabPrecoDescPromocionalList), '|');
			Vector vector = new Vector(tabelasPromocionais);
			int index = vector.indexOf(cdTabelaPreco);
			if (index != -1) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isProdutoHaveDescPromocional(ProdutoBase produto, Pedido pedido) throws SQLException {
		String cdTabelaPreco = getCdTabelaPreco(pedido);
		return DescPromocionalService.getInstance().isProdutoPossuiValorNoGrupoDescPromo(pedido, produto, cdTabelaPreco);
	}

	private String getCdTabelaPreco(Pedido pedido) throws SQLException {
		String cdTabelaPreco = null;
		if (!LavenderePdaConfig.permiteTabPrecoItemDiferentePedido()) {
			if (!LavenderePdaConfig.ocultaTabelaPrecoPedido) {
				cdTabelaPreco = pedido.cdTabelaPreco;
			} else if (ValueUtil.isNotEmpty(pedido.getCliente().cdTabelaPreco)) {
				cdTabelaPreco = pedido.getCliente().cdTabelaPreco;
			}
		}
		return cdTabelaPreco;
	}
	
	public Vector filtraSugestaoVendaPorTipoPedido(Pedido pedido, Vector sugestaoVendaProdList) throws SQLException {
		ProdutoTipoPed produtoTipoPedFilter = new ProdutoTipoPed();
		produtoTipoPedFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoTipoPedFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		produtoTipoPedFilter.cdTipoPedido = pedido.cdTipoPedido;
		Vector listProdutoTipoPed = ProdutoTipoPedService.getInstance().findAllByExample(produtoTipoPedFilter);
		if (listProdutoTipoPed.size() == 0) {
			return sugestaoVendaProdList; // Se não tem nenhum relacionamento, não restringe nada e mostra todos os produtos.
		}
		TipoPedido tipoPedido = TipoPedidoService.getInstance().getTipoPedido(pedido.cdTipoPedido);
		Vector result = new Vector();
		int size = sugestaoVendaProdList.size();
		for (int i = 0; i < size; i++) {
			SugestaoVendaProd sugestaoVendaProd = (SugestaoVendaProd) sugestaoVendaProdList.items[i];
			ProdutoTipoPed produtoTipoPed = new ProdutoTipoPed();
			produtoTipoPed.cdEmpresa = SessionLavenderePda.cdEmpresa;
			produtoTipoPed.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			produtoTipoPed.cdTipoPedido = pedido.cdTipoPedido;
			produtoTipoPed.cdProduto = sugestaoVendaProd.cdProduto;
			boolean encontrou = false;
			//--Verfica se o item está na lista
			if (listProdutoTipoPed.indexOf(produtoTipoPed) != -1) {
				encontrou = true;
			}
			if (!encontrou && tipoPedido.isFlExcecaoProduto()) {
				result.addElement(sugestaoVendaProd);
			} else if (encontrou && !tipoPedido.isFlExcecaoProduto()) {
				result.addElement(sugestaoVendaProd);
			}
		}
		return result;
		
	}
	
    public double getPrecoProduto(ProdutoBase produto, CondicaoComercial condicaoComercial, String cdTabelaPreco, String cdUf) throws SQLException {
    	if (LavenderePdaConfig.usaPrecoPadraoProdutoParaSerExibidoNaLista()) {
    		if (LavenderePdaConfig.realizaCalculoIndicesPrecoProdutoListaAdiconarItensPedido) {
    			if (LavenderePdaConfig.usaCondicaoComercialPedido) {
    				CondicaoComercialService.getInstance().aplicaIndiceFinanceiroCondComercial(produto, condicaoComercial);
    			}
    			if (LavenderePdaConfig.usaIndiceFinanceiroSupRep) {
				    produto.vlPrecoPadrao = ItemTabelaPrecoService.getInstance().aplicaIndiceFinanceiroSupRep(produto.vlPrecoPadrao);
    		}
    			if(LavenderePdaConfig.usaIndiceGrupoProdutoTabPrecoCondPagto) {
    				ItemTabelaPreco itemTabelaPreco = ItemTabelaPrecoService.getInstance().getItemTabelaPreco(cdTabelaPreco, produto.cdProduto, cdUf);
    				IndiceGrupoProd indiceGrupoProd = null;
    				if(itemTabelaPreco!=null) {
    					indiceGrupoProd = IndiceGrupoProdService.getInstance().escolheIndicePorItemTabPreco(itemTabelaPreco);
    					if(indiceGrupoProd!=null) {
        					Double vlPrecoPadrao = ValueUtil.round(produto.vlPrecoPadrao * indiceGrupoProd.vlIndiceFinanceiro);
        					return vlPrecoPadrao;
        				}
    				}
    				
    			}
    		}
    		return produto.vlPrecoPadrao;
    	} else {
    		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
    			int cdPrazoPagtoPreco = produto.itemTabelaPreco != null  ? produto.itemTabelaPreco.cdPrazoPagtoPreco : 1;
    			int qtItem = produto.itemTabelaPreco != null ? produto.itemTabelaPreco.qtItem : ItemTabelaPreco.QTITEM_VALOR_PADRAO;
    			ItemTabelaPreco itemTabelaPreco = ItemTabelaPrecoService.getInstance().getItemTabelaPreco(cdTabelaPreco, produto.cdProduto, cdUf, produto.cdUnidade, cdPrazoPagtoPreco, qtItem);
    			produto.itemTabelaPreco = (ItemTabelaPreco) itemTabelaPreco.clone();
    		}
    		if (produto.itemTabelaPreco == null) {
    			ItemTabelaPreco itemTabelaPreco = ItemTabelaPrecoService.getInstance().getTabelaPrecoFilter(cdTabelaPreco, produto.cdProduto, cdUf, ProdutoUnidade.CDUNIDADE_PADRAO, 0, 0);
    			itemTabelaPreco = (ItemTabelaPreco) ItemTabelaPrecoService.getInstance().findByPrimaryKey(itemTabelaPreco);
    			produto.itemTabelaPreco = itemTabelaPreco;
    		}
		    if (LavenderePdaConfig.usaCondicaoComercialPedido) {
			    CondicaoComercialService.getInstance().aplicaIndiceFinanceiroCondComercialProdutoBase(produto, condicaoComercial, true);
    	}
		    if (LavenderePdaConfig.realizaCalculoIndicesPrecoProdutoListaAdiconarItensPedido && LavenderePdaConfig.usaIndiceFinanceiroSupRep && produto.itemTabelaPreco != null) {
			    produto.itemTabelaPreco.vlUnitario = ItemTabelaPrecoService.getInstance().aplicaIndiceFinanceiroSupRep(produto.itemTabelaPreco.vlUnitario);
    }
		    if(LavenderePdaConfig.usaIndiceGrupoProdutoTabPrecoCondPagto && produto.itemTabelaPreco != null) {
				IndiceGrupoProd indiceGrupoProd = IndiceGrupoProdService.getInstance().escolheIndicePorItemTabPreco(produto.itemTabelaPreco);
				if(indiceGrupoProd != null) {
					produto.itemTabelaPreco.vlUnitario = ValueUtil.round(produto.itemTabelaPreco.vlUnitario * indiceGrupoProd.vlIndiceFinanceiro);
				}
			}
    		return produto.itemTabelaPreco != null ? produto.itemTabelaPreco.vlUnitario : 0;
    	}
    }
    
    public Vector findProdutosSugeridosCombo(Produto filter, ItemCombo itemCombo, String cdPedido, String cdCliente, String flOrigemPedido, String cdCombo, String flTipoItemCombo, boolean isFechandoPedidoSugestaoCombo) throws SQLException {
	    return ProdutoPdbxDao.getInstance().findProdutosByItemCombo(filter, itemCombo, cdPedido, cdCliente, flOrigemPedido, cdCombo, flTipoItemCombo, isFechandoPedidoSugestaoCombo);
    }
    
	public Vector findProdutosSugeridosComboSummary(Produto filter, ItemCombo itemCombo, String cdPedido, String cdCliente, String flOrigemPedido, String cdCombo, String flTipoItemCombo) throws SQLException {
		return ProdutoPdbxDao.getInstance().findProdutosByItemComboSummary(filter, itemCombo, cdPedido, cdCliente, flOrigemPedido, cdCombo, flTipoItemCombo);
	}
    
	public String getDsProdutoSlide(ProdutoBase produto) {
		if (produto == null) {
			return "";
		}
		String descricao = !LavenderePdaConfig.ocultaColunaCdProduto ? produto.cdProduto + DESCRICAO_SEPARATOR + getDescricaoProdutoComReferencia(produto) : getDescricaoProdutoComReferencia(produto);
        return StringUtil.getStringValue(descricao);
	}
    
    
    public Vector findAllProdutoAndQtdVendaPeriodo(String dsFiltro, Produto produto) throws SQLException {
    	addProdutoFilterByDsFiltro(dsFiltro, produto);
		return ProdutoPdbxDao.getInstance().findAllProdutoAndQtdVendaPeriodo(produto);
    }

	public void addProdutoFilterByDsFiltro(String dsFiltro, Produto produto) {
		boolean onlyStartString = false;
		boolean isDsFiltroValidNumber = ValueUtil.isValidNumber(dsFiltro);
    	if (LavenderePdaConfig.usaPesquisaInicioString) {
    		if (dsFiltro.startsWith("*")) {
        		dsFiltro = dsFiltro.substring(1);
    		} else {
        		onlyStartString = true;
    		}
    	}
    	String str = strBufferLikeDesc(dsFiltro, onlyStartString).toString();
		if (ValueUtil.isNotEmpty(dsFiltro)) {
			produto.dsProduto = LavenderePdaConfig.usaFiltraProdutoCodigoExato() && isDsFiltroValidNumber ? "" : str;
			if (!LavenderePdaConfig.usaFiltroSomenteDescricaoProduto) {
				produto.nuCodigoBarras = dsFiltro;
				if (LavenderePdaConfig.usaFiltraProdutoCodigoExato() && isDsFiltroValidNumber) {
					produto.cdProduto = dsFiltro;
				} else {
					produto.cdProdutoLikeFilter = str;
				}
				if (LavenderePdaConfig.usaFiltroMarcaProduto) {
					produto.dsMarca = str;
				}
			}
			if (LavenderePdaConfig.isMostraDescricaoReferencia()) {
				if (LavenderePdaConfig.usaFiltraProdutoCodigoExato() && isDsFiltroValidNumber) {
					produto.dsReferencia = dsFiltro;
				} else {
					produto.dsReferenciaLikeFilter = str;
				}
			}
		}
	}
    
    public void addDescricaoProdutoLista(Produto produto, Vector itens) {
		if (!LavenderePdaConfig.ocultaColunaCdProduto && LavenderePdaConfig.isMostraDescricaoReferencia()) {
			itens.addElement(produto.cdProduto + DESCRICAO_SEPARATOR);
			if (LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
				itens.addElement("[" + StringUtil.getStringValue(produto.dsReferencia) + "] " + StringUtil.getStringValue(produto.dsProduto));
			} else {
				itens.addElement(StringUtil.getStringValue(produto.dsProduto) + " [" + StringUtil.getStringValue(produto.dsReferencia) + "]");
			}
		} else if (!LavenderePdaConfig.ocultaColunaCdProduto && !LavenderePdaConfig.isMostraDescricaoReferencia()) {
			itens.addElement(produto.cdProduto + DESCRICAO_SEPARATOR);
			itens.addElement(StringUtil.getStringValue(produto.dsProduto));
		} else if (LavenderePdaConfig.ocultaColunaCdProduto && LavenderePdaConfig.isMostraDescricaoReferencia()) {
			if (LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
				itens.addElement("[" + StringUtil.getStringValue(produto.dsReferencia) + "] ");
				itens.addElement(StringUtil.getStringValue(produto.dsProduto));
			} else {
				itens.addElement(StringUtil.getStringValue(produto.dsProduto));
				itens.addElement(" [" + StringUtil.getStringValue(produto.dsReferencia) + "]");
			}
		} else if (LavenderePdaConfig.ocultaColunaCdProduto && !LavenderePdaConfig.isMostraDescricaoReferencia()) {
			itens.addElement(produto.toString());
			itens.addElement("");
		}
	}
    
    public void addDescricaoProdutoLista(String cdProduto, Vector itens) throws SQLException {
    	Produto produtoFilter = new Produto();
		produtoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		produtoFilter.cdProduto = cdProduto;
		produtoFilter.dsProduto = ProdutoPdbxDao.getInstance().findColumnByRowKey(produtoFilter.getRowKey(), "DSPRODUTO");
		if (LavenderePdaConfig.isMostraDescricaoReferencia()) {
			produtoFilter.dsReferencia = ProdutoPdbxDao.getInstance().findColumnByRowKey(produtoFilter.getRowKey(), "DSREFERENCIA");
		}
		addDescricaoProdutoLista(produtoFilter, itens);
    }
    
    public Vector findProdutoListGeracaoRemessa(Produto filter) throws SQLException {
    	return ProdutoPdbxDao.getInstance().findProdutoListGeracaoRemessa(filter);
    }
    
    public Vector getProdutoListDevolucao(boolean parcial) throws SQLException {
    	return ProdutoPdbxDao.getInstance().getProdutoListDevolucao(parcial);
    }
    
    public Produto findProdutoByCdBarras(Produto produto) throws SQLException {
    	return ProdutoPdbxDao.getInstance().findProdutoByCdBarras(produto);
    }
    
	public Vector findProdutoByDsPrincipioAtivoDsProduto(Produto produto, String cdGrupoProduto1, String cdGrupoProduto2, String cdGrupoProduto3, String cdGrupoProduto4, String empresa, String representante) throws SQLException {
		return ProdutoPdbxDao.getInstance().findProdutoByDsPrincipioAtivoDsProduto(produto, cdGrupoProduto1, cdGrupoProduto2, cdGrupoProduto3, cdGrupoProduto4, empresa, representante);
	}

	public Vector findProdutoByCategoria(StringBuffer cdProdutoListIn) throws SQLException {
		return ProdutoPdbxDao.getInstance().findProdutoByCategoria(cdProdutoListIn);
	}

	public Vector findProdutosNotInPesquisaMercado(PesquisaMercadoConfig pesquisaMercadoConfig, String dsFiltro, String sortAtributte, String sortAsc) throws SQLException {
        return ProdutoPdbxDao.getInstance().findProdutosNotInPesquisaMercado(pesquisaMercadoConfig, dsFiltro, sortAtributte, sortAsc);
	}

	public boolean isExistsProduto(ItemPedido itemPedido) throws SQLException {
		if (itemPedido == null || itemPedido.cdEmpresa == null || itemPedido.cdRepresentante == null || itemPedido.cdProduto == null) {
			return false;
		}
		Produto produtoFilter = new Produto();
		produtoFilter.cdEmpresa = itemPedido.cdEmpresa;
		produtoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Produto.class);
		produtoFilter.cdProduto = itemPedido.cdProduto;
		return findByPrimaryKey(produtoFilter) != null;
	}
	
	public Vector findProdutosAgrupador(Produto produto, boolean fromKit, Pedido pedido, boolean consideraEstoque) throws SQLException {
    	return findProdutosAgrupador(produto, fromKit, null, pedido, consideraEstoque);
	}

	public Vector findProdutosAgrupador(Produto produto, boolean fromKit, String dsFiltro, Pedido pedido, boolean consideraEstoque) throws SQLException {
		return ProdutoPdbxDao.getInstance().findProdutosAgrupador(produto, fromKit, dsFiltro, pedido, consideraEstoque);
	}

	public Vector findAllProdutoSimilarByAgrupador(ProdutoSimilar produtoSimilarFilter, Pedido pedido) throws SQLException {
    	Produto produtoFilter = new Produto(produtoSimilarFilter.cdEmpresa, produtoSimilarFilter.cdRepresentante);
    	produtoFilter.cdProduto = produtoSimilarFilter.cdProduto;
		produtoFilter.cdAgrupProdSimilar = produtoSimilarFilter.cdAgrupProdSimilar;
		produtoFilter.cdCliente = produtoSimilarFilter.cdClienteFilter;
		if (produtoFilter.estoque == null && LavenderePdaConfig.isUsaLocalEstoque()) {
			Estoque estoque = new Estoque();
			estoque.cdLocalEstoque = pedido == null ? Estoque.CD_LOCAL_ESTOQUE_PADRAO : pedido.getCdLocalEstoque();
			produtoFilter.estoque = estoque;
		}
		return ProdutoPdbxDao.getInstance().findAllProdutoSimilarByAgrupador(produtoFilter);
	}
	
	public void filtraSugestaoVendaPorProdutoClienteExclusivo(Pedido pedido, Vector sugestaoVendaProdList) throws SQLException {
		ProdutoCliente produtoClienteFilter = new ProdutoCliente();
	    produtoClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
	    produtoClienteFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ProdutoCliente.class);
	    produtoClienteFilter.cdCliente = pedido.cdCliente;
	    produtoClienteFilter.flTipoRelacao = ProdutoCliente.RELACAOEXCLUSIVA;
	    Vector produtoClienteList = ProdutoClienteService.getInstance().findAllByExample(produtoClienteFilter);
	    int size = sugestaoVendaProdList.size();
	    if (produtoClienteList.size() > 0) {
	    	for (int i = 0; i < size; i++) {
	    		produtoClienteFilter.cdProduto = getCdProduto(sugestaoVendaProdList.items[i]);
	    		if (isPossuiProdutoTipoRelacaoExclusivo(produtoClienteFilter, produtoClienteList)) {
	    			sugestaoVendaProdList.removeElementAt(i);
	    			i--;
	    			size--;
	    		}
	    	}
	    }
	}
	
	public void filtraSugestaoVendaPorClienteProdutoExclusivo(Pedido pedido, Vector sugestaoVendaProdList) throws SQLException {
		ClienteProduto clienteProdutoFilter = new ClienteProduto();
	    clienteProdutoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
	    clienteProdutoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ClienteProduto.class);
	    clienteProdutoFilter.cdCliente = pedido.cdCliente;
	    clienteProdutoFilter.flTipoRelacao = ClienteProduto.RELACAOEXCLUSIVA;
	    Vector clienteProdutoList = ClienteProdutoService.getInstance().findAllByExample(clienteProdutoFilter);
	    int size = sugestaoVendaProdList.size();
	    if (clienteProdutoList.size() > 0) {
	    	for (int i = 0; i < size; i++) {
	    		clienteProdutoFilter.cdProduto = getCdProduto(sugestaoVendaProdList.items[i]);
	    		if (isPossuiProdutoTipoRelacaoExclusivo(clienteProdutoFilter, clienteProdutoList)) {
	    			sugestaoVendaProdList.removeElementAt(i);
	    			i--;
	    			size--;
	    		}
	    	}
	    }
	}
	
	public void filtraSugestaoVendaPorProdutoCondPagtoExclusivo(Pedido pedido, Vector sugestaoVendaProdList) throws SQLException {
		ProdutoCondPagto produtoCondPagtoFilter = new ProdutoCondPagto();
		produtoCondPagtoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoCondPagtoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ProdutoCondPagto.class);
		produtoCondPagtoFilter.cdCondicaoPagamento = pedido.cdCondicaoPagamento;
		produtoCondPagtoFilter.flTipoRelacao = ProdutoCondPagto.RELACAOEXCLUSIVA;
		Vector clienteProdutoList = ProdutoCondPagtoService.getInstance().findAllByExample(produtoCondPagtoFilter);
		int size = sugestaoVendaProdList.size();
		if (clienteProdutoList.size() > 0) {
			for (int i = 0; i < size; i++) {
				produtoCondPagtoFilter.cdProduto = getCdProduto(sugestaoVendaProdList.items[i]);
				if (isPossuiProdutoTipoRelacaoExclusivo(produtoCondPagtoFilter, clienteProdutoList)) {
					sugestaoVendaProdList.removeElementAt(i);
					i--;
					size--;
				}
			}
		}
	}
	
	private boolean isPossuiProdutoTipoRelacaoExclusivo(ProdutoTipoRelacaoBase produtoTipoRelacaoFilter, Vector produtoClienteList) {
		int size = produtoClienteList.size();
		for (int i = 0; i < size; i++) {
			ProdutoTipoRelacaoBase produtoTipoRelacaoBase = (ProdutoTipoRelacaoBase) produtoClienteList.items[i];
			if (produtoTipoRelacaoFilter.cdProduto.equals(produtoTipoRelacaoBase.cdProduto)) {
				return true;
			}
		}
		return false;
	}
	
	private String getCdProduto(Object domain) {
		if (domain instanceof SugestaoVendaProd) {
			return ((SugestaoVendaProd) domain).cdProduto;
		} else if (domain instanceof GiroProduto) {
			return ((GiroProduto) domain).cdProduto;
		} else {
			return ((ProdutoSimilar) domain).cdProdutoSimilar;
		}
	}
	


	public Vector findAllProdutoByFamiliaProd(Produto produto) throws SQLException {
		return ProdutoPdbxDao.getInstance().findAllProdutoByFamiliaProd(produto);
	}

    public Image[] getIconsMarcadores(ProdutoBase produto, Map<String, Image> mapMarcadores) throws SQLException {
    	int size = produto.cdMarcadores == null ? 0 : produto.cdMarcadores.size();
    	Image[] icons = new Image[size];
    	Vector cdMarcadores = produto.cdMarcadores;
    	for (int i = 0; i < size; i++) {
    		Image img = mapMarcadores.get(String.valueOf(cdMarcadores.items[i]));
    		if (img != null) {
    			icons[i] = img;
    		}
    	}
    	return getIconsNotNull(icons);
    }
    
    public void loadImagesMarcadores(Map<String, Image> mapIconsMarcadores, int iconSize) throws SQLException {
		Vector marcadorList = MarcadorService.getInstance().buscaMarcadoresVigentes(Marcador.ENTIDADE_MARCADOR_PRODUTO);
		int size = marcadorList.size();
		for (int i = 0; i < size; i++) {
			Marcador marcador = (Marcador)marcadorList.items[i];
			Image icon = null;
			if (marcador.imMarcadorAtivo != null) {
				icon = UiUtil.getSmoothScaledImage(UiUtil.getImage(marcador.imMarcadorAtivo), iconSize, iconSize);
			}
			mapIconsMarcadores.put(marcador.cdMarcador, icon);
		}
	}
    
    private Image[] getIconsNotNull(Image[] icons) {
    	Vector list = new Vector();
    	list.addElementsNotNull(icons);
    	int size = list.size();
    	if (size > 0) {
    		Image[] imgs = new Image[size];
    		for (int i = 0; i < size; i++) {
    			imgs[i] = (Image)list.items[i];
    		}
    		return imgs;
    	}
    	return null;
    }
    
    public boolean isPossuiFamiliasDescProg(Pedido pedido, String cdFamiliaDescProg) throws SQLException {
    	return ProdutoPdbxDao.getInstance().isPossuiFamiliasDescProg(pedido, cdFamiliaDescProg);
    }
    
    public Image[] getIconsLegend(ProdutoBase domain, Map<String, Image> mapIconsMarcadores, String cdCliente, int size, boolean filtrandoAgrupadorGrade) throws SQLException {
    	Image[] iconsLegend = null;
    	if (LavenderePdaConfig.usaGradeProduto5() && filtrandoAgrupadorGrade && ValueUtil.isNotEmpty(domain.getDsAgrupadorGrade())) {
    		return new Image[] {UiUtil.getColorfulImage("images/grade.png", size, size)};
    	}
		if (LavenderePdaConfig.apresentaMarcadoresProduto()) {
			iconsLegend = getIconsMarcadores(domain, mapIconsMarcadores);
		}
		return iconsLegend;
    }
    
    public Map<String, Produto> montaHashProduto(String cdEmpresa, String cdItemGrade1, String dsAgrupadorGrade, String cdTabelaPreco) throws SQLException {
    	Produto filter = new Produto();
    	filter.cdEmpresa = cdEmpresa;
    	filter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Produto.class);
    	filter.cdItemGrade1 = cdItemGrade1;
    	filter.setDsAgrupadorGrade(dsAgrupadorGrade);
    	filter.itemTabelaPreco = new ItemTabelaPreco();
    	filter.itemTabelaPreco.cdTabelaPreco = cdTabelaPreco;
    	filter.hideProductPicture = true;
    	
    	Vector list = ProdutoPdbxDao.getInstance().findProdutoByGrade(filter);
    	int size = list.size();
    	Map<String, Produto> hashProduto = new HashMap<>(size);
    	for (int i = 0; i < size; i++) {
    		Produto produto = (Produto) list.items[i];
    		hashProduto.put(produto.getKeyGrade(), produto);
    	}
    	return hashProduto;
    }
    
    public boolean isSameAgrupadorGrade(ProdutoBase produto, ItemPedido itemPedido) {
    	Produto itemPedidoProduto = getProdutoOfItemPedido(itemPedido);
    	if (itemPedidoProduto != null) {
    		return ValueUtil.valueEqualsIfNotNull(produto.getDsAgrupadorGrade(), itemPedidoProduto.getDsAgrupadorGrade());
    	}
    	return false;
    }
    
    public Produto getProdutoOfItemPedido(ItemPedido itemPedido) {
    	try {
    		return itemPedido.getProduto();
    	} catch (SQLException e) {
    		return null;
    	}
    }
    
    public double getMaxMultiploEspecialPermitido(double qtInserida, Produto produto) {
    	double nuMultiploEspecial = produto == null || produto.nuMultiploEspecial < 1 ? 1 : produto.nuMultiploEspecial;
    	return (int) qtInserida / (int) nuMultiploEspecial * (int) nuMultiploEspecial;
    }
    
    public String getDsAgrupadorGradeByRowKey(ProdutoBase produto) throws SQLException {
    	return ProdutoPdbxDao.getInstance().getDsAgrupadorGradeByRowKey(produto);
    }
    
    
    public boolean isIgnoraValidacao(final ProdutoBase produto) {
    	return LavenderePdaConfig.isUsaFlIgnoraValidaco() && ValueUtil.getBooleanValue(produto.flIgnoraValidacao);
    }

    public Vector findProdutoGradeComNmFotoByItemPedido(ItemPedido itemPedido) throws SQLException {
		String cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Produto.class);
    	return ((ProdutoPdbxDao)getCrudDao()).findProdutoComNmFotoByItemPedido(itemPedido, cdRepresentante);
    }
    
	public double getValorBonusProdutoNaLista(ItemTabelaPreco itemTabelaPreco) {
		return itemTabelaPreco.vlUnitario - itemTabelaPreco.vlBase;
	}
	
	public boolean isParametrizadoJoinComItemTabelaPreco() {
		return LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPrecoTipo3()
				|| LavenderePdaConfig.isFiltraItemTabelaPrecoListaProduto()
				|| LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido()
				|| LavenderePdaConfig.usaValorExcecaoNaCondicaoComercial || LavenderePdaConfig.usaFiltroComissao
				|| LavenderePdaConfig.isMostraPrecoItemStNaListaProduto()
				|| LavenderePdaConfig.isMostraPrecoItemStNaListaProdutoDoPedido() 
				|| LavenderePdaConfig.isMostraPrecoItemStNoDetalheDoProduto()
				|| LavenderePdaConfig.destacaProdutoQuantidadeMaximaVenda
				|| LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente
				|| LavenderePdaConfig.usaGrupoDestaqueItemTabPreco;
		
	}
	
	public void updateFlVendidoProduto(Produto produto, String flVendido) throws SQLException {
		ProdutoPdbxDao.getInstance().updateFlVendidoProduto(produto, flVendido);
	}

	public Vector findProdutoGradeComDsProdutoByItemPedido(ItemPedido itemPedido) throws SQLException {
		String cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Produto.class);
		return ((ProdutoPdbxDao)getCrudDao()).findProdutoGradeComDsProdutoByItemPedido(itemPedido, cdRepresentante);
	}
	
	public void validateProdutoRelacaoDisponivel(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		ProdutoBase produtoFilter = LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() ? new ProdutoTabPreco() : new Produto();
		produtoFilter.cdEmpresa = itemPedido.cdEmpresa;
		produtoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Produto.class);
		produtoFilter.cdProduto = itemPedido.cdProduto;
		
		ItemTabelaPreco itemTabelaPreco = new ItemTabelaPreco();
		ItemTabelaPreco itemTabPreco = itemPedido.getItemTabelaPreco();
		itemTabelaPreco.cdEmpresa = itemTabPreco.cdEmpresa;
		itemTabelaPreco.cdRepresentante = itemTabPreco.cdRepresentante;
		itemTabelaPreco.cdTabelaPreco = itemTabPreco.cdTabelaPreco;
		itemTabelaPreco.cdProduto = itemTabPreco.cdProduto;
		itemTabelaPreco.cdUnidade = itemTabPreco.cdUnidade;
		itemTabelaPreco.qtItem = itemTabPreco.qtItem;
		
		produtoFilter.itemTabelaPreco = itemTabelaPreco;
		boolean produtoRelacaoCliente = false;
		if (LavenderePdaConfig.filtraClientePorProdutoRepresentante) {
			produtoRelacaoCliente = true;
			produtoFilter.clienteProdutoFilter = ClienteProdutoService.getInstance().getClienteProdutoFilter(pedido.cdCliente);
		}
		if (LavenderePdaConfig.filtraProdutoClienteRepresentante) {
			produtoRelacaoCliente = true;
			produtoFilter.produtoClienteFilter = ProdutoClienteService.getInstance().getProdutoClienteFilter(pedido.cdCliente);
		}
		if (produtoRelacaoCliente) {
			validateProdutoTipoRelacaoByProduto(pedido, itemPedido, produtoFilter, true);
		}
		if (LavenderePdaConfig.usaFiltroProdutoCondicaoPagamentoRepresentante) {
			produtoFilter.clienteProdutoFilter = null;
			produtoFilter.produtoClienteFilter = null;
			produtoFilter.produtoCondPagtoFilter = ProdutoCondPagtoService.getInstance().getProdutoCondPagtoFilter(pedido.cdCondicaoPagamento);
			validateProdutoTipoRelacaoByProduto(pedido, itemPedido, produtoFilter, false);
		}
	}

	private void validateProdutoTipoRelacaoByProduto(Pedido pedido, ItemPedido itemPedido, ProdutoBase produtoFilter, boolean produtoRelacaoCliente) throws SQLException {
		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
			if (ProdutoTabPrecoService.getInstance().countByExampleFullSQL(produtoFilter) == 0) {
				throw new ProdutoTipoRelacaoException(getMessageValidateProdutoTipoRelacao(pedido, produtoRelacaoCliente));
			}
		} else if (countByExampleFullSQL(produtoFilter) == 0) {
			throw new ProdutoTipoRelacaoException(getMessageValidateProdutoTipoRelacao(pedido, produtoRelacaoCliente));
		}
	}

	private String getMessageValidateProdutoTipoRelacao(Pedido pedido, boolean produtoRelacaoCliente) throws SQLException {
		String dsCausa = produtoRelacaoCliente ? MessageUtil.getMessage(Messages.PRODUTOTIPORELACAO_RESTRITO_POR_CLIENTE, pedido.getCliente().toString())
				: MessageUtil.getMessage(Messages.PRODUTOTIPORELACAO_RESTRITO_POR_CONDPAGTO, pedido.getCondicaoPagamento().toString());
		return MessageUtil.getMessage(Messages.PRODUTOTIPORELACAO_PRODUTO_RESTRITO, dsCausa);
	}
	
	public int countByExampleFullSQL(BaseDomain filter) throws SQLException {
    	return ProdutoPdbxDao.getInstance().countByExampleFullSQL(filter);
    }
    
}
