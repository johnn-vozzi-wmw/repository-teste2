package br.com.wmw.lavenderepda.business.domain;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.FieldMapper;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.dto.ItemNfeDTO;
import br.com.wmw.lavenderepda.business.domain.dto.ItemNfeReferenciaDTO;
import br.com.wmw.lavenderepda.business.domain.dto.NfeDTO;
import br.com.wmw.lavenderepda.business.service.StatusNfeService;
import totalcross.util.Date;
import totalcross.util.Vector;

public class Nfe extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPNFE";
	
	public static final String NMCOLUNA_VLNUNFE = "nuNfe";
	public static final int TIPO_EMISSAO = 1;
	public static final String EMISSAO_CONTINGENCIA = "5";
	public static final String EMISSAO_NORMAL = "1";
	public static final String CDMENSAGEMRETORNO_SUCESSO = "100";
	public static final String CDMENSAGEMRETORNO_ERRO = "-3";
	public static final String CDMENSAGEMRETORNO_CANCELAMENTO = "135";
	public static final String DSMENSAGEMRETORNO_CANCELAMENTO = "Evento Registrado e Vinculado à NF-e";

    public String cdEmpresa;
    public String cdRepresentante;
    public String nuPedido;
    public String flOrigemPedido;
    public String cdStatusNfe;
    public String dsNaturezaOperacao;
    public String vlChaveAcesso;
    public Date dtSolicitacao;
    public String dsSerieNfe;
    public Date dtResposta;
    public int nuLote;
    public String dsTipoEmissao;
    public String dsObservacao;
    public int nuNfe;
    public String cdTipoOperacaoNfe;
    public Date dtEmissao;
    public Date dtSaida;
    public String hrSaida;
    public double vlIbpt;
    public double vlTotalNfe;
    public double vlTotalIcms;
    public double vlTotalSt;
    public double vlTotalIpi;
    public double vlTotalFrete;
    public double vlTotalSeguro;
    public double vlTotalDesconto;
    public double vlTotalProdutosNfe;
    public String cdTransportadora;
    public String flAmbiente;
    public String cdMensagemRetorno;
    public String dsMensagemRetorno;
    public String cdCliente;
    public String cdTipoPedido;
    public String hrEmissao;
    public String hrResposta;
    public String nuProtocolo;
    public double vlTotalDescontoFin;
	public String dsMensagemNotaCredito;
    
    //Não persistente
    public Vector itemNfeList;
    public Transportadora transportadora;
    public int nuCarimboOrFilter;
    public String dsTipoEmissaoFiter;
    public boolean filtraPorNaoEnviadoServidor;
    public boolean filtraPorNaoCancelado;
    public String[] cdRepresentanteSupList;
    
    public Nfe() {
    	itemNfeList = new Vector(0);
    }
    
    public Nfe(NfeDTO nfeDTO) {
    	itemNfeList = new Vector(0);
    	try {
    		if (LavenderePdaConfig.usaNfePorReferencia) {
    			if (nfeDTO.itemNfeReferenciaList != null) {
    				for (ItemNfeReferenciaDTO itemNfeDTO : nfeDTO.itemNfeReferenciaList) {
    					itemNfeList.addElement(new ItemNfeReferencia(itemNfeDTO));
    				}
    			}
    		} else {
    			if (nfeDTO.itemNfeList != null) {
    				for (ItemNfeDTO itemNfeDTO : nfeDTO.itemNfeList) {
    					itemNfeList.addElement(new ItemNfe(itemNfeDTO));
    				}
    			}
    		}
    		FieldMapper.copy(nfeDTO, this);
    	} catch (Throwable e) {
			e.printStackTrace();
		}
    }
    
    public boolean equals(Object obj) {
        if (obj instanceof Nfe) {
        	Nfe nfe = (Nfe) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, nfe.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, nfe.cdRepresentante) &&
                ValueUtil.valueEquals(nuPedido, nfe.nuPedido) &&
            	ValueUtil.valueEquals(flOrigemPedido, nfe.flOrigemPedido);
        }
        return false;
    }

	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
		primaryKey.append(cdEmpresa);
		primaryKey.append(";");
		primaryKey.append(cdRepresentante);
		primaryKey.append(";");
		primaryKey.append(nuPedido);
		primaryKey.append(";");
		primaryKey.append(flOrigemPedido);
		return primaryKey.toString();
	}

	public String getStatusNfe() throws SQLException {
		StatusNfe statusNfeFilter = new StatusNfe();
		statusNfeFilter.cdEmpresa = cdEmpresa;
		statusNfeFilter.cdRepresentante = cdRepresentante;
		statusNfeFilter.cdStatusNfe = cdStatusNfe;
		StatusNfe statusNfe = (StatusNfe) StatusNfeService.getInstance().findByRowKey(statusNfeFilter.getRowKey());
		if (statusNfe != null) {
			return statusNfe.toString();
		}
		return StringUtil.getStringValue(cdStatusNfe);
	}

	public double getSomaVlTotalBaseIcmsItem() {
		double soma = 0;
		for (int i = 0; i < itemNfeList.size(); i++) {
			ItemNfe itemNfe = (ItemNfe) itemNfeList.items[i];
			if (LavenderePdaConfig.usaInfoAdicionalImpressaoNfeVendaCigarros && ValueUtil.VALOR_SIM.equals(itemNfe.flCigarro)) {
				continue;
			}
			soma += itemNfe.vlTotalBaseIcmsItem;
		}
		return soma;
	}

	public double getSomaVlTotalBaseStItem() {
		double soma = 0;
		for (int i = 0; i < itemNfeList.size(); i++) {
			ItemNfe itemNfe = (ItemNfe) itemNfeList.items[i];
			if (LavenderePdaConfig.usaInfoAdicionalImpressaoNfeVendaCigarros && ValueUtil.VALOR_SIM.equals(itemNfe.flCigarro)) {
				continue;
			}
			soma += itemNfe.vlTotalBaseStItem;
		}
		return soma;
	}

	public double getSomaVlDespesaAcessoria() {
		double soma = 0;
		for (int i = 0; i < itemNfeList.size(); i++) {
			ItemNfe itemNfe = (ItemNfe) itemNfeList.items[i];
			soma += itemNfe.vlDespesaAcessoria;
		}
		return soma;
	}

	public double getSomaQtItemFisicoCigarro() {
		double soma = 0;
		for (int i = 0; i < itemNfeList.size(); i++) {
			ItemNfe itemNfe = (ItemNfe) itemNfeList.items[i];
			if (ValueUtil.VALOR_SIM.equals(itemNfe.flCigarro)) {
				soma += itemNfe.qtItemFisico;
			}
		}
		return soma;
	}

	public double getVlTotalBaseIcmsItemCigarro() {
		double soma = 0;
		for (int i = 0; i < itemNfeList.size(); i++) {
			ItemNfe itemNfe = (ItemNfe) itemNfeList.items[i];
			if (ValueUtil.VALOR_SIM.equals(itemNfe.flCigarro)) {
				soma += itemNfe.vlTotalBaseIcmsItem;
			}
		}
		return soma;
	}

	public double getVlTotalBaseStItemCigarro() {
		double soma = 0;
		for (int i = 0; i < itemNfeList.size(); i++) {
			ItemNfe itemNfe = (ItemNfe) itemNfeList.items[i];
			if (ValueUtil.VALOR_SIM.equals(itemNfe.flCigarro)) {
				soma += itemNfe.vlTotalBaseStItem;
			}
		}
		return soma;
	}
	
	public double getVlTotalStItemCigarro() {
		double soma = 0;
		for (int i = 0; i < itemNfeList.size(); i++) {
			ItemNfe itemNfe = (ItemNfe) itemNfeList.items[i];
			if (ValueUtil.VALOR_SIM.equals(itemNfe.flCigarro)) {
				soma += itemNfe.vlTotalStItem;
			}
		}
		return soma;
	}
	
	public boolean isContingencia() {
		return ValueUtil.valueEquals(Nfe.EMISSAO_CONTINGENCIA, dsTipoEmissao);
	}
	
	public boolean isNormal() {
		return ValueUtil.valueEquals(Nfe.EMISSAO_NORMAL, dsTipoEmissao);
	}
	
}
