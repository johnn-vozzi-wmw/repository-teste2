package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.PeriodoNovidade;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConfig;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.RelNovSolAutorizacao;
import br.com.wmw.lavenderepda.business.domain.RelNovidadeCli;
import br.com.wmw.lavenderepda.business.domain.RelNovidadeNovoCli;
import br.com.wmw.lavenderepda.business.domain.RelNovidadePesquisaMercado;
import br.com.wmw.lavenderepda.business.domain.RelNovidadeProd;
import br.com.wmw.lavenderepda.business.domain.SolAutorizacao;
import br.com.wmw.lavenderepda.business.domain.TipoItemGrade;
import br.com.wmw.lavenderepda.business.domain.TipoNovidade;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.PesquisaMercadoConfigService;
import br.com.wmw.lavenderepda.business.service.ProdutoGradeService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.RelNovSolAutorizacaoService;
import br.com.wmw.lavenderepda.business.service.RelNovidadeCliService;
import br.com.wmw.lavenderepda.business.service.RelNovidadeNovoCliService;
import br.com.wmw.lavenderepda.business.service.RelNovidadePesquisaMercadoService;
import br.com.wmw.lavenderepda.business.service.RelNovidadeProdService;
import br.com.wmw.lavenderepda.business.service.TipoNovidadeService;
import br.com.wmw.lavenderepda.presentation.ui.combo.EntidadeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.PeriodoNovidadeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoNovidadeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;
import totalcross.util.IntHashtable;
import totalcross.util.Vector;

public class ListRelNovidadeForm extends LavendereCrudListForm {

	private LabelName lbTipoNovidade;
	private LabelName lbPeriodo;
	private LabelName lbEntidade;
	private PeriodoNovidadeComboBox cbPeriodo;
	private TipoNovidadeComboBox cbTipoNovidade;
	private EntidadeComboBox cbEntidadeComboBox;
	public CadProdutoMenuForm cadProdutoForm;

	private final int COL_SOL_AUTORIZACAO_FL_AUTORIZADO = 3;

	public ListRelNovidadeForm() throws SQLException {
		super(Messages.MENU_OPCAO_RELNOVIDADEPRODUTO);
		setBaseCrudCadForm(new CadRelNovidadeForm());
		if (!LavenderePdaConfig.apresentaNovidadesClienteRelatorioNovidadeProduto) {
			edFiltro.idAgrupador = Produto.APPOBJ_CAMPOS_FILTRO_PRODUTO;
		}
		lbPeriodo = new LabelName(Messages.VALORINDICADOR_PERIODO);
		cbPeriodo = new PeriodoNovidadeComboBox();
		lbTipoNovidade = new LabelName(Messages.RELNOVIDADEPRODUTO_LABEL_CDTIPONOVIDADE);
		cbTipoNovidade = new TipoNovidadeComboBox();
		lbEntidade = new LabelName(Messages.LABEL_ENTIDADE_COMBOBOX);
		cbEntidadeComboBox = new EntidadeComboBox();
		singleClickOn = true;
		constructorListContainer();
		getCrudService().clearCache();
	}

    private void constructorListContainer() {
    	configListContainer("DSPRODUTO");
    	listContainer = new GridListContainer(LavenderePdaConfig.mostraColunaMarcaNaListaNovidadesDeProdutos() ? 7 : 6 , 2, true);
    	if (LavenderePdaConfig.mostraColunaMarcaNaListaNovidadesDeProdutos()) {
    		listContainer.setColsSort(new String[][]{
    			{Messages.PRODUTO_LABEL_CODIGO, "CDPRODUTO"},
    			{Messages.DATA_LABEL_DATA, "DTEMISSAORELATORIO"},
    			{Messages.PRODUTO_LABEL_DSPRODUTO, "DSPRODUTO"},
    			{Messages.RELNOVIDADE_LABEL_DSNOVIDADE, "DSNOVIDADEPRODUTO"},
				{Messages.RELNOVIDADEPRODUTO_LAVEL_DSMARCA,"DSMARCA"}
        	});			
		} else {
	    	listContainer.setColsSort(new String[][]{
				{Messages.PRODUTO_LABEL_CODIGO, "CDPRODUTO"},
				{Messages.DATA_LABEL_DATA, "DTEMISSAORELATORIO"},
				{Messages.PRODUTO_LABEL_DSPRODUTO, "DSPRODUTO"},
				{Messages.RELNOVIDADE_LABEL_DSNOVIDADE, "DSNOVIDADEPRODUTO"}
	    	});
		}
    	listContainer.setColPosition(3, RIGHT);
    	listContainer.setColPosition(5, RIGHT);
    }

	//@Override
	protected CrudService getCrudService() throws SQLException {
		BaseDomain domain = getSelectedDomain2();
		if (domain != null) {
			if (domain instanceof RelNovidadeNovoCli) {
				return RelNovidadeNovoCliService.getInstance();
			} else if (domain instanceof RelNovidadeCli) {
				return RelNovidadeCliService.getInstance();
			} else if (domain instanceof RelNovidadePesquisaMercado) {
				return RelNovidadePesquisaMercadoService.getInstance();
			} else if (domain instanceof RelNovSolAutorizacao) {
				return RelNovSolAutorizacaoService.getInstance();
			}
		}
		return RelNovidadeProdService.getInstance();
	}

