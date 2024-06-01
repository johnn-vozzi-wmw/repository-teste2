package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ClassUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.framework.config.ListContainerConfig;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.TipoRanking;
import br.com.wmw.lavenderepda.presentation.ui.ListProdutoForm;
import totalcross.util.Vector;

public class TipoRankingService extends CrudService {
	
	private static TipoRankingService instance;

    private TipoRankingService() {
        //--
    }

    public static TipoRankingService getInstance() {
        if (instance == null) {
            instance = new TipoRankingService();
        }
        return instance;
    }

	@Override
	public void validate(BaseDomain domain) throws java.sql.SQLException {
		
	}

	@Override
	protected CrudDao getCrudDao() {
		return null;
	}
	
	@Override
	public Vector findAll() throws java.sql.SQLException {
		Vector tipoRankingList = new Vector();
		
		TipoRanking tipoRanking0 = new TipoRanking();
		tipoRanking0.cdTipoRanking = TipoRanking.CDTIPORANKING_ORDENACAO_PADRAO;
		tipoRanking0.dsTipoRanking = TipoRanking.DSTIPORANKING_ORDENACAO_PADRAO;
		tipoRankingList.addElement(tipoRanking0);
		//--
		if (LavenderePdaConfig.isUsaOrdenacaoRankingProdutoEmpresa()) {
			TipoRanking tipoRanking1 = new TipoRanking();
			tipoRanking1.cdTipoRanking = TipoRanking.CDTIPORANKING_EMPRESA;
			tipoRanking1.dsTipoRanking = TipoRanking.DSTIPORANKING_EMPRESA;
			tipoRankingList.addElement(tipoRanking1);
		}
		//--
		if (LavenderePdaConfig.isUsaOrdenacaoRankingProdutoRepresentante()) {
			TipoRanking tipoRanking2 = new TipoRanking();
			tipoRanking2.cdTipoRanking = TipoRanking.CDTIPORANKING_REPRESENTANTE;
			tipoRanking2.dsTipoRanking = TipoRanking.DSTIPORANKING_REPRESENTANTE;
			tipoRankingList.addElement(tipoRanking2);
		}
		//--
		if (LavenderePdaConfig.isUsaOrdenacaoRankingProdutoSupervisao()) {
			TipoRanking tipoRanking3 = new TipoRanking();
			tipoRanking3.cdTipoRanking = TipoRanking.CDTIPORANKING_SUPERVISAO;
			tipoRanking3.dsTipoRanking = TipoRanking.DSTIPORANKING_SUPERVISAO;
			tipoRankingList.addElement(tipoRanking3);
		}
		//--
		if (LavenderePdaConfig.isUsaOrdenacaoRankingProdutoRegional()) {
			TipoRanking tipoRanking4 = new TipoRanking();
			tipoRanking4.cdTipoRanking = TipoRanking.CDTIPORANKING_REGIONAL;
			tipoRanking4.dsTipoRanking = TipoRanking.DSTIPORANKING_REGIONAL;
			tipoRankingList.addElement(tipoRanking4);
		}
		
		return tipoRankingList;
	}
	
	public String getSortColumnNuRankingProduto(Integer cdTipoRanking) {
		String nmColumnRanking = LavenderePdaConfig.usaOrdenacaoPorCodigoListaProdutos ? ProdutoBase.SORT_COLUMN_CDPRODUTO : ProdutoBase.SORT_COLUMN_DSPRODUTO.toUpperCase();
		switch (cdTipoRanking) {
			case TipoRanking.CDTIPORANKING_ORDENACAO_PADRAO:
				nmColumnRanking = LavenderePdaConfig.usaOrdenacaoPorCodigoListaProdutos ? ProdutoBase.SORT_COLUMN_CDPRODUTO : ProdutoBase.SORT_COLUMN_DSPRODUTO.toUpperCase();
				break;
			case TipoRanking.CDTIPORANKING_EMPRESA:
				nmColumnRanking = ProdutoBase.NMCOLUMN_NURANKINGEMP;
				break;
			case TipoRanking.CDTIPORANKING_REPRESENTANTE:
				nmColumnRanking = ProdutoBase.NMCOLUMN_NURANKINGREP;
				break;
			case TipoRanking.CDTIPORANKING_SUPERVISAO:
				nmColumnRanking = ProdutoBase.NMCOLUMN_NURANKINGSUP;
				break;
			case TipoRanking.CDTIPORANKING_REGIONAL:
				nmColumnRanking = ProdutoBase.NMCOLUMN_NURANKINGREG;
				break;
		}
		return nmColumnRanking;
	}
	
	private int getCdTipoRankingByNmColumn(String nmColumn) {
		if (ValueUtil.isNotEmpty(nmColumn)) {
			if (ProdutoBase.NMCOLUMN_NURANKINGEMP.equals(nmColumn)) {
				return TipoRanking.CDTIPORANKING_EMPRESA;
			}
			if (ProdutoBase.NMCOLUMN_NURANKINGREG.equals(nmColumn)) {
				return TipoRanking.CDTIPORANKING_REGIONAL;
			}
			if (ProdutoBase.NMCOLUMN_NURANKINGREP.equals(nmColumn)) {
				return TipoRanking.CDTIPORANKING_REPRESENTANTE;
			}
			if (ProdutoBase.NMCOLUMN_NURANKINGSUP.equals(nmColumn)) {
				return TipoRanking.CDTIPORANKING_SUPERVISAO;
			}
		}
		return TipoRanking.CDTIPORANKING_ORDENACAO_PADRAO;
	}
	
	public TipoRanking getTipoRankingListProduto() {
		TipoRanking tipoRanking = new TipoRanking();
		if (ValueUtil.isNotEmpty(ListContainerConfig.listasConfig)) {
			String[] values = (String[]) ListContainerConfig.listasConfig.get(ClassUtil.getSimpleName(ListProdutoForm.class));
			if (ValueUtil.isNotEmpty(values) && values.length >= 3) {
				if (ProdutoBase.SORTCOLUMN_NURANKING.equals(values[0])) {
					tipoRanking.cdTipoRanking = getCdTipoRankingByNmColumn(values[2]);
					return tipoRanking;
				}
			}
		}
		tipoRanking.cdTipoRanking = TipoRanking.CDTIPORANKING_ORDENACAO_PADRAO;
		return tipoRanking;
	}
	
}
