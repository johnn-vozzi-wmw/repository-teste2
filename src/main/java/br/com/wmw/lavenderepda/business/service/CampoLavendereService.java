package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.business.domain.Campo;
import br.com.wmw.framework.business.service.CampoService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AtividadePedido;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ClienteAtua;
import br.com.wmw.lavenderepda.business.domain.ClienteEndAtua;
import br.com.wmw.lavenderepda.business.domain.ClienteEndereco;
import br.com.wmw.lavenderepda.business.domain.Contato;
import br.com.wmw.lavenderepda.business.domain.FichaFinanceira;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.NfDevolucao;
import br.com.wmw.lavenderepda.business.domain.NotaFiscal;
import br.com.wmw.lavenderepda.business.domain.NovoCliEndereco;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoDesejado;
import br.com.wmw.lavenderepda.business.domain.Prospect;
import br.com.wmw.lavenderepda.business.domain.RedeClienteTitulo;
import br.com.wmw.lavenderepda.business.domain.TituloFinanceiro;
import br.com.wmw.lavenderepda.business.domain.ValorIndicadorWmw;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CampoLavendereDbxDao;
import totalcross.util.Vector;

public class CampoLavendereService extends CampoService {

	private static CampoLavendereService instance;

    public static CampoLavendereService getCampoLavendereInstance() {
        if (instance == null) {
            instance = new CampoLavendereService();
        }
        return instance;
    }

    protected CrudDao getCrudDao() {
        return CampoLavendereDbxDao.getInstance();
    }

    public void loadConfigPersonCadList() throws SQLException {
		BasePersonDomain.limpaConfigsPersonCad();

    	//NovoCliente
		if (LavenderePdaConfig.isUsaCadastroNovoCliente()) {
			BasePersonDomain.loadConfigPersonCadList(NovoCliente.TABLE_NAME, getCamposEntidade(NovoCliente.TABLE_NAME));
			if (LavenderePdaConfig.isUsaMultiplosEnderecosCadastroNovoCliente()) {
				BasePersonDomain.loadConfigPersonCadList(NovoCliEndereco.TABLE_NAME, getCamposEntidade(NovoCliEndereco.TABLE_NAME));
			}
		}

		//Cliente
		BasePersonDomain.loadConfigPersonCadList(Cliente.TABLE_NAME, getCamposEntidade(Cliente.TABLE_NAME));

		//ClienteAtua
		if (LavenderePdaConfig.nuDiasSolicitaAtualizacaoCliente > 0 || LavenderePdaConfig.isPermiteAtualizarManualmenteDadosCadastraisCliente() || LavenderePdaConfig.habilitaAtualizacaoCadastroCliente) {
			BasePersonDomain.loadConfigPersonCadList(ClienteAtua.TABLE_NAME, getCamposEntidade(ClienteAtua.TABLE_NAME));
		}

		//Produto
		BasePersonDomain.loadConfigPersonCadList(Produto.TABLE_NAME, getCamposEntidade(Produto.TABLE_NAME));

		//Pedido
		BasePersonDomain.loadConfigPersonCadList(Pedido.TABLE_NAME_PEDIDO, getCamposEntidade(Pedido.TABLE_NAME_PEDIDO));

		//PedidoErp
		BasePersonDomain.loadConfigPersonCadList(Pedido.TABLE_NAME_PEDIDOERP, getCamposEntidade(Pedido.TABLE_NAME_PEDIDOERP));

		//Ficha Financeira
		BasePersonDomain.loadConfigPersonCadList(FichaFinanceira.TABLE_NAME, getCamposEntidade(FichaFinanceira.TABLE_NAME));

		//Título Financeiro
		BasePersonDomain.loadConfigPersonCadList(TituloFinanceiro.TABLE_NAME, getCamposEntidade(TituloFinanceiro.TABLE_NAME));
		
		//RedeCliente Titulo Financeiro
		BasePersonDomain.loadConfigPersonCadList(RedeClienteTitulo.TABLE_NAME, getCamposEntidade(RedeClienteTitulo.TABLE_NAME));

		//Item Tabela Preco
		BasePersonDomain.loadConfigPersonCadList(ItemTabelaPreco.TABLE_NAME, getCamposEntidade(ItemTabelaPreco.TABLE_NAME));

		//Contato
		BasePersonDomain.loadConfigPersonCadList(Contato.TABLE_NAME, getCamposEntidade(Contato.TABLE_NAME));
		
		//ClienteEndAtua
		BasePersonDomain.loadConfigPersonCadList(ClienteEndAtua.TABLE_NAME, getCamposEntidade(ClienteEndAtua.TABLE_NAME));
		
		//AtividadePedido
		BasePersonDomain.loadConfigPersonCadList(AtividadePedido.TABLE_NAME, getCamposEntidade(AtividadePedido.TABLE_NAME));

		//ProdutoDesejado
		if (LavenderePdaConfig.isUsaCadastroProdutoDesejadosForaCatalogo()) {
			BasePersonDomain.loadConfigPersonCadList(ProdutoDesejado.TABLE_NAME, getCamposEntidade(ProdutoDesejado.TABLE_NAME));
		}
		//Nota Fiscal
		if (LavenderePdaConfig.exibirNotasFiscaisPedido) {
			BasePersonDomain.loadConfigPersonCadList(NotaFiscal.TABLE_NAME, getCamposEntidade(NotaFiscal.TABLE_NAME));
		}
		//Prospect
		BasePersonDomain.loadConfigPersonCadList(Prospect.TABLE_NAME, getCamposEntidade(Prospect.TABLE_NAME));
		
		//Cliente Endereco
		BasePersonDomain.loadConfigPersonCadList(ClienteEndereco.TABLE_NAME, getCamposEntidade(ClienteEndereco.TABLE_NAME));
		
		//Valor Indicador WMW
		BasePersonDomain.loadConfigPersonCadList(ValorIndicadorWmw.TABLE_NAME, getCamposEntidade(ValorIndicadorWmw.TABLE_NAME));
		
		//Nota Fiscal Devolucao
		if (LavenderePdaConfig.exibirNotasDevolucaoNaFichaFinanceira) {
			BasePersonDomain.loadConfigPersonCadList(NfDevolucao.TABLE_NAME, getCamposEntidade(NfDevolucao.TABLE_NAME));
			
		}
	}
    
