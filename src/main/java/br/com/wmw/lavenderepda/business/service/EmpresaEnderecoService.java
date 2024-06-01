package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ClienteEndereco;
import br.com.wmw.lavenderepda.business.domain.EmpresaEndereco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.EmpresaEnderecoDbxDao;
import totalcross.util.Date;

public class EmpresaEnderecoService extends CrudService {

    private static EmpresaEnderecoService instance;
    
    private EmpresaEnderecoService() {
        //--
    }
    
    public static EmpresaEnderecoService getInstance() {
        if (instance == null) {
            instance = new EmpresaEnderecoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return EmpresaEnderecoDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    public boolean isDtPrevisaoEntregaInvalidaForEmpresaEndereco(Pedido pedido) throws SQLException {
    	return isDtPrevisaoEntregaInvalidaForEmpresaEndereco(pedido, null);
    }
    
    public boolean isDtPrevisaoEntregaInvalidaForEmpresaEndereco(Pedido pedido, Date dtEntrega) throws SQLException {
    	if (pedido != null) {
    		EmpresaEndereco empresaEndereco = getEmpresaEnderecoByClientePedido(pedido);
    		if (empresaEndereco == null) {
    			return false;
    		}
    		if (empresaEndereco.dsDiasEntrega == null) {
    			return false;
    		}
    		String[] dsDiasEntrega = empresaEndereco.dsDiasEntrega.split(";");
    		if (dsDiasEntrega.length != 7) {
    			return false;
    		}
    		dtEntrega = dtEntrega == null ? pedido.dtEntrega : dtEntrega;
    		int diaDaSemana = DateUtil.getDayOfWeek(dtEntrega);
    		return !ValueUtil.VALOR_SIM.equalsIgnoreCase(dsDiasEntrega[diaDaSemana]);
    	}
    	return true;
    }
    
    
    public boolean isExisteDiaPossivel(Pedido pedido) throws SQLException {
    	if (pedido != null) {
    		EmpresaEndereco empresaEndereco = getEmpresaEnderecoByClientePedido(pedido);
    		if (empresaEndereco == null) {
    			return false;
    		}
    		if (empresaEndereco.dsDiasEntrega == null) {
    			return false;
    		}
    		String[] dsDiasEntrega = empresaEndereco.dsDiasEntrega.split(";");
    		if (dsDiasEntrega.length != 7) {
    			return false;
    		}
    		for (String diaDaSemanaSimOuNao : dsDiasEntrega) {
    			if (diaDaSemanaSimOuNao.equals(ValueUtil.VALOR_SIM)) {
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    private EmpresaEndereco getEmpresaEnderecoByClientePedido(Pedido pedido) throws SQLException {
    	if (pedido.getCliente() != null) {
    		EmpresaEndereco empresaEndereco = null;
    		ClienteEndereco clienteEndereco = null;
    		if (LavenderePdaConfig.getConfigUsaEnderecoEntregaPedido() > 0) {
    			clienteEndereco = ClienteEnderecoService.getInstance().getClienteEnderecoPedido(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdCliente, pedido.cdEnderecoCliente);
    		}
    		EmpresaEndereco empresaEnderecoFilter = new EmpresaEndereco();
    		empresaEnderecoFilter.cdEmpresa = pedido.getCliente().cdEmpresa;
    		if (clienteEndereco != null) {
    			empresaEnderecoFilter.dsBairro = clienteEndereco.dsBairro;
    			empresaEnderecoFilter.dsCidade = clienteEndereco.dsCidade;
    		} else {
    			empresaEnderecoFilter.dsBairro = pedido.getCliente().dsBairroComercial;
    			empresaEnderecoFilter.dsCidade = pedido.getCliente().dsCidadeComercial;
    		}
    		empresaEndereco = (EmpresaEndereco) findByRowKey(empresaEnderecoFilter.getRowKey());
    		if (empresaEndereco == null) {
    			empresaEnderecoFilter.dsBairro = ValueUtil.VALOR_ZERO;
    			empresaEndereco = (EmpresaEndereco) findByRowKey(empresaEnderecoFilter.getRowKey());
    			if (empresaEndereco == null) {
    				empresaEnderecoFilter.dsCidade = ValueUtil.VALOR_ZERO;
    				empresaEndereco = (EmpresaEndereco) findByRowKey(empresaEnderecoFilter.getRowKey());
    			}
    		}
    		return empresaEndereco;
    	}
    	return null;
    }
    
    public EmpresaEndereco findEmpresaEndereco(String cdEmpresa) throws SQLException {
    	return findEmpresaEndereco(cdEmpresa, ValueUtil.VALOR_ZERO, ValueUtil.VALOR_ZERO);
    }
    
    public EmpresaEndereco findEmpresaEndereco(String cdEmpresa, String dsBairro, String dsCidade) throws SQLException {
    	EmpresaEndereco empresaEndereco;
    	EmpresaEndereco empresaEnderecoFilter = new EmpresaEndereco();
    	empresaEnderecoFilter.cdEmpresa = cdEmpresa;
    	empresaEnderecoFilter.dsBairro = StringUtil.trimWhitespace(dsBairro);
    	empresaEnderecoFilter.dsCidade = StringUtil.trimWhitespace(dsCidade);
    	empresaEndereco = (EmpresaEndereco) findByRowKey(empresaEnderecoFilter.getRowKey());
    	if (empresaEndereco == null) {
    		empresaEnderecoFilter.dsBairro = ValueUtil.VALOR_ZERO;
    		empresaEndereco = (EmpresaEndereco) findByRowKey(empresaEnderecoFilter.getRowKey());
    		if (empresaEndereco == null) {
    			empresaEnderecoFilter.dsCidade = ValueUtil.VALOR_ZERO;
    			empresaEndereco = (EmpresaEndereco) findByRowKey(empresaEnderecoFilter.getRowKey());
    		}
    	}
    	if (empresaEndereco == null) {
    		empresaEndereco = new EmpresaEndereco();
    		empresaEndereco.dsDiasEntrega = EmpresaEndereco.DSDIASENTREGA_NENHUM;
    	}
    	return empresaEndereco;
    }

}