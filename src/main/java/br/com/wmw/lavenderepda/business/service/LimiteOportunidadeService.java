package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.LimiteOportunidade;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.LimiteOportunidadeDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import totalcross.util.Vector;

public class LimiteOportunidadeService extends CrudService {

    private static LimiteOportunidadeService instance;

    private LimiteOportunidadeService() {
        //--
    }

    public static LimiteOportunidadeService getInstance() {
        if (instance == null) {
            instance = new LimiteOportunidadeService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return LimiteOportunidadeDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

	public void validateSaldo(ItemPedido itemPedido) throws SQLException {
		double vlSaldo = getVlSaldo();
		int size = itemPedido.pedido.itemPedidoOportunidadeList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoAux = (ItemPedido) itemPedido.pedido.itemPedidoOportunidadeList.items[i];
			if (!itemPedidoAux.equals(itemPedido)) {
				vlSaldo -= itemPedidoAux.vlTotalItemPedido;
			}
		}
		vlSaldo += itemPedido.vlOportunidadeOld;
		if (vlSaldo - itemPedido.vlTotalItemPedido < 0) {
			throw new ValidationException(MessageUtil.getMessage(Messages.LIMITEOPORTUNIDADE_SALDO_INSUFICIENTE, new String[] {StringUtil.getStringValue(vlSaldo), StringUtil.getStringValue(itemPedido.vlTotalItemPedido)}));
		}
	}


	public boolean isPersisteLimiteVendaItensOportunidade(ItemPedido itemPedido) throws SQLException {
		return LavenderePdaConfig.usaControleSaldoOportunidade && LavenderePdaConfig.usaOportunidadeVenda &&
				itemPedido.pedido.isOportunidade();
	}

	public LimiteOportunidade getLimiteOportunidadeErp() throws SQLException {
		LimiteOportunidade limiteOportunidadeFilter = new LimiteOportunidade();
    	limiteOportunidadeFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	limiteOportunidadeFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	limiteOportunidadeFilter.flOrigemSaldo = LimiteOportunidade.LIMITE_PORTUNIDADE_ERP;
    	return (LimiteOportunidade) findByRowKey(limiteOportunidadeFilter.getRowKey());
	}

	public double getVlSaldo() throws SQLException {
    	LimiteOportunidade limiteOportunidadeErp = getLimiteOportunidadeErp();
    	LimiteOportunidade limiteOportunidadePda = getLimiteOportunidadePda();
    	if (limiteOportunidadePda == null) {
    		limiteOportunidadePda = createLimiteOportunidadePda();
    		limiteOportunidadePda.vlSaldo = 0;
			insert(limiteOportunidadePda);
    	}
    	double vlSaldoErp = 0;
    	double vlSaldoPda = 0;
    	if (limiteOportunidadeErp != null) {
    		vlSaldoErp = limiteOportunidadeErp.vlSaldo;
    	}
    	vlSaldoPda = limiteOportunidadePda.vlSaldo;
		return (vlSaldoErp + vlSaldoPda);
    }

	private LimiteOportunidade createLimiteOportunidadePda() {
		LimiteOportunidade limiteOportunidade = new LimiteOportunidade();
		limiteOportunidade.cdEmpresa = SessionLavenderePda.cdEmpresa;
		limiteOportunidade.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		limiteOportunidade.flOrigemSaldo = LimiteOportunidade.LIMITE_PORTUNIDADE_PDA;
		return limiteOportunidade;
	}

	public LimiteOportunidade getLimiteOportunidadePda() throws SQLException {
		return (LimiteOportunidade) findByRowKey(createLimiteOportunidadePda().getRowKey());
	}

	public void insertVlSaldo(ItemPedido itemPedido) throws SQLException {
		LimiteOportunidade limiteOportunidade = getLimiteOportunidadePda();
		if (limiteOportunidade != null) {
			limiteOportunidade.vlSaldo -= itemPedido.vlTotalItemPedido;
			update(limiteOportunidade);
		} else {
			limiteOportunidade = createLimiteOportunidadePda();
			limiteOportunidade.vlSaldo = (itemPedido.vlTotalItemPedido * -1);
			insert(limiteOportunidade);
		}
		itemPedido.vlOportunidadeOld = (itemPedido.vlTotalItemPedido);
	}


	public void updateVlSaldo(ItemPedido itemPedido) throws SQLException {
		LimiteOportunidade limiteOportunidade = getLimiteOportunidadePda();
		if (limiteOportunidade != null) {
			limiteOportunidade.vlSaldo += itemPedido.vlOportunidadeOld;
			limiteOportunidade.vlSaldo -= itemPedido.vlTotalItemPedido;
			update(limiteOportunidade);
		} else {
			limiteOportunidade = createLimiteOportunidadePda();
			limiteOportunidade.vlSaldo = (itemPedido.vlTotalItemPedido * -1);
			insert(limiteOportunidade);
		}
		itemPedido.vlOportunidadeOld = (itemPedido.vlTotalItemPedido);
	}


	public void deleteVlSaldo(ItemPedido itemPedido) throws SQLException {
		LimiteOportunidade limiteOportunidade = getLimiteOportunidadePda();
		if (limiteOportunidade != null) {
			limiteOportunidade.vlSaldo += itemPedido.vlOportunidadeOld;
			update(limiteOportunidade);
		}
	}

	public void updateSaldoLimiteOportunidadeForPedidosEnviados(Pedido pedido) throws SQLException {
		if (pedido.isOportunidade()) {
			LimiteOportunidade limiteOportunidadeErp = getLimiteOportunidadeErp();
			LimiteOportunidade limiteOportunidadePda = getLimiteOportunidadePda();
			if (limiteOportunidadeErp != null && limiteOportunidadePda != null) {
				limiteOportunidadeErp.vlSaldo -= pedido.vlTotalItens;
				update(limiteOportunidadeErp);
				limiteOportunidadePda.vlSaldo += pedido.vlTotalItens;
				update(limiteOportunidadePda);
			}
		}
	}

	public void recalculateAndUpdateLimiteOportunidadePda() throws SQLException {
		LimiteOportunidade limiteOportunidadeFilter = createLimiteOportunidadePda();
		deleteAllByExample(limiteOportunidadeFilter);
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdStatusPedidoDif = LavenderePdaConfig.cdStatusPedidoTransmitido;
		Vector pedidoList = PedidoPdbxDao.getInstance().findAllPedidosPdaResumido(pedidoFilter);
		int sizePedidoList = pedidoList.size();
        for (int i = 0; i < sizePedidoList; i++) {
    		Pedido pedido = (Pedido) pedidoList.items[i];
    		if (!pedido.isOportunidade()) {
    			continue;
    		}
    		PedidoService.getInstance().findItemPedidoList(pedido);
    		Vector itemPedidoList = pedido.itemPedidoList;
    		int itemSize = itemPedidoList.size();
    		for (int j = 0; j < itemSize; j++) {
    			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[j];
    			insertVlSaldo(itemPedido);
    		}
    	}
    }


}