	//@Override
	protected TipoNovidadeService getTipoNovidadeService() {
		return TipoNovidadeService.getInstance();
	}

	protected BaseDomain getDomainFilter() {
    	return new RelNovidadeProd();
    }

	//@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		String dsFiltro = edFiltro.getValue();
		if (dsFiltro == null) {
			dsFiltro = "";
		}
		
		int periodoNovidade = LavenderePdaConfig.usaNovidadesRecentesNoRelNovidades ? PeriodoNovidade.PERIODO_NOVIDADE_CDTODOS : cbPeriodo.getValue();
		Vector relNovidadeProdList = (cbEntidadeComboBox.isFiltroPorProduto()) ? RelNovidadeProdService.getInstance().findRelNovidadeProd(dsFiltro, periodoNovidade) : new Vector();
		Vector relNovidadeCliList = (LavenderePdaConfig.apresentaNovidadesClienteRelatorioNovidadeProduto && cbEntidadeComboBox.isFiltroPorCliente()) ? RelNovidadeCliService.getInstance().findRelNovidadeCli(dsFiltro, periodoNovidade) : new Vector();
		Vector relNovidadeNovoCliList = ((LavenderePdaConfig.usaNovidadeNovoClienteNaoIntegrado || LavenderePdaConfig.isValidaCadastroDuasEtapas()) && cbEntidadeComboBox.isFiltroPorNovoCliente()) ? RelNovidadeNovoCliService.getInstance().findRelNovidadeNovoCli(dsFiltro, periodoNovidade) : new Vector();
		Vector relNovidadePesquisaMercadoList = LavenderePdaConfig.geraNovidadePesquisaMercado() && cbEntidadeComboBox.isFiltroPorPesquisaMercado() ? RelNovidadePesquisaMercadoService.getInstance().findRelNovidadePesquisaMercado(dsFiltro, periodoNovidade) : new Vector();
		Vector relNovidadeSolAutorizacaoList = LavenderePdaConfig.geraNovidadeAutorizacao && cbEntidadeComboBox.isFiltroPorSolAutorizacao() ? RelNovSolAutorizacaoService.getInstance().findRelNovSolAutorizacao(dsFiltro, periodoNovidade) : new Vector();
		atualizaComboTipoNovidade(relNovidadeProdList, relNovidadeCliList, relNovidadeNovoCliList, relNovidadePesquisaMercadoList, relNovidadeSolAutorizacaoList);
		
