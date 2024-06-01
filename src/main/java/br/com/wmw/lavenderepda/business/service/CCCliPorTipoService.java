package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CCCliPorTipo;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CCCliPorTipoPdbxDao;
import totalcross.util.Vector;

public class CCCliPorTipoService extends CrudService {

    private static CCCliPorTipoService instance;

    private CCCliPorTipoService() {
        //--
    }

    public static CCCliPorTipoService getInstance() {
        if (instance == null) {
            instance = new CCCliPorTipoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CCCliPorTipoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {

        CCCliPorTipo cCCliPorTipo = (CCCliPorTipo) domain;

        //cdEmpresa
        if (ValueUtil.isEmpty(cCCliPorTipo.cdEmpresa)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.EMPRESA_NOME_ENTIDADE);
        }
        //cdRepresentante
        if (ValueUtil.isEmpty(cCCliPorTipo.cdRepresentante)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.REPRESENTANTE_NOME_ENTIDADE);
        }
        //cdCliente
        if (ValueUtil.isEmpty(cCCliPorTipo.cdCliente)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO,  Messages.CLIENTE_NOME_ENTIDADE);
        }
        //nuDocumento
        if (ValueUtil.isEmpty(cCCliPorTipo.nuDocumento)) {
            throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO, Messages.CCCLIPORTIPO_LABEL_NUDOCUMENTO);
        }
    }

	public Vector getCCCliPorTipoMaiAntigo(boolean usaNuPedidoNaBusca , String nuPedido) throws SQLException {
		CCCliPorTipo cCCliPorTipo = new CCCliPorTipo();
		cCCliPorTipo.cdEmpresa = SessionLavenderePda.cdEmpresa;
		cCCliPorTipo.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		cCCliPorTipo.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		cCCliPorTipo.usaFindPedidoVazio = !usaNuPedidoNaBusca;
		if (usaNuPedidoNaBusca && !ValueUtil.isEmpty(nuPedido)) {
			cCCliPorTipo.nuPedido = nuPedido;
		}
		Vector cCCliPorTipoList =  CCCliPorTipoService.getInstance().findAllByExample(cCCliPorTipo);
		//--
		Vector cCCliPorTipoFinalList = new Vector(2);
		CCCliPorTipo cCCliPeso = new CCCliPorTipo();
		CCCliPorTipo cCCliQuebraAndMort = new CCCliPorTipo();
    	int size = cCCliPorTipoList.size();
    	boolean checkedQuebraAndMort = false;
    	for (int i = 0; i < size; i++) {
    		cCCliPorTipo = (CCCliPorTipo)cCCliPorTipoList.items[i];
    		if (cCCliPorTipo.vlPeso != 0 && cCCliPeso.vlPeso != 0) {
    			cCCliPeso.vlPeso = cCCliPorTipo.vlPeso;
    			cCCliPeso.dtDocumento = cCCliPorTipo.dtDocumento;
    			cCCliPeso.nuDocumento = cCCliPorTipo.nuDocumento;
    			cCCliPeso.cdEmpresa = cCCliPorTipo.cdEmpresa;
    			cCCliPeso.cdRepresentante = cCCliPorTipo.cdRepresentante;
    			cCCliPeso.cdCliente = cCCliPorTipo.cdCliente;
    		}
    		if (((cCCliPorTipo.vlQuebra != 0 && cCCliQuebraAndMort.vlQuebra != 0) || (cCCliPorTipo.vlMortalidade != 0 && cCCliQuebraAndMort.vlMortalidade != 0)) && !checkedQuebraAndMort) {
    			cCCliQuebraAndMort.vlQuebra = cCCliPorTipo.vlQuebra;
    			cCCliQuebraAndMort.vlMortalidade = cCCliPorTipo.vlMortalidade;
    			cCCliQuebraAndMort.dtDocumento = cCCliPorTipo.dtDocumento;
    			cCCliQuebraAndMort.nuDocumento = cCCliPorTipo.nuDocumento;
    			cCCliQuebraAndMort.cdEmpresa = cCCliPorTipo.cdEmpresa;
    			cCCliQuebraAndMort.cdRepresentante = cCCliPorTipo.cdRepresentante;
    			cCCliQuebraAndMort.cdCliente = cCCliPorTipo.cdCliente;
    			checkedQuebraAndMort = true;
    		}
    		if (cCCliPeso.vlPeso != 0 && (cCCliQuebraAndMort.vlQuebra != 0 || cCCliQuebraAndMort.vlMortalidade != 0)) {
    			break;
    		}
    	}
		cCCliPorTipoFinalList.addElement(cCCliPeso);
		cCCliPorTipoFinalList.addElement(cCCliQuebraAndMort);
		//--
    	cCCliPorTipoList = null;
    	cCCliQuebraAndMort = null;
    	cCCliPeso = null;
    	//--
		return cCCliPorTipoFinalList;
	}

	public void updateCCCliPorTipoByPedido(Pedido pedido) throws SQLException {
		CCCliPorTipo cCCliPorTipoPeso = new CCCliPorTipo();
		cCCliPorTipoPeso.cdEmpresa = SessionLavenderePda.cdEmpresa;
		cCCliPorTipoPeso.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		cCCliPorTipoPeso.cdCliente = pedido.cdCliente;
		cCCliPorTipoPeso.nuPedido = pedido.nuPedido;
		Vector cccliPorTipoList = CCCliPorTipoService.getInstance().findAllByExample(cCCliPorTipoPeso);
    	int size = cccliPorTipoList.size();
    	for (int i = 0; i < size; i++) {
    		cCCliPorTipoPeso = (CCCliPorTipo)cccliPorTipoList.items[i];
    		cCCliPorTipoPeso.nuPedido = "";
    		CCCliPorTipoService.getInstance().update(cCCliPorTipoPeso);
    	}
	}

	public void updateCCCliPorTipoAoInserirItemPedido(String nuPedido) throws SQLException {
		Vector vetor  = CCCliPorTipoService.getInstance().getCCCliPorTipoMaiAntigo(false , null);
		CCCliPorTipo cCCliPorTipoPeso = (CCCliPorTipo)vetor.items[0];
		if (!ValueUtil.isEmpty(cCCliPorTipoPeso.nuDocumento) && !ValueUtil.isEmpty(cCCliPorTipoPeso.cdCliente)) {
			cCCliPorTipoPeso.nuPedido = nuPedido;
			CCCliPorTipoService.getInstance().update(cCCliPorTipoPeso);
		}
		//--
		CCCliPorTipo cCCliPorTipoQuebraAndMort = (CCCliPorTipo)vetor.items[1];
		if (!ValueUtil.isEmpty(cCCliPorTipoQuebraAndMort.nuDocumento) && !ValueUtil.isEmpty(cCCliPorTipoQuebraAndMort.cdCliente)) {
			cCCliPorTipoQuebraAndMort.nuPedido = nuPedido;
			CCCliPorTipoService.getInstance().update(cCCliPorTipoQuebraAndMort);
		}
	}
	//--
	public void validateBasedOnTheFirstItem(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		Vector grupoProdutoList = new Vector(StringUtil.split(LavenderePdaConfig.nuGrupoProdutoForaDaCCClientePorTipoPedido,';'));
		ItemPedido itemPedidoExemplo = (ItemPedido)pedido.itemPedidoList.items[0];
		if (grupoProdutoList.indexOf(StringUtil.getStringValue(itemPedidoExemplo.getProduto().cdGrupoProduto1)) == -1) {
			if ((grupoProdutoList.indexOf(StringUtil.getStringValue(itemPedido.getProduto().cdGrupoProduto1)) != -1)) {
				throw new ValidationException(Messages.CCCLIPORTIPO_MSG_GRUPO_NAO_COMPATIVEL);
			}
		} else {
			if (grupoProdutoList.indexOf(StringUtil.getStringValue(itemPedido.getProduto().cdGrupoProduto1)) == -1) {
				throw new ValidationException(Messages.CCCLIPORTIPO_MSG_GRUPO_NAO_COMPATIVEL);
			}
		}
	}
	//--
	public void updateCCCliPorTipoIfIsFirstItem(Pedido pedido,ItemPedido itemPedido) throws SQLException {
		String param = !ValueUtil.isEmpty(LavenderePdaConfig.nuGrupoProdutoForaDaCCClientePorTipoPedido) ? LavenderePdaConfig.nuGrupoProdutoForaDaCCClientePorTipoPedido : "";
		Vector grupoProdutoList = new Vector(StringUtil.split(param,';'));
		if (grupoProdutoList.indexOf(StringUtil.getStringValue(itemPedido.getProduto().cdGrupoProduto1)) == -1) {
			CCCliPorTipoService.getInstance().updateCCCliPorTipoAoInserirItemPedido(pedido.nuPedido);
		}
	}
	//--
	public void updateCCCliPorTipoIfIsLastItem(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		String param = !ValueUtil.isEmpty(LavenderePdaConfig.nuGrupoProdutoForaDaCCClientePorTipoPedido) ? LavenderePdaConfig.nuGrupoProdutoForaDaCCClientePorTipoPedido : "";
		Vector grupoProdutoList = new Vector(StringUtil.split(param,';'));
		if (grupoProdutoList.indexOf(StringUtil.getStringValue(itemPedido.getProduto().cdGrupoProduto1)) == -1) {
			CCCliPorTipoService.getInstance().updateCCCliPorTipoByPedido(pedido);
		}
	}
}