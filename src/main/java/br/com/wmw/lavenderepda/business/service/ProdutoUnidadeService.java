package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoUnidadePdbxDao;
import totalcross.util.Vector;

public class ProdutoUnidadeService extends CrudService {

    private static ProdutoUnidadeService instance;

    private ProdutoUnidadeService() {
        //--
    }

    public static ProdutoUnidadeService getInstance() {
        if (instance == null) {
            instance = new ProdutoUnidadeService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ProdutoUnidadePdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public Vector findAllByProduto(Produto produto) throws SQLException {
    	return findAllByProduto(null, produto, null);
    }

    public Vector findAllByProduto(ItemPedido itemPedido, ProdutoBase produto, String cdTabelaPreco) throws SQLException {
    	Vector produtoUnidadeList = new Vector();
    	ProdutoUnidade produtoUnidadeFilter = new ProdutoUnidade();
    	produtoUnidadeFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	produtoUnidadeFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	produtoUnidadeFilter.cdProduto = produto.cdProduto;
    	produtoUnidadeFilter.sortAtributte = ProdutoUnidade.DS_COLUNA_NUCONVERSAOUNIDADE;
    	produtoUnidadeFilter.sortAsc = ValueUtil.VALOR_SIM;
		if ((LavenderePdaConfig.usaGradeProduto1() || LavenderePdaConfig.usaGradeProduto4()) && itemPedido != null) {
    		produtoUnidadeFilter.cdItemGrade1 = itemPedido.cdItemGrade1;
    		produtoUnidadeList = findAllByExample(produtoUnidadeFilter);
    	}
    	if (ValueUtil.isEmpty(produtoUnidadeList)) {
    		produtoUnidadeFilter.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
    		produtoUnidadeList = findAllByExample(produtoUnidadeFilter);
    	}
    	//Se deve ocultar unidades alternativas que não tem preço
    	if (cdTabelaPreco != null && LavenderePdaConfig.usaApenasUnidadesComPreco) {
    		ItemTabelaPreco itemTabelaPrecoFilter = new ItemTabelaPreco();
    		itemTabelaPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		itemTabelaPrecoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    		itemTabelaPrecoFilter.cdTabelaPreco = cdTabelaPreco;
    		itemTabelaPrecoFilter.cdProduto = produto.cdProduto;
			Vector itemTabPrecoList = ItemTabelaPrecoService.getInstance().findAllByExample(itemTabelaPrecoFilter);
			produtoUnidadeList = mergeUnidadesComPreco(produtoUnidadeList, itemTabPrecoList);
    	}
    	//Se deve ocultar unidades alternativas que são restritas por tipo de pedido
    	if (itemPedido != null) {
    		for (int i = 0; i < produtoUnidadeList.size(); i++) {
    			ProdutoUnidade produtoUnidade = (ProdutoUnidade) produtoUnidadeList.items[i];
    			if (RestricaoVendaUnService.getInstance().isUnidadeRestrita(itemPedido, produtoUnidade.cdUnidade, itemPedido.pedido.cdTipoPedido)) {
    				produtoUnidadeList.removeElement(produtoUnidade);
    				i--;
    			}
    		}
    	}
		//Adiciona a unidade padrão do produto na lista
    	if (LavenderePdaConfig.usaRestricaoVendaProdutoPorUnidade) {
			if (itemPedido != null && ValueUtil.isNotEmpty(itemPedido.getProduto().cdUnidade) && RestricaoVendaUnService.getInstance().isUnidadeRestrita(itemPedido, produto.cdUnidade, itemPedido.pedido.cdTipoPedido)) {
				if (ValueUtil.isEmpty(produtoUnidadeList) && !isInsereMultiplosSemNegociacao(itemPedido.pedido)) {
					throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_UNIDADE_BLOQUEADA, itemPedido.getProdutoUnidade().toString()) , StringUtil.getStringValue(itemPedido.getProdutoUnidade().toString()));
				} else {
					return produtoUnidadeList;
				} 
			} else {
				addProdutoUnidadePadrao(itemPedido, produto, cdTabelaPreco, produtoUnidadeList);
			}
		} else {
			addProdutoUnidadePadrao(itemPedido, produto, cdTabelaPreco, produtoUnidadeList);
		}
    	return produtoUnidadeList;
    }
    
