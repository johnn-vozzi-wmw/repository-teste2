package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ClienteEndAtua;
import br.com.wmw.lavenderepda.business.domain.ClienteEndereco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PeriodoEntrega;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ClienteEnderecoPdbxDao;
import totalcross.util.Vector;

public class ClienteEnderecoService extends CrudPersonLavendereService {

    private static ClienteEnderecoService instance;
    
    private ClienteEnderecoService() {
        //--
    }
    
    public static ClienteEnderecoService getInstance() {
        if (instance == null) {
            instance = new ClienteEnderecoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ClienteEnderecoPdbxDao.getInstance();
    }

	@Override
	public void validate(BaseDomain domain) throws java.sql.SQLException {
		//--
	}
	
	public ClienteEndereco getClienteEnderecoPedido(String cdEmpresa, String cdRepresentante, String cdCliente, String cdEnderecoCliente) throws SQLException {
		return getClienteEnderecoPedido(cdEmpresa, cdRepresentante, cdCliente, cdEnderecoCliente, null);
	}
	public ClienteEndereco getClienteEnderecoPedido(String cdEmpresa, String cdRepresentante, String cdCliente, String cdEnderecoCliente, Pedido pedido) throws SQLException {
		if(pedido!= null && pedido.isPedidoNovoCliente() && LavenderePdaConfig.isApresentaEnderecoNovoCliente()) {
			return NovoCliEnderecoService.getInstance().buscaEnderecoNovoCliente(pedido);
		}
		else if (ValueUtil.isNotEmpty(cdEnderecoCliente)) {
				ClienteEndereco clienteEnderecoFilter = new ClienteEndereco();
				clienteEnderecoFilter.cdEmpresa = cdEmpresa;
				clienteEnderecoFilter.cdRepresentante = cdRepresentante;
				clienteEnderecoFilter.cdCliente = cdCliente;
				clienteEnderecoFilter.cdEndereco = cdEnderecoCliente;
				ClienteEndereco cliEndereco = (ClienteEndereco) findByRowKey(clienteEnderecoFilter.getRowKey());
				if (cliEndereco != null) {
					ClienteEndereco cliEndHash = (ClienteEndereco) ClienteEnderecoService.getInstance().findByRowKeyDyn(cliEndereco.getRowKey());
					cliEndereco.setHashValuesDinamicos(cliEndHash.getHashValuesDinamicos());
					if (LavenderePdaConfig.isApresentaEnderecoAtualizadoEntrega()) {
						ClienteEndAtua cliEndAtua = new ClienteEndAtua();
						cliEndAtua.cdCliente = cliEndereco.cdCliente;
						cliEndAtua.cdEmpresa = cliEndereco.cdEmpresa;
						cliEndAtua.cdRepresentante = cliEndereco.cdRepresentante;
						cliEndAtua.cdEndereco = cliEndereco.cdEndereco;
						Vector listClienteEndAtua = ClienteEndAtuaService.getInstance().findAllByExample(cliEndAtua);
						return getCliEnderecoAtua(cliEndereco, listClienteEndAtua);
						} else {
							return cliEndereco;
						}
				} else {
					if (LavenderePdaConfig.isApresentaEnderecoAtualizadoEntrega()) {
						ClienteEndAtua cliEndAtua = new ClienteEndAtua();
						cliEndAtua.cdCliente = clienteEnderecoFilter.cdCliente;
						cliEndAtua.cdEmpresa = clienteEnderecoFilter.cdEmpresa;
						cliEndAtua.cdRepresentante = clienteEnderecoFilter.cdRepresentante;
						cliEndAtua.cdRegistro = clienteEnderecoFilter.cdEndereco;
						Vector listClienteEndAtua = ClienteEndAtuaService.getInstance().findAllByExample(cliEndAtua);
						return getCliEnderecoAtua(clienteEnderecoFilter, listClienteEndAtua);
					}
				}
		} else {
			if (LavenderePdaConfig.isApresentaEnderecoAtualizadoEntrega()) {
				ClienteEndAtua cliEndAtua = new ClienteEndAtua();
				ClienteEndereco cliEndereco = new ClienteEndereco();
				cliEndAtua.cdCliente = cdCliente;
				cliEndAtua.cdEmpresa = cdEmpresa;
				cliEndAtua.cdRepresentante = cdRepresentante;
				Vector listClienteEndAtua = ClienteEndAtuaService.getInstance().findAllByExample(cliEndAtua);
				return getCliEnderecoAtua(cliEndereco, listClienteEndAtua);
			}
		}
		return null; 
	}
	
	public ClienteEndereco getClienteEnderecoPadrao(final Pedido pedido) throws SQLException {
		return getClienteEnderecoPadrao(pedido, false);
	}
	
	public ClienteEndereco getClienteEnderecoPadrao(final Pedido pedido, boolean isEnderecoCobranca) throws SQLException {
		if (pedido != null) {
			ClienteEndereco clienteEnderecoFilter = new ClienteEndereco();
			clienteEnderecoFilter.cdEmpresa = pedido.cdEmpresa;
			clienteEnderecoFilter.cdRepresentante = pedido.cdRepresentante;
			if (ValueUtil.isNotEmpty(pedido.cdClienteEntrega)) {
				clienteEnderecoFilter.cdCliente = pedido.cdClienteEntrega;
			} else {
				clienteEnderecoFilter.cdCliente = pedido.cdCliente;
			}
			if (isEnderecoCobranca) {				
				clienteEnderecoFilter.flCobranca = ValueUtil.VALOR_SIM;
				clienteEnderecoFilter.flCobrancaPadrao = ValueUtil.VALOR_SIM;
			} else {
				clienteEnderecoFilter.flEntrega = ValueUtil.VALOR_SIM;
				clienteEnderecoFilter.flEntregaPadrao = ValueUtil.VALOR_SIM;
			}
			Vector clienteEnderecoList = findAllByExample(clienteEnderecoFilter);
			if (clienteEnderecoList.size() == 0 && pedido.getCliente() != null && (pedido.getCliente().isNovoCliente() || pedido.getCliente().isClienteDefaultParaNovoPedido())) {
				clienteEnderecoFilter.flEntregaPadrao = ValueUtil.VALOR_NAO;
				clienteEnderecoList = findAllByExample(clienteEnderecoFilter);
			}
			if (LavenderePdaConfig.isApresentaEnderecoAtualizadoEntrega()) {
				if (clienteEnderecoList.size() > 0) {
					return ValueUtil.isNotEmpty(clienteEnderecoList) ? (ClienteEndereco) clienteEnderecoList.items[0] : null;
				} else {
					clienteEnderecoFilter = getClienteEnderecoPedido(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdCliente, pedido.cdEnderecoCliente, pedido);
					if (clienteEnderecoFilter.cdEndereco != null) {
						return clienteEnderecoFilter;
					}
					else {
						return null;
					}
				}
			} else {
				return ValueUtil.isNotEmpty(clienteEnderecoList) ? (ClienteEndereco) clienteEnderecoList.items[0] : null;
			}
		}
		return null;
	}
	
	public boolean isDtEntregaPedidoInvalidaForClienteEndereco(Pedido pedido) throws SQLException {
		if (pedido != null) {
			ClienteEndereco clienteEndereco;
			if (LavenderePdaConfig.usaIndicacaoClienteEntregaPedido && ValueUtil.isNotEmpty(pedido.cdClienteEntrega)) {
				clienteEndereco = getClienteEnderecoPedido(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdClienteEntrega, pedido.cdEnderecoCliente);
			} else {
				clienteEndereco = getClienteEnderecoPedido(pedido.cdEmpresa, pedido.cdRepresentante, pedido.cdCliente, pedido.cdEnderecoCliente);
			}
			if (clienteEndereco != null && clienteEndereco.dsDiaEntrega != null) {
				String[] dsDiaEntrega = StringUtil.split(clienteEndereco.dsDiaEntrega, ';', true);
				if (dsDiaEntrega.length == 7) {
					int diaDaSemana = pedido.dtEntrega.getDayOfWeek();
					return !ValueUtil.VALOR_SIM.equalsIgnoreCase(dsDiaEntrega[diaDaSemana]);
				}
			}
			else if(clienteEndereco.dsDiaEntrega == null || clienteEndereco.dsDiaEntrega.isEmpty()) {
				return false;
			}
		}
		return true;
	}
	
	public boolean isExisteEnderecosEntregaParaCliente(Pedido pedido) throws SQLException {
		return isExisteEnderecosEntregaOuCobrancaParaCliente(pedido, false);
	}
	
	public boolean isExisteEnderecosEntregaOuCobrancaParaCliente(Pedido pedido, boolean isEnderecoCobranca) throws SQLException {
		if (pedido != null ) {
			ClienteEndereco clienteEnderecoFilter = new ClienteEndereco();
			clienteEnderecoFilter.cdEmpresa = pedido.cdEmpresa;
			clienteEnderecoFilter.cdRepresentante = pedido.cdRepresentante;
			clienteEnderecoFilter.cdCliente = LavenderePdaConfig.usaIndicacaoClienteEntregaPedido && ValueUtil.isNotEmpty(pedido.cdClienteEntrega) ? pedido.cdClienteEntrega : pedido.cdCliente;
			if (isEnderecoCobranca) {				
				clienteEnderecoFilter.flCobranca = ValueUtil.VALOR_SIM;
				clienteEnderecoFilter.flCobrancaPadrao = ValueUtil.VALOR_SIM;				
			} else {
				clienteEnderecoFilter.flEntrega = ValueUtil.VALOR_SIM;
				clienteEnderecoFilter.flEntregaPadrao = ValueUtil.VALOR_SIM;				
			}
			return ClienteEnderecoPdbxDao.getInstance().countClienteEnderecoEntregaOuCobranca(clienteEnderecoFilter, isEnderecoCobranca) > 0;
		}
		return false;
	}

	public void aplicaAlteracoesEndereco(ClienteEndereco clienteEnderecoOriginal, ClienteEndereco clienteEndereco) {
		ClienteEnderecoPdbxDao.getInstance().aplicaAlteracoesEndereco(clienteEnderecoOriginal, clienteEndereco);
	}
	
	public boolean isTodosEnderecosComPeriodoEntrega(Cliente cliente) throws SQLException {
		if (cliente != null) {
			ClienteEndereco filter = new ClienteEndereco();
			filter.cdEmpresa = cliente.cdEmpresa;
			filter.cdRepresentante = cliente.cdRepresentante;
			filter.cdCliente = cliente.cdCliente;
			Vector v = findAllByExample(filter);
			Vector v2 = PeriodoEntregaService.getInstance().findPeriodoEntregaCliEndereco(v);
			int size = v.size();
			for(int i = 0; i < size; i++) {
				ClienteEndereco cliEnd = (ClienteEndereco)v.items[i];
				PeriodoEntrega periodo = new PeriodoEntrega();
				periodo.cdPeriodoEntrega = cliEnd.cdPeriodoEntrega;
				if (!v2.contains(periodo)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	public Vector buscaEnderecoAtualizado(Vector listClienteEndereco) throws SQLException {
		ClienteEndAtua cliEndAtua = new ClienteEndAtua();
		ClienteEndereco cliEndereco = (ClienteEndereco) listClienteEndereco.items[0];
		if(cliEndereco != null) {
			cliEndAtua.cdCliente = cliEndereco.cdCliente;
			cliEndAtua.cdEmpresa = cliEndereco.cdEmpresa;
			cliEndAtua.cdRepresentante = cliEndereco.cdRepresentante;
			Vector listClienteEndAux = new Vector();
			Vector listClienteEndAtua = ClienteEndAtuaService.getInstance().findAllByExample(cliEndAtua);
			for (int i = 0; i < listClienteEndereco.size(); i++) {
				cliEndereco = (ClienteEndereco) listClienteEndereco.items[i];
				if (!cliEndereco.getPrimaryKey().contains(ValueUtil.VALOR_NULL)) {
					ClienteEndereco cliEndHash = (ClienteEndereco) ClienteEnderecoService.getInstance().findByRowKeyDyn(cliEndereco.getRowKey());
					cliEndereco.setHashValuesDinamicos(cliEndHash.getHashValuesDinamicos());
				}
				if (cliEndereco.cdEndereco != null) {
					listClienteEndAux.addElement(getCliEnderecoAtua(cliEndereco,listClienteEndAtua));
				}

			}
			for (int x = 0; x < listClienteEndAtua.size(); x++) {
				ClienteEndAtua cliEndAtuaAux = (ClienteEndAtua) listClienteEndAtua.items[x];
				if (!verificaExistente(cliEndAtuaAux,listClienteEndAux)) {
					for (int i = 0; i < listClienteEndereco.size(); i++) {
						ClienteEndereco cliEnderecoAux = (ClienteEndereco) listClienteEndereco.items[i];
						cliEndereco = montarCliEnderecoAtua(cliEndAtuaAux);
						if(ValueUtil.valueEquals(cliEnderecoAux.getPrimaryKey(),cliEndereco.getPrimaryKey()))
							cliEndereco.flEntrega = cliEnderecoAux.flEntrega;
							cliEndereco.flCobranca = cliEnderecoAux.flCobranca;
					}
					listClienteEndAux.addElement(cliEndereco);
				}
			}
			return listClienteEndAux;
		}
		return listClienteEndereco;
	}

	private boolean verificaExistente(ClienteEndAtua cliEndAtuaAux, Vector listClienteEndAux) {
		for (int i = 0; i < listClienteEndAux.size(); i++) {
			ClienteEndereco cliEndereco = (ClienteEndereco) listClienteEndAux.items[i];
			if (ValueUtil.valueEquals(cliEndereco.cdEndereco,cliEndAtuaAux.cdEndereco) 
					||ValueUtil.valueEquals(cliEndereco.cdEndereco,cliEndAtuaAux.cdRegistro)) {
				listClienteEndAux.removeElement(cliEndereco);
				cliEndAtuaAux.flEntrega = cliEndereco.flEntrega;
				cliEndAtuaAux.flCobranca = cliEndereco.flCobranca;
				cliEndereco = montarCliEnderecoAtua(cliEndAtuaAux);
				listClienteEndAux.addElement(cliEndereco);
				return true;
			}
		}
		return false;
	}

	
	private ClienteEndereco montarCliEnderecoAtua(ClienteEndAtua cliEndAtuaAux) {
		ClienteEndereco clienteEndereco = new ClienteEndereco();
		try {
			clienteEndereco.cdEndereco = !ValueUtil.valueEquals(cliEndAtuaAux.cdEndereco,"0") ? cliEndAtuaAux.cdEndereco : cliEndAtuaAux.cdRegistro ;
			ClienteEndAtua cliEndAtuaHash;
			clienteEndereco.cdCliente = cliEndAtuaAux.cdCliente;
			clienteEndereco.cdEmpresa = cliEndAtuaAux.cdEmpresa;
			clienteEndereco.cdRepresentante = cliEndAtuaAux.cdRepresentante;
			clienteEndereco.dsCep = cliEndAtuaAux.dsCep;
			clienteEndereco.dsPais = cliEndAtuaAux.dsPais;
			clienteEndereco.dsEstado = cliEndAtuaAux.dsEstado;
			clienteEndereco.dsCidade = cliEndAtuaAux.dsCidade;
			clienteEndereco.dsBairro = cliEndAtuaAux.dsBairro;
			clienteEndereco.dsLogradouro = cliEndAtuaAux.dsLogradouro;
			clienteEndereco.nuLogradouro = cliEndAtuaAux.nuLogradouro;
			clienteEndereco.dsComplemento = cliEndAtuaAux.dsComplemento;
			clienteEndereco.flEntrega = cliEndAtuaAux.flEntrega != null ? cliEndAtuaAux.flEntrega : "S";
			clienteEndereco.flCobranca = cliEndAtuaAux.flCobranca;
			clienteEndereco.dsDiaEntrega = null;
			
			cliEndAtuaHash = (ClienteEndAtua) ClienteEndAtuaService.getInstance().findByRowKeyDyn(cliEndAtuaAux.getRowKey());
			if(cliEndAtuaHash == null) {
				cliEndAtuaAux.cdEndereco = "0";
				cliEndAtuaHash = (ClienteEndAtua) ClienteEndAtuaService.getInstance().findByRowKeyDyn(cliEndAtuaAux.getRowKey());
			}
			clienteEndereco.setHashValuesDinamicos(cliEndAtuaHash.getHashValuesDinamicos());
			setCamposDinamicosCliEndAtua(clienteEndereco, cliEndAtuaHash);
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
			e.printStackTrace();
		}
		return clienteEndereco;
	}
	
	

	public ClienteEndereco getCliEnderecoAtua(ClienteEndereco cliEndereco2, Vector listClienteEndAtua) {
		ClienteEndAtua cliEndAtua = new ClienteEndAtua();
		ClienteEndereco cliEndereco = cliEndereco2 ;
		for (int x = 0; x < listClienteEndAtua.size(); x++) {
			ClienteEndAtua cliEndAtuaAux = (ClienteEndAtua) listClienteEndAtua.items[x];
			if (cliEndAtua.cdRegistro == null && cliEndAtuaAux != null) {
				cliEndAtua = cliEndAtuaAux;
				if (cliEndAtua.cdEndereco.equals("0")) {
					cliEndAtua.cdEndereco = cliEndAtuaAux.cdRegistro;
				}
			}
			if (ValueUtil.getIntegerValue(cliEndAtua.cdRegistro) < ValueUtil.getIntegerValue(cliEndAtuaAux.cdRegistro)) {
				cliEndAtua = cliEndAtuaAux;
			}
		}
		if (ValueUtil.valueEquals(cliEndereco.cdEndereco, cliEndAtua.cdEndereco) || cliEndereco.cdEndereco == null) {
			cliEndereco.dsCep = cliEndAtua.dsCep;
			cliEndereco.dsPais = cliEndAtua.dsPais;
			cliEndereco.dsEstado = cliEndAtua.dsEstado;
			cliEndereco.dsCidade = cliEndAtua.dsCidade;
			cliEndereco.dsBairro = cliEndAtua.dsBairro;
			cliEndereco.dsLogradouro = cliEndAtua.dsLogradouro;
			cliEndereco.nuLogradouro = cliEndAtua.nuLogradouro;
			cliEndereco.dsComplemento = cliEndAtua.dsComplemento;
			cliEndereco.cdCliente = cliEndAtua.cdCliente;
			cliEndereco.cdEmpresa = cliEndAtua.cdEmpresa;
			cliEndereco.cdRepresentante = cliEndAtua.cdRepresentante;
			cliEndereco.cdEndereco = cliEndAtua.cdEndereco != "0" ? cliEndAtua.cdEndereco : cliEndAtua.cdRegistro ;
			
			if(ValueUtil.valueEquals(cliEndereco.cdEndereco,cliEndAtua.cdRegistro)) {
				cliEndAtua.cdEndereco = "0";
				try {
					if (!cliEndAtua.getPrimaryKey().contains(ValueUtil.VALOR_NULL)) {
						ClienteEndAtua cliEndAtuaHash = (ClienteEndAtua) ClienteEndAtuaService.getInstance().findByRowKeyDyn(cliEndAtua.getRowKey());
						cliEndereco.setHashValuesDinamicos(cliEndAtuaHash.getHashValuesDinamicos());
						setCamposDinamicosCliEndAtua(cliEndereco, cliEndAtua);
					}
				} catch (SQLException e) {
					ExceptionUtil.handle(e);
				}
			}
			else {
				try {
					if (!cliEndereco.getPrimaryKey().contains(ValueUtil.VALOR_NULL)) {
						ClienteEndereco cliEndHash = (ClienteEndereco) ClienteEnderecoService.getInstance().findByRowKeyDyn(cliEndereco.getRowKey());
						if (cliEndHash == null) {
							ClienteEndAtua cliEndAtuaHash = (ClienteEndAtua) ClienteEndAtuaService.getInstance().findByRowKeyDyn(cliEndAtua.getRowKey());
							cliEndereco.setHashValuesDinamicos(cliEndAtuaHash.getHashValuesDinamicos());
							setCamposDinamicosCliEndAtua(cliEndereco, cliEndAtuaHash);
						} else {
							cliEndereco.setHashValuesDinamicos(cliEndHash.getHashValuesDinamicos());
						}
					}
				} catch (SQLException e) {
					ExceptionUtil.handle(e);
				}
			}
		}	
		return cliEndereco;
	}
	
	private void setCamposDinamicosCliEndAtua(ClienteEndereco clienteEndereco, ClienteEndAtua clienteEndAtua) {
		clienteEndereco.dsDiaEntrega = clienteEndAtua.getHashValuesDinamicos().getString("DSDIAENTREGA");
		clienteEndereco.dsPeriodoEntrega = clienteEndAtua.getHashValuesDinamicos().getString("DSPERIODOENTREGA");
		clienteEndereco.dsPeriodoEntregaAlternativo = clienteEndAtua.getHashValuesDinamicos().getString("DSPERIODOENTREGAALTERNATIVO");
		clienteEndereco.dsDiaAbertura = clienteEndAtua.getHashValuesDinamicos().getString("DSDIAABERTURA");
		clienteEndereco.dsPeriodoAbertura = clienteEndAtua.getHashValuesDinamicos().getString("DSPERIODOABERTURA");
		clienteEndereco.flComercial = clienteEndAtua.getHashValuesDinamicos().getString("FLCOMERCIAL");
		clienteEndereco.flEntrega = clienteEndAtua.getHashValuesDinamicos().getString("FLENTREGA");
		clienteEndereco.flEntregaPadrao = clienteEndAtua.getHashValuesDinamicos().getString("FLENTREGAPADRAO");
		clienteEndereco.flEntregaAgendada = clienteEndAtua.getHashValuesDinamicos().getString("FLENTREGAAGENDADA");
		clienteEndereco.flCobranca = clienteEndAtua.getHashValuesDinamicos().getString("FLCOBRANCA");
        clienteEndereco.flCobrancaPadrao = clienteEndAtua.getHashValuesDinamicos().getString("FLCOBRANCAPADRAO");
	}
}
