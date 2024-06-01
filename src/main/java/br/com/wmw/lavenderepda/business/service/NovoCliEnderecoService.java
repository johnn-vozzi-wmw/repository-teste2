package br.com.wmw.lavenderepda.business.service;

import java.lang.reflect.Field;
import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ClienteEndereco;
import br.com.wmw.lavenderepda.business.domain.NovoCliEndereco;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NovoCliEnderecoDao;
import totalcross.util.Vector;

public class NovoCliEnderecoService extends CrudPersonLavendereService {

    private static NovoCliEnderecoService instance;
    
    private NovoCliEnderecoService() {
    }
    
    public static NovoCliEnderecoService getInstance() {
        if (instance == null) {
            instance = new NovoCliEnderecoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return NovoCliEnderecoDao.getInstance();
    }
       
    public void validateNovoClienteEndereco(NovoCliente novoCliente) {
    	if (LavenderePdaConfig.isUsaMultiplosEnderecosCadastroNovoCliente()) {
    		try {
    			for (int i = 0; i < novoCliente.novoCliEnderecoList.size(); i++) {
    				NovoCliEndereco novoCliEndereco = (NovoCliEndereco) novoCliente.novoCliEnderecoList.items[i];
    				super.validate(novoCliEndereco);
    			}
			} catch (Throwable e) {
				String params = ((ValidationException)e).params;
				throw new ValidationException(Messages.NOVOCLIENDERECO_VALIDACAO_CAMPO_NAO_INFORMADO, params);
			}
    	}
    }
    
	public void insereEnderecoNovoCliente(NovoCliente novoCliente) throws SQLException {
		if (LavenderePdaConfig.isUsaMultiplosEnderecosCadastroNovoCliente()) {
			deleteRegistrosNovoCliEndereco(novoCliente);
			for (int i = 0; i < novoCliente.novoCliEnderecoList.size(); i++) {
				NovoCliEndereco novoCliEndereco = (NovoCliEndereco) novoCliente.novoCliEnderecoList.items[i];
				insert(novoCliEndereco);
			}
		}
	}

	public void deleteRegistrosNovoCliEndereco(NovoCliente novoCliente) throws SQLException {
		NovoCliEndereco novoCliEnderecoFilter = new NovoCliEndereco();
		novoCliEnderecoFilter.cdEmpresa = novoCliente.cdEmpresa;
		novoCliEnderecoFilter.cdRepresentante = novoCliente.cdRepresentante;
		novoCliEnderecoFilter.cdNovoCliente = novoCliente.cdNovoCliente;
		novoCliEnderecoFilter.flOrigemNovoCliente = novoCliente.flOrigemNovoCliente;
		deleteAllByExample(novoCliEnderecoFilter);
	}

	public Vector loadNovoCliEnderecoList(NovoCliente novoCliente) throws SQLException {
		NovoCliEndereco novoCliEnderecoFilter = new NovoCliEndereco();
		novoCliEnderecoFilter.cdEmpresa = novoCliente.cdEmpresa;
		novoCliEnderecoFilter.cdRepresentante = novoCliente.cdRepresentante;
		novoCliEnderecoFilter.cdNovoCliente = novoCliente.cdNovoCliente;
		novoCliEnderecoFilter.flOrigemNovoCliente = novoCliente.flOrigemNovoCliente;
		return findAllByExampleDyn(novoCliEnderecoFilter);
	}

	public NovoCliEndereco getEnderecoNovoCli(String cdEmpresa, String cdRepresentante, String cdNovoCliente) throws SQLException {
		NovoCliEndereco novoCliEndereco = new NovoCliEndereco();
		novoCliEndereco.cdEmpresa = cdEmpresa;
		novoCliEndereco.cdRepresentante = cdRepresentante;
		novoCliEndereco.cdNovoCliente = cdNovoCliente;
		novoCliEndereco.limit = 1;
		return (NovoCliEndereco) findAllByExample(novoCliEndereco).items[0];
	}
	
	public NovoCliEndereco localizaNovoCliEnderecoNaLista(Vector novoCliEnderecoList, String rowkey) {
	    for (int i = 0; i < novoCliEnderecoList.size(); i++) {
	    	NovoCliEndereco novoCliEndereco = (NovoCliEndereco) novoCliEnderecoList.items[i];
	    	if (rowkey.equals(novoCliEndereco.getRowKey())) {
	    		return novoCliEndereco;
	    	}
		}
	    return null;
	}

	public void copiaEnderecosProspectsParaNovoCliente(NovoCliente novoCliente, Cliente clienteProspects) throws SQLException {
		ClienteEndereco clienteEnderecoProspectsFilter = new ClienteEndereco();
		clienteEnderecoProspectsFilter.cdEmpresa = clienteProspects.cdEmpresa;
		clienteEnderecoProspectsFilter.cdRepresentante = clienteProspects.cdRepresentante;
		clienteEnderecoProspectsFilter.cdCliente = clienteProspects.cdCliente;
		Vector clienteEnderecoProspectsList = ClienteEnderecoService.getInstance().findAllByExample(clienteEnderecoProspectsFilter);

		if (ValueUtil.isNotEmpty(clienteEnderecoProspectsList)) {
			int size = clienteEnderecoProspectsList.size();
			for (int i = 0; i < size; i++) {
				ClienteEndereco clienteEndereco = (ClienteEndereco) clienteEnderecoProspectsList.items[i];
				NovoCliEndereco novoCliEndereco = new NovoCliEndereco();
				novoCliEndereco.cdEmpresa = novoCliente.cdEmpresa;
				novoCliEndereco.cdRepresentante = novoCliente.cdRepresentante;
				novoCliEndereco.cdNovoCliente = novoCliente.cdNovoCliente;
				novoCliEndereco.flOrigemNovoCliente = OrigemPedido.FLORIGEMPEDIDO_PDA;
				novoCliEndereco.cdEndereco = clienteEndereco.cdEndereco;
				novoCliEndereco.cdPeriodoEntrega = clienteEndereco.cdPeriodoEntrega;
				NovoCliEndereco.setDadosAlteracao(novoCliEndereco);
				Field[] camposClienteEndereco = ClienteEndereco.class.getDeclaredFields();
				for (int j = 0; j < camposClienteEndereco.length; j++) {
					Field field = camposClienteEndereco[j];
					if (novoCliEndereco.getHashValuesDinamicos().exists(field.getName().toUpperCase())) {
						try {
							if (CampoLavendereService.getCampoLavendereInstance().isNecessarioExtrairNumerosDeCamposMascarados(NovoCliEndereco.TABLE_NAME, field.getName().toUpperCase())) {
								field.set(clienteProspects, ValueUtil.getValidNumbers((String) field.get(clienteEndereco)));
							}
							novoCliEndereco.getHashValuesDinamicos().put(field.getName().toUpperCase(), field.get(clienteEndereco));
						} catch (Throwable e) {
							// Não faz nada
						} 
					}
				}
				verificaCamposDinamicosDeClienteProspect(novoCliEndereco, clienteEndereco);
				novoCliente.novoCliEnderecoList.addElement(novoCliEndereco);
			}
		}
	}

	private void verificaCamposDinamicosDeClienteProspect(NovoCliEndereco novoCliEndereco, ClienteEndereco clienteEndereco) {
		Field[] camposClienteEndereco = ClienteEndereco.class.getDeclaredFields();
		for (int j = 0; j < camposClienteEndereco.length; j++) {
			String value;
			try {
				String coluna = camposClienteEndereco[j].getName().toUpperCase();
				value = (String) camposClienteEndereco[j].get(clienteEndereco);
				if (ValueUtil.isNotEmpty(value) && novoCliEndereco.getHashValuesDinamicos().exists(coluna)) {
					if (CampoLavendereService.getCampoLavendereInstance().isNecessarioExtrairNumerosDeCamposMascarados(NovoCliEndereco.TABLE_NAME, coluna)) {
						value = ValueUtil.getValidNumbers(value);
					}
					novoCliEndereco.getHashValuesDinamicos().put(coluna, value);
				}
			} catch (Throwable e) {
				// Não faz nada
			}
		}
	}
	
	@Override
	public void validate(BaseDomain domain) throws SQLException {
		setUfNaoPermiteInscEstadualVazio(NovoCliEndereco.TABLE_NAME);
		super.validate(domain);
		if (LavenderePdaConfig.isUsaControleFlagsEnderecoNovoCliente()) {
			validateTipoEnderecoSelecionado(domain);			
		}
	}
	
	private void validateTipoEnderecoSelecionado(BaseDomain domain) {
		NovoCliEndereco novoCliEndereco = (NovoCliEndereco)domain;
		String flComercial = (String) novoCliEndereco.getHashValuesDinamicos().get(NovoCliEndereco.NMCOLUNA_FLCOMERCIAL);
		String flEntrega = (String) novoCliEndereco.getHashValuesDinamicos().get(NovoCliEndereco.NMCOLUNA_FLENTREGA);
		if (ValueUtil.valueEquals(flComercial, ValueUtil.VALOR_NAO) && ValueUtil.valueEquals(flEntrega, ValueUtil.VALOR_NAO)) {
			throw new ValidationException(Messages.NOVO_CLIENTE_TIPO_ENDERECO_NAO_INFORMADO);
		}
	}

	public void buscaEnderecoNovoClienteList(Pedido pedido, Vector listClienteEndereco) throws SQLException {
		NovoCliente novoClienteFilter = new NovoCliente();
		novoClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		novoClienteFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		novoClienteFilter.nuCnpj = pedido.getCliente().cdCliente;
		Vector novoClienteList = NovoClienteService.getInstance().findAllByExample(novoClienteFilter);
		NovoCliente novoCliente = new NovoCliente();
		if(novoClienteList.size() > 0) {
		   novoCliente = (NovoCliente) novoClienteList.items[0];
		}
		NovoCliEndereco novoCliEnderecoFilter = new NovoCliEndereco();
		novoCliEnderecoFilter.cdEmpresa = pedido.cdEmpresa;
		novoCliEnderecoFilter.cdRepresentante = pedido.cdRepresentante;
		novoCliEnderecoFilter.cdNovoCliente = novoCliente.cdNovoCliente;
		
		Vector listNovoCliEndereco = findAllByExample(novoCliEnderecoFilter);
		if(listNovoCliEndereco.size() > 0) {
			for (int i = 0; i < listNovoCliEndereco.size(); i++) {
				novoCliEnderecoFilter = (NovoCliEndereco) listNovoCliEndereco.items[i];
				ClienteEndereco clienteEndereco = new ClienteEndereco();
				clienteEndereco.cdCliente = novoCliEnderecoFilter.cdNovoCliente;
				clienteEndereco.cdEmpresa = novoCliEnderecoFilter.cdEmpresa;
				clienteEndereco.cdRepresentante = novoCliEnderecoFilter.cdRepresentante;
				clienteEndereco.dsCep = novoCliEnderecoFilter.dsCep;
				clienteEndereco.dsPais = novoCliEnderecoFilter.dsPais;
				clienteEndereco.dsEstado = novoCliEnderecoFilter.cdUf;
				clienteEndereco.dsCidade = novoCliEnderecoFilter.dsCidade;
				clienteEndereco.dsBairro = novoCliEnderecoFilter.dsBairro;
				clienteEndereco.dsLogradouro = novoCliEnderecoFilter.dsLogradouro;
				clienteEndereco.nuLogradouro = novoCliEnderecoFilter.nuLogradouro;
				clienteEndereco.dsComplemento = novoCliEnderecoFilter.dsComplemento;
				clienteEndereco.cdEndereco = novoCliEnderecoFilter.cdEndereco ;
				clienteEndereco.flEntrega = novoCliEnderecoFilter.flEntrega;
				clienteEndereco.dsObservacao = novoCliEnderecoFilter.dsObservacao;
				NovoCliEndereco cliEndHash = (NovoCliEndereco) NovoCliEnderecoService.getInstance().findByRowKeyDyn(novoCliEnderecoFilter.getRowKey());
				ajustaHashEndereco(cliEndHash);
				clienteEndereco.setHashValuesDinamicos(cliEndHash.getHashValuesDinamicos());
				listClienteEndereco.addElement(clienteEndereco);
			}
		}
		
	}
	public ClienteEndereco buscaEnderecoNovoCliente(Pedido pedido) throws SQLException {
		NovoCliente novoClienteFilter = new NovoCliente();
		novoClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		novoClienteFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		novoClienteFilter.nuCnpj = pedido.getCliente().cdCliente;
		Vector novoClienteList = NovoClienteService.getInstance().findAllByExample(novoClienteFilter);
		NovoCliente novoCliente = new NovoCliente();
		if(novoClienteList.size() > 0) {
			novoCliente = (NovoCliente) novoClienteList.items[0];
		}
		NovoCliEndereco novoCliEnderecoFilter = new NovoCliEndereco();
		novoCliEnderecoFilter.cdEmpresa = pedido.cdEmpresa;
		novoCliEnderecoFilter.cdRepresentante = pedido.cdRepresentante;
		novoCliEnderecoFilter.cdNovoCliente = novoCliente.cdNovoCliente;
		novoCliEnderecoFilter.cdEndereco = pedido.cdEnderecoCliente;
		
		ClienteEndereco clienteEndereco = new ClienteEndereco();
		Vector listNovoCliEndereco = findAllByExample(novoCliEnderecoFilter);
		if(listNovoCliEndereco.size() > 0) {
			for (int i = 0; i < listNovoCliEndereco.size(); i++) {
				novoCliEnderecoFilter = (NovoCliEndereco) listNovoCliEndereco.items[i];
				clienteEndereco.cdCliente = novoCliEnderecoFilter.cdNovoCliente;
				clienteEndereco.cdEmpresa = novoCliEnderecoFilter.cdEmpresa;
				clienteEndereco.cdRepresentante = novoCliEnderecoFilter.cdRepresentante;
				clienteEndereco.dsCep = novoCliEnderecoFilter.dsCep;
				clienteEndereco.dsPais = novoCliEnderecoFilter.dsPais;
				clienteEndereco.dsEstado = novoCliEnderecoFilter.cdUf;
				clienteEndereco.dsCidade = novoCliEnderecoFilter.dsCidade;
				clienteEndereco.dsBairro = novoCliEnderecoFilter.dsBairro;
				clienteEndereco.dsLogradouro = novoCliEnderecoFilter.dsLogradouro;
				clienteEndereco.dsComplemento = novoCliEnderecoFilter.dsComplemento;
				clienteEndereco.cdEndereco = novoCliEnderecoFilter.cdEndereco ;
				clienteEndereco.flEntrega = novoCliEnderecoFilter.flEntrega;
				clienteEndereco.dsObservacao = novoCliEnderecoFilter.dsObservacao;
				NovoCliEndereco cliEndHash = (NovoCliEndereco) NovoCliEnderecoService.getInstance().findByRowKeyDyn(novoCliEnderecoFilter.getRowKey());
				ajustaHashEndereco(cliEndHash);
				clienteEndereco.setHashValuesDinamicos(cliEndHash.getHashValuesDinamicos());
			}
		}
		return clienteEndereco;
		
	}

	private void ajustaHashEndereco(NovoCliEndereco cliEndHash) {
		cliEndHash.getHashValuesDinamicos().put("DSESTADO", cliEndHash.getHashValuesDinamicos().get("CDUF"));
		cliEndHash.getHashValuesDinamicos().remove("CDUF");
		
		cliEndHash.getHashValuesDinamicos().put("DSOBS", cliEndHash.getHashValuesDinamicos().get("DSOBSERVACAO"));
		cliEndHash.getHashValuesDinamicos().remove("DSOBSERVACAO");
		
	}
	
}