    protected boolean isInsereMultiplosSemNegociacao(Pedido pedido) throws SQLException {
		return pedido.isPermiteInserirMultiplosItensPorVezNoPedido() && (LavenderePdaConfig.isInsereQtdDescMultipla() || LavenderePdaConfig.isInsereSomenteQtdMultipla());
	}
    
    private void addProdutoUnidadePadrao(ItemPedido itemPedido, ProdutoBase produto, String cdTabelaPreco, Vector produtoUnidadeList) throws SQLException {
		produto.cdUnidade = ValueUtil.isEmpty(produto.cdProduto) && !itemPedido.pedido.isPedidoAberto() ? itemPedido.cdUnidade : produto.cdUnidade;
		if (cdTabelaPreco != null && LavenderePdaConfig.usaApenasUnidadesComPreco) {
			ItemTabelaPreco itemTabelaPrecoFilter = new ItemTabelaPreco();
			itemTabelaPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			itemTabelaPrecoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			itemTabelaPrecoFilter.cdTabelaPreco = cdTabelaPreco;
			itemTabelaPrecoFilter.cdProduto = produto.cdProduto;
			itemTabelaPrecoFilter.cdUnidade = produto.cdUnidade;
			if (ItemTabelaPrecoService.getInstance().countByExample(itemTabelaPrecoFilter) > 0) {
				ProdutoUnidade produtoUnidadePadrao = getProdutoUnidadePadrao(produto, true, itemPedido);
				if (isProdutoUnidadePadraoJaInserido(produtoUnidadeList, produtoUnidadePadrao)) {
					return;
				}
				produtoUnidadeList.addElement(produtoUnidadePadrao);
			}
		} else {
			ProdutoUnidade produtoUnidadePadrao = getProdutoUnidadePadrao(produto, true, itemPedido);
			if (isProdutoUnidadePadraoJaInserido(produtoUnidadeList, produtoUnidadePadrao)) {
				return;
			}
			produtoUnidadeList.addElement(produtoUnidadePadrao);
		}
	}

	public boolean isProdutoUnidadePadraoJaInserido(Vector produtoUnidadeList, ProdutoUnidade produtoUnidade) {
		int size = produtoUnidadeList.size();
		ProdutoUnidade produtoUnidadeAux;
		for (int i = 0; i < size; i++) {
			produtoUnidadeAux = (ProdutoUnidade) produtoUnidadeList.items[i];
			if (ValueUtil.valueEquals(produtoUnidade.cdUnidade, produtoUnidadeAux.cdUnidade)) {
				return true;
			}
		}
		return false;
	}

	private Vector mergeUnidadesComPreco(Vector produtoUnidadeList, Vector itemTabPrecoList) {
		Vector newProdutoUnidadeList = new Vector();
		if (produtoUnidadeList != null && itemTabPrecoList != null) {
			for (int i = 0; i < itemTabPrecoList.size(); i++) {
				ItemTabelaPreco itemTabelaPreco = (ItemTabelaPreco)itemTabPrecoList.items[i];
				for (int j = 0; j < produtoUnidadeList.size(); j++) {
					ProdutoUnidade produtoUnidade = (ProdutoUnidade)produtoUnidadeList.items[j];
					if (StringUtil.getStringValue(produtoUnidade.cdUnidade).equals(StringUtil.getStringValue(itemTabelaPreco.cdUnidade))) {
						newProdutoUnidadeList.addElement(produtoUnidade);
					}
				}
			}
		}
		return newProdutoUnidadeList;
	}

    public double[] getTamanhosEmbalagensByExample(BaseDomain domain) throws SQLException {
    	Vector qtEmbalagemList = ProdutoUnidadePdbxDao.getInstance().getQtEmbalagensByExample(domain);
    	
    	if (ValueUtil.isNotEmpty(qtEmbalagemList)) {
    		int size = qtEmbalagemList.size();
    		double[] arrayQtEmbalagens = new double[size];
    		for (int i = 0; i < size; i++) {
    			arrayQtEmbalagens[i] = ((ProdutoUnidade) qtEmbalagemList.items[i]).nuConversaoUnidade;
    		}
    		
    		return arrayQtEmbalagens;
    	}
    	
		return null;
    }

