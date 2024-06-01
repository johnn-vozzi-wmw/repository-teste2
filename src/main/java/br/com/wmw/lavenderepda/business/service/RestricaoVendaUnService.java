package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.RestricaoVendaUn;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.validation.RestricaoVendaUnException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RestricaoVendaUnDbxDao;
import totalcross.util.Vector;

public class RestricaoVendaUnService extends CrudService {

    private static RestricaoVendaUnService instance;

    private RestricaoVendaUnService() { }

    public static RestricaoVendaUnService getInstance() {
        if (instance == null) {
            instance = new RestricaoVendaUnService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return RestricaoVendaUnDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public void validateUnidadeRestritaItensDoPedido(Pedido pedido) throws SQLException {
    	int size = pedido.itemPedidoList.size();
    	ItemPedido itemPedido;
    	for (int i = 0; i < size; i++) {
			itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
			if (isUnidadeRestrita(itemPedido, itemPedido.cdUnidade, pedido.cdTipoPedido)) {
				throw new RestricaoVendaUnException(Messages.TIPOPEDIDO_MSG_PRODUTOS_INADEQUADOS);
			}
		}
    }

    public Vector getItensNaoConformeByRestricaoVendaUn(Pedido pedido, TipoPedido tipoPedidoNew) throws SQLException {
    	Vector itensNaoConforme = new Vector();
		int size = pedido.itemPedidoList.size();
		ItemPedido itemPedido;
		for (int i = 0; i < size; i++) {
			itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (isUnidadeRestrita(itemPedido, itemPedido.cdUnidade, tipoPedidoNew.cdTipoPedido)) {
				itensNaoConforme.addElement(itemPedido);
			}
		}
		return itensNaoConforme;
    }

	public boolean isUnidadeRestrita(ItemPedido itemPedido, String cdUnidade, String cdTipoPedido) throws SQLException {
		RestricaoVendaUn restricaoVendaUnFilter = new RestricaoVendaUn();
		if (LavenderePdaConfig.usaRestricaoVendaProdutoPorUnidade) {
			restricaoVendaUnFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			restricaoVendaUnFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(RestricaoVendaUn.class);
			restricaoVendaUnFilter.cdUnidade = cdUnidade;
			restricaoVendaUnFilter.cdProduto = itemPedido.cdProduto;
			restricaoVendaUnFilter.flBloqueiaVenda = ValueUtil.VALOR_SIM;
			restricaoVendaUnFilter.cdTabelaPrecoOrFilter = itemPedido.cdTabelaPreco;
			if (LavenderePdaConfig.usaOportunidadeVenda && itemPedido.isOportunidade()) {
				TipoPedido tipoPedido = TipoPedidoService.getInstance().findTipoPedidoOportunidade(itemPedido.cdEmpresa,
						itemPedido.cdRepresentante);
				if (tipoPedido != null) {
					restricaoVendaUnFilter.cdTipoPedidoOrFilter = tipoPedido.cdTipoPedido;
				} else {
					throw new ValidationException(Messages.OPORTUNIDADE_TIPO_PEDIDO_OPORTUNIDADE_NAO_ENCONTRADO);
				}
			} else {
				restricaoVendaUnFilter.cdTipoPedidoOrFilter = cdTipoPedido;
			}
			if (itemPedido.pedido != null) {
				restricaoVendaUnFilter.cdClienteOrFilter = itemPedido.pedido.cdCliente;
			}
			return countByExample(restricaoVendaUnFilter) > 0;
		}
		return false;
	}
    
}