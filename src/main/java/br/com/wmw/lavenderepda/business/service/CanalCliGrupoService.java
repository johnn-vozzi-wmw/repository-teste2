package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CanalCliGrupo;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto1;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CanalCliGrupoDao;

public class CanalCliGrupoService extends CrudService {

    private static CanalCliGrupoService instance;
    
    private CanalCliGrupoService() {
        //--
    }
    
    public static CanalCliGrupoService getInstance() {
        if (instance == null) {
            instance = new CanalCliGrupoService();
        }
        return instance;
    }
    
    //@Override
    protected CrudDao getCrudDao() {
    	return CanalCliGrupoDao.getInstance();
    }
    
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    public double getDescontoCanalCliGrupo(ItemPedido itemPedido, Cliente cliente) throws SQLException {
    	double vlPctDesconto = 0;
    	CanalCliGrupo canalCliGrupo = new CanalCliGrupo();
    	canalCliGrupo.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	canalCliGrupo.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	canalCliGrupo.cdCanalCliente = cliente.cdCanalCliente;
    	if (itemPedido.getProduto() != null && isProdutoPossuiDescontoCanalCliGrupoEspecifico(itemPedido.getProduto(), cliente)) {
    		GrupoProduto1 grupoProduto1Filter = new GrupoProduto1();
    		grupoProduto1Filter.cdGrupoproduto1 = itemPedido.getProduto().cdGrupoProduto1;
    		canalCliGrupo.cdCanalGrupo = GrupoProduto1Service.getInstance().findColumnByRowKey(grupoProduto1Filter.getRowKey(), "CDCANALGRUPO");
		} else {
			canalCliGrupo.cdCanalGrupo = GrupoProduto1.CD_GRUPO_PRODUTO_VAZIO;
		}
    	vlPctDesconto = ValueUtil.getDoubleValue(findColumnByRowKey(canalCliGrupo.getRowKey(), "vlPctMaxDesconto"));
    	return ValueUtil.round(vlPctDesconto);
    }
    
    public boolean isProdutoPossuiDescontoCanalCliGrupoEspecifico(Produto produto, Cliente cliente) throws SQLException {
    	if (ValueUtil.isEmpty(produto.cdGrupoProduto1) || GrupoProduto1.CD_GRUPO_PRODUTO_VAZIO.equals(produto.cdGrupoProduto1)) {
			return false;
		}
    	CanalCliGrupo canalCliGrupo = new CanalCliGrupo();
    	canalCliGrupo.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	canalCliGrupo.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    	canalCliGrupo.cdCanalCliente = cliente.cdCanalCliente;
    	GrupoProduto1 grupoProduto1Filter = new GrupoProduto1();
		grupoProduto1Filter.cdGrupoproduto1 = produto.cdGrupoProduto1;
    	canalCliGrupo.cdCanalGrupo = GrupoProduto1Service.getInstance().findColumnByRowKey(grupoProduto1Filter.getRowKey(), "CDCANALGRUPO");
    	if (ValueUtil.isEmpty(canalCliGrupo.cdCanalGrupo)) {
			return false;
		}
    	canalCliGrupo = (CanalCliGrupo) findByRowKey(canalCliGrupo.getRowKey());
    	return canalCliGrupo != null && ValueUtil.isNotEmpty(canalCliGrupo.cdCanalGrupo) && !GrupoProduto1.CD_GRUPO_PRODUTO_VAZIO.equals(canalCliGrupo.cdCanalGrupo);
    }

    public double calculaDescontoMaximoCanalEContratoCliente(ItemPedido itemPedido, double vlPctDescCanal, double vlPctContratoCli) {
    	double vlPctDescontoMaximoCanal = 0;
    	double valorItemComDesconto = itemPedido.vlBaseItemTabelaPreco * (1 - (vlPctDescCanal / 100));
    	if (ValueUtil.round(vlPctDescCanal + vlPctContratoCli) > ValueUtil.round(LavenderePdaConfig.vlPctLimiteSomaCanalContratoDecisaoCalculo) && ValueUtil.round(vlPctContratoCli) < ValueUtil.round(LavenderePdaConfig.vlPctLimiteContratoDecisaoCalculoDesc)) {
    		valorItemComDesconto *= 1 + ((vlPctDescCanal + vlPctContratoCli - LavenderePdaConfig.vlPctLimiteSomaCanalContratoDecisaoCalculo) / 100);
    	} 
    	vlPctDescontoMaximoCanal =  ItemPedidoService.getInstance().calculaVlPctDesconto(itemPedido.vlBaseItemTabelaPreco, ValueUtil.round(valorItemComDesconto));
    	return ValueUtil.round(vlPctDescontoMaximoCanal);
    }
    
    public double calculaFormulaA(ItemPedido itemPedido) {
    	return itemPedido.vlUnidadePadrao - (itemPedido.vlUnidadePadrao / 100 * itemPedido.vlPctDescontoCanal);
    }
    
    public double calculaFormulaB(ItemPedido itemPedido) throws SQLException {
		return itemPedido.getItemTabelaPreco().vlBase + ((itemPedido.getVlItemComTributos() + itemPedido.getVlTotalFrete()) / 100 * itemPedido.vlPctContratoCli);
    }
    
}