	public ProdutoUnidade getUnidadeAlternativaByItemPedido(ItemPedido itemPedido) throws SQLException {
		if (ValueUtil.isEmpty(itemPedido.cdUnidade) || itemPedido.getProduto() == null || (itemPedido.isCdUnidadeIgualCdUnidadeProduto() && !LavenderePdaConfig.usaDescPromocionalRegraInterpolacaoUnidadeProduto())) {
			return null;
		}
		ProdutoUnidade produtoUnidadeFilter = new ProdutoUnidade();
		produtoUnidadeFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoUnidadeFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		produtoUnidadeFilter.cdUnidade = (ValueUtil.isEmpty(itemPedido.cdUnidadeDescPromocionalFilter)) ? itemPedido.cdUnidade : itemPedido.cdUnidadeDescPromocionalFilter;
		produtoUnidadeFilter.cdProduto = itemPedido.cdProduto;
		if (LavenderePdaConfig.usaGradeProduto1() || LavenderePdaConfig.usaGradeProduto4()) {
			produtoUnidadeFilter.cdItemGrade1 = itemPedido.cdItemGrade1;
		} else {
			produtoUnidadeFilter.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		}
		ProdutoUnidade produtoUnidade = (ProdutoUnidade) ProdutoUnidadeService.getInstance().findByRowKey(produtoUnidadeFilter.getRowKey());
		if (produtoUnidade == null) {
			produtoUnidadeFilter.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
			produtoUnidade = (ProdutoUnidade) ProdutoUnidadeService.getInstance().findByRowKey(produtoUnidadeFilter.getRowKey());
		}
		if (produtoUnidade != null) {
			if (itemPedido.nuConversaoUnidadePu > 0) {
				produtoUnidade.nuConversaoUnidade = itemPedido.nuConversaoUnidadePu; 
			}
			if (ValueUtil.isNotEmpty(itemPedido.flDivideMultiplicaPu) && !ProdutoUnidade.FL_MULTIPLICA.equals(itemPedido.flDivideMultiplicaPu) && !LavenderePdaConfig.usaInterpolacaoPrecoProduto && !LavenderePdaConfig.usaDescPromocionalRegraInterpolacaoUnidadeProduto()) {
				produtoUnidade.flDivideMultiplica = itemPedido.flDivideMultiplicaPu;
			}
			if (itemPedido.vlIndiceFinanceiroPu != 0) {
				produtoUnidade.vlIndiceFinanceiro = itemPedido.vlIndiceFinanceiroPu;
			}
		}
		return produtoUnidade;
	}

