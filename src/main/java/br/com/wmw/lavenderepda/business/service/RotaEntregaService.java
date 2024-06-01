package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CargaPedido;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.RotaEntrega;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RotaEntregaPdbxDao;
import totalcross.util.Date;
import totalcross.util.Vector;

public class RotaEntregaService extends CrudService {

    private static RotaEntregaService instance;

    private RotaEntregaService() {
        //--
    }

    public static RotaEntregaService getInstance() {
        if (instance == null) {
            instance = new RotaEntregaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return RotaEntregaPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    public Vector findAllRotasEntregaCliente2Combo(String cdCliente) throws SQLException {
		if ("2".equals(LavenderePdaConfig.usaRotaDeEntregaNoPedidoComCadastro) || "3".equals(LavenderePdaConfig.usaRotaDeEntregaNoPedidoComCadastro)  || LavenderePdaConfig.permiteCadastrarCargaNaCapaDoPedido) {
			return filterRotaEntregaPorCliente(cdCliente);
		}
		return findAllRotaEntrega();
    }

	public Vector findAllRotaEntrega() throws SQLException {
		RotaEntrega rotaEntrega = new RotaEntrega();
		rotaEntrega.cdEmpresa = SessionLavenderePda.cdEmpresa;
		rotaEntrega.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(RotaEntrega.class);
		if (LavenderePdaConfig.isUsaRotaDeEntregaPadraoDoCliente()) {
			rotaEntrega.cdRotaEntrega = SessionLavenderePda.getCliente().cdRotaEntrega;
		}
		Vector list = findAllByExample(rotaEntrega);
		return list;
	}

	private Vector filterRotaEntregaPorCliente(String cdCliente) throws SQLException {
		return RotaEntregaPdbxDao.getInstance().filterRotaEntregaPorCliente(cdCliente);
	}

	public RotaEntrega getRotaEntregaByCargaPedido(CargaPedido cargaPedido) throws SQLException {
		RotaEntrega rotaEntregaFilter = new RotaEntrega();
		rotaEntregaFilter.cdEmpresa = cargaPedido.cdEmpresa;
		rotaEntregaFilter.cdRepresentante = cargaPedido.cdRepresentante;
		rotaEntregaFilter.cdRotaEntrega = cargaPedido.cdRotaEntrega;
		return (RotaEntrega) findByRowKey(rotaEntregaFilter.getRowKey());
	}

	public void validaRotaEntregaPadraoConfigurada(Cliente cliente) throws SQLException {
		if (!LavenderePdaConfig.isUsaRotaDeEntregaPadraoDoCliente()) return;
		
		if (ValueUtil.isEmpty(cliente.cdRotaEntrega) || RotaEntregaPdbxDao.getInstance().countRotaEntregaPorCliente(cliente) == 0) {
			throw new ValidationException(Messages.ROTA_ENTREGA_CLIENTE_SEM_ROTA_CADASTRADA);
		}
	}
	
	public Date getSugestaoDataEntregaBaseadaEmUmaRota(Date dataEntrega, Cliente cliente) throws SQLException {
		RotaEntrega rotaEntregaFilter = new RotaEntrega(cliente.cdEmpresa, cliente.cdRepresentante, cliente.cdRotaEntrega, null, null);
		String diasEntregaRota = findColumnByRowKey(rotaEntregaFilter.getRowKey(), "DSDIASENTREGA");
		return getSugestaoDataEntregaBaseadaEmDiaDisponivel(dataEntrega, diasEntregaRota);
	}

	protected Date getSugestaoDataEntregaBaseadaEmDiaDisponivel(Date dataEntrega, String diasEntregaRota) throws SQLException {
		if (!LavenderePdaConfig.isUsaRotaDeEntregaPadraoDoCliente() || ValueUtil.isEmpty(diasEntregaRota)) return dataEntrega;
		
		return getDataEntregaDisponivel(dataEntrega, diasEntregaRota);
	}
	

	protected Date getDataEntregaDisponivel(Date dataEntrega, String diasEntregaRota) throws SQLException {
		List<String> diaSemanaList =  Arrays.asList(diasEntregaRota.split(ValueUtil.VALOR_EMBRANCO));
		if (!isPossuiInformacoesValidas(diaSemanaList)) return dataEntrega;
		
		int size = diaSemanaList.size();
		for (int i = 0; i < size; i++) {
			if (isDiaValido(i, diaSemanaList.get(i), dataEntrega.getDayOfWeek())) {
				dataEntrega.advance(i - dataEntrega.getDayOfWeek());
				if (!FeriadoService.getInstance().isFeriado(dataEntrega)) {
					return dataEntrega;
				}
			}
		}
		dataEntrega.advance(1);
		return getDataEntregaDisponivel(dataEntrega, diasEntregaRota);
		
	}
	
	private boolean isPossuiInformacoesValidas(List<String> diaSemanaList) {
		if (diaSemanaList.size() < 7) return false;
		
		for (String valor : diaSemanaList) {
			if (ValueUtil.valueEquals(ValueUtil.VALOR_SIM, valor)) return true;
		}
		return false;
	}
	
	private boolean isDiaValido(int posicaoLista, String valorDaLista, int diaSemanaDaEntrega) {
		return ValueUtil.getBooleanValue(valorDaLista) && posicaoLista == diaSemanaDaEntrega;
	}
}