    public String getNmEntidadeFromPdaTableName(String tableName) {
    	return CrudDbxDao.PREFIXO_TABLE_WEB + tableName.substring(CrudDbxDao.PREFIXO_TABLE_APP_VENDAS.length()).toUpperCase();
    }

	public Vector getCamposEntidade(String tableName) throws SQLException {
		Campo conf = new Campo();
		conf.nmEntidade = getNmEntidadeFromPdaTableName(tableName);
		Vector list = findAllByExample(conf);
		for (int i = 0; i < list.size(); i++) {
			conf = (Campo)list.items[i];
			conf.nmCampo = conf.nmCampo.toUpperCase();
			if (removeCamposDinamicosByParametro(conf)) {
				list.removeElementAt(i);
				i--;
			}
		}
		return list;
	}
	
	protected boolean isNecessarioExtrairNumerosDeCamposMascarados(String tableName, String nmCampo) {
		Campo campoFilter = new Campo();
		campoFilter.cdSistema = Campo.CD_SISTEMA_PADRAO;
		campoFilter.nmEntidade = CrudDbxDao.PREFIXO_TABLE_WEB + tableName.substring(CrudDbxDao.PREFIXO_TABLE_APP_VENDAS.length()).toUpperCase();
		campoFilter.nmCampo = nmCampo;
		Vector configPersonCadList  = BasePersonDomain.getConfigPersonCadList(tableName);
		for (int i = 0; i < configPersonCadList.size(); i++) {
			Campo campo = (Campo) configPersonCadList.items[i];
			if (campo.equals(campoFilter)) {
				if (ValueUtil.isNotEmpty(campo.dsMascara)) {
					for (int j = 0; j < campo.dsMascara.length(); j++) {
						if (campo.dsMascara.charAt(j) == 	'@') {
							return false;
						}
					}
				} else {
					return Campo.FORMATO_CEP.equals(campo.dsFormato) || Campo.FORMATO_TELEFONE.equals(campo.dsFormato) || Campo.FORMATO_CEPAUTO.equals(campo.dsFormato);
				}
			}
			
		}
		return true;
	}
	
	private boolean removeCamposDinamicosByParametro(Campo campo) {
		return removeCampoEnvioEmailPedidoByParametro(campo) || removeCampoEmailAlternativoPedidoByParametro(campo) || removeCampoClienteEntregaPedidoByParametro(campo);
	}
	
	private boolean removeCampoEnvioEmailPedidoByParametro(Campo campo) {
		return Pedido.TABLE_NAME_PEDIDOWEB.equals(campo.nmEntidade) && Pedido.NMCOLUNA_FLENVIAEMAIL.equals(campo.nmCampo) 
				&& !LavenderePdaConfig.isEnviarEmailPedidoAutoCliente() && !LavenderePdaConfig.isPermiteEmailAlternativoPedOrcamento();
	}
	
	private boolean removeCampoEmailAlternativoPedidoByParametro(Campo campo) {
		return Pedido.TABLE_NAME_PEDIDOWEB.equals(campo.nmEntidade) && Pedido.NMCOLUNA_DSEMAILSDESTINO.equals(campo.nmCampo) 
				&& (!LavenderePdaConfig.isEnviarEmailPedidoAutoCliente() || !LavenderePdaConfig.isPermiteInserirEmailAlternativoPedido() && !LavenderePdaConfig.isPermiteEmailAlternativoPedOrcamento());
	}
	
	private boolean removeCampoClienteEntregaPedidoByParametro(Campo campo) {
		return Pedido.TABLE_NAME_PEDIDOWEB.equals(campo.nmEntidade) && Pedido.NMCOLUNA_CDCLIENTEENTREGA.equals(campo.nmCampo) && !LavenderePdaConfig.usaIndicacaoClienteEntregaPedido;
	}
		
}