	public double calculateUnidadeAlternativa(Pedido pedido, ProdutoUnidade produtoUnidade, ItemTabelaPreco itemTabelaPreco, double vlProduto, Produto produto) throws SQLException {
		if (LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco) {
			produtoUnidade.vlEmbElementar =  vlProduto;
			produtoUnidade.qtEmbElementar = itemTabelaPreco.getNuConversaoUnidade();
			produtoUnidade.vlIndiceFinanceiro = 1.0;
			return 0;
		} else {
			double vlItem;
			double nuConversaoUnidadesMedida = produto.nuConversaoUnidadesMedida;
			if (nuConversaoUnidadesMedida == 0) {
				nuConversaoUnidadesMedida = 1;
			}
			if (produtoUnidade.vlIndiceFinanceiro == 0) {
				produtoUnidade.vlIndiceFinanceiro = 1;
			}
			boolean usaUnidadeFracao = produto.nuFracao > 0 && LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto;
			int nuFracao = usaUnidadeFracao ? produto.nuFracao : 1;
			if (usaUnidadeFracao) {
				nuConversaoUnidadesMedida = nuFracao;
			}
			double nuConversaoUnidade = getNuConversaoUnidade(itemTabelaPreco, produtoUnidade);
			//--
			double qtEmbSelecionada = 0;
			if (produtoUnidade.isMultiplica()) {
				if (LavenderePdaConfig.aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa) {
					vlProduto = ValueUtil.round(vlProduto * produtoUnidade.vlIndiceFinanceiro);
				}
				vlItem = vlProduto * nuConversaoUnidade;
				//--
				qtEmbSelecionada =  nuConversaoUnidadesMedida * nuConversaoUnidade;
			} else {
				vlItem = vlProduto / nuConversaoUnidade;
				//--
				qtEmbSelecionada =  nuConversaoUnidadesMedida / nuConversaoUnidade;
			}
			boolean aplicaIndice = true;
			boolean isTabelaPromocao = ItemTabelaPrecoService.getInstance().isTabelaPrecoPromocional(produto.cdProduto, itemTabelaPreco.cdTabelaPreco);
			boolean isDescPromocional = pedido != null && DescPromocionalService.getInstance().isProdutoPossuiValorNoGrupoDescPromo(pedido, produto, itemTabelaPreco.cdTabelaPreco);
			if ((LavenderePdaConfig.isIgnoraIndiceFinanceiroUnidadeAlternativaProdutosPromocao() && isTabelaPromocao) 
					|| ((LavenderePdaConfig.isIgnoraIndiceFinanceiroUnidadeAlternativaProdutosPromocao() || LavenderePdaConfig.isIgnoraIndiceFinanceiroUnidadeAlternativaProdutosDescPromocional()) && isDescPromocional)) {
					aplicaIndice = false;
				}
			if (LavenderePdaConfig.aplicaIndicesNaUnidadePadraoParaUnidadeAlternativa) {
				aplicaIndice = false;
			}
			if (nuConversaoUnidadesMedida == 1 && !produtoUnidade.isMultiplica()) {
				produtoUnidade.vlEmbElementar =   ValueUtil.round((vlItem * (aplicaIndice ? produtoUnidade.vlIndiceFinanceiro : 1)) * (aplicaIndice ? produtoUnidade.vlIndiceFinanceiro : 1));
				produtoUnidade.qtEmbElementar = nuConversaoUnidade;
			} else {
				produtoUnidade.vlEmbElementar =  ValueUtil.round((vlItem / qtEmbSelecionada) * (aplicaIndice ? produtoUnidade.vlIndiceFinanceiro : 1));
				produtoUnidade.qtEmbElementar = ValueUtil.round(qtEmbSelecionada);
			}
			return vlItem * (aplicaIndice ? produtoUnidade.vlIndiceFinanceiro : 1);
		}
	}

	public ProdutoUnidade getProdutoUnidadePadrao(ProdutoBase produto, boolean validaProdutoSemUnidadePadrao, ItemPedido itemPedido) throws SQLException {
		validaUnidadePadraoProduto(produto.cdUnidade, validaProdutoSemUnidadePadrao);
		ProdutoUnidade produtoUnidade = new ProdutoUnidade();
    	produtoUnidade.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	produtoUnidade.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	produtoUnidade.cdProduto = produto.cdProduto;
		if ((LavenderePdaConfig.usaGradeProduto1() || LavenderePdaConfig.usaGradeProduto4()) && itemPedido != null) {
    		produtoUnidade.cdItemGrade1 = itemPedido.cdItemGrade1;
    	} else {
    		produtoUnidade.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
    	}
    	produtoUnidade.cdUnidade = produto.cdUnidade;
		produtoUnidade.unidade = produto.unidade == null ? UnidadeService.getInstance().findByCdUnidade(produto.cdUnidade, false) : produto.unidade;	
    	produtoUnidade.nuConversaoUnidade = 1;
    	produtoUnidade.flDivideMultiplica = ProdutoUnidade.FL_MULTIPLICA;
    	produtoUnidade.vlIndiceFinanceiro = 1;
    	if (LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto) {
    		produtoUnidade.nuFracao = produto.nuFracao;
    		produtoUnidade.unidadeFracao = UnidadeService.getInstance().findByCdUnidade(produto.cdUnidadeFracao, true);
    	}
    	//--
    	return produtoUnidade;
	}

