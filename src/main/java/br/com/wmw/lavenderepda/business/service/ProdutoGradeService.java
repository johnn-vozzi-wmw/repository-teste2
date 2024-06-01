package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.GiroProduto;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.RelNovidadeProd;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoGradeDbxDao;
import br.com.wmw.lavenderepda.util.Util;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class ProdutoGradeService extends CrudService {

    private static ProdutoGradeService instance;

    private ProdutoGradeService() {
        //--
    }

    public static ProdutoGradeService getInstance() {
        if (instance == null) {
            instance = new ProdutoGradeService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ProdutoGradeDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public ProdutoGrade getProdutoGradeByItemPedidoGrade(ItemPedidoGrade itemPedidoGrade) throws SQLException {
		ProdutoGrade produtoGrade = new ProdutoGrade();
		produtoGrade.cdEmpresa = itemPedidoGrade.cdEmpresa;
		produtoGrade.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ItemPedidoGrade.class);
		produtoGrade.cdProduto = itemPedidoGrade.cdProduto;
		produtoGrade.cdItemGrade1 = itemPedidoGrade.cdItemGrade1;
		produtoGrade.cdItemGrade2 = itemPedidoGrade.cdItemGrade2;
		produtoGrade.cdItemGrade3 = itemPedidoGrade.cdItemGrade3;
		if (LavenderePdaConfig.usaGradeProdutoPorTabelaPreco) {
			produtoGrade.cdTabelaPreco = ItemPedidoGradeService.getInstance().getCdTabelaPreco(itemPedidoGrade);
		} else {
			produtoGrade.cdTabelaPreco = ProdutoGrade.CDTABELAPRECO_PADRAO;
		}
		return (ProdutoGrade)findByRowKey(produtoGrade.getRowKey());
    }

    public ProdutoGrade getProdutoGradeByGiroProduto(GiroProduto giroProduto) throws SQLException {
    	ProdutoGrade produtoGrade = new ProdutoGrade();
    	produtoGrade.cdEmpresa = giroProduto.cdEmpresa;
    	produtoGrade.cdRepresentante = giroProduto.cdRepresentante;
    	produtoGrade.cdProduto = giroProduto.cdProduto;
    	produtoGrade.cdItemGrade1 = giroProduto.cdItemGrade1;
    	produtoGrade.cdItemGrade2 = giroProduto.cdItemGrade2;
    	produtoGrade.cdItemGrade3 = giroProduto.cdItemGrade3;
    	if (LavenderePdaConfig.usaGradeProdutoPorTabelaPreco) {
    		produtoGrade.cdTabelaPreco = giroProduto.cdTabelaPreco;
    	} else {
    		produtoGrade.cdTabelaPreco = ProdutoGrade.CDTABELAPRECO_PADRAO;
    	}
    	return (ProdutoGrade)findByRowKey(produtoGrade.getRowKey());
    }

    public String getCdTipoItemGrade1ByItemPedidoGrade(ItemPedidoGrade itemPedidoGrade, String cdTabelaPreco) throws SQLException {
    	ProdutoGrade produtoGrade = new ProdutoGrade();
    	produtoGrade.cdEmpresa = itemPedidoGrade.cdEmpresa;
    	produtoGrade.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ItemPedidoGrade.class);
    	produtoGrade.cdProduto = itemPedidoGrade.cdProduto;
    	produtoGrade.cdItemGrade1 = itemPedidoGrade.cdItemGrade1;
    	produtoGrade.cdItemGrade2 = itemPedidoGrade.cdItemGrade2;
    	produtoGrade.cdItemGrade3 = itemPedidoGrade.cdItemGrade3;
    	if (LavenderePdaConfig.usaGradeProdutoPorTabelaPreco) {
    		produtoGrade.cdTabelaPreco = cdTabelaPreco;
    	} else {
    		produtoGrade.cdTabelaPreco = ProdutoGrade.CDTABELAPRECO_PADRAO;
    	}
    	return findColumnByRowKey(produtoGrade.getRowKey(), "CDTIPOITEMGRADE1");
    }

    public String getCdTipoItemGrade1ByItemPedido(ItemPedido itemPedido) throws SQLException {
    	ProdutoGrade produtoGrade = new ProdutoGrade();
    	produtoGrade.cdEmpresa = itemPedido.cdEmpresa;
    	produtoGrade.cdRepresentante = itemPedido.cdRepresentante;
    	produtoGrade.cdProduto = itemPedido.cdProduto;
    	produtoGrade.cdItemGrade1 = itemPedido.cdItemGrade1;
    	produtoGrade.cdItemGrade2 = itemPedido.cdItemGrade2;
    	produtoGrade.cdItemGrade3 = itemPedido.cdItemGrade3;
    	if (LavenderePdaConfig.usaGradeProdutoPorTabelaPreco) {
    		produtoGrade.cdTabelaPreco = itemPedido.getCdTabelaPreco();
    	} else {
    		produtoGrade.cdTabelaPreco = ProdutoGrade.CDTABELAPRECO_PADRAO;
    	}
    	return findColumnByRowKey(produtoGrade.getRowKey(), "CDTIPOITEMGRADE1");
    }
    
    public Vector findProdutoGradeList(String cdProduto) throws SQLException {
    	return findProdutoGradeList(cdProduto, null);
    }
    
    public Vector findProdutoGradeList(String cdProduto, String cdTabelaPreco) throws SQLException {
    	return findProdutoGradeList(cdProduto, cdTabelaPreco, null, null);
    	
    }
    
    public Vector findProdutoGradeList(ItemPedido itemPedido) throws SQLException {
    	if (LavenderePdaConfig.usaGradeProduto5() && itemPedido.getProduto().isProdutoAgrupadorGrade()) {
    		return findAllProdutoGradeAgrupadorGrade(itemPedido.getCdTabelaPreco(), itemPedido.getProduto().getDsAgrupadorGrade(), itemPedido.isInseridoNoPedido());
    	}
    	return findProdutoGradeList(itemPedido.cdProduto, itemPedido.getCdTabelaPreco());
    }
    
    public Vector findProdutoGradeList(String cdProduto, String cdTabelaPreco, String cdItemGrade1, RelNovidadeProd relNovidadeProdFilter) throws SQLException {
    	ProdutoGrade produtoGradeFilter = new ProdutoGrade();
		produtoGradeFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoGradeFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		produtoGradeFilter.cdProduto = cdProduto;
		produtoGradeFilter.cdItemGrade1 = cdItemGrade1;
		if (LavenderePdaConfig.usaGradeProdutoPorTabelaPreco) {
			produtoGradeFilter.cdTabelaPreco = cdTabelaPreco;
		} else {
			produtoGradeFilter.cdTabelaPreco = ProdutoGrade.CDTABELAPRECO_PADRAO;
		}
		produtoGradeFilter.relNovidadeProdFilter = relNovidadeProdFilter;
    	return findAllByExample(produtoGradeFilter);
    }
    
    public Vector findAllProdutoGradeErpByItemPedido(ItemPedido itemPedido) throws SQLException {
    	return ProdutoGradeDbxDao.getInstance().findAllProdutoGradeErpByItemPedido(itemPedido);
    }
    
    public Vector findAllProdutoGradeInseridosByItemPedido(ItemPedido itemPedido) throws SQLException {
    	return ProdutoGradeDbxDao.getInstance().findAllProdutoGradeInseridosByItemPedido(itemPedido);
    }

    public Vector getProdutoGradeListCache(ProdutoBase produtoBase) throws SQLException {
    	return findAllCacheByPartialRowKey(produtoBase);
    }
    
    public boolean isProdutoPossuiGrade(ProdutoBase produtoBase) throws SQLException {
		return produtoBase.possuiGrade = ValueUtil.isNotEmpty(getProdutoGradeListCache(produtoBase));
    }

	public ProdutoGrade getProdutoGradeFilter(String cdItemGrade1, String cdItemGrade2, String cdItemGrade3, String cdProduto, String cdTabelaPreco) {
		ProdutoGrade produtoGradeFilter = new ProdutoGrade();
		produtoGradeFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoGradeFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ProdutoGrade.class);
		produtoGradeFilter.cdProduto = cdProduto;
		produtoGradeFilter.cdItemGrade1 = cdItemGrade1;
		produtoGradeFilter.cdItemGrade2 = cdItemGrade2 != null ? cdItemGrade2 : ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		produtoGradeFilter.cdItemGrade3 = cdItemGrade3 != null ? cdItemGrade3 : ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		if (LavenderePdaConfig.usaGradeProdutoPorTabelaPreco) {
			produtoGradeFilter.cdTabelaPreco = cdTabelaPreco;
		} else {
			produtoGradeFilter.cdTabelaPreco = ProdutoGrade.CDTABELAPRECO_PADRAO;
		}
		return produtoGradeFilter;
	}

	public void validaInfosComplObrigatoriasProdutoGrade(double vlAltura, double vlLargura, double vlComprimento) {
		if (vlAltura <= 0) {
			throw new ValidationException(Messages.INFO_COMPL_VL_ALTURA_VALIDACAO_VAZIO);
		} else if (vlLargura <= 0) {
			throw new ValidationException(Messages.INFO_COMPL_VL_LARGURA_VALIDACAO_VAZIO);
		} else if (vlComprimento <= 0) {
			throw new ValidationException(Messages.INFO_COMPL_VL_COMPRIMENTO_VALIDACAO_VAZIO);
		}
	}

	public void validaInfosComplProdutoGrade(double vlAltura, double vlLargura, double vlComprimento) {
		if (vlAltura > 0 || vlLargura > 0 || vlComprimento > 0) {
			if (vlAltura <= 0 || vlLargura <= 0 || vlComprimento <= 0) {
				throw new ValidationException(Messages.INFO_COMPL_VALIDACAO_CAMPO_VAZIO);
			}
		}
	}

	public void validaQtItemFisico(double qtItemFisico) {
		if (qtItemFisico <= 0) {
			throw new ValidationException(Messages.INFO_COMPL_QT_ITEM_VALIDACAO_VAZIO);
		}
	}

	public void validaVlMinMaxInfosComplProdutoGrade(ProdutoGrade produtoGrade, double vlAltura, double vlLargura, double vlComprimento) {
		if (vlAltura > 0) {
			if (produtoGrade.vlAlturaMin > 0 && vlAltura < produtoGrade.vlAlturaMin) {
				throw new ValidationException(MessageUtil.getMessage(Messages.INFO_COMPL_VALIDACAO_CAMPO_ALTURA_MIN, new Object[] {StringUtil.getStringValueToInterface(vlAltura), StringUtil.getStringValueToInterface(produtoGrade.vlAlturaMin)}));
			} else if (produtoGrade.vlAlturaMax > 0 && vlAltura > produtoGrade.vlAlturaMax) {
				throw new ValidationException(MessageUtil.getMessage(Messages.INFO_COMPL_VALIDACAO_CAMPO_ALTURA_MAX, new Object[] {StringUtil.getStringValueToInterface(vlAltura), StringUtil.getStringValueToInterface(produtoGrade.vlAlturaMax)}));
			}
		} 
		if (vlLargura > 0) {
			if (produtoGrade.vlLarguraMin > 0 && vlLargura < produtoGrade.vlLarguraMin) {
				throw new ValidationException(MessageUtil.getMessage(Messages.INFO_COMPL_VALIDACAO_CAMPO_LARGURA_MIN, new Object[] {StringUtil.getStringValueToInterface(vlLargura), StringUtil.getStringValueToInterface(produtoGrade.vlLarguraMin)}));
			} else if (produtoGrade.vlLarguraMax > 0 && vlLargura > produtoGrade.vlLarguraMax) {
				throw new ValidationException(MessageUtil.getMessage(Messages.INFO_COMPL_VALIDACAO_CAMPO_LARGURA_MAX, new Object[] {StringUtil.getStringValueToInterface(vlLargura), StringUtil.getStringValueToInterface(produtoGrade.vlLarguraMax)}));
			}
		} 
		if (vlComprimento > 0) {
			if (produtoGrade.vlComprimentoMin > 0 && vlComprimento < produtoGrade.vlComprimentoMin) {
				throw new ValidationException(MessageUtil.getMessage(Messages.INFO_COMPL_VALIDACAO_CAMPO_COMPRIMENTO_MIN, new Object[] {StringUtil.getStringValueToInterface(vlComprimento), StringUtil.getStringValueToInterface(produtoGrade.vlComprimentoMin)}));
			} else if (produtoGrade.vlComprimentoMax > 0 && vlComprimento > produtoGrade.vlComprimentoMax) {
				throw new ValidationException(MessageUtil.getMessage(Messages.INFO_COMPL_VALIDACAO_CAMPO_COMPRIMENTO_MAX, new Object[] {StringUtil.getStringValueToInterface(vlComprimento), StringUtil.getStringValueToInterface(produtoGrade.vlComprimentoMax)}));
			}
		}		
	}
	
	public Vector findAllProdutoGradeAgrupadorGrade(String cdTabelaPreco, String dsAgrupadorGrade) throws SQLException {
		return findAllProdutoGradeAgrupadorGrade(null, cdTabelaPreco, dsAgrupadorGrade, false);
	}
	public Vector findAllProdutoGradeAgrupadorGrade(String cdTabelaPreco, String dsAgrupadorGrade, Boolean existeNoPedido) throws SQLException {
		return findAllProdutoGradeAgrupadorGrade(null, cdTabelaPreco, dsAgrupadorGrade, existeNoPedido);
	}
	
	public Vector findAllProdutoGradeAgrupadorGrade(String cdProduto, String cdTabelaPreco, String dsAgrupadorGrade, Boolean existeNoPedido) throws SQLException {
		ProdutoGrade filter = getProdutoGradeFilter(null, null, null, cdProduto, cdTabelaPreco);
		filter.cdItemGrade2 = filter.cdItemGrade3 = null;
		filter.dsAgrupadorGrade = dsAgrupadorGrade;
		return ProdutoGradeDbxDao.getInstance().findAllProdutoGradeAgrupadorGrade(filter, existeNoPedido);
	}
	
	public Map<String, ProdutoGrade> montaHashProdutoGradeAgrupador(String cdTabelaPreco, String cdItemGrade1, String dsAgrupadorGrade) throws SQLException {
		ProdutoGrade filter = getProdutoGradeFilter(cdItemGrade1, null, null, null, cdTabelaPreco);
		filter.cdItemGrade2 = filter.cdItemGrade3 = null;
		filter.dsAgrupadorGrade = dsAgrupadorGrade;
		Vector list = ProdutoGradeDbxDao.getInstance().findProdutoGradeAgrupadorGrade1(filter);
		int size = list.size();
		Map<String, ProdutoGrade> hashProdutoGrade = new HashMap<>(size);
		
		for (int i = 0; i < size; i++) {
			ProdutoGrade produtoGrade = (ProdutoGrade) list.items[i];
			hashProdutoGrade.put(produtoGrade.getProdutoGradeKey(), produtoGrade);
		}
		return hashProdutoGrade;
	}
	
	public LinkedHashMap<String, Image> getImagesAgrupadorGradeGroupByProduto(ItemPedido itemPedido) throws SQLException {
		Vector produtoList = ProdutoService.getInstance().findProdutoGradeComNmFotoByItemPedido(itemPedido);
		LinkedHashMap<String, Image> produtoImageMap = new LinkedHashMap<>();
		for (int i = 0; i < produtoList.size(); i++) {
			Produto produto = (Produto)produtoList.items[i];
			Image image = Util.getImageForProdutoList(produto, 0, true);
			produtoImageMap.put(produto.getRowKey(), image);
		}
		return produtoImageMap;
	}
	
}