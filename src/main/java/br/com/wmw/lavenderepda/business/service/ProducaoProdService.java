package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigIntegWebTc;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ProducaoProd;
import br.com.wmw.lavenderepda.business.domain.ProducaoProdRep;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoErro;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProducaoProdDbxDao;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.json.JSONArray;
import totalcross.json.JSONObject;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ProducaoProdService extends CrudService {

    private static ProducaoProdService instance;
    
    private ProducaoProdService() {
        //--
    }
    
    public static ProducaoProdService getInstance() {
        if (instance == null) {
            instance = new ProducaoProdService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ProducaoProdDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    }
    
    public String getDadosProducaoProdFormatoJson(Vector producaoProdList) {
		JSONArray jSonProducaoProdList = new JSONArray();
		if (ValueUtil.isNotEmpty(producaoProdList)) {
			for (int i = 0; i < producaoProdList.size(); i++) {
				ProducaoProd producaoProd = (ProducaoProd) producaoProdList.items[i];
				JSONObject jSon = new JSONObject();
				jSon.put("cdEmpresa", producaoProd.cdEmpresa);
				jSon.put("cdRepresentante", producaoProd.cdRepresentante);
				jSon.put("cdProduto", producaoProd.cdProduto);
				jSon.put("dtInicialString", producaoProd.dtInicial);
				jSon.put("dtFinalString", producaoProd.dtFinal);
				jSon.put("qtdRateioProducao", producaoProd.qtdReserva);
				jSonProducaoProdList.put(jSon);
			}
		}
		return jSonProducaoProdList.toString();
	}

    @Override
    public Vector findAllByExample(BaseDomain domain) throws SQLException {
    	return ProducaoProdDbxDao.getInstance().findAllByExample((ProducaoProd) domain);
    }
     
    public Vector geraRateioProducaoProd(Vector producaoProdList) {
		try {
			String retorno = SyncManager.geraRateioProducaoProd(producaoProdList);
			recebeAtualizacaoProdutocaoProd();
			return getProducaoProdErroList(retorno);
		} catch (Throwable e) {
			throw new ValidationException(MessageUtil.getMessage(Messages.PRODUCAOPRODREP_MSG_VALIDACAO_ERRO_RATEIO_PRODUCAOPROD, e.getMessage()));
		}
	}
    
    private void recebeAtualizacaoProdutocaoProd() {
    	LoadingBoxWindow mb = null;
    	ConfigIntegWebTc configIntegWebTcFilter;
		configIntegWebTcFilter = new ConfigIntegWebTc();
		Vector web2SyncList = new Vector();
    	try {
    		mb = UiUtil.createProcessingMessage(Messages.PRODUCAOPRODREP_MSG_RECEBENDO_DADOS);
    		mb.popupNonBlocking();
    		LavendereWeb2Tc erpToPda = new LavendereWeb2Tc();
    		//--
    		configIntegWebTcFilter.dsTabela = ProducaoProd.TABLE_NAME;
			ConfigIntegWebTc webTcProducaoProd = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			web2SyncList.addElement(webTcProducaoProd);
			//--
			configIntegWebTcFilter.dsTabela = ProducaoProdRep.TABLE_NAME;
			ConfigIntegWebTc webTcProducaoProdRep = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			web2SyncList.addElement(webTcProducaoProdRep);
			//--
			configIntegWebTcFilter.dsTabela = Estoque.TABLE_NAME;
			ConfigIntegWebTc webTcEstoque = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			web2SyncList.addElement(webTcEstoque);
    		//--
			erpToPda.recebeDadosDisponiveisServidor(SyncManager.getInfoAtualizacaoByWeb2SyncList(web2SyncList));
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(ex.getMessage());
		} finally {
			mb.unpop();
		}
	}

	public Vector getProducaoProdErroList(String gSonProducaoProdErroList) throws SQLException {
		Vector producaoProdErroList = new Vector();
		if (ValueUtil.isNotEmpty(gSonProducaoProdErroList) && !gSonProducaoProdErroList.startsWith("ERRO") && !gSonProducaoProdErroList.startsWith("OK")) {
			JSONArray jsonProducaoProdArray = new JSONArray(gSonProducaoProdErroList);
			if (jsonProducaoProdArray != null) {
				for (int i = 0; i < jsonProducaoProdArray.length(); i++) {
					JSONObject jsonProducaoProd = (JSONObject) jsonProducaoProdArray.get(i);
					String cdProduto = jsonProducaoProd.getString("cdProduto");
					Produto produto = ProdutoService.getInstance().getProduto(cdProduto);
					int qtdDisponivel = jsonProducaoProd.getInt("qtdDisponivel");
					int qtdReserva = jsonProducaoProd.getInt("qtdRateioProducao");
					String msgErro = null;
					if (qtdReserva > 0) {
						msgErro = MessageUtil.getMessage(Messages.PRODUCAOPRODREP_MSG_ERRO_RATEIO, new Object[] {StringUtil.getStringValueToInterface(qtdDisponivel), StringUtil.getStringValueToInterface(qtdReserva)});
					} else if (qtdReserva == -1 || qtdReserva == -3) {
						msgErro = Messages.PRODUCAOPRODREP_MSG_ERRO_RATEIO_JAEXISTENTE;
					} else if (qtdReserva == -2) {
						msgErro = Messages.PRODUCAOPRODREP_MSG_ERRO_QTD_RATEIO_EDITADA_MENOR_ATUAL;
					}
					producaoProdErroList.addElement(new ProdutoErro(produto, "", msgErro));
				}
				return producaoProdErroList;
			}
		}
		return null;
	}
	
	public boolean isPermiteRealizarRateio(String dsPeriodo) {
		if (ValueUtil.isNotEmpty(dsPeriodo)) {
			Date dtFinal = DateUtil.getDateValue((StringUtil.split(dsPeriodo, '-'))[1].trim());
			return dtFinal.isAfter(DateUtil.getCurrentDate()) || dtFinal.equals(DateUtil.getCurrentDate());
		}
		return false;
	}
	
	public boolean isPeriodoVigente(Date dtComparacao, String dsPeriodo) {
		if (ValueUtil.isNotEmpty(dsPeriodo)) {
			Date dtInicial = DateUtil.getDateValue((StringUtil.split(dsPeriodo, '-'))[0].trim());
			Date dtFinal = DateUtil.getDateValue((StringUtil.split(dsPeriodo, '-'))[1].trim());
			return DateUtil.isChosenDtBetweenDates(dtInicial, dtFinal, dtComparacao);
		}
		return false;
	}

	public boolean hasProducaoProduto(ItemPedido itemPedido) throws SQLException {
		ProducaoProd producaoProd = new ProducaoProd();
		producaoProd.cdEmpresa = itemPedido.cdEmpresa;
		producaoProd.cdProduto = itemPedido.cdProduto;
		producaoProd.dtFilter = DateUtil.getCurrentDate();
		return ProducaoProdDbxDao.getInstance().hasProducaoProdVigente(producaoProd);
	}

	public void executaLimpezaEstoqueProducaoProd() throws SQLException {
		try {
			if (LavenderePdaConfig.isUsaRateioProducaoPorRepresentante()) {
				ProducaoProd producaoProdFilter = new ProducaoProd();
				producaoProdFilter.dtFinalFilter = DateUtil.getCurrentDate();
				producaoProdFilter.flEstoqueExcluido = ValueUtil.VALOR_NAO;
				Vector producaoProdList = ProducaoProdDbxDao.getInstance().findAllNaoVigentes(producaoProdFilter);
				int size = producaoProdList.size();
				for (int i = 0; i < size; i++) {
					ProducaoProd producaoProd = (ProducaoProd) producaoProdList.items[i];
					EstoqueService.getInstance().deleteByProducaoProd(producaoProd);
					producaoProd.flEstoqueExcluido = ValueUtil.VALOR_SIM;
					update(producaoProd);
				}
			}
		} catch (Throwable e) {
			if (!FrameworkMessages.MSG_ERRO_ATUALIZACAO.equalsIgnoreCase(e.getMessage())) {
				throw e;
			}
		}
	}
	
    public Vector findAllDistinctPeriodo() throws SQLException {
    	ProducaoProd producaoProdFilter = new ProducaoProd();
    	producaoProdFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	return ProducaoProdDbxDao.getInstance().findAllDistinctPeriodo(producaoProdFilter);
    }

}