	public void validaUnidadePadraoProduto(String cdUnidade, boolean validaProdutoSemUnidadePadrao) {
		if (LavenderePdaConfig.usaUnidadeAlternativa && ValueUtil.isEmpty(cdUnidade) && validaProdutoSemUnidadePadrao) {
    		throw new ValidationException(Messages.PRODUTO_SEM_UNIDADE_PADRAO);
    	}
	}

	public double getNuConversaoUnidade(ItemTabelaPreco itemTabelaPreco, ProdutoUnidade produtoUnidade) {
		double nuConversaoUnidade = 1;
		if (produtoUnidade != null) {
			nuConversaoUnidade = produtoUnidade.nuConversaoUnidade;
		}
		if (LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco) {
			nuConversaoUnidade = itemTabelaPreco.getNuConversaoUnidade();
		}
		return nuConversaoUnidade == 0 ? 1 : nuConversaoUnidade;
	}

	public ProdutoUnidade getProdutoUnidadeByCdBarras(ItemPedido itemPedido, String cdBarras) throws SQLException {
		ProdutoUnidade produtoUnidadeFilter = new ProdutoUnidade();
		produtoUnidadeFilter.cdEmpresa = itemPedido.cdEmpresa;
		produtoUnidadeFilter.cdRepresentante = itemPedido.cdRepresentante;
		produtoUnidadeFilter.nuCodigoBarras = cdBarras;
		Vector list = findAllByExample(produtoUnidadeFilter);
		if (ValueUtil.isNotEmpty(list)) {
			return (ProdutoUnidade) list.items[0];
		}
		return null;
	}
	
	public ProdutoUnidade getProdutoUnidadeByCdBarras(String cdBarras) throws SQLException {
		ProdutoUnidade produtoUnidadeFilter = new ProdutoUnidade();
		produtoUnidadeFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoUnidadeFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ProdutoUnidade.class);
		produtoUnidadeFilter.nuCodigoBarras = cdBarras;
		Vector list = findAllByExample(produtoUnidadeFilter);
		if (ValueUtil.isNotEmpty(list)) {
			return (ProdutoUnidade) list.items[0];
		}
		return null;
	}
	
	public Vector getProdutoUnidadeListSorted(ProdutoBase produto, Pedido pedido) throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.cdEmpresa = pedido.cdEmpresa;
		itemPedido.cdRepresentante = pedido.cdRepresentante;
		itemPedido.cdProduto = produto.cdProduto;
		itemPedido.pedido = pedido;
		itemPedido.cdTabelaPreco = pedido.cdTabelaPreco;
		Vector unidades = findAllByProduto(itemPedido, produto, pedido.cdTabelaPreco);
		return unidades;
	}
	
	public double getMultiploEspecialUnidadeAlternativa(ItemPedido itemPedido, ProdutoBase filter, String cdUnidade) throws SQLException {
		if (filter == null) return 0d;
		ProdutoUnidade prodUni = new ProdutoUnidade();
		prodUni.cdEmpresa = filter.cdEmpresa;
		prodUni.cdRepresentante = filter.cdRepresentante;
		prodUni.cdProduto = filter.cdProduto;
		prodUni.cdUnidade = cdUnidade;
		prodUni.cdItemGrade1 = ((LavenderePdaConfig.usaGradeProduto1() || LavenderePdaConfig.usaGradeProduto4()) && itemPedido != null) ? itemPedido.cdItemGrade1 : ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		prodUni = ProdutoUnidadePdbxDao.getInstance().getFirstProdutoUnidadeByExample(prodUni);
		return prodUni == null ? 0d : prodUni.nuMultiploEspecial;
	}

	public String getDescricaoByExample(ProdutoUnidade filter) throws SQLException {
		Vector unidadesEncontradas = findAllByExample(filter);
		ProdutoUnidade produtoUnidade = !ValueUtil.isEmpty(unidadesEncontradas) ? (ProdutoUnidade) unidadesEncontradas.items[0] : null;
		return produtoUnidade != null ? produtoUnidade.toString() : filter.cdUnidade;
	}

}