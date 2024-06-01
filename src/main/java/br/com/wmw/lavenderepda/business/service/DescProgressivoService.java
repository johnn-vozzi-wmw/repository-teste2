package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescProgressivo;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescProgressivoPdbxDao;
import totalcross.util.Vector;

public class DescProgressivoService extends CrudService {

    private static DescProgressivoService instance;

    private DescProgressivoService() {
        //--
    }

    public static DescProgressivoService getInstance() {
        if (instance == null) {
            instance = new DescProgressivoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return DescProgressivoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public DescProgressivo getDescProgressivoPedido(Pedido pedido) throws SQLException {
    	if (LavenderePdaConfig.anulaDescontoPessoaFisica && pedido.getCliente().isPessoaFisica()) {
    		return null;
    	}
    	DescProgressivo dscProgressivo = new DescProgressivo();
		dscProgressivo.cdEmpresa = SessionLavenderePda.cdEmpresa;
		dscProgressivo.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		dscProgressivo.vlInicioFaixa = pedido.vlTotalItens;;
		dscProgressivo.vlFinalFaixa = pedido.vlTotalItens;;
		if (LavenderePdaConfig.usaDescontoProgressivoPorTabelaPreco) {
			dscProgressivo.cdTabelaPreco = pedido.cdTabelaPreco;
		}
		if (LavenderePdaConfig.usaDescontoProgressivoPorCondComercial) {
			dscProgressivo.cdCondicaoComercial = pedido.cdCondicaoComercial;
		}
		Vector vector = findAllByExample(dscProgressivo);
		if (vector.size() > 0) {
			return (DescProgressivo)vector.items[0];
		} else {
			return null;
		}
    }


    public DescProgressivo getDescProgressivoItemPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
    	if (LavenderePdaConfig.anulaDescontoPessoaFisica && pedido.getCliente().isPessoaFisica()) {
    		return null;
    	}
    	double vlTotalPedido = 0;
		ItemPedido itemPedidoTemp;
		int size = pedido.itemPedidoList.size();
    	for (int i = 0; i < size; i++) {
    		itemPedidoTemp = (ItemPedido)pedido.itemPedidoList.items[i];
    		if (!itemPedidoTemp.equals(itemPedido)) {
    			vlTotalPedido += itemPedidoTemp.vlTotalItemPedido;
    		}
    	}
    	//--
    	DescProgressivo dscProgressivo = new DescProgressivo();
		dscProgressivo.cdEmpresa = SessionLavenderePda.cdEmpresa;
		dscProgressivo.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		dscProgressivo.vlInicioFaixa = (vlTotalPedido + itemPedido.vlTotalItemPedido) + 0.1;
		dscProgressivo.vlFinalFaixa = (vlTotalPedido + itemPedido.vlTotalItemPedido) + 0.1;
		if (LavenderePdaConfig.usaDescontoProgressivoPorTabelaPreco) {
			dscProgressivo.cdTabelaPreco = pedido.cdTabelaPreco;
		}
		if (LavenderePdaConfig.usaDescontoProgressivoPorCondComercial) {
			dscProgressivo.cdCondicaoComercial = pedido.cdCondicaoComercial;
		}
		Vector vector = findAllByExample(dscProgressivo);
		if (vector.size() > 0) {
			return (DescProgressivo)vector.items[0];
		} else {
			return null;
		}
    }

}