		relNovidadeProdList = RelNovidadeProdService.getInstance().findAllByExample(dsFiltro, cbTipoNovidade.getValue(), periodoNovidade);
		relNovidadeCliList = RelNovidadeCliService.getInstance().findAllByExample(dsFiltro, cbTipoNovidade.getValue(), periodoNovidade);
		relNovidadeNovoCliList = ((LavenderePdaConfig.usaNovidadeNovoClienteNaoIntegrado || LavenderePdaConfig.isValidaCadastroDuasEtapas())) ? RelNovidadeNovoCliService.getInstance().findAllByExample(dsFiltro, cbTipoNovidade.getValue(), periodoNovidade) : null;
		relNovidadePesquisaMercadoList = LavenderePdaConfig.geraNovidadePesquisaMercado() ? RelNovidadePesquisaMercadoService.getInstance().findAllByExample(dsFiltro, cbTipoNovidade.getValue(), periodoNovidade) : null;
		relNovidadeSolAutorizacaoList = LavenderePdaConfig.geraNovidadeAutorizacao ? RelNovSolAutorizacaoService.getInstance().findAllByExample(dsFiltro, cbTipoNovidade.getValue(), periodoNovidade) : null;
		Vector relNovidadesList = getRelNovidadesList(relNovidadeProdList, relNovidadeCliList, relNovidadeNovoCliList, relNovidadePesquisaMercadoList, relNovidadeSolAutorizacaoList);
		ordenaLista(domain, relNovidadesList);
		return relNovidadesList;
	}

	private Vector getRelNovidadesList(Vector relNovidadeProdList, Vector relNovidadeCliList, Vector relNovidadeNovoCliList, Vector relNovidadePesquisaMercadoList, Vector relNovidadeSolAutorizacaoList) {
		Vector relNovidadesList = new Vector();
		if (ValueUtil.valueEquals(cbEntidadeComboBox.getValue(), Produto.APPOBJ_CAMPOS_FILTRO_PRODUTO)) {
			relNovidadesList.addElementsNotNull(relNovidadeProdList.items);
		} else if (ValueUtil.valueEquals(cbEntidadeComboBox.getValue(), Cliente.APPOBJ_CAMPOS_FILTRO_CLIENTE)) {
			relNovidadesList.addElementsNotNull(relNovidadeCliList.items);
		} else if (ValueUtil.valueEquals(cbEntidadeComboBox.getValue(), NovoCliente.APPOBJ_CAMPOS_FILTRO_NOVO_CLIENTE) && (LavenderePdaConfig.usaNovidadeNovoClienteNaoIntegrado || LavenderePdaConfig.isValidaCadastroDuasEtapas())) {
			relNovidadesList.addElementsNotNull(relNovidadeNovoCliList.items);
		} else if (ValueUtil.valueEquals(cbEntidadeComboBox.getValue(), PesquisaMercadoConfig.APPOBJ_CAMPOS_FILTRO_PESQUISA_MERCADO) && LavenderePdaConfig.geraNovidadePesquisaMercado()) {
			relNovidadesList.addElementsNotNull(relNovidadePesquisaMercadoList.items);
		} else if (ValueUtil.valueEquals(cbEntidadeComboBox.getValue(), SolAutorizacao.APPOBJ_CAMPOS_FILTRO_SOL_AUTORIZACAO) && LavenderePdaConfig.geraNovidadeAutorizacao) {
			relNovidadesList.addElementsNotNull(relNovidadeSolAutorizacaoList.items);
		} else {
			relNovidadesList.addElementsNotNull(relNovidadeProdList.items);
			relNovidadesList.addElementsNotNull(relNovidadeCliList.items);
			if (LavenderePdaConfig.usaNovidadeNovoClienteNaoIntegrado || LavenderePdaConfig.isValidaCadastroDuasEtapas()) {
				relNovidadesList.addElementsNotNull(relNovidadeNovoCliList.items);
			}
			if (LavenderePdaConfig.geraNovidadePesquisaMercado()) {
				relNovidadesList.addElementsNotNull(relNovidadePesquisaMercadoList.items);
			}
			if (LavenderePdaConfig.geraNovidadeAutorizacao) {
				relNovidadesList.addElementsNotNull(relNovidadeSolAutorizacaoList.items);
			}
		}
		return relNovidadesList;
	}
	
	private void ordenaLista(BaseDomain domain, Vector relNovidadesList) {
		RelNovidadeProd.sortAttr = domain.sortAtributte;
		if (RelNovidadeProd.SORT_COLUMN_CDPRODUTO.equals(domain.sortAtributte) || RelNovidadeProd.SORT_COLUMN_DTEMISSAORELATORIO.equals(domain.sortAtributte)) {
			SortUtil.qsortInt(relNovidadesList.items, 0, relNovidadesList.size() - 1, true);
		} else {
			SortUtil.qsortString(relNovidadesList.items, 0, relNovidadesList.size() - 1, true);
		}
		if (domain.sortAsc.startsWith(ValueUtil.VALOR_NAO)) {
			relNovidadesList.reverse();
		}
	}

	private void atualizaComboTipoNovidade(Vector relNovidadeProdList, Vector relNovidadeCliList, Vector relNovidadeNovoCliList, Vector relNovidadePesquisaMercadoList, Vector relNovidadeSolAutorizacaoList) throws SQLException {
		IntHashtable tipoHashtable = new IntHashtable(5);
		if (ValueUtil.valueEquals(cbEntidadeComboBox.getValue(), Produto.APPOBJ_CAMPOS_FILTRO_PRODUTO)) {
			populateRelNovidadeProd(relNovidadeProdList, tipoHashtable);
		} else if (ValueUtil.valueEquals(cbEntidadeComboBox.getValue(), Cliente.APPOBJ_CAMPOS_FILTRO_CLIENTE)) {
			populateRelNovidadeCli(relNovidadeCliList, tipoHashtable);
		} else if (ValueUtil.valueEquals(cbEntidadeComboBox.getValue(), NovoCliente.APPOBJ_CAMPOS_FILTRO_NOVO_CLIENTE)) {
			populateRelNovidadeNovoCli(relNovidadeNovoCliList, tipoHashtable);
		} else if (ValueUtil.valueEquals(cbEntidadeComboBox.getValue(), PesquisaMercadoConfig.APPOBJ_CAMPOS_FILTRO_PESQUISA_MERCADO)) {
			populateRelNovidadePesqMercado(relNovidadePesquisaMercadoList, tipoHashtable);
		} else if (ValueUtil.valueEquals(cbEntidadeComboBox.getValue(), SolAutorizacao.APPOBJ_CAMPOS_FILTRO_SOL_AUTORIZACAO)) {
			populateRelNovidadeSolAutorizacao(relNovidadeSolAutorizacaoList, tipoHashtable);
		} else {
			populateRelNovidadeProd(relNovidadeProdList, tipoHashtable);
			populateRelNovidadeCli(relNovidadeCliList, tipoHashtable);
			populateRelNovidadeNovoCli(relNovidadeNovoCliList, tipoHashtable);
			populateRelNovidadePesqMercado(relNovidadePesquisaMercadoList, tipoHashtable);
			populateRelNovidadeSolAutorizacao(relNovidadeSolAutorizacaoList, tipoHashtable);
		}
		cbTipoNovidade.addQtItensTipo(tipoHashtable);
	}

	private void populateRelNovidadeSolAutorizacao(Vector relNovidadeSolAutorizacaoList, IntHashtable tipoHashtable) {
		int size = relNovidadeSolAutorizacaoList.size();
		for (int i = 0; i < size; i++) {
			RelNovSolAutorizacao relNovSolAutorizacao = (RelNovSolAutorizacao) relNovidadeSolAutorizacaoList.items[i];
			tipoHashtable.put(relNovSolAutorizacao.cdTipoNovidade, relNovSolAutorizacao.qtRegistrosTipoNovidade);
		}
	}

	private void populateRelNovidadePesqMercado(Vector relNovidadePesquisaMercadoList, IntHashtable tipoHashtable) {
		int size = relNovidadePesquisaMercadoList.size();
		for (int i = 0; i < size; i++) {
			RelNovidadePesquisaMercado relNovidadePesquisaMercado = (RelNovidadePesquisaMercado) relNovidadePesquisaMercadoList.items[i];
			tipoHashtable.put(relNovidadePesquisaMercado.cdTipoNovidade, relNovidadePesquisaMercado.qtRegistrosTipoNovidade);
		}
	}

	private void populateRelNovidadeNovoCli(Vector relNovidadeNovoCliList, IntHashtable tipoHashtable) {
		int size = relNovidadeNovoCliList.size();
		for (int i = 0; i < size; i++) {
			RelNovidadeNovoCli relNovidadeNovoCli = (RelNovidadeNovoCli) relNovidadeNovoCliList.items[i];
			tipoHashtable.put(relNovidadeNovoCli.cdTipoNovidade, relNovidadeNovoCli.qtRegistrosTipoNovidade);
		}
	}

	private void populateRelNovidadeCli(Vector relNovidadeCliList, IntHashtable tipoHashtable) {
		int size = relNovidadeCliList.size();
		for (int i = 0; i < size; i++) {
			RelNovidadeCli relNovidadeCli = (RelNovidadeCli) relNovidadeCliList.items[i];
			tipoHashtable.put(relNovidadeCli.cdTipoNovidade, relNovidadeCli.qtRegistrosTipoNovidade);
		}
	}

	private void populateRelNovidadeProd(Vector relNovidadeProdList, IntHashtable tipoHashtable) {
		int size = relNovidadeProdList.size();
		for (int i = 0; i < size; i++) {
			RelNovidadeProd relNovidadeProd = (RelNovidadeProd) relNovidadeProdList.items[i];
			tipoHashtable.put(relNovidadeProd.cdTipoNovidade, relNovidadeProd.qtRegistrosTipoNovidade);
		}
	}

	//@Override
	protected String[] getItem(Object domain) throws SQLException {
		if (!(domain instanceof RelNovSolAutorizacao)) {
			listContainer.setColColor(COL_SOL_AUTORIZACAO_FL_AUTORIZADO, ColorUtil.labelNameForeColor);
		}
		if (domain instanceof RelNovidadeProd) {
			RelNovidadeProd relNovidadeProd = (RelNovidadeProd) domain;
			relNovidadeProd.dsProduto = ProdutoService.getInstance().getDescricaoProdutoComReferencia(getProdutoForReferenciaFilter(relNovidadeProd));
			return (String[]) getRelNovidadeProdItem(relNovidadeProd).toObjectArray();
		} else if (domain instanceof RelNovidadeCli) {
			return (String[]) getRelNovidadeCliItem(domain).toObjectArray();
		} else if (domain instanceof RelNovidadePesquisaMercado) {
			return getRelNovidadePesquisaMercadoItem(domain);
		} else if (domain instanceof RelNovSolAutorizacao) {
			return getRelNovidadeSolAutorizacaoItem(domain);
		} else {
			return (String[]) getRelNovidadeNovoCliItem(domain).toObjectArray();
		}
	}

	private Produto getProdutoForReferenciaFilter(RelNovidadeProd relNovidadeProd) {
		Produto produto = new Produto();
		produto.cdProduto = relNovidadeProd.cdProduto;
		produto.dsProduto = relNovidadeProd.dsProduto;
		produto.dsReferencia = ValueUtil.isEmpty(relNovidadeProd.dsReferenciaProduto) ? "" : relNovidadeProd.dsReferenciaProduto;
		return produto;
	}

	private Vector getRelNovidadeItem(String codigo, String descricao, String cdTipoNovidade, String nmEntidade, String dsNovidade, Date dtEmissaoRelatorio, String dsMarca, RelNovidadeProd relNovidadeProd) throws SQLException {
		Vector itens = new Vector();
		itens.addElement(StringUtil.getStringValue(codigo));
		itens.addElement(" - " + StringUtil.getStringValue(descricao));
		itens.addElement(StringUtil.getStringValue(getTipoNovidadeService().getDsTipoNovidade(cdTipoNovidade)));
		itens.addElement(StringUtil.getStringValue(nmEntidade));
		if (LavenderePdaConfig.ocultaQtdEstoqueNovidade && relNovidadeProd != null  && relNovidadeProd.possuiOrigemEstoque() && relNovidadeProd.isNovidadeEntradaEstoque()) {
			itens.addElement(Messages.NOVIDADE_LABEL_ORIGEMESTOQUE + StringUtil.getStringValue(EstoqueService.getInstance().getDsOrigemEstoque(relNovidadeProd.flOrigemEstoque)));
		} else {
			itens.addElement(StringUtil.getStringAbreviada(getDsNovidadeFormatada(dsNovidade, relNovidadeProd), this.width / 2));
		}
		itens.addElement(StringUtil.getStringValue(dtEmissaoRelatorio));
		if (LavenderePdaConfig.mostraColunaMarcaNaListaNovidadesDeProdutos()) {
			itens.addElement(StringUtil.getStringValue(dsMarca));
		}
		return itens;
	}

	private String getDsNovidadeFormatada(String dsNovidade, RelNovidadeProd relNovidadeProd) {
		if (relNovidadeProd != null && relNovidadeProd.isNovidadeEntradaEstoque()) {
			try {
				double qtEstoque = (int) Double.parseDouble(ValueUtil.removeCurrency(dsNovidade));
				return LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO) ? StringUtil.getStringValueToInterface((int) qtEstoque) : StringUtil.getStringValueToInterface(qtEstoque);
			} catch (Throwable e) {
				return dsNovidade;
			}
		}
		return dsNovidade;
	}
	
	private String[] getRelNovidadeSolAutorizacaoItem(Object domain) throws SQLException {
		RelNovSolAutorizacao relNovSolAutorizacao =  (RelNovSolAutorizacao) domain;
		String[] item = new String[LavenderePdaConfig.mostraColunaMarcaNaListaNovidadesDeProdutos() ? 7 : 6];
		SolAutorizacao autorizacao = new SolAutorizacao(relNovSolAutorizacao.flAutorizado);
		item[0] = relNovSolAutorizacao.cdProduto;
		item[1] = " - " + relNovSolAutorizacao.dsProduto;
		item[2] = TipoNovidadeService.getInstance().getDsTipoNovidade(TipoNovidade.TIPONOVIDADESOLAUTORIZACAO_RESPOSTA_AUTORIZACAO);
		item[3] = autorizacao.getStatus();
		item[4] = MessageUtil.getMessage(Messages.REL_NOV_SOL_AUTORIZACAO_PEDIDO_LABEL, relNovSolAutorizacao.nuPedido);
		item[5] = MessageUtil.getMessage(Messages.REL_NOV_SOL_AUTORIZACAO_CLIENTE_LABEL, ClienteService.getInstance().getCliente(relNovSolAutorizacao.cdEmpresa, relNovSolAutorizacao.cdRepresentante, relNovSolAutorizacao.cdCliente).toString());
		if (LavenderePdaConfig.mostraColunaMarcaNaListaNovidadesDeProdutos()) {
			item[6] = ValueUtil.VALOR_NI;
		}
		if (autorizacao.isAutorizado()) {
			listContainer.setColColor(COL_SOL_AUTORIZACAO_FL_AUTORIZADO, ColorUtil.softGreen);
		} else {
			listContainer.setColColor(COL_SOL_AUTORIZACAO_FL_AUTORIZADO, ColorUtil.softRed);
		}
		return item;
	}

	private String[] getRelNovidadePesquisaMercadoItem(Object domain) throws SQLException {
		RelNovidadePesquisaMercado relNovidadePesquisaMercado = (RelNovidadePesquisaMercado) domain;
		PesquisaMercadoConfig pesquisaMercadoConfig = RelNovidadePesquisaMercadoService.getInstance().getConfigWithQuantidades(relNovidadePesquisaMercado);
		String[] item = new String[LavenderePdaConfig.mostraColunaMarcaNaListaNovidadesDeProdutos() ? 7 : 6];
		return PesquisaMercadoConfigService.getInstance().getListFormItem(pesquisaMercadoConfig, item);
	}

	private Vector getRelNovidadeNovoCliItem(Object domain) throws SQLException {
		RelNovidadeNovoCli relNovidadeNovoCliente = (RelNovidadeNovoCli) domain;
		String dsNovidade = relNovidadeNovoCliente.dsMensagem != null && !TipoNovidade.TIPONOVIDADENOVOCLIENTE_SEGUNDA_ETAPA.equals(relNovidadeNovoCliente.cdTipoNovidade) ? relNovidadeNovoCliente.dsMensagem : "";
		return getRelNovidadeItem(relNovidadeNovoCliente.cdNovoCliente, relNovidadeNovoCliente.nmRazaoSocial, relNovidadeNovoCliente.cdTipoNovidade, NovoCliente.class.getSimpleName(), dsNovidade, relNovidadeNovoCliente.dtGeracao, "", null);
	}
	
	private Vector getRelNovidadeCliItem(Object domain) throws SQLException {
		RelNovidadeCli relNovidadeCliente = (RelNovidadeCli) domain;
		String dsNovidade = relNovidadeCliente.dsNovidadeCliente != null ? relNovidadeCliente.dsNovidadeCliente : "";
		return getRelNovidadeItem(relNovidadeCliente.cdCliente, relNovidadeCliente.nmRazaoSocial, relNovidadeCliente.cdTipoNovidade, Cliente.class.getSimpleName(), dsNovidade, relNovidadeCliente.dtEmissaoRelatorio, "", null);
	}

	private Vector getRelNovidadeProdItem(Object domain) throws SQLException {
		RelNovidadeProd relNovidadeProduto = (RelNovidadeProd) domain;
		String nmEntidade = LavenderePdaConfig.apresentaNovidadesClienteRelatorioNovidadeProduto ? Produto.class.getSimpleName() : "";
		String dsNovidade = formataNovidade(relNovidadeProduto.cdTipoNovidade, relNovidadeProduto.dsNovidadeProduto != null ? relNovidadeProduto.dsNovidadeProduto : "", relNovidadeProduto.vlPrecoAntigo);
		String dsMarca = LavenderePdaConfig.mostraColunaMarcaNaListaNovidadesDeProdutos() ? relNovidadeProduto.dsMarcaProduto : "";
		return getRelNovidadeItem(relNovidadeProduto.cdProduto, getDsProduto(relNovidadeProduto), relNovidadeProduto.cdTipoNovidade, nmEntidade, dsNovidade, relNovidadeProduto.dtEmissaoRelatorio, dsMarca, relNovidadeProduto);
	}

	private Vector addSubListRelNovidadeProd(RelNovidadeProd relNovidadeProd) throws SQLException {
		Vector vector = new Vector();
		addSubListGradeItem(relNovidadeProd, vector);
		
		if (relNovidadeProd.mostraValorDaUnidadePrecoPorEmbalagemNovidade()) {
			final String relNovidadeProdPreco = RelNovidadeProdService.getInstance().getPrecoInterfaceValue(relNovidadeProd);
			vector.addElement(MessageUtil.getMessage(Messages.PRODUTO_LABEL_PRECO_UNID_RS, relNovidadeProdPreco));
			vector.addElement(ValueUtil.VALOR_NI);
		}
		if (relNovidadeProd.possuiTabelaPreco()) {
			String dsTabelaPreco = ValueUtil.isNotEmpty(relNovidadeProd.tabelaPreco.dsTabelaPreco) ? relNovidadeProd.tabelaPreco.toString() : relNovidadeProd.cdTabelaPreco;
			vector.addElement(Messages.NOVIDADE_LABEL_TABELAPRECO + StringUtil.getStringValue(dsTabelaPreco));
			vector.addElement(ValueUtil.VALOR_NI);
		}
		if (relNovidadeProd.possuiUF()) {
			vector.addElement(Messages.NOVIDADE_LABEL_UF + StringUtil.getStringValue(relNovidadeProd.cdUf));
		}
		if (relNovidadeProd.possuiUnidade()) {
			vector.addElement(Messages.NOVIDADE_LABEL_UNID + StringUtil.getStringValue(relNovidadeProd.cdUnidade));
		}
		if (relNovidadeProd.possuiQuantidade()) {
			vector.addElement(Messages.NOVIDADE_LABEL_QTD + StringUtil.getStringValueToInterface(relNovidadeProd.qtItem));
		}
		if (relNovidadeProd.possuiPrazoPagto()) {
			vector.addElement(Messages.NOVIDADE_LABEL_PRAZO + StringUtil.getStringValueToInterface(relNovidadeProd.cdPrazoPagtoPreco));
		}
		if (relNovidadeProd.possuiLocalEstoque()) {
			String dsLocalEstoque = ValueUtil.isNotEmpty(relNovidadeProd.localEstoque.dsLocalEstoque) ? relNovidadeProd.localEstoque.toString() : relNovidadeProd.cdLocalEstoque;
			vector.addElement(Messages.NOVIDADE_LABEL_LOCALESTOQUE + StringUtil.getStringValue(dsLocalEstoque));
		}
		if (relNovidadeProd.possuiOrigemEstoque() && !LavenderePdaConfig.ocultaQtdEstoqueNovidade && relNovidadeProd.isNovidadeEntradaEstoque()) {
			vector.addElement(Messages.NOVIDADE_LABEL_ORIGEMESTOQUE + StringUtil.getStringValue(EstoqueService.getInstance().getDsOrigemEstoque(relNovidadeProd.flOrigemEstoque)));
		}
		return vector;
	}
	
	private void addSubListGradeItem(RelNovidadeProd relNovidadeProd, Vector vector) {
		if (!LavenderePdaConfig.isConfigGradeProduto()) return;
		StringBuffer valorGrade = new StringBuffer();
		StringBuffer dsTipoItemGrade = new StringBuffer();
		String dsTipoItemGradeStr;
		if (relNovidadeProd.possuiGrade1()) {
			String dsGrade = ValueUtil.isNotEmpty(relNovidadeProd.itemGrade1.dsItemGrade) ? relNovidadeProd.itemGrade1.toString() : relNovidadeProd.cdItemGrade1;
			dsTipoItemGradeStr = getDsTipoItemGrade(dsTipoItemGrade, relNovidadeProd.tipoItemGrade1.dsTipoItemGrade, relNovidadeProd.tipoItemGrade1, Messages.NOVIDADE_LABEL_ITEMGRADE1);
			valorGrade.append(dsTipoItemGradeStr).append(StringUtil.getStringValue(dsGrade));
		}
		if (relNovidadeProd.possuiGrade2()) {
			String dsGrade = ValueUtil.isNotEmpty(relNovidadeProd.itemGrade2.dsItemGrade) ? relNovidadeProd.itemGrade2.toString() : relNovidadeProd.cdItemGrade2;
			dsTipoItemGradeStr = getDsTipoItemGrade(dsTipoItemGrade, relNovidadeProd.tipoItemGrade2.dsTipoItemGrade, relNovidadeProd.tipoItemGrade2, Messages.NOVIDADE_LABEL_ITEMGRADE1);
			valorGrade.append(Messages.NOVIDADE_SEPARADOR_GRADE).append(dsTipoItemGradeStr).append(StringUtil.getStringValue(dsGrade));
		}
		if (relNovidadeProd.possuiGrade3()) {
			String dsGrade = ValueUtil.isNotEmpty(relNovidadeProd.itemGrade3.dsItemGrade) ? relNovidadeProd.itemGrade3.toString() : relNovidadeProd.cdItemGrade3;
			dsTipoItemGradeStr = getDsTipoItemGrade(dsTipoItemGrade, relNovidadeProd.tipoItemGrade3.dsTipoItemGrade, relNovidadeProd.tipoItemGrade3, Messages.NOVIDADE_LABEL_ITEMGRADE1);
			valorGrade.append(Messages.NOVIDADE_SEPARADOR_GRADE).append(dsTipoItemGradeStr).append(StringUtil.getStringValue(dsGrade));
		}
		if (relNovidadeProd.possuiGrades()) {
			vector.addElement(valorGrade.toString());
			vector.addElement(ValueUtil.VALOR_NI);
		}
	}

	private String getDsTipoItemGrade(StringBuffer itemGrade, String dsTipoItemGrade, TipoItemGrade tipoItemGrade, String valorPadrao) {
		itemGrade.setLength(0);
		return ValueUtil.isNotEmpty(dsTipoItemGrade) ? itemGrade.append(tipoItemGrade.toString()).append(": ").toString() : valorPadrao;
	}

	@Override
	protected void setPropertiesInRowList(Item containerItem, BaseDomain domain) throws SQLException {
		super.setPropertiesInRowList(containerItem, domain);
		if (domain instanceof RelNovidadeProd) {
			String[] itens = new String[] {"", ""};
			int pos = 1;
			Vector allItens = addSubListRelNovidadeProd((RelNovidadeProd) domain);
			int size = allItens.size();
			for (int i = 0; i < size; i ++) {
				itens[pos-1] = (String) allItens.items[i];
				if (pos == 2) {
					containerItem.addSublistItem(itens);
					pos = 1;
					itens = new String[] {"", ""};
				} else {
					pos++;
				}
			}
			if (pos != 1) {
				containerItem.addSublistItem(itens);
			}
		}
	}

	private String formataNovidade(String cdTipoNovidade, String dsNovidade, double vlPrecoAntigo) {
		try {
			if (!LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
				String dsNovidadeFormatada = formataDsNovidade(dsNovidade);
				String vlPrecoAntigoFormatado = formataVlPrecoAntigo(vlPrecoAntigo);
				if (ValueUtil.valueEquals(TipoNovidade.TIPONOVIDADEPRODUTO_AUMENTO_PRECO, cdTipoNovidade)) {
					return MessageUtil.getMessage(Messages.NOVIDADE_ALTERACAO_PRECO_AUMENTO, new String[] {vlPrecoAntigoFormatado, dsNovidadeFormatada});
				} else if (ValueUtil.valueEquals(TipoNovidade.TIPONOVIDADEPRODUTO_QUEDA_PRECO, cdTipoNovidade)) {
					return MessageUtil.getMessage(Messages.NOVIDADE_ALTERACAO_PRECO_QUEDA, new String[] {vlPrecoAntigoFormatado, dsNovidadeFormatada});
				}
			}
			return StringUtil.getStringValueToInterface(Integer.parseInt(dsNovidade));
		} catch (Throwable e) {
			return dsNovidade;
		}
	}

	private String formataVlPrecoAntigo(double vlPrecoAntigo) throws SQLException {
		if (LavenderePdaConfig.realizaCalculoIndicesPrecoProdutoListaAdiconarItensPedido && LavenderePdaConfig.usaIndiceFinanceiroSupRep) {
			vlPrecoAntigo = ItemTabelaPrecoService.getInstance().aplicaIndiceFinanceiroSupRep(vlPrecoAntigo);
		}
		return StringUtil.getStringValueToInterface(vlPrecoAntigo);
	}

	private String formataDsNovidade(String dsNovidade) {
		try {
			if (LavenderePdaConfig.realizaCalculoIndicesPrecoProdutoListaAdiconarItensPedido && LavenderePdaConfig.usaIndiceFinanceiroSupRep) {
				String dsVlNovidade = dsNovidade.replaceAll("[^\\d.,]", "");
				double vlNovidade = Double.parseDouble(ValueUtil.removeCurrency(dsVlNovidade));
				vlNovidade = ItemTabelaPrecoService.getInstance().aplicaIndiceFinanceiroSupRep(vlNovidade);
				return Messages.MOEDA + " " + StringUtil.getStringValueToInterface(vlNovidade);
			}
		} catch (Throwable e) {
			return dsNovidade;
		}
		return dsNovidade;
	}

	private String getDsProduto(RelNovidadeProd relNovidadeProduto) throws SQLException {
		if (relNovidadeProduto.cdProduto.equalsIgnoreCase(relNovidadeProduto.dsProduto)) {
			return ProdutoService.getInstance().getDsProduto(relNovidadeProduto.cdProduto);
		}
		return relNovidadeProduto.dsProduto;
	}

    //@Override
    protected String getToolTip(BaseDomain domain) throws SQLException {
    	if (domain instanceof RelNovidadeProd) {
    		return StringUtil.getStringValue(((RelNovidadeProd) domain).dsProduto);
		} else if (domain instanceof RelNovidadeCli) {
			return StringUtil.getStringValue(((RelNovidadeCli) domain).nmRazaoSocial);
		} else if (domain instanceof RelNovidadeNovoCli) {
			return StringUtil.getStringValue(((RelNovidadeNovoCli) domain).nmRazaoSocial);
		} else if (domain instanceof RelNovidadePesquisaMercado) {
    		return StringUtil.getStringValue(((RelNovidadePesquisaMercado) domain).dsNovidadePesquisa);
    	} else if (domain instanceof RelNovSolAutorizacao) {
    		return domain.toString();
	    } else {
    		return "";
	    }

    }

    public void visibleState() {
    	super.visibleState();
    	barBottomContainer.setVisible(false);
    }

	//@Override
	protected void onFormStart() throws SQLException {
		int yComponents = getTop() + HEIGHT_GAP;
		if (!LavenderePdaConfig.usaNovidadesRecentesNoRelNovidades) {
			UiUtil.add(this, lbPeriodo, cbPeriodo, getLeft(), yComponents);
			yComponents = AFTER + HEIGHT_GAP;
		}
		if (LavenderePdaConfig.apresentaNovidadesClienteRelatorioNovidadeProduto) {
			UiUtil.add(this, lbEntidade, cbEntidadeComboBox, getLeft(), yComponents);
			yComponents = AFTER + HEIGHT_GAP;
		}
		UiUtil.add(this, lbTipoNovidade, cbTipoNovidade, getLeft(), yComponents);
		UiUtil.add(this, edFiltro, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
	}

	public void loadDefaultFilters() throws SQLException {
		edFiltro.setText("");
		cbTipoNovidade.setSelectedIndex(0);
		cbPeriodo.setValue(PeriodoNovidade.PERIODO_NOVIDADE_CDHOJE);
		cbEntidadeComboBox.setSelectedIndex(0);
	}

	//@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == cbTipoNovidade || event.target == cbPeriodo) {
					filtrarClick();
				} else if (event.target == cbEntidadeComboBox) {
					cbcbEntidadeComboBoxChange();
				}
				break;
			}
		}
	}
	
	private void cbcbEntidadeComboBoxChange() throws SQLException {
		edFiltro.idAgrupador = cbEntidadeComboBox.getValue();
		filtrarClick();
	}
	
	protected void filtrarClick() throws SQLException {
		super.filtrarClick();
		list();
	}
	
	@Override
	public void onFormClose() throws SQLException {
		super.onFormClose();
		getCrudService().clearCache();
	}
	
	@Override
	protected BaseDomain getDomain(BaseDomain domain) {
		return domain;
	}
	
	@Override
	public void detalhesClick() throws SQLException {
		BaseDomain selectedDomain = getSelectedDomain();
		if (selectedDomain instanceof RelNovidadeProd) {
			RelNovidadeProd domain = (RelNovidadeProd) selectedDomain;
			if (LavenderePdaConfig.isConfigGradeProduto() && TipoNovidade.TIPONOVIDADEPRODUTO_ENTRADA_ESTOQUE.equalsIgnoreCase(domain.cdTipoNovidade)) {
				Produto produto = ProdutoService.getInstance().getProduto(RelNovidadeProdService.getInstance().getCodigoProdutoNaListaDeNovidade(getSelectedRowKey()));
				if (produto == null || ValueUtil.isEmpty(produto.cdProduto)) {
					UiUtil.showWarnMessage(Messages.RELNOVIDADEPROD_PRODUTO_INEXISTENTE, UiUtil.DEFAULT_MESSAGETIME_LONG);
				} else {
					if (ProdutoGradeService.getInstance().isProdutoPossuiGrade(produto)) {
						String dsFiltro = edFiltro.getValue();
						if (dsFiltro == null) {
							dsFiltro = "";
						}
						int periodoNovidade = LavenderePdaConfig.usaNovidadesRecentesNoRelNovidades ? PeriodoNovidade.PERIODO_NOVIDADE_CDTODOS : cbPeriodo.getValue();
						RelNovidadeProd relNovidadeProdFilter = RelNovidadeProdService.getInstance().getFiltro(dsFiltro, cbTipoNovidade.getValue(), periodoNovidade);
						relNovidadeProdFilter.addWhereTabPreco = false;
						ListEstoqueGradeWindow window = new ListEstoqueGradeWindow(produto, null, null, 0, new Vector(), false, relNovidadeProdFilter, null);
						window.popup();
						if (window.showMenuProduto) {
							showCadProdutoMenu(produto);
						}
					} else if (LavenderePdaConfig.permiteAcessoAoMenuProdutoAtravesRelNovidade) {
						showCadProdutoMenu(produto);
					}
				}
			} else if (LavenderePdaConfig.permiteAcessoAoMenuProdutoAtravesRelNovidade) {
				String codigoProduto = RelNovidadeProdService.getInstance().getCodigoProdutoNaListaDeNovidade(getSelectedRowKey());
				Produto produto = ProdutoService.getInstance().getProduto(codigoProduto);
				if (produto == null || ValueUtil.isEmpty(produto.cdProduto)) {
					UiUtil.showWarnMessage(Messages.RELNOVIDADEPROD_PRODUTO_INEXISTENTE, UiUtil.DEFAULT_MESSAGETIME_LONG);
				} else {
					showCadProdutoMenu(produto);
				}
			} 
		} else if (!(selectedDomain instanceof RelNovSolAutorizacao)) {
			super.detalhesClick();
		}
	}
	
	private void showCadProdutoMenu(Produto produto) throws SQLException {
		cadProdutoForm = new CadProdutoMenuForm();
		cadProdutoForm.setEnabled(false);
		cadProdutoForm.edit(produto);
		cadProdutoForm.relNovidadeProduto = true;
		show(cadProdutoForm);
	}
	
}
