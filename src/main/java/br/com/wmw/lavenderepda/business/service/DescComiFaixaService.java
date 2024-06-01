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
import br.com.wmw.lavenderepda.business.domain.DescComi;
import br.com.wmw.lavenderepda.business.domain.DescComiFaixa;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescComiFaixaPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import totalcross.util.Vector;

public class DescComiFaixaService extends CrudService {

    private static DescComiFaixaService instance;

    private DescComiFaixaService() {
        //--
    }

    public static DescComiFaixaService getInstance() {
        if (instance == null) {
            instance = new DescComiFaixaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return DescComiFaixaPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

	//*********************
	//DESCCOMIGRUPO
	//*********************

    public Vector findAllByGrupo(String cdCliente, String cdRepresentante, String cdCondicaoPagamento, String cdRamoAtividade, String cdCidade, String cdGrupoProduto1, String cdGrupoProduto2, String cdGrupoProduto3) throws SQLException {
    	DescComi descComi = DescComiService.getInstance().getDescComissaoGrupoVigente(cdCliente, cdRepresentante, cdCondicaoPagamento, cdRamoAtividade, cdCidade, cdGrupoProduto1, cdGrupoProduto2, cdGrupoProduto3);
    	if (descComi != null) {
    		DescComiFaixa descComiProdFilter = new DescComiFaixa();
    		descComiProdFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		descComiProdFilter.cdDescComi = descComi.cdDescComi;
    		return findAllByExample(descComiProdFilter);
    	}
    	return new Vector(0);
	}

	public DescComiFaixa findDescComiGrupoPadrao(String cdCliente, String cdRepresentante, String cdCondicaoPagamento, String cdRamoAtividade, String cdCidade, Produto produto) throws SQLException {
		Vector list = findAllByGrupo(cdCliente, cdRepresentante, cdCondicaoPagamento, cdRamoAtividade, cdCidade, produto.cdGrupoProduto1, produto.cdGrupoProduto2, produto.cdGrupoProduto3);
		if (list.size() > 0) {
			return (DescComiFaixa) list.items[0];
		}
		return null;
	}

	public Vector findDescComiGrupoByItemPedido(Pedido pedido, final ItemPedido itemPedido) throws SQLException {
    	DescComi descComi = DescComiService.getInstance().getDescComissaoGrupoVigente(pedido.cdCliente, SessionLavenderePda.usuarioPdaRep.cdUsuario, pedido.cdCondicaoPagamento, pedido.getCliente().cdRamoAtividade, pedido.getCliente().cdCidadeComercial, itemPedido.getProduto().cdGrupoProduto1, itemPedido.getProduto().cdGrupoProduto2, itemPedido.getProduto().cdGrupoProduto3);
    	if (descComi != null) {
    		DescComiFaixa descComiFaixaFilter = new DescComiFaixa();
    		descComiFaixaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		descComiFaixaFilter.cdDescComi = descComi.cdDescComi;
    		descComiFaixaFilter.vlPctComissao = itemPedido.vlPctComissao;
    		descComiFaixaFilter.vlPctDesconto = itemPedido.vlPctDesconto;
    		return findAllByExample(descComiFaixaFilter);
    	}
    	return new Vector(0);
	}

	public void aplicaDescComiGrupoNoItemPedido(final ItemPedido itemPedido, final DescComiFaixa descComiGrupo, final boolean aplicaDesconto) throws SQLException {
		itemPedido.descComissaoGrupo = descComiGrupo;
		TipoPedido tipoPedido = itemPedido.pedido.getTipoPedido();
    	itemPedido.vlPctComissao = itemPedido.descComissaoGrupo != null && (tipoPedido != null && !tipoPedido.isIgnoraCalculoComissao() || tipoPedido == null) ? itemPedido.descComissaoGrupo.vlPctComissao : 0;
		if (aplicaDesconto) {
			itemPedido.vlPctDesconto = itemPedido.descComissaoGrupo != null ? itemPedido.descComissaoGrupo.vlPctDesconto : 0;
		}
	}

	public Vector verificaQtdeMinimaDosItensPedido(Pedido pedido, boolean lanceException) throws SQLException {
		Vector itensNaoConforme = new Vector();
		int size = pedido.itemPedidoList.size();
		for ( int i = 0 ; i < size; i++ ) {
			ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
			if (itemPedido.vlPctComissao != 0) {
				String cdGrupoProduto1 = itemPedido.getProduto().cdGrupoProduto1;
				String cdGrupoProduto2 = itemPedido.getProduto().cdGrupoProduto2;
				String cdGrupoProduto3 = itemPedido.getProduto().cdGrupoProduto3;
				if (!ValueUtil.isEmpty(cdGrupoProduto1)) {
					double qtItensGrupo = 0d;
					for ( int j = 0 ; j < size; j++ ) {
						ItemPedido itemPedidoTemp = (ItemPedido)pedido.itemPedidoList.items[j];
						if (ValueUtil.valueEquals(cdGrupoProduto1, itemPedidoTemp.getProduto().cdGrupoProduto1) &&
								(ValueUtil.valueEquals(cdGrupoProduto2, itemPedidoTemp.getProduto().cdGrupoProduto2) || ValueUtil.isEmpty(cdGrupoProduto2)) &&
								(ValueUtil.valueEquals(cdGrupoProduto3, itemPedidoTemp.getProduto().cdGrupoProduto3) || ValueUtil.isEmpty(cdGrupoProduto3)) ) {
							qtItensGrupo += itemPedidoTemp.getQtItemFisico();
						}
					}
					double qtMin = isQtMinimoRespeitadaNoDescGrupo(pedido.cdCliente, SessionLavenderePda.usuarioPdaRep.cdRepresentante, pedido.cdCondicaoPagamento, pedido.getCliente().cdRamoAtividade, pedido.getCliente().cdCidadeComercial, itemPedido, qtItensGrupo);
					if (qtMin > 0) {
						if (lanceException) {
							itensNaoConforme = null;
							StringBuffer strBuffer = new StringBuffer();
							throw new ValidationException(strBuffer.append(DescComiFaixa.SIGLE_EXCEPTION).append(Messages.DESCCOMIGRUPO_MSG_VALIDACAO).toString());
						} else {
							itemPedido.qtItemMinDescComissao = qtMin;
							itemPedido.qtItemMesmoGrupo = qtItensGrupo;
							itensNaoConforme.addElement(itemPedido);
						}
					}
				}
			}
		}
		return itensNaoConforme;
	}

	private double isQtMinimoRespeitadaNoDescGrupo(String cdCliente, String cdRepresentante, String cdCondicaoPagamento, String cdRamoAtividade, String cdCidade, final ItemPedido itemPedido, final double qtItensGrupo) throws SQLException {
		Vector descComiGrupoList = findAllByGrupo(cdCliente, cdRepresentante, cdCondicaoPagamento, cdRamoAtividade, cdCidade, itemPedido.getProduto().cdGrupoProduto1, itemPedido.getProduto().cdGrupoProduto2, itemPedido.getProduto().cdGrupoProduto3);
		int size = descComiGrupoList.size();
		if (size == 0) {
			return 0;
		}
		DescComiFaixa descComiGrupo;
		double qtMinAprox = 0;
		for (int j = 0; j < size; j++) {
			descComiGrupo = (DescComiFaixa)descComiGrupoList.items[j];
			if ((ValueUtil.round(descComiGrupo.vlPctComissao) == ValueUtil.round(itemPedido.vlPctComissao)) &&
					(ValueUtil.round(descComiGrupo.vlPctDesconto) >= ValueUtil.round(itemPedido.vlPctDesconto)) &&
					(ValueUtil.round(descComiGrupo.qtItem) <= ValueUtil.round(qtItensGrupo))) {
				return 0;
			}
			if ((ValueUtil.round(descComiGrupo.vlPctComissao) == ValueUtil.round(itemPedido.vlPctComissao)) &&
					(ValueUtil.round(descComiGrupo.vlPctDesconto) >= ValueUtil.round(itemPedido.vlPctDesconto))) {
				qtMinAprox = descComiGrupo.qtItem;
			}
		}
		return qtMinAprox;
	}

	//*********************
	//DESCCOMIPROD
	//*********************

    public Vector findAllByProduto(String cdCliente, String cdRepresentante, String cdCondicaoPagamento, String cdRamoAtividade, String cdCidade, String cdProduto) throws SQLException {
    	DescComi descComi = DescComiService.getInstance().getDescComissaoProdVigente(cdCliente, cdRepresentante, cdCondicaoPagamento, cdRamoAtividade, cdCidade, cdProduto);
    	if (descComi != null) {
    		DescComiFaixa descComiProdFilter = new DescComiFaixa();
    		descComiProdFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		descComiProdFilter.cdDescComi = descComi.cdDescComi;
    		return findAllByExample(descComiProdFilter);
    	}
    	return new Vector(0);
    }

    public DescComiFaixa findDescComiProdPadrao(String cdCliente, String cdRepresentante, String cdCondicaoPagamento, String cdRamoAtividade, String cdCidade, String cdProduto) throws SQLException {
    	Vector list = findAllByProduto(cdCliente, cdRepresentante, cdCondicaoPagamento, cdRamoAtividade, cdCidade, cdProduto);
    	if (list.size() > 0) {
    		return (DescComiFaixa) list.items[0];
    	}
    	return null;
    }

    public Vector findDescComiProdByItemPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
    	DescComi descComi = DescComiService.getInstance().getDescComissaoProdVigente(pedido.cdCliente, SessionLavenderePda.usuarioPdaRep.cdUsuario, pedido.cdCondicaoPagamento, pedido.getCliente().cdRamoAtividade, pedido.getCliente().cdCidadeComercial, itemPedido.cdProduto);
    	if (descComi != null) {
    		DescComiFaixa descComiProdFilter = new DescComiFaixa();
    		descComiProdFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		descComiProdFilter.cdDescComi = descComi.cdDescComi;
    		descComiProdFilter.vlPctComissao = itemPedido.vlPctComissao;
    		descComiProdFilter.qtItem = itemPedido.getQtItemFisico();
    		descComiProdFilter.vlPctDesconto = itemPedido.vlPctDesconto;
    		return findAllByExample(descComiProdFilter);
    	}
    	return new Vector(0);
    }

    public void aplicaDescComiProdNoItemPedido(ItemPedido itemPedido, DescComiFaixa descComiProd, boolean aplicaDesconto) throws SQLException {
    	itemPedido.descComissaoProd = descComiProd;
    	TipoPedido tipoPedido = itemPedido.pedido.getTipoPedido();
    	itemPedido.vlPctComissao = itemPedido.descComissaoProd != null && (tipoPedido != null && !tipoPedido.isIgnoraCalculoComissao() || tipoPedido == null) ? itemPedido.descComissaoProd.vlPctComissao : 0;
    	if (aplicaDesconto) {
        	itemPedido.vlPctDesconto = itemPedido.descComissaoProd != null ? itemPedido.descComissaoProd.vlPctDesconto : 0;
    	}
    }

    public void reloadDescComiItensPedidoNaTrocaCondicao(Pedido pedido, String cdCondicaoPagtoOld, String cdCondicaoPagtoNew) throws SQLException {
    	if (pedido.itemPedidoList.size() > 0) {
    		boolean houveAlgumaAlteracaoDescComi = false;
    		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
    			ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
    			//--
    			boolean aplicouDescComiProd = false;
    			boolean aplicouDescComiGrupo = false;
    			if (LavenderePdaConfig.usaDescontoComissaoPorProduto) {
    				pedido.cdCondicaoPagamento = cdCondicaoPagtoOld;
    				Vector descComiProdListOld = findDescComiProdByItemPedido(pedido, itemPedido);
    				pedido.cdCondicaoPagamento = cdCondicaoPagtoNew;
    				Vector descComiProdList = findDescComiProdByItemPedido(pedido, itemPedido);
    				//--
    		    	if (descComiProdList.size() > 0) {
    		    		String cdDescComiOld = "";
    		    		if (descComiProdListOld.size() > 0) {
    		    			cdDescComiOld = ((DescComiFaixa)descComiProdListOld.items[0]).cdDescComi;
    		    		}
    		    		String cdDescComiNew = ((DescComiFaixa)descComiProdList.items[0]).cdDescComi;
    		    		if (!cdDescComiNew.equals(cdDescComiOld)) {
    		    			if (descComiProdListOld.size() > 1 || descComiProdList.size() > 1) {
    	    					throw new ValidationException(MessageUtil.getMessage(Messages.DESCCOMIPROD_MULTILPOS_DESC_TROCA_COMBO, itemPedido.toString()));
    	    				}
    		    			aplicaDescComiProdNoItemPedido(itemPedido, (DescComiFaixa)descComiProdList.items[0], true);
    		    			ItemPedidoService.getInstance().calculate(itemPedido, pedido);
    		    			houveAlgumaAlteracaoDescComi = true;
    		    		}
    		    		aplicouDescComiProd = true;
    		    	}
    			}
    			//--
    			if (LavenderePdaConfig.usaDescontoComissaoPorGrupo && !aplicouDescComiProd) {
        			pedido.cdCondicaoPagamento = cdCondicaoPagtoOld;
        			Vector descComiGrupoListOld = findDescComiGrupoByItemPedido(pedido, itemPedido);
    				pedido.cdCondicaoPagamento = cdCondicaoPagtoNew;
    				Vector descComiGrupoList = findDescComiGrupoByItemPedido(pedido, itemPedido);
    				//--
    				if (descComiGrupoList.size() > 0) {
    		    		String cdDescComiGrupoOld = "";
    		    		if (descComiGrupoListOld.size() > 0) {
    		    			cdDescComiGrupoOld = ((DescComiFaixa)descComiGrupoListOld.items[0]).cdDescComi;
    		    		}
    		    		String cdDescComiGrupoNew = ((DescComiFaixa)descComiGrupoList.items[0]).cdDescComi;
    		    		if (!cdDescComiGrupoNew.equals(cdDescComiGrupoOld)) {
    		    			if (descComiGrupoListOld.size() > 1 || descComiGrupoList.size() > 1) {
    	        				throw new ValidationException(MessageUtil.getMessage(Messages.DESCCOMIGRUPO_MULTILPOS_DESC_TROCA_COMBO, itemPedido.getProduto().dsProduto));
    	        			}
    						aplicaDescComiGrupoNoItemPedido(itemPedido, (DescComiFaixa)descComiGrupoList.items[0], true);
    						ItemPedidoService.getInstance().calculate(itemPedido, pedido);
    		    			houveAlgumaAlteracaoDescComi = true;
    		    		}
    		    		aplicouDescComiGrupo = true;
    				}
    			}
    			//--
    			if (!aplicouDescComiProd && !aplicouDescComiGrupo) {
    				throw new ValidationException(MessageUtil.getMessage(Messages.DESCCOMIPROD_SEM_DESC_TROCA_COMBO, itemPedido.getProduto().dsProduto));
    			}
    		}
    		//--
    		if (houveAlgumaAlteracaoDescComi) {
    			PedidoService.getInstance().updateItensPedidoAfterChanges(pedido);
    			PedidoService.getInstance().calculate(pedido);
    			PedidoService.getInstance().updatePedidoAfterCrudItemPedido(pedido);
    			PedidoPdbxDao.getInstance().update(pedido);
    		}
    	}
    }

}