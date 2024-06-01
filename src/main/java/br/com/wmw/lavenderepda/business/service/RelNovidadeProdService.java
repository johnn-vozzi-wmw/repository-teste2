package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PeriodoNovidade;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.RelNovidadeProd;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RelNovidadeProdPdbxDao;
import totalcross.util.Date;
import totalcross.util.Vector;

public class RelNovidadeProdService extends CrudService {

	private static RelNovidadeProdService instance;

	private RelNovidadeProdService() {
		//--
	}

	public static RelNovidadeProdService getInstance() {
		if (instance == null) {
			instance = new RelNovidadeProdService();
		}
		return instance;
	}

	@Override
	protected CrudDao getCrudDao() {
		return RelNovidadeProdPdbxDao.getInstance();
	}

	@Override
	public void validate(BaseDomain domain) { }
	
	public Vector findRelNovidadeProd(String dsFiltro, int periodoNovidade) throws SQLException {
		return RelNovidadeProdPdbxDao.getInstance().getRegistrosPorTipoNovidade(getFiltro(dsFiltro, null, periodoNovidade)); 
	}
	
	public Vector findAllByExample(String dsFiltro, String cdTipoNovidade, int cdPeriodo) throws SQLException {
		RelNovidadeProd renNovidadeProdutoFilter = getFiltro(dsFiltro, cdTipoNovidade, cdPeriodo);
		return findAllByExample(renNovidadeProdutoFilter);
	}

	public RelNovidadeProd getFiltro(String dsFiltro, String cdTipoNovidade, int cdPeriodo) {
		boolean searchStartsWith = false;
		if (LavenderePdaConfig.usaPesquisaInicioString) {
			if (dsFiltro.startsWith("*")) {
				dsFiltro = dsFiltro.substring(1);
			} else {
				searchStartsWith = true;
			}
		}
		StringBuffer strBuffer = new StringBuffer();
		if (!searchStartsWith) {
			strBuffer.append("%");
		}
		strBuffer.append(dsFiltro);
		strBuffer.append("%");
		Date date = null;
		if (PeriodoNovidade.PERIODO_NOVIDADE_CDHOJE == cdPeriodo) {
			date = DateUtil.getCurrentDate();
			date.advance(-1);
		}
		if (PeriodoNovidade.PERIODO_NOVIDADE_CDPARAMETRO == cdPeriodo) {
			date = DateUtil.getCurrentDate();
			date.advance(-LavenderePdaConfig.nuDiasPermanenciaNovidadesProduto - 1);
		}
		RelNovidadeProd renNovidadeProdutoFilter = (RelNovidadeProd) new RelNovidadeProd();
		renNovidadeProdutoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		renNovidadeProdutoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		renNovidadeProdutoFilter.dtEmissaoRelatorio = date;
		if (cdTipoNovidade != null) {
			renNovidadeProdutoFilter.cdTipoNovidade = cdTipoNovidade;
		}
		if (ValueUtil.isNotEmpty(dsFiltro)) {
			renNovidadeProdutoFilter.cdProduto = strBuffer.toString();
			renNovidadeProdutoFilter.dsProduto = strBuffer.toString();
		}
		renNovidadeProdutoFilter.addWhereTabPreco = true;
		renNovidadeProdutoFilter.limit = LavenderePdaConfig.nuLinhasRetornoBuscaSistema;
		return renNovidadeProdutoFilter;
	}

	public String getCodigoProdutoNaListaDeNovidade(String rowKeyNovidadeProduto) throws SQLException {
		if (ValueUtil.isEmpty(rowKeyNovidadeProduto)) {		
			throw new ValidationException(Messages.RELNOVIDADEPRODUTO_NAO_ENCONTRADO);
		}
		String codigoProduto  = RelNovidadeProdService.getInstance().findColumnByRowKey(rowKeyNovidadeProduto, "cdProduto");
		if (ValueUtil.isEmpty(codigoProduto)) {
			throw new ValidationException(Messages.RELNOVIDADEPRODUTO_NAO_ENCONTRADO);
		} 
		return codigoProduto;
	}
	
	public double getQtEstoqueNivel1(Produto produto, String cdItemGrade1) throws SQLException {
		return getQtEstoqueNivel2(produto, cdItemGrade1, ProdutoGrade.CD_ITEM_GRADE_PADRAO);
	}
	
	public double getQtEstoqueNivel2(Produto produto, String cdItemGrade1, String cdItemGrade2) throws SQLException {
		return getQtEstoqueNivel3(produto, cdItemGrade1, cdItemGrade2, ProdutoGrade.CD_ITEM_GRADE_PADRAO);
	}
	
	public double getQtEstoqueNivel3(Produto produto, String cdItemGrade1, String cdItemGrade2, String cdItemGrade3) throws SQLException {
		RelNovidadeProd filter = new RelNovidadeProd();
		filter.cdEmpresa = produto.cdEmpresa;
		filter.cdRepresentante = produto.cdRepresentante;
		filter.cdProduto = produto.cdProduto;
		filter.cdItemGrade1 = cdItemGrade1;
		filter.cdItemGrade2 = cdItemGrade2;
		filter.cdItemGrade3 = cdItemGrade3;
		filter.filterGrade = true;
		Vector list = findAllByExampleSummary(filter);
		int size = list.size();
		double qtEstoque = 0d;
		for (int i = 0; i < size; i++) {
			qtEstoque += ValueUtil.getDoubleValue(((RelNovidadeProd)list.items[i]).dsNovidadeProduto);
		}
		return qtEstoque;
	}
	
	public double getSumQtEstoque(Produto produto, String cdItemGrade1, String cdItemGrade2, String cdItemGrade3, RelNovidadeProd relNovidadeProdFilter) throws SQLException {
		relNovidadeProdFilter.cdEmpresa = produto.cdEmpresa;
		relNovidadeProdFilter.cdRepresentante = produto.cdRepresentante;
		relNovidadeProdFilter.cdProduto = produto.cdProduto;
		relNovidadeProdFilter.cdItemGrade1 = cdItemGrade1;
		relNovidadeProdFilter.cdItemGrade2 = cdItemGrade2;
		relNovidadeProdFilter.cdItemGrade3 = cdItemGrade3;
		return sumByExample(relNovidadeProdFilter, "DSNOVIDADEPRODUTO");
	}
	
	public double getPrecoProdutoUnidade(RelNovidadeProd relNovidadeProd) throws SQLException {
		final Produto produto = ProdutoService.getInstance().getProduto(relNovidadeProd.cdProduto);
		produto.itemTabelaPreco = null; 
		double preco = ProdutoService.getInstance().getPrecoProduto(produto, null, relNovidadeProd.cdTabelaPreco, relNovidadeProd.cdUf);
		if (produto.nuConversaoUnidadesMedida > 0) {
			preco /= produto.nuConversaoUnidadesMedida;
		}
		return preco;
	}
	
	public String getPrecoInterfaceValue(RelNovidadeProd relNovidadeProd) throws SQLException {
		final double unidadePreco = getPrecoProdutoUnidade(relNovidadeProd);
		return StringUtil.getStringValueToInterface(unidadePreco);
	}
	
}