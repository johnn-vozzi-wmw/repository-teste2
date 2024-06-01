package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ValorParametroPdbxDao;
import totalcross.util.Vector;

public class ValorParametroService extends CrudService {

    private static ValorParametroService instance;

    private ValorParametroService() {
    }

    public static ValorParametroService getInstance() {
        if (instance == null) {
            instance = new ValorParametroService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return ValorParametroPdbxDao.getInstance();
    }
    
    @Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    @Override
    protected void setDadosAlteracao(BaseDomain domain) {
    	//Tivemos que sobreescrever pois as alterações não devem ser enviadas para o servidor
    }

    public Vector findAllConfigParamPda() throws SQLException {
    	Vector list = new Vector();
    	//-- usaEstoqueOnline
    	ValorParametro configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.USAESTOQUEONLINE);
    	if (configParametro != null) {
    		configParametro.dsParametro = Messages.PARAMETRO_USAESTOQUEONLINE;
    		list.addElement(configParametro);
    	}
    	//-- ocultaColunaCdProduto
    	configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.OCULTACOLUNACDPRODUTO);
    	if (configParametro != null) {
    		configParametro.dsParametro = Messages.PARAMETRO_OCULTARCOLUNACDPRODUTO;
    		list.addElement(configParametro);
    	}
    	//-- usaPesquisaInicioString
    	configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.USAPESQUISAINICIOSTRING);
    	if (configParametro != null) {
    		configParametro.dsParametro = Messages.PARAMETRO_USAPESQUISAINICIOSTRING;
    		list.addElement(configParametro);
    	}
    	//-- limpaFiltroProdutoAutomaticamente
    	configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.LIMPAFILTROPRODUTOAUTOMATICAMENTE);
    	if (configParametro != null) {
    		configParametro.dsParametro = Messages.PARAMETRO_LIMPACAMPOBUSCAPRODUTO;
    		list.addElement(configParametro);
    	}
    	//-- usaTecladoVirtual
    	//-- Verifica primeiro se o parametro 1460 está desligado, para então adicionar na lista
    	configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.USAFOCOCAMPOBUSCAAUTOMATICAMENTE);
        if (configParametro != null && configParametro.vlParametro.equals("N")) {
        	configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.USATECLADOVIRTUAL);
        	configParametro.dsParametro = Messages.PARAMETRO_TECLADOVIRTUAL;
        	list.addElement(configParametro);
        }
    	//-- usaTooltipDsProdutoListaProdutos
    	configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.USAORDENACAOPORCODIGOLISTAPRODUTOS);
    	if (configParametro != null) {
    		configParametro.dsParametro = Messages.PARAMETRO_ORDENACAO_PRODUTOS;
    		list.addElement(configParametro);
    	}
    	//-- emiteBeepAoInserirItemPedido
    	configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.EMITEBEEPAOINSERIRITEMPEDIDO);
    	if (configParametro != null) {
    		configParametro.dsParametro = Messages.PARAMETRO_EMITEBEEP_INSERIR_ITENS;
    		list.addElement(configParametro);
    	}
    	//-- usaTecladoFixoTelaItemPedido
    	configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.USATECLADOFIXOTELAITEMPEDIDO);
    	if (configParametro != null) {
    		configParametro.dsParametro = Messages.PARAMETRO_USATECLADOFIXOTELAITEMPEDIDO;
    		list.addElement(configParametro);
    	}
    	//-- usaScroolLateralListasProdutos
    	configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.USASCROOLLATERALLISTASPRODUTOS);
    	if (configParametro != null) {
    		configParametro.dsParametro = Messages.PARAMETRO_USASCROOLLATERALLISTASPRODUTOS;
    		list.addElement(configParametro);
    	}
    	//-- usaFocoCampoBuscaAutomaticamente
    	configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.USAFOCOCAMPOBUSCAAUTOMATICAMENTE);
    	if (configParametro != null && VmUtil.isSimulador()) {
    		configParametro.dsParametro = Messages.PARAMETRO_USAFOCOCAMPOBUSCAAUTOMATICAMENTE;
    		list.addElement(configParametro);
    	}
    	//-- usaTeclaEnterComoConfirmacaoItemPedido
    	configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.USATECLAENTERCOMOCONFIRMACAOITEMPEDIDO);
    	if (configParametro != null && VmUtil.isSimulador()) {
    		configParametro.dsParametro = Messages.PARAMETRO_USATECLAENTERCOMOCONFIRMACAOITEMPEDIDO;
    		list.addElement(configParametro);
    	}
    	//-- emiteBeepDeErro
    	configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.EMITEBEEPDEERRO);
    	if (configParametro != null) {
    		configParametro.dsParametro = Messages.PARAMETRO_EMITEBEEPDEERRO;
    		list.addElement(configParametro);
    	}
    	//-- permiteDigitacaoCampoData
    	configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.PERMITEDIGITACAOCAMPODATA);
    	if (configParametro != null) {
    		configParametro.dsParametro = Messages.PARAMETRO_PERMITEDIGITACAOCAMPODATA;
    		list.addElement(configParametro);
    	}
    	//-- nivelLogSyncBackground
    	configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.NIVELLOGSYNCBACKGROUND);
    	if (configParametro != null) {
    		configParametro.dsParametro = Messages.PARAMETRO_NIVEL_LOG_SYNC_BACKGROUND;
    		list.addElement(configParametro);
    	}
    	return list;
    }

    @Override
    public void update(BaseDomain domain) throws SQLException {
    	ValorParametro valorParametro = (ValorParametro) domain;
    	super.update(valorParametro);
    	LogPdaService.getInstance().logAlteraParametroPda(valorParametro.cdParametro, valorParametro.vlParametro);
    }

    public Vector findAllParamLiberaComSenha() throws SQLException {
    	Vector list = new Vector();
    	//-- liberaComSenhaClienteAtrasadoNovoPedido
    	if (LavenderePdaConfig.liberaComSenhaClienteAtrasadoNovoPedido() && !LavenderePdaConfig.getOcultaLiberacaoClienteAtrasado()) {
	    	ValorParametro configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.NUDIASLIBERACAOCOMSENHACLIENTEATRASADONOVOPEDIDO);
	    	configParametro.dsParametro = Messages.PARAMETRO_LIBERACOMSENHACLIENTEATRASADO;
	    	list.addElement(configParametro);
    	}
    	//--
    	if (LavenderePdaConfig.isUsaConfigLiberacaoComSenhaLimiteCreditoCliente() && !LavenderePdaConfig.getOcultaLiberacaoLimiteCreditoCliente()) {
    		ValorParametro configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.LIBERACOMSENHALIMITECREDITOCLIENTE);
	    	configParametro.dsParametro = Messages.PARAMETRO_LIBERACOMSENHACLIENTELIMITECREDITO;
	    	list.addElement(configParametro);
	    	
	    	if (LavenderePdaConfig.isLiberaComSenhaLimiteCreditoClienteAoFecharPedido()) {
	    		ValorParametro valorParametro = new ValorParametro(ValorParametro.LIBERACOMSENHALIMITECREDITOCLIENTEAOFECHARPEDIDO, Messages.LIBERAR_FECHAMENTO_PEDIDO_LIMITE_CREDITO);
	    		list.addElement(valorParametro);
	    	}
	    	if (LavenderePdaConfig.isUsaLiberacaoPorUsuarioEAlcada()) {
	    		ValorParametro valorParametro = new ValorParametro(ValorParametro.LIBERACAOPORUSUARIOEALCADA, Messages.LIBERAR_FECHAMENTO_PEDIDO_LIMITE_CREDITO_USUARIO);
	    		list.addElement(valorParametro);
	    	}
    	}
    	//--
    	if (LavenderePdaConfig.liberaComSenhaPrecoDeVenda && !LavenderePdaConfig.getOcultaLiberacaoPrecoVendaPedido()) {
    		ValorParametro configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.LIBERACOMSENHAPRECODEVENDA);
	    	configParametro.dsParametro = Messages.PARAMETRO_LIBERACOMSENHAPRECOVENDA;
	    	list.addElement(configParametro);
    	}
    	//--
    	if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido && !LavenderePdaConfig.getOcultaLiberacaoTipoPedido()) {
    		ValorParametro configParametro = new ValorParametro();
	    	configParametro.cdParametro = ValorParametro.LIBERACOMSENHATIPOPEDIDOFLEXIGESENHA;
	    	configParametro.dsParametro = Messages.PARAMETRO_LIBERACOMSENHATIPODEPEDIDO;
	    	list.addElement(configParametro);
    	}
    	//--
    	if ((LavenderePdaConfig.obrigaEnvioDePedidosPorTempoDecorrido > 0 || LavenderePdaConfig.obrigaEnvioDePedidosPorQtdPedidosPendentes > 0)  && !LavenderePdaConfig.getOcultaLiberacaoSemEnvioDados()) {
    		ValorParametro configParametro = new ValorParametro();
    		configParametro.cdParametro = ValorParametro.OBRIGAENVIODEPEDIDOSPORTEMPODECORRIDO;
    		configParametro.dsParametro = Messages.PARAMETRO_LIBERACOMSENHANOVOPEDIDO_ENVIA;
    		list.addElement(configParametro);
    	}
    	//--
    	if (LavenderePdaConfig.isObrigaReceberDadosBloqueiaNovoPedido() && !LavenderePdaConfig.getOcultaLiberacaoPedidoSemRecebimento()) {
    		ValorParametro configParametro = new ValorParametro();
	    	configParametro.cdParametro = ValorParametro.OBRIGARECEBERDADOSBLOQUEIANOVOPEDIDO;
	    	configParametro.dsParametro = Messages.PARAMETRO_LIBERACOMSENHANOVOPEDIDO_RECEB;
	    	list.addElement(configParametro);
    	}
    	//--
    	if (LavenderePdaConfig.isObrigaReceberDadosBloqueiaUsoSistema() && !LavenderePdaConfig.getOcultaLiberacaoSistemaSemRecebimento()) {
    		ValorParametro configParametro = new ValorParametro();
	    	configParametro.cdParametro = ValorParametro.OBRIGARECEBERDADOSBLOQUEIAUSOSISTEMA;
	    	configParametro.dsParametro = Messages.PARAMETRO_LIBERACOMSENHASISTEMA_RECEB;
	    	list.addElement(configParametro);
    	}
    	if (LavenderePdaConfig.nuDiasBloqueiaClienteSemPedido > 0 && !LavenderePdaConfig.getOcultaLiberacaoClientesSemPedidos()) {
    		ValorParametro configParametro = new ValorParametro();
    		configParametro.cdParametro = ValorParametro.NUDIASBLOQUEIACLIENTESEMPEDIDO;
    		configParametro.dsParametro = Messages.PARAMETRO_LIBERACOMSENHASISTEMA_CLIENTE_SEM_PEDIDO;
    		list.addElement(configParametro);
    	}
    	if (LavenderePdaConfig.isLiberaComSenhaClienteRedeAtrasadoNovoPedido() && !LavenderePdaConfig.getOcultaLiberacaoClienteRedeAtrasado()) {
	    	ValorParametro configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.LIBERACOMSENHACLIENTEREDEATRASADONOVOPEDIDO);
	    	configParametro.dsParametro = Messages.PARAMETRO_LIBERACOMSENHACLIENTEREDEATRASADO;
	    	list.addElement(configParametro);
    	}
    	if (LavenderePdaConfig.liberaComSenhaVendaProdutoBloqueado && !LavenderePdaConfig.getOcultaLiberacaoVendaProdutoBloqueado()) {
    		ValorParametro configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.LIBERACOMSENHAVENDAPRODUTOBLOQUEADO);
    		configParametro.dsParametro = Messages.SENHADINAMICA_COMBO_PRODUTO_BLOQ;
    		list.addElement(configParametro);
    	}
    	if (LavenderePdaConfig.bloqueiaSistemaGpsInativo && !LavenderePdaConfig.getOcultaLiberacaoSistemaGPSInativo()) {
    		ValorParametro configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.BLOQUEIASISTEMAGPSINATIVO);
    		configParametro.dsParametro = Messages.SENHADINAMICA_COMBO_GPS_INATIVO;
    		list.addElement(configParametro);
    	}
    	if (LavenderePdaConfig.isHoraLimiteParaEnvioPedidos() && !LavenderePdaConfig.getOcultaLiberacaoHoraLimiteEnvio()) {
    		ValorParametro configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.HORALIMITEPARAENVIOPEDIDOS);
    		configParametro.cdParametro = ValorParametro.HORALIMITEPARAENVIOPEDIDOS;
    		configParametro.dsParametro = Messages.PARAMETRO_HORALIMITEENVIOPEDIDOS;
    		list.addElement(configParametro);
    	}
    	if (LavenderePdaConfig.liberaComSenhaPrecoProduto && !LavenderePdaConfig.getOcultaLiberacaoPrecoVendaProdutoCliente()) {
    		ValorParametro configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.LIBERACOMSENHAPRECOPRODUTO);
    		configParametro.cdParametro = ValorParametro.LIBERACOMSENHAPRECOPRODUTO;
    		configParametro.dsParametro = Messages.PARAMETRO_LIBERACOMSENHAPRECOPRODUTO;
    		list.addElement(configParametro);
    	}
    	if (LavenderePdaConfig.liberaSenhaQtdItemMaiorPedidoOriginal && !LavenderePdaConfig.getOcultaLiberacaoQtdeItemMaiorPedidoOrig()) {
    		ValorParametro configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.LIBERASENHAQTDITEMMAIORPEDIDOORIGINAL);
    		configParametro.cdParametro = ValorParametro.LIBERASENHAQTDITEMMAIORPEDIDOORIGINAL;
    		configParametro.dsParametro = Messages.PARAMETRO_LIBERASENHAQTDITEMMAIORPEDIDOORIGINAL;
    		list.addElement(configParametro);
    	}
    	if (LavenderePdaConfig.isLiberaSenhaDiaEntregaPedido() && !LavenderePdaConfig.getOcultaLiberacaoDiaEntregaPedido()) {
    		ValorParametro configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.LIBERASENHADIAENTREGAPEDIDO);
    		configParametro.cdParametro = ValorParametro.LIBERASENHADIAENTREGAPEDIDO;
    		configParametro.dsParametro = Messages.PARAMETRO_LIBERASENHADIAENTREGAPEDIDO;
    		list.addElement(configParametro);
    	}
    	if (LavenderePdaConfig.isBloqueiaFechamentoPedidoRentabilidadeMinima() && !LavenderePdaConfig.getOcultaLiberacaoPedidoRentabilidadeMinima()) {
    		ValorParametro configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.SEMENTESENHALIBERACAORENTABILIDADEMINIMA);
    		configParametro.cdParametro = ValorParametro.SEMENTESENHALIBERACAORENTABILIDADEMINIMA;
    		configParametro.dsParametro = Messages.PARAMETRO_LIBERASENHARENTABILIDADEMINIMA;
    		list.addElement(configParametro);
    	}
    	if (LavenderePdaConfig.liberaComSenhaSaldoBonificacaoExtrapolado && !LavenderePdaConfig.getOcultaLiberacaoPedidoSaldoBonifInsuficiente()) {
    		ValorParametro configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.SEMENTESENHASALDOBONIFICACAOEXTRAPOLADO);
    		configParametro.cdParametro = ValorParametro.SEMENTESENHASALDOBONIFICACAOEXTRAPOLADO;
    		configParametro.dsParametro = Messages.PARAMETRO_LIBERASENHABONIFICACAOSALDO;
    		list.addElement(configParametro);
    	}
    	if (LavenderePdaConfig.liberaSenhaVisitaClienteForaAgenda && !LavenderePdaConfig.getOcultaLiberacaoVisitaClienteForaAgenda()) {
    		ValorParametro configParametro = LavenderePdaConfig.getParametroPorRepresentante(ValorParametro.SEMENTESENHAVISITACLIENTEFORAAGENDA);
    		configParametro.cdParametro = ValorParametro.SEMENTESENHAVISITACLIENTEFORAAGENDA;
    		configParametro.dsParametro = Messages.SENHADINAMICA_LIBERACAO_VISITA_FORA_AGENDA;
    		list.addElement(configParametro);
    	}
    	return list;
    }
    
    public Vector getValorParametroLigadoList(ValorParametro valorParametroFilter) throws SQLException {
    	return ValorParametroPdbxDao.getInstance().getValorParametroLigadoList(valorParametroFilter);
    }
    
}