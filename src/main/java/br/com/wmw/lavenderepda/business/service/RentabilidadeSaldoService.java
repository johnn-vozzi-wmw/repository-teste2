package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.RentabilidadeSaldo;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RentabilidadeSaldoDbxDao;
import totalcross.util.Vector;

public class RentabilidadeSaldoService extends CrudService {

    private static RentabilidadeSaldoService instance;

    private RentabilidadeSaldoService() {
        //--
    }

    public static RentabilidadeSaldoService getInstance() {
        if (instance == null) {
            instance = new RentabilidadeSaldoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return RentabilidadeSaldoDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
/*
        RentabilidadeSaldo rentabilidadeSaldo = (RentabilidadeSaldo) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(rentabilidadeSaldo.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.RENTABILIDADESALDO_LABEL_CDEMPRESA);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(rentabilidadeSaldo.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.RENTABILIDADESALDO_LABEL_CDREPRESENTANTE);
        }
        //flOrigemSaldo
        if (ValueUtil.isEmpty(rentabilidadeSaldo.flOrigemSaldo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.RENTABILIDADESALDO_LABEL_FLORIGEMSALDO);
        }
        //vlTotalRentabilidade
        if (ValueUtil.isEmpty(rentabilidadeSaldo.vlTotalRentabilidade)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.RENTABILIDADESALDO_LABEL_VLTOTALRENTABILIDADE);
        }
        //vlTotalItens
        if (ValueUtil.isEmpty(rentabilidadeSaldo.vlTotalItens)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.RENTABILIDADESALDO_LABEL_VLTOTALITENS);
        }
        //nuCarimbo
        if (ValueUtil.isEmpty(rentabilidadeSaldo.nuCarimbo)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.RENTABILIDADESALDO_LABEL_NUCARIMBO);
        }
        //flTipoAlteracao
        if (ValueUtil.isEmpty(rentabilidadeSaldo.flTipoAlteracao)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.RENTABILIDADESALDO_LABEL_FLTIPOALTERACAO);
        }
        //cdUsuario
        if (ValueUtil.isEmpty(rentabilidadeSaldo.cdUsuario)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.RENTABILIDADESALDO_LABEL_CDUSUARIO);
        }
*/
    }

    public double getPctRentabilidadeFinal() throws SQLException {
		double vlTotalRentabilidade = getVlRentabilidadeFinal();
		double vlTotalItens = getVlVendaFinal();
		if (vlTotalItens > 0) {
			return (vlTotalRentabilidade / vlTotalItens) * 100;
		}
		return 0;
    }

    public double getVlRentabilidadeFinal() throws SQLException {
    	RentabilidadeSaldo rentabilidadeSaldoFilter = new RentabilidadeSaldo();
    	rentabilidadeSaldoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	rentabilidadeSaldoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	Vector rentabilidadeSaldoList = findAllByExample(rentabilidadeSaldoFilter);
    	//--
    	if (rentabilidadeSaldoList == null) {
    		return 0;
    	} else {
    		boolean hasRentabilidadeSaldoPda = false;
    		int size = rentabilidadeSaldoList.size();
    		for (int i = 0; i < size; i++) {
    			RentabilidadeSaldo rentabilidadeSaldo = (RentabilidadeSaldo) rentabilidadeSaldoList.items[i];
    			if (RentabilidadeSaldo.RENTABILIDADESALDO_PDA.equals(rentabilidadeSaldo.flOrigemSaldo)) {
    				hasRentabilidadeSaldoPda = true;
    				break;
    			}
    		}
    		//Se não tiver verbasaldo pda, cria-se uma
    		if (!hasRentabilidadeSaldoPda && (size > 0)) {
    			RentabilidadeSaldo rentabilidadeSaldoPda = new RentabilidadeSaldo();
    			rentabilidadeSaldoPda.cdEmpresa = SessionLavenderePda.cdEmpresa;
    			rentabilidadeSaldoPda.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    			rentabilidadeSaldoPda.flOrigemSaldo = RentabilidadeSaldo.RENTABILIDADESALDO_PDA;
    			rentabilidadeSaldoPda.vlTotalItens = 0;
    			rentabilidadeSaldoPda.vlTotalRentabilidade = 0;
    			if (findByRowKey(rentabilidadeSaldoPda.getRowKey()) == null) {
    				insert(rentabilidadeSaldoPda);
    			}
    		}
    		//--
    		double vlTotalRentabilidade = 0;
    		for (int  i = 0; i < size; i++) {
    			RentabilidadeSaldo rentabilidadeSaldo = (RentabilidadeSaldo) rentabilidadeSaldoList.items[i];
    			vlTotalRentabilidade += rentabilidadeSaldo.vlTotalRentabilidade;
    		}
    		return vlTotalRentabilidade;
    	}
    }

    public double getVlVendaFinal() throws SQLException {
    	RentabilidadeSaldo rentabilidadeSaldoFilter = new RentabilidadeSaldo();
    	rentabilidadeSaldoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	rentabilidadeSaldoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	Vector rentabilidadeSaldoList = findAllByExample(rentabilidadeSaldoFilter);
    	//--
    	if (rentabilidadeSaldoList == null) {
    		return 0;
    	} else {
    		boolean hasRentabilidadeSaldoPda = false;
    		int size = rentabilidadeSaldoList.size();
    		for (int i = 0; i < size; i++) {
    			RentabilidadeSaldo rentabilidadeSaldo = (RentabilidadeSaldo) rentabilidadeSaldoList.items[i];
    			if (RentabilidadeSaldo.RENTABILIDADESALDO_PDA.equals(rentabilidadeSaldo.flOrigemSaldo)) {
    				hasRentabilidadeSaldoPda = true;
    				break;
    			}
    		}
    		//Se não tiver verbasaldo pda, cria-se uma
    		if (!hasRentabilidadeSaldoPda && (size > 0)) {
    			RentabilidadeSaldo rentabilidadeSaldoPda = new RentabilidadeSaldo();
    			rentabilidadeSaldoPda.cdEmpresa = SessionLavenderePda.cdEmpresa;
    			rentabilidadeSaldoPda.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    			rentabilidadeSaldoPda.flOrigemSaldo = RentabilidadeSaldo.RENTABILIDADESALDO_PDA;
    			rentabilidadeSaldoPda.vlTotalItens = 0;
    			rentabilidadeSaldoPda.vlTotalRentabilidade = 0;
    			if (findByRowKey(rentabilidadeSaldoPda.getRowKey()) == null) {
    				insert(rentabilidadeSaldoPda);
    			}
    		}
    		//--
    		double vlTotalItens = 0;
    		for (int  i = 0; i < size; i++) {
    			RentabilidadeSaldo rentabilidadeSaldo = (RentabilidadeSaldo) rentabilidadeSaldoList.items[i];
    			vlTotalItens += rentabilidadeSaldo.vlTotalItens;
    		}
    		return vlTotalItens;
    	}
    }

    public RentabilidadeSaldo getRentabilidadeSaldoPda(final String cdEmpresa, final String cdRepresentante) throws SQLException {
		RentabilidadeSaldo rentabilidadeSaldoFilter = new RentabilidadeSaldo();
		rentabilidadeSaldoFilter.cdEmpresa = cdEmpresa;
		rentabilidadeSaldoFilter.cdRepresentante = cdRepresentante;
		rentabilidadeSaldoFilter.flOrigemSaldo = RentabilidadeSaldo.RENTABILIDADESALDO_PDA;
		RentabilidadeSaldo rentabilidadeSaldo = (RentabilidadeSaldo) findByRowKey(rentabilidadeSaldoFilter.getRowKey());
		if (rentabilidadeSaldo == null) {
			insert(rentabilidadeSaldoFilter);
			return rentabilidadeSaldoFilter;
		}
		return rentabilidadeSaldo;
    }

    public void insertVlRentabilidadeSaldo(ItemPedido itemPedido) throws SQLException {
    	if (itemPedido.vlRentabilidade >= 0 && !itemPedido.getProduto().isNeutro()) {
    		RentabilidadeSaldo rentabilidadeSaldo = getRentabilidadeSaldoPda(itemPedido.cdEmpresa, itemPedido.cdRepresentante);
    		rentabilidadeSaldo.vlTotalRentabilidade += itemPedido.vlRentabilidade;
    		rentabilidadeSaldo.vlTotalItens += ValueUtil.round(itemPedido.vlItemPedido * itemPedido.getQtItemFisico());
			update(rentabilidadeSaldo);
			//--
    		itemPedido.vlRentabilidadeOld = itemPedido.vlRentabilidade;
    		itemPedido.vlTotalItemPedidoOld = itemPedido.vlTotalItemPedido;
    	}
    }

    public void updateVlRentabilidadeSaldo(ItemPedido itemPedido) throws SQLException {
    	if (itemPedido.vlRentabilidade >= 0 && !itemPedido.getProduto().isNeutro()) {
    		RentabilidadeSaldo rentabilidadeSaldo = getRentabilidadeSaldoPda(itemPedido.cdEmpresa, itemPedido.cdRepresentante);
    		rentabilidadeSaldo.vlTotalRentabilidade -= itemPedido.vlRentabilidadeOld;
    		rentabilidadeSaldo.vlTotalRentabilidade += itemPedido.vlRentabilidade;
    		rentabilidadeSaldo.vlTotalItens -= ValueUtil.round(itemPedido.vlTotalItemPedidoOld);
    		rentabilidadeSaldo.vlTotalItens += ValueUtil.round(itemPedido.vlItemPedido * itemPedido.getQtItemFisico());
			update(rentabilidadeSaldo);
			//--
    		itemPedido.vlRentabilidadeOld = itemPedido.vlRentabilidade;
    		itemPedido.vlTotalItemPedidoOld = itemPedido.vlTotalItemPedido;
    	}
    }

    public void deleteVlRentabilidadeSaldo(ItemPedido itemPedido) throws SQLException {
    	if (itemPedido.vlRentabilidade >= 0 && !itemPedido.getProduto().isNeutro()) {
    		RentabilidadeSaldo rentabilidadeSaldo = getRentabilidadeSaldoPda(itemPedido.cdEmpresa, itemPedido.cdRepresentante);
    		rentabilidadeSaldo.vlTotalRentabilidade -= itemPedido.vlRentabilidadeOld;
    		rentabilidadeSaldo.vlTotalItens -= itemPedido.vlTotalItemPedidoOld;
			update(rentabilidadeSaldo);
    	}
    }

    public void updateRentabilidadeSaldoPda2Erp(Pedido pedido) throws SQLException {
		int size = pedido.itemPedidoList.size();
        for (int i = 0; i < size; i++) {
    		ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
    		if (itemPedido.getProduto().isNeutro() || itemPedido.isFazParteKitFechado()) {
    			continue;
    		}
    		// --------------------------------
    		RentabilidadeSaldo rentabilidadeSaldoFilter = new RentabilidadeSaldo();
    		rentabilidadeSaldoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		rentabilidadeSaldoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    		Vector rentabilidadeSaldoList = findAllByExample(rentabilidadeSaldoFilter);
    		if (rentabilidadeSaldoList != null) {
    			int size2 = rentabilidadeSaldoList.size();
        		for (int  j = 0; j < size2; j++) {
        			RentabilidadeSaldo rentabilidadeSaldo = (RentabilidadeSaldo) rentabilidadeSaldoList.items[j];
        			if (RentabilidadeSaldo.RENTABILIDADESALDO_PDA.equals(rentabilidadeSaldo.flOrigemSaldo)) {
        				rentabilidadeSaldo.vlTotalRentabilidade -= itemPedido.vlRentabilidade;
        				rentabilidadeSaldo.vlTotalItens -= itemPedido.vlTotalItemPedido;
        				update(rentabilidadeSaldo);
        			} else if (RentabilidadeSaldo.RENTABILIDADESALDO_ERP.equals(rentabilidadeSaldo.flOrigemSaldo)) {
        				rentabilidadeSaldo.vlTotalRentabilidade += itemPedido.vlRentabilidade;
        				rentabilidadeSaldo.vlTotalItens += itemPedido.vlTotalItemPedido;
        				update(rentabilidadeSaldo);
        			}
        		}
    		}
    	}
    }

    public void recalculateAndUpdateRentabilidadeSaldoPda() throws SQLException {
    	RentabilidadeSaldo rentabilidadeSaldoFilter = new RentabilidadeSaldo();
    	rentabilidadeSaldoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	rentabilidadeSaldoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	Vector listFull = findAllByExample(rentabilidadeSaldoFilter);
    	Vector listVerbaSaldoPda = new Vector();
    	//--
		int size = listFull.size();
        for (int i = 0; i < size; i++) {
    		RentabilidadeSaldo verbaSaldo = (RentabilidadeSaldo) listFull.items[i];
    		if (RentabilidadeSaldo.RENTABILIDADESALDO_PDA.equals(verbaSaldo.flOrigemSaldo)) {
    			listVerbaSaldoPda.addElement(verbaSaldo);
    		}
    	}
    	//--
		int sizeVS = listVerbaSaldoPda.size();
        for (int i = 0; i < sizeVS; i++) {
        	RentabilidadeSaldo verbaSaldoPda = (RentabilidadeSaldo) listVerbaSaldoPda.items[i];
    		delete(verbaSaldoPda);
    	}
    	//--
        Pedido pedidoFilter = new Pedido();
        pedidoFilter.cdStatusPedidoDif = LavenderePdaConfig.cdStatusPedidoTransmitido;
        Vector pedidoList = PedidoPdbxDao.getInstance().findAllPedidosPdaResumido(pedidoFilter);
		int sizePedidoList = pedidoList.size();
        for (int i = 0; i < sizePedidoList; i++) {
    		Pedido pedido = (Pedido) pedidoList.items[i];
    		PedidoService.getInstance().findItemPedidoList(pedido);
    		Vector itemPedidoList = pedido.itemPedidoList;
    		int itemSize = itemPedidoList.size();
    		for (int j = 0; j < itemSize; j++) {
    			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[j];
    			insertVlRentabilidadeSaldo(itemPedido);
    		}
    	}
    }

}