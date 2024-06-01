package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.DescontoCCP;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescontoCCPPdbxDao;

public class DescontoCCPService extends CrudService {

    private static DescontoCCPService instance;

    private DescontoCCPService() {
        //--
    }

    public static DescontoCCPService getInstance() {
        if (instance == null) {
            instance = new DescontoCCPService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return DescontoCCPPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public double getDescCCPItemPedido(ItemPedido itemPedido, Cliente cliente) throws SQLException {
    	if (LavenderePdaConfig.anulaDescontoPessoaFisica && cliente.isPessoaFisica()) {
    		return 0;
    	}
    	if ((cliente != null) && (itemPedido.getProduto() != null)) {
	    	DescontoCCP dscCCP = new DescontoCCP();
			dscCCP.cdEmpresa = SessionLavenderePda.cdEmpresa;
			dscCCP.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			dscCCP.cdClassecliente = cliente.cdCategoria;
			dscCCP.cdClasseProduto= itemPedido.getProduto().cdClasse;
			if (dscCCP.cdEmpresa != null && dscCCP.cdRepresentante != null && dscCCP.cdClassecliente != null && dscCCP.cdClasseProduto != null) {
				return ValueUtil.getDoubleValue(findColumnByRowKey(dscCCP.getRowKey(), "vlPctDesconto"));
			} else {
				return 0;
			}
    	} else {
    		return 0;
    